/*
This is a tile-matching puzzle video game engine.
Copyright (C) 2018 Aaron Calder

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
*/

package engine.graphics2d.swing;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.InputMap;
import javax.swing.KeyStroke;

import engine.FileIO;
import engine.Game;
import engine.Screen;
import engine.puzzle.PieceAction;
import engine.puzzle.PuzzleGame;

import javax.swing.ActionMap;
import javax.swing.AbstractAction;

public class SwingKeyboard {
	private static final String STOP_PREFIX = "stop_";
	
	private static void initInputMap(InputMap inputMap) {
		FileIO.GameProperties inputToAction = new FileIO.GameProperties("input_to_action");

		for(Object key : inputToAction.keySet()) {

			String action = ((String) key).toLowerCase();
			String[] keyStrokes =
				inputToAction.getArrayUpperCase((String) key);

			for(String keyStroke : keyStrokes) {
				inputMap.put(KeyStroke.getKeyStroke(keyStroke), action);
				inputMap.put(KeyStroke.getKeyStroke("released " + keyStroke), STOP_PREFIX + action);
			}
		}

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, KeyEvent.CTRL_DOWN_MASK), "counter");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ALT, KeyEvent.ALT_DOWN_MASK), "clock");
	}
	
	@SuppressWarnings("serial")
	public static void initInputActionMaps(Game game, InputMap inputMap, ActionMap aMap) {
		initInputMap(inputMap);

		PieceAction action = ((PuzzleGame)game).pieceAction;

		for(KeyStroke keyStroke : inputMap.allKeys()) {
			final Runnable pieceActionRunner;
			String keyBinding  = ((String)inputMap.get(keyStroke));
			
			if (keyBinding.contains("quit")) {
				pieceActionRunner = () -> System.exit(0);
			}
			else {
				try {
					pieceActionRunner = (keyBinding.startsWith(STOP_PREFIX))
						? ((PieceAction)PieceAction.Action.class.getField(
							  keyBinding.substring(STOP_PREFIX.length()).toUpperCase()
						  ).get(action))::stopPieceAction
						: ((PieceAction)PieceAction.Action.class.getField(
							  keyBinding.toUpperCase()
						  ).get(action))::startPieceAction;
				} catch (NullPointerException | NoSuchFieldException | IllegalAccessException e) {
					// The value in the input map does not correspond with anything
					// in the PieceActionMap so don't add an action to the ActionMap.
					continue;
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