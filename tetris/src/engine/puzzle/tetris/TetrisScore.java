package engine.puzzle.tetris;

import engine.Game;
import engine.Score;

public class TetrisScore extends Score {
	private enum ScoreType { NES }
	
	public TetrisScore(Game game) {
		super(game, getScoreCalculator(ScoreType.NES), 2, (byte)7);
	}

	public static Score.Calculator getScoreCalculator(ScoreType type) {
		switch (type) {
			case NES : 
				return (level, rowCount) -> {
					int[] multipliers = {40, 100, 300, 1200};
					return multipliers[rowCount - 1] * level.get();
				}; 
			default: return null;
		} 
	}

}
