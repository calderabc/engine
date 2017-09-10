package engine.puzzle;

import java.util.Random;

import engine.puzzle.tetris.TetrisPiece;
import engine.puzzle.tetris.TetrisPieceData;

public final class PiecePool {
	private final TetrisPiece[] pieces;
	
	private final TetrisPieceData pieceData;
	
	protected PiecePool(TetrisPieceData newPieceData) {
		pieceData = newPieceData;
		
		int pieceCount = pieceData.pieceTemplate.length;

		pieces = new TetrisPiece[pieceCount];
		for (int i = 0; i < pieceCount; i++) {
			pieces[i] = new TetrisPiece(i, pieceData);	
		}	
	}
	
	public TetrisPiece getRandomPiece() {
		// deep copy a random Piece from pieces and return
		return new TetrisPiece(pieces[new Random().nextInt(pieces.length)]);
	}
	
	public TetrisPiece getPiece(int pieceIndex) {
		return pieces[pieceIndex];
	}
				
	/*
	@Override
	public void printInfo() {
		System.out.println("Pieces - Start");
		for (Piece piece : pieces) {
			piece.printInfo();
		}
		System.out.println("Pieces - Finish");
	}
	*/

}
