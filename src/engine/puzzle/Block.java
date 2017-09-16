package engine.puzzle;

import engine.Coordinates;
import engine.MovablePart;
import engine.puzzle.tetris.TetrisGame;

public class Block extends MovablePart {	
	private final int type;
	private int state;

	public Block(Coordinates newCoords, int newType, int newState) {
		super(newCoords);
		type = newType;
		state = newState;

	}

	public Block(Block other)
	{
		super(other);
		visual = TetrisGame.me.engine.newVisual(other.visual);
		type = other.getType();
		state = other.getState();
	}
	
	public final int getType() {
		return type;
	}
	
	public final void setState(int newState) {
		state = newState;
	}

	public final int getState() {
		return state;
	}
	
	// TODO: Review the following.  Make sure they have purpose.

	public int getHeight() {
		return 1;
	}
	
	public int getWidth() {
		return 1;
	}

	public int getMaxX() {
		return pos.x;
	}

	public int getMaxY() {
		return pos.y;
	}

}
