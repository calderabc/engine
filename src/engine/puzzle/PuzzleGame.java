package engine.puzzle;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import engine.Game;
import engine.puzzle.tetris.ScoreCalculator;
import engine.puzzle.tetris.TetrisPiece;
import engine.puzzle.tetris.swing.PieceAction;
import engine.swing.Swing;

public abstract class PuzzleGame extends Game {
	public static PuzzleGame me = (PuzzleGame)Game.me;
	
	
	protected Board board;
	protected TetrisPiece piece;
	protected Score score;
	protected boolean isPieceLanded;
	protected Level level;

	public ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(2);
	 
	public void run() {
		scheduler.setRemoveOnCancelPolicy(true);
		

		score = new Score(ScoreCalculator.NINTENDO, 10, 0);
		level = new Level(1);
		
		piece = board.startNewPiece();
		while (board.doesPieceFit(piece)) {		
			screen.addParts(piece.getBlocks());
			screen.update();

			isPieceLanded = false;
			PieceAction.startFalling();
			
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

			PieceAction.stopFalling();

			piece = board.startNewPiece();
		}
	}
	 
}
