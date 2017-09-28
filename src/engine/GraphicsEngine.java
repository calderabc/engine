package engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class GraphicsEngine {
	public final Screen screen;
	protected final Class<? extends Screen> screenClass;
	
	protected GraphicsEngine(Game game, Class<? extends Screen> newScreenClass) {
		screenClass = newScreenClass;
		Screen temp = null;
			Constructor<? extends Screen> con;
			try {
				con = screenClass.getConstructor(game.getClass());
				temp = (Screen) con.newInstance(game);
			} catch (NoSuchMethodException | SecurityException 
			         | InstantiationException | IllegalAccessException 
			         | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		screen = temp;
	}
	
	protected abstract Class<? extends Visual> getVisualClass(Part part);

	public Visual newVisual(Part part, Visual.Id id) {
			try {
				return getVisualClass(part)
					.getConstructor(part.getClass(), Visual.Id.class)
					.newInstance(part, id);
			} catch (InstantiationException | IllegalAccessException 
			         | IllegalArgumentException | InvocationTargetException 
			         | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		return null;
	}

	public Visual newVisual(Visual visual) {
			if (visual == null) return null;
			try {
				return visual.getClass().getConstructor(Visual.class).newInstance(visual);
			} catch (InstantiationException | IllegalAccessException 
			         | IllegalArgumentException | InvocationTargetException 
			         | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		return null;
	}
}
