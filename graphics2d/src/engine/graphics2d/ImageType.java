package engine.graphics2d;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import engine.Coordinates;
import engine.FileIO;
import engine.Part;
import engine.Visual;

public abstract class ImageType implements Serializable {

	static Map<Visual.Id, Object[]> imageListMap = new HashMap<>(30);

	private enum ScanDirection {
		VERTICAL,
		HORIZONTAL
	}

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

	private Node scanInstructions;

	public final Coordinates dimensions;
	public final Coordinates translationFactor;

	public final Class<?> imageClass;

	private final Object sourceImage;

	public ImageType(String configFileName,
	                 Class<? extends Part> partClass,
	                 String imageFileName,
	                 Class<?> imageClass) {

		this.imageClass = imageClass;
		sourceImage = loadImageFromFile(imageFileName);

		String partString = partClass.getSimpleName().toLowerCase();
		FileIO.GameProperties properties = new FileIO.GameProperties(configFileName);

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

		imageFileName = properties.getProperty(partString + "_image_file");
		dimensions =
			properties.getCoordinates(partString + "_dimensions");
		translationFactor =
			properties.getCoordinates(partString + "_translation");


		scan(new Visual.Id(Visual.Id.getUnique()),
		     scanInstructions,
		     Coordinates.ORIGIN);
	}

	protected abstract Object loadImageFromFile(String fileName);
	protected abstract Object getSubimage(Object image, Coordinates position, Coordinates dimensions);


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



	private Result scan(Visual.Id id,
	                    Node instruction,
	                    Coordinates startPosition) {
		if (instruction == null) {
			// Base of recursion.
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

		Coordinates scanPosition = startPosition.clone();
		for (byte i = 0; i < instruction.count; i++) {

			Result result = scan(new Visual.Id(id, i),
			                     instruction,
			                     startPosition);
			if (doImageList) {
				images[i] = result.image;
			}
			// Move to next position to scan.
			movePosition(scanPosition,
			             result.dimensions,
			             instruction.direction,
			             ScanDirection.HORIZONTAL);
		}

		if (doImageList) {
			// Add image array to lookup map.
			imageListMap.put(id, images);
		}
		// Return the dimensions of the rectangle just scanned.
		return new Result(
			Coordinates.subtract(
				movePosition(scanPosition,
				             dimensions,
				             instruction.direction,
				             ScanDirection.VERTICAL),
                startPosition
			)
		);
	}

}
