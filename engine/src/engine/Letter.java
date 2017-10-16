package engine;

public class Letter extends Symbol {

	public Letter(Game newGame, Coordinates newPosition, char newChar) {
		// TODO: Throw error if newChar is not an English letter.
		// 'A' to 'Z' -> 0 to 25, 'a' to 'z' -> 26 to 51
		super(newGame, newPosition, new Visual.Id(
			"Letter",
			(Character.isLowerCase(newChar)) ? (byte)(newChar - 'a' + 26)
			                                 : (byte)(newChar - 'A')
		));
	}

	public char get() {
		if (value < 26)
			return (char)(value + 'A');
		return (char)(value + 'a' - 26);
	}
}
