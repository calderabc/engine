package engine.graphics2d.swing;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.InputMap;
import javax.swing.KeyStroke;

import engine.FileIO;
import engine.puzzle.PieceAction;

import javax.swing.ActionMap;
import javax.swing.AbstractAction;

public class SwingKeyboard {
	private static final String STOP_PREFIX = "stop_";
	
	private static void initInputMap(InputMap inputMap) {
		FileIO.GameProperties inputToAction = new FileIO.GameProperties("input_to_action");

		for(Object key : inputToAction.keySet()) {

			String action = ((String) key).toLowerCase();
			String[] keyStrokes =
				inputToAction.getPropertyArrayUpperCase((String) key);

			for(String keyStroke : keyStrokes) {
				inputMap.put(KeyStroke.getKeyStroke(keyStroke), action);
				inputMap.put(KeyStroke.getKeyStroke("released " + keyStroke), STOP_PREFIX + action);
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