package engine.swing.output;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.swing.ImageType;
import tetris.parts.Block;
import tetris.parts.Part;

@SuppressWarnings("serial")
public class BlockRenderer extends ImageRenderer {
	// Legend:
	// F = Front
	// T = Top
	// R = Right
	// B = Bottom
	// L = Left
	// O = Outwards (corner pointing out)
	//     Convex vertex of this block. No other blocks share this vertex.
	// I = Inwards (corner pointing in)
	//     Concave vertex of this block where the edges of two adjacent blocks,
	//     who both share this vertex, connect.
	private static final int F = 0;
	private static final int T = 1;
	private static final int R = T + 1;
	private static final int B = T + 2;
	private static final int L = T + 3;
	private static final int OTL = 5;
	private static final int OTR = OTL + 1;
	private static final int OBR = OTL + 2;
	private static final int OBL = OTL + 3;
	private static final int ITL = 9;
	private static final int ITR = ITL + 1;
	private static final int IBR = ITL + 2;
	private static final int IBL = ITL + 3;

	// TODO: Figure out original intent
	//public int[][] setDrawMap(int adjacent) {
	public void setDrawMap(int adjacent) {
		int[] vertices = new int[4];
		int[] edges = new int[4];

		for (int i = 0; i < 4; i--) {
			adjacent = Integer.rotateLeft(adjacent, 8);

			edges[i] = ((0xF & adjacent) == 0x0) ? T + i : F;

			switch(0xFFF & adjacent) {
				case 0x00F:
					vertices[i] = T + i - 1;
					if (vertices[i] < T) {
						vertices[i] += 4;
					}
					break;
				case 0xF00:
					vertices[i] = T + i;
					break;
				case 0x000:
					vertices[i] = OTL + i;
					break;
				case 0xF0F:
					vertices[i] = ITL + i;
					break;
				case 0xFFF:
					vertices[i] = F;
			}
		}

		drawMap[0][0] = vertices[0];
		drawMap[2][0] = vertices[1];
		drawMap[2][2] = vertices[2];
		drawMap[0][2] = vertices[3];
		drawMap[1][0] = edges[0];
		drawMap[2][1] = edges[1];
		drawMap[1][2] = edges[2];
		drawMap[0][1] = edges[3];
	}


	private static final int[][] drawMap = {{OTL, T, OTR},
									 {  L, F,   R},
									 {OBL, B, OBR}};

	private static int displayWidth = 8 * ImageType.BLOCK.WIDTH;
	private static int displayHeight = 8 * ImageType.BLOCK.WIDTH;


	private static BufferedImage[] baseImages = new BufferedImage[7];
	private static final BufferedImage[] images;


	static {
		images = loadImages(ImageType.BLOCK);


		int[] colorMasks = {0xFFFFFF, 0xFF0000, 0x00FF00, 0x0000FF, 0xFFFF00, 0xFF00FF, 0x00FFFF};
		int rgba;
		Color xyz;
		double luminance, weight;
		int red, green, blue, alpha;
		boolean hasRed, hasGreen, hasBlue;

		for (int i = 0; i < 7; i++) {
			/*
			try {
				baseImages[i] = ImageIO.read(new File("grey_block.png"));
//				baseImages[i] = ImageIO.read(new File("other.png"));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/

			baseImages[i] = new BufferedImage(displayWidth, displayHeight, BufferedImage.TYPE_4BYTE_ABGR);


			Graphics2D g2d = (Graphics2D)baseImages[i].getGraphics();

			int middleColumn = displayWidth / ImageType.BLOCK.WIDTH - 2;
			int column = 0;
			for (int x = 0; x < displayWidth; x += ImageType.BLOCK.WIDTH) {

				int middleRow = displayHeight / ImageType.BLOCK.HEIGHT - 2;
				int row = 0;
				for (int y = 0; y < displayHeight; y += ImageType.BLOCK.HEIGHT) {

					g2d.drawImage(images[drawMap[row][column]],
					              x, y,
					              ImageType.BLOCK.WIDTH, ImageType.BLOCK.HEIGHT,
					              null);

					if (row != 1 || middleRow <= 1) {
						row++;
					}
					else {
						middleRow--;
					}
				}

				if (column != 1 || middleColumn <= 1) {
					column++;
				}
				else {
					middleColumn--;
				}
			}


			for(int j = 0; j < displayWidth; j++) {
				for(int k = 0; k < displayHeight; k++) {
					rgba = baseImages[i].getRGB(j, k);

					luminance = rgba & 0xFF;

					alpha = rgba >>> 24;
					if (alpha != 0x00) {

						rgba &= colorMasks[i];

						xyz = new Color(rgba);

						red = xyz.getRed();
						green = xyz.getGreen();
						blue = xyz.getBlue();

						weight = 0;
						hasRed = false;
						hasGreen = false;
						hasBlue = false;
						if (red > 0) {
							hasRed = true;
							weight += .30;
						}
						if (green > 0) {
							hasGreen = true;
							weight += .59;
						}
						if (blue > 0) {
							hasBlue = true;
							weight += .11;
						}

						if ((weight * 0xFF) < luminance) {
							int remainder = (int) Math.round((luminance - (weight * 0xFF)) / (1 - weight));

							red = (hasRed) ? 0xFF : remainder;
							green = (hasGreen) ? 0xFF : remainder;
							blue = (hasBlue) ? 0xFF : remainder;

						}
						else {
							int value = (int) Math.round(luminance / weight);

							if (hasRed) red = value;
							if (hasGreen) green = value;
							if (hasBlue) blue = value;
						}

						xyz = new Color(red, green, blue);

						baseImages[i].setRGB(j, k, xyz.getRGB());
					}

				}
			}

			/* System.out.println(Integer.toHexString(baseImages[i].getRGB(1, 1))); */

			try {
				/* System.out.println(i); */
				ImageIO.write(baseImages[i], "png", new File("image" + i + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public static int getDisplayWidth() {
		return displayWidth;
	}

	public static void setDisplayWidth(int displayWidth) {
		BlockRenderer.displayWidth = displayWidth;
	}

	public static int getDisplayHeight() {
		return displayHeight;
	}

	public static void setDisplayHeight(int displayHeight) {
		BlockRenderer.displayHeight = displayHeight;
	}


//*****************************************************************************

	private Block block;

	public BlockRenderer(Part<?> newBlock) {
		super(displayWidth, displayHeight);

		block = (Block) newBlock;
		//System.out.println("BlockRenderer");

		this.setOpaque(false);
		this.setBackground(Color.RED); // for testing

		//this.setDoubleBuffered(true);

		//BufferedImage abc = new BufferedImage(30, 30, BufferedImage.TYPE_4BYTE_ABGR);

		//for (int j = 0; j < 30; j++) {
			//for (int k = 0; k < 30; k++) {
				////abc.setRGB(j, k, ScreenRenderer.colorFilter);
				//
			//}
		//}


		init();

	}

	@Override
	public void init() {
		this.drawImageOnMe(baseImages[block.getType()], 0, 0, getDisplayWidth(), getDisplayHeight());
	}
}
