package ru.hse.se.parsers.errors;

public class TypeMismatchError extends Error {
    private static final long serialVersionUID = 1L;

    public TypeMismatchError(Class<?> given, Class<?> required, int line) {

        super("Line " + line + ": " + 
                "type '" + given.getSimpleName() +
                "' does not match type '" +
                required.getSimpleName());
        this.line = line;
    }
    
    public int getLine() {
        return line;
    }
    
    private int line;
}
