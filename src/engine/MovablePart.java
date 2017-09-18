package engine;

import engine.puzzle.PuzzleGame;

public class MovablePart extends Part implements Movable {
	public Coordinates pos;

	public MovablePart(Coordinates newPosition, Visual.Id newId) {
		pos = new Coordinates(newPosition);
		visual = PuzzleGame.me.engine.newVisual(this, newId);
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
