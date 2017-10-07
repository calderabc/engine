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

	public Coordinates scale(Coordinates scaleFactor) {
		x *= scaleFactor.x;
		y *=  scaleFactor.y;
		z *= scaleFactor.z;
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
		return new Coordinates(x, y, z);
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

}