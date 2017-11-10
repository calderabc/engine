package engine.graphics2d.swing;

import java.awt.*;
import java.io.File;

import engine.Coordinates;
import engine.graphics2d.ImageType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public final class SwingImageType extends ImageType {
	public SwingImageType(String configFileName,
	                      String defaultConfigFileName,
	                      String partLabel) {
		super(configFileName,
		      defaultConfigFileName,
		      partLabel,
		      BufferedImage.class);
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

	@Override
	public BufferedImage getScaledImage(Object image, Coordinates newDimensions) {
		BufferedImage newImage = new BufferedImage(newDimensions.x,
		                                           newDimensions.y,
		                                           ((BufferedImage) image).getType());
		newImage.createGraphics().drawImage((Image)image, 0, 0,
		                                    newDimensions.x, newDimensions.y,
		                                    null);
		return newImage;
	}

	@Override
	protected Coordinates imageSize(Object image) {
		return new Coordinates( ((BufferedImage)image).getWidth(),
		                        ((BufferedImage)image).getHeight() );
	}
}
