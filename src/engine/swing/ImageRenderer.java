package engine.swing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


@SuppressWarnings("serial")
public abstract class ImageRenderer extends Renderer {
	
	//private static final HashMap<ImageReadY, Integer> imagesReadY;
	
	private static final BufferedImage spritesImage;
	
	static {
		BufferedImage tempImage = null;
		
		try {
			tempImage = ImageIO.read(new File("sprites.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}	
		
		spritesImage = tempImage; 
	}
	
	protected static final BufferedImage[] loadImages(ImageType type) {
		BufferedImage[] returnImages = new BufferedImage[type.COUNT]; 
		
		for (int i = 0; i < type.COUNT; i++) {
			returnImages[i] = spritesImage.getSubimage(type.X + i * type.WIDTH, type.Y, type.WIDTH, type.HEIGHT);
		}
		
		return returnImages;
	}
	
//*****************************************************************************	
	
	
	protected int readWidth;
	protected int readHeight;
	
	private BufferedImage image;
	
	private int width;
	private int height;
	
	protected ImageRenderer() {
		newImage();
	}
	
	
	public ImageRenderer(int newWidth, int newHeight) {
		newImage(newWidth, newHeight);
		
	}
	
	protected final void newImage() {
		newImage(2000, 2000);
	}
	
	protected final void newImage(int newWidth, int newHeight) {
		image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_4BYTE_ABGR );
	}
	
	protected final void setImage(BufferedImage newImage) {
		image = newImage;
	}
	
	private final BufferedImage getImage() {
		return image;
	}
	
	protected final BufferedImage drawImageOnMe(BufferedImage otherImage, int oX, int oY, int oWidth, int oHeight) {
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		g2d.drawImage(otherImage, oX, oY, oWidth, oHeight, null);
		
		return image;
	}
	
	protected final BufferedImage drawImageOnMe(ImageRenderer other, int oX, int oY, int oWidth, int oHeight) {
		return drawImageOnMe(other.getImage(), oX, oY, oWidth, oHeight);
	}
	
	protected final BufferedImage drawImageOnMe(BufferedImage otherImage, int oX, int oY) {
		return drawImageOnMe(otherImage, oX, oY, otherImage.getWidth(), otherImage.getHeight());
	}
	
	protected final BufferedImage drawImageOnMe(ImageRenderer other, int oX, int oY) {
		return drawImageOnMe(other, oX, oY, other.getWidth(), other.getHeight());
	}
	
	// scale passed image to cover this image
	protected final BufferedImage drawImageOnMe(BufferedImage otherImage) {
		return drawImageOnMe(otherImage, 0, 0, width, height);
	}
	
	// scale passed ImageRenderer to cover this image
	protected final BufferedImage drawImageOnMe(ImageRenderer other) {
		return drawImageOnMe(other.getImage());
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
	@Override 
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}

}
