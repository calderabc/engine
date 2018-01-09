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
import java.util.function.BiFunction;

/**
 * Generic three dimensional Cartesian coordinates.  Represents integer X, Y, and Z coordinates.  
 * 
 * @author Aaron Calder
 *
 */

public final class Coordinates implements Movable, Serializable, Cloneable {
	private static final long serialVersionUID = -3811798543403117929L;

	public static final Coordinates ORIGIN = new Coordinates();

	public int x;
	public int y;
	public int z;

	public Coordinates(int newX, int newY, int newZ) {
		x = newX;
		y = newY;
		z = newZ;
	}

	public Coordinates(int newX, int newY) {
		x = newX;
		y = newY;
	}

	public Coordinates(int newX) {
		x = newX;
	}

	public Coordinates(int[] integers) {
		switch (integers.length) {
			case 3: z = integers[2];
			case 2: y = integers[1];
			case 1: x = integers[0];
			default: // TODO: throw exception here.
		}


	}

	// Redundant. Faster than loop conversion to int[] then private method call.
	public Coordinates(String[] intStrings) {
		switch (intStrings.length) {
			case 3: z = Integer.valueOf(intStrings[2]);
			case 2: y = Integer.valueOf(intStrings[1]);
			case 1: x = Integer.valueOf(intStrings[0]);
			default: // TODO: throw exception here.
		}
	}

	public Coordinates() {}

	public Coordinates(Coordinates other) {
		this(other.x, other.y, other.z);
	}
	
	@Override
	public final Coordinates move(Coordinates offset) {
		x += offset.x;
		y += offset.y;
		z += offset.z;
		return this;
	}

	public final Coordinates moveX(int xOffset) {
		x += xOffset;
		return this;
	}

	public final Coordinates moveY(int yOffset) {
		y += yOffset;
		return this;
	}

	public Coordinates scale(Coordinates scaleFactor) {
		x *= scaleFactor.x;
		y *=  scaleFactor.y;
		z *= scaleFactor.z;
		return this;
	}

	public Coordinates scale(double scaleFactor) {
		// TODO: Make sure 'x *= scaleFactor' has the same result.
		// TODO: Which is Convert x to double, multiply, then convert to int.
		x = (int)(x * scaleFactor);
		y = (int)(y * scaleFactor);
		z = (int)(z * scaleFactor);
		return this;
	}

	public Coordinates invert() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}

	@Override
	public Coordinates clone() {
		return new Coordinates(this);
	}

	public static Coordinates max(Coordinates... coordinates) {
		// Only works and is useful for positive coordinates.
		int maxX = 0;
		int maxY = 0;
		int maxZ = 0;
		for (Coordinates curr : coordinates) {
			if (curr.x > maxX) maxX = curr.x;
			if (curr.y > maxY) maxY = curr.y;
			if (curr.z > maxZ) maxZ = curr.z;
		}
		return new Coordinates(maxX, maxY, maxZ);
	}

	// Much more elegant with overloaded operators in C++.
	public static Coordinates add(Coordinates a, Coordinates b) {
		return new Coordinates(a.x + b.x, a.y + b.y, a.z + b.z);
	}

	public static Coordinates subtract(Coordinates a, Coordinates b) {
		return new Coordinates(a.x - b.x, a.y - b.y, a.z - b.z);
	}

	public static Coordinates multiply(Coordinates a, Coordinates b) {
		return new Coordinates(a.x * b.x, a.y * b.y, a.z * b.z);
	}

	public static Coordinates inversion(Coordinates a) {
		return new Coordinates(-a.x, -a.y, -a.z);
	}


	// Lambdas are elegant, but I don't want the overhead: autoboxing, extra objects, etc.
	// TODO: See if lambdas would be worth it here.
	/*
	public static Coordinates operation(BiFunction<Integer, Integer, Integer> function,
	                                    Coordinates a,
	                                    Coordinates b) {
		return new Coordinates(function.apply(a.x, b.x),
		                       function.apply(a.y, b.y),
		                       function.apply(a.z, b.z));
	}

	public static BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
	public static BiFunction<Integer, Integer, Integer> subtract = (a, b) -> a - b;
	public static BiFunction<Integer, Integer, Integer> multiply = (a, b) -> a * b;

	// Look how pretty the function calls would be.
	// operation(Coordinates.add, a, b);

	*/

	@Override
	public String toString() {
		return "(" + Integer.toString(x) + ", "
		       + Integer.toString(y) + ", "
		       + Integer.toString(z) + ")";
	}
}