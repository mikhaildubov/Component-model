package ru.hse.se.types;

import java.util.zip.DataFormatException;
import ru.hse.se.parsers.Parser;

public abstract class ValueType extends VRMLType {
    
    /**
     * Parses a value type from the input stream using
     * some parser (that allows to parse both VRML and X3D encoding).
     * 
     * @param parser Parser that has the first token of the value as lookahead
     * @return the ValueType object
     */
    public static ValueType parse(Parser parser) {
        return null;
    }
    
    /**
     * Parses a value type from the input string.
     * 
     * @param str String that contains the value
     * @return the ValueType object
     * @throws DataFormatException if there are syntax errors
     */
    public static ValueType parse(String str) throws DataFormatException {
        return null;
    }
    
    /**
     * Tries to parse a value type from the input string,
     * returns null if parsing didn't succeed.
     * 
     * @param strString that contains the value
     * @return the ValueType object or null
     */
    public static ValueType tryParse(String str) {
        return null;
    }
}
