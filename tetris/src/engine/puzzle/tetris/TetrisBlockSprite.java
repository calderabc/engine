package engine.puzzle.tetris;

import engine.Coordinates;
import engine.Visual;
import engine.puzzle.Block;
import engine.graphics2d.ImageType;
import engine.graphics2d.ImageList;
import engine.graphics2d.Sprite;

import java.awt.image.BufferedImage;

public class TetrisBlockSprite extends Sprite {
	public static int go;
	public static ImageType block;
	static {
		block = new ImageType("tetris", Block.class, BufferedImage.class);

		/*
		for(byte i = 0; i < 7; i++) {
			int y = 0;
			for(byte j = 0; j < 4; j++) {
				// Encoding: abc, a=1 for block, b=0-6 piece type, c=0-3 block in piece.
				imageListMap.put(new Id((byte)1, i, j),
				                 new ImageList(block, new Coordinates(x, y))
				);
				y += block.dimensions.y;
			}
			x += block.dimensions.x * 4;
		}
		*/
	}

	public TetrisBlockSprite(Block block, Id newId) {
		super(block, newId);
	}
	
	public TetrisBlockSprite(Visual visual) {
		super(visual);
	}
	
	@Override
	public void rotate(int offset) {
		currImage -= offset;

		// TODO: A little cryptic.
		int lastIndex = images.size() - 1;
		
		if (currImage < 0) currImage = lastIndex;
		else if (currImage > lastIndex) currImage = 0;
	}
}
