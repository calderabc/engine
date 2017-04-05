package engine.puzzle;

import engine.Part;
import engine.StaticPart;

public class Digit extends StaticPart<Part<?>> {
	private int value;
	
	public Digit(int newX, int newY, int newValue) {
		super(newX, newY);
		
		value = newValue;
	}
	
	public int getValue() {
		return value;
	}
	
	public int setValue(int newValue) {
		if (value != newValue) {
			value = newValue;
			
			getRenderer().update();
		}
		
		return value;
	}
	
	
}
