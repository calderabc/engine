package engine.puzzle.columns;

import engine.Game;
import engine.puzzle.Score;

public class ColumnsScore extends Score {

	public ColumnsScore(Game game) {
		// TODO: Add scoring code. This is just default return 0.
		this(game, (level, clearCount) -> { return 0; }, 20, 7);

	}
	protected ColumnsScore(Game game,
	                       Calculator newCalculator,
	                       int newClearCountPerLevel,
	                       int newLength) {
		super(game, newCalculator, newClearCountPerLevel, newLength);
		// TODO Auto-generated constructor stub
	}

}
