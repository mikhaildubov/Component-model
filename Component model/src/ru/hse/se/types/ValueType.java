package ru.hse.se.types;

import ru.hse.se.parsers.Parser;
import ru.hse.se.parsers.errors.SyntaxError;

public abstract class ValueType extends VRMLType {
    
    public static ValueType parse(Parser parser) throws SyntaxError {
        return null;
    }
}
