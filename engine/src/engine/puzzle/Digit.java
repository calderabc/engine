package engine.puzzle;


import engine.Coordinates;
import engine.Game;
import engine.Part;
import engine.Visual;

public class Digit extends Part {
	private byte value;
	public final Number.Type type;
	
	public Digit(Game game, Number.Type newType, Coordinates newPosition, byte value) {
		super(newPosition);
		type = newType;
		set(value);
		initVisual(game, new Visual.Id("Digit") );
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
		if (value != oldValue) {
			// If the value has changed update the visual.
			visual.update(this);
		}
		
		return this;
	}
}
