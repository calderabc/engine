package engine;

import engine.graphics2d.ImageType;

import java.util.Hashtable;
import java.util.Map;

public abstract class Game {
	public static Game me;

	final Map<Class<? extends Part>, ImageType> imageTypeMap = new Hashtable<>();

	protected final String gameName;
	final String gameTypeName;
	final String engineName;
	final String engineTypeName;

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
