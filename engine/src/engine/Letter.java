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

public class Letter extends Symbol {

	public Letter(Game newGame, Coordinates newPosition, char newChar) {
		// TODO: Throw error if newChar is not an English letter.
		// 'A' to 'Z' -> 0 to 25, 'a' to 'z' -> 26 to 51
		super(newGame, newPosition, new Visual.Id(
			"Letter",
			(Character.isLowerCase(newChar)) ? (byte)(newChar - 'a' + 26)
			                                 : (byte)(newChar - 'A')
		));
	}

	public char get() {
		if (value < 26)
			return (char)(value + 'A');
		return (char)(value + 'a' - 26);
	}
}
