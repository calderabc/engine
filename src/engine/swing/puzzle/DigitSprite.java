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

	public DigitSprite(Digit digit, Id newId) {
		super(digit, newId);
	}
	
	public DigitSprite(Visual other) {
		super(other);
	}

	@Override
	public void update(MovablePart part) {
		System.out.println(part.pos.x());
		super.update(part);
		currImage = ((Digit)part).get();
	}
}
