package engine.graphics2d;

import java.io.IOException;


import engine.Coordinates;

public abstract class ImageList<T> {

	final public ImageType imageType;
	final private Object[] images;

	// Variable names shortened for brevety. "image" could be appended to beginning of each.
	public ImageList(ImageType newImageType, Coordinates scanStart) {
		imageType = newImageType;
		images = new Object[imageType.COUNT];
		
		try {
			T spriteImageSource = loadImageFromFile(imageType.imageFileName);

			int scanX = scanStart.x;
			int scanY = scanStart.y;
			int width = imageType.dimensions.x;
			int height = imageType.dimensions.y;
			for(int i = 0; i < imageType.COUNT; i++) {
				images[i] = getSubimage(spriteImageSource,
				                        new Coordinates(scanX, scanY),
				                        imageType.dimensions);
				
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



	public final T get(int index) {
		if (index < 0 || index >= imageType.COUNT) throw new IndexOutOfBoundsException();
		return (T)images[index];
	}

	protected abstract T loadImageFromFile(String fileName) throws IOException;
	protected abstract T getSubimage(T image, Coordinates position, Coordinates dimensions);
}
