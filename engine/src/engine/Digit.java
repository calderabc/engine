package engine;


public class Digit extends Symbol {
	public Digit(Game game, Coordinates newPosition, byte newValue) {
		super(game, newPosition, new Visual.Id("Digit"));
		// Don't use setter because visual may not be initialized yet.
		this.value = newValue;

		game.screen.addPart(this);
	}
	
	public Digit(Game game, Coordinates newPosition) {
		this(game, newPosition, (byte)0);
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
