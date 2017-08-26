package engine.puzzle;

import java.util.Random;

import engine.Debug;

public final class PiecePool implements Debug {
	private final Piece[] pieces;
	
	private final PieceData pieceData;
	
	protected PiecePool(PieceData newPieceData) {
		pieceData = newPieceData;
		
		int pieceCount = pieceData.pieceTemplate.length;

		pieces = new Piece[pieceCount];
		for (int i = 0; i < pieceCount; i++) {
			pieces[i] = new Piece(i, pieceData);	
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
