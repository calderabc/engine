package engine.puzzle;

import engine.Coordinates;
import engine.Game;
import engine.Part;
import engine.Visual;

public class Block extends Part {
	public final byte type;


	public Block(Game game, Coordinates newCoords, Visual.Id newId, byte newType) {
		super(game, newCoords, newId);
		type = newType;
	}

	public Block(Game game, Coordinates newCoords, Visual.Id newId) {
		this(game, newCoords, newId, (byte)0);
	}

	public Block(Block other)
	{
		super(other);
		type = other.type;
	}
}
