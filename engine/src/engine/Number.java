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

// This is only meant to work for positive numbers.
// Negative values have no meaning, won't work.
public class Number {
	private volatile long value;
	private final Digit[] digits;

	public Number(Game game, int row, int newLength) {
		digits = new Digit[newLength];
		for (int i = 0; i < newLength; i++) {
			digits[i] =  new Digit(game, new Coordinates(i, row));
		}
	}
	
	public final Number add(long offset) {
		value += offset;
		set(value);
		return this;
	}
	
	public final Number add(Number other) {
		return add(other.value);
	}
	
	public final Number set(long newValue) {
		value = newValue;
		for (int i = digits.length - 1; i >= 0; i--) {
			digits[i].set((byte)(newValue % 10));
			newValue = newValue / 10; 
		}
		
		return this;
	}

	public final long get() {
		return value;
	}
}
