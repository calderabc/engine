package engine.swing;

import engine.GraphicsEngine;
import engine.puzzle.Game;
import engine.puzzle.tetris.swing.TetrisSprite;

public class Swing extends GraphicsEngine {

	public Swing(Game game) {
		screenClass = SwingScreen.class;
		
		// TODO: Choose dynamically
		visualClass = TetrisSprite.class;
	}
}
