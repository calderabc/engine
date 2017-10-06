package engine.puzzle.swing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.Coordinates;
import engine.puzzle.ImageType;

public class ImageList {

	final public ImageType imageType;
	final private BufferedImage[] images;

	// Variable names shortened for brevety. "image" could be appended to beginning of each.
	public ImageList(ImageType newImageType, Coordinates scanStart) {
		imageType = newImageType;
		images = new BufferedImage[imageType.COUNT];
		
		try {
			BufferedImage spriteImageSource = 
				ImageIO.read(new File(imageType.IMAGE_FILE_NAME));
		
			int scanX = scanStart.x;
			int scanY = scanStart.y;
			int width = imageType.DIMENSIONS.x;
			int height = imageType.DIMENSIONS.y;
			for(int i = 0; i < imageType.COUNT; i++) {
				images[i] = spriteImageSource.getSubimage(scanX, scanY, width, height);
				
				if (imageType.SCAN_DIRECTION == ImageType.ScanDirection.VERTICAL)
					scanY += height;
				else
					scanX += width;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}	
	}
	
	public BufferedImage get(int index) {
		if (index < 0 || index >= imageType.COUNT) throw new IndexOutOfBoundsException();
		return images[index];
	}
}
