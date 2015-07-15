package tetris.parts;

import tetris.parts.Number;


public class Level extends Number {
	
	public Level(int newValue) {
		super(0, 1, 3, newValue);
	}
	
	public long next() {
		return add(1);
	}
}
