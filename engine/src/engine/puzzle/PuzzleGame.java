package engine.puzzle;

import java.util.Arrays;

import engine.Game;
import engine.Reflection;
import engine.Concurrent;


public final class PuzzleGame extends Game {
	// The have to be public for reflection to work.
	public Board board;
	public Score score;
	public Piece piece;

	private Number level;
	private Number rowsCleared;
	private Number pieceCount;
	private boolean isPieceLanded;
	public PieceAction.Action pieceAction;


	private PuzzleGame(String newGameName, String newEngineName) {
		super(newGameName, "Puzzle", newEngineName, "Graphics2d");


		pieceAction = PieceAction.newInstance(this);

		// Everyone else depends on screen so run first.
		screen = Reflection.newScreen(this);

		Concurrent.run(
			() -> board = (Board)Reflection.newGameField(this, "Board"),
			() -> Reflection.instantiateGameField(this, "Piece"),
			() -> Reflection.instantiateGameField(this, "Score"),
			() -> level = new Number(this, Number.Type.LEVEL, (byte)2).set(1),
			() -> rowsCleared = new Number(this, Number.Type.ROWS, (byte)3),
			() -> pieceCount = new Number(this, Number.Type.PIECES, (byte)4)
		);


		while (board.doesPieceFit(piece)) {
			screen.initVisuals();

			screen.update();

			isPieceLanded = false;
			// Start the piece a-fallin'.
			pieceAction.FALL.startPieceAction();

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


	boolean tryToMovePiece(PieceAction action) {
		System.out.println("tryToMovePiece");
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

	void landPiece() {
		synchronized(piece) {
			if (!isPieceLanded) {
				// Stop all scheduled piece actions, the piece needs to land!
				pieceAction.resetAll();
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
