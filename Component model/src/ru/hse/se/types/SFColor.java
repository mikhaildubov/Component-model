package ru.hse.se.types;

import java.util.zip.DataFormatException;
import ru.hse.se.parsers.Parser;

public class SFColor extends ValueType {
    
    public SFColor(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public double getR() {
        return r;
    }
    
    public double getG() {
        return g;
    }
    
    public double getB() {
        return b;
    }
    
    /**
     * Reads a SFColor value from the stream.
     * 
     ***************************************
     * sfcolorValue ::=                    *
     *          float float float          *
     ***************************************
     */
    public static SFColor parse(Parser parser) {

        SFFloat r = SFFloat.parse(parser);
        SFFloat g = SFFloat.parse(parser);
        SFFloat b = SFFloat.parse(parser);
        
        return new SFColor(r.getValue(), g.getValue(), b.getValue());
    }
    
    public static SFColor parse(String str) throws DataFormatException {

        String r = "", g = "", b = "";
        int i = 0;
        
        while (i < str.length() &&
                str.charAt(i) != ' ' && str.charAt(i) != ',') {
            r += str.charAt(i);
            i++;
        }
        
        while (i < str.length() &&
                (str.charAt(i) == ' ' || str.charAt(i) == ',')) {
            i++;
        }
        
        while (i < str.length() &&
                str.charAt(i) != ' ' && str.charAt(i) != ',') {
            g += str.charAt(i);
            i++;
        }
        
        while (i < str.length() &&
                (str.charAt(i) == ' ' || str.charAt(i) == ',')) {
            i++;
        }
        
        while (i < str.length() &&
                str.charAt(i) != ' ' && str.charAt(i) != ',') {
            b += str.charAt(i);
            i++;
        }
        
        return new SFColor(SFFloat.parse(r).getValue(),
                            SFFloat.parse(g).getValue(),
                            SFFloat.parse(b).getValue());
    }
    
    public static SFColor tryParse(String str) {
        try {
            return parse(str);
        } catch (DataFormatException e) {
            return null;
        }
    }
    
    @Override
    public String toString() {
        return (r + " " + g + " " + b);
    }
    
    
    
    private double r, g, b;
}