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

package engine.puzzle.columns;

import engine.Game;
import engine.Score;

public class ColumnsScore extends Score {

	public ColumnsScore(Game game) {
		// TODO: Add scoring code. This is just default return 0.
		this(game, (level, clearCount) -> { return 0; }, 20, 7);

	}
	protected ColumnsScore(Game game,
	                       Calculator newCalculator,
	                       int newClearCountPerLevel,
	                       int newLength) {
		super(game, newCalculator, newClearCountPerLevel, newLength);
		// TODO Auto-generated constructor stub
	}

}
