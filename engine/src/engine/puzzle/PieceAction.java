package engine.puzzle;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import engine.Coordinates;
import engine.Game;

// This class is mean to look like an enum from the outside with the additional
// ability for the values to be able to reference instance variables (a game).
public class PieceAction implements Serializable {
	// Threads for the scheduled piece movement actions, piece falling etc.
	// TODO: How many concurrent scheduled actions do I need?
	private static ScheduledThreadPoolExecutor scheduler =
	new ScheduledThreadPoolExecutor(2);

	static {
		scheduler.setRemoveOnCancelPolicy(true);
	}

	private PieceAction() {
		type = null;
		offset = null;
	}

	private PieceAction(Game game) {
		this();
		this.game = game;
	}

	static PieceAction.Action newInstance(Game game) {
		return new PieceAction(game).new Action();
	}

	private Game game;


	public class Action extends PieceAction {
		private Action() {};

		// TODO: Could reduce data file size by saving values instead of whole objects.
		final PieceAction WARP = new PieceAction(PieceAction.Type.MOVE, 100, new Coordinates(0, 1)) {
			@Override
			public synchronized void startPieceAction() {
				if (!isMoving) {
					FALL.stopPieceAction();

					runner = () -> {
						if (!((PuzzleGame)game).tryToMovePiece(this)) {
							isMoving = false;
							FALL.startPieceAction();
							future.cancel(true);
						}
					};

					scheduleFuture(true);
				}
			}

			@Override
			public synchronized void stopPieceAction() { }
		};
		final PieceAction FALL = new PieceAction (Type.MOVE, 2, new Coordinates(0, 1)) {
			@Override
			public synchronized void startPieceAction() {
				if (!isMoving) {
					runner = () -> {
						if (!((PuzzleGame)game).tryToMovePiece(this)) {
							stopPieceAction();
							((PuzzleGame)game).landPiece();
						}
					};
					scheduleFuture(false);
				}
			}

			@Override
			public synchronized void stopPieceAction() {
				if (isMoving) {
					cancelFuture();
				}
			}
		};
		final PieceAction SPEED = new PieceAction (Type.MOVE, 20, new Coordinates(0, 1)) {
			@Override
			public synchronized void startPieceAction() {
				if (!isPressed) {
					isPressed = true;

					if (!isMoving) {
						FALL.stopPieceAction();

						runner = () -> {
							if (!((PuzzleGame) game).tryToMovePiece(this)) {
								((PuzzleGame) game).landPiece();
								cancelFuture();
							}
						};

						scheduleFuture(true);
					}
				}
			}

			@Override
			public synchronized void stopPieceAction() {
				cancelFuture();
				isPressed = false;
				FALL.stopPieceAction();
			}
		};
		final PieceAction LEFT = new PieceAction (Type.MOVE, 6, new Coordinates(-1));
		final PieceAction RIGHT = new PieceAction (Type.MOVE, 6, new Coordinates(1), LEFT);
		final PieceAction CLOCK = new PieceAction (Type.ROTATE, 6, new Coordinates(-1));
		final PieceAction COUNTER = new PieceAction (Type.ROTATE, 6, new Coordinates(1), CLOCK);
	}


	// Delay between actions (moving/rotating the piece) in milliseconds.(?)
	// TODO: Figure the apparent discord between milliseconds and 1.0e9 (nanosecond)
	// Why does setting the delay to "1.0e9 / newFrequency" work if things are
	// done in terms of milliseconds (1.0e3 per second)?
	private long delay;

	private PieceAction opposite;
	private ScheduledFuture<?> future;

	private boolean isMoving = false;
	private boolean isPressed = false;

	private Runnable runner;

	public enum Type {
		MOVE,
		ROTATE }
	public final Type type;
	public final Coordinates offset;

	PieceAction(Type newType, double newFrequency, Coordinates newOffset, PieceAction newOpposite) {
		delay = Math.round(1.0e9 / newFrequency);
		offset = newOffset;
		type = newType;

		opposite = newOpposite;
		if (opposite != null) {
			opposite.opposite = this;
		}

		runner = () -> {
			if (isPressed) {
				if (!((PuzzleGame)game).tryToMovePiece(this)) {
					future.cancel(false);
					isMoving = false;
				}
			}
		};

	}

	PieceAction(Type newType, double newFrequency, Coordinates newCoordinates){
			this(newType, newFrequency, newCoordinates, null);
		}

	public void resetAll() {
		for (Field currField : this.getClass().getFields()) {
			PieceAction currAction = null;
			try {
				currAction = (PieceAction)currField.get(this);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (currAction.future != null) {
				currAction.future.cancel(false);
			}
			currAction.isMoving = false;
			currAction.isPressed = false;
		}
	}

	protected void scheduleFuture(boolean startNow) {
		isMoving = true;
		future = scheduler.scheduleAtFixedRate(runner,
		(startNow) ? 0 : delay,
		delay,
		TimeUnit.NANOSECONDS);
	}

	protected void cancelFuture() {
		future.cancel(false);
		isMoving = false;
	}

	public synchronized void startPieceAction() {
		if (!isPressed) {
			isPressed = true;
			if (opposite.isPressed) {
				opposite.cancelFuture();
			}

			scheduleFuture(true);
		}
	}

	public synchronized void stopPieceAction() {
		cancelFuture();
		isPressed = false;

		if (opposite.isPressed) {
			opposite.startPieceAction();
		}
	}

}