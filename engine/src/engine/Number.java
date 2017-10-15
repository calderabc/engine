package engine;

// This is only meant to work for positive numbers.
// Negative values have no meaning, won't work.
public class Number {
	private volatile long value;
	private final Digit[] digits;

	public Number(Game game, int row, int newLength) {
		digits = new Digit[newLength];
		for (int i = 0; i < newLength; i++) {
			digits[i] =  new Digit(game, new Coordinates(i, row));
		}
	}
	
	public final Number add(long offset) {
		value += offset;
		set(value);
		return this;
	}
	
	public final Number add(Number other) {
		return add(other.value);
	}
	
	public final Number set(long newValue) {
		value = newValue;
		for (int i = digits.length - 1; i >= 0; i--) {
			digits[i].set((byte)(newValue % 10));
			newValue = newValue / 10; 
		}
		
		return this;
	}

	public final long get() {
		return value;
	}
}
