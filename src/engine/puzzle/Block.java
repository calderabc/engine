package engine.puzzle;

import engine.Coordinates;
import engine.MovablePart;
import engine.Visual;
import engine.puzzle.tetris.TetrisGame;

public class Block extends MovablePart {	

	public Block(Coordinates newCoords, Visual.Id newId) {
		super(newCoords);
		visual = TetrisGame.me.engine.newVisual(this, newId);

	}

	public Block(Block other)
	{
		super(other);
		visual = TetrisGame.me.engine.newVisual(other.visual);
	}
	
}
