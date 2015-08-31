package engine;

import java.util.concurrent.ScheduledFuture;

public enum PieceAction {
	WARP ("warp down", 0),
	FALL ("fall", 2 / 1.0e9),
	DOWN ("move down", 20 / 1.0e9),
	LEFT ("move left", 6 / 1.0e9),
	RIGHT ("move right", 6 / 1.0e9),
	CLOCKWISE ("rotate clockwise", 6 / 1.0e9),
	COUNTERCLOCKWISE ("rotate counterclockwise", 6 / 1.0e9);
	
	
	
	private double speed;
	private long delay = 0; 
	private long releaseTime = System.currentTimeMillis();
	private long pressTime = System.currentTimeMillis();
	
	public String name;
	public ScheduledFuture<?> future;
	public boolean isMoving = false;  
	public boolean isPressed = false;
	
	
	private PieceAction(String newName, double newSpeed) {
		name = newName;
		speed = newSpeed;
		setDelayFromSpeed(newSpeed);
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
	
	
	public Double getSpeed() {
		return speed;
	}
	
	public Double setSpeed(double newSpeed) {
		if (this == PieceAction.RIGHT) {
			PieceAction.LEFT.speed = newSpeed;
			PieceAction.LEFT.setDelayFromSpeed(newSpeed); 
		}
		else if (this == PieceAction.LEFT) { 
			PieceAction.RIGHT.speed = newSpeed;
			PieceAction.RIGHT.setDelayFromSpeed(newSpeed); 
		} 
		else if (this == PieceAction.CLOCKWISE) { 
			PieceAction.COUNTERCLOCKWISE.speed = newSpeed;
			PieceAction.COUNTERCLOCKWISE.setDelayFromSpeed(newSpeed); 
		}
		else if (this == PieceAction.COUNTERCLOCKWISE) { 
			PieceAction.CLOCKWISE.speed = newSpeed;
			PieceAction.CLOCKWISE.setDelayFromSpeed(newSpeed); 
		}
		
		speed = newSpeed;
		setDelayFromSpeed(newSpeed);
		
		return speed;
	}
	
	private void setDelayFromSpeed(double newSpeed) {
		delay = Math.round(1 / newSpeed);
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
