package engine.tetris;

import java.util.Random;

import engine.puzzle.Piece;
import engine.puzzle.PieceData;

public final class TetrisPiecePool {
	private final Piece[] pieces;
	
	private final PieceData pieceData;
	
	public TetrisPiecePool(PieceData newPieceData) {
		pieceData = newPieceData;
		
		int pieceCount = ((TetrisPieceData)pieceData).pieceTemplate.length;

		pieces = new TetrisPiece[pieceCount];
		for (byte i = 0; i < pieceCount; i++) {
			pieces[i] = new TetrisPiece(i, (TetrisPieceData)pieceData);	
		}	
	}
	
	public Piece getRandomPiece() {
		// deep copy a random Piece from pieces and return
		return new TetrisPiece((TetrisPiece)pieces[new Random().nextInt(pieces.length)]);
	}
	
	public Piece getPiece(int pieceIndex) {
		return pieces[pieceIndex];
	}
				
}
