package engine;

import engine.puzzle.PuzzleGame;

public abstract class Part implements Movable {
	public Visual visual = null; 
	public Coordinates pos = null;

	public Part(Coordinates newPosition) {
		pos = new Coordinates(newPosition);
	}

	public Part(Part other) {
		pos = new Coordinates(other.pos);
		visual = PuzzleGame.me.engine.newVisual(other.visual);
		//visual = other.visual;
	}	

	protected void initVisual(Visual.Id newId) {
		visual = PuzzleGame.me.engine.newVisual(this, newId);
		visual.update(this);
	}
	
	public void terminate() {
		if (visual != null) { 
			PuzzleGame.me.screen.removePart(this);
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