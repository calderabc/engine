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

	//public static final Coordinates ORIGIN = new Coordinates(0, 0, 0);
	public static final Coordinates ORIGIN = new Coordinates(0);

	private int[] values;

	// I'm not going to add exceptions here.  I'll just assume the developer
	// Is smart enough to figure out that if they are getting an
	// ArrayIndexOutOfBounds exception it's because they're calling a 
	// function they shouldn't.
	public int x() { return values[0]; } 
	public int x(int newX) { return values[0] = newX; }
	public int y() { 
		if (values.length < 2) {
			return 0;
		}
		return values[1]; 
	}
	public int y(int newY) { return values[1] = newY; }
	public int z() {
		if (values.length < 3) {
			return 0;
		}
		return values[2]; 
	}
	public int z(int newZ) { return values[2] = newZ; }
	
	public int get(int ordinal) {
		if (ordinal >= values.length) {
			return 0;
		}
		return values[ordinal];
	}

	public Coordinates(int... newValues) {
		if (newValues.length == 0) {
			throw new IllegalArgumentException();
		}
		setValues(newValues);
	}
	
	public Coordinates(Coordinates other) {
		setValues(other.values);
	}
	
	@Override
	public final Coordinates move(Coordinates offset) {
		if (offset.values.length > values.length) {
			int[] oldValues = values;
			values = new int[offset.values.length];
			for (int i = 0; i < values.length; i++) {
				values[i] = (i < oldValues.length) ? oldValues[i]
				                                   : 0;
			}
		}
		for (int i = 0; i < offset.values.length; i++) {
			values[i] += offset.values[i];
		}

		return this;
	}	

	private void setValues(int[] newValues)	 {
		values = new int[newValues.length];
		for (int i = 0; i < newValues.length; i++) {
			values[i] = newValues[i];
		}
	}
	
	// TODO: This is basically a scale transform.  Maybe I can reuse some standard java code.
	public Coordinates scale(Coordinates scaleFactor) {
		if (scaleFactor.values.length > values.length) {
			int[] oldValues = values;
			values = new int[scaleFactor.values.length];
			for (int i = 0; i < values.length; i++) {
				values[i] = (i < oldValues.length) ? oldValues[i]
				                                   : 0;
			}
		}

		Coordinates scaledCoordinates = new Coordinates(this);
		for (int i = 0; i < scaleFactor.values.length; i++) {
			scaledCoordinates.values[i] *= scaleFactor.get(i);
		}
		return scaledCoordinates;
	}
}