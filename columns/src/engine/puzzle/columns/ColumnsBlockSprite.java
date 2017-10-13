package engine.puzzle.columns;

import engine.Game;
import engine.puzzle.Block;
import engine.graphics2d.Sprite;

public class ColumnsBlockSprite extends Sprite {
	// TODO: This class is a placeholder, all constructors pass through.
	// TODO: Fix Reflection utility to instantiate "Sprite" in the case
	// TODO: ColumnsBlockSprite is not found so I can get rid of this class.
	public ColumnsBlockSprite(ColumnsBlockSprite other) {
		super(other);
	}

	public ColumnsBlockSprite(Game game, Block newPart, Id newId) {
		super(game, newPart, newId);
	}
}
