package engine.swing;

import engine.GraphicsEngine;

public class Swing extends GraphicsEngine {
	public Swing() {
		screenClass = SwingScreen.class;
		visualClass = Sprite.class;
	}

}
