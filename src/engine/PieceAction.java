package engine;

import java.util.concurrent.ScheduledFuture;

public enum PieceAction {
	WARP ("warp down", 0),
	FALL ("fall", 2),
	DOWN ("move down", 20),
	LEFT ("move left", 6),
	RIGHT ("move right", 6),
	CLOCKWISE ("rotate clockwise", 6),
	COUNTERCLOCKWISE ("rotate counterclockwise", 6);
	
	// Delay between actions (moving/rotating the piece) in milliseconds.(?) 
	// TODO: Figure the apparent discord between milliseconds and 1.0e9 (nanosecond)
	// Why does setting the delay to "1.0e9 / newFrequency" work if things are 
	// done in terms of milliseconds (1.0e3 per second)?
	private long delay = 0; 
	private long releaseTime = System.currentTimeMillis();
	private long pressTime = System.currentTimeMillis();
	
	public String name;
	public ScheduledFuture<?> future;
	public boolean isMoving = false;  
	public boolean isPressed = false;
	
	
	private PieceAction(String newName, double newFrequency) {
		name = newName;
		delay = Math.round(1.0e9 / newFrequency);
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
}
