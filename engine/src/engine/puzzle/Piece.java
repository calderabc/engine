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

import java.lang.reflect.InvocationTargetException;

import engine.Coordinates;
import engine.Game;
import engine.Reflection;


public abstract class Piece implements Cloneable {
	protected final Block[] blocks;
	
	protected Piece(int newBlockCount) {
		blocks = new Block[newBlockCount];	
	}
	
	public Piece(Piece other) {
		Block[] otherBlocks = other.blocks;
		blocks = new Block[otherBlocks.length];
		int i = 0;
		for (Block currOtherBlock: otherBlocks) {
			blocks[i++] = new Block(currOtherBlock);
		}
	}
	
	public final Piece newPiece() {
		return (Piece)Reflection.newInstance(
			getClass(),
			new Reflection.ClassAndObject(Game.class, blocks[0].game)
		);
	}

	@Override
	protected Piece clone() {
			Class<? extends Piece> myClass = this.getClass();
			try {
				return myClass.getConstructor(myClass).newInstance(this);
			} catch (InstantiationException | IllegalAccessException 
			         | IllegalArgumentException | InvocationTargetException 
			         | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		return null;
	}

	public void updateVisual() {
		for (Block block : blocks) {
			block.visual.update(block);
		}
	}

	protected Piece move(Coordinates offset) {
		for (Block currBlock: blocks) {
			currBlock.move(offset);
		}
		return this;
	}
	
	protected abstract Piece rotate(Coordinates offset);
		

	public Piece move(PieceAction action) {
		// TODO: Maybe lambda/method-reference this.
		return (action.type == PieceAction.Type.MOVE)
			? move(action.offset)
			: rotate(action.offset); 
	}
}
