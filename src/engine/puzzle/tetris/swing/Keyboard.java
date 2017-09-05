package engine.puzzle.tetris.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.swing.InputMap;
import javax.swing.KeyStroke;
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
			}
		}

		inputMap.put(KeyStroke.getKeyStroke("released DOWN"), "stop_move_down");
		inputMap.put(KeyStroke.getKeyStroke("released LEFT"), "stop_move_left");
		inputMap.put(KeyStroke.getKeyStroke("released RIGHT"), "stop_move_right");
		//inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "quit");
		//inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, KeyEvent.CTRL_DOWN_MASK), "rotate counterclockwise");
		inputMap.put(KeyStroke.getKeyStroke("released CONTROL"), "stop_rotate_counterclockwise");
		//inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ALT, KeyEvent.ALT_DOWN_MASK), "rotate clockwise");
		inputMap.put(KeyStroke.getKeyStroke("released ALT"), "stop_rotate_clockwise");
	}
	
	@SuppressWarnings("serial")
	public static void initInputActionMaps(Game game, InputMap inputMap, ActionMap aMap) {
		initInputMap(inputMap);

		// TODO:  This seems kludgey.  There's got to be a better way.
		class PieceAbstractAction extends AbstractAction {
			Runnable pieceActionRunner;
			public PieceAbstractAction(Runnable newPieceActionRunner) {
				pieceActionRunner = newPieceActionRunner;
			}
			public void actionPerformed(ActionEvent e) {
				pieceActionRunner.run();
			}
		}

		for(KeyStroke keyStroke : inputMap.allKeys()) {
			String keyBinding  = (String)inputMap.get(keyStroke);

			Runnable pieceActionRunner;
			switch(keyBinding) {
				case "speed_down": pieceActionRunner = game::startMovingDown; break;
				case "warp": pieceActionRunner = game::warpDown; break;
				case "move_right": pieceActionRunner = game::startMovingRight; break;
				case "move_left": pieceActionRunner = game::startMovingLeft; break;
				case "rotate_clock": pieceActionRunner = game::startRotatingClockwise; break;
				case "rotate_count": pieceActionRunner = game::startRotatingCounterClockwise; break;
				case "quit": pieceActionRunner = () -> System.exit(0);
				default: pieceActionRunner = null; //TODO: Should throw exception/error here?
			}
			
			aMap.put(keyBinding, new PieceAbstractAction(pieceActionRunner));
		}
		
		aMap.put("stop_move_down", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				PieceAction.DOWN.setReleaseTime();
				game.stopMovingDown();
			}
		});
		
		aMap.put("stop_move_left", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				PieceAction.LEFT.setReleaseTime();
				game.stopMovingLeft();
			}
		});
		
		aMap.put("stop_move_right", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				PieceAction.RIGHT.setReleaseTime(); 
				
				game.stopMovingRight();
			}
		});
		
		aMap.put("stop_rotate_clockwise", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				PieceAction.CLOCKWISE.setReleaseTime();
				game.stopRotatingClockwise();
			}
		});
		
		aMap.put("stop_rotate_counterclockwise", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				PieceAction.COUNTERCLOCKWISE.setReleaseTime();
				game.stopRotatingCounterClockwise();
			}
		});
		
		aMap.put("quit", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
	}	
}
