package engine.puzzle;

import engine.Coordinates;
import engine.Game;
import engine.Part;
import engine.Visual;
import engine.Visual.Id;
import engine.graphics2d.ImageType;

public class Digit extends Part {
	private byte value;
	public final Number.Type type;
	
	public Digit(Number.Type newType, Coordinates newPosition, byte newValue) {
		super(newPosition);
		type = newType;
		set(newValue);
		initVisual( ((PuzzleGame)Game.me).digitImageType,
		            new Visual.Id("Digit") );
		Game.me.screen.addPart(this);
	}
	
	public Digit(Number.Type newType, Coordinates newPosition) {
		this(newType, newPosition, (byte)0);
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
