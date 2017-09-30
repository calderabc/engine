package engine.swing;

import engine.GraphicsEngine;
import engine.Part;
import engine.swing.puzzle.BlockSprite;
import engine.swing.puzzle.ColumnsSprite;
import engine.swing.puzzle.DigitSprite;

public class SwingEngine extends GraphicsEngine {

	static {
		// Poke the classes.
		ColumnsSprite.go = 0;
		//BlockSprite.go = 0;
		DigitSprite.go = 0;

	}

	@Override
	protected Class<? extends Sprite> getVisualClass(Part part) {
		return Sprite.visualMap.get(part.getClass());
	}
}
