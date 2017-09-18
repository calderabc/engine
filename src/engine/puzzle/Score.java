package engine.puzzle;


import engine.puzzle.tetris.ScoreCalculator;
import engine.puzzle.Number;

public class Score extends Number {
	private final ScoreCalculator calculator;
	
	public Score(ScoreCalculator newCalculator, byte newLength, int newValue) {
		super(Number.Type.SCORE, newValue, newLength);
		calculator = newCalculator; 
	}

	public Score(ScoreCalculator newCalculator, byte newLength) {
		this(newCalculator, newLength, 0);
	}
	
	public void update(Number level, int rowCount) {
		add(calculator.calculate(level, rowCount));
	}
	
	public int getRowsPerLevel() {
		return calculator.rowsPerLevel;
	}
}
