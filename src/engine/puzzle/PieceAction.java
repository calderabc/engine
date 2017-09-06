package engine.puzzle;

import java.util.concurrent.ScheduledFuture;

import javax.swing.SwingUtilities;

import engine.Coordinates;

public enum PieceAction {
	WARP ("warp down", Type.MOVE, 0, new Coordinates(0, 1)),
	FALL ("fall", Type.MOVE, 3, new Coordinates(0, 1)),
	DOWN ("move down", Type.MOVE, 20, new Coordinates(0, 1)),
	LEFT ("move left", Type.MOVE, 6, new Coordinates(-1, 0)),
	RIGHT ("move right", Type.MOVE, 6, new Coordinates(1, 0), PieceAction.LEFT),
	CLOCKWISE ("rotate clockwise", Type.ROTATE, 6, new Coordinates(-1, 0)),
	COUNTERCLOCKWISE ("rotate counterclockwise", Type.ROTATE, 6, new Coordinates(1, 0), PieceAction.CLOCKWISE);

	// Delay between actions (moving/rotating the piece) in milliseconds.(?) 
	// TODO: Figure the apparent discord between milliseconds and 1.0e9 (nanosecond)
	// Why does setting the delay to "1.0e9 / newFrequency" work if things are 
	// done in terms of milliseconds (1.0e3 per second)?
	private long delay = 0; 
	
	// TODO: Verify these are working, useful for something.
	private long releaseTime = System.currentTimeMillis();
	private long pressTime = System.currentTimeMillis();

	public String name;
	public ScheduledFuture<?> future;
	public boolean isMoving = false;  
	public boolean isPressed = false;


	public enum Type {
		MOVE,
		ROTATE }
	public Type type;
	
	public Coordinates offset;
	public PieceAction opposite;
	public Runnable runner;

	private PieceAction(String newName, Type newType, double newFrequency, Coordinates newCoordinates) {
		this(newName, newType, newFrequency, newCoordinates, null);
	}
		
	private PieceAction(String newName, Type newType, double newFrequency, Coordinates newOffset, PieceAction newOpposite) {
		name = newName;
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
				currAction.future.cancel(true);
			}
			currAction.isMoving = false;
			currAction.isPressed = false;
		}
		
	}

	public long getDelay() {
		return delay;
	}
	
	public Long setReleaseTime() {
		releaseTime = System.currentTimeMillis();
		return releaseTime;
	}
	
	public boolean isLegitKeyPress() {
		return (System.currentTimeMillis() - releaseTime) > 30;
	}
	
	private class ActionRunner implements Runnable {
		private PieceAction action;
		public ActionRunner(PieceAction newAction) {
			action = newAction;
		}
		public void run() {
			System.out.println(SwingUtilities.isEventDispatchThread());
			if (action.isPressed) {
				if (!Game.me.tryToMovePiece(action)) {
					action.future.cancel(false);
					action.isMoving = false;
				}
			}
		}
	}

}