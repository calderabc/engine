package engine.columns.swing;

import engine.Coordinates;
import engine.Visual;
import engine.puzzle.Block;
import engine.graphics2d.ImageType;
import engine.graphics2d.ImageType.ScanDirection;
import engine.swing.ImageList;
import engine.graphics2d.Sprite;

public class BlockSprite extends Sprite {
	public static int go;

	static {
		ImageType columnBlock = new ImageType("columns.png",
		                                     new Coordinates(48, 48),
		                                     new Coordinates(48, 48),
		                                     1,
		                                     ScanDirection.HORIZONTAL);


		for (int i = 0; i < 6; i++) {
			imageListMap.put(new Visual.Id((byte) 3, (byte) i),
			new ImageList(columnBlock, new Coordinates(i * 48, 0)));

		}
	}

	public BlockSprite(Visual other) {
		super(other);
	}

	public BlockSprite(Block newPart, Id newId) {
		super(newPart, newId);
	}
}
