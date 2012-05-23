package ru.hse.se.types;

import java.util.zip.DataFormatException;
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
    public static SFInt32 parse(Parser parser) {

        SFInt32 res = null;
        
        try {
            res = parse(parser.lookahead());
            parser.nextToken();
        } catch (DataFormatException e) {
            parser.registerError(new SyntaxError(e.getMessage(),
                                parser.tokenizer().lineno()));
        }
        
        return res;
    }
    
    public static SFInt32 parse(String str) throws DataFormatException {
        int sign = 1;
        if (str.charAt(0) == '+') {
            str = str.substring(1);
        } else if (str.charAt(0) == '-') {
            sign = -1;
            str = str.substring(1);
        }
        
        int res = 0;
        
        if(str.startsWith("0x")) { // hex format
            
            char temp;
            for (int i = 2; i < str.length(); i++) {
                temp = Character.toLowerCase(str.charAt(i));
                if (temp >= '0' && temp <= '9') {
                    res = 16*res + (temp-'0');
                } else if (temp >= 'a' && temp <= 'f') {
                    res = 16*res + (10+temp-'a');
                } else {
                    throw new DataFormatException
                        ("Expected a hexadecimal integer, " +
                                    "but got '" + str +"'");
                }
            }
            
        } else { // decimal format
            
            try {
                res = Integer.parseInt(str);
            } catch (Exception e) {
                throw new DataFormatException
                    ("Expected an integer number, " + "but got '" + str + "'");
            }
            
        }
        
        return new SFInt32(res*sign);
    }
    
    public static SFInt32 tryParse(String str) {
        try {
            return parse(str);
        } catch (DataFormatException e) {
            return null;
        }
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    
    
    private int value;
}
