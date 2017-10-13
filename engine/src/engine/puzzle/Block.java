package engine.puzzle;

import engine.Coordinates;
import engine.Part;
import engine.Visual;

public class Block extends Part {
	public final byte type;

	public Block(Coordinates newCoords, Visual.Id newId) {
		this(newCoords, newId, (byte)0);
	}

	public Block(Coordinates newCoords, Visual.Id newId, byte newType) {
		super(newCoords);
		type = newType;
		initVisual(newId);
	}

	public Block(Block other)
	{
		super(other);
		type = other.type;
	}
}
