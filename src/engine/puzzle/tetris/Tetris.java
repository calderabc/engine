package engine.puzzle.tetris;

import engine.puzzle.Game;
import engine.puzzle.PieceData;

public class Tetris extends Game {

	public static final PieceData TETRIS_PIECE_DATA = new PieceData();

	public Tetris() {
		super(TETRIS_PIECE_DATA);
	}

	public static void main(String argv[]) {
		//Tetris.profile = new Profile(Profile.option1);
		@SuppressWarnings("unused")
		Game tetrisGame = new Tetris();

		System.exit(0);
	}
}
