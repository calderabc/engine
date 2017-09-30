package engine.swing.puzzle;

import engine.Coordinates;
import engine.Part;
import engine.Visual;
import engine.puzzle.Digit;
import engine.swing.ImageList;
import engine.swing.Sprite;

public class DigitSprite extends Sprite {
	public static int go;
	static {
		ImageType digit = new ImageType("numbers.png",
		                                new Coordinates(30, 60),
		                                new Coordinates(40),
		                                10,
		                                ImageType.ScanDirection.HORIZONTAL);

		visualMap.put(Digit.class, DigitSprite.class);

		imageListMap.put(new Id((byte)2),
						 new ImageList(digit, Coordinates.ORIGIN));


	}

	public DigitSprite(Digit digit, Id newId) {
		super(digit, newId);

		// TODO: Kludgy.  Make better.
		switch(digit.type) {
			case SCORE : origin = new Coordinates(400); break;
			case LEVEL : origin = new Coordinates(400, 100); break;
			case ROWS : origin = new Coordinates(400, 200); break;
			case PIECES : origin = new Coordinates(400, 300); break;
			default : origin = Coordinates.ORIGIN;
		}

	}
	
	public DigitSprite(Visual other) {
		super(other);
	}

	@Override
	public void update(Part part) {
		super.update(part);
		currImage = ((Digit)part).get();
	}
}
