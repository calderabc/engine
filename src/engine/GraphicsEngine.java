package engine;

import engine.swing.Screen;

public abstract class GraphicsEngine {
	protected Class<? extends Screen> screenClass;
	protected Class<? extends Visual> visualClass; 
	
	public Screen newScreen() {
		try {
			return screenClass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Visual newVisual() {
		try {
			return visualClass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
