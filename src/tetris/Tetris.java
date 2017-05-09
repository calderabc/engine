package tetris;

import engine.puzzle.Game;

public class Tetris extends Game {
	public Tetris() {
		super(new TetrisPiecePool());
	}

	public static void main(String argv[]) {
		Tetris.profile = new Profile(Profile.option1);
		@SuppressWarnings("unused")
		Game tetrisGame = new Tetris();

		System.exit(0);
	}
}
