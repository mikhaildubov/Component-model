package ru.hse.se.parsers;

import ru.hse.se.nodes.*;
import ru.hse.se.types.*;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
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
    protected ArrayList<Node> parseScene() throws SyntaxError, IOException {
        
        init(); // Initialization - to read the first token
                
        // !!! The entry point !!!
        parseStatements();
        // As a result - the filled resultingNodes array.
        
        return resultingNodes;
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
        
        return (parseProtoStatement()) ||
        
               (parseRouteStatement()) ||
               
               (parseNodeStatement() && addRootNode());
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
        
        return (lookahead("DEF") && match("DEF") && matchId() && parseNode()) ||
        
               (lookahead("USE") && match("USE") && matchId() && instantiateNodeById()) ||
                          
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
        
        return (lookahead("Script") && match("Script") &&
                    match("{") && parseScriptBody() && match("}")) ||
        
               (lookaheadIsId() && matchTypeId() && instantiateNode() &&
                    match("{") && parseNodeBody() && match("}"));
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
        
        return (parseRouteStatement()) ||
        
               (parseProtoStatement()) ||
                          
               (lookaheadIsId() && matchFieldId() &&
                       
                    (lookahead("IS") && match("IS") && matchId()
                            /* && ??? -> 3 productions!!! */) ||
                       
                    (matchFieldValueAndSetField()));
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
        
        return (lookahead("ROUTE") && match("ROUTE") /* && ...ToDo...*/);
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
    private void init() throws IOException {
        
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
        return (lookahead == null ? String.valueOf(numahead) : lookahead);
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
     * @return true, if matching is successful
     */
    private boolean matchId() {
        
        if(lookaheadIsId()) {
            
            currentId = lookahead;
            nextToken();
            
            return true;
            
        } else {
            
            throw new SyntaxError("'" + lookahead + "' is not a valid id",
                                                    tokenizer.lineno());
        }
    }
    
    /**
     * Reads the next token that represents a fieldId
     * (which is also an Id), and pushes it into the stack.
     */
    private boolean matchFieldId() {
        
        if(lookaheadIsId()) {
            
            currentField.push(lookahead);
            nextToken();
            
            return true;
            
        } else {
            
            throw new SyntaxError("'" + lookahead + "' is not a valid id",
                                                    tokenizer.lineno());
        }
    }
    
    /**
     * Reads the next token that represents a type
     * (which is also an Id).
     */
    private boolean matchTypeId() {
        
        if(lookaheadIsId()) {
            
            currentType = lookahead;
            nextToken();
            
            return true;
            
        } else {
            
            throw new SyntaxError("'" + lookahead + "' is not a valid id",
                                                    tokenizer.lineno());
        }
    }
    
    /**
     * Reads the next token from the input.
     */
    @Override
    public void nextToken() {
        
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
            } // No TT_NUMBER or TT_EOL can arise
        } catch (IOException e) {}
    }
        
    

    /*************************************************************
     *            Building up the JavaBeans components.          *
     *************************************************************/
    
    /**
     * Instantiates the next Node Bean by its type.
     */
    private boolean instantiateNode() {
        
        try {

            Node node = (Node)(Class.forName(nodesPackageName +
                                 "."+ currentType).newInstance());
            
            // If there was the "DEF" keyword
            if (currentId != null) {
                node.setId(currentId); 
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
     */
    private boolean instantiateNodeById() {
        
        Node node = defNodesTable.get(currentId);
        
        if (node != null) {
            
            currentNodes.push(node);
            
                                System.out.println("Instantiated existing Node" +
                                  (currentId == null ? "" : (" '"+currentId)+"'"));  
            
            currentId = null;

            return true;
        } else {

            throw new SyntaxError("Node named '" + currentId +
                    "' is not declared.", tokenizer.lineno());
        }
    }
    
    /**
     * Adds the parsed node into the resultingNodes array.
     */
    private boolean addRootNode() {
        
        resultingNodes.add(currentNodes.pop());
        
                            System.out.println("Added root node");
                            System.out.println();
        
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
            if (lookahead("NULL")) {
                
                match("NULL");
                value = null;
                
            } else {
                
                parseNodeStatement(); // involves currentNodes.push(...)
                value = currentNodes.pop(); // after parseNodeStatement the node is on the top
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
                                        + " field to value of type " +
                                        value.getClass().getName() +
                                        ": " + value.toString());
            
            currentField.pop();
            
            return true;
        } catch (Exception e) {
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
        resultingNodes = new ArrayList<Node>();
        defNodesTable = new HashMap<String, Node>();
        //protoNodesTable = new HashMap<String, Node>();
        currentNodes = new Stack<Node>();
        currentField = new Stack<String>();
        currentId = null;
    }
    
    /* The result of scene parsing */
    private ArrayList<Node> resultingNodes;
    
    /* 
     * Hash table that stores named (DEF) nodes
     * for their further use in USE statements.
     */
    private HashMap<String, Node> defNodesTable;
    //private HashMap<String, Node> protoNodesTable;
    
    /* current Token */
    protected String lookahead;
    private double numahead;
    
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
