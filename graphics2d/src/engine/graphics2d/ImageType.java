package engine.graphics2d;

import java.io.Serializable;

import engine.Coordinates;
import engine.FileIO;
import engine.Part;

public abstract class ImageType implements Serializable {

	public enum ScanDirection {
		VERTICAL,
		HORIZONTAL
	}

	public class Node {
		public Node(ScanDirection direction, int count, Node next) {
			this.direction = direction;
			this.count = count;
			this.next = next;
		}
		public final ScanDirection direction;
		public final int count;
		public Node next;
	}

	Node scanInstructions = null;

	public final String imageFileName;
	public final Coordinates dimensions;
	public final Coordinates translationFactor;


	public ImageType(String fileName, Class<? extends Part> partClass, Class<?> newImageClass) {
		imageClass = newImageClass;

		String partString = partClass.getSimpleName().toLowerCase();
		FileIO.GameProperties properties = new FileIO.GameProperties(fileName);

		// <><> Populate the linked list with scan instructions.
		String[] directions =
			properties.getPropertyArrayUpperCase(partString + "_scan_direction");
		String[] counts =
			properties.getPropertyArrayLowerCase(partString + "_scan_count");
		for (int i = counts.length - 1; i >= 0; i--) {
			Node instruction = new Node(ScanDirection.valueOf(directions[i]),
			                            Integer.valueOf(counts[i]),
			                            scanInstructions);
			scanInstructions = instruction;
		} // <><>

		// TODO: Implement these with a loop and reflection. Would be overkill.
		imageFileName = properties.getProperty(partString + "_image_file");
		dimensions = new Coordinates(
			properties.getPropertyArray(partString + "_dimensions")
		);
		translationFactor = new Coordinates(
			properties.getPropertyArray(partString + "_translation")
		);
	}

	private Class<?> imageClass;

	protected abstract T loadImageFromFile(String fileName) throws IOException;
	protected abstract T getSubimage(T image, Coordinates position, Coordinates dimensions);

	private Coordinates scan(Node instruction, Coordinates startPosition) {
		if (instruction.next == null) {
			// Create an image list based on the instruction.
			ImageList<?>

		/*
			try {
				T spriteImageSource = loadImageFromFile(imageType.imageFileName);

				int scanX = scanStart.x;
				int scanY = scanStart.y;
				int width = imageType.dimensions.x;
				int height = imageType.dimensions.y;
				for(int i = 0; i < imageType.COUNT; i++) {
					images[i] = getSubimage(spriteImageSource,
					new Coordinates(scanX, scanY),
					imageType.dimensions);

					if (imageType.SCAN_DIRECTION == ImageType.ScanDirection.VERTICAL)
						scanY += height;
					else
						scanX += width;
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		*/
		}
		else {
			for (int i = 0; i < instruction.count; i++) {
				// Recurse
				Coordinates scanDimensions = scan(instruction.next, startPosition);

				// Move the scan start position by the size (width or height) of
				// the size of what was scanned the next recursion in.
				if (instruction.direction == ScanDirection.HORIZONTAL) {
					startPosition.x += scanDimensions.x;
				} else {
					startPosition.y += scanDimensions.y;
				}
			}
		}





	}

}
