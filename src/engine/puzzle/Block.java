package engine.puzzle;

import engine.Coordinates;
import engine.Debug;
import engine.HasDimension;
import engine.MovablePart;
import engine.Part;

public class Block extends MovablePart<Part<?>> implements HasDimension, Debug {	
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
			return getX() == ((Block) obj).getX() 
				&& getY() == ((Block) obj).getY();
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
		return getX() == other.getX() 
			&& getY() == other.getY();
	}
	
	// Overridden to fulfill hashCode's general contract since equals was also overridden.
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		
		synchronized(this) {
			result = prime * result + getX();
			result = prime * result + getY();
		}
		
		return result;
	}
	
	public final int getType() {
		return type;
	}

	@Override
	public int getHeight() {
		return 1;
	}
	
	@Override
	public int getWidth() {
		return 1;
	}

	@Override
	public int getMaxX() {
		return getX();
	}

	@Override
	public int getMaxY() {
		return getY();
	}

	@Override
	public void printInfo() {
		/*System.out.println("Block - X:" + getX() + " Y:" + getY() + " Type:" + getType());*/
	}
}
