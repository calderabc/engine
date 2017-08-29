package engine.swing;

import java.awt.Graphics2D;
import java.util.Hashtable;
import java.util.Map;

import engine.Coordinates;
import engine.puzzle.tetris.swing.ImageType;

public class Sprite {
	private ImageList images;
	private Graphics2D canvas;

	private Coordinates position;
	private Coordinates dimensions;
	private int currImage;

	private static Map<Integer, ImageList> imageListMap = 
		new Hashtable<Integer, ImageList>(30);

	static {
		int id;
		int x = 0;
		for(int i = 0; i < 70; i += 10) {
			for(int j = 0; j < 4; j++) {
				// Encoding: abc, a=1 for block, b=0-6 piece type, c=0-3 block in piece.
				id = 100 + i + j; 
				imageListMap.put(id, new ImageList(ImageType.BLOCK, new Coordinates(x, 0)));
				x += ImageType.BLOCK.DIMENSIONS.x;
			}
		}
	}
	
	public Sprite(int id, Coordinates newPosition, Graphics2D graphics) {
		images = imageListMap.get(id);
		position = newPosition;
		dimensions = images.imageType.DIMENSIONS;
		currImage = 0;
		canvas = graphics;
	}

	public void draw() {
		canvas.drawImage(images.get(currImage),  
					     position.x, position.y, 
					     dimensions.x, dimensions.y, null);
	}

/*
	@Override 
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(images.get(currImage), 0, 0, getWidth(), getHeight(), null);
	}
*/
}