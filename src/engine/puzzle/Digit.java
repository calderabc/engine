package engine.puzzle;

import engine.Part;

public class Digit extends Part {
	private int value;
	
	public Digit(int newX, int newY, int newValue) {
		
		value = newValue;
	}
	
	public int getValue() {
		return value;
	}
	
	public int setValue(int newValue) {
		if (value != newValue) {
			value = newValue;
			
		}
		
		return value;
	}
	
	
}
