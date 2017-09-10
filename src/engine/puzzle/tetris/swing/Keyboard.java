package engine.puzzle.tetris.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.ActionMap;
import javax.swing.AbstractAction;

import engine.puzzle.Game;
import engine.puzzle.PieceAction;

public class Keyboard {
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
				inputMap.put(KeyStroke.getKeyStroke("released " + keyStroke.trim()), "stop_" + action);
			}
		}

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, KeyEvent.CTRL_DOWN_MASK), "rotate_count");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ALT, KeyEvent.ALT_DOWN_MASK), "rotate_clock");
	}
	
	@SuppressWarnings("serial")
	public static void initInputActionMaps(Game game, InputMap inputMap, ActionMap aMap) {
		initInputMap(inputMap);

		for(KeyStroke keyStroke : inputMap.allKeys()) {
			Runnable pieceActionRunner;
			String keyBinding  = (String)inputMap.get(keyStroke);
			switch(keyBinding) {
				case "speed_down": 
					pieceActionRunner = game::startMovingDown; 
					break;
				case "stop_speed_down": 
					pieceActionRunner = game::stopMovingDown; 
					break;
				case "warp": 
					pieceActionRunner = game::warpDown; 
					break;
				case "move_right": 
					pieceActionRunner = () -> game.startPieceAction(PieceAction.RIGHT); 
					break;
				case "stop_move_right": 
					pieceActionRunner = () -> game.stopPieceAction(PieceAction.RIGHT); 
					break;
				case "move_left": 
					pieceActionRunner = () -> game.startPieceAction(PieceAction.LEFT); 
					break;
				case "stop_move_left": 
					pieceActionRunner = () -> game.stopPieceAction(PieceAction.LEFT); 
					break;
				case "rotate_clock": 
					pieceActionRunner = () -> game.startPieceAction(PieceAction.CLOCKWISE); 
					break;
				case "stop_rotate_clock": 
					pieceActionRunner = () -> game.stopPieceAction(PieceAction.CLOCKWISE); 
					break;
				case "rotate_count": 
					pieceActionRunner = () -> game.startPieceAction(PieceAction.COUNTERCLOCKWISE); 
					break;
				case "stop_rotate_count": 
					pieceActionRunner = () -> game.stopPieceAction(PieceAction.COUNTERCLOCKWISE); 
					break;
				case "quit": 
					pieceActionRunner = () -> System.exit(0);
					break;
				default: pieceActionRunner = () -> {}; //TODO: Should throw exception/error here?
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