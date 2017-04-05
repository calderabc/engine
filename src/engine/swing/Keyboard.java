package engine.swing;


import javax.swing.InputMap;
import javax.swing.KeyStroke;

public class Keyboard {
	 

	public Keyboard(InputMap map) {
		map.put(KeyStroke.getKeyStroke("UP"), "up");
		map.put(KeyStroke.getKeyStroke("DOWN"), "down");
		map.put(KeyStroke.getKeyStroke("LEFT"), "left");
		map.put(KeyStroke.getKeyStroke("RIGHT"), "right");
		map.put(KeyStroke.getKeyStroke("ESCAPE"), "quit");
	}
	
	
	
}
