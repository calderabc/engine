package engine.swing;

import engine.GraphicsEngine;
import engine.MovablePart;
import engine.puzzle.tetris.swing.ImageType;

public class Swing extends GraphicsEngine {

	public Swing() {
		screenClass = SwingScreen.class;
	}

	@Override
	protected Class<? extends Sprite> getVisualClass(MovablePart part) {
		// Holy reflection Batman!
		return ImageType.valueOf(part.getClass()
		                             .getSimpleName()
		                             .toUpperCase()).SPRITE_CLASS;
	}
}
