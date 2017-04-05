package engine.swing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.Part;
import engine.puzzle.Digit;


@SuppressWarnings("serial")
public class DigitRenderer extends ImageRenderer {
	private static final BufferedImage[] images = loadDigitImages();
	
	private static BufferedImage[] loadDigitImages() {
		BufferedImage numeralImage;
		BufferedImage[] loadImages = new BufferedImage[10];
		
		try {
			numeralImage = ImageIO.read(new File("numbers.png"));
			
			int imageWidth = numeralImage.getWidth() / 10;
			int imageHeight = numeralImage.getHeight();
			
			for (int i = 0; i < 10; i++) {
				loadImages[i] = numeralImage.getSubimage(i * imageWidth, 
														 0, 
														 imageWidth, 
														 imageHeight);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
			
		return loadImages;
	}
	
	
	
	private Digit digit;	
	
	public DigitRenderer(Part newDigit) {
		super(30, 60);
		
		digit = (Digit) newDigit;
		
		this.setOpaque(false);
		
		this.setBounds(
			digit.pos.x * (getWidth() + NumberRenderer.padding) + NumberRenderer.padding,
			NumberRenderer.padding, 
			getWidth(), 
			getHeight());
		
		update();
	}
	
	@Override
	public void update() {
		setImage(images[digit.getValue()]);
	}
}
