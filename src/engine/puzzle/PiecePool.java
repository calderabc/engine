package engine.puzzle;

import java.util.Random;

import engine.Debug;

public abstract class PiecePool implements Debug {
	private final Piece[] pieces;
	
	private final int[][][] pieceTemplate;
	public final int[][] pieceCenter;
	
	protected PiecePool(int[][][] newPieceTemplate, int[][] newPieceCenter) {
		pieceTemplate = newPieceTemplate;
		pieceCenter = newPieceCenter;
		
		int pieceCount = pieceTemplate.length;

		pieces = new Piece[pieceCount];
		for (int i = 0; i < pieceCount; i++) {
			pieces[i] = new Piece(i, pieceTemplate, pieceCenter);	
		}	
	}
	
	public Piece getRandomPiece() {
		// deep copy a random Piece from pieces and return
		return new Piece(pieces[new Random().nextInt(pieces.length)]);
	}
	
	public Piece getPiece(int pieceIndex) {
		return pieces[pieceIndex];
	}
				
	@Override
	public void printInfo() {
		System.out.println("Pieces - Start");
		for (Piece piece : pieces) {
			piece.printInfo();
		}
		System.out.println("Pieces - Finish");
	}

}
