package engine.swing;

import java.awt.Graphics2D;
import java.util.Hashtable;
import java.util.Map;

import engine.Visual;
import engine.Coordinates;

public abstract class Sprite implements Visual {
	static protected Map<Integer, ImageList> imageListMap = 
		new Hashtable<Integer, ImageList>(30);

	protected ImageList images;
	protected Coordinates position;
	protected Coordinates dimensions;
	protected int currImage;
	
	public void draw(Graphics2D canvas) {
		System.out.println("DRAW");
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