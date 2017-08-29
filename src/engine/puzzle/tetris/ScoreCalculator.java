package engine.puzzle.tetris;

import engine.puzzle.Level;

public enum ScoreCalculator {
	NINTENDO (2) {
			
		public long calculate(Level level, int rowCount) {
			long score = 0;
			
			switch(rowCount) {
				case 1: 
					score = 40 * level.getValue();
					break;
				case 2: 
					score = 100 * level.getValue();
					break;
				case 3: 
					score = 300 * level.getValue();
					break;
				case 4: 
					score = 1200 * level.getValue();
					break;
			}
			
			return score;
		}
	};
	
	public final int rowsPerLevel;
	
	private ScoreCalculator(int newRowsPerLevel) {
		rowsPerLevel = newRowsPerLevel;
	}
	
	public abstract long calculate(Level level, int rowcount); 
}
