package engine.puzzle.swing;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import engine.Visual;
import engine.Coordinates;
import engine.Part;

public abstract class Sprite extends Visual {
	static protected Map<Visual.Id, ImageList> imageListMap =
		new HashMap<>(30);

	static protected Map<Class<? extends Part>,
	                     Class<? extends Sprite>> visualMap =
		new HashMap<>(5);

	protected ImageList images;
	protected Coordinates position;
	protected Coordinates dimensions;
	protected Coordinates positionScaleFactor;
	protected Coordinates origin = Coordinates.ORIGIN;
	protected int currImage;
	
	protected Sprite(Part newPart, Id newId) {
		images = imageListMap.get(newId); // Save memory by always using the same images.
		positionScaleFactor = images.imageType.POSITION_SCALE_FACTOR;
		//position = new Coordinates(newPart.pos.scale(positionScaleFactor));
		dimensions = images.imageType.DIMENSIONS;
		currImage = 0;
	}

	protected Sprite(Visual other) {
		images = ((Sprite)other).images;
		position = ((Sprite)other).position;
		dimensions = ((Sprite)other).dimensions;
		positionScaleFactor = ((Sprite)other).positionScaleFactor;
		origin = ((Sprite)other).origin;
		currImage = ((Sprite)other).currImage;
	}
	
	public void draw(Graphics2D canvas) {
		canvas.drawImage(images.get(currImage),  
					     position.x, position.y,
					     dimensions.x, dimensions.y, null);
	}

	@Override
	public void update(Part part) {
		position = Coordinates.multiply(part.pos, positionScaleFactor).move(origin);
	}

	@Override
	public void rotate(int offset) {
		// TODO: For now the default sprite rotation does nothing.
		// Eventually add rotation by degrees functionality.
	}
}