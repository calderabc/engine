package engine;

/**
 * Generic three dimensional Cartesian coordinates.  Represents integer X, Y, and Z coordinates.  
 * 
 * @author Aaron Calder
 *
 */
// TODO: Not sure if I need to make this class implement Comparable.  Do more research. 
/* public class Coordinates implements Comparable<Object>{ */

public class Coordinates implements Movable {
	
	static public final Coordinates ORIGIN = new Coordinates(0, 0, 0);

	public int x;
	public int y;
	public int z;
	
	/**
	 * Constructs Coordinates from the specified X and Y values, setting Z to default 0.
	 * @param newX initial x coordinate value
	 * @param newY initial y coordinate value
	 */
	public Coordinates(int newX, int newY) {
		this(newX, newY, 0);
	}
	
	/**
	 * Constructs Coordinates from the specified X, Y, and Z values.
	 * @param newX initial x coordinate value.
	 * @param newY initial y coordinate value.
	 * @param newZ initial z coordinate value.
	 */
	public Coordinates(int newX, int newY, int newZ) {
		x = newX;
		y = newY;
		z = newZ;
	}
	
	/**
	 * Constructs Coordinates based on the values in the specified Coordinates. 
	 * @param other Coordinates object containing values to be assigned to 
	 * 				the new Coordinates object.
	 */
	public Coordinates(Coordinates other) {
		this(other.x, other.y, other.z);
	}
	
	private boolean isBetween(int a, int b, int c) {
		return !(a < b && a < c || a > c && a > b);
	}

	// Determine if this point is within rectangle formed by two points.
	public boolean isWithin(Coordinates a, Coordinates b) {
		return isBetween(x, a.x, b.x) && isBetween(y, a.y, b.y);
	}
	
	@Override
	public final Coordinates move(Coordinates offset) {
		return move(offset.x, offset.y, offset.z);
	}	
	
	@Override
	public final Coordinates move(int offsetX, int offsetY) {
		return move(offsetX, offsetY, 0);
	}

	@Override
	public final Coordinates move(int offsetX, int offsetY, int offsetZ) {
		x += offsetX;
		y += offsetY;
		z += offsetZ;
		return this;
	}
}


	

	//*************** Override some methods from the Object class **************
	
	/**
	 *  a.equals(b) is true if the x, y, and z coordinates in 'a' equal 
	 *  the the corresponding x, y and z, coordinates in 'b'.
	 */
	/*
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Coordinates)) 
			return false;
		Coordinates other = (Coordinates) obj;
		
		synchronized(this) {
			if (x != other.x || y != other.y || z != other.z)
				return false;
		}
		
		return true;
	}
	*/
	
	
	// Overridden to fulfill hashCode's general contract since equals was also overridden.
	/*
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 13;
		
		synchronized(this) {
			result = prime * result + x;
			result = prime * result + y;
			result = prime * result + z;
		}
		
		return result;
	}
	*/

	
	// TODO: I need to better define what it means to be less than or greater than for a coordinate.
	/*
	@Override
	public int compareTo(Object o) throws ClassCastException {
		if (!(o instanceof Coordinates))
			throw new ClassCastException();
		
		Coordinates other = (Coordinates) o;
		int result = 0;
		
		synchronized(this) {
			if (x != other.x) 
				result = (x > other.x) ? 1 : -1;
			else if (y != other.y)
				result = (y > other.y) ? 1 : -1;
			else if (z != other.z)
				result = (y > other.y) ? 1 : -1;
		}
		
		return result; 
	}
	*/