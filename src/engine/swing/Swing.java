package engine.swing;

import engine.GraphicsEngine;
import engine.Part;

public class Swing extends GraphicsEngine {

	public Swing() {
		screenClass = SwingScreen.class;
	}

	@Override
	protected Class<? extends Sprite> getVisualClass(Part part) {
		// Holy reflection Batman!
		return ImageType.valueOf(part.getClass()
		                             .getSimpleName()
		                             .toUpperCase()).SPRITE_CLASS;
	}
}
