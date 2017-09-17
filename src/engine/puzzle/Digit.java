package engine.puzzle;

import engine.Coordinates;
import engine.MovablePart;

public class Digit extends MovablePart {
	private int value;
	
	public Digit(Coordinates newPosition, int newValue) {
		super(newPosition);

		setValue(newValue);
	}
	
	public int getValue() {
		return value;
	}
	
	public int setValue(int newValue) {
		if (value > 9 || value < 0) {
			throw new IllegalArgumentException();
		}
			value = newValue;
		
		return value;
	}
	
	
}
