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


public class Digit extends Symbol {

	public Digit(Game game, Coordinates newPosition, byte newValue) {
		super(game, newPosition, new Visual.Id("Digit"));
		// Don't use setter because visual may not be initialized yet.
		this.value = newValue;

		game.screen.addPart(this);
	}
	
	public Digit(Game game, Coordinates newPosition) {
		this(game, newPosition, (byte)0);
	}
	
	public byte get() {
		return value;
	}
	
	public Digit set(byte newValue) {
		if (value > 9 || value < 0) {
			throw new IllegalArgumentException();
		}
		byte oldValue = value;
		value = newValue;
		if (value != oldValue && visual != null) {
			// If the value has changed update the visual.
				visual.update(this);
		}
		
		return this;
	}
}
