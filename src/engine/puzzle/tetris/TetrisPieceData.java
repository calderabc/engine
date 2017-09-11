package engine.puzzle.tetris;

import java.io.Serializable;

import engine.Coordinates;

public class TetrisPieceData implements Serializable {
	private static final long serialVersionUID = -9105193668883492424L;

	public static final String FILE_NAME = "tetris_piece.dat";

	public final int[][][] pieceTemplate;
	public final int[][] pieceCenter;
	public final Coordinates pieceStartPos;

	
	public TetrisPieceData(int[][][] newPieceTemplate, int[][] newPieceCenter, Coordinates newPieceStartPos) {
		pieceTemplate = newPieceTemplate;
		pieceCenter = newPieceCenter;
		pieceStartPos = newPieceStartPos;
	}

	public final Coordinates getCurrCenter(int pieceID) {
		return new Coordinates(pieceCenter[pieceID][0], pieceCenter[pieceID][1]);
	}

	public final Coordinates getDestCenter(int pieceID) {
		return new Coordinates(pieceCenter[pieceID][2], pieceCenter[pieceID][3]);
	}
}
