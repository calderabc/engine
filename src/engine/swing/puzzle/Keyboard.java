package engine.swing.puzzle;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.swing.InputMap;
import javax.swing.KeyStroke;

import engine.puzzle.PieceAction;

import javax.swing.ActionMap;
import javax.swing.AbstractAction;

public class Keyboard {
	private static final String STOP_PREFIX = "stop_";
	
	private static void initInputMap(InputMap inputMap) {
		Properties inputToAction = new Properties();
		try {
			inputToAction.load(new FileInputStream("input_to_action.dat"));
			inputToAction.storeToXML(new FileOutputStream("input_to_action.xml"), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(Map.Entry<Object, Object> entry : inputToAction.entrySet()) {
			String[] keyStrokes = ((String)entry.getValue()).toUpperCase().split(",");
			String action = ((String)entry.getKey()).toLowerCase().trim();
			
			for(String keyStroke : keyStrokes) {
				inputMap.put(KeyStroke.getKeyStroke(keyStroke.trim()), action);
				inputMap.put(KeyStroke.getKeyStroke("released " + keyStroke.trim()), STOP_PREFIX + action);
			}
		}

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, KeyEvent.CTRL_DOWN_MASK), "counter");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ALT, KeyEvent.ALT_DOWN_MASK), "clock");
	}
	
	@SuppressWarnings("serial")
	public static void initInputActionMaps(InputMap inputMap, ActionMap aMap) {
		initInputMap(inputMap);

		for(KeyStroke keyStroke : inputMap.allKeys()) {
			final Runnable pieceActionRunner;
			String keyBinding  = ((String)inputMap.get(keyStroke));
			
			if (keyBinding.contains("quit")) {
				pieceActionRunner = () -> System.exit(0);
			}
			else {
				try {
					pieceActionRunner = (keyBinding.startsWith(STOP_PREFIX))
						? PieceAction.valueOf(
							  keyBinding.substring(STOP_PREFIX.length()).toUpperCase()
						  )::stopPieceAction
						: PieceAction.valueOf(
							  keyBinding.toUpperCase()
						  )::startPieceAction;
				} catch (NullPointerException e) {
					// The value in the input map does not correspond with anything
					// in the PieceActionMap so don't add an action to the ActionMap.
					break;
				}
			}
			
			
			aMap.put(keyBinding, new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Use another thread because the action methods called will eventually
					// call "tryToMovePiece" which calls "invokeAndWait", which if called
					// from the swing event dispatch thread throws an exception.
					//
					// Plus the swing event dispatch thread shouldn't be used for doing
					// game business logic anyway.
					new Thread(pieceActionRunner).start();
				}
			});
		}
		
	}	
		
}