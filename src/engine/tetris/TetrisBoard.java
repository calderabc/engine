package engine.tetris;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import engine.Coordinates;
import engine.puzzle.Block;
import engine.puzzle.Board;
import engine.puzzle.Piece;

/**
 * A Board represents the game element that Blocks collect on after Pieces 
 * containing blocks land. Conceptually it's the main playing field.
 * 
 * @author Aaron Calder
 *
 */
public final class TetrisBoard extends Board {
	public final int width;
	public final int height;

	private static final int DEFAULT_BOARD_HEIGHT = 20;
	private static final int DEFAULT_BOARD_WIDTH = 10;
	/*
	private static final Coordinates MAX_POSITION = 
		new Coordinates(DEFAULT_BOARD_WIDTH - 1, DEFAULT_BOARD_HEIGHT - 1);
	*/
	
	private List<Row> blockMatrix = new Vector<Row>(DEFAULT_BOARD_HEIGHT);

	
	private final class Row implements Iterable<Block> {
		public class PositionOccupiedException extends Exception {
			private static final long serialVersionUID = 1L; }

		private Block[] blocks = newRow();
		private int blockCount = 0;

		public void set(int index, Block element) throws PositionOccupiedException {
			if (blocks[index] != null)
				throw new PositionOccupiedException();

			blocks[index] = element;
			blockCount++;
		}
		
		public Block get(int index) {
			if (index < 0 || index >= DEFAULT_BOARD_WIDTH)
				throw new ArrayIndexOutOfBoundsException();

			return blocks[index];
		}
		
		public boolean isFull() {
			// TODO: Should I throw exception is blockCount > DEFAULT_BOARD_WIDTH ?
			return blockCount >= DEFAULT_BOARD_WIDTH;
		}
		
		public void terminate() {
			for (Block terminalBlock : blocks) { 
				terminalBlock.terminate();
			}
			blocks = newRow();
		}
		
		private Block[] newRow() {
			blockCount = 0;
			return new Block[DEFAULT_BOARD_WIDTH];
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
		this(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT);
	}
	
	/**
	 * Constructs a new Board of the specified width and height
	 * in Blocks.
	 * @param newWidth how many Blocks wide to make the Board
	 * @param newHeight how many Blocks high to make the Board
	 */
	private TetrisBoard(int newWidth, int newHeight) {

		for (int i = 0; i < DEFAULT_BOARD_HEIGHT; i++) {
			blockMatrix.add(new Row());
		}

		width = newWidth;
		height = newHeight;
	}
	
	
	/**
	 * Add all the Blocks that compose the specified Piece to this Board.
	 * @param landingPiece piece containing Blocks to be added to this Board
	 */
	@Override
	public void landPiece(Piece landingPiece) {
		for (Block landingBlock : landingPiece.getBlocks()) {
			try {
				blockMatrix.get(landingBlock.pos.y())
						   .set(landingBlock.pos.x(), landingBlock);
			} catch (engine.tetris.TetrisBoard.Row.PositionOccupiedException e) {
				e.printStackTrace();
			}

		}
	}
	
	@Override
	public int tryRemoveBlocks() {
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

	/**
	 * Tests to see if the specified piece fits somewhere on the board.
	 * If none of the Blocks in the piece exceed the bounds of the board 
	 * nor overlap with Blocks already on the Board then true is returned,
	 * else false is returned.
	 * 
	 * @param testPiece Piece to be tested
	 * @return true if the Piece fits somewhere on the board, and false if it does not.
	 */
	@Override
	public boolean doesPieceFit(Piece testPiece) {
		for (Block testBlock : testPiece.getBlocks()) {
			try {
				if (blockMatrix.get(testBlock.pos.y()).get(testBlock.pos.x()) != null)
					return false;
			}
			catch (IndexOutOfBoundsException e) {
				return false;
			}
			// Using try/catch for logic (above) is bad form but I reckon it'll
			// execute faster than the procedural way (below). 
			// TODO: Verify.
			// if (!testBlock.pos.isWithin(new Coordinates(0, 0), MAX_POSITION)
			//     || blockMatrix.get(testBlock.pos.y).get(testBlock.pos.x) != null) {
			//		return false;
			// }
		}
		return true;
	}

}
