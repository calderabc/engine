package tetris.engine.display.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

import tetris.engine.display.Renderer;
import tetris.parts.Board;
import tetris.parts.Part;
import tetris.parts.Screen;

@SuppressWarnings("serial")
public class ScreenRenderer extends Renderer implements ComponentListener{
	public static int colorFilter = 0x40FFB0B0;
	
	private int colorFilterIndex = 0;
	private int[] colorMasks = {0x40FFB0B0, 0x40B0FFB0, 0x40B0B0FF, 0x40FFFFB0, 0x40FFB0FF, 0x40B0FFFF};
	
	
	private JFrame frame;
	
	private boolean isSizeSet = false;
	
	private Screen screen;
	
	public ScreenRenderer(Part<?> newScreen) {
		//System.out.println("ScreenRenderer");
		screen = (Screen) newScreen;
		
		
		frame = new JFrame("Tetris");
		//panel = new JPanel();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.WHITE);
		
		
		
		
		this.setBackground(Color.BLACK);
		this.setOpaque(true);
		
		
		frame.setContentPane(this);
		super.setDoubleBuffered(false);
		
		frame.setLayout(null);
		//frame.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		//frame.setUndecorated(true);
		//frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		
		
		frame.setFocusable(true);
		//frame.addKeyListener(new Keyboard());
		
		this.requestFocusInWindow();
		
		update();
		
		frame.addComponentListener(this);
	}
	
	public void changeColorFilter() {
		colorFilterIndex++;
		
		if (colorFilterIndex > 5) {
			colorFilterIndex = 0;
		}
		
		ScreenRenderer.colorFilter = colorMasks[colorFilterIndex];
		
		System.out.println(Integer.toHexString(ScreenRenderer.colorFilter));
		
	}
	
	/*
	@Override
	public void update() {
		//System.out.println("Update ScreenRenderer");
		if (!isSizeSet) {
			for (Part currChild: screen.getChildren()) {
				if (currChild instanceof Board) {
					JComponent com = ((RenderablePart) currChild).getRenderer();
					frame.setSize(com.getWidth() + 20, com.getHeight() + 20 + 30);
					frame.setBounds(0, 0, com.getWidth() + 20, com.getHeight() + 20 + 30);
					
					isSizeSet = true;
				}
			}
		}
	}
	*/
	
	@Override
	public void update() {
		//System.out.println("Update ScreenRenderer");
	//	if (!isSizeSet) {
		
			int width = 20;
			int height = 50;
			for (Part<?> currChild: screen.getChildren()) {
				JComponent com = currChild.getRenderer();
				width += com.getWidth(); 
				
				if (currChild instanceof Board) {
					height += com.getHeight();
				}
			}
			
			//frame.setSize(width, height);
			frame.setSize(700, 670);
			//frame.setPreferredSize(new Dimension(width, height));
			//frame.pack();
			
			isSizeSet = true;
			
	//	}
	}

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
