package engine.swing;

import javax.swing.JComponent;
import javax.swing.JPanel;

import engine.Coordinates;
import engine.Part;

@SuppressWarnings("serial")
public abstract class Renderer extends JPanel {
	
	public void add(Part owner) {
	}
	
	public void remove(Part owner) {
	}
	
	abstract public void update();
}
