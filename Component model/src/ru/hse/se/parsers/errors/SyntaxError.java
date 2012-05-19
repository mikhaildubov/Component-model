package ru.hse.se.parsers.errors;

public class SyntaxError extends ParsingError {

	public SyntaxError(String error, int line) {

        super(error, line);
	}
    
    private static final long serialVersionUID = 1L;
}
