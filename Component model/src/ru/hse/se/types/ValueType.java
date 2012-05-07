package ru.hse.se.types;

import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.parsers.Parser;

public abstract class ValueType extends VRMLType {
    
    public static ValueType parse(Parser parser) throws SyntaxError {
        return null;
    }
}
