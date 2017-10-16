package engine.puzzle.tetris;

import engine.Part;
import engine.graphics2d.Sprite;


public class TetrisBlockSprite extends Sprite {
	public TetrisBlockSprite(Part part) {
		super(part, null);
	}
	
	public TetrisBlockSprite(TetrisBlockSprite sprite) {
		super(sprite);
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
