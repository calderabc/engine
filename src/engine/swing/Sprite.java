package engine.swing;

import java.awt.Graphics2D;
import java.util.Hashtable;
import java.util.Map;

import engine.Visual;
import engine.Coordinates;
import engine.Part;
import engine.puzzle.Block;
import engine.puzzle.tetris.swing.ImageType;

public class Sprite implements Visual {
	private ImageList images;

	private Coordinates position;
	private Coordinates dimensions;
	private int currImage;
	private Updater updater;

	private static Map<Integer, ImageList> imageListMap = 
		new Hashtable<Integer, ImageList>(30);
	
	private interface Updater {
		public void Update(Part part);
	}
	
	static private class UpdateMethods {
		static public void updateBlock (Part part) {
			
		}
	}

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
	
	public static Sprite factory(Part part) {
		String className = part.getClass().getSimpleName();
		
		switch(className) {
			case "Block" : 
				return new Sprite(100, 
								  new Coordinates(part.pos.x * 32, part.pos.y * 32), 
								  UpdateMethods::updateBlock ) {
					
				};
			case "Digit" :
				return new Sprite
		}
		
		return new Sprite(100, new Coordinates(0, 0));
	}
	
	
	private Sprite(int id, Coordinates newPosition, Updater newUpdater) {
		images = imageListMap.get(id);
		position = newPosition;
		dimensions = images.imageType.DIMENSIONS;
		currImage = 0;
		updater = newUpdater;
	}

	public void draw(Graphics2D canvas) {
		canvas.drawImage(images.get(currImage),  
					     position.x, position.y, 
					     dimensions.x, dimensions.y, null);
	}


	@Override
	public void update(Part part) {
		updater.Update(part);
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