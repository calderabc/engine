package engine.puzzle.columns;

import engine.Visual;
import engine.puzzle.Block;
import engine.graphics2d.ImageType;
import engine.graphics2d.Sprite;

import java.awt.image.BufferedImage;

public class ColumnsBlockSprite extends Sprite {
	public static int go;

	static {
		ImageType columnBlock = new ImageType("columns", Block.class, BufferedImage.class);

		/*
		for (int i = 0; i < 6; i++) {
			imageListMap.put(new Visual.Id((byte) 3, (byte) i),
			new ImageList(columnBlock, new Coordinates(i * 48, 0)));

		}
		*/
	}

	public ColumnsBlockSprite(Visual other) {
		super(other);
	}

	public ColumnsBlockSprite(Block newPart, Id newId) {
		super(newPart, newId);
	}
}
