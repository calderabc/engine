package engine.graphics2d;

import engine.Coordinates;
import engine.Game;
import engine.Part;
import engine.Reflection;
import engine.Visual;
import engine.puzzle.PuzzleGame;

public abstract class Sprite extends Visual {
	protected Object[] images;
	public Coordinates position;
	protected int currImage;

	protected Coordinates origin = Coordinates.ORIGIN;

	private final Coordinates dimensions;
	private final Coordinates positionScaleFactor;


	private static ImageType newImageType(Part part) {
		Class<? extends Part> partClass = part.getClass();
		Game game = part.game;
		ImageType imageType = ((Graphics2dScreen)game.screen).imageTypeMap
		                                                     .get(partClass);
		if (imageType == null) {
			imageType = (ImageType)Reflection.newInstance(
				new String[] {game.engineTypeName,
				              game.engineName,
				              "ImageType"},
				new Reflection.ClassAndObject(game.gameName.toLowerCase()),
				new Reflection.ClassAndObject(game.gameTypeName.toLowerCase()),
				new Reflection.ClassAndObject(Part.class, part)
			);
			((Graphics2dScreen)game.screen).imageTypeMap
			                               .put(partClass, imageType);
		}

		return imageType;
	}


	protected Sprite(Part newPart) {
		super(newPart);
		ImageType imageType = newImageType(part);

		positionScaleFactor = imageType.translationFactor.clone();
		dimensions = imageType.dimensions.clone();

		// Scale images and positioning of images to match screens size.
		// Note 'positionScaleFactor' and 'dimensions' will be resized
		// (scaled) by the actions of this method.
		images = imageType.scaleImageArray(
			ImageType.imageListMap.get(part.visualId),
			positionScaleFactor,
			dimensions,
			((Graphics2dScreen)part.game.screen).dimensions,
			((PuzzleGame)part.game).board.dimensions
		);

		update(part);
		currImage = 0;
	}

	protected Sprite(Visual other) {
		super(other);
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

	public Object getCurrImage() {
		if (images == null) {
			images = ImageType.imageListMap.get(part.visualId); // Save memory by always using the same images.
		}
		if (images == null) {
			return null;
		}
		return images[currImage];
	}

}