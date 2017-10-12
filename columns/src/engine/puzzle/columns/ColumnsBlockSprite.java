package engine.puzzle.columns;

import engine.Visual;
import engine.puzzle.Block;
import engine.graphics2d.ImageType;
import engine.graphics2d.Sprite;

import java.awt.image.BufferedImage;

public class ColumnsBlockSprite extends Sprite {
	// TODO: This class is a placeholder, all constructors pass through.
	// TODO: Fix Reflection utility to instantiate "Sprite" in the case
	// TODO: ColumnsBlockSprite is not found so I can get rid of this class.
	public ColumnsBlockSprite(ColumnsBlockSprite other) {
		super(other);
	}

	public ColumnsBlockSprite(Block newPart, ImageType imageType, Id newId) {
		super(newPart, imageType, newId);
	}
}
