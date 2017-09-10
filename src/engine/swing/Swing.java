package engine.swing;

import engine.GraphicsEngine;
import engine.puzzle.tetris.TetrisGame;
import engine.puzzle.tetris.swing.TetrisSprite;

public class Swing extends GraphicsEngine {

	public Swing(TetrisGame game) {
		screenClass = SwingScreen.class;
		
		// TODO: Choose dynamically
		visualClass = TetrisSprite.class;
	}
}
