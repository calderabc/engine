package tetris.parts;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;

/**
 * A Board represents the game element that Blocks collect on after Pieces 
 * containing blocks land. Conceptually it's the main playing field.
 * 
 * @author Aaron Calder
 *
 */
public final class Board extends Part<MovablePart<?>> {
	private class TestBlock {
		public Block element;
		public MovablePart<Block> parent;
		public boolean below;
	}
	
	//private final Collection<TestBlock> TestRow;
	

	private final Pieces pieces = new Pieces(4); 
	

	
	private Piece fallPiece;
	
	private static final boolean BY_BLOCK = false;
	
	private static final int DEFAULT_BOARD_HEIGHT = 20;
	private static final int DEFAULT_BOARD_WIDTH = 10;
	
	
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
	public Board() {
		this(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT);
	}
	
	/**
	 * Constructs a new Board of the specified width and height
	 * in Blocks.
	 * @param newWidth how many Blocks wide to make the Board
	 * @param newHeight how many Blocks high to make the Board
	 */
	public Board(int newWidth, int newHeight) {
		System.out.println("Board");
		
		width = newWidth;
		height = newHeight;
		
		//TestRow = new ArrayList<TestBlock>(height);
		for (int i = 0; i < 7; i++) {
			for (Block currBlock : pieces.getPiece(i).getChildren()) {
				currBlock.printInfo();
			}
		}
		
	}
	
	public Piece startNewPiece() {
		//System.out.println("Before getRandomPiece()");
		//pieces.printInfo();
		fallPiece = pieces.getRandomPiece();
		//fallPiece = new Piece(4);
		
		getRenderer().add(fallPiece.getRenderer());
		
		//System.out.println("After getRandomPiece()");
		//pieces.printInfo();
		return fallPiece;
	}
	
	
	/**
	 * Determines which rows on the board are completely full of blocks and returns
	 * a Collection of integers representing the y Position for the full rows.
	 * @return a Collection of integers each representing the y position of 
	 * 		   a row that is full
	 */
	public final Collection<Collection<TestBlock>> getFullRows(Piece landedPiece) {
		Collection<Collection<TestBlock>> fullRows = new ArrayList<Collection<TestBlock>>(height);
		
		int landedPieceMinY = landedPiece.getY();
		int landedPieceMaxY = landedPieceMinY + landedPiece.getHeight() - 1;
		
		fullRows = new ArrayList<Collection<TestBlock>>(width);
		for (int i = landedPieceMinY; i <= landedPieceMaxY; i++) {
			// TODO: Finish
		}
		
		for (MovablePart<?> currPart: getChildren()) {
			// TODO: Finish
		}
		
		/*	
		for (int i = 0; i < height; i++) {
			if (isRowFull(i)) {
				fullRows.add(i);
			}
		}
		*/
		return fullRows;
	}
	
	/**
	 * If every position in the specified y value's row contains a Block
	 * returns true, otherwise returns false.
	 * @param testY the y position of the row to be checked
	 * @return
	 */
	private final boolean isRowFull(int testY) {
		int blockCount = 0;
		
		for (int i = 0; i < width; i++) {
			if (isSquareOccupied(new Block(i, testY))) {
				blockCount++;
			}
		}
		
		return blockCount == width;
	}

	
	
	/**
	 * Add all the Blocks that compose the specified Piece to this Board.
	 * @param landingPiece piece containing Blocks to be added to this Board
	 * @return reference to the Board
	 */
	public final Board landPiece() {
		
		if (BY_BLOCK) {
			this.addChildren(fallPiece.getChildren());
			System.out.println(this.getChildren());
			// TODO: make sure this is correct
			getRenderer().remove(fallPiece.getRenderer());
			
			this.removeChild(fallPiece);
		}
		else {
			this.addChild(fallPiece);
		}
			
		return this;
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
		// If test piece is out of bounds of the board it doesn't fit.
		if (isPieceOutOfBounds(testPiece)) {
			return false;
		}
		
		int currPartMinX, currPartMaxX, currPartMinY, currPartMaxY;
		
		// Calculate bounds of test piece.
		int testPieceMinX = testPiece.getX();
		int testPieceMaxX = testPiece.getMaxX();
		int testPieceMinY = testPiece.getY();
		int testPieceMaxY = testPiece.getMaxY();
		
		for (MovablePart<?> currPart: getChildren()) {
			// Calculate bounds of current part.
			currPartMinX = currPart.getX();
			currPartMaxX = currPart.getMaxX();
			currPartMinY = currPart.getY();
			currPartMaxY = currPart.getMaxY();
			
			// Only check individual blocks in the test piece if its 
			// boundaries overlap with the boundaries of the current
			// part.  Use standard algorithm (analogue to checking 
			// two rectangular 2D sprites for overlap)
			// TODO: Reorder/alter these comparisons for efficiency
			// Maybe base order on what sort of move the test piece 
			// has just tried to make.
			if (   currPartMaxX  >= testPieceMinX
				&& testPieceMaxX >= currPartMinX 
				&& currPartMaxY  >= testPieceMinY 
				&& testPieceMaxY >= currPartMinY) {
				
				return (currPart instanceof Piece) 
					? Collections.disjoint(testPiece.getChildren(), 
										   currPart.getChildren())
					: !testPiece.getChildren().contains(currPart);
			/*	
				if (currPartIsPiece) {
					return Collections.disjoint(testPiece.getChildren(), 
											    currPart.getChildren());
				}
				else {
					return !testPiece.getChildren().contains(currPart);
				}
				*/
				
				/*
				Collection<Block> blocksInCurrPart;
				if (currPartIsPiece) {
					blocksInCurrPart = ((Piece) currPart).getChildren();
				}
				else {
					blocksInCurrPart = new Vector<Block>();
					blocksInCurrPart.add((Block) currPart);
				}
				
				for (Block currPartBlock: blocksInCurrPart) {
					if (testPiece.getChildren().contains(currPartBlock)) {
						return false;
					}
				}
				*/
				
				/*
				if (currPartIsPiece) {
					// test if any blocks in testPiece are contained in currPart
					blocksInCurrPart = ((Piece) currPart).getChildren();
					for (Block currTestBlock: testPiece.getChildren()) {
						if (blocksInCurrPart.contains(currTestBlock)) {
							return false;
						}
					}
				} 
				else {
					// test if currPart (a Block) is contained in testPiece
					if (testPiece.getChildren().contains(currPart)) {
						return false;
					}
				}
				*/
			} 
			
		} // end: for (MovablePart<?> currPart: getChildren())
			
		return true;
		
	}
	
	
	
	

	/**
	 * Removes the Blocks from the Board on all the rows specified in 
	 * the Collection of integers.
	 * @param fullRows Collection of integers each representing the y position
	 * 		  of a row to remove all the Blocks from
	 */
	public final int removeRows(Collection<Collection<MovablePart<?>>> fullRows) {
		
		for (Collection<MovablePart<?>> currRow: fullRows) {
			
		}
		
		/*
		for (Integer row : fullRows) {
			removeRow(row);
		}
		*/
		
		return fullRows.size();
	}
/*	
	private final boolean doesBlockOverlap(Block testBlock) {
		return isBlockOutOfBounds(testBlock) || isSquareOccupied(testBlock);
	}
*/
	
	/**
	 * Removes all the Blocks on the specified row of the Board.
	 * @param rowY the y position of the row to remove
	 */
	private final void removeRow(int rowY) {
		for (int i = 0; i < width; i ++) {
			removeChild(new Block(i, rowY));
			
			update();
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// TODO: finish this for Board with Pieces on
		Block currBlock;
		for (MovablePart<?> currChild: getChildren()) {
			if (currChild instanceof Block) {
				currBlock = (Block)currChild;
				if (currBlock.getY() < rowY) {
					currBlock.moveY(1);
				}
			}
		}
		
		update();
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
		return (testPiece.getY() + testPiece.getHeight() > height)
				|| (testPiece.getX() + testPiece.getWidth() > width)
				|| (testPiece.getX() < 0);
	}
	
	/**
	 * Returns true if the Board contains a Block at the same
	 * position as the specified Block else returns false.
	 * @param testBlock Block to be tested. 
	 * @return true if Board already has Block at that position, false otherwise.
	 */
	private final boolean isSquareOccupied(Block testBlock) {
		return getChildren().contains(testBlock);
	}
}
