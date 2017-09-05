package engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import engine.puzzle.Game;

public abstract class GraphicsEngine {
	protected Class<? extends Screen> screenClass;
	protected Class<? extends Visual> visualClass; 
	
	public Screen newScreen(Game game) {
		System.out.println("newScreen");
		try {
			Constructor<? extends Screen> con = screenClass.getConstructor(Game.class);
			return (Screen) con.newInstance(game);
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
		return null;
	}

	public Visual newVisual(Part part) {
		System.out.println("newVisual");
		try {
			Visual v = visualClass.getConstructor(Part.class).newInstance(part);
			System.out.println(v);
			return v;
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
