package engine.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import engine.Part;
import engine.Screen;
import engine.puzzle.tetris.TetrisGame;
import engine.puzzle.tetris.swing.Keyboard;


@SuppressWarnings("serial")
public class SwingScreen extends JPanel implements Screen {
	public static int colorFilter = 0x40FFB0B0;
	public JFrame frame;
	
	private int colorFilterIndex = 0;
	private int[] colorMasks = {0x40FFB0B0, 0x40B0FFB0, 0x40B0B0FF, 0x40FFFFB0, 0x40FFB0FF, 0x40B0FFFF};
	
	private List<Part> displayedParts = new Vector<Part>();
	

	public SwingScreen(TetrisGame game) {
		// TODO: Do this method call on another thread.
		Keyboard.initInputActionMaps(game, getInputMap(), getActionMap());
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
		// TODO: There's got to be a better way to make sure this panel always has focus.
		grabFocus();

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
		while (displayedParts.size() == 0) {
			
		}
		for (Part displayPart: displayedParts) {
			((Sprite)displayPart.visual).draw(g2d);
		}
	}

	@Override
	public void update() {
		try {
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
	
	@Override
	public void addPart(Part part) {
		displayedParts.add(part);
	}

	@Override
	public void removePart(Part terminalPart) {
		displayedParts.remove(terminalPart);
	}
	
}