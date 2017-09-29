package engine.puzzle;

import engine.Coordinates;

public abstract class SimpleBoard extends Board {
	protected final Block[][] blockMatrix;

	protected SimpleBoard(int newWidth, int newHeight) {
		super(newWidth, newHeight);
		blockMatrix = new Block[newWidth][newHeight];
	}

	protected Block getBlock(Coordinates position) {
		return blockMatrix[position.x()][position.y()];
	}

	@Override
	protected Board landBlock(Block landingBlock) throws PositionOccupiedException {
		if (getBlock(landingBlock.pos) != null) {
			throw new PositionOccupiedException();
		}

		blockMatrix[landingBlock.pos.x()][landingBlock.pos.y()] = landingBlock;
		return this;
	}
}
