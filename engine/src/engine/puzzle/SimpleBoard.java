package engine.puzzle;

import engine.Coordinates;
import engine.Game;

public abstract class SimpleBoard extends Board {
	protected final Block[][] blockMatrix;

	protected SimpleBoard(Coordinates newDimensions) {
		super(newDimensions);
		blockMatrix = new Block[dimensions.x][dimensions.y];
	}

	protected Block getBlock(Coordinates position) {
		return blockMatrix[position.x][position.y];
	}

	@Override
	protected Board landBlock(Block landingBlock) throws PositionOccupiedException {
		if (getBlock(landingBlock.pos) != null) {
			throw new PositionOccupiedException();
		}

		blockMatrix[landingBlock.pos.x][landingBlock.pos.y] = landingBlock;
		return this;
	}
}
