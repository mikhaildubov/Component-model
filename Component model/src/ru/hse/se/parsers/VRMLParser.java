package ru.hse.se.parsers;

import ru.hse.se.nodes.*;
import ru.hse.se.parsers.errors.*;
import ru.hse.se.types.MFNode;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

/**
 * VRML parser. Builds up a bunch of beans
 * on the basis of its declarative description.
 * 
 * @author Mikhail Dubov
 */
public class VRMLParser extends Parser {

    /**
     * Sets up the tokenizer object
     * according to the VRML grammar.
     * (defines terminals etc.)
     */
    @Override
    protected void setUpTokenizer() {
        
        super.setUpTokenizer();
        
        tokenizer.commentChar('#');

        // Terminals
        tokenizer.ordinaryChar('{');
        tokenizer.ordinaryChar('}');
        tokenizer.ordinaryChar('[');
        tokenizer.ordinaryChar(']');

        //tokenizer.parseNumbers(); // => No! Bad for advanced float/int32 parsing
        tokenizer.lowerCaseMode(false); // VRML is not case-sensitive
        tokenizer.eolIsSignificant(false); // We can count lines with tokenizer.lineno()
    }
    

    /*************************************************************
     *    Recursive-descent predictive top-down VRML parser.     *
     *                                                           *
     *   See 2.4 in the "Dragon book" for technique description  *
     *         and http://bit.ly/wF541A for VRML grammar.        *
     *************************************************************/
    
    /************************************
     * vrmlScene ::=                    *
     *      statements                  *
     ************************************/
    @Override
    protected void parseScene() throws IOException {
        
        parseStatements();
        
        // Some token left unparsed
        if (lookahead != null) {
            registerError (new SyntaxError("Unrecognized lexeme sequence starting at '"
                                        + lookahead + "'", tokenizer.lineno()));  
        }
    }
    
    
    /*************************************************************
     *                    The basic grammar.                     *
     *************************************************************/
    
    /************************************
     * statements ::=                   *
     *        statement |               *
     *        statement statements |    *
     *        empty                     *
     ************************************/
    private boolean parseStatements() {
        
        while (parseStatement());
        
        return true;
    }
    
    /************************************
     * statement ::=                    *
     *       nodeStatement |            *
     *       protoStatement |           *
     *       routeStatement             *
     ************************************/
    private boolean parseStatement() {

        // ! NB: The order is essential, because of the FIRST elements
        
        return (lookahead != null) &&
        
               ((parseProtoStatement()) ||
        
               (parseRouteStatement()) ||
               
               (parseNodeStatement() && addRootNode()));
    }
    
    /**
     * Parses a node and stores the link
     * to that node on the top of the
     * 'currentNodes' stack.
     * 
     ************************************
     * nodeStatement ::=                *
     *     node |                       *
     *     DEF nodeNameId node |        *
     *     USE nodeNameId               *
     *                                  *
     * FIRST = nodeId | DEF | USE       *
     ************************************/
    private boolean parseNodeStatement() {
        
        // ! NB: The order is essential, because of the FIRST elements
        
        // In the "DEF" production, parseNode() will store
        // the node in the hash table by its id.
        
        return (tryMatch("DEF") && matchId() && parseNode()) ||
        
               (tryMatch("USE") && matchId() && instantiateNodeById()) ||
                          
               (parseNode());
    }
    
    /**
     * Parses the next Node from the input stream.
     * Needed for MFNode parsing.
     */
    @Override
    public Node parseChildNode() {
        if (parseNodeStatement()) {
            return currentNodes.pop();
        } else {
            return null;
        }
    }
    
    /************************************
     * node ::=                         *
     *     nodeTypeId { nodeBody } |    *
     *     Script { scriptBody }        *
     *                                  *
     * FIRST = Script | empty           *
     ************************************/
    private boolean parseNode() {
        
        // ! NB: The order is essential, because of the FIRST elements
        
        return (tryMatch("Script") &&
                    match("{") && parseScriptBody() && match("}")) ||
        
               (tryMatchTypeId() && instantiateNode() &&
                    match("{") && parseNodeBody() && match("}")) ||
                    
                // Handling a typical syntax error case, trying to recover
                // '}' is the only correct lexeme at this point
               (! lookahead("}") &&
                   registerError(possibleError) && panicModeRecovery() &&
                   (currentNodes.push(null) == null)); // pushing fake node
    }
    
    /************************************
     * nodeBody ::=                     *
     *     nodeBodyElement |            *
     *     nodeBodyElement nodeBody |   *
     *     empty                        *
     ************************************/
    private boolean parseNodeBody() {

        while (parseNodeBodyElement());
        
        return true;
    }
    
    /************************************
     * nodeBodyElement ::=              *
     *     fieldId fieldValue |         *
     *     fieldId IS fieldId |         *
     *     eventInId IS eventInId |     *
     *     eventOutId IS eventOutId |   *
     *     routeStatement |             *
     *     protoStatement               *
     ************************************/
    private boolean parseNodeBodyElement() {
        
        // Trying to parse routeStatement or protoStatement at first
        // (Since they contain terminals at their FIRST position).
        System.out.println("___"+lookahead);
        
        return (parseRouteStatement()) ||
        
               (parseProtoStatement()) ||
                          
               (tryMatchFieldId() &&
                       
                    ((tryMatch("IS") && matchId()
                            /* && ??? -> 3 productions!!! */) ||
                       
                    (matchFieldValueAndSetField()))) ||
                    
               // Handling a typical syntax error case, trying to recover
               // '}' is the only correct lexeme at this point
               (! lookahead("}") &&
                   registerError(possibleError) && panicModeRecovery());
    }
    
    
    

    /*************************************************************
     *                 PROTO & ROUTE statements.                 *
     *************************************************************/
    
    /************************************
     * protoStatement ::=               *
     *     proto |                      *
     *     externproto                  *
     *                                  *
     * FIRST = PROTO | EXTERNPROTO      *
     ************************************/
    private boolean parseProtoStatement() {
        
        return (lookahead("PROTO") && parseProto()) ||
        
               (lookahead("EXTERNPROTO") /* && ...ToDo...*/);
    }
    
    /************************************
     * proto ::=                        *
     *       PROTO nodeTypeId           *
     *       [ interfaceDeclarations ]  *
     *       { protoBody } ;            *
     ************************************/
    private boolean parseProto() {
        
        return false;//(match("PROTO") && matchTypeId() && instantiateProtoNode() &&
                // match("[") && parseProtoInterface() && match("]") &&
                // match("{") && parseProtoBody() && match("}"));
    }
    
    /************************************
     * routeStatement ::=               *
     *    ROUTE nodeNameId . eventOutId *
     *    TO nodeNameId . eventInId     *
     *                                  *
     * FIRST = ROUTE                    *
     ************************************/
    private boolean parseRouteStatement() {
        
        return (tryMatch("ROUTE") /* && ...ToDo...*/);
    }
    
    /************************************
     * scriptBody ::=                   *
     *   scriptBodyElement |            *
     *   scriptBodyElement scriptBody | *
     *   empty                          *
     ************************************/
    private boolean parseScriptBody() {
        
        // ToDo
        
        return false;
    }
    
    
    

    /*************************************************************
     *                     Token operaions.                      *
     *************************************************************/
    
    /**
     * Initializes the parser by reading the first
     * token and storing it in the lookahead variable.
     */
    protected void init() throws IOException {
        
        initFields();
        
        nextToken();
                
        if (lookahead == null) {
            throw new IOException();
        }
    }
    
    /**
     * Returns the lookahead token.
     */
    @Override
    public String lookahead() {
        return (lookahead == null ? "" : lookahead);
    }
    
    /**
     * Checks a token for being a valid Id.
     * nodeNameId, nodeTypeId, fieldId, eventInId, eventOutId
     * are all id's.
     * 
     *****************************************************************
     * Id ::=                                                        *
     *     IdFirstChar |                                             *
     *     IdFirstChar IdRestChars                                   *
     * IdFirstChar ::=                                               *
     *     Any ISO-10646 character encoded using UTF-8 except:       *
     *     0x30-0x39, 0x0-0x20, 0x22, 0x23, 0x27, 0x2b, 0x2c, 0x2d,  *
     *     0x2e, 0x5b, 0x5c, 0x5d, 0x7b, 0x7d, 0x7f                  *
     * IdRestChars ::=                                               *
     *     Any number of ISO-10646 characters except:                *
     *     0x0-0x20, 0x22, 0x23, 0x27, 0x2c, 0x2e, 0x5b,             *
     *     0x5c, 0x5d, 0x7b, 0x7d, 0x7f                              *
     *****************************************************************
     * @param id
     * @return true if the token is a correct id, false otherwise
     */
    private boolean lookaheadIsId() {
        
        // TODO: More Id checking (see rules)
        
        return (lookahead != null) && (lookahead != "") &&
               (Character.isLetter(lookahead.charAt(0)) || lookahead.charAt(0) == '_');
    }
    
    /**
     * Reads the next token that represents an Id.
     *
     * @return true
     */
    private boolean matchId() {

        if(lookaheadIsId()) {
            
            currentId = lookahead;
            nextToken();
            
        } else {
            
            registerError(new SyntaxError("'" + lookahead + "' is not a valid id",
                                                    tokenizer.lineno()));
        }
        
        return true;
    }
    
    /**
     * Determines whether the current token is
     * a field name of the current node.
     * @return true, if the current token is a field, false otherwise
     */
    private boolean lookaheadIsFieldName() {
        
        if (currentNodes.isEmpty()) {
            return false;
        }

        boolean isFieldName = false;
        
        Method[] methods = currentNodes.peek().getClass().getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().startsWith("get")) {
                String field = Character.toLowerCase(m.getName().charAt(3)) + 
                                                     m.getName().substring(4);
                if (field.equals(lookahead)) {
                    isFieldName = true;
                    break;
                }
            }
        }
        
        return isFieldName;
    }
    
    /**
     * Returns the hash set of the current node fields,
     * used for lexical error diagnostics.
     * 
     * @return HashSet of the current node fields
     */
    private HashSet<String> getCurrentNodeFields() {
        
        if (currentNodes.isEmpty()) {
            return null;
        }

        HashSet<String> res = new HashSet<String>();
        
        Method[] methods = currentNodes.peek().getClass().getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().startsWith("get")) {
                String field = Character.toLowerCase(m.getName().charAt(3)) + 
                                                     m.getName().substring(4);
                res.add(field);
            }
        }
        
        return res;
    }
    
    /**
     * Reads the next token that represents a fieldId
     * (which is also an Id), and pushes it into the stack.
     */
    private boolean tryMatchFieldId() {
        
        if(lookaheadIsFieldName()) {
            
            currentField.push(lookahead);
            nextToken();
            
            return true;
            
        } else {

            // There is no field with the given name
            
            possibleError = new LexicalError("'" + lookahead +
                                "' is not a valid field name",
                                tokenizer.lineno(), lookahead,
                                getCurrentNodeFields());
            return false;
        }
    }
    
    /**
     * Reads the next token that represents a type
     * (which is also an Id).
     * @return true, if lookahead is a valid node name, false otherwise
     */
    private boolean tryMatchTypeId() {
        System.out.println("!!!"+lookahead);
        Class<?> nodeType = classForNodeName(lookahead);
        if (nodeType != null) {
            
            currentType = lookahead;
            nextToken();
            
            // There is a node with the given name,
            // but it should be checked for type matching
            try {
                Class<?> fieldType = currentNodes.peek().getClass().
                        getDeclaredMethod("get" + Character.toUpperCase
                        (currentField.peek().charAt(0)) +
                         currentField.peek().substring(1)).getReturnType();

                if (! fieldType.isAssignableFrom(nodeType) &&
                    ! fieldType.isAssignableFrom(MFNode.class)) {
                    possibleError = new TypeMismatchError
                            (nodeType, fieldType, tokenizer.lineno());
                    currentId = null; // to preserve invalid DEF assignments
                    
                    return false;
                }
            } catch (Exception e) { }
            System.out.println("???"+lookahead);
            
            return true;
            
        } else {
            
            // There is no node with the given name
            
            possibleError = new LexicalError("'" + lookahead +
                                "' is not a valid node name",
                                tokenizer.lineno(), lookahead, null);
            currentId = null; // to preserve invalid DEF assignments
            
            return false;
        }
    }
    
    /**
     * Reads the next token from the input.
     * @return false when the next token is unavailable, true otherwise
     */
    @Override
    public boolean nextToken() {
        
        try {
            int ttype = tokenizer.nextToken();
            
            if (ttype == '{' || ttype == '}' ||
                ttype == '[' || ttype == ']') {
                // Terminals
                lookahead = String.valueOf(((char)tokenizer.ttype));
            } else if (ttype == StreamTokenizer.TT_WORD) {
                // Non-terminals
                lookahead = tokenizer.sval;
            } else if (ttype == '"') {
                // Quoted Strings
                lookahead = tokenizer.sval;
            } else if (ttype == StreamTokenizer.TT_EOF) {
                // End of file
                lookahead = null; // to indicate EOF
                return false;
            } // No TT_NUMBER or TT_EOL can arise
        } catch (IOException e) {
            return false;
        }
        
        return true;
    }

    
    
    /*************************************************************
     *                      Error recovery.                      *
     *************************************************************/
    
    /**
     * Tries to recover from errors in order
     * for the parser ro be able to continue
     * reading the input stream.
     * 
     * See 4.1 in the "Dragon book" for technique description.
     * 
     * @return true, if recovery proceeded, false otherwise
     */
    private boolean panicModeRecovery() {
        
        // The error is an invalid node name
        // or its absence.
        
        // Recovery possibility - if the current
        // or the next tokens is an opening parenthese.
        while (! lookahead("{") && // for Nodes
               ! lookahead("}") && // for ValueTypes
                lookahead != null) {
            nextToken();
        }
        
        // Trying to recover by reading parentheses
        // until the end of the "damaged" Node is reached
        if (lookahead("{")) {
            int parentheses = 1;
            while (nextToken()) {
                if (lookahead("{")) {
                    parentheses++;
                } else if (lookahead("}")) {
                    parentheses--;
                }
                if (parentheses == 0) {
                    nextToken();
                    return true;
                }
            }
        }
        
        // ValueType
        if (lookahead("}")) {
            return true;
        }
        
        return false;
    }
        
    

    /*************************************************************
     *            Building up the JavaBeans components.          *
     *************************************************************/
    
    /**
     * Instantiates the next Node Bean by its type.
     */
    private boolean instantiateNode() {
        
        try {

            // Uses REFLECTION
            Node node = createInstance(currentType);
            
            // If there was the "DEF" keyword
            if (currentId != null) {
                
                node.setId(currentId); 
                
                // Warning if the named node is already defined
                if (defNodesTable.containsKey(currentId)) {
                    registerError(new Warning("Node named '" + currentId +
                          "' is already defined", tokenizer.lineno()));
                }
                
                // Store the node in the hash table
                defNodesTable.put(currentId, node); 
            }
            
            // Pushing the node into the stack
            currentNodes.push(node);
            
                                System.out.println("Instantiated Node" +
                                        (currentId == null ? "" : (" '"+currentId)+"'")
                                        + " of type " + currentType);  

            currentId = null;
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Searches for an existing Node Bean in the hash table
     * by its id and, if found, acts like instantiateNode().
     * 
     * @return true
     */
    private boolean instantiateNodeById() {
        
        Node node = defNodesTable.get(currentId);
        
        // can be null
        currentNodes.push(node);
        
        if (node == null) {
            
            registerError(new LexicalError("Node named '" + currentId +
                    "' is not declared.", tokenizer.lineno(), currentId, 
                    new HashSet<String>(defNodesTable.keySet())));
            
        } else {

            System.out.println("Instantiated existing Node" +
              (currentId == null ? "" : (" '"+currentId)+"'"));             
        }       

        currentId = null;

        return true;
    }
    
    /**
     * Adds the parsed node into the sceneGraph array.
     */
    private boolean addRootNode() {
        
                            System.out.print("Added root node ");
                            System.out.println(currentNodes.peek() == null ? "null" :
                                      currentNodes.peek().getClass().getSimpleName());
                            System.out.println();
        
        sceneGraph.add(currentNodes.pop());
        
        return true;        
    }

    /**
     * Gets the value of the next field and stores it
     * in the appropriate Bean. Works both for value types
     * and for Node types (recursively).
     */
    private boolean matchFieldValueAndSetField() {
        
        // ! NB: Here, it was NOT the grammar which gave the information
        //       on the type of the field. To retrieve it, reflection was used.
        
        Object value = null;
        Class<?> currentFieldType;
        
        try {
            currentFieldType = currentNodes.peek().getClass().getDeclaredMethod("get" +
                    Character.toUpperCase(currentField.peek().charAt(0)) +
                    currentField.peek().substring(1)).getReturnType();
        } catch (Exception e) {
            return false;
        }
        
        /****** a) Node type => recursive call of the appropriate parser Methods ******/
        if (Node.class.isAssignableFrom(currentFieldType)) {
            
            /************************
             * sfnodeValue ::=      *
             *     nodeStatement |  *
             *     NULL             *
             ************************/
            if (tryMatch("NULL")) {
                
                value = null;
                
            } else {
                System.out.println("---"+lookahead);
                // involves currentNodes.push(...)
                parseNodeStatement(); 

                // after parseNodeStatement the node is on the top
                value = currentNodes.pop(); 
            }
        }
        
        /****** b) Value type => call "parse" method in the type class via reflection ******/
        else {
            // Implementation - in the superclass.
            value = parseValueType(currentFieldType);
        }

        /****** Invoking setXxx(value) ******/
        try {
            currentNodes.peek().getClass().getDeclaredMethod("set" +
                Character.toUpperCase(currentField.peek().charAt(0)) +
                currentField.peek().substring(1),
                new Class[] {currentFieldType}).
                invoke(currentNodes.peek(), value);

                                    System.out.println("    Set the " +
                                        currentField.peek()
                                        + " field to value " +
                                        ((value == null) ? "null" : ("of type " +
                                        value.getClass().getName() +
                                        ": " + value.toString())));
            currentField.pop();
            
            return true;
            
        } catch (Exception e) {
            System.out.println();
            return false;
        }
    }

    
    
    /*************************************************************
     *                   (Private fields).                       *
     *************************************************************/
    
    /**
     * Initializes the private fields before the parser starts.
     */
    private void initFields() {
        defNodesTable = new HashMap<String, Node>();
        //protoNodesTable = new HashMap<String, Node>();
        currentNodes = new Stack<Node>();
        currentField = new Stack<String>();
        currentId = null;
    }
    
    /* 
     * Hash table that stores named (DEF) nodes
     * for their further use in USE statements.
     */
    private HashMap<String, Node> defNodesTable;
    //private HashMap<String, Node> protoNodesTable;
    
    /* current Token */
    protected String lookahead;
    
    /* current Node id */
    private String currentId;
    /* current Node type */
    private String currentType;
    
    // ! NB: The nested structure of VRML nodes requires
    //       maintaining of two stacks: for field names
    //       and for the appropriate nodes (if needed).
    
    /* Field stack */
    private Stack<String> currentField;
    /* Node stack */
    private Stack<Node> currentNodes;
}
