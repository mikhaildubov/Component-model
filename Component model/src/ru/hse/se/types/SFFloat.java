package ru.hse.se.types;

import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.parsers.Parser;

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
    public static SFFloat parse(Parser parser) throws SyntaxError {

        double res = 0;
        
        try {
            res = Double.parseDouble(parser.lookahead());
            parser.nextToken();
            
        } catch (Exception e) {
            parser.error(new SyntaxError("Expected a double-precision float number" +
                                    " in ANSI C format, but got '" +
                                    parser.lookahead() + "'", parser.tokenizer().lineno()));
        }
        
        return new SFFloat(res);
    }
    
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    
    
    private double value;
}
