package engine.puzzle;

import engine.Coordinates;
import engine.Game;
import engine.Part;
import engine.Visual;
import engine.graphics2d.ImageType;

public class Block extends Part {	
	public final byte type;

	public Block(Coordinates newCoords, Visual.Id newId) {
		this(newCoords, newId, (byte)0);
	}

	public Block(Coordinates newCoords, Visual.Id newId, byte newType) {
		super(newCoords);
		initVisual(((PuzzleGame)Game.me).blockImageType, newId);
		type = newType;
	}

	public Block(Block other)
	{
		super(other);
		type = other.type;
	}
}
