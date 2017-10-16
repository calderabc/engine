package engine;

public class Word extends Part {
	public final String label;
	public Word(Game newGame, Coordinates newPosition, String label) {
		super(newGame, newPosition, new Visual.Id(label));

		this.label = label;

		game.screen.addPart(this);
	}
}
