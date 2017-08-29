package engine.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Screen extends JFrame implements ComponentListener{
	public static int colorFilter = 0x40FFB0B0;
	
	private int colorFilterIndex = 0;
	private int[] colorMasks = {0x40FFB0B0, 0x40B0FFB0, 0x40B0B0FF, 0x40FFFFB0, 0x40FFB0FF, 0x40B0FFFF};
	
	
	public Screen() {
		super("Tetris");
		//panel = new JPanel();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.WHITE);
		
		JPanel pane = new JPanel();
		pane.setBackground(Color.BLACK);
		pane.setOpaque(true);
		pane.setDoubleBuffered(false);
		
		this.setContentPane(pane);
		
		this.setLayout(null);
		//frame.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		//frame.setUndecorated(true);
		//frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setVisible(true);
		
		
		this.setFocusable(true);
		//frame.addKeyListener(new Keyboard());
		
		this.requestFocusInWindow();
		
		this.setSize(700, 670);
		
		this.addComponentListener(this);
	}
	
	public void changeColorFilter() {
		colorFilterIndex++;
		
		if (colorFilterIndex > 5) {
			colorFilterIndex = 0;
		}
		
		Screen.colorFilter = colorMasks[colorFilterIndex];
		
/*		System.out.println(Integer.toHexString(ScreenRenderer.colorFilter)); */
		
	}
	
	// TODO: Figure out what the following are for.
	
	@Override
	public void componentHidden(ComponentEvent arg0) { }

	@Override
	public void componentMoved(ComponentEvent arg0) { }

	@Override
	public void componentResized(ComponentEvent arg0) {
		for(Component curr: this.getComponents()) {
			if (curr instanceof Renderer) {
				((Renderer) curr).update();
			}
			
		}
	}

	@Override
	public void componentShown(ComponentEvent arg0) { }
}
