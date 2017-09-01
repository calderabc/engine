package engine;

import engine.swing.Sprite;


@SuppressWarnings("serial")
public abstract class Part {
	public Coordinates pos;
	public Visual visual = null; 
	
	public Part() {
	}

	public Part(int newX, int newY) {
		pos = new Coordinates(newX, newY);
	}
	
	public Part(Coordinates newPosition) {
		pos = new Coordinates(newPosition); 
	}
	
	public Part(Part other) {
		pos = new Coordinates(other.pos);
	}	
	
}