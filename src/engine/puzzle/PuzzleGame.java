package engine.puzzle;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import engine.Game;

public abstract class PuzzleGame extends Game {
	
	public Board board;
	public Score score;
	public Piece piece;
	protected Number level;
	protected Number rowsCleared;
	protected boolean isPieceLanded;

	public ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(2);
	 
	public void run() {
		scheduler.setRemoveOnCancelPolicy(true);

		level = new Number(Number.Type.LEVEL, 1, (byte)2);
		rowsCleared = new Number(Number.Type.ROWS, 0, (byte)3);

		
		//piece = new TetrisPiece();
		while (board.doesPieceFit(piece)) {		
			engine.screen.addParts(piece.getBlocks());
			engine.screen.update();

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

			piece = piece.newPiece();
		}
	}
	 
	public abstract boolean tryToMovePiece(PieceAction action);
	public abstract void landPiece();
}
