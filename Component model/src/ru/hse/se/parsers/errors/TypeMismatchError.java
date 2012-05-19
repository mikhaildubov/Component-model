package ru.hse.se.parsers.errors;

public class TypeMismatchError extends ParsingError {

    public TypeMismatchError(Class<?> given, Class<?> required, int line) {

        super("type '" + given.getSimpleName() +
                "' does not match type '" +
                required.getSimpleName() + "'",
                line);
    }
    
    private static final long serialVersionUID = 1L;
}
