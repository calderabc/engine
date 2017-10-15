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
	new ScheduledThreadPoolExecutor(3);

	static {
		scheduler.setRemoveOnCancelPolicy(true);
	}

	private PieceAction() {
		type = null;
		offset = null;
	}

	private PieceAction(Game newGame) {
		this();
		game = newGame;
	}

	static PieceAction.Action newInstance(Game game) {
		return new PieceAction(game).new Action();
	}

	private Game game;


	public class Action extends PieceAction {
		private Action() {}

		// TODO: Could reduce data file size by saving values instead of whole objects.
		public final PieceAction WARP = new PieceAction(game, PieceAction.Type.MOVE, 100, new Coordinates(0, 1)) {
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
		public final PieceAction FALL = new PieceAction (game, Type.MOVE, 2, new Coordinates(0, 1)) {
			@Override
			public synchronized void startPieceAction() {
				if (!isMoving) {
					runner = () -> {
						System.out.println("fall");
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
				//if (isMoving) {
					cancelFuture();
				//}
			}
		};
		public final PieceAction SPEED = new PieceAction (game, Type.MOVE, 20, new Coordinates(0, 1)) {
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
		public final PieceAction LEFT = new PieceAction (game, Type.MOVE, 6, new Coordinates(-1));
		public final PieceAction RIGHT = new PieceAction (game, Type.MOVE, 6, new Coordinates(1), LEFT);
		public final PieceAction CLOCK = new PieceAction (game, Type.ROTATE, 6, new Coordinates(-1));
		public final PieceAction COUNTER = new PieceAction (game, Type.ROTATE, 6, new Coordinates(1), CLOCK);
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

	// This has to be protected so anonymous classes can alter.
	protected Runnable runner;

	public enum Type {
		MOVE,
		ROTATE }
	public final Type type;
	public final Coordinates offset;

	PieceAction(Game newGame, Type newType, double newFrequency, Coordinates newOffset, PieceAction newOpposite) {
		game = newGame;
		delay = Math.round(1.0e9 / newFrequency);
		offset = newOffset;
		type = newType;


		opposite = newOpposite;
		if (opposite != null) {
			opposite.opposite = this;
		}

		runner = () -> {
			System.out.println("runner");
			if (isPressed) {
				if (!((PuzzleGame)game).tryToMovePiece(this)) {
					future.cancel(false);
					isMoving = false;
				}
			}
		};

	}

	PieceAction(Game game, Type newType, double newFrequency, Coordinates newCoordinates){
			this(game, newType, newFrequency, newCoordinates, null);
		}

	public void resetAll() {
		for (Field currField : this.getClass().getFields()) {
			PieceAction currAction = null;
			try {
				Object object = currField.get(this);
				if (object instanceof PieceAction) {
					currAction = (PieceAction)object;
				}
				else continue;
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
		System.out.println("startPieceAction");
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