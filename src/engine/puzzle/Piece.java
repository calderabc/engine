package engine.puzzle;

import java.util.Vector;

import engine.Coordinates;
import engine.HasDimension;
import engine.Movable;
import engine.MovablePart;
import engine.Part;
import engine.Visual;
import engine.puzzle.tetris.swing.TetrisSprite;

import java.util.Collection;
import java.util.List;

/**
 * This Piece class represents a Tetris piece.  It is composed of Blocks.  It has
 *
 * @author Aaron Calder
 *
 */

public class Piece extends Part {

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

	private List<Block> blocks;

	/**
	 * Default Constructor.  Will have better features when I get around to it.
	 */
	public Piece(int newID, PieceData newPieceData) {

		id = newID;
		blockCount = newPieceData.pieceTemplate[id].length;
		blocks = new Vector<Block>(blockCount);
		
		generatePiece(newPieceData, id);
	}

	public Piece(Piece other) {
		super(other);

		id = other.getID();
		blockCount = other.getBlockCount();
		blocks = new Vector<Block>(blockCount);
		
		for (Block currOtherBlock: other.getBlocks()) {
			blocks.add(new Block(currOtherBlock));
		}

		currCenter = new Coordinates(other.currCenter);
		destCenter = new Coordinates(other.destCenter);
	}

	private final void generatePiece(PieceData newPieceData, int pieceID) {
		int state = 0;
		for (int[] blockXY: newPieceData.pieceTemplate[pieceID]) {
			blocks.add(new Block(new Coordinates(blockXY[0], blockXY[1]), id, state++));
		}

		// Centers are represented multiplied by two, this is why the 'pos' 
		// variables are doubled to match the already doubled 'pieceCenter'.
		Coordinates posDoubled = new Coordinates(newPieceData.pieceStartPos.x << 1, 
		                                         newPieceData.pieceStartPos.y << 1);
		currCenter = newPieceData.getCurrCenter(pieceID).move(posDoubled);
		destCenter = newPieceData.getDestCenter(pieceID).move(posDoubled);

		for (Block currBlock: blocks) {
				currBlock.move(newPieceData.pieceStartPos);
		}
	}

	public List<Block> getBlocks() {
		return blocks;
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

	private final synchronized Piece rotate(int flip) {
		// flip: -1 Clockwise, 1 CounterClockwise.
		// Move the Blocks in the piece to their new positions.
		for(Block currBlock: blocks) {
				/* Equivalent but slower:
				currBlock.pos = new Coordinates( (destCenter[X] + flip * (2 * currBlock.pos.y - currCenter[Y])) / 2,
				                                 (destCenter[Y] - flip * (2 * currBlock.pos.x - currCenter[X])) / 2 ); */
				currBlock.pos = new Coordinates( destCenter.x + flip * ((currBlock.pos.y << 1) - currCenter.y) >> 1,
				                                 destCenter.y - flip * ((currBlock.pos.x << 1) - currCenter.x) >> 1 );
				
				currBlock.visual.rotate(flip);
		}

		// swap values of current center and destination center
		Coordinates dummy = destCenter;
		destCenter = currCenter;
		currCenter = dummy;

		return this;
	}
	
	public Piece move(PieceAction action) {
		return (action.type == PieceAction.Type.MOVE)
			? move(action.offset)
			: rotate(action.offset.x); // x designated as direction for rotate algorithm.
	}

	private Piece move(Coordinates offset) {
		Coordinates offsetDoubled = new Coordinates(offset.x << 1, offset.y << 1);
		currCenter.move(offsetDoubled);
		destCenter.move(offsetDoubled);

		for (Block currBlock: blocks) {
			currBlock.move(offset);
		}

		return this;
	}

	public void updateVisual() {
		for (Block block : blocks) {
			if (block.visual == null) {
				block.visual = Game.me.engine.newVisual(block);
			}
			((TetrisSprite)block.visual).update(block);
		}
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
