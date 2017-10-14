package engine.puzzle;


import engine.Coordinates;
import engine.Game;
import engine.Part;
import engine.Visual;

public class Digit extends Part {
	private byte value;
	public final Number.Type type;
	
	public Digit(Game game, Number.Type newType, Coordinates newPosition, byte newValue) {
		super(game, newPosition, new Visual.Id("Digit"));
		type = newType;
		// Don't use setter because visual may not be initialized yet.
		this.value = newValue;

		game.screen.addPart(this);
	}
	
	public Digit(Game game, Number.Type newType, Coordinates newPosition) {
		this(game, newType, newPosition, (byte)0);
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
		if (value != oldValue && visual != null) {
			// If the value has changed update the visual.
				visual.update(this);
		}
		
		return this;
	}
}
