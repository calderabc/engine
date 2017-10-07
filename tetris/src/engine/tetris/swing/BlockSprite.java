package engine.tetris.swing;

import engine.Coordinates;
import engine.Visual;
import engine.puzzle.Block;
import engine.graphics2d.ImageType;
import engine.swing.ImageList;
import engine.graphics2d.Sprite;

public class BlockSprite extends Sprite {
	public static int go;
	public static ImageType block;
	static {
		block = new ImageType("piece_image_sheet.png",
		                      new Coordinates(32, 32),
		                      new Coordinates(32, 32),
		                      4,
		                      ImageType.ScanDirection.HORIZONTAL);

		int x = 0;
		for(byte i = 0; i < 7; i++) {
			int y = 0;
			for(byte j = 0; j < 4; j++) {
				// Encoding: abc, a=1 for block, b=0-6 piece type, c=0-3 block in piece.
				imageListMap.put(new Id((byte)1, i, j), 
				                 new ImageList(block, new Coordinates(x, y))
				);
				y += block.DIMENSIONS.y;
			}
			x += block.DIMENSIONS.x * 4;
		}
	}

	public BlockSprite(Block block, Id newId) {
		super(block, newId);
	}
	
	public BlockSprite(Visual visual) {
		super(visual);
	}
	
	@Override
	public void rotate(int offset) {
		currImage -= offset;

		// TODO: A little cryptic.
		int lastIndex = images.imageType.COUNT - 1;
		
		if (currImage < 0) currImage = lastIndex;
		else if (currImage > lastIndex) currImage = 0;
	}
}
