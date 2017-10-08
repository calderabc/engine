package engine.puzzle.tetris;

import java.io.Serializable;

import engine.Coordinates;
import engine.puzzle.PieceData;

public class TetrisPieceData extends PieceData implements Serializable {
	private static final long serialVersionUID = -9105193668883492424L;

	public static final String FILE_NAME = "tetris_piece.dat";

	public final byte[][][] pieceTemplate;
	public final byte[][] pieceCenter;
	public final Coordinates pieceStartPos;

	
	public TetrisPieceData(byte[][][] newPieceTemplate, 
	                       byte[][] newPieceCenter, 
	                       Coordinates newPieceStartPos) {
		pieceTemplate = newPieceTemplate;
		pieceCenter = newPieceCenter;
		pieceStartPos = newPieceStartPos;
	}

	public final Coordinates getCurrCenter(int pieceID) {
		return new Coordinates(pieceCenter[pieceID][0], 
		                       pieceCenter[pieceID][1]);
	}

	public final Coordinates getDestCenter(int pieceID) {
		return new Coordinates(pieceCenter[pieceID][2], 
		                       pieceCenter[pieceID][3]);
	}
}
