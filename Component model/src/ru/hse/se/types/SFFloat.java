package ru.hse.se.types;

import java.util.zip.DataFormatException;
import ru.hse.se.parsers.Parser;
import ru.hse.se.parsers.errors.SyntaxError;

public class SFFloat extends ValueType {
    
    public SFFloat(double value) {
        this.value = value;
    }
    
    public double getValue() {
        return value;
    }
    
    /**
     * Reads a double / SFFloat value from the stream.
     * 
     ***************************************
     * sffloatValue ::=                    *
     *    floating point number in         *
     *    ANSI C floating point format     *
     ***************************************
     */
    public static SFFloat parse(Parser parser) {

        SFFloat res = null;
        
        try {
            res = parse(parser.lookahead());
            parser.nextToken();
        } catch (DataFormatException e) {
            parser.registerError(new SyntaxError(e.getMessage(),
                                    parser.tokenizer().lineno()));
        }
        
        return res;
    }
    
    public static SFFloat parse(String str) throws DataFormatException {
        
        double res = 0;
        
        try {
            res = Double.parseDouble(str);
            
        } catch (Exception e) {
            throw new DataFormatException
                ("Expected a double-precision float number" +
                 " in ANSI C format, but got '" + str + "'");
        }
        
        return new SFFloat(res);
    }
    
    public static SFFloat tryParse(String str) {
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
    
    
    private double value;
}
