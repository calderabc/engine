package engine.puzzle;

import engine.puzzle.Number;


public class Level extends Number {
	
	public Level(int newValue) {
		super(0, 1, 3, newValue);
	}
	
	public long next() {
		return add(1);
	}
}
