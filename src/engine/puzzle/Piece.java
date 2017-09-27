package engine.puzzle;

import java.util.List;
import java.util.Vector;

import engine.puzzle.tetris.swing.PieceAction;


public abstract class Piece {
	protected List<Block> blocks;
	
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
	
	public List<Block> getBlocks() {
		return blocks;
	}

	public final int getBlockCount() {
		return blocks.size();
	}

	public void updateVisual() {
		for (Block block : blocks) {
			block.visual.update(block);
		}
	}

	public abstract Piece move(PieceAction action);
}
