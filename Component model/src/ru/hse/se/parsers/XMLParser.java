package ru.hse.se.parsers;

import ru.hse.se.nodes.Node;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * XML parser. Builds up a bunch of beans
 * on the basis of its declarative description.
 * 
 * @author Mikhail Dubov
 */
public class XMLParser extends Parser {

    /**
     * Sets up the tokenizer object
     * according to the XML grammar.
     * (defines terminals etc.)
     */
    @Override
    protected void setUpTokenizer() {
        
        tokenizer.resetSyntax();
        
        tokenizer.wordChars('a', 'z'); // Id's
        tokenizer.wordChars('A', 'Z'); // Id's
        tokenizer.wordChars('0', '9'); // Id's
        tokenizer.wordChars('_', '_'); // Id's can contain '_'
        
        // TODO: comments??
        tokenizer.commentChar('#');
        tokenizer.quoteChar('"');
        tokenizer.quoteChar('\'');

        // Terminals
        tokenizer.ordinaryChar('<');
        tokenizer.ordinaryChar('>');
        tokenizer.ordinaryChar('/');
        tokenizer.ordinaryChar('=');
        
        tokenizer.whitespaceChars(' ', ' ');
        tokenizer.whitespaceChars('\n', '\n');
        tokenizer.whitespaceChars('\t', '\t');
        tokenizer.whitespaceChars('\r', '\r');

        tokenizer.parseNumbers(); // TODO: manual parsing required or not?
        tokenizer.lowerCaseMode(false); // X3D is case-sensitive
        tokenizer.eolIsSignificant(false); // We can count lines with tokenizer.lineno()
    }

    
    /*************************************************************
     *       XML parser built according to the SAX approach.     *
     *             (that is, an event-driven parser)             *
     *************************************************************/
    
    /**
     * Performs the parsing of the input file
     * and returns an ArrayList of core nodes.
     */
    @Override
    protected ArrayList<Node> parseScene() throws SyntaxError, IOException {

        init(); // Initialization - to read the first token
                
        // !!! The entry point !!!
        parseXML();
        // As a result - the filled resultingNodes array.
        
        return resultingNodes;
    }
    
    /**
     * The main parsing routine that
     * goes through the XML file
     * and reports events, such as
     * opening tag, closing tag etc.
     */
    private void parseXML() {
        // TODO: This is a DFA! (a picture to the coursework presentation?)
        while (lookahead != null) {
            
            // 1. Opening or closing tag starts
            if (lookahead("<")) {
                match("<");
                readingTag = true;
                
                if (lookahead("/")) {
                    match("/");
                    if (currentTag.size() == 0) {
                        throw new SyntaxError ("Closing tag + '" + lookahead
                                + "' does not match any opening tag.",
                                tokenizer.lineno());
                    }
                    String openingTag = currentTag.pop();
                    if (! lookahead(openingTag)) {
                        throw new SyntaxError ("Closing tag + '" + lookahead
                                      + "' does not match the opening tag + '"
                                      + openingTag + "'.", tokenizer.lineno());
                    }
                    closingTag(lookahead);
                    
                } else {
                    currentTag.push(lookahead);
                    openingTag(lookahead);
                }
                
                nextToken();
            }
            // 2. Some tag ends
            else if (lookahead(">")) {
                
                match(">");
                readingTag = false;
            }
            // 3. Opening tag is closed at once
            else if (lookahead("/")) {
                match("/");
                match(">");
                
                readingTag = false;
                closingTag(currentTag.pop());
            }
            // 4. Attribute
            else if (readingTag) {
                matchAttributeId();
                match("=");
                
                attribute(currentAttribute, lookahead);
                nextToken();                
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
    
    
    /*************************************************************
     *                           Events.                         *
     *************************************************************/
    
    private void openingTag(String name) {
        
System.out.println("Opening: " + name);

    }
    
    private void closingTag(String name) {
        
System.out.println("Closing: " + name);
        
    }
    
    private void attribute(String name, String value) {
        
System.out.println("Attribute: " + name + " set to " + value);

    }
    
    private void textNode(String value) {
        
System.out.println("Text node: " + value);
        
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
     * Determines whether the lookahead token
     * coincides with the given one.
     */
    public boolean lookahead(String token) {
        return (lookahead != null && token != null && lookahead.equals(token));
    }
    
    /**
     * Compares the token with the lookahead symbol and
     * advances to the next input terminal if they match.
     * 
     * @param token Token to be matched
     * @throws SyntaxError if token isn't matched
     * @return true if token is matched and the next token is read,
     */
    public boolean match(String token) throws SyntaxError {
        
        if(token.equals(lookahead)) {

            nextToken();
            
            return true;
            
        } else {
            
            throw new SyntaxError("Expected '" + token + "', but got '" + 
                                    lookahead + "'", tokenizer.lineno());
        }
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
            
            return true;
            
        } else {
            
            throw new SyntaxError("'" + lookahead + "' is not a valid id", tokenizer.lineno());
        }
    }


    /**
     * Reads the next token from the input.
     */
    public void nextToken() {
        
        try {
            int ttype = tokenizer.nextToken();
            
            if (ttype == '<' || ttype == '>' ||
                ttype == '/'|| ttype == '=') {
                // Terminals
                lookahead = String.valueOf(((char)tokenizer.ttype));
            } else if (ttype == StreamTokenizer.TT_WORD) {
                // Non-terminals
                lookahead = tokenizer.sval;
            }  else if (ttype == '"' || ttype == '\'') {
                // Quoted Strings
                lookahead = tokenizer.sval;
            } else if (ttype == StreamTokenizer.TT_EOF) {
                lookahead = null;
            } // No TT_NUMBER or TT_EOL can arise
        } catch (IOException e) {}
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
        currentTag = new Stack<String>();
        readingTag = false;
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
    private String lookahead;
    
    /** Determines whether we are inside a tag */
    private boolean readingTag;
    
    /* current Attribute id */
    private String currentAttribute;
    /* current Tag (Node) Type */
    //private String currentTag;
    
    // ! NB: The nested structure of XML nodes requires
    //       maintaining of a tag stacks: for tag names
    //       and for the appropriate nodes (if needed).
    
    /* Tag stack */
    private Stack<String> currentTag;
    /* Node stack */
    private Stack<Node> currentNodes;
}
