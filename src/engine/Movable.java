package engine;

/**
 * Interface Movable specifies the interface for objects that have the ability to move
 * from place to place within the Cartesian coordinate system X, Y, and Z.
 * 
 * 
 * @author Aaron Calder
 *
 */
public interface Movable {
	
	public Movable move(Coordinates offset);
	
	public Movable move(int offsetX, int offsetY);
	
	public Movable move(int offsetX, int offsetY, int offsetZ);
	
	public Movable moveX(int offsetX);
	
	public Movable moveY(int offsetY);
	
	public Movable moveZ(int offsetZ);
	
}

