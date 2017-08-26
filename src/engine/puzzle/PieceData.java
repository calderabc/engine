package engine.puzzle;

import engine.Coordinates;

public abstract class PieceData {
	public final int[][][] pieceTemplate = null;
	public final int[][] pieceCenter = null;
	public final Coordinates pieceStartPos = null;
	
	public final Coordinates getCurrCenter(int pieceID) {
		return new Coordinates(pieceCenter[pieceID][0], pieceCenter[pieceID][1]);
	}

	public final Coordinates getDestCenter(int pieceID) {
		return new Coordinates(pieceCenter[pieceID][2], pieceCenter[pieceID][3]);
	}
}
