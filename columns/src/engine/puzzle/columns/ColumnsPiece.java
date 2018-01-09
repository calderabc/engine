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

import java.util.Random;

import engine.Coordinates;
import engine.Game;
import engine.Visual;
import engine.puzzle.Block;
import engine.puzzle.Piece;

public class ColumnsPiece extends Piece {

	private ColumnsPiece(Game game, int newBlockCount, int typeCount) {
		super(newBlockCount);
		for(int i = 0; i < newBlockCount; i++) {
			byte type = (byte) new Random().nextInt(typeCount);
			blocks[i] =
				new Block(game,
					new Coordinates(3, i),
					new Visual.Id("block", type),
					type
				);
		}

	}

	public ColumnsPiece(Game game) {
		this(game, 3, 6);
	}

	public ColumnsPiece(ColumnsPiece other) {
		super(other);
	}
	
	@Override
	protected Piece rotate(Coordinates offset) {
		int length = blocks.length;
		int shift = offset.x;

		int topPosition = 40000;
		for (int i = 0; i < length; i++) {
			int blockPos = blocks[i].pos.y;
			if (blockPos < topPosition) topPosition = blockPos;
		}

		// TODO: There's got to be a simpler way.  Work on it.
		for (int i = 0; i < length; i++) {
			int ordinal = blocks[i].pos.y - topPosition + shift;
			if (ordinal < 0) {
				ordinal += length;
			} else if (ordinal >= length) {
				ordinal -= length;
			}

			blocks[i].pos.y = topPosition + ordinal;
		}

		return this;
	}

}
