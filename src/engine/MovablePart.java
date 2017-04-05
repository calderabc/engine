package engine;

public abstract class MovablePart<T extends Part<?>> extends StaticPart<T> implements Movable, HasDimension {
	

	
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
		super(newX, newY);
	}
	/**
	 * Constructs a MovablePart based on a deep copy of the specified MovablePart
	 * @param other
	 */
	public MovablePart(StaticPart<T> other) {
		super(other);
		//pos = new Position(other.pos);
	}

	// Implement the Movable interface.
	
	@Override
	public final MovablePart<T> move(Coordinates offset) {
		getPosition().move(offset);
		
		update();
		
		return this;
	}
	
	@Override
	public MovablePart<T> move(int offsetX, int offsetY) {
		getPosition().move(offsetX, offsetY);
		
		update();
		
		return this;
	}
	
	@Override
	public final MovablePart<T> move(int offsetX, int offsetY, int offsetZ) {
		getPosition().move(offsetX, offsetY, offsetZ);
		
		update();
		
		return this;
	}

	@Override
	public final MovablePart<T> moveX(int offsetX) {
		getPosition().moveX(offsetX);
		
		update();
		
		return this;
	}

	@Override
	public final MovablePart<T> moveY(int offsetY) {
		getPosition().moveY(offsetY);
		
		update();
		
		return this;
	}
	
	@Override
	public MovablePart<T> moveZ(int offsetZ) {
		getPosition().moveZ(offsetZ);
		
		update();
		
		return this;
	}
}
