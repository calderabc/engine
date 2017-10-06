package engine.puzzle;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import engine.Game;

public abstract class PuzzleGame extends Game {

	public Board board;
	public Score score;
	public Piece piece;

	public final String name;

	private Number level;
	private Number rowsCleared;
	private Number pieceCount;
	private boolean isPieceLanded;

	private void magicMirror(String packageString, String... classNames) {
		// Who's the fairest of them all?
		Thread[] threads = new Thread[classNames.length];
		int i = 0;
		for (String className : classNames) {
			threads[i] = new Thread(() -> {
				String fullClassName = "engine."
									   + packageString.toLowerCase() + "."
									   + packageString + className;
				try {
					this.getClass()    // Holy Reflection Batman!
						.getField(className.toLowerCase())
						.set(this, Class.forName(fullClassName)
						                .getDeclaredConstructor()
						                .newInstance());
				} catch (IllegalArgumentException | IllegalAccessException
						 | NoSuchFieldException | SecurityException 
						 | InstantiationException | ClassNotFoundException
				         | NoSuchMethodException | InvocationTargetException e) {
					e.printStackTrace();
				}
			});

			threads[i++].start();
		}
		// Game objects require that the engine objects are created first.
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected PuzzleGame() {
		String gameString = this.getClass().getSimpleName();
		gameString = gameString.substring(0, gameString.length() - 4);
		name = gameString;
	}
	
	public final void run(String engineString) {
		System.out.println(name);
		magicMirror(engineString, "Screen");
		magicMirror(name, "Board", "Piece", "Score");
		screen.setScale(board, piece.getBlocks()[0].visual);

		level = new Number(Number.Type.LEVEL, (byte)2).set(1);
		rowsCleared = new Number(Number.Type.ROWS, (byte)3);
		pieceCount = new Number(Number.Type.PIECES, (byte)4);

		while (board.doesPieceFit(piece)) {
			// Add the piece's blocks to screen so they will be displayed.
			screen.addParts(Arrays.asList(piece.getBlocks()));
			screen.update();

			isPieceLanded = false;
			// Start the piece a-fallin'.
			PieceAction.FALL.startPieceAction();

			while(!isPieceLanded)  // In case of a spurious wake up.
				try {
					// Suspend thread until notified of the piece's landing.
					synchronized(this) {
						this.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

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
				// Stop all scheduled piece actions, the piece needs to land!
				PieceAction.resetAll();
				int removedCount = board.landPiece(piece);
				// Block removing is done, safe to start a new piece.
				isPieceLanded = true;
				synchronized(this) {
					// The Eagle has landed. Main thread please resume.
					this.notify();
				}
				// Update score and statistics.
				pieceCount.add(1);
				if (removedCount > 0) {
					rowsCleared.add(removedCount);
					// TODO: verify this is how scoring works.
					// Here score is compounded according the the level
					// being switched to.  Probably a way to
					// score part on old level, and part on new level.
					level = score.checkLevel(level, rowsCleared);
					score.update(level, removedCount);
				}
			}
		}
	}
}
