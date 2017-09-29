package engine.puzzle;

import engine.Coordinates;

public abstract class Board {
	@SuppressWarnings("serial")
	public static class PositionOccupiedException extends Throwable {};
	
	protected final Coordinates dimensions;
	
	protected Board(int newWidth, int newHeight) {
		dimensions = new Coordinates(newWidth, newHeight);
	}
	
	/**
	 * Tests to see if the specified piece fits somewhere on the board.
	 * If none of the Blocks in the piece exceed the bounds of the board 
	 * nor overlap with Blocks already on the Board then true is returned,
	 * else false is returned.
	 * 
	 * @param testPiece Piece to be tested
	 * @return true if the Piece fits somewhere on the board, and false if it does not.
	 */
	public boolean doesPieceFit(Piece testPiece) {
		for (Block testBlock : testPiece.getBlocks()) {
				if (!doesBlockFit(testBlock)) 
					return false;
		}
		return true;
	}

	private boolean doesBlockFit(Block testBlock) {
		try {
			return getBlock(testBlock.pos) == null;
		}
		catch (IndexOutOfBoundsException e) {
			return false;
		}
		// Using try/catch for logic (above) is bad form but I reckon it'll
		// execute faster than the procedural way (below). 
		// TODO: Verify.
	}


	/**
	 * Add all the Blocks that compose the specified Piece to this Board.
	 * @param landingPiece piece containing Blocks to be added to this Board
	 */
	public void landPiece(Piece landingPiece) {
		for (Block landingBlock : landingPiece.getBlocks()) {
			try {
				landBlock(landingBlock);
			} catch (PositionOccupiedException e) {
				e.printStackTrace();
			}

		}
	}

	protected abstract Block getBlock(Coordinates position);

	protected abstract Board landBlock(Block landingBlock) 
		throws PositionOccupiedException;

	public abstract int tryRemoveBlocks();
}
