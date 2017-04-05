package engine.puzzle;


import tetris.RowsCleared;
import tetris.ScoreCalculator;

public class Score extends Number {
	private ScoreCalculator calculator;
	
	
	public Score(ScoreCalculator newCalculator, int newSize, int newValue) {
		super(0, 0, newSize, newValue);
		
		calculator = newCalculator; 
	}
	
	
	public void updateScore(Level level, int rowCount) {
		add(calculator.calculate(level, rowCount));
	}
	
	public int getRowsPerLevel() {
		return calculator.rowsPerLevel;
	}
	
	public boolean isLevelUp(Level level, RowsCleared rowsCleared) {
		System.out.println(rowsCleared.getValue());
		System.out.println(level.getValue());
		return rowsCleared.getValue() 
			>= (level.getValue() * calculator.rowsPerLevel);
		
	}
}
