package engine.puzzle;

import java.util.Vector;

import engine.Coordinates;
import engine.Movable;
import engine.MovablePart;

import java.util.Collection;

/**
 * This Piece class represents a Tetris piece.  It is composed of Blocks.  It has
 *
 * @author Aaron Calder
 *
 */

public class Piece extends MovablePart<Block> {

	private final int blockCount;
	public final int getBlockCount() {
		return blockCount;
	}

//*****************************************************************************


	private final int id;
	public int getID() {
		return id;
	}

	public Coordinates currCenter = null;
	public  Coordinates destCenter = null;

	private int width;
	private int height;

	/**
	 * Default Constructor.  Will have better features when I get around to it.
	 */
	public Piece(int newID, PieceData newPieceData) {
		super(newPieceData.pieceStartPos);

		id = newID;
		blockCount = newPieceData.pieceTemplate[id].length;
		
		generatePiece(newPieceData, id);
		generateDimensions();
	}

	public Piece(Piece other) {
		super(other);
		setVisible(true);

		blockCount = other.getBlockCount();
		id = other.getID();

		currCenter = new Coordinates(other.currCenter);
		destCenter = new Coordinates(other.destCenter);

		for (Block currOtherBlock: other.getBlocks()) {
		//System.out.println("Construct me");
			addChild(currOtherBlock);
			//addChild(currOtherBlock.move(-getPosition().getX(), -getPosition().getY()));
		}

		generateDimensions();
	}

	private final void generatePiece(PieceData newPieceData, int pieceID) {
		for (int[] blockXY: newPieceData.pieceTemplate[pieceID]) {
			addChild(new Block(blockXY[0], blockXY[1], id));
		}

		// Everything in represented multiplied by two, this is why the 'pos' 
		// variables are doubled to match the already doubled 'pieceCenter'.
		int posXDoubled = pos.x << 1, posYDoubled = pos.y << 1;
		currCenter = newPieceData.getCurrCenter(pieceID).move(posXDoubled, posYDoubled);
		destCenter = newPieceData.getDestCenter(pieceID).move(posXDoubled, posYDoubled);

		for (Block currBlock: this.getChildren()) {
			if (currBlock instanceof Block) {
				currBlock.move(pos);
			}
		}
	}

	private final void generateDimensions() {

		int currX, currY;
		int minX = 100, minY = 100, maxX = 0, maxY = 0;

		for(Block currBlock: getChildren()) {

			currX = currBlock.pos.x;
			if (currX > maxX) maxX = currX;
			if (currX < minX) minX = currX;

			currY = currBlock.pos.y;
			if (currY > maxY) maxY = currY;
			if (currY < minY) minY = currY;
		}

		pos = new Coordinates(minX, minY);
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

	/**
	 * Rotate the blocks in a piece. 
	 * 
	 * The equation for determining the x and y values after rotating blocks in
	 * a piece starts with the standard formula for rotating a point about the 
	 * origin by an angle:
	 * newX = x*cos(angle) - y*sin(angle)
	 * newY = x*sin(angle) + y*cos(angle)
	 * 
	 * This is synonymous with affine transform matrix for rotation.
	 * The matrix and above formula are derived by adding the rotation angle to 
	 * the original angle of the point both (relative to the origin) and using
	 * the angle sum and difference identities for sine and cosine. 
	 * More info: google 'rotation matrix' and 'trigonometric identities'.
	 * 
	 * The y coordinates for positioning blocks are inverted from the standard 
	 * cartesian plane (like Java coordinates).  So positive angles rotate in 
	 * the clockwise direction instead of counter-clockwise. 
	 * 
	 * So to rotate block clockwise one quadrant the formula is applied with 
	 * the angle value of 90 degrees:
	 * newX = x*cos(90) - y*sin(90) = x*0 - y*1 = -y
	 * newY = x*sin(90) + y*cos(90) = x*1 - y*0 = x
	 *
	 * For counter-clockwise -90 degrees:
	 * newX = x*cos(-90) - y*sin(-90) = x*0 - y*(-1) = y
	 * newY = x*sin(-90) + y*cos(-90) = x*(-1) + y*0 = -x
	 *
	 * So the formula for rotating blocks clockwise around the origin is the
	 * same as the formula for rotating counter-clockwise with the results
	 * multiplied by -1.  This is the purpose of the 'flip' variable.
	 *
	 * TODO: Explain better.
	 * The blocks are supposed to rotate around the center of the piece,
	 * wherever it may be, and not the origin.  By rotating the blocks
	 * around the origin the center of piece has also rotated around the
	 * origin and the whole piece appears to have moved position in addition
	 * to rotating.  To adjust the blocks, so they appear to have rotated
	 * around the center of the piece, the difference, in the coordinates of
	 * the intended piece center after rotation (destCenter) and the
	 * coordinates of the current piece center (currCenter) once it's been
	 * rotated around the origin, has to be added to the rotated blocks.
	 * 
	 * The "Center" values are all represented as twice their actual values.
	 * This is to allow for half fractional values (_.5) to be represented 
	 * as integers (to avoid the use of floating point values).  The original
	 * position values are multiplied by two to match for the calculation 
	 * then the results are divided by two to yield the correct position values.
	 */

	public final synchronized Piece rotate(PieceAction action) {
		int flip = (action == PieceAction.CLOCKWISE) ? -1: 1;

		// Move the Blocks in the piece to their new positions.
		for(Block currBlock: getChildren()) {
				/* Equivalent but slower:
				currBlock.pos = new Coordinates( (destCenter[X] + flip * (2 * currBlock.pos.y - currCenter[Y])) / 2,
				                                 (destCenter[Y] - flip * (2 * currBlock.pos.x - currCenter[X])) / 2 ); */
				currBlock.pos = new Coordinates( destCenter.x + flip * ((currBlock.pos.y << 1) - currCenter.y) >> 1,
				                                 destCenter.y - flip * ((currBlock.pos.x << 1) - currCenter.x) >> 1 );
		}
		this.generateDimensions();

		// swap values of current center and destination center
		Coordinates dummy = destCenter;
		destCenter = currCenter;
		currCenter = dummy;

		update();

		return this;
	}

	@Override
	public MovablePart<Block> move(Coordinates offset) {
		super.move(offset);

		//System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");

		Coordinates offsetDoubled = new Coordinates(offset.x << 1, offset.y << 1);
		currCenter.move(offsetDoubled);
		destCenter.move(offsetDoubled);

		for (Block currBlock: getChildren()) {
			currBlock.move(offset);
		}
		//this.printInfo();

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
		return pos.x + width - 1;
	}

	@Override
	public int getMaxY() {
		return pos.y + height - 1;
	}

	/*
	@Override
	public void printInfo() {
		System.out.println("Piece - Start");
		for(Block currBlock : this.getChildren()) {
			currBlock.printInfo();
		}
		System.out.println("Piece - End");
	}
	*/
}
