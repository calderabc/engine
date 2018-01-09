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
