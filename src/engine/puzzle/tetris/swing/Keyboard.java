package engine.puzzle.tetris.swing;


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.ActionMap;
import javax.swing.AbstractAction;

import engine.puzzle.PieceAction;

public class Keyboard {
	public static void initInputMap(InputMap map) {
		map.put(KeyStroke.getKeyStroke("UP"), "warp down");
		map.put(KeyStroke.getKeyStroke("DOWN"), "move down");
		map.put(KeyStroke.getKeyStroke("released DOWN"), "stop move down");
		map.put(KeyStroke.getKeyStroke("LEFT"), "move left");
		map.put(KeyStroke.getKeyStroke("released LEFT"), "stop move left");
		map.put(KeyStroke.getKeyStroke("RIGHT"), "move right");
		map.put(KeyStroke.getKeyStroke("released RIGHT"), "stop move right");
		map.put(KeyStroke.getKeyStroke("ESCAPE"), "quit");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, KeyEvent.CTRL_DOWN_MASK), "rotate counterclockwise");
		map.put(KeyStroke.getKeyStroke("released CONTROL"), "stop rotate counterclockwise");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ALT, KeyEvent.ALT_DOWN_MASK), "rotate clockwise");
		map.put(KeyStroke.getKeyStroke("released ALT"), "stop rotate clockwise");
	}
	
	@SuppressWarnings("serial")
	public static void initActionMap(ActionMap aMap) {
	
		aMap.put("warp down", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				warpDown();
			}
		});
		
		aMap.put("move down", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (PieceAction.DOWN.isLegitKeyPress()) {
					startMovingDown();
				}
				
			}
		});
		
		aMap.put("stop move down", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				PieceAction.DOWN.setReleaseTime();
				stopMovingDown();
			}
		});
		
		aMap.put("move left", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (PieceAction.LEFT.isLegitKeyPress()) {
					startMovingLeft();
				}
			}
		});
		
		aMap.put("stop move left", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				PieceAction.LEFT.setReleaseTime();
				stopMovingLeft();
			}
		});
		
		aMap.put("move right", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (PieceAction.RIGHT.isLegitKeyPress()) {
					startMovingRight();
				}
			}
		});
		
		aMap.put("stop move right", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				PieceAction.RIGHT.setReleaseTime(); 
				
				stopMovingRight();
			}
		});
		
		aMap.put("rotate clockwise", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
	//			if (PieceAction.CLOCKWISE.isLegitKeyPress()) { 
					startRotatingClockwise();
	//			}
			}
		});
		
		aMap.put("stop rotate clockwise", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				PieceAction.CLOCKWISE.setReleaseTime();
				stopRotatingClockwise();
			}
		});
		
		aMap.put("rotate counterclockwise", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
	//			if (PieceAction.COUNTERCLOCKWISE.isLegitKeyPress()) { 
					startRotatingCounterClockwise();
	//			}
			}
		});
		
		aMap.put("stop rotate counterclockwise", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				PieceAction.COUNTERCLOCKWISE.setReleaseTime();
				stopRotatingCounterClockwise();
			}
		});
		
		aMap.put("quit", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
	}	
	
}
