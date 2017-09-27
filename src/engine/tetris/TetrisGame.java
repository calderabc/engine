package engine.tetris;

import engine.FileIO;
import engine.puzzle.PieceAction;
import engine.puzzle.PuzzleGame;
import engine.puzzle.Score;
import engine.swing.Swing;

public final class TetrisGame extends PuzzleGame {
	public static final TetrisPieceData TETRIS_PIECE_DATA =
		(TetrisPieceData)FileIO.load(TetrisPieceData.FILE_NAME);
	
	public static void main(String argv[]) {
		me = new TetrisGame();
		me.engine = new Swing(me);
		((PuzzleGame)me).board = new TetrisBoard(TETRIS_PIECE_DATA);
		// TODO: For testing.  Switch to 10 for actual thing.
		((PuzzleGame)me).score = new Score(getScoreCalculator(ScoreType.NES), 2, (byte)7);
		((PuzzleGame)me).run();

		System.exit(0);
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
					engine.screen.update();

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
				board.landPiece(piece);
				int numRowsRemoved = board.tryRemoveBlocks();
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
	
	
	// TODO: Serialize the score mechanism to file.  Not sure if it would benefit.
	
	enum ScoreType { NES }
	private static Score.Calculator getScoreCalculator(ScoreType type) {
		switch (type) {
			case NES : 
				return (level, rowCount) -> {
					int[] multipliers = {40, 100, 300, 1200};
					return multipliers[rowCount - 1] * level.get();
				}; 
			default: return null;
		} 
	}

}
