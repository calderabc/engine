package engine.puzzle.tetris;

import engine.Game;
import engine.puzzle.Board;
import engine.puzzle.PuzzleGame;
import engine.puzzle.tetris.swing.PieceAction;
import engine.swing.Swing;

public class TetrisGame extends PuzzleGame {
	public static TetrisGame me = (TetrisGame)Game.me;
	static public final TetrisPieceData TETRIS_PIECE_DATA = new TetrisPieceData();

	public static void main(String argv[]) {
		me = new TetrisGame(TETRIS_PIECE_DATA);
		// TODO: Here I would choose between graphical engines.
		me.engine = new Swing((TetrisGame)me);

		me.screen = me.engine.newScreen((TetrisGame)me);
		((PuzzleGame)me).run();

		System.exit(0);
	}
	
	public TetrisGame(TetrisPieceData pieceData) {
		board = new Board(pieceData);
	}
	
	
	public boolean tryToMovePiece(PieceAction action) {
		synchronized(piece) {
			if (!isPieceLanded) {
				/** 
				 * TODO:  Add code to slide the piece left or right if there is room
				 * for the rotated piece to fit but the act of rotating would cause the right
				 * or left edge of the piece to overlap the edges or other blocks.
				 * For instance if the 4 straight long piece is vertical and on either right
				 * or left edge it should still be able to rotate.  Simple rotation would put
				 * the piece over the edge once horizontal so shifting the piece will allow it
				 * to rotate.  If a piece is rotated this way rotating back should return
				 * the piece to its original position.  Perhaps add as an optional feature
				 * if not present in games people are used to playing.
				 */			
				TetrisPiece testPiece =  new TetrisPiece(piece).move(action);
				
				if (board.doesPieceFit(testPiece)) {
					piece.move(action);

					piece.updateVisual();
					screen.update();

					return true;
				}
			}
		}
		return false;
	}
	
	public void landPiece() {
		synchronized(piece) {
			if (!isPieceLanded) {
				PieceAction.resetAll();
				board.landPiece(piece);
				board.tryRemoveBlocks();
				
				isPieceLanded = true;
			}
		}
	}
	
	
}