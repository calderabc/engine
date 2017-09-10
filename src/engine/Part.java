package engine;

import engine.puzzle.Game;

public abstract class Part {
	public Visual visual = null; 

	public Part() {
	}

	public Part(Part other) {
		visual = other.visual;
	}	
	
	public void terminate() {
		if (visual != null) { 
			Game.me.screen.removePart(this);
		}
		visual = null;
	}
}