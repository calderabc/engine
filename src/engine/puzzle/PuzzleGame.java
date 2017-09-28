package engine.puzzle;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import engine.Game;

public abstract class PuzzleGame extends Game {
	// If ever the package hierarchy is restructured change this accordingly.
	private static final String BASE_CLASSPATH = "engine.";

	public Board board;
	public Score score;
	public Piece piece;
	protected Number level;
	protected Number rowsCleared;
	protected boolean isPieceLanded;

	public ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(2);

	private void magicMirror(String packageString, String... classNames) {
		// Who's the fairest of them all?
		Thread[] threads = new Thread[classNames.length];
		int i = 0;
		for (String className : classNames) {
			threads[i] = new Thread(() -> {
				String fullClassName = BASE_CLASSPATH
									   + packageString.toLowerCase() + "."
									   + packageString + className;
				try {
					this.getClass()    // Holy Reflection Batman!
						.getField(className.toLowerCase())
						.set(this, Class.forName(fullClassName).newInstance());
				} catch (IllegalArgumentException | IllegalAccessException 
						 | NoSuchFieldException | SecurityException 
						 | InstantiationException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			});
			threads[i++].start();
		}

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public final void run(String engineString) {
		String gameString = this.getClass().getSimpleName();
		gameString = gameString.substring(0, gameString.length() - 4);

		magicMirror(engineString, "Engine", "Screen");
		magicMirror(gameString, "Board", "Piece", "Score");

		scheduler.setRemoveOnCancelPolicy(true);

		level = new Number(Number.Type.LEVEL, (byte)2).set(1);
		rowsCleared = new Number(Number.Type.ROWS, (byte)3);

		
		//piece = new TetrisPiece();
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

			piece = piece.newPiece();
		}
	}
	 
	public abstract boolean tryToMovePiece(PieceAction action);
	public abstract void landPiece();
}
