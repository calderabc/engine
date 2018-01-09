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

package engine.puzzle.tetris;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import engine.Coordinates;
import engine.puzzle.Block;
import engine.puzzle.Board;

/**
 * A Board represents the game element that Blocks collect on after Pieces 
 * containing blocks land. Conceptually it's the main playing field.
 * 
 * @author Aaron Calder
 *
 */
public final class TetrisBoard extends Board {

	private static final int DEFAULT_BOARD_HEIGHT = 20;
	private static final int width = 10;

	private List<Row> blockMatrix = new Vector<Row>(DEFAULT_BOARD_HEIGHT);

	
	private final class Row implements Iterable<Block> {
		private Block[] blocks = newRow();
		private int blockCount = 0;

		public void set(int index, Block element) throws PositionOccupiedException {
			if (blocks[index] != null)
				throw new PositionOccupiedException();

			blocks[index] = element;
			blockCount++;
		}
		
		public Block get(int index) {
			if (index < 0 || index >= width)
				throw new ArrayIndexOutOfBoundsException();

			return blocks[index];
		}
		
		public boolean isFull() {
			// TODO: Should I throw exception is blockCount > DEFAULT_BOARD_WIDTH ?
			return blockCount >= width;
		}
		
		public void terminate() {
			for (Block terminalBlock : blocks) { 
				terminalBlock.terminate();
			}
			blocks = newRow();
		}
		
		private Block[] newRow() {
			blockCount = 0;
			return new Block[width];
		}
		
		@Override
		public Iterator<Block> iterator() {
			return Arrays.asList(blocks).iterator();
		}
	}
	
	
	/**
	 * Default Constructor
	 */
	public TetrisBoard() {
		super(new Coordinates(10, 20));

		blockMatrix = new Vector<Row>();
		for (int i = 0; i < dimensions.y; i++) {
			blockMatrix.add(new Row());
		}
	}
	
	@Override
	protected int tryRemoveBlocks(Block[] blocksJustAdded) {
		Collection<Row> terminalRows = getFullRows();
		int count = terminalRows.size();
		
		for (Row terminalRow : terminalRows) {
			terminalRow.terminate();
			
			for (int i = 0; i < blockMatrix.indexOf(terminalRow); i++) {
				for (Block block : blockMatrix.get(i)) {
					if (block != null) {
						block.move(new Coordinates(0, 1));
						block.visual.update(block);
					}
				}
			}

			blockMatrix.remove(terminalRow);
			blockMatrix.add(0, new Row());
		}

		return count;
	}
	
	private Collection<Row> getFullRows() {
		Collection<Row> fullRows = new Vector<Row>();
		for(Row testRow : blockMatrix) {
			if (testRow.isFull()) {
				fullRows.add(testRow);
			}
		}
		
		return fullRows;
	}
	
	@Override
	public Block getBlock(Coordinates position) {
		return blockMatrix.get(position.y).get(position.x);
	}

	@Override
	public Board landBlock(Block landingBlock) 
		throws PositionOccupiedException {
		blockMatrix.get(landingBlock.pos.y)
				   .set(landingBlock.pos.x, landingBlock);
		return this;
	}
}
