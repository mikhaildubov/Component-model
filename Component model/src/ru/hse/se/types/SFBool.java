package ru.hse.se.types;

public class SFBool extends VRMLType {
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
	public boolean value;
}
