package engine.puzzle.tetris;

import engine.Game;
import engine.graphics2d.Graphics2dScreen;
import engine.puzzle.Block;
import engine.graphics2d.Sprite;


public class TetrisBlockSprite extends Sprite {
	public TetrisBlockSprite(Game game, Block block, Id newId) {
		super(game, block, newId );

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
