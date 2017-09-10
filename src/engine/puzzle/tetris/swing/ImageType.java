package engine.puzzle.tetris.swing;

import engine.Coordinates;

public enum ImageType {
	// TODO: Convert these into a serialized objects which populate from a data file.
	// Why? These are data (specifically meta-data). They should not be hard-coded.
	BLOCK ("piece_image_sheet.png", new Coordinates(32, 32), 4, ScanDirection.HORIZONTAL),
	DIGIT ("numbers.png", new Coordinates(30, 60), 10, ScanDirection.HORIZONTAL);

	public enum ScanDirection {
		VERTICAL,
		HORIZONTAL
	}
	
	private ImageType(String newImageFileName, Coordinates newDimensions, int newImageCount, ScanDirection newScanDirection) {
		IMAGE_FILE_NAME = newImageFileName;
		DIMENSIONS = newDimensions;
		COUNT = newImageCount;
		SCAN_DIRECTION = newScanDirection;
	}


	public final String IMAGE_FILE_NAME;
	public final Coordinates DIMENSIONS;
	public final int COUNT;
	public final ScanDirection SCAN_DIRECTION;
}
