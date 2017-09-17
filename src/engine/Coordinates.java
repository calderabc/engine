package engine;

import java.io.Serializable;

/**
 * Generic three dimensional Cartesian coordinates.  Represents integer X, Y, and Z coordinates.  
 * 
 * @author Aaron Calder
 *
 */
// TODO: Not sure if I need to make this class implement Comparable.  Do more research. 
/* public class Coordinates implements Comparable<Object>{ */

public final class Coordinates implements Movable, Serializable {
	private static final long serialVersionUID = -3811798543403117929L;

	public static final Coordinates ORIGIN = new Coordinates(0, 0, 0);

	private final byte numOfDimensions;
	private int[] values;

	// I'm not going to add exceptions here.  I'll just assume the developer
	// Is smart enough to figure out that if they are getting an
	// ArrayIndexOutOfBounds exception it's because they're calling a 
	// function they shouldn't.
	public int x() { return values[0]; } 
	public int x(int newX) { return values[0] = newX; }
	public int y() { return values[1]; }
	public int y(int newY) { return values[1] = newY; }
	public int z() { return values[2]; }
	public int z(int newZ) { return values[2] = newZ; }
	
	public int get(int ordinal) {
		if (ordinal > numOfDimensions) {
			throw new IllegalArgumentException();
		}
		return values[ordinal];
	}

	public Coordinates(int... newValues) {
		numOfDimensions = (byte)newValues.length;
		if (numOfDimensions == 0) {
			throw new IllegalArgumentException();
		}
		setValues(newValues);
	}
	
	public Coordinates(Coordinates other) {
		numOfDimensions = other.numOfDimensions;
		setValues(other.values);
	}
	
	@Override
	public final Coordinates move(Coordinates offset) {
		if (offset.numOfDimensions > numOfDimensions) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < offset.numOfDimensions; i++) {
			values[i] += offset.values[i];
		}

		return this;
	}	

	private void setValues(int[] newValues)	 {
		values = new int[numOfDimensions];
		for (int i = 0; i < numOfDimensions; i++) {
			values[i] = newValues[i];
		}
	}
	
}