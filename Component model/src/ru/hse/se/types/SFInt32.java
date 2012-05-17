package ru.hse.se.types;

import ru.hse.se.parsers.Parser;
import ru.hse.se.parsers.errors.SyntaxError;

public class SFInt32 extends ValueType {
    
    public SFInt32(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    /**
     * Reads an Integer / SFInt32 value from the stream.
     * 
     ***************************************
     * sfint32Value ::=                    *
     *    [[+]|-]{[0-9]+|0x[0-9a-fA-F]+}   *
     ***************************************
     */
    public static SFInt32 parse(Parser parser) throws SyntaxError {

        String lookahead = parser.lookahead();
        
        int sign = 1;
        if (lookahead.charAt(0) == '+') {
            lookahead = lookahead.substring(1);
        } else if (parser.lookahead().charAt(0) == '-') {
            sign = -1;
            lookahead = lookahead.substring(1);
        }
        
        int res = 0;
        
        if(lookahead.startsWith("0x")) { // hex format
            
            char temp;
            for (int i = 2; i < lookahead.length(); i++) {
                temp = Character.toLowerCase(lookahead.charAt(i));
                if (temp >= '0' && temp <= '9') {
                    res = 16*res + (temp-'0');
                } else if (temp >= 'a' && temp <= 'f') {
                    res = 16*res + (10+temp-'a');
                } else {
                    parser.registerError(new SyntaxError("Expected a hexadecimal integer, " +
                           "but got '" + lookahead +"'", parser.tokenizer().lineno()));
                }
            }
            
        } else { // decimal format
            
            try {
                res = Integer.parseInt(lookahead);
            } catch (Exception e) {
                parser.registerError(new SyntaxError("Expected an integer number, " +
                  "but got '" + lookahead + "'", parser.tokenizer().lineno()));
            }
            
        }
        
        parser.nextToken();
        
        return new SFInt32(res*sign);
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    
    
    private int value;
}
