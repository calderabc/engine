package engine.puzzle;

public class Score extends Number {
	public final Calculator calculator;
	public final int clearCountPerLevel;

	@FunctionalInterface
	public interface Calculator {
		long calculate(Number level, int clearCount);
	}
	
	public Score(Calculator newCalculator, 
	             int newClearCountPerLevel, 
	             byte newLength, 
	             int newValue) {
		super(Number.Type.SCORE, newValue, newLength);
		calculator = newCalculator; 
		clearCountPerLevel = newClearCountPerLevel;
	}

	public Score(Calculator newCalculator, 
	             int newClearCountPerLevel, 
	             byte newLength) {
		this(newCalculator, newClearCountPerLevel, newLength, 0);
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