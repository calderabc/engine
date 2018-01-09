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

public abstract class Part implements Movable {
	public Visual visual = null; 
	public Coordinates pos = null;

	public final Visual.Id visualId;
	// TODO: Half ass to have public non final variable.
	public Game game;

	public Part(Game newGame, Coordinates newPosition, Visual.Id newVisualId) {
		game = newGame;
		pos = new Coordinates(newPosition);
		visualId = newVisualId;
	}

	public Part(Part other) {
		game = other.game;
		pos = new Coordinates(other.pos);
		visual = (Visual)Reflection.newInstance(other.visual);
		visualId = other.visualId;
	}	

	public void terminate() {
		if (visual != null) { 
			game.screen.removePart(this);
		}
		visual = null;
	}

	// Implement the Movable interface.
	@Override
	public final Movable move(Coordinates offset) {
		pos.move(offset);
		
		return this;
	}
}