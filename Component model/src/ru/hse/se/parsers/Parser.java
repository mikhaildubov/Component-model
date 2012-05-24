package ru.hse.se.parsers;

import ru.hse.se.nodes.Node;
import ru.hse.se.parsers.errors.*;
import ru.hse.se.types.*;
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
     * @throws If the parser cannot process some errors
     */
    public ArrayList<Node> parse(InputStreamReader reader)
                                throws IOException, Error {

        tokenizer = new StreamTokenizer(new BufferedReader(reader));
        
        // TODO: Encoding issue??? *.wrl UTF8 -> bad; ANSI -> ok
        
        setUpTokenizer();
        
        init();

        sceneGraph = new ArrayList<Node>();
        parsingErrors = new ArrayList<ParsingError>();

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
    protected Object parseValueType(Class<?> currentFieldType) {

        Object value = null;
        // TODO: No Syntax Error messages from type classes?..
        
        /****** a) Value type => call "parse" method in the type class via reflection ******/
        if (ValueType.class.isAssignableFrom(currentFieldType)) {
            try {
                value = currentFieldType.getDeclaredMethod
                    ("parse", Parser.class).invoke(null, this);
            } catch (NoSuchMethodException e) {
                registerError (new ParsingError("Parse rules for type " +
                            currentFieldType.getName() + " not defined.",
                                                    tokenizer.lineno()));
            } catch (Exception e) { }
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
            
        } else if (currentFieldType == String.class) { 
            value = (String)(SFString.parse(this).getValue());
            
        } else if (currentFieldType == String[].class) {
            ArrayList<SFString> val = MFString.parse(this).getValue();
            value = new String[val.size()];
            for (int i = 0; i < val.size(); i++) {
                ((String[])value)[i] = (String)(val.get(i).getValue());
            }
            
        }
        
        //else if (currentFieldType == ArrayList.class) {
        // TODO: Process ArrayLists?    
        //}
        
        /****** c) Error otherwise ******/
        else {
           registerError(new ParsingError
                    ("Value of unknown type", tokenizer.lineno()));
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
     * Determines whether lookahead is a quotated string.
     * 
     * @return true if lookahead is a string, false otherwise
     */
    public boolean lookaheadIsQuotatedString() {
        return lookaheadIsQuotatedString;
    }
    
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
            
            registerError(new SyntaxError("Expected '" + token + "', but got '" + 
                                         lookahead() + "'", tokenizer.lineno()));
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
    public boolean tryMatch(String token) {
        if (lookahead(token)) {
            match(token);
            return true;
        } else {
            possibleError = new SyntaxError("Expected '" + token +
                    "', but got '" + lookahead() + "'", tokenizer.lineno());
            return false;
        }
    }
    
    /**
     * Processes a parsing error.
     * 
     * @param e the error object
     * @throws Error if there are too many errors
     */
    public boolean registerError(ParsingError e) throws Error {

        if (parsingErrors.size() >= 128) {
            throw new Error("Too many errors during parsing");
        }
        
        if (e != null) {
            parsingErrors.add(e);
        }
        
        return true;
    }
    
    /**
     * Returns the list of parsing errors.
     * 
     * @return the ArrayList of parsing error objects
     */
    public ArrayList<ParsingError> getParsingErrors() {
        return parsingErrors;
    }
    
    /**
     * Determines whether the node with a given
     * name exists in one of registered node packages,
     * and returns the appropriate Class<?> object
     * if there is such a node type.
     * 
     * @param str Node name (simple name)
     * @return the appropriate Class if the node exists,
     *         null otherwise
     */
    protected Class<?> classForNodeName(String str) {
        Class<?> res = null;
        for (String pkg : nodePackages) {
            try {
                res = Class.forName(pkg + "." + str);
                // here => Class found
                break;
            } catch (ClassNotFoundException e) {}
        }
        return res;
    }

    
    /**
     * Creates instance of a node for its name.
     * 
     * @param str Node name (simple name)
     * @return the node object (if this node type exists)
     * @throws Exception if there are instantiation errors
     */
    protected Node createInstance(String str)
                    throws ClassNotFoundException {
        for (String pkg : nodePackages) {
            try {
                Node res = (Node)(Class.forName(pkg + "." + str).newInstance());
                // here => Class found
                return res;
            } catch (Exception e) {}           
        }
        throw new ClassNotFoundException();
    }
    
    /**
     * Registers new package that contains nodes
     * that can appear in the input file.
     * Registering is needed in order for the parser
     * to be able to check for errors during the parsing.
     * 
     * @param packageName packageName
     */
    public void registerNodePackage(String packageName) {
        nodePackages.add(packageName);
    }

    /** Tokenizer */
    protected StreamTokenizer tokenizer;
    
    /** The result of scene parsing */
    protected ArrayList<Node> sceneGraph;
    
    /** The errors that occured during parsing */
    protected ArrayList<ParsingError> parsingErrors;
    
    /**
     * The error that occured during the last call
     * of some tryXxx method; such an error is not
     * registered until registerPossibleError() is called.
     */
    protected ParsingError possibleError = null;
    
    /** The nodes package name (needed for reflection) */
    protected static final ArrayList<String> nodePackages;
    
    /** Determines whether lookahead is a quotated string */
    protected boolean lookaheadIsQuotatedString = false;
    
    static {
        nodePackages = new ArrayList<String>();
        nodePackages.add("ru.hse.se.nodes");
    }
    
}
