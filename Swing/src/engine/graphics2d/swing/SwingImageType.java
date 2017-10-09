package engine.graphics2d.swing;

import java.io.File;

import engine.Coordinates;
import engine.Part;
import engine.graphics2d.ImageType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public final class SwingImageType extends ImageType {
	public SwingImageType(String configFileName,
	                      Class<? extends Part> partClass,
	                      String imageFileName) {
		super(configFileName, partClass, imageFileName, BufferedImage.class);
	}

	@Override
	protected BufferedImage loadImageFromFile(String fileName) {
		try {
			return ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null; // In case of exception.
	}

	@Override
	protected BufferedImage getSubimage(Object image,
	                                    Coordinates position,
	                                    Coordinates dimensions) {
		return ((BufferedImage)image).getSubimage(position.x, position.y,
		                              dimensions.x, dimensions.y);
	}
}
