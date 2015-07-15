package tetris.coordinates;

public interface Positionable {
	public Positionable setPosition(int newX, int newY);
	public Positionable setX(int newX);
	public Positionable setY(int newY);
	public Position getPosition(); 
	public int getX();
	public int getY();
}
