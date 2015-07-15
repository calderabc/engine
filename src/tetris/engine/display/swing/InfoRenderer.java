package tetris.engine.display.swing;

import java.awt.Color;

import tetris.engine.display.Renderer;
import tetris.parts.Info;
import tetris.parts.Part;

@SuppressWarnings("serial")
public class InfoRenderer extends Renderer {
	Info info;
	
	private static final int padding = 10;
	
	public InfoRenderer (Part<?> newInfo) {
		info = (Info) newInfo;
		
		this.setOpaque(true);
		this.setBackground(Color.RED);
		this.setLayout(null);
		
		
		update();
		
	}
	
	
	@Override 
	public void update() {
		int width = InfoRenderer.padding;
		int childWidth;
		int height = 0;
		
		for (Part<?> child: info.getChildren()) {
			if (child instanceof Part<?>) {
				height += child.getRenderer().getHeight() + InfoRenderer.padding;
				
				childWidth = child.getRenderer().getWidth();
				if (childWidth > width) {
					width = childWidth;
				}
			}
		}
		
		//this.setMinimumSize(new Dimension(width + InfoRenderer.padding, height));
		//this.setSize(width + InfoRenderer.padding, height);
		this.setBounds(BoardRenderer.width + 2 * BoardRenderer.padding, 0, width + InfoRenderer.padding, height);
		System.out.println("***********************************************************");
		System.out.println(BoardRenderer.width);
		System.out.println("***********************************************************");
	}

}
