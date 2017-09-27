package engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class GraphicsEngine {
	public final Screen screen;
	protected final Class<? extends Screen> screenClass;
	
	protected GraphicsEngine(Game game, Class<? extends Screen> newScreenClass) {
		screenClass = newScreenClass;
		Screen temp = null;
		try {
			Constructor<? extends Screen> con = screenClass.getConstructor(game.getClass());
			temp = (Screen) con.newInstance(game);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.out.println(e.getCause().toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
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
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Visual newVisual(Visual visual) {
		try {
			if (visual == null) return null;
			return visual.getClass().getConstructor(Visual.class).newInstance(visual);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
