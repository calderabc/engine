package engine.swing;

import engine.GraphicsEngine;
import engine.puzzle.tetris.swing.TetrisSprite;

public class Swing extends GraphicsEngine {

	public Swing() {
		screenClass = SwingScreen.class;
		
		// TODO: Choose dynamically
		visualClass = TetrisSprite.class;
	}
}
