package engine.graphics2d;

import java.io.Serializable;

import engine.Coordinates;
import engine.FileIO;
import engine.Part;

public class ImageType implements Serializable {

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


	public ImageType(String fileName, Class<? extends Part> partClass) {
		String partString = partClass.getSimpleName().toLowerCase();
		FileIO.GameProperties properties = new FileIO.GameProperties(fileName);

		// Populate the linked list with scan instructions.
		String[] directions =
			properties.getPropertyArrayUpperCase(partString + "_scan_direction");
		String[] counts =
			properties.getPropertyArrayLowerCase(partString + "_scan_count");
		for (int i = counts.length - 1; i >= 0; i--) {
			Node instruction = new Node(ScanDirection.valueOf(directions[i]),
			                            Integer.valueOf(counts[i]),
			                            scanInstructions);
			scanInstructions = instruction;
		}

		// TODO: Implement these with a loop and reflection. Would be overkill.
		imageFileName = properties.getProperty(partString + "_image_file");
		dimensions = new Coordinates(
			properties.getPropertyArray(partString + "_dimensions")
		);
		translationFactor = new Coordinates(
			properties.getPropertyArray(partString + "_translation")
		);
	}

}
