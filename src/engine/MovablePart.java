package engine;

public abstract class MovablePart extends Part implements Movable {
	
	/**
	 * Constructs a MovablePart given the specified Coordinates
	 * @param newPosition Coordinates to set the new Movable part to.
	 */
	public MovablePart(Coordinates newPosition) {
		super(newPosition);
		//pos = (Position)newPosition;
	}
	
	/**
	 * Constructs a MovablePart with specified x and y Coordinate values
	 * @param newX
	 * @param newY
	 */
	public MovablePart(int newX, int newY) {
		this(new Coordinates(newX, newY));
	}
	/**
	 * Constructs a MovablePart based on a deep copy of the specified MovablePart
	 * @param other
	 */
	public MovablePart(MovablePart other) {
		super(other);
		//pos = new Position(other.pos);
	}

	// Implement the Movable interface.
	
	@Override
	public Movable move(Coordinates offset) {
		pos.move(offset);
		
		return this;
	}
	
	@Override
	public final Movable move(int offsetX, int offsetY) {
		return move(new Coordinates(offsetX, offsetY));
	}
	
	@Override
	public final Movable move(int offsetX, int offsetY, int offsetZ) {
		return move(new Coordinates(offsetX, offsetY, offsetZ));
	}
}
