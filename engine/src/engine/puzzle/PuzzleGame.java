package engine.puzzle;


import engine.*;
import engine.Number;


public final class PuzzleGame extends Game {
	// These all have to be public for reflection to work.
	public Board board;
	public Score score;
	public Piece piece;

	private Number level;
	private Number rowsCleared;
	private Number pieceCount;
	private Word scoreLabel;
	private Word levelLabel;
	private Word linesLabel;
	private Word piecesLabel;
	private boolean isPieceLanded;
	public PieceAction.Action pieceAction;



	private PuzzleGame(String newGameName, String newEngineName) {
		super(newGameName, "Puzzle", newEngineName, "Graphics2d");
		// Screen depends on pieceAction so run even more first.
		pieceAction = PieceAction.newInstance(this);
		// Everyone else depends on screen so run first.
		Concurrent.run(
			() -> screen = Reflection.newScreen(this),
			() -> board = (Board)Reflection.newGameField(this, "Board")
		);
		// On your mark. Get set. Go!
		Concurrent.run(
			() -> Reflection.instantiateGameField(this, "Piece"),
			() -> Reflection.instantiateGameField(this, "Score"),
			() -> level = new Number(this, 1, (byte)2).set(1),
			() -> rowsCleared = new Number(this, 2, (byte)3),
			() -> pieceCount = new Number(this, 3, (byte)4)
		);

		Concurrent.run(
			() -> scoreLabel = new Word(this, new Coordinates(2, 1), "Score"),
			() -> levelLabel = new Word(this, new Coordinates(2, 3), "Level"),
			() -> linesLabel = new Word(this, new Coordinates(2, 5), "Lines")//,
			//() -> piecesLabel = new Word(this, new Coordinates(2, 7), "Pieces")
		);

		setScoreBoardDimensions(screen.getParts());

		while (board.doesPieceFit(piece)) {
			// Add the piece's blocks to screen so they will be displayed.
			screen.addParts(piece.blocks);

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
					// The Eagle has landed: main thread please resume.
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
