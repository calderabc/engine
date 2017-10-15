package engine;

public class Symbol extends Part {
	protected byte value;

	public Symbol(Game newGame, Coordinates newPosition, Visual.Id newVisualId) {
		super(newGame, newPosition, newVisualId);
	}
}
