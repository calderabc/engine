package engine.puzzle;

import engine.Coordinates;

// This is only meant to work for positive numbers.
// Negative values have no meaning, won't work.
public class Number {
	private long value;
	private Digit[] digits;
	
	public Number(long newValue, byte newLength) {
		digits = new Digit[newLength];
		for (int i = 0; i < newLength; i++) {
			digits[i] =  new Digit(new Coordinates(i));
		}
		set(newValue);
	}
	
	public final Number add(long offset) {
		value += offset;
		set(value);
		return this;
	}
	
	public final Number add(Number other) {
		return add(other.value);
	}
	
	public final Number set(long value) {
		for (int i = digits.length - 1; i >= 0; i--) {
			digits[i].set((byte)(value % 10));
			value = value % 10; 
		}
		
		return this;
	}
	
	public final long get() {
		return value;
	}
}
