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

package engine.puzzle;

import engine.Coordinates;
import engine.Game;

public abstract class SimpleBoard extends Board {
	protected final Block[][] blockMatrix;

	protected SimpleBoard(Coordinates newDimensions) {
		super(newDimensions);
		blockMatrix = new Block[dimensions.x][dimensions.y];
	}

	protected Block getBlock(Coordinates position) {
		return blockMatrix[position.x][position.y];
	}

	@Override
	protected Board landBlock(Block landingBlock) throws PositionOccupiedException {
		if (getBlock(landingBlock.pos) != null) {
			throw new PositionOccupiedException();
		}

		blockMatrix[landingBlock.pos.x][landingBlock.pos.y] = landingBlock;
		return this;
	}
}
