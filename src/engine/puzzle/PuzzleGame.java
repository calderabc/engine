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
		
		while (board.doesPieceFit(piece)) {		
			screen.addParts(piece.getBlocks());
			screen.update();

			isPieceLanded = false;
			PieceAction.FALL.startPieceAction();
			

			while(!isPieceLanded) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			PieceAction.FALL.stopPieceAction();

			piece = piece.newPiece();
		}
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
				if (board.doesPieceFit(piece.clone().move(action))) {
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
}
