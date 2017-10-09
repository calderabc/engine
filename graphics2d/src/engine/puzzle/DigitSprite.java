package engine.puzzle;

import engine.Coordinates;
import engine.Part;
import engine.Visual;
import engine.graphics2d.ImageType;
import engine.graphics2d.Sprite;

public class DigitSprite extends Sprite {
	public DigitSprite(Digit digit, ImageType imageType, Id newId) {
		super(digit, imageType, newId);

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
