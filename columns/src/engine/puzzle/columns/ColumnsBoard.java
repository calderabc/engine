package engine.puzzle.columns;

import engine.Coordinates;
import engine.Game;
import engine.puzzle.Block;
import engine.puzzle.SimpleBoard;

import java.util.*;

public class ColumnsBoard extends SimpleBoard {
	// Coordinate offsets for scanning around a block position, in a circular order.
	private static final Coordinates[] aroundCoords = {new Coordinates(-1, 1),
	                                                   new Coordinates( 0, 1),
	                                                   new Coordinates( 1, 1),
	                                                   new Coordinates( 1, 0)};

	public ColumnsBoard(Game game) {
		super(game, 6, 13);
	}


	private Block getBlockIfSameType(Block block, Coordinates offset) {
		return null;
	}

	private Block getMatchingBlock(Block block, Coordinates offset) {
		Block testBlock = null;
		try {
			testBlock = getBlock(Coordinates.add(block.pos, offset));
		} catch (ArrayIndexOutOfBoundsException e) {};
		if (testBlock != null && block.type == testBlock.type) {
			return testBlock;
		}
		return null;
	}

	private Block checkAhead(Block currBlock,
	                         Set<Block> deathRow,
	                         Coordinates offset) {
		Block nearBlock, farBlock;
		if ((nearBlock = getMatchingBlock(currBlock, offset)) != null) {
			if ((farBlock = getMatchingBlock(nearBlock, offset)) != null) {
				deathRow.add(currBlock);
				deathRow.add(nearBlock);
				deathRow.add(farBlock);
			}
		}
		return nearBlock;
	}

	// Remove blocks which form straight lines of 3 or more of the same
	// block type in a row vertically, diagnally, or horizontally.
	@Override
	public int tryRemoveBlocks(Block[] blocksJustLanded) {
		if (blocksJustLanded.length == 0) return 0; // Nothing (more) to do.
		// Use a Set so terminal blocks won't be added multiple times.
		Set<Block> deathRow = new HashSet<>();
		for (Block currBlock : blocksJustLanded) {
			for (int i = 0; i < 4; i++) {
				Block forwards = checkAhead(currBlock,
				                            deathRow,
				                            aroundCoords[i]);
				Block backwards =
					checkAhead(currBlock,
					           deathRow,
					           Coordinates.inversion(aroundCoords[i]));

				if (forwards != null && backwards != null) {
					deathRow.add(forwards);
					deathRow.add(currBlock);
					deathRow.add(backwards);
				}
			}
		}

		Set<Block> theFallen = new HashSet();
		for (Block terminalBlock : deathRow) {
			int x = terminalBlock.pos.x;
			// Move all blocks directly above this block down a position.
			for (int y = terminalBlock.pos.y - 1; y >= 0; y--) {
				Block aBlockAbove = blockMatrix[x][y];
				blockMatrix[x][y + 1] = aBlockAbove;
				if (aBlockAbove != null) {
					theFallen.add(aBlockAbove);
					aBlockAbove.move(new Coordinates(0, 1));
					aBlockAbove.visual.update(aBlockAbove);
				}
			}
			// Be sure tile at top of column is empty.
			blockMatrix[x][0] = null;
			// Remove block from screen.
			terminalBlock.terminate(game);
		}
		// Now call block removal for all the blocks which were above and fell.
		// TODO: Add mechanism for scoring chain moves.
		return deathRow.size() + tryRemoveBlocks(theFallen.toArray(new Block[0]));
	}

}
