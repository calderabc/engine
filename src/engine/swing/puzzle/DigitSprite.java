package engine.swing.puzzle;

import engine.Coordinates;
import engine.MovablePart;
import engine.Visual;
import engine.puzzle.Digit;
import engine.swing.ImageList;
import engine.swing.ImageType;
import engine.swing.Sprite;

public class DigitSprite extends Sprite {
	static {
		imageListMap.put(new Id((byte)2), 
						 new ImageList(ImageType.DIGIT, Coordinates.ORIGIN));
	}
	
	public static final Coordinates SCORE = new Coordinates(400);
	public static final Coordinates LEVEL = new Coordinates(400, 100);
	public static final Coordinates ROWS = new Coordinates(400, 200);
	
	

	
	public DigitSprite(Digit digit, Id newId) {
		super(digit, newId);

		// TODO: This is real ambiguous.
		switch(digit.type) {
			case SCORE : origin = SCORE; break; 
			case LEVEL : origin = LEVEL; break;
			case ROWS : origin = ROWS; break;
			default : origin = Coordinates.ORIGIN; 
		}
	}
	
	public DigitSprite(Visual other) {
		super(other);
	}

	@Override
	public void update(MovablePart part) {
		System.out.println(part);
		super.update(part);
		currImage = ((Digit)part).get();
	}
}
