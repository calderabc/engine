/*
This is a tile-matching puzzle video game engine.
Copyright (C) 2018 Aaron Calder

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
*/

package engine;

import java.io.Serializable;

public class Score extends Number {
	public final Calculator calculator;
	public final int clearCountPerLevel;

	@FunctionalInterface
	public interface Calculator extends Serializable {
		long calculate(Number level, int clearCount);
	}
	
	protected Score(Game game,
	                Calculator newCalculator,
	                int newClearCountPerLevel,
	                int newLength) {
		super(game, 0, newLength);
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