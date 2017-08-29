package engine.swing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.lang.reflect.Method;

import engine.Coordinates;
import engine.Part;
import engine.puzzle.Block;
import engine.puzzle.Game;
import engine.puzzle.tetris.swing.ImageType;

@SuppressWarnings("serial")
public class BlockRenderer extends ImageRenderer {
	public static final int IMAGES_PER_BLOCK = 8;
	public static final int NUM_OF_ORIENTATIONS = 4;
	public static final int BUFF_IMAGE_TYPE = BufferedImage.TYPE_4BYTE_ABGR;


	private final BufferedImage[] blockImages;

	public static String blockImageFile = "piece_image_sheet.png";

	private BufferedImage everyBlockImage;

	public int getDisplayWidth() {
		return getWidth();
	}

	public int getDisplayHeight() {
		return getHeight();
	}
	
	public void loadBlockImages (Game myGame) {
		String packageName = this.getClass().getPackage().getName();
		
		String typeOfGame = myGame.getClass().getName();
		switch (typeOfGame) {
			case "Tetris": packageName += ".tetris";
			default:
		}
		
		Class<?> bitmapGeneratorClass;

		try {
			bitmapGeneratorClass = Class.forName(packageName + ".BitmapGenerator");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		Method main = bitmapGeneratorClass.getMethod("main", String[].class);
		main.invoke(bitmapGeneratorClass, new String() );
		*/

		
	}

//*****************************************************************************

	private BufferedImage currBlockImage;
	
	private void drawBlockImage() {
		this.drawImageOnMe(currBlockImage, 0, 0, getDisplayWidth(), getDisplayHeight());
	}

	public void orient(int orientation) {
		currBlockImage = blockImages[orientation];
	}

	public BlockRenderer(Block newBlock) {
		super(IMAGES_PER_BLOCK * 4,
		      IMAGES_PER_BLOCK * 4);
		//System.out.println("BlockRenderer");

		BufferedImage blockImageSheet;
		try {
			blockImageSheet = ImageIO.read(new File(blockImageFile));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}	
		
		/*
		blockImageSheet.getSubimage(x, y, w, h)
		
		everyBlockImage = new BufferedImage(displayWidth, displayHeight, BUFF_IMAGE_TYPE);
		*/

		blockImages = new BufferedImage[NUM_OF_ORIENTATIONS];


		this.setOpaque(false);
		this.setBackground(Color.RED); // for testing

		orient(0);
		drawBlockImage();
	}
	
	public void update(Coordinates partPosition) {
		drawBlockImage();
	}
	/*
	@Override
	public void init() {
		this.drawImageOnMe(baseImages[block.getType()], 0, 0, getDisplayWidth(), getDisplayHeight());
	}
	*/

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
