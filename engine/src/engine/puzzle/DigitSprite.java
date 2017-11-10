package engine.puzzle;

import engine.*;
import engine.graphics2d.Sprite;

public class DigitSprite extends Sprite {
	public DigitSprite(Part part) {
		// TODO: Come up with a better pass through mechanism.
		super(part, null);
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
