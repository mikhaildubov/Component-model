package ru.hse.se.types;

import java.util.zip.DataFormatException;
import ru.hse.se.parsers.Parser;

public class SFRotation extends ValueType {
    
    public SFRotation(double x, double y, double z, double a) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.a = a;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public double getZ() {
        return z;
    }
    
    public double getA() {
        return a;
    }
    
    /**
     * Reads a SFColor value from the stream.
     * 
     ***************************************
     * sfcolorValue ::=                    *
     *          float float float          *
     ***************************************
     */
    public static SFRotation parse(Parser parser) {

        SFFloat x = SFFloat.parse(parser);
        SFFloat y = SFFloat.parse(parser);
        SFFloat z = SFFloat.parse(parser);
        SFFloat a = SFFloat.parse(parser);
        
        return new SFRotation(x.getValue(), y.getValue(),
                            z.getValue(), a.getValue());
    }
    
    public static SFRotation parse(String str) throws DataFormatException {

        String x = "", y = "", z = "", a = "";
        int i = 0;
        
        while (i < str.length() &&
                str.charAt(i) != ' ' && str.charAt(i) != ',') {
            x += str.charAt(i);
            i++;
        }
        
        while (i < str.length() &&
                (str.charAt(i) == ' ' || str.charAt(i) == ',')) {
            i++;
        }
        
        while (i < str.length() &&
                str.charAt(i) != ' ' && str.charAt(i) != ',') {
            y += str.charAt(i);
            i++;
        }
        
        while (i < str.length() &&
                (str.charAt(i) == ' ' || str.charAt(i) == ',')) {
            i++;
        }
        
        while (i < str.length() &&
                str.charAt(i) != ' ' && str.charAt(i) != ',') {
            z += str.charAt(i);
            i++;
        }
        
        while (i < str.length() &&
                (str.charAt(i) == ' ' || str.charAt(i) == ',')) {
            i++;
        }
        
        while (i < str.length() &&
                str.charAt(i) != ' ' && str.charAt(i) != ',') {
            a += str.charAt(i);
            i++;
        }
        
        return new SFRotation(SFFloat.parse(x).getValue(),
                            SFFloat.parse(y).getValue(),
                            SFFloat.parse(z).getValue(),
                            SFFloat.parse(a).getValue());
    }
    
    public static SFRotation tryParse(String str) {
        try {
            return parse(str);
        } catch (DataFormatException e) {
            return null;
        }
    }
    
    @Override
    public String toString() {
        return (x + " " + y + " " + z + " " + a);
    }
    
    
    
    private double x, y, z, a;
}