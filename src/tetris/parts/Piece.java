package tetris.parts;

import java.util.Vector;
import java.util.Collection;

import engine.PieceAction;
import tetris.Debug;
import tetris.coordinates.Movable;

/**
 * This Piece class represents a Tetris piece.  It is composed of Blocks.  It has
 *   
 * @author Aaron Calder
 *
 */

public class Piece extends MovablePart<Block> implements Movable, Debug {
	private final static int X = 0;
	private final static int Y = 1;
	
	
	
	public static final int[][][] pieceTemplate = {{{0, 0}, {1, 0}, {0, 1}, {1, 1}},
			 							           {{0, 0}, {1, 0}, {2, 0}, {1, 1}},
			 							           {{0, 0}, {1, 0}, {2, 0}, {2, 1}},
			 							           {{0, 0}, {1, 0}, {2, 0}, {0, 1}},
			 							           {{0, 0}, {1, 0}, {2, 0}, {3, 0}},
			 							           {{0, 0}, {1, 0}, {2, 1}, {1, 1}},
			 							           {{0, 1}, {1, 0}, {2, 0}, {1, 1}}};
			 							           
	public static final double[][] pieceCenter = {{0.5, 0.5, 0.5, 0.5},
												  {  1,   0,   1,   0},
												  {  1,   0,   1,   0},
												  {  1,   0,   1,   0}, 
												  {1.5,   0,   1,-0.5},
												  {  1, 0.5, 1.5,   0},
												  {  1, 0.5, 0.5,   0}}; 


	
	//public static int count = 0;
	
	
	private final int blockCount;
	public final int getBlockCount() {
		return blockCount;
	}
	
//*****************************************************************************
	
	
	private final int id;
	public int getID() {
		return id;
	}
	
	//private Coordinates centerOfMass; 
	//private Coordinates otherCenterOfMass;

	public double[] currCenter = new double[2];
	public  double[] destCenter = new double[2];
	
	private int width;
	private int height;
	
	/**
	 * Default Constructor.  Will have better features when I get around to it.
	 */
	public Piece(int newBlockCount, int newID) {
		super(3, 0);
		//System.out.println("Piece");
		
		blockCount = newBlockCount;
		id = newID;

		generatePiece(id);
		generateDimensions();
		
			
		
		//newRenderer();
		init();
	}
	
	public Piece(Piece other) {
		super(other);
		setVisible(true);
		
		blockCount = other.getBlockCount();
		id = other.getID();
		
		currCenter[X] = other.currCenter[X];
		currCenter[Y] = other.currCenter[Y];
		destCenter[X] = other.destCenter[X];
		destCenter[Y] = other.destCenter[Y];
		
		for (Block currOtherBlock: other.getBlocks()) {
		//System.out.println("Construct me");
			addChild(currOtherBlock);
			//addChild(currOtherBlock.move(-getPosition().getX(), -getPosition().getY()));
		}
		
		
		generateDimensions();
		init();
	}
	
	private final void generatePiece(int pieceID) {
		for (int[] blockXY: pieceTemplate[pieceID]) {
			addChild(new Block(blockXY[0], blockXY[1], pieceID));
		}
		
		
		currCenter[X] = pieceCenter[pieceID][0] + getX();
		currCenter[Y] = pieceCenter[pieceID][1] + getY();
		destCenter[X] = pieceCenter[pieceID][2] + getX();
		destCenter[Y] = pieceCenter[pieceID][3] + getY();
	
		for (Block currBlock: this.getChildren()) {
			if (currBlock instanceof Block) {
				currBlock.move(getPosition());
			}
		}
		//setCenterOfMass();
	}
	
	private final void generateDimensions() {

		int currX, currY;
		int minX = 100, minY = 100, maxX = 0, maxY = 0;
		
		for(Block currBlock: getChildren()) {
			
			currX = currBlock.getX();
			if (currX > maxX) maxX = currX;
			if (currX < minX) minX = currX;
			
			currY = currBlock.getY();
			if (currY > maxY) maxY = currY;
			if (currY < minY) minY = currY;
		}
		
		this.setPosition(minX, minY);
		width = maxX - minX + 1;
		height = maxY - minY + 1;
	}
	
	
	/**
	 * The description below does not match the method.  The method returns the actual
	 * coordinates.  If board coordinates are added they do not appear to be added
	 * here.  06/06/2013
	 * 
	 * Return a collection of blocks whose coordinates are absolute and correspond
	 * with the coordinates on the board.
	 * @return Collection of Blocks with their coordinates adjusted to match 
	 * 		   board coordinates.
	 */
	public Collection<Block> getBlocks() {
		Collection<Block> returnBlocks = new Vector<Block>();

		synchronized(this) {
			for (Block currBlock : getChildren()) {
				returnBlocks.add(new Block(currBlock));
			}
			return returnBlocks;
		}
	}
	
	/*
	private Collection<Block> getBlocksRef() {
		
		for (Part currBlock : getChildren()) {
			if (currBlock instanceof Block) {
		
				returnBlocks.add((Block) currBlock);
			}
		}
		
		
		return returnBlocks;
		
	}
	*/
	
	/*	
	 * origin = Coordinates where x=0 and y=0
	 * Formula to rotate cartesian coordinates around the origin, 
	 * by a specified angle:
	 * newX = x*cos(angle) - y*sin(angle)
	 * newY = x*sin(angle) + y*cos(angle)
	 * 
	 * To rotate counter clockwise 90 degrees the angle = 90
	 * Given that sin(90) = 1 and cos(90) = 0 the result is:
	 * newX = -y
	 * newY = x
	 * 
	 * To rotate clockwise 90 degrees the angle = -90 = 270
	 * Given that sin(270) = -1 and cos(270) = 0 the result is:
	 * newX = y 
	 * newY = -x
	 * 
	 * So the formula for rotating blocks clockwise around the origin is the 
	 * same as the formula for rotating counter-clockwise with the results 
	 * multiplied by -1.  This is the purpose of the 'flip' variable.
	 * 
	 * 
	 * Once the blocks' coordinates are rotated around the origin the coordinates
	 * need to be adjusted so the blocks appear to have rotated around the 
	 * center of the piece and not the origin.  This is the purpose of AdjustX and AdjustY.
	 * These values are acquired by subtracting the coordinates of where the 
	 * center of the piece is supposed to be after the rotation (destCenter) 
	 * from the coordinates of the current center of the piece (currCenter) 
	 * once rotated around the origin.  
	 * 
	 * The blocks are supposed to rotate around the center of the piece, 
	 * wherever it may be, and not the origin.  By rotating the blocks 
	 * around the origin the center of piece has also rotated around the 
	 * origin and the whole piece appears to have moved position in addition 
	 * to rotating.  To adjust the blocks, so they appear to have rotated 
	 * around the center of the piece, the difference, in the coordinates of 
	 * the intended piece center after rotation (destCenter) and the 
	 * coordinates of the current piece center (currCenter) once it's been 
	 * rotated around the origin, has to be added to the rotated blocks.  
	 * This is the purpose of AdjustX and AdjustY.
	 */
	
	public final synchronized Piece rotate(PieceAction action) {
		int flip = (action == PieceAction.CLOCKWISE) ? -1: 1;
		
		// Calculate the X and Y distances the current center would have to
		// be moved, once rotated about (0, 0), to make it equal
		// to the position of the destination center.
		// '+ flip * currCenter' is equivalent to '- (-flip * currCenter[X])';
		int adjustX = (int) Math.round(destCenter[X] - flip * currCenter[Y]);
		int adjustY = (int) Math.round(destCenter[Y] + flip * currCenter[X]);
		System.out.println("Rotate debug start");	
		// Move the Blocks in the piece to their new positions.
		for(Block currBlock: getChildren()) {
				currBlock.printInfo();
				System.out.println("adjustX:" + adjustX + " adjustY:" + adjustY + " flip:" + flip + " x:" + currBlock.getX() + " y:" + currBlock.getY());
				currBlock.setPosition(
					flip * currBlock.getY() + adjustX, 
					-flip * currBlock.getX() + adjustY);
				currBlock.printInfo();
				System.out.println("adjustX:" + adjustX + " adjustY:" + adjustY + " flip:" + flip + " x:" + currBlock.getX() + " y:" + currBlock.getY());
						
		}
		System.out.println("Rotate debug end");
		this.generateDimensions();
		
		// swap values of current center and destination center
		double[] dummy = destCenter;
		destCenter = currCenter;
		currCenter = dummy;
		
		
		init();
		update();
		
		return this;
	}
	
/*	
	private void setCenterOfMass() {
		int block_count = 0;
		int totalX = 0, totalY = 0;
		
		for (Block currBlock: getBlocks()) {
			totalX += currBlock.getX();
			totalY += currBlock.getY();
			
			block_count++;
		}
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		System.out.println(((double)totalX) / block_count);
		System.out.println(((double)totalY) / block_count);
		//centerOfMass = new Coordinates(totalX / block_count, totalY / block_count);
	}
*/
	

	@Override
	public MovablePart<Block> move(int offsetX, int offsetY) {
		super.move(offsetX, offsetY);
		
		System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
		currCenter[X] += offsetX;
		currCenter[Y] += offsetY;
		destCenter[X] += offsetX;
		destCenter[Y] += offsetY;
		
		for (Block currBlock: getChildren()) {
			currBlock.move(offsetX, offsetY);
		}
		this.printInfo();
		
		return this;
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getMaxX() {
		return getX() + width - 1;
	}
	
	@Override
	public int getMaxY() {
		return getY() + height - 1;
	}

	/*
	@Override
	public boolean equals(Object obj) {
		//System.out.println("***************** hello ********************************");
		if (!(obj instanceof Block || obj instanceof Piece)) 
			return false;
		
		for (Block currChild: getBlocks()) {
			if (currChild.equals(obj)) {
				return true;
			}
		}
		
		return false;
	}
	*/
	
	@Override
	public void printInfo() {
		System.out.println("Piece - Start");
		for(Block currBlock : this.getChildren()) {
			currBlock.printInfo();
		}
		System.out.println("Piece - End");
	}

	
}
