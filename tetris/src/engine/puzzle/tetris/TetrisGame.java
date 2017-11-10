package engine.puzzle.tetris;

import engine.puzzle.PuzzleGame;

public final class TetrisGame extends PuzzleGame {
	private TetrisGame(String newGameName, String newEngineName) {
		super(newGameName, newEngineName);
	}

	public static void main(String[] argv) {
		new TetrisGame("Tetris", "Swing");
	}

}
