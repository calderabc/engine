package engine.puzzle;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Vector;


public abstract class Piece implements Cloneable {
	protected final List<Block> blocks;
	
	protected Piece(int newBlockCount) {
		blocks = new Vector<Block>(newBlockCount);	
	}
	
	public Piece(Piece other) {
		List<Block> otherBlocks = other.getBlocks();
		blocks = new Vector<Block>(otherBlocks.size());
		for (Block currOtherBlock: other.getBlocks()) {
			blocks.add(new Block(currOtherBlock));
		}
	}
	
	// TODO: Shallow copy.  Make sure anything which calls this
	// doesn't alter 'blocks' or makes a deep copy before alteration.
	public List<Block> getBlocks() {
		return blocks;
	}

	public final int getBlockCount() {
		return blocks.size();
	}

	public final Piece newPiece() {
		try {
			return getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException 
		         | IllegalArgumentException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected Piece clone() {
			Class<? extends Piece> myClass = this.getClass();
			try {
				return myClass.getConstructor(myClass).newInstance(this);
			} catch (InstantiationException | IllegalAccessException 
			         | IllegalArgumentException | InvocationTargetException 
			         | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		return null;
	}

	public void updateVisual() {
		for (Block block : blocks) {
			block.visual.update(block);
		}
	}


	public abstract Piece move(PieceAction action);
}
