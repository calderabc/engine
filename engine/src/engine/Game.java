package engine;

import engine.graphics2d.ImageType;

import java.util.Hashtable;
import java.util.Map;

public abstract class Game {
	public static Game me;


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

		screen = Reflection.newScreen(this);
	}
}
