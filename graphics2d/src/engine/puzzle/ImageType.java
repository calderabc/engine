package engine.puzzle;

import java.io.Serializable;

import engine.Coordinates;

public class ImageType implements Serializable {

	public enum ScanDirection {
		VERTICAL,
		HORIZONTAL
	}

	public final String IMAGE_FILE_NAME;
	public final Coordinates DIMENSIONS;
	public final Coordinates POSITION_SCALE_FACTOR;
	public final int COUNT;
	public final ScanDirection SCAN_DIRECTION;
	
	public ImageType(String newImageFileName,
	                  Coordinates newDimensions,
	                  Coordinates newPositionScaleFactor, 
	                  int newImageCount, 
	                  ScanDirection newScanDirection) {
		IMAGE_FILE_NAME = newImageFileName;
		DIMENSIONS = newDimensions;
		POSITION_SCALE_FACTOR = newPositionScaleFactor;
		COUNT = newImageCount;
		SCAN_DIRECTION = newScanDirection;
	}
}
