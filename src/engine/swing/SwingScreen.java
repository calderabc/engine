package engine.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

import engine.Part;
import engine.Screen;
import engine.puzzle.Game;
import engine.puzzle.tetris.swing.Keyboard;


@SuppressWarnings("serial")
public class SwingScreen extends JPanel implements Screen {
	public static int colorFilter = 0x40FFB0B0;
	public JFrame frame;
	
	private int colorFilterIndex = 0;
	private int[] colorMasks = {0x40FFB0B0, 0x40B0FFB0, 0x40B0B0FF, 0x40FFFFB0, 0x40FFB0FF, 0x40B0FFFF};
	
	private List<Part> displayedParts = new Vector<Part>();
	

	public SwingScreen(Game game) {
		// TODO: Do this method call on another thread.
		Keyboard.initInputActionMaps(game, getInputMap(), getActionMap());

		System.out.println("SwingScreen constructor: " + SwingUtilities.isEventDispatchThread());
		frame = new JFrame("Tetris");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 670);
		frame.setFocusable(true);
		frame.requestFocusInWindow();

		// Init JPanel
		setBackground(Color.BLACK);
		setOpaque(true);
		setDoubleBuffered(true);
		frame.setContentPane(this);

		// This method causes the event dispatch thread to paint.
		frame.setVisible(true);

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
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		System.out.println("SwingScreen.paintComponent()");
		System.out.println(displayedParts.size());
		while (displayedParts.size() == 0) {
			
		}
		for (Part displayPart: displayedParts) {
			// If the sprite for the part doesn't exist create it.
			System.out.println("SwingScreen.paintComponent() inside for loop");
			System.out.println(displayPart);
			if (displayPart.visual == null) {
				System.out.println("SwingScreen.paintComponent() newVisual");
				displayPart.visual = Game.me.engine.newVisual(displayPart);
			}
			((Sprite)displayPart.visual).draw(g2d);
		}
	}

	@Override
	public void update() {
		try {
			System.out.println("SwingScreen.update()");
			// Repaint right now!
			javax.swing.SwingUtilities.invokeAndWait(
				() -> paintImmediately(0, 0, this.getWidth() - 1, this.getHeight() - 1)
			);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) { }
		
		// This fixes a bug I've wasted many hours on.  Apparently my 
		// development systems were buffering graphic writes causing an erratic 
		// delay in visuals being displayed. The piece appeared to fall at a 
		// noticeably inconsistent rate even though swing painting happened 
		// correctly at constant intervals. The piece would fall at a constant 
		// rate when the mouse was moving around in front of the window (I'm 
		// assuming because the moving mouse graphic forced buffer. 
		//
		// Oracle/Sun, you suck. I should not have to call some cryptic method 
		// to force my system to refresh the window's display when something is 
		// drawn by swing. It should be done automatically by the JVM!
		Toolkit.getDefaultToolkit().sync();
	}

	@Override
	public void addParts(Collection<? extends Part> parts) {
		displayedParts.addAll(parts);
	}
	
}