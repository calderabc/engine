package engine.puzzle;

import engine.graphics2d.ImageType;

import java.io.Serializable;

public class Score extends Number {
	public final Calculator calculator;
	public final int clearCountPerLevel;

	@FunctionalInterface
	public interface Calculator extends Serializable {
		long calculate(Number level, int clearCount);
	}
	
	protected Score(Calculator newCalculator, 
	             int newClearCountPerLevel, 
	             int newLength) {
		super(Number.Type.SCORE, newLength);
		calculator = newCalculator; 
		clearCountPerLevel = newClearCountPerLevel;
	}

	public void update(Number level, int clearCount) {
		add(calculator.calculate(level, clearCount));
	}
	
	public Number checkLevel(Number level, Number clearCount) {
		while (level.get() * clearCountPerLevel < clearCount.get()) {
			level.add(1);
		}
		return level;
	}
	
}