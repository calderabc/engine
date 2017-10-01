package engine.columns;

import engine.Coordinates;
import engine.puzzle.Block;
import engine.puzzle.SimpleBoard;

import java.util.*;

public class ColumnsBoard extends SimpleBoard {
	// Coordinate offsets for scanning around a block position, in a circular order.
	private static final Coordinates[] aroundCoords = {new Coordinates(-1, 1),
	                                                   new Coordinates( 0, 1),
	                                                   new Coordinates( 1, 1),
	                                                   new Coordinates( 1, 0),
	                                                   new Coordinates( 1,-1),
	                                                   new Coordinates( 0,-1),
	                                                   new Coordinates(-1,-1),
	                                                   new Coordinates(-1, 0)};

	public ColumnsBoard() {
		super(6, 13);
	}


	private Block getBlockIfSameType(Block block, Coordinates offset) {
		return null;
	}


	// Remove blocks which form straight lines of 3 or more of the same
	// block type in a row vertically, diagnally, or horizontally.
	@Override
	public int tryRemoveBlocks(Block[] blocksJustLanded) {
		if (blocksJustLanded.length == 0) return 0; // Nothing (more) to do.

		// Used a Set so terminal blocks won't be added multiple times.
		Set<Block> deathRow = new HashSet<>();

		for (Block currBlock : blocksJustLanded) {
			if (currBlock == null) continue;

			// TODO: Surely this algorithm can be made more efficient.
			// Combine 2 or all of the for loops.

			Block[] suspects = new Block[8];
			// Get adjacent blocks which are of the same type.
			// Add to suspects array in circular order around the block.
			for (int i = 0; i < 8; i++) {
				Block testBlock = null;
				try {
					testBlock = getBlock(
						Coordinates.add(currBlock.pos, aroundCoords[i])
					);
				} catch (ArrayIndexOutOfBoundsException e) {};
				if (testBlock != null && currBlock.type == testBlock.type) {
					suspects[i] = testBlock;
				}
			}
			// If this block is directly between two matching blocks
			// remove all three blocks.
			for (int i = 0; i < 4; i++) {
				if (suspects[i] != null && suspects[i + 4] != null) {
					deathRow.add(suspects[i + 4]);
					deathRow.add(currBlock);
					deathRow.add(suspects[i]);
				}
			}
			// If any of the adjacent matching blocks are directly between this
			// block and another matching block remove all three blocks.
			for (int i = 0; i < 8; i++) {
				if (suspects[i] != null) {
					Block testBlock = null;
					try {
						testBlock = getBlock(
						Coordinates.add(suspects[i].pos, aroundCoords[i])
						);
					} catch (ArrayIndexOutOfBoundsException e) {};
				    if (testBlock != null && currBlock.type == testBlock.type) {
				    	deathRow.add(currBlock);
					    deathRow.add(suspects[i]);
					    deathRow.add(testBlock);
				    }
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
			terminalBlock.terminate();
		}
		// Now call block removal for all the blocks which were above and fell.
		// TODO: Add mechanism for scoring chain moves.
		return deathRow.size() + tryRemoveBlocks(theFallen.toArray(new Block[0]));
	}

}
