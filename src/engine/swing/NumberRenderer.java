package engine.swing;

import java.awt.Color;

import engine.Part;
import engine.puzzle.Digit;
import engine.puzzle.Number;


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
		
		this.setBounds(number.pos.x * 80, number.pos.y * 80 + 10, width, height);
		
	}
}
