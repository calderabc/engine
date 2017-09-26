package engine.puzzle;

import engine.Coordinates;
import engine.Part;
import engine.Visual;

public class Block extends Part {	

	public Block(Coordinates newCoords, Visual.Id newId) {
		super(newCoords);
		initVisual(newId);
	}

	public Block(Block other)
	{
		super(other);
	}
}
