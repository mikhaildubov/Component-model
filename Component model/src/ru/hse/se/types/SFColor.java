package ru.hse.se.types;

public class SFColor extends VRMLType {
	
	@Override
	public String toString() {
		return (r + " " + g + " " + b);
	}
	
	public double r, g, b;
}