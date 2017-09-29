package engine.puzzle;

public abstract class Board {
	public abstract boolean doesPieceFit(Piece testPiece);
	public abstract void landPiece(Piece landingPiece);
	public abstract int tryRemoveBlocks();
}
