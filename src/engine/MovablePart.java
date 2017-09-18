package engine;

public class MovablePart extends Part implements Movable {
	public Coordinates pos;

	public MovablePart(Coordinates newPosition) {
		pos = new Coordinates(newPosition);
	}
	
	public MovablePart(MovablePart other) {
		super(other);
		pos = new Coordinates(other.pos);
	}

	// Implement the Movable interface.
	@Override
	public final Movable move(Coordinates offset) {
		pos.move(offset);
		
		return this;
	}
}
