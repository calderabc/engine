package tetris.coordinates;

/**
 * Generic three dimensional Cartesian coordinates.  Represents integer X, Y, and Z coordinates.  
 * 
 * @author Aaron Calder
 *
 */
public class Coordinates implements Cloneable, Comparable<Object>{
	
	public static class Double {
		public double x;
		public double y;
		public double z;
		
		
		public Double(double newX, double newY) {
			this(newX, newY, 0);
		}
		
		public Double(double newX, double newY, double newZ) {
			x = newX;
			y = newY;
			z = newZ;
		}
		
		public Double(Coordinates.Double other) {
			this(other.x, other.y, other.z);
		}
	}
	
	
	public int x;
	public int y;
	public int z;
	
	protected Coordinates() {
		this(0, 0, 0);
	}
	
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
	

	//*************** Override some methods from the Object class **************
	
	// Make Coordinates clonable
	@Override
	public Coordinates clone() throws CloneNotSupportedException
	{
		return (Coordinates) super.clone();
	}
	
	/**
	 *  a.equals(b) is true if the x, y, and z coordinates in 'a' equal 
	 *  the the corresponding x, y and z, coordinates in 'b'.
	 */
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
	
	
	// Overridden to fulfill hashCode's general contract since equals was also overridden.
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

	
	// TODO: I need to better define what it means to be less than or greater than for a coordinate.
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
}
