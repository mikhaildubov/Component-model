package ru.hse.se.parsers;

import ru.hse.se.nodes.Node;
import ru.hse.se.types.MFBool;
import ru.hse.se.types.MFFloat;
import ru.hse.se.types.MFInt32;
import ru.hse.se.types.SFBool;
import ru.hse.se.types.SFFloat;
import ru.hse.se.types.SFInt32;
import ru.hse.se.types.ValueType;

import java.io.*;
import java.util.ArrayList;

/**
 * An abstract parser class that defines
 * the basic items each parser should
 * have to analyze a scene.
 * 
 * @author Mikhail Dubov
 */
public abstract class Parser {

    /**
     * Parses the input file and builds
     * an array of root nodes.
     * 
     * @param reader The input stream reader
     * @return nodes array of root nodes
     * @throws IOException if there is no input in the stream
     */
    public ArrayList<Node> parse(InputStreamReader reader)
                                throws IOException {

        tokenizer = new StreamTokenizer(new BufferedReader(reader));
        
        // TODO: Encoding issue??? *.wrl UTF8 -> bad; ANSI -> ok
        
        setUpTokenizer();
        
        init();

        sceneGraph = new ArrayList<Node>();
        parsingErrors = new ArrayList<Error>();

        // !!! The entry point !!!
        parseScene();
        // As a result - the filled sceneGraph array,
        // that may be null if there were parsing errors.

        if (! parsingErrors.isEmpty()) {
            return null;
        } else {
            return sceneGraph;
        }
    }

    /**
     * Sets up the tokenizer object.
     */
    protected void setUpTokenizer() {
        
        tokenizer.resetSyntax();
        
        tokenizer.wordChars('a', 'z'); // Id's
        tokenizer.wordChars('A', 'Z'); // Id's
        tokenizer.wordChars('0', '9'); // Id's
        tokenizer.wordChars('_', '_'); // Id's can contain '_'
        tokenizer.wordChars('+', '+'); // For floats and ints
        tokenizer.wordChars('-', '-'); // For floats and ints
        tokenizer.wordChars('.', '.'); // For floats

        tokenizer.quoteChar('"');
        
        tokenizer.whitespaceChars(' ', ' ');
        tokenizer.whitespaceChars('\n', '\n');
        tokenizer.whitespaceChars('\t', '\t');
        tokenizer.whitespaceChars('\r', '\r');
        tokenizer.whitespaceChars(',', ','); // => for [ children ]
    }
    
    /**
     * Initializes the parser by reading the first
     * token and storing it in the lookahead variable.
     */
    protected abstract void init() throws IOException;
    
    /**
     * Performs the parsing of the input file
     * and fills out the ArrayList of root nodes,
     * namely the sceneGraph array.
     */
    protected abstract void parseScene() throws IOException;
    
    /**
     * Parses the next Node from the input stream.
     * Needed for MFNode parsing.
     */
    public abstract Node parseChildNode();
    
    /**
     * Parses a value of particular type from the input stream.
     * 
     * @param currentFieldType the type of the value
     * @return the value read from the stream
     * @throws Error
     */
    protected Object parseValueType(Class<?> currentFieldType) throws Error {

        Object value = null;
        // TODO: No Syntax Error messages from type classes?..
        
        /****** a) Value type => call "parse" method in the type class via reflection ******/
        if (ValueType.class.isAssignableFrom(currentFieldType)) {
            try {
                value = currentFieldType.getDeclaredMethod
                    ("parse", Parser.class).invoke(null, this);
            } catch (Exception e) { }
            
            if (value == null) {
                throw new Error("Parse rules for type " +
                        currentFieldType.getName() + " not defined.");
            }
        }
        
        /****** b) Java primitive types => use VRML wrappers (SFBool, SFFloat, ...) ******/
        // TODO: TEST!
        else if (currentFieldType == int.class) { 
            value = SFInt32.parse(this).getValue();
              
        } else if (currentFieldType == int[].class) { 
            ArrayList<SFInt32> val = MFInt32.parse(this).getValue();
            value = new int[val.size()];
            for (int i = 0; i < val.size(); i++) {
                ((int[])value)[i] = val.get(i).getValue();
            }
            
        } else if (currentFieldType == boolean.class) { 
            value = SFBool.parse(this).getValue();
            
        } else if (currentFieldType == boolean[].class) { 
            ArrayList<SFBool> val = MFBool.parse(this).getValue();
            value = new boolean[val.size()];
            for (int i = 0; i < val.size(); i++) {
                ((boolean[])value)[i] = val.get(i).getValue();
            }
            
        } else if (currentFieldType == double.class) { 
            value = SFFloat.parse(this).getValue();
            
        } else if (currentFieldType == double[].class) {
            ArrayList<SFFloat> val = MFFloat.parse(this).getValue();
            value = new double[val.size()];
            for (int i = 0; i < val.size(); i++) {
                ((double[])value)[i] = val.get(i).getValue();
            }
            
        } else if (currentFieldType == float.class) { 
            value = (float)(SFFloat.parse(this).getValue());
            
        } else if (currentFieldType == float[].class) {
            ArrayList<SFFloat> val = MFFloat.parse(this).getValue();
            value = new double[val.size()];
            for (int i = 0; i < val.size(); i++) {
                ((float[])value)[i] = (float)(val.get(i).getValue());
            }
            
        }
        
        //else if (currentFieldType == ArrayList.class) {
        // TODO: Process ArrayLists?    
        //}
        
        /****** c) Error otherwise ******/
        else {
            throw new Error("Value of unknown type");
        }
        
        return value;
    }
    
    /**
     * Returns the tokenizer.
     */
    public StreamTokenizer tokenizer() {
        return tokenizer;
    }
    
    /**
     * Determines whether the lookahead token
     * coincides with the given one.
     */
    public boolean lookahead(String token) {
        return (lookahead() != null && token != null && lookahead().equals(token));
    }
    
    /**
     * Returns the lookahead token.
     */
    public abstract String lookahead();
    
    /**
     * Reads the next token from the input.
     * @returns false when the next token is unavailable, true otherwise
     */
    public abstract boolean nextToken();
    
    /**
     * Compares the token with the lookahead symbol and
     * advances to the next input terminal if they match,
     * registers a syntax error otherwise
     * 
     * Note: The method is case sensitive.
     * 
     * @param token Token to be matched
     * @return true
     */
    public boolean match(String token) {
        
        if(lookahead().equals(token)) {

            nextToken();
            
        } else {
            
            error(new SyntaxError("Expected '" + token + "', but got '" + 
                                    lookahead() + "'",
                                    tokenizer.lineno()));
        }
        
        return true;
    }
    
    /**
     * Determines whether the lookahead token
     * is equal to the one passed as a parameter
     * and matches it if the result is true.
     * 
     * @param token the token
     * @return true if the matching was successful, false otherwise
     */
    protected boolean tryMatch(String token) {
        if (lookahead(token)) {
            match(token);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Processes a parsing error.
     * 
     * @param e the error object
     */
    protected boolean error(Error e) {

        parsingErrors.add(e);
        
        return true;
    }
    
    /**
     * Returns the list of parsing errors.
     * 
     * @return the ArrayList of parsing error objects
     */
    public ArrayList<Error> getParsingErrors() {
        return parsingErrors;
    }
    
    protected boolean isNodeName(String str) {
        for (String s : nodeNames) {
            if (s.equals(str)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Registers new nodes that can appear in
     * the input file. Registering is needed
     * in order for the parser to be able
     * to check for error during the parsing.
     * 
     * @param name array of node class names
     */
    public void registerNodes(ArrayList<String> names) {
        nodeNames.addAll(names);
    }

    /** Tokenizer */
    protected StreamTokenizer tokenizer;
    
    /** The result of scene parsing */
    protected ArrayList<Node> sceneGraph;
    
    /** The errors that occured during parsing */
    protected ArrayList<Error> parsingErrors;
    
    /** The nodes package name (needed for reflection) */
    protected static final String nodesPackageName = "ru.hse.se.nodes";
    
    /** The list of possible node names */
    protected static ArrayList<String> nodeNames;
    
    // Registering standard node names
    {
        nodeNames = new ArrayList<String>();
        nodeNames.add("Appearance");
        nodeNames.add("Box");
        nodeNames.add("Group");
        nodeNames.add("Material");
        nodeNames.add("Shape");
        nodeNames.add("Sphere");
        nodeNames.add("Text");
    }
}
