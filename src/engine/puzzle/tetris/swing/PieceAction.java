package engine.puzzle.tetris.swing;

import java.io.Serializable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import engine.Coordinates;
import engine.puzzle.tetris.TetrisGame;

//Map<String, PieceAction> pieceActionMap = new HashMap<String, PieceAction>(20);
public enum PieceAction implements Serializable {
	// TODO: Could reduce data file size by saving values instead of whole objects.
	WARP(PieceAction.Type.MOVE, 100, new Coordinates(0, 1)) {
		@Override 
		public synchronized void startPieceAction() {
			if (!isMoving) {
				FALL.stopPieceAction();

				runner = () -> {
					if (!TetrisGame.me.tryToMovePiece(this)) {
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
	},
	FALL (Type.MOVE, 3, new Coordinates(0, 1)) {
		@Override
		public synchronized void startPieceAction() {
			if (!isMoving) {
				runner = () -> {
					if (!TetrisGame.me.tryToMovePiece(this)) {
						stopPieceAction();
						TetrisGame.me.landPiece();
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
	},
	SPEED (Type.MOVE, 20, new Coordinates(0, 1)) {
		@Override
		public synchronized void startPieceAction() {
			if (!isPressed) {
				isPressed = true;
				
				if (!isMoving) {
					FALL.stopPieceAction();
					
					runner = () -> {
						if (!TetrisGame.me.tryToMovePiece(this)) {
							TetrisGame.me.landPiece();
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
	},
	LEFT (Type.MOVE, 6, new Coordinates(-1, 0)),
	RIGHT (Type.MOVE, 6, new Coordinates(1, 0), LEFT),
	CLOCK (Type.ROTATE, 6, new Coordinates(-1, 0)),
	COUNTER (Type.ROTATE, 6, new Coordinates(1, 0), CLOCK);

	public static ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(2);

	static {
		scheduler.setRemoveOnCancelPolicy(true);
	}

	// Delay between actions (moving/rotating the piece) in milliseconds.(?) 
	// TODO: Figure the apparent discord between milliseconds and 1.0e9 (nanosecond)
	// Why does setting the delay to "1.0e9 / newFrequency" work if things are 
	// done in terms of milliseconds (1.0e3 per second)?
	private long delay; 

	private PieceAction opposite;
	protected ScheduledFuture<?> future;

	protected boolean isMoving = false;  
	protected boolean isPressed = false;
	
	protected Runnable runner; 

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

		runner = () -> {
			if (isPressed) {
				if (!TetrisGame.me.tryToMovePiece(this)) {
					future.cancel(false);
					isMoving = false;
				}
			}
		};

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