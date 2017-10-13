package engine.graphics2d;

import engine.Coordinates;
import engine.Game;
import engine.Part;
import engine.Reflection;
import engine.Visual;

public abstract class Sprite extends Visual {
	public Object[] images;
	public Coordinates position;
	public int currImage;

	protected Coordinates origin = Coordinates.ORIGIN;

	private Coordinates dimensions;
	private Coordinates positionScaleFactor;


	private static ImageType newImageType(Game game,
	                                      Class<? extends Part> partClass) {

		ImageType imageType = ((Graphics2dScreen)game.screen).imageTypeMap .get(partClass);
		if (imageType == null) {
			imageType = (ImageType)Reflection.newInstance(
				Reflection.getClass(game.engineTypeName,
				                    game.engineName,
				                    "ImageType"),
				game.gameName.toLowerCase(),
				game.gameTypeName.toLowerCase(),
				partClass
			);
			((Graphics2dScreen)game.screen).imageTypeMap
			                               .put(partClass, imageType);
		}

		return imageType;
	}

	protected Sprite(Game game, Part part, Id newId) {
		ImageType imageType = newImageType(game, part.getClass());

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