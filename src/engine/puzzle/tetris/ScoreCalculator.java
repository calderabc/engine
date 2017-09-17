package engine.puzzle.tetris;

import engine.puzzle.Number;

public enum ScoreCalculator {
	// TODO: For testing.  Switch to 10 for actual thing.
	NINTENDO (2) {
		public long calculate(Number level, int rowCount) {
			long multiplier = 0;
			switch(rowCount) {
				case 1: 
					multiplier = 40;
					break;
				case 2: 
					multiplier = 100;
					break;
				case 3: 
					multiplier = 300;
					break;
				case 4: 
					multiplier = 1200;
					break;
			}
			return multiplier * level.get();
		}
	};
	
	public final int rowsPerLevel;
	
	private ScoreCalculator(int newRowsPerLevel) {
		rowsPerLevel = newRowsPerLevel;
	}
	
	public abstract long calculate(Number level, int rowcount); 
}
