package engine.puzzle.tetris.swing;

import java.io.Serializable;

import engine.Coordinates;
import engine.swing.Sprite;
import engine.swing.puzzle.BlockSprite;
import engine.swing.puzzle.DigitSprite;

public enum ImageType implements Serializable {
	// TODO: Convert these into a serialized objects which populate from a data file.
	// Why? These are data (specifically meta-data). They should not be hard-coded.
	BLOCK ("piece_image_sheet.png", 
	       BlockSprite.class,
	       new Coordinates(32, 32), 
	       new Coordinates(32, 32),
	       4, 
	       ScanDirection.HORIZONTAL),
	DIGIT ("numbers.png", 
	       DigitSprite.class,
	       new Coordinates(30, 60), 
	       new Coordinates(40), 
	       10, 
	       ScanDirection.HORIZONTAL);

	public enum ScanDirection {
		VERTICAL,
		HORIZONTAL
	}

	public final String IMAGE_FILE_NAME;
	public final Class<? extends Sprite> SPRITE_CLASS;
	public final Coordinates DIMENSIONS;
	public final Coordinates POSITION_SCALE_FACTOR;
	public final int COUNT;
	public final ScanDirection SCAN_DIRECTION;
	
	private ImageType(String newImageFileName, 
	                  Class<? extends Sprite> newSpriteClass,
	                  Coordinates newDimensions, 
	                  Coordinates newPositionScaleFactor, 
	                  int newImageCount, 
	                  ScanDirection newScanDirection) {
		IMAGE_FILE_NAME = newImageFileName;
		SPRITE_CLASS = newSpriteClass;
		DIMENSIONS = newDimensions;
		POSITION_SCALE_FACTOR = newPositionScaleFactor;
		COUNT = newImageCount;
		SCAN_DIRECTION = newScanDirection;
	}
}
