package engine.puzzle;

import engine.Coordinates;
import engine.MovablePart;
import engine.Visual.Id;

public class Digit extends MovablePart {
	private byte value;
	
	public Digit(Coordinates newPosition, byte newValue) {
		super(newPosition, new Id((byte)2));
		set(newValue);
		PuzzleGame.me.screen.addPart(this);
	}
	
	public Digit(Coordinates newPosition) {
		this(newPosition, (byte)0);
	}
	
	public byte get() {
		return value;
	}
	
	public Digit set(byte newValue) {
		if (value > 9 || value < 0) {
			throw new IllegalArgumentException();
		}
		byte oldValue = value;
		value = newValue;
		if (value != oldValue) {
			// If the value has changed update the visual.
			visual.update(this);
		}
		
		return this;
	}
}
