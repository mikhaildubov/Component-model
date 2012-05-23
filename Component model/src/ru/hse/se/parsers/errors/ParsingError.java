package ru.hse.se.parsers.errors;

public class ParsingError extends Error {

    public ParsingError(String msg, int line) {
        super(msg);
        this.line = line;
    }
    
    public int getLine() {
        return line;
    }
    
    public String getLineMessage() {
        return ("Line " + line + ": " + getMessage());
    }
    
    protected int line;
    
    private static final long serialVersionUID = 1L;
}
