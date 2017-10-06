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
}

