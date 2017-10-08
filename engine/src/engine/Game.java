package engine;

public abstract class Game {
	public static Game me;

	public final String gameName;
	public final String gameTypeName;
	public final String engineName;
	public final String engineTypeName;

	public Screen screen;

	protected Game(String gameName,
	               String gameTypeName,
	               String engineName,
	               String engineTypeName) {
		this.gameName = gameName;
		this.gameTypeName = gameTypeName;
		this.engineName = engineName;
		this.engineTypeName = engineTypeName;

		screen = Reflection.newScreen(this);
	}
}
