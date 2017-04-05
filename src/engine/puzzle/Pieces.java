package engine.puzzle;

import java.util.Random;

import engine.Debug;

public final class Pieces implements Debug {
	private final Piece[] pieces;
	
	private final int blocksPerPiece;
	
	public Pieces(int newBlocksPerPiece) {
		int pieceCount;
		
		blocksPerPiece = newBlocksPerPiece;
		
		if (blocksPerPiece == 4) {
			pieceCount = 7;
		} else {
			pieceCount = 0;
		}
		int dummy = 1/pieceCount; // so I'll remember to fix above code later
		
		pieces = new Piece[pieceCount];
		for (int i = 0; i < pieceCount; i++) {
			pieces[i] = new Piece(blocksPerPiece, i);	
		}	
	}
	
	public Piece getRandomPiece() {
		// deep copy a random Piece from pieces and return
		return new Piece(pieces[new Random().nextInt(pieces.length)]);
	}
	
	public Piece getPiece(int pieceIndex) {
		return pieces[pieceIndex];
	}
				
	
	public int blocksPerPiece() {
		return blocksPerPiece;
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
