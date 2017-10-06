package engine.tetris;

import engine.puzzle.PuzzleGame;

public final class TetrisGame extends PuzzleGame {
	
	public static void main(String argv[]) {
		me = new TetrisGame();
		((PuzzleGame)me).run("Swing");
		System.exit(0);
	}
	

}
