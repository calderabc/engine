package engine.puzzle;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import engine.Game;
import engine.puzzle.tetris.ScoreCalculator;
import engine.puzzle.tetris.TetrisPiece;
import engine.puzzle.tetris.swing.PieceAction;

public abstract class PuzzleGame extends Game {
	public static PuzzleGame me = (PuzzleGame)Game.me;
	
	
	protected Board board;
	protected TetrisPiece piece;
	protected Score score;
	protected Number level;
	protected Number rowsCleared;
	protected boolean isPieceLanded;

	public ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(2);
	 
	public void run() {
		scheduler.setRemoveOnCancelPolicy(true);

		score = new Score(ScoreCalculator.NINTENDO, (byte)5);
		level = new Number(1, (byte)2);
		rowsCleared = new Number(0, (byte)3);

		
		piece = board.startNewPiece();
		while (board.doesPieceFit(piece)) {		
			screen.addParts(piece.getBlocks());
			screen.update();

			isPieceLanded = false;
			PieceAction.FALL.startPieceAction();
			
			// TODO: Make this thread (main) stop and wait for an interrupt.
			// Polling isPieceLanded wastes CPU processing time. Interrupt, or signal 
			// of some sort from the thread which lands the piece would be better.
			do {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (!isPieceLanded);

			PieceAction.FALL.stopPieceAction();

			piece = board.startNewPiece();
		}
	}
	 
	public abstract boolean tryToMovePiece(PieceAction action);
	public abstract void landPiece();
}
