package ru.hse.se.parsers.errors;

public class Warning extends ParsingError {

    public Warning(String error, int line) {

        super(error, line);
    }
    
    private static final long serialVersionUID = 1L;
}
