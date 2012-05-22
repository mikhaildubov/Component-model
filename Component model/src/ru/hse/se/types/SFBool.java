package ru.hse.se.types;

import java.util.zip.DataFormatException;
import ru.hse.se.parsers.Parser;
import ru.hse.se.parsers.errors.SyntaxError;

public class SFBool extends ValueType {
    
    public SFBool(boolean value) {
        this.value = value;
    }
    
    public boolean getValue() {
        return value;
    }
    
    /**
     * Parses a boolean / SFBool value from the stream.
     * 
     ***************************************
     * sfboolValue ::=                     *
     *         TRUE |                      *
     *         FALSE                       *
     ***************************************
     */
    public static SFBool parse(Parser parser) {

        SFBool res = new SFBool(false);
        
        try {
            res = parse(parser.lookahead());
            parser.nextToken();
        } catch (DataFormatException e) {
            parser.registerError(new SyntaxError(e.getMessage(),
                                    parser.tokenizer().lineno()));
        }
        
        return res;
    }
    
    public static SFBool parse(String str) throws DataFormatException {
        
        if (str.toUpperCase().equals("TRUE")) {
            return new SFBool(true);
        } else if (str.toUpperCase().equals("FALSE")) {
            return new SFBool(false);
        } else {
            throw new DataFormatException("Expected 'TRUE' or 'FALSE', "+
                                            "but got '" + str + "'");
        }
    }
    
    public static SFBool tryParse(String str) {
        try {
            return parse(str);
        } catch (DataFormatException e) {
            return null;
        }
    }
    
    @Override
    public String toString() {
        return value ? "TRUE" : "FALSE";
    }
    
    
    private boolean value;
}
