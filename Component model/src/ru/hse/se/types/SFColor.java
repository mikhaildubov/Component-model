package ru.hse.se.types;

import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.parsers.VRMLParser;

public class SFColor extends ValueType {
	
	public SFColor(double r, double g, double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public double getR() {
		return r;
	}
	
	public double getG() {
		return g;
	}
	
	public double getB() {
		return b;
	}
	
	/**
     * Reads a SFColor value from the stream.
     * 
     ***************************************
     * sfcolorValue ::=                    *
     *          float float float          *
     ***************************************
     */
	public static SFColor parse(VRMLParser parser) throws SyntaxError {

		SFFloat r = SFFloat.parse(parser);
		SFFloat g = SFFloat.parse(parser);
		SFFloat b = SFFloat.parse(parser);
        
        return new SFColor(r.getValue(), g.getValue(), b.getValue());
	}
	
	@Override
	public String toString() {
		return (r + " " + g + " " + b);
	}
	
	
	
	private double r, g, b;
}