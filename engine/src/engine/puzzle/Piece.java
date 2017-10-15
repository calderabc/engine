package engine.puzzle;

import java.lang.reflect.InvocationTargetException;

import engine.Coordinates;
import engine.Game;
import engine.Reflection;


public abstract class Piece implements Cloneable {
	protected final Block[] blocks;
	
	protected Piece(int newBlockCount) {
		blocks = new Block[newBlockCount];	
	}
	
	public Piece(Piece other) {
		Block[] otherBlocks = other.getBlocks();
		blocks = new Block[otherBlocks.length];
		int i = 0;
		for (Block currOtherBlock: other.getBlocks()) {
			blocks[i++] = new Block(currOtherBlock);
		}
	}
	
	// TODO: Shallow copy.  Make sure anything which calls this
	// doesn't alter 'blocks' or makes a deep copy before alteration.
	public Block[] getBlocks() {
		return blocks;
	}

	public final int getBlockCount() {
		return blocks.length;
	}

	public final Piece newPiece() {
		return (Piece)Reflection.newInstance(
			getClass(),
			new Reflection.ClassAndObject(Game.class, blocks[0].game)
		);
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

	protected Piece move(Coordinates offset) {
		for (Block currBlock: blocks) {
			currBlock.move(offset);
		}
		return this;
	}
	
	protected abstract Piece rotate(Coordinates offset);
		

	public Piece move(PieceAction action) {
		// TODO: Maybe lambda/method-reference this.
		return (action.type == PieceAction.Type.MOVE)
			? move(action.offset)
			: rotate(action.offset); 
	}
}
