package engine;

public abstract class Part implements Movable {
	public Visual visual = null; 
	public Coordinates pos = null;

	public final Visual.Id visualId;
	// TODO: Half ass to have public non final variable.
	public Game game;

	public Part(Game newGame, Coordinates newPosition, Visual.Id newVisualId) {
		game = newGame;
		pos = new Coordinates(newPosition);
		visualId = newVisualId;
	}

	public Part(Part other) {
		game = other.game;
		pos = new Coordinates(other.pos);
		visual = (Visual)Reflection.newInstance(other.visual);
		visualId = other.visualId;
	}	

	public void terminate() {
		if (visual != null) { 
			game.screen.removePart(this);
		}
		visual = null;
	}

	// Implement the Movable interface.
	@Override
	public final Movable move(Coordinates offset) {
		pos.move(offset);
		
		return this;
	}
}