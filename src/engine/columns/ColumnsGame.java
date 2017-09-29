package engine.columns;

import engine.puzzle.PuzzleGame;

public class ColumnsGame extends PuzzleGame {
	public static void main(String argv[]) {
		me = new ColumnsGame();
		((PuzzleGame)me).run("Swing");
	}
}
