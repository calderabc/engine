/*
This is a tile-matching puzzle video game engine.
Copyright (C) 2018 Aaron Calder

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
*/

package engine.puzzle;

import engine.Coordinates;
import engine.Field;
import engine.Game;


public abstract class Board extends Field {
	@SuppressWarnings("serial")
	public static class PositionOccupiedException extends Throwable {};
	
	public final Coordinates dimensions;

	protected Board(Coordinates newDimensions) {
		dimensions = newDimensions;
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
		for (Block testBlock : testPiece.blocks) {
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
		// Using try/catch to check if the block is out of bounds is probably
		// bad form but is simpler and may execute faster than explicit checks.
		// TODO: Verify.
	}


	/**
	 * Add all the Blocks that compose the specified Piece to this Board.
	 * @param landingPiece piece containing Blocks to be added to this Board
	 */
	public final int landPiece(Piece landingPiece) {
		for (Block landingBlock : landingPiece.blocks) {
			try {
				landBlock(landingBlock);
			} catch (PositionOccupiedException e) {
				e.printStackTrace();
			}

		}

		return tryRemoveBlocks(landingPiece.blocks);

	}

	protected abstract Block getBlock(Coordinates position);

	protected abstract Board landBlock(Block landingBlock)
		throws PositionOccupiedException;

	protected abstract int tryRemoveBlocks(Block[] blocksJustLanded);

	public int getHeight() {
		return dimensions.y;
	}

	public int getWidth() {
		return dimensions.x;
	}

}
