package engine.graphics2d;

import java.io.IOException;
import java.lang.reflect.Array;


import engine.Coordinates;

public class ImageList<T> {
	final public ImageType imageType;
	final private T[] images;

	// Variable names shortened for brevety. "image" could be appended to beginning of each.
	public ImageList(ImageType newImageType, Class<? extends T> imageClass, int imageCount) {
		imageType = newImageType;
		//noinspection unchecked
		images = (T[])Array.newInstance(imageClass, imageCount);
	}

	public final T get(int index) {
		return images[index];
	}

	public final int size() {
			return images.length;
	}
}
