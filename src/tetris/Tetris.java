package tetris;

import tetris.parts.Game;

public class Tetris  {

	public static void main(String argv[]) {
		Game game = new Game();
		Thread abc = new Thread(game);
		abc.isDaemon();
		abc.start();

		try {
			abc.join();
		} catch (InterruptedException e) { }

		System.exit(0);

	}
}
