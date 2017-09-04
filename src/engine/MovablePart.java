package engine;

public abstract class MovablePart extends Part implements Movable {
	public Coordinates pos;

	/**
	 * Constructs a MovablePart given the specified Coordinates
	 * @param newPosition Coordinates to set the new Movable part to.
	 */
	public MovablePart(Coordinates newPosition) {
		pos = newPosition;
	}
	
	/**
	 * Constructs a MovablePart based on a deep copy of the specified MovablePart
	 * @param other
	 */
	public MovablePart(MovablePart other) {
		super(other);
		pos = new Coordinates(other.pos);
	}

	// Implement the Movable interface.
	
	@Override
	public Movable move(Coordinates offset) {
		pos.move(offset);
		
		return this;
	}
}