package engine;

/**
 * A Position Object is a Coordinates object with the addition of several move methods.  
 * 
 * @author Aaron Calder
 *
 */
public class Position extends Coordinates implements  Movable {
	
	/**
	 * Constructs Position based the values in the specified Coordinates.
	 * @param newPosition Coordinates containing x, y, and z values to be 
	 * 					  assigned to this Position.
	 */
	public Position(Coordinates newPosition) {
		this(newPosition.x, newPosition.y);
	}
	
	/**
	 * Constructs Coordinates from the specified x and y values, z is set to default 0.
	 * @param newX initial x coordinate value
	 * @param newY initial y coordinate value
	 */
	public Position(int newX, int newY) {
		super(newX, newY);
	}
	
	public Position(int newX, int newY, int newZ) {
		super(newX, newY, newZ);
	}
	
	/**
	 * Relocates Position by adding the x, y, and z values of the specified 
	 * offset Position to the x, y and z values of the current Position.
	 * @param offset Coordinates object whose x, y, and z values are added
	 * 				 to the current Position's values to determine the 
	 * 				 new Position.
	 */
	public final synchronized Position move(Coordinates offset) {
		move(offset.x, offset.y, offset.z);
		return this;
	}
	
	
	/**
	 * Relocates Position by adding the specified offset values for x and y
	 * to the current Position. The z value does not change.
	 * @param offsetX amount to move Position in the x direction
	 * @param offsetY amount to move Position in the y direction
	 * @return reference to the moved Position object
	 */
	@Override
	public final synchronized Position move(int offsetX, int offsetY) {
		move(offsetX, offsetY, 0);
		return this;
	}
	

	/**
	 * Relocates Position by adding the specified offset values for x, y, and z 
	 * to the current Position.
	 * @param offsetX amount to move Position in the x direction
	 * @param offsetY amount to move Position in the y direction
	 * @param offsetZ amount to move Position in the z direction
	 * @return reference to the moved Position object
	 */
	@Override
	public final synchronized Position move(int offsetX, int offsetY, int offsetZ) {
		x += offsetX;
		y += offsetY;
		z += offsetZ;
		return this;
	}
	
	/**
	 * Sets the Coordinate values based on the values in the specified Coordinates.
	 * @param other Coordinates containing values to be assigned to this Coordinates.
	 * @return reference to the Coordinates object just set
	 */
	public final synchronized Position set(Coordinates other) {
		x = other.x;
		y = other.y;
		z = other.z;
		return this;
	}
}
