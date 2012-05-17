package ru.hse.se.parsers.errors;

public class LexicalError extends Error {
    private static final long serialVersionUID = 1L;

    public LexicalError(String error, int line, String token) {

        super("Line " + line + ": " + error);
        // TODO: Suggest substitutions
        this.line = line;
    }
    
    public int getLine() {
        return line;
    }
    
    private int line;
}
