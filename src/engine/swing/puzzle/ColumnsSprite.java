package engine.swing.puzzle;

import engine.Coordinates;
import engine.Part;
import engine.Visual;
import engine.puzzle.Block;
import engine.swing.ImageList;
import engine.swing.Sprite;
import engine.swing.puzzle.ImageType.ScanDirection;

public class ColumnsSprite extends Sprite {
	public static int go;

	static {
		ImageType columnBlock = new ImageType("columns.png",
		                                     new Coordinates(48, 48),
		                                     new Coordinates(48, 48),
		                                     1,
		                                     ScanDirection.HORIZONTAL);

		visualMap.put(Block.class, ColumnsSprite.class);

		for (int i = 0; i < 6; i++) {
			imageListMap.put(new Visual.Id((byte) 3, (byte) i),
			new ImageList(columnBlock, new Coordinates(i * 48, 0)));

		}
	}

	public ColumnsSprite(Visual other) {
		super(other);
	}

	public ColumnsSprite(Block newPart, Id newId) {
		super(newPart, newId);
	}
}
