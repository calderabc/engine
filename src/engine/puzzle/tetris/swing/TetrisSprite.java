package engine.puzzle.tetris.swing;

import engine.Coordinates;
import engine.MovablePart;
import engine.Part;
import engine.Visual;
import engine.puzzle.Block;
import engine.swing.ImageList;
import engine.swing.Sprite;

public class TetrisSprite extends Sprite {
	static {
		int id;
		int x = 0;
		for(int i = 0; i < 70; i += 10) {
			int y = 0;
			for(int j = 0; j < 4; j++) {
				// Encoding: abc, a=1 for block, b=0-6 piece type, c=0-3 block in piece.
				id = 100 + i + j; 
				imageListMap.put(id, new ImageList(ImageType.BLOCK, new Coordinates(x, y)));
				y += ImageType.BLOCK.DIMENSIONS.y;
			}
			x += ImageType.BLOCK.DIMENSIONS.x * 4;
		}
	}

	// TODO: Figure out how to reproduce this functionality but all with data from a config file.	
	public TetrisSprite(Part part) {
		String className = part.getClass().getSimpleName();
		switch(className) {
			case "Block" : 
				Block block = (Block)part;
				int id = 100 + block.getType()*10 + block.getState(); 
				images = imageListMap.get(id);
				position = new Coordinates(block.pos.x * 32, block.pos.y * 32);
				dimensions = images.imageType.DIMENSIONS;
				currImage = 0;
			case "Digit" :
		}
	}
	
	
	// TODO: All this casting makes me nervous.
	public TetrisSprite(Visual other) {
		
		images = ((TetrisSprite)other).images; // Save memory by all using the same image lists.
		position = new Coordinates(((TetrisSprite)other).position);
		dimensions = new Coordinates(((TetrisSprite)other).dimensions);
		currImage = ((TetrisSprite)other).currImage;
	}
	
	@Override
	public void update(MovablePart part) {
		position = new Coordinates(part.pos.x * 32, part.pos.y * 32);
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
