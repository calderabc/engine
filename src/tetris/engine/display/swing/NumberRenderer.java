package tetris.engine.display.swing;

import java.awt.Color;

import tetris.engine.display.Renderer;
import tetris.parts.Digit;
import tetris.parts.Number;
import tetris.parts.Part;


@SuppressWarnings("serial")
public class NumberRenderer extends Renderer {
	public static final int padding = 5;
	
	private Number number;
	
	
	public NumberRenderer(Part<?> newNumber) {
		number = (Number) newNumber;
		
		this.setOpaque(true);
		this.setBackground(Color.GRAY);
		this.setLayout(null);
	}
	
	
	@Override
	public void update() {
		int width = NumberRenderer.padding;
		int height = 0;
		int digitHeight;
		
		for(Part<?> child: number.getChildren()) {
			if (child instanceof Digit) {
				
				width += child.getRenderer().getWidth() 
						 + NumberRenderer.padding;
				
				digitHeight = child.getRenderer().getHeight();
				if (digitHeight > height) {
					height = digitHeight;
				}
			}
		}
		
		height += 2 * NumberRenderer.padding;
		
		this.setBounds(number.getX() * 80, number.getY() * 80 + 10, width, height);
		
	}
}
