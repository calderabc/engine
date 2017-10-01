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


	private void terminateBlocks(Block[] blocksJustLanded,
	                             Set<Block> deathRow,
	                             Block... blocks) {
		for (Block terminalBlock : blocks) {
			deathRow.add(terminalBlock);
			// If this block happens to be part of the same piece there is no reason
			// to check again so take it out of the running.
			for (int i = 0; i < blocksJustLanded.length; i++) {
				if (blocksJustLanded[i] == terminalBlock) {
					blocksJustLanded[i] = null;
				}
			}
		}

	}

	// Remove blocks with three or more of the same type in a row from board.
	@Override
	public int tryRemoveBlocks(Block[] blocksJustLanded) {
		// Use a set so terminal blocks won't be added multiple times.
		Set<Block> deathRow = new HashSet<>();

		for (Block currBlock : blocksJustLanded) {
			if (currBlock == null) continue;

			Block[] candidates = new Block[8];
			// Get adjacent blocks which are of the same type.
			// Add to candidate array in circular order around the block.
			for (int i = 0; i < 8; i++) {
				Block testBlock = null;
				try {
					testBlock = getBlock(
					Coordinates.add(currBlock.pos, aroundCoords[i])
					);
				} catch (ArrayIndexOutOfBoundsException e) {};
				if (testBlock != null && currBlock.type == testBlock.type) {
					candidates[i] = testBlock;
				}
			}
			// If this block is directly between two matching blocks
			// remove all three blocks.
			for (int i = 0; i < 4; i++) {
				if (candidates[i] != null && candidates[i + 4] != null) {
					terminateBlocks(blocksJustLanded,
					                deathRow,
					                candidates[i],
					                candidates[i + 4]);
					deathRow.add(currBlock);
				}
			}
			// If any of the adjacent matching blocks are directly between this
			// block and another matching block remove all three blocks.
			for (int i = 0; i < 8; i++) {
				if (candidates[i] != null) {
					Block testBlock = null;
					try {
						testBlock = getBlock(
						Coordinates.add(candidates[i].pos, aroundCoords[i])
						);
					} catch (ArrayIndexOutOfBoundsException e) {};
				    if (testBlock != null && currBlock.type == testBlock.type) {
				    	terminateBlocks(blocksJustLanded,
					                    deathRow,
					                    candidates[i],
					                    testBlock);
				    	deathRow.add(currBlock);
				    }
				}
			}
			System.out.println("got here");

		}

		for (Block terminalBlock : deathRow) {

			/*
			// Move all blocks directly above this block down a position.
			for (int y = terminalBlock.pos.y; y >= 0; y--) {
				blockMatrix[terminalBlock.pos.x][y].pos.move(
					new Coordinates(0,1)
				);
			}
			*/
			// Remove block from screen.
			terminalBlock.terminate();
			// Remove block from the board;
			blockMatrix[terminalBlock.pos.x][terminalBlock.pos.y] = null;
		}

		return deathRow.size();

	}

}
