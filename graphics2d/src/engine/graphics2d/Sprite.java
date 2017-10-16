package engine.graphics2d;

import engine.*;
import engine.Symbol;
import engine.puzzle.Block;
import engine.puzzle.PuzzleGame;

import java.util.Arrays;

public abstract class Sprite extends Visual {
	protected Object[] images;
	public Coordinates position;
	protected int currImage;

	protected Coordinates origin = Coordinates.ORIGIN;

	private final Coordinates dimensions;
	private final Coordinates positionScaleFactor;


	protected Sprite(Part newPart, String label) {
		super(newPart);

		if (label != null) {

		}




		Game game = part.game;

		// TODO: Improve this.
		Coordinates gridDimensions = (part instanceof Block)
		                             ? ((PuzzleGame)part.game).board.dimensions
		                             : game.getScoreBoardDimensions();

		// TODO: This is an inaccurate hack.  Find better solution.
		if (part instanceof Symbol) {
			int x = ((Graphics2dScreen)part.game.screen).dimensions.x / 2;
			origin = new Coordinates(x);
		}

		ImageType imageType = ImageType.newImageType(part, label);


		dimensions = imageType.dimensions.clone();

		Object[] sourceImages = ImageType.imageListMap.get(part.visualId);

		// If position scale factor is not given set it to the same dimensions
		// large enough to hold any image in the image array.
		positionScaleFactor = (imageType.translationFactor == null)
			? Coordinates.max(
				(Coordinates[])Arrays.stream(sourceImages)
									 .parallel()
									 .map(imageType::imageSize)
				                     .toArray()
			)
			: imageType.translationFactor.clone();

		// Scale images and positioning of images to match screens size.
		// Note 'positionScaleFactor' and 'dimensions' will be resized
		// (scaled) by the actions of this method.
		images = imageType.scaleImageArray(
			sourceImages,
			positionScaleFactor,
			dimensions,
			((Graphics2dScreen)part.game.screen).dimensions,
			gridDimensions
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