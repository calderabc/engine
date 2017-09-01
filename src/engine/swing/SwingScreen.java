package engine.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import engine.Part;
import engine.Screen;
import engine.puzzle.Game;
import engine.puzzle.tetris.swing.Keyboard;


@SuppressWarnings("serial")
public class SwingScreen extends JPanel implements Screen {
	public static int colorFilter = 0x40FFB0B0;
	
	private int colorFilterIndex = 0;
	private int[] colorMasks = {0x40FFB0B0, 0x40B0FFB0, 0x40B0B0FF, 0x40FFFFB0, 0x40FFB0FF, 0x40B0FFFF};
	
	private List<? extends Part> displayedParts;
	

	public SwingScreen(List<? extends Part> newDisplayedParts) {
		displayedParts = newDisplayedParts;
		JFrame frame = new JFrame("Tetris");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 670);
		frame.setVisible(true);
		frame.setFocusable(true);
		frame.requestFocusInWindow();
		frame.setBackground(Color.BLACK);

		// Init JPanel
		setBackground(Color.BLACK);
		setOpaque(false);
		setDoubleBuffered(true);
		setVisible(true);
		frame.setContentPane(this);

		Keyboard.initInputMap(getInputMap());
		Keyboard.initActionMap(getActionMap());
	}
	
	public void changeColorFilter() {
		colorFilterIndex++;
		
		if (colorFilterIndex > 5) {
			colorFilterIndex = 0;
		}
		
		SwingScreen.colorFilter = colorMasks[colorFilterIndex];
	}

	@Override 
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		System.out.println("paintComponent");
		for (Part displayPart: displayedParts) {
			// If the sprite for the part doesn't exist create it.
			if (displayPart.visual == null) {
				System.out.println("new Visual");
				displayPart.visual = Game.me.engine.newVisual(displayPart);
			}
			((Sprite)displayPart.visual).draw(g2d);
		}
		
	}

	@Override
	public void update() {
		repaint();
	}
	
}