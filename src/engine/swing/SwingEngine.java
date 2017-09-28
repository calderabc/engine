package engine.swing;

import engine.GraphicsEngine;
import engine.Part;
import engine.swing.puzzle.ImageType;

public class SwingEngine extends GraphicsEngine {

	@Override
	protected Class<? extends Sprite> getVisualClass(Part part) {
		return ImageType.valueOf(part.getClass()
		                             .getSimpleName()
		                             .toUpperCase()).SPRITE_CLASS;
	}
}
