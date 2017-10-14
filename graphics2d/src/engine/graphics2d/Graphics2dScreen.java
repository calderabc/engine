package engine.graphics2d;

import engine.Coordinates;
import engine.Part;
import engine.Screen;

import java.util.Hashtable;
import java.util.Map;

public abstract class Graphics2dScreen extends Screen {
	final Map<Class<? extends Part>, ImageType> imageTypeMap = new Hashtable<>();

	protected Coordinates dimensions;

	public Graphics2dScreen(String visualName) {
		super(visualName);
	}
}

