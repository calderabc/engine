package engine.puzzle;

import engine.Coordinates;
import engine.MovablePart;
import engine.Part;

public class Block extends MovablePart {	
	private final int type;

	public Block(Coordinates newCoords, int newType) {
		super(newCoords);
		type = newType;
		
	}
	
	public Block(int newX, int newY) {
		super(newX, newY);
		type = 0;
	}
	
	public Block(int newX, int newY, int newType)
	{
		super(newX, newY);
		type = newType;
		//System.out.println("Block");
		
	}
	
	public Block(Block other)
	{
		super(other);
		type = other.getType();
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Block)) {
			return false;
		}
		else {
			return pos.x == ((Block) obj).pos.x 
				&& pos.y == ((Block) obj).pos.y;
		}
	}
	
	/*
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		
		if (obj instanceof Block) {
			result = isEqual((Block) obj);
		}
		else if (obj instanceof Piece) {
			for(Block currBlock: ((Piece) obj).getBlocks()) {
				if (isEqual((Block) currBlock)) {
					result = true;
					break;
				}
			}
		}
		
		return result;
	}
	*/
	
	private boolean isEqual(Block other) {
		return pos.x == other.pos.x 
			&& pos.y == other.pos.y;
	}
	
	// Overridden to fulfill hashCode's general contract since equals was also overridden.
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		
		synchronized(this) {
			result = prime * result + pos.x;
			result = prime * result + pos.y;
		}
		
		return result;
	}
	
	public final int getType() {
		return type;
	}

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
