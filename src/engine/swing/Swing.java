package engine.swing;

import engine.Game;
import engine.GraphicsEngine;
import engine.Part;
import engine.swing.puzzle.ImageType;

public class Swing extends GraphicsEngine {

	public Swing(Game game) {
		super(game, SwingScreen.class);
	}

	@Override
	protected Class<? extends Sprite> getVisualClass(Part part) {
		// Holy reflection Batman!
		return ImageType.valueOf(part.getClass()
		                             .getSimpleName()
		                             .toUpperCase()).SPRITE_CLASS;
	}
}
