package ru.hse.se.types;

import ru.hse.se.parsers.VRMLParser;
import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.parsers.XMLParser;

public abstract class ValueType extends VRMLType {
    
    public static ValueType parse(VRMLParser parser) throws SyntaxError {
        return null;
    }
    
    public static ValueType parse(XMLParser parser) throws SyntaxError {
        return null;
    }
}
