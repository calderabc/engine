package engine.graphics2d;

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

	public ImageList images;
	public Coordinates position;
	protected Coordinates dimensions;
	protected Coordinates positionScaleFactor;
	protected Coordinates origin = Coordinates.ORIGIN;
	public int currImage;


	protected Sprite(Part newPart, Id newId) {
		images = imageListMap.get(newId); // Save memory by always using the same images.
		positionScaleFactor = images.imageType.translationFactor;
		//position = new Coordinates(newPart.pos.scale(translationFactor));
		dimensions = images.imageType.dimensions;
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
	
	@Override
	public void update(Part part) {
		position = Coordinates.multiply(part.pos, positionScaleFactor).move(origin);
	}

	@Override
	public void rotate(int offset) {
		// TODO: For now the default sprite rotation does nothing.
		// Eventually add rotation by degrees functionality.
	}

	public int getHeight() {
		return dimensions.y;
	}

}