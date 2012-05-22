package ru.hse.se.types;

import java.util.zip.DataFormatException;
import ru.hse.se.parsers.Parser;
import ru.hse.se.parsers.errors.SyntaxError;

public class SFString extends ValueType {
    
    public SFString(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    /**
     * Parses a SFString value from the stream.
     * 
     ***************************************
     * sfstringValue ::=                   *
            string ;                       *
     * string ::=                          *
     *      ".*" ... double-quotes must be *
     *      \", backslashes must be \\...  *
     ***************************************
     */
    public static SFString parse(Parser parser) {
        // TODO: Check whether it is a string (qutation marks)!
        SFString res = new SFString("");
        
        try {
            res = parse(parser.lookahead());
            parser.nextToken();
        } catch (DataFormatException e) {
            parser.registerError(new SyntaxError(e.getMessage(),
                                parser.tokenizer().lineno()));
        }
        
        return res;
    }
    
    public static SFString parse(String str) throws DataFormatException {
        // Trim spaces and '"'
        while (str.charAt(0) == ' ') {
            str = str.substring(1);
        }
        while (str.charAt(str.length()-1) == ' ') {
            str = str.substring(0, str.length()-1);
        }
        if (str.charAt(0) == '"') {
            str = str.substring(1);
        }
        if (str.charAt(str.length()-1) == '"') {
            str = str.substring(0, str.length()-1);
        }
        return new SFString(str);
    }
    
    public static SFString tryParse(String str) {
        try {
            return parse(str);
        } catch (DataFormatException e) {
            return null;
        }
    }
    
    
    @Override
    public String toString() {
        return '"' + value + '"';
    }
    
    
    private String value;
}
