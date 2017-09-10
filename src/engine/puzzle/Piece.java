package engine.puzzle;

import java.util.List;

import engine.Part;

public abstract class Piece extends Part {
	protected List<Block> blocks;
	
	public Piece() {
		
	}

	public Piece(Piece other) {
		super(other);
	}
	

}
