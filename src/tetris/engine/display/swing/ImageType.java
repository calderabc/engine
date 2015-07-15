package tetris.engine.display.swing;

public enum ImageType {
	BLOCK (0, 0,  4,  4, 13),
	DIGIT (0, 5, 30, 60, 10);
	
	private ImageType(int newX, int newY, int newWidth, int newHeight, int newImageCount) {
		X = newX;	
		Y = newY;
		WIDTH = newWidth;
		HEIGHT = newHeight;
		COUNT = newImageCount;
	}

	public final int X;
	public final int Y;
	public final int HEIGHT;
	public final int WIDTH;
	public final int COUNT;
}
