package tetris.parts;

public final class Clock implements Runnable {
	
	private long startTime;
	private long delay;
	
	Clock(int newDelay) {
		startTime = System.nanoTime();
		delay = newDelay;
	}
	
	public final void setDelay(int newDelay) {
		delay = newDelay;
	}
	
	public final boolean isFinished() {
		long myTime = System.nanoTime();
		
		if (myTime >= startTime + delay) {
			startTime += delay;
			return true;
		} else 
			return false;
	}

	@Override
	public void run() {
		
	}

}
