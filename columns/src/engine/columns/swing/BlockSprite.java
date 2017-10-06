package engine.columns.swing;

import engine.Coordinates;
import engine.Visual;
import engine.puzzle.Block;
import engine.puzzle.ImageType;
import engine.puzzle.ImageType.ScanDirection;
import engine.puzzle.swing.ImageList;
import engine.puzzle.swing.Sprite;

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
