package engine.tetris;


import engine.puzzle.PieceAction;
import engine.puzzle.PuzzleGame;

public final class TetrisGame extends PuzzleGame {
	
	public static void main(String argv[]) {
		me = new TetrisGame();
		((PuzzleGame)me).run("Swing");
	}
	
	@Override
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
				TetrisPiece testPiece =  new TetrisPiece((TetrisPiece)piece).move(action);

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

	@Override
	public void landPiece() {
		synchronized(piece) {
			if (!isPieceLanded) {
				PieceAction.resetAll();
				((TetrisBoard)board).landPiece(piece);
				int numRowsRemoved = ((TetrisBoard)board).tryRemoveBlocks();
				if (numRowsRemoved > 0) {
					rowsCleared.add(numRowsRemoved);
					// TODO: verify this is how scoring works.
					// Here score is compounded according the the level
					// being switched to.  Probably a way to
					// score part on old level, and part on new level.
					level = score.checkLevel(level, rowsCleared);

					score.update(level, numRowsRemoved);
				}

				isPieceLanded = true;
			}
		}
	}

}
