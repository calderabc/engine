package tetris.parts;

import tetris.coordinates.Coordinates;
import tetris.coordinates.Position;
import tetris.coordinates.Positionable;

public abstract class StaticPart<T extends Part<?>> extends Part<T> implements Positionable {
	private final Position pos;
	
	
	public StaticPart(int newX, int newY) {
		pos = new Position(newX, newY);
	}
	
	public StaticPart(Coordinates newPosition) {
		pos = new Position(newPosition); 
	}
	
	public StaticPart(StaticPart<T> other) {
		pos = new Position(other.getPosition());
	}	
	
	@Override
	public int getX() {
		return pos.x;
	}

	@Override
	public int getY() {
		return pos.y;
		
	}

	@Override
	public Positionable setX(int newX) {
		pos.x = newX;
		return this;
	}

	@Override
	public Positionable setY(int newY) {
		pos.y = newY;
		return this;
	}

	@Override
	public Position getPosition() {
		return pos;
	}

	@Override
	public Positionable setPosition(int newX, int newY) {
		setX(newX);
		setY(newY);
		
		return this;
	}
	
}
