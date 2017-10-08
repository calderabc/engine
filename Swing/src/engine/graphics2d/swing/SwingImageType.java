package engine.graphics2d.swing;

import java.io.File;

import engine.Coordinates;
import engine.graphics2d.ImageList;
import engine.graphics2d.ImageType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public final class SwingImageType extends ImageType {
	public SwingImageType(ImageType newImageType, Coordinates scanStart) {
		super(newImageType, scanStart);
	}

	@Override
	protected BufferedImage loadImageFromFile(String fileName) throws IOException {
		return ImageIO.read(new File(imageType.imageFileName));
	}

	@Override
	protected BufferedImage getSubimage(BufferedImage image,
	                                    Coordinates position,
	                                    Coordinates dimensions) {
		return image.getSubimage(position.x, position.y,
		                         dimensions.x, dimensions.y);
	}
}
