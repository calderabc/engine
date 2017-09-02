package engine.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;

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
	
	private List<? extends Part> displayedParts;
	

	public SwingScreen(List<? extends Part> newDisplayedParts) {
		displayedParts = newDisplayedParts;
		frame = new JFrame("Tetris");
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

		//RepaintManager.currentManager(this).setDoubleBufferingEnabled(false);

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
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		System.out.println("paintComponent");
		System.out.println(javax.swing.SwingUtilities.isEventDispatchThread());
		System.out.println(System.currentTimeMillis());
		for (Part displayPart: displayedParts) {
			// If the sprite for the part doesn't exist create it.
			if (displayPart.visual == null) {
				System.out.println("new Visual");
				displayPart.visual = Game.me.engine.newVisual(displayPart);
			}
			((Sprite)displayPart.visual).draw(g2d);
		}
		System.out.println(System.currentTimeMillis());
	}

	@Override
	public void update() {
		System.out.println("Update >>>>>>");
		System.out.println(System.currentTimeMillis());
		System.out.println(javax.swing.SwingUtilities.isEventDispatchThread());

		try {
			// Repaint right now!
			javax.swing.SwingUtilities.invokeAndWait(() -> paintImmediately(0, 0, this.getWidth() - 1, this.getHeight() - 1));
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// This fixes a bug I've wasted many hours on.  Apparently my 
		// development systems were buffering graphic writes causing 
		// a erratic delay in visuals being displayed. The piece 
		// appeared to fall at a noticeably inconsistent rate even 
		// though swing painting was happening correctly at constant 
		// intervals. The piece would fall at a constant rate when 
		// the mouse was moving around in front of the window (I'm 
		// assuming because the moving mouse graphic forced buffer 
		// swap). Oracle/Sun, you suck. I should not have to use some 
		// cryptic method call to force my system to refresh the 
		// window's display when something is drawn by swing.  
		// It should be done automatically by the JVM!
		Toolkit.getDefaultToolkit().sync();
	}
	
}