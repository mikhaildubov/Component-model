package ru.hse.se.types;

import java.util.zip.DataFormatException;
import ru.hse.se.parsers.Parser;

public class SFVec3f extends ValueType {
    
    public SFVec3f(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    public double getA() {
        return a;
    }
    
    public double getB() {
        return b;
    }
    
    public double getC() {
        return c;
    }
    
    /**
     * Reads a SFColor value from the stream.
     * 
     ***************************************
     * sfvec3fValue ::=                    *
     *          float float float          *
     ***************************************
     */
    public static SFVec3f parse(Parser parser) {

        SFFloat a = SFFloat.parse(parser);
        SFFloat b = SFFloat.parse(parser);
        SFFloat c = SFFloat.parse(parser);
        
        return new SFVec3f(a.getValue(), b.getValue(), c.getValue());
    }
    
    public static SFVec3f parse(String str) throws DataFormatException {

        String a = "", b = "", c = "";
        int i = 0;
        
        while (i < str.length() &&
                str.charAt(i) != ' ' && str.charAt(i) != ',') {
            a += str.charAt(i);
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
        
        while (i < str.length() &&
                (str.charAt(i) == ' ' || str.charAt(i) == ',')) {
            i++;
        }
        
        while (i < str.length() &&
                str.charAt(i) != ' ' && str.charAt(i) != ',') {
            c += str.charAt(i);
            i++;
        }
        
        return new SFVec3f(SFFloat.parse(a).getValue(),
                            SFFloat.parse(b).getValue(),
                            SFFloat.parse(c).getValue());
    }
    
    public static SFVec3f tryParse(String str) {
        try {
            return parse(str);
        } catch (DataFormatException e) {
            return null;
        }
    }
    
    @Override
    public String toString() {
        return (a + " " + b + " " + c);
    }
    
    
    
    private double a, b, c;
}