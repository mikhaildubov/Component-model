package ru.hse.se.types;

import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.parsers.Parser;

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
    public static SFString parse(Parser parser) throws SyntaxError {
        SFString res = new SFString(parser.lookahead());
        parser.nextToken();
        return res;
    }
    
    
    @Override
    public String toString() {
        return '"' + value + '"';
    }
    
    
    private String value;
}
