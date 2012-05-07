package ru.hse.se.parsers;

public class SyntaxError extends Error {
	private static final long serialVersionUID = 1L;

	public SyntaxError(String error, int line) {

        super("Line " + line + ": " + error);
	    this.line = line;
	}
	
	public int getLine() {
	    return line;
	}
	
	private int line;
}
