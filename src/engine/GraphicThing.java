package engine;

import engine.swing.SwingScreen;

public interface GraphicThing {
	public int className;
	
	default void GraphicThing() {
		className = 1;
	}
}
