package engine.puzzle;

import engine.Coordinates;
import engine.MovablePart;
import engine.Visual;

public class Block extends MovablePart {	

	public Block(Coordinates newCoords, Visual.Id newId) {
		super(newCoords);
		visual = PuzzleGame.me.engine.newVisual(this, newId);
	}

	public Block(Block other)
	{
		super(other);
	}
}
