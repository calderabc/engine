package engine.graphics2d;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Hashtable;
import java.util.Map;

import engine.*;

public abstract class ImageType implements Serializable {
	// To contain arrays of images for any sprites which may be made.
	static Map<Visual.Id, Object[]> imageListMap = new Hashtable<>(30);

	// The direction to scan in a scan instruction
	private enum ScanDirection {
		VERTICAL,
		HORIZONTAL
	}

	// For a linked list of instructions to be used by recursive scan();
	private Node scanInstructions;
	private class Node {
		Node(ScanDirection direction, int count, Node next) {
			this.direction = direction;
			this.count = count;
			this.next = next;
		}
		final ScanDirection direction;
		final int count;
		Node next;
	}

	// Type used to return the dimensions of this scan and a image if this
	// scan is the recursions base case.
	private class Result {
		public Result(Coordinates dimensions, Object image) {
			this.dimensions = dimensions;
			this.image = image;
		}
		public Result(Coordinates dimensions) {
			this(dimensions, null);
		}
		final Coordinates dimensions;
		final Object image;
	}

	public final Coordinates dimensions;
	public final Coordinates translationFactor;
	public final Class<?> imageClass;
	private final Object sourceImage;

	static ImageType newImageType(Part part, String partLabel) {
		if (partLabel == null) {
			partLabel = part.getClass().getSimpleName();
		}
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
			new Reflection.ClassAndObject(partLabel)
			);
			((Graphics2dScreen)game.screen).imageTypeMap
			.put(partClass, imageType);
		}

		return imageType;
	}


	public ImageType(String configFileName,
	                 String defaultConfigFileName,
	                 String partLabel,
	                 Class<?> imageClass) {

		this.imageClass = imageClass;

		// TODO: This system needs much improvement!
		// TODO: Make everything fail gracefully if config file
		// TODO: doesn't have the property wanted. Output useful error message.
		//
		// TODO: Create new exceptions for any sort of bad data in config file.
		// TODO: Check data retrieved from config file.  Catch user errors.

		FileIO.GameProperties properties = new FileIO.GameProperties(
			configFileName,
			new FileIO.GameProperties(defaultConfigFileName)
		);

		String partString = partLabel.toLowerCase();
		sourceImage = loadImageFromFile(
			properties.getProperty(partString + "_image_file")
		);

		// <><> Populate the linked list with scan instructions.
		String[] directions =
			properties.getArrayUpperCase(partString + "_scan_direction");
		String[] counts =
			properties.getArrayLowerCase(partString + "_scan_count");

		for (int i = counts.length - 1; i >= 0; i--) {
			Node instruction = new Node(ScanDirection.valueOf(directions[i]),
			                            Integer.valueOf(counts[i]),
			                            scanInstructions);
			scanInstructions = instruction;
		} // <><>

		dimensions =
			properties.getCoordinates(partString + "_dimensions");

		Coordinates dummy =
			properties.getCoordinates(partString + "_translation");
		translationFactor = (dummy != null) ? dummy : dimensions;

		// Head into the recursive scan.
		scan(new Visual.Id(partLabel),
		     scanInstructions,
		     Coordinates.ORIGIN);
	}



	private Coordinates movePosition(Coordinates position,
	                                 Coordinates offset,
	                                 ScanDirection direction,
	                                 ScanDirection ifDirection) {
		if (direction == ifDirection)
			position.moveX(offset.x);
		else
			position.moveY(offset.y);
		return position;
	}

	// Scan the image file to populate the image list lookup table.
	private Result scan(Visual.Id id,
	                    Node instruction,
	                    Coordinates startPosition) {
		if (instruction == null) {
			// Base case of the recursion. Return the scanned image.
			return new Result(
				dimensions,
				getSubimage(sourceImage, startPosition, dimensions)
			);
		}

		boolean doImageList = instruction.next == null;

		Object[] images = null;
		if (doImageList) {
			images = (Object[])Array.newInstance(imageClass, instruction.count);
		}

		Result result = null;
		Coordinates scanPosition = startPosition.clone();
		for (byte i = 0; i < instruction.count; i++) {
			// Recurse.
			result = scan(new Visual.Id(id, i),
			              instruction.next,
			              scanPosition);
			if (doImageList) {
				images[i] = result.image;
			}
			// Move to next position to scan.
			movePosition(scanPosition, result.dimensions,
			             instruction.direction, ScanDirection.HORIZONTAL);
		}

		if (doImageList) {
			// Add image array to lookup map.
			imageListMap.put(id, images);
		}
		// Return the dimensions of the rectangle just scanned.
		return new Result(
			Coordinates.subtract(
				movePosition(scanPosition, result.dimensions,
				             instruction.direction, ScanDirection.VERTICAL),
                startPosition
			)
		);
	}

	// "i" image dimensions
	// "b" bounding rectangle dimensions
	// "g" dimensions of image cell grid (how many cells wide and high)
	// to be contained in the bounding rectangle.
	// "O" for "original" x or y
	// "F" for "fitted" x or y the x or y of image expanded or shrunk
	// to fit on the image cell grid within the bounding rectangle.
	private double getScaleFactor(Coordinates i,
	                                   Coordinates b,
	                                   Coordinates g) {
		// Get ratios of fitted sizes to original sizes.
		double xRatioFtoO = (double)b.x / (g.x * i.x);
		double yRatioFtoO = (double)b.y / (g.y * i.y);
		// Return the smallest ratio (scale factor);
		return (yRatioFtoO > xRatioFtoO)? xRatioFtoO: yRatioFtoO;

	}

	public Object[] scaleImageArray(Object[] imageArray,
	                                Coordinates positionScaleFactor,
	                                Coordinates imageDimensions,
	                                Coordinates boundingDimensions,
	                                Coordinates gridDimensions) {

		Coordinates cellSize = Coordinates.max(imageDimensions,
		                                       positionScaleFactor);

		double scaleFactor = getScaleFactor(cellSize,
		                                    boundingDimensions,
		                                    gridDimensions);

		// Resize the dimensions of image size and position spacing.
		// Notice this directly effects the associated external
		// Coordinate values passed into scaleImageArray().
		positionScaleFactor.scale(scaleFactor);
		imageDimensions.scale(scaleFactor);

		Object[] scaledImageArray = new Object[imageArray.length];
		for (int i = 0; i < imageArray.length; i++) {
			scaledImageArray[i] = getScaledImage(imageArray[i],
			                                     imageDimensions);
		}
		return scaledImageArray;
	}

	protected abstract Object loadImageFromFile(String fileName);
	protected abstract Object getSubimage(Object image, Coordinates position, Coordinates dimensions);
	protected abstract Object getScaledImage(Object image, Coordinates newDimensions);
	protected abstract Coordinates imageSize(Object image);
}
