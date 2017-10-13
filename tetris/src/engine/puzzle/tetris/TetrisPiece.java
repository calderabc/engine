package engine.puzzle.tetris;

import java.util.Random;

import engine.Coordinates;
import engine.FileIO;
import engine.Game;
import engine.Visual;
import engine.puzzle.Block;
import engine.puzzle.Piece;

/**
 * This Piece class represents a Tetris piece.  It is composed of Blocks.  It has
 *
 * @author Aaron Calder
 *
 */

public final class TetrisPiece extends Piece {
	private static Piece[] pieces;

	private void populatePieces(Game game) {
		TetrisPieceData pieceData =
				(TetrisPieceData)FileIO.load(TetrisPieceData.FILE_NAME);
		int pieceCount = pieceData.pieceTemplate.length;

		pieces = new TetrisPiece[pieceCount];
		for (byte i = 0; i < pieceCount; i++) {
			pieces[i] = new TetrisPiece(game, i, pieceData);
		}	
	}
					
	public volatile Coordinates currCenter;
	public volatile Coordinates destCenter;

	private TetrisPiece newPiece() {
		if (pieces == null) {
			populatePieces(game);
		}
		TetrisPiece piece = (TetrisPiece)pieces[new Random().nextInt(pieces.length)];
		
	}

	public TetrisPiece(Game game) {
	}

	public TetrisPiece(TetrisPiece other) {
		super(other);
		currCenter = new Coordinates(other.currCenter);
		destCenter = new Coordinates(other.destCenter);
	}

	private TetrisPiece(Game game, byte pieceId, TetrisPieceData newPieceData) {
		super(newPieceData.pieceTemplate[pieceId].length);	

		byte state = 0;
		int i = 0;
		for (byte[] blockXY: newPieceData.pieceTemplate[pieceId]) {
			blocks[i++] = new Block(
				game,
				new Coordinates(blockXY[0], blockXY[1]),
				new Visual.Id("Block", pieceId, state++)
			);
		}

		// Centers are represented multiplied by two, this is why the 'pos' 
		// variables are doubled to match the already doubled 'pieceCenter'.
		Coordinates posDoubled = new Coordinates(newPieceData.pieceStartPos.x << 1,
		                                         newPieceData.pieceStartPos.y << 1);
		currCenter = newPieceData.getCurrCenter(pieceId).move(posDoubled);
		destCenter = newPieceData.getDestCenter(pieceId).move(posDoubled);

		for (Block currBlock: blocks) {
				currBlock.move(newPieceData.pieceStartPos);
				currBlock.visual.update(currBlock);
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
	@Override
	protected Piece rotate(Coordinates offset) {
		// offset.x: -1 Clockwise, 1 CounterClockwise.
		// Move the Blocks in the piece to their new positions.
		int flip = offset.x;
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

	public static Piece getPiece(int pieceIndex) {
		return pieces[pieceIndex];
	}

	protected TetrisPiece move(Coordinates offset) {
		Coordinates offsetDoubled = 
			new Coordinates(offset.x << 1, offset.y << 1);
		currCenter.move(offsetDoubled);
		destCenter.move(offsetDoubled);

		super.move(offset);
		return this;
	}
}
