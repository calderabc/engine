package engine.puzzle.columns;

import engine.Visual;
import engine.puzzle.Block;
import engine.graphics2d.ImageType;
import engine.graphics2d.Sprite;

import java.awt.image.BufferedImage;

public class ColumnsBlockSprite extends Sprite {
	public ColumnsBlockSprite(Visual other) {
		super(other);
	}

	public ColumnsBlockSprite(Block newPart, ImageType imageType, Id newId) {
		super(newPart, imageType, newId);
	}
}
