package engine.graphics2d;

import engine.Visual;
import engine.Coordinates;
import engine.Part;

public abstract class Sprite extends Visual {
	public Object[] images;
	public Coordinates position;
	protected Coordinates dimensions;
	protected Coordinates positionScaleFactor;
	protected Coordinates origin = Coordinates.ORIGIN;
	public int currImage;


	public ImageType imageType;

	protected Sprite(Part newPart, ImageType imageType, Id newId) {
		images = ImageType.imageListMap.get(newId); // Save memory by always using the same images.
		dimensions = imageType.dimensions;
		positionScaleFactor = imageType.translationFactor;
		//position = new Coordinates(newPart.pos.scale(translationFactor));
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