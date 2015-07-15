package tetris.coordinates;

/**
 * A Position Object is a Coordinates object with the addition of several move methods.  
 * 
 * @author Aaron Calder
 *
 */
public class Position extends Coordinates implements Positionable, Movable {
	
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
		moveX(offsetX);
		moveY(offsetY);
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
		moveX(offsetX);
		moveY(offsetY);
		moveZ(offsetZ);
		return this;
	}
	
	
	/**
	 * Relocates Position, in the x direction, by the specified offset amount.
	 * @param offsetX offset to move Position in the X direction
	 * @return reference to the moved Position object
	 */
	@Override
	public final synchronized Position moveX(int offsetX)
	{
		x += offsetX;
		return this;
	}
	
	/**
	 * Relocates Position, in the y direction, by the specified offset amount.
	 * @param offsetY offset to move Position in the Y direction.
	 * @return reference to the moved Position object
	 */
	@Override
	public final synchronized Position moveY(int offsetY)
	{
		y += offsetY;
		return this;
	}
	
	/**
	 * Relocates Position, in the z direction, by the specified offset amount.
	 * @param offsetZ amount to move Position in the Z direction.
	 * @return reference to the moved Position object
	 */
	@Override
	public final synchronized Position moveZ(int offsetZ)
	{
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
	
	
	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}
	
	
	/**
	 * Sets the X and Y Coordinate values and leave the Z Coordinate value unchanged.
	 * @param newX x coordinate value
	 * @param newY y coordinate value
	 * @return reference to the Coordinates object just set
	 */
	@Override
	public Position setPosition(int newX, int newY) {
		setX(newX);
		setY(newY);
		return this;
	}
	
	/**
	 * Sets the x Coordinate value.
	 * @param newX 
	 * @return reference to the Coordinate object
	 */
	@Override
	public final synchronized Position setX(int newX) {
			x = newX;
			return this;
	}
	
	
	/**
	 * Sets the y coordinate value.
	 * @param newY
	 * @return reference to the Coordinate object
	 */
	@Override
	public final synchronized Position setY(int newY) {
		y = newY;
		return this;
	}

	@Override
	public Position getPosition() {
		return this;
	}

}
