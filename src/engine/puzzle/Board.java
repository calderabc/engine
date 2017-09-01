package engine.puzzle;

import engine.Coordinates;
import engine.Part;

/**
 * A Board represents the game element that Blocks collect on after Pieces 
 * containing blocks land. Conceptually it's the main playing field.
 * 
 * @author Aaron Calder
 *
 */
public final class Board extends Part {
	private final PiecePool pieces;
	
	private Piece fallPiece;
	
	private static final int DEFAULT_BOARD_HEIGHT = 20;
	private static final int DEFAULT_BOARD_WIDTH = 10;
	
	private static final Coordinates MAX_POSITION = 
		new Coordinates(DEFAULT_BOARD_WIDTH - 1, DEFAULT_BOARD_HEIGHT - 1);
	
	
	private final int width;
	public int getWidth() {
		return width;
	}
	
	private final int height;
	public int getHeight() {
		return height;
	}
	
	/**
	 * Default Constructor
	 */
	public Board(PieceData newPieceData) {
		this(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, newPieceData);
	}
	
	/**
	 * Constructs a new Board of the specified width and height
	 * in Blocks.
	 * @param newWidth how many Blocks wide to make the Board
	 * @param newHeight how many Blocks high to make the Board
	 */
	private Board(int newWidth, int newHeight, PieceData newPieceData) {
		pieces = new PiecePool(newPieceData);

		width = newWidth;
		height = newHeight;
		
		//TestRow = new ArrayList<TestBlock>(height);
		/*
		for (int i = 0; i < 7; i++) {
			for (Block currBlock : pieces.getPiece(i).getChildren()) {
				currBlock.printInfo();
			}
		}
	*/	
	}
	
	public Piece startNewPiece() {
		fallPiece = pieces.getRandomPiece();
		return fallPiece;
	}
	
	
	/**
	 * Determines which rows on the board are completely full of blocks and returns
	 * a Collection of integers representing the y Position for the full rows.
	 * @return a Collection of integers each representing the y position of 
	 * 		   a row that is full
	 */
	public final void getFullRows(Piece landedPiece) {
	}
	
	/**
	 * If every position in the specified y value's row contains a Block
	 * returns true, otherwise returns false.
	 * @param testY the y position of the row to be checked
	 * @return
	 */
	private final void isRowFull(int testY) {
	}

	
	
	/**
	 * Add all the Blocks that compose the specified Piece to this Board.
	 * @param landingPiece piece containing Blocks to be added to this Board
	 * @return reference to the Board
	 */
	public final void landPiece() {
		
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
	public final boolean doesPieceFit(Piece testPiece) {
		for (Block testBlock : testPiece.getBlocks()) {
			if (!testBlock.pos.isWithin(new Coordinates(0, 0), MAX_POSITION))
				return false;
		}
		return true;
	}
	
	
	
	

	/**
	 * Removes the Blocks from the Board on all the rows specified in 
	 * the Collection of integers.
	 * @param fullRows Collection of integers each representing the y position
	 * 		  of a row to remove all the Blocks from
	 */
	public final void removeRows() {
	}
	
	/**
	 * Removes all the Blocks on the specified row of the Board.
	 * @param rowY the y position of the row to remove
	 */
	private final void removeRow(int rowY) {
		for (int i = 0; i < width; i ++) {
			
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// TODO: finish this for Board with Pieces on
		Block currBlock;
		
	}
	
	/**
	 * If the Block specified is outside the bounds of the board false is
	 * returned, else true is returned. 
	 * @param testBlock Block to test for out of bounds.
	 * @return true if block is outside the allowed bounds of the Board. 
	 * 		   false if the Block is inside the bounds of the board.
	 */
	/*
	private final boolean isBlockOutOfBounds(Block testBlock) {
		synchronized (testBlock) {
			Position testBlockPosition = testBlock.getPosition();
			
			return (testBlockPosition.getY() >= height) 
			        || (testBlockPosition.getX() >= width) 
			        || (testBlockPosition.getX() < 0);
		}
	}
	*/
	
	private final boolean isPieceOutOfBounds(Piece testPiece) {
		// TODO: Make sure this is correct
		return (testPiece.pos.y + testPiece.getHeight() > height)
				|| (testPiece.pos.x + testPiece.getWidth() > width)
				|| (testPiece.pos.x < 0);
	}
	
	
}
