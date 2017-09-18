package engine;

import engine.puzzle.tetris.TetrisGame;

public abstract class Part {
	public Visual visual = null; 

	protected Part() { }

	public Part(Part other) {
		visual = TetrisGame.me.engine.newVisual(other.visual);
		//visual = other.visual;
	}	
	
	public void terminate() {
		if (visual != null) { 
			TetrisGame.me.screen.removePart(this);
		}
		visual = null;
	}
}