package engine.puzzle.tetris;

import engine.Reflection;
import engine.Visual;
import engine.puzzle.Block;
import engine.graphics2d.ImageType;
import engine.graphics2d.Sprite;

import java.awt.image.BufferedImage;

public class TetrisBlockSprite extends Sprite {
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
		int lastIndex = images.length - 1;
		
		if (currImage < 0) currImage = lastIndex;
		else if (currImage > lastIndex) currImage = 0;
	}
}
