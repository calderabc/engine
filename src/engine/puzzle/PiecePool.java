package engine.puzzle;

import java.util.Random;

import engine.puzzle.tetris.TetrisPiece;
import engine.puzzle.tetris.TetrisPieceData;

public final class PiecePool {
	private final Piece[] pieces;
	
	private final PieceData pieceData;
	
	protected PiecePool(PieceData newPieceData) {
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
