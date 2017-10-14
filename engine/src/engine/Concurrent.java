package engine;

public final class Concurrent {
	private Concurrent() {}

	public static void run(Runnable... runners) {
		Thread[] threads = new Thread[runners.length];
		int i = 0;
		for (Runnable runner : runners) {
			threads[i] = new Thread(runner);
			threads[i++].start();
		}
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
