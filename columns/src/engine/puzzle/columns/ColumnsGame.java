package engine.puzzle.columns;

import engine.puzzle.PuzzleGame;

public final class ColumnsGame extends PuzzleGame {
	private ColumnsGame(String newGameName, String newEngineName) {
		super(newGameName, newEngineName);
	}

	public static void main(String[] argv) {
		new ColumnsGame("Columns", "Swing");
	}

}
