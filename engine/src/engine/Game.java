package engine;


public abstract class Game {
	public final String gameName;
	public String gameTypeName;
	public String engineName;
	public String engineTypeName;

	public Screen screen;

	protected Game(String gameName,
	               String gameTypeName,
	               String engineName,
	               String engineTypeName) {
		this.gameName = gameName;
		this.gameTypeName = gameTypeName;
		this.engineName = engineName;
		this.engineTypeName = engineTypeName;

	}
}
