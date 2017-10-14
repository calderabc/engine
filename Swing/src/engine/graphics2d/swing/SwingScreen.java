package engine.graphics2d.swing;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import engine.*;
import engine.graphics2d.Graphics2dScreen;
import engine.graphics2d.Sprite;
import engine.puzzle.Board;


@SuppressWarnings("serial")
public class SwingScreen extends Graphics2dScreen {
	public static int colorFilter = 0x40FFB0B0;
	public JFrame frame;

	public JPanel panel = new JPanel(true) {
		private void draw(Sprite sprite, Graphics2D canvas) {
			if (sprite != null) {
				canvas.drawImage((Image) sprite.getCurrImage(),
				sprite.position.x, sprite.position.y,
				null);
			}

		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			// Synchronized to avoid exception where another thread alters
			// visualParts' structure while this thread is iterating through it.
			synchronized (visualParts) {
				for (Part part : visualParts) {
					draw((Sprite)part.visual, g2d);
				}
			}
		}

	};

	private double scale;

	private int colorFilterIndex = 0;
	private static final int[] colorMasks = {0x40FFB0B0, 0x40B0FFB0, 0x40B0B0FF, 0x40FFFFB0, 0x40FFB0FF, 0x40B0FFFF};
	

	public SwingScreen(Game game) {
		super("Sprite");

		// TODO: Do this method call on another thread.
		SwingKeyboard.initInputActionMaps((Game)game, panel.getInputMap(), panel.getActionMap());
		frame = new JFrame("Tetris");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Make fullscreen.


		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//frame.setSize(1000, 800);
		//frame.setUndecorated(true);

		frame.setFocusable(true);
		frame.requestFocusInWindow();

		// Init JPanel
		panel.setBackground(Color.BLACK);
		panel.setOpaque(true);
		panel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				dimensions = new Coordinates(panel.getWidth(), panel.getHeight());
				System.out.println("jpanel is resized.");
				System.out.println(dimensions);
			}
		});
		frame.setContentPane(panel);


		// This method causes the event dispatch thread to paint.
		frame.setVisible(true);
		// TODO: There's got to be a better way to make sure this panel always has focus.
		panel.grabFocus();

	}
	
	public void changeColorFilter() {
		colorFilterIndex++;
		
		if (colorFilterIndex > 5) {
			colorFilterIndex = 0;
		}
		
		SwingScreen.colorFilter = colorMasks[colorFilterIndex];
	}

	@Override
	public void update() {
		try {
			// TODO: This repaints the whole screen.
			// TODO: Make it repaint only the area which has changed.
			//
			// Repaint right now!
			javax.swing.SwingUtilities.invokeAndWait(
				() -> panel.paintImmediately(0,
				                             0,
				                             panel.getWidth() - 1,
				                             panel.getHeight() - 1)
			);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) { }
		
		// This fixes a bug I've wasted many hours on.  Apparently my 
		// development systems were buffering graphic writes causing an erratic 
		// delay in visualParts being displayed. The piece appeared to fall at a
		// noticeably inconsistent rate even though swing painting happened 
		// correctly at constant intervals. The piece would fall at a constant 
		// rate when the mouse was moving around in front of the window (I'm 
		// assuming because the moving mouse graphic forced buffer refresh. 
		//
		// Oracle/Sun, you suck. I should not have to call some cryptic method 
		// to force my system to refresh the window's display when something is 
		// drawn by swing. It should be done automatically by the JVM!
		Toolkit.getDefaultToolkit().sync();
	}

}