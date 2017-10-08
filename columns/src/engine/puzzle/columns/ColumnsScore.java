package engine.puzzle.columns;

import engine.puzzle.Score;

public class ColumnsScore extends Score {

	public ColumnsScore() {
		// TODO: Add scoring code. This is just default return 0.
		this((level, clearCount) -> { return 0; }, 20, 7);

	}
	protected ColumnsScore(Calculator newCalculator, 
	                       int newClearCountPerLevel, 
	                       int newLength) {
		super(newCalculator, newClearCountPerLevel, newLength);
		// TODO Auto-generated constructor stub
	}

}
