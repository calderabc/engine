package engine.puzzle.tetris.swing;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import engine.Coordinates;
import engine.puzzle.tetris.TetrisGame;

public enum PieceAction {
	WARP (Type.MOVE, 100, new Coordinates(0, 1)),
	FALL (Type.MOVE, 3, new Coordinates(0, 1)),
	DOWN (Type.MOVE, 20, new Coordinates(0, 1)),
	LEFT (Type.MOVE, 6, new Coordinates(-1, 0)),
	RIGHT (Type.MOVE, 6, new Coordinates(1, 0), PieceAction.LEFT),
	CLOCKWISE (Type.ROTATE, 6, new Coordinates(-1, 0)),
	COUNTERCLOCKWISE (Type.ROTATE, 6, new Coordinates(1, 0), PieceAction.CLOCKWISE);

	private static final Object sync = new Object();
	// Delay between actions (moving/rotating the piece) in milliseconds.(?) 
	// TODO: Figure the apparent discord between milliseconds and 1.0e9 (nanosecond)
	// Why does setting the delay to "1.0e9 / newFrequency" work if things are 
	// done in terms of milliseconds (1.0e3 per second)?
	private long delay; 

	private PieceAction opposite;
	private ScheduledFuture<?> future;
	private Runnable runner;
	private boolean isMoving = false;  
	private boolean isPressed = false;

	public enum Type {
		MOVE,
		ROTATE }
	public final Type type;
	public final Coordinates offset;
	

	private PieceAction(Type newType, double newFrequency, Coordinates newCoordinates) {
		this(newType, newFrequency, newCoordinates, null);
	}
		
	private PieceAction(Type newType, double newFrequency, Coordinates newOffset, PieceAction newOpposite) {
		delay = Math.round(1.0e9 / newFrequency);
		offset = newOffset;
		type = newType;
		
		opposite = newOpposite;
		if (opposite != null) {
			opposite.opposite = this;
		}
		
		runner = new ActionRunner(this);
	}
	
	public static void resetAll() {
		for(PieceAction currAction: values()) {
			if (currAction.future != null) {
				currAction.future.cancel(false);
			}
			currAction.isMoving = false;
			currAction.isPressed = false;
		}
	}

	private class ActionRunner implements Runnable {
		private PieceAction action;
		public ActionRunner(PieceAction newAction) {
			action = newAction;
		}
		public void run() {
			if (action.isPressed) {
				if (!TetrisGame.me.tryToMovePiece(action)) {
					action.future.cancel(false);
					action.isMoving = false;
				}
			}
		}
	}

	private static void scheduleFuture(PieceAction action, boolean startNow) {
		action.isMoving = true;
		action.future = 
			TetrisGame.me.scheduler.scheduleAtFixedRate(action.runner, 
		                                  (startNow) ? 0 : action.delay, 
		                                  action.delay, 
		                                  TimeUnit.NANOSECONDS);
	}
	
	private static void cancelFuture(PieceAction action) {
		action.future.cancel(false);
		action.isMoving = false;
	}

	public static synchronized void startFalling() {
		if (!PieceAction.FALL.isMoving) {
			PieceAction.FALL.runner = () -> {
				if (!TetrisGame.me.tryToMovePiece(PieceAction.FALL)) {
					stopFalling();
					TetrisGame.me.landPiece();
				}
			};
			scheduleFuture(PieceAction.FALL, false);
		}
	}

	public static synchronized void stopFalling() {
		if (PieceAction.FALL.isMoving) {
			cancelFuture(PieceAction.FALL);
		}
	}
	
	public static synchronized void warpDown() {
		if (!PieceAction.WARP.isMoving) {
			stopFalling();

			PieceAction.WARP.runner = () -> {
				if (!TetrisGame.me.tryToMovePiece(PieceAction.WARP)) {
					PieceAction.WARP.isMoving = false;
					startFalling();
					PieceAction.WARP.future.cancel(true);
				}
			};
			
			scheduleFuture(PieceAction.WARP, true);
		}
	}

	public static synchronized void startPieceAction(PieceAction action) {
		if (!action.isPressed) {
			action.isPressed = true;
			
			if (action.opposite.isPressed) {
				cancelFuture(action.opposite);
			}
			
			scheduleFuture(action, true);
		}			
	}

	public static synchronized void stopPieceAction(PieceAction action) {
		cancelFuture(action);
		action.isPressed = false;

		if (action.opposite.isPressed) {
			startPieceAction(action.opposite);
		}
	}

	public static synchronized void startMovingDown() {
		if (!PieceAction.DOWN.isPressed) {
			PieceAction.DOWN.isPressed = true;
			
			if (!PieceAction.DOWN.isMoving) {
				stopFalling();
				
				PieceAction.DOWN.runner = () -> {
					if (!TetrisGame.me.tryToMovePiece(PieceAction.DOWN)) {
						TetrisGame.me.landPiece();
						cancelFuture(PieceAction.DOWN);
					}
				};
		
				scheduleFuture(PieceAction.DOWN, true);
			}
		}
	}

	public static synchronized void stopMovingDown() {
		cancelFuture(PieceAction.DOWN);
		PieceAction.DOWN.isPressed = false;
		startFalling();
	}

}