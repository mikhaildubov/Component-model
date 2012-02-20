package ru.hse.se.parsers;

import ru.hse.se.nodes.*;
import ru.hse.se.types.*;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Stack;

/**
 * VRML parser. Builds up a bunch of beans
 * on the basis of the source code.
 * 
 * @author Mikhail Dubov
 */
public class VRMLParser extends Parser {

    @Override
    protected void setUpTokenizer() {
        tokenizer.commentChar('#');
        tokenizer.quoteChar('"');
        
        tokenizer.ordinaryChar('{');
        tokenizer.ordinaryChar('}');
        tokenizer.ordinaryChar('[');
        tokenizer.ordinaryChar(']');
        
        tokenizer.wordChars('_', '_');
        
        //tokenizer.whitespaceChars(' ', ' ');

        tokenizer.parseNumbers();
        tokenizer.lowerCaseMode(false); // VRML is not case-sensitive
        tokenizer.eolIsSignificant(false);
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
    protected ArrayList<Node> parseScene() {
        
        resultingNodes = new ArrayList<Node>();  
        syntaxError = false;
        currentNodes = new Stack<Node>();
        currentField = new Stack<String>();
        currentId = null;
        
        if (init()) { // Initialization - to read the first token
        	
        	// !!! The entry point !!!
        	
        	// This changes the resultingNodes array
        	// and also the syntaxError flag.
        	parseStatements();
        	
        } else {
        	// => Something bad with the file (?)
        	return null;
        }
        
        if (syntaxError) {
            // TODO: Report syntax error
            return null;
        }

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
    
    /************************************
     * nodeStatement ::=                *
     *     node |                       *
     *     DEF nodeNameId node |        *
     *     USE nodeNameId               *
     *                                  *
     * FIRST = nodeId | DEF | USE       *
     ************************************/
    private boolean parseNodeStatement() {
        
    	// ! NB: The order is essential, because of the FIRST elements
    	
        return (match("DEF") && getId() && parseNode() /*...&& store to the table...*/) ||
        
               (match("USE") /* && ...Later...*/) ||
                          
               (parseNode());
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
    	
        return (match("Script") &&
                    match("{") && parseScriptBody() && match("}")) ||
        
               (getType() && instantiateNode() &&
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
                          
               (getFieldId() &&
                       
                    (match("IS") && getId() /* && ??? -> 3 productions!!! */) ||
                       
                    (getValueAndSetField()));
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
        
        return (match("PROTO") /* && ...Later...*/) ||
        
               (match("EXTERNPROTO") /* && ...Later...*/);
    }
    
    /************************************
     * routeStatement ::=               *
     *    ROUTE nodeNameId . eventOutId *
     *    TO nodeNameId . eventInId     *
     *                                  *
     * FIRST = ROUTE                    *
     ************************************/
    private boolean parseRouteStatement() {
        
        return (match("ROUTE") /* && ...Later...*/);
    }
    
    /************************************
     * scriptBody ::=                   *
     *   scriptBodyElement |            *
     *   scriptBodyElement scriptBody | *
     *   empty                          *
     ************************************/
    private boolean parseScriptBody() {
        
        // Later
        
        return false;
    }
    
    
    

    /*************************************************************
     *                     Token operaions.                      *
     *************************************************************/
    
    
    /** Initializes the parser by reading the first
     * token and storing it in the lookahead variable.
     * @return
     */
    private boolean init() {
        
        nextToken();
                
        return (lookahead != null);
    }
    
    /**
     * Compares the token with the lookahead symbol and
     * advances to the next input terminal if they match.
     * 
     * Note: VRML is case sensitive.
     * 
     * @param token Token to be matched
     * @return true if token is matched and the next token is read,
     *         false otherwise
     */
    private boolean match(String token) {
        
        if(token.equals(lookahead)) {

            nextToken();
            
            return true;
        }
        
        return false; // (reason: syntax error or wrong alternative)
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
     * @return
     */
    private boolean checkId(String id) {
    	
    	// TODO: More Id checking (see rules)
    	
        return (id != null) && (id != "") &&
                   Character.isLetter(id.charAt(0));
    }
    
    /**
     * Reads the next token that represents an Id.
     *
     * @return the Id symbol
     */
    private boolean getId() {
    	
    	boolean success = checkId(lookahead);
       
    	if (success) {
    		
    		currentId = lookahead;
        	nextToken();
    	}
    	
        return success;
    }
    
    /**
     * Reads the next token that represents a fieldId
     * (which is also an Id), and pushes it into the stack.
     */
    private boolean getFieldId() {
        
        boolean success = checkId(lookahead);
        
        if (success) {
            currentField.push(lookahead);
            nextToken();
        }
        
        return success;
    }
    
    /**
     * Reads the next token that represents a type
     * (which is also an Id).
     */
    private boolean getType() {
        
    	boolean success = checkId(lookahead);
        
    	if (success) {
    		
    		currentType = lookahead;
        	nextToken();
    	}
    	
        return success;
    }
    
    /**
     * Reads the next token from the input.
     */
    private void nextToken() {
        
        try {
            int ttype = tokenizer.nextToken();
            
            if (ttype == '{' || ttype == '}' || ttype == '[' || ttype == ']') {
                lookahead = String.valueOf(((char)tokenizer.ttype));
            } else if (ttype == StreamTokenizer.TT_WORD) {
                lookahead = tokenizer.sval;
            } else if (ttype == StreamTokenizer.TT_NUMBER) {
                numahead = tokenizer.nval;
                lookahead = null;
            }
        } catch (IOException e) {}
    }
    
    
    

    /*************************************************************
     *            Building up the JavaBeans components.          *
     *************************************************************/
    
    
    private static final String nodesPackageName = "ru.hse.se.nodes";
    
    /**
     * Instantiates the next Node Bean by its type.
     */
    private boolean instantiateNode() {
        
        try {

            Node node = (Node)(Class.forName(nodesPackageName +
                                 "."+ currentType).newInstance());
            
            if (currentId != null) {
                node.setId(currentId); // If there was the "DEF" keyword
            }
            
            currentNodes.push(node);
            
System.out.println("Instantiated Node" + (currentId == null ? "" : (" '"+currentId)+"'") + " of type " + currentType);  

            currentId = null;
            
            return true;
            
        } catch (Exception e) {
            return false;
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
     * in the appropriate Bean. Works both for primitive types
     * and for Node types (recursively).
     */
    private boolean getValueAndSetField() {
        
        // ! NB: Here, it was NOT the grammar which gave the information
        //       on the type of the field. To retrieve it, reflection was used.
        
        VRMLType value = null;
        Class currentFieldType;
        
        try {
            currentFieldType = currentNodes.peek().getClass().getDeclaredMethod("get" +
                    Character.toUpperCase(currentField.peek().charAt(0)) +
                    currentField.peek().substring(1)).getReturnType();
        } catch (Exception e) {
        	return false;
        }
        
        if (Node.class.isAssignableFrom(currentFieldType)) {
            
            /************************
             * sfnodeValue ::=      *
             *     nodeStatement |  *
             *     NULL             *
             ************************/
            if (match("NULL")) {
            	
            	value = null;
            	
            } else {
            	
            	parseNodeStatement(); // involves currentNodes.push(...)
                value = currentNodes.pop();
            }
            
        } else if (currentFieldType == SFBool.class) { 
            
            value = parseSFBool();            
            
        } else if (currentFieldType == SFColor.class) {  
            
            value = parseSFColor();
        }

        try {
        	// Invoking setXxx(value)
            currentNodes.peek().getClass().getDeclaredMethod("set" +
                Character.toUpperCase(currentField.peek().charAt(0)) +
                currentField.peek().substring(1), new Class[] {currentFieldType}).
                invoke(currentNodes.peek(), value);
            
System.out.println("    Set the " + currentField.peek() + " field to value of type " + value.getClass().getName() + ": " + value.toString());
            
            currentField.pop();
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    

    /*************************************************************
     *                   Primitive VRML types.                   *
     *************************************************************/
    
    
    /**
     * Parses a SFBool value from the stream.
     */
    private SFBool parseSFBool() {
        
        SFBool res = new SFBool();
        
        if (match("TRUE")) {
            res.value = true;
        } else if (match("FALSE")) {
            res.value = false;
        } else {
            res = null;
            syntaxError = true;
        }
        
        return res;
    }
    
    /**
     * Reads a SFColor value from the stream.
     */
    private SFColor parseSFColor() {
    	
        SFColor res = new SFColor();
        
        res.r = parseFloat();
        res.g = parseFloat();
        res.b = parseFloat();
        
        return res;
    }
    
    /**
     * Reads a Float value from the stream.
     * Stores the value as a value of type double.
     */
    private double parseFloat() {
        // TODO: Type checking (check that it was really float, but not a string)
        
        double res = numahead;
        
        nextToken();
        
        return res;
    }
    
    

    /*************************************************************
     *                   (Private fields).                       *
     *************************************************************/
    
    
    /* The result of scene parsing */
    private ArrayList<Node> resultingNodes;
    
    /* Determines if there was a syntax error */
    private boolean syntaxError;
    
    /* current Token */
    private String lookahead;
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