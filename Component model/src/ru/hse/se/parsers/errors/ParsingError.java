package ru.hse.se.parsers.errors;

public abstract class ParsingError extends Error {

    public ParsingError(String msg, int line) {
        super("Line " + line + ": " + msg);
    }
    
    public int getLine() {
        return line;
    }
    
    protected int line;
    
    private static final long serialVersionUID = 1L;
}
