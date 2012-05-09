package ru.hse.se.parsers;

import ru.hse.se.nodes.Node;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.Stack;

/**
 * XML parser. Builds up a bunch of beans
 * on the basis of its declarative description.
 * 
 * @author Mikhail Dubov
 */
public class X3DParser extends Parser {

    /**
     * Sets up the tokenizer object
     * according to the XML grammar.
     * (defines terminals etc.)
     */
    @Override
    protected void setUpTokenizer() {
        
        super.setUpTokenizer();
        
        // TODO: comments??

        // Terminals
        tokenizer.ordinaryChar('<');
        tokenizer.ordinaryChar('>');
        tokenizer.ordinaryChar('/');
        tokenizer.ordinaryChar('=');
        tokenizer.ordinaryChar('\''); // Reading attributes manually

        //tokenizer.parseNumbers(); // => No! Bad for advanced float/int32 parsing
        tokenizer.lowerCaseMode(false); // X3D is case-sensitive
        tokenizer.eolIsSignificant(false); // We can count lines with tokenizer.lineno()
    }

    
    /*************************************************************
     *       XML parser built according to the SAX approach.     *
     *             (that is, an event-driven parser)             *
     *************************************************************/
    
    /**
     * Performs the parsing of the input file
     * and returns an ArrayList of root nodes.
     */
    @Override
    protected void parseScene() throws IOException {

        parseXML();
    }
    
    /**
     * The main parsing routine that
     * goes through the XML file
     * and reports events, such as
     * opening tag, closing tag etc.
     */
    private void parseXML() {
        
        // Works like a DFA.
        
        while (lookahead != null) {
            
            // 1. Opening or closing tag starts
            if (tryMatch("<")) {

                readingTag = true;
                
                if (lookahead("/")) {
                    match("/");
                    if (currentTags.isEmpty()) {
                        error(new SyntaxError ("Closing tag + '" + lookahead
                                + "' does not match any opening tag.",
                                tokenizer.lineno()));
                    } else {
                        String openingTag = currentTags.pop();
                        
                        if (! lookahead(openingTag)) {
                            error(new SyntaxError ("Closing tag + '" + lookahead
                                              + "' does not match the opening tag + '"
                                              + openingTag + "'.", tokenizer.lineno()));
                        } else {
                            closingTag(lookahead);
                        }
                    }
                    
                    nextToken();
                    match(">");
                    readingTag = false;
                    
                } else {
                    currentTags.push(lookahead);
                    openingTag(lookahead);
                    
                    nextToken();
                }
            }
            // 2. Opening tag ends
            else if (tryMatch(">")) {
                
                readingTag = false;
            }
            // 3. Opening tag is closed at once
            else if (tryMatch("/")) {

                match(">");
                
                readingTag = false;
                closingTag(currentTags.pop());
            }
            // 4. Attribute
            else if (readingTag) {
                matchAttributeId();
                match("=");
                
                attribute(currentAttribute);
            }
            // 5. Text node
            else {
                StringBuilder value = new StringBuilder();
                do {
                    value.append(lookahead);
                    value.append(" ");
                    nextToken();
                } while(! lookahead("<"));
                textNode(value.toString());
            }
        }
    }
    
    /**
     * Parses the next Node from the input stream.
     * Needed for MFNode parsing.
     */
    @Override
    public Node parseChildNode() {
        return null;
    }
    
    
    /*************************************************************
     *                           Events.                         *
     *************************************************************/
    
    /**
     * Called whenever the parser meets an opening tag.
     * 
     * @param name tag name
     */
    private void openingTag(String name) {
        
                            System.out.println("Opening: " + name);
                            
        if (name.equals("X3D") || name.equals("Scene")) {
            
            // X3D and Scene are simply
            // root nodes with no functionality
            return;
        }

        // Nested nodes; not value types

        try {

            Node currentNode = (Node)(Class.forName(nodesPackageName +
                                "."+ name).newInstance());
            
            if (! currentNodes.isEmpty()) {
                
                // Child node is some field of the parent node.
                // To determine which field is to be set,
                // we use the containterField property.
                
                Node parentNode = currentNodes.peek();
                String field = currentNode.containerField();
                
                Class<?> currentFieldType = parentNode.getClass().
                        getDeclaredMethod("get" +
                        Character.toUpperCase(field.charAt(0)) +
                        field.substring(1)).getReturnType();
                
                /****** Invoking setXxx(value) ******/
                parentNode.getClass().getDeclaredMethod("set" +
                    Character.toUpperCase(field.charAt(0)) + field.substring(1),
                    new Class[] {currentFieldType}).
                    invoke(currentNodes.peek(), currentNode);
                
            }
            
            currentNodes.push(currentNode);
            
        } catch (Exception e) {
            error(new Error("Could not instantiate node " + name));
        }
    }
    
    /**
     * Called whenever the parser meets a closing tag.
     * 
     * @param name tag name
     */
    private void closingTag(String name) {
        
                            System.out.println("Closing: " + name);
                            
        if (name.equals("X3D") || name.equals("Scene")) {
            
            // X3D and Scene are simply
            // root nodes with no functionality
            return;
        }
        
        Node closed = currentNodes.pop();
        
        // Adds a root node to the sceneGraph array
        if (currentNodes.isEmpty()) {
            sceneGraph.add(closed);
        }
    }
    
    /**
     * Called whenever the parser meets an attribute
     * inside the opening tag.
     * 
     * @param name attribute name
     */
    private void attribute(String name) {
        
                            System.out.println("Attribute: " + name);

        match("'");
        
        // DEF keyword
        if (name.equals("DEF")) {
            
            Node currentNode = currentNodes.peek();
            currentNode.setId(lookahead);
            
            defNodesTable.put(lookahead, currentNode);
            
            nextToken();
        }
        // USE keyword
        else if (name.equals("USE")) {
            
            // The just instantiated Node was a "fake node"
            currentNodes.pop();
            
            // Get the Node from the hash table
            Node node = defNodesTable.get(lookahead);
            
            if (node != null) {
                if (! currentNodes.isEmpty()) {
                    
                    
                    // Child node is some field of the parent node.
                    // To determine which field is to be set,
                    // we use the containterField property.
                    
                    Node parentNode = currentNodes.peek();
                    String field = node.containerField();
                    
                    try {
                        
                        Class<?> currentFieldType = parentNode.getClass().
                                getDeclaredMethod("get" +
                                Character.toUpperCase(field.charAt(0)) +
                                field.substring(1)).getReturnType();
                        
                        /****** Invoking setXxx(value) ******/
                        parentNode.getClass().getDeclaredMethod("set" +
                            Character.toUpperCase(field.charAt(0)) + field.substring(1),
                            new Class[] {currentFieldType}).
                            invoke(currentNodes.peek(), node);
                    } catch (Exception e) {
                        
                        error(new Error("Could not use node " + lookahead));
                    }
                }
                
                currentNodes.push(node);
                
            } else {

                error(new SyntaxError("Node named '" + lookahead +
                        "' is not declared.", tokenizer.lineno()));
            }
            
            nextToken();
        }
        // Fields (value types, NOT nested nodes)
        else {
            
            Node currentNode = currentNodes.peek();
            Class<?> currentFieldType;
            
            try {
                /****** Getting the field type ******/
                currentFieldType = currentNode.getClass().
                        getDeclaredMethod("get" +
                    Character.toUpperCase(name.charAt(0)) +
                    name.substring(1)).getReturnType();

                Object attrValue = parseValueType(currentFieldType);
                
                /****** Invoking setXxx(value) ******/
                currentNode.getClass().getDeclaredMethod("set" +
                    Character.toUpperCase(name.charAt(0)) + name.substring(1),
                    new Class[] {currentFieldType}).
                    invoke(currentNode, attrValue);
    
            } catch (Exception e) {
                System.out.print(e.getMessage());
                error(new Error("Could not set the value of field " + name));
            }
        }

        match("'");
    }
    
    /**
     * Called whenever the parser meets a text node.
     * 
     * @param value text
     */
    private void textNode(String value) {
        
                            System.out.println("Text node: " + value);
        
        error(new SyntaxError("No text nodes allowed in X3D format",
                                                      tokenizer.lineno()));
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
    public String lookahead() {
        return lookahead;
    }
    
    /**
     * Checks a token for being a valid Id.
     * 
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
    private boolean matchAttributeId() {
        
        if(lookaheadIsId()) {
            
            currentAttribute = lookahead;
            nextToken();
            
        } else {
            
            error(new SyntaxError("'" + lookahead + "' is not a valid id",
                                                            tokenizer.lineno()));
        }
        
        return true;
    }

    
    /*************************************************************
     *                   Token operations.                       *
     *************************************************************/
    
    /**
     * Reads the next token from the input.
     * @returns false when the next token is unavailable, true otherwise
     */
    @Override
    public boolean nextToken() {
        
        try {
            int ttype = tokenizer.nextToken();
            
            if (ttype == '<' || ttype == '>' ||
                ttype == '/' || ttype == '=' ||
                ttype == '\'') {
                // Terminals
                lookahead = String.valueOf(((char)tokenizer.ttype));
            } else if (ttype == StreamTokenizer.TT_WORD) {
                // Non-terminals
                lookahead = tokenizer.sval;
            } else if (ttype == '"') {
                // Quoted Strings
                lookahead = tokenizer.sval;
            } else if (ttype == StreamTokenizer.TT_EOF) {
                lookahead = null; // to indicate EOF
                return false;
            } // No TT_NUMBER or TT_EOL can arise
        } catch (IOException e) {
            return false;
        }
        
        return true;
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
        currentTags = new Stack<String>();
        readingTag = false;
    }
    
    /* 
     * Hash table that stores named (DEF) nodes
     * for their further use in USE statements.
     */
    private HashMap<String, Node> defNodesTable;
    //private HashMap<String, Node> protoNodesTable;
    
    /* current Token */
    private String lookahead;
    
    /** Determines whether we are inside a tag */
    private boolean readingTag;
    
    /* current Attribute id */
    private String currentAttribute;
    
    // ! NB: The nested structure of XML nodes requires
    //       maintaining of a tag stacks: for tag names
    //       and for the appropriate nodes (if needed).
    
    /* Tag stack */
    private Stack<String> currentTags;
    /* Node stack */
    private Stack<Node> currentNodes;
}
