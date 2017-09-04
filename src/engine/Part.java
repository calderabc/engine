package engine;

import engine.swing.Sprite;


@SuppressWarnings("serial")
public abstract class Part {
	public Visual visual = null; 

	public Part() {
	}

	public Part(Part other) {
		visual = other.visual;
	}	
	
}