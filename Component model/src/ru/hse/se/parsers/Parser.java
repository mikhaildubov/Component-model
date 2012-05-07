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
     * @throws SyntaxError
     * @throws IOException
     */
    public ArrayList<Node> parse(InputStreamReader reader)
                                throws SyntaxError, IOException {

        tokenizer = new StreamTokenizer(new BufferedReader(reader));
        
        // TODO: Encoding issue??? *.wrl UTF8 -> bad; ANSI -> ok
        
        setUpTokenizer();
        
        ArrayList<Node> sceneGraph = parseScene();

        return sceneGraph;
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
     * Performs the parsing of the input file
     * and returns an ArrayList of root nodes.
     */
    protected abstract ArrayList<Node> parseScene() throws SyntaxError, IOException;
    
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
     */
    public abstract void nextToken();
    
    /**
     * Compares the token with the lookahead symbol and
     * advances to the next input terminal if they match.
     * 
     * Note: The method is case sensitive.
     * 
     * @param token Token to be matched
     * @throws SyntaxError if token isn't matched
     * @return true if token is matched and the next token is read,
     */
    public boolean match(String token) throws SyntaxError {
        
        if(token.equals(lookahead())) {

            nextToken();
            
            return true;
            
        } else {
            
            throw new SyntaxError("Expected '" + token + "', but got '" + 
                                    lookahead() + "'",
                                    tokenizer.lineno());
        }
    }

    /** Tokenizer */
    protected StreamTokenizer tokenizer;
    
    /** The nodes package name (needed for reflection) */
    protected static final String nodesPackageName = "ru.hse.se.nodes";
}
