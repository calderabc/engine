package engine;

import engine.puzzle.tetris.TetrisGame;

public abstract class Part {
	public Visual visual = null; 

	public Part() {
	}

	public Part(Part other) {
		visual = other.visual;
	}	
	
	public void terminate() {
		if (visual != null) { 
			TetrisGame.me.screen.removePart(this);
		}
		visual = null;
	}
}