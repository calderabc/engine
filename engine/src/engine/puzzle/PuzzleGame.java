package engine.puzzle;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import engine.Game;
import engine.Reflection;
import engine.graphics2d.ImageType;

public class PuzzleGame extends Game {

	public Board board;
	public Score score;
	public Piece piece;

	private Number level;
	private Number rowsCleared;
	private Number pieceCount;
	private boolean isPieceLanded;

	public final ImageType blockImageType;
	public final ImageType digitImageType;


	private void newFields(String packageString, String... classNames) {
		Thread[] threads = new Thread[classNames.length];
		int i = 0;
		for (String className : classNames) {
			threads[i] = new Thread(() -> {
				Reflection.instantiateGameField(this, className);
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

	public PuzzleGame(String newGameName, String newEngineName) {
		super(newGameName, "Puzzle", newEngineName, "Graphics2d");

		blockImageType = Reflection.newImageType(this, Block.class);
		digitImageType = Reflection.newImageType(this, Digit.class);

		newFields(gameName, "Board", "Piece", "Score");
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

	public static void main(String argv[]) {
		// TODO: Make more robust. Validate arguments.
		if (argv.length != 2) {
			throw new IllegalArgumentException();
		}
		new PuzzleGame(argv[0], argv[1]);
	}
}
