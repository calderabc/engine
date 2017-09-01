package engine.puzzle.tetris.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.puzzle.Game;
import engine.swing.BlockRenderer;
//import engine.swing.ImageRenderer;

public class BitmapGenerator {
	private static final int X=0;
	private static final int Y=1;
	
	// TODO: Make more elegant. Low priority.
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

	private static final int BLOCK_DISPLAY_WIDTH = 
			BlockRenderer.IMAGES_PER_BLOCK * 4;
	private static final int BLOCK_DISPLAY_HEIGHT = 
			BlockRenderer.IMAGES_PER_BLOCK * 4;
	private static final int SHEET_DISPLAY_WIDTH = 
			BlockRenderer.NUM_OF_ORIENTATIONS * BLOCK_DISPLAY_WIDTH;

	private static final BufferedImage spritesImage;

	static {
		/* Using tempImage so compiler doesn't complain that spritesImage might 
		 * not get initialized. Thank you over-protective compiler. */
		BufferedImage tempImage = null;
		try {
			tempImage = ImageIO.read(new File("sprites.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}	
		spritesImage = tempImage; 
	}

	private static final int COUNT = 13;
	private static final BufferedImage[] images = loadImages(ImageType.BLOCK);

	public static final BufferedImage[] loadImages(ImageType type) {
		BufferedImage[] returnImages = new BufferedImage[COUNT]; 
		
		for (int i = 0; i < COUNT; i++) {
			returnImages[i] = spritesImage.getSubimage(i * 4, 0, 4, 4);
		}
		
		return returnImages;
	}	

	// Easier than doing programmatically.
	private static final int[][] aroundCoords = {{-1,-1},
	                                             { 0,-1},
	                                             { 1,-1},
	                                             { 1, 0},
	                                             { 1, 1},
	                                             { 0, 1},
	                                             {-1, 1},
	                                             {-1, 0}};

	private static byte getCellImageCode(boolean[][] expandedPieceMap, int cellX, int cellY) {
		byte code = 0b0;
		byte mask = (byte)0b1000_0000;
		for (int[] offsetCoords: aroundCoords) {
			int testX = cellX + offsetCoords[X];
			int testY = cellY + offsetCoords[Y];
			if (testY >= 0 && testX >= 0 
			    && testY < expandedPieceMap.length && testX < expandedPieceMap[0].length 
			    && expandedPieceMap[testY][testX]) {

				code |= mask;
			}
			
			/* One would expect 'mask >>>= 1' to work.  But Java converts byte
			 * values to int with a signed conversion before bitwise operations.  
			 * So the result of '>>>=' on a byte value is the same as '>>='. 
			 * Unexpected and silly. C/C++ 'unsigned' for the win.
			 */
			// Force unsigned conversion to int before "unsigned" shift. Lame.
			mask = (byte)((mask & 0xFF) >>> 1); 
		}
		return code;
	}

	private static boolean[][] getExpandedPieceMap(boolean[][] pieceMap, 
	                                               int expandedMapWidth, 
	                                               int expandedMapHeight) {
		int mapHeight = pieceMap.length;
		int mapWidth = pieceMap[0].length;

		boolean[][] returnExpandedPieceMap = new boolean[expandedMapHeight][expandedMapWidth];
		for (int mapY = 0; mapY < mapHeight; mapY++) {
			for (int mapX = 0; mapX < mapWidth; mapX++) {
				int originY = mapY * BlockRenderer.IMAGES_PER_BLOCK;
				int originX = mapX * BlockRenderer.IMAGES_PER_BLOCK; 
				
				if (pieceMap[mapY][mapX]) {
					for (int fillY = originY; 
					     fillY < (originY + BlockRenderer.IMAGES_PER_BLOCK); 
					     fillY++) {

						for (int fillX = originX; 
						     fillX < (originX + BlockRenderer.IMAGES_PER_BLOCK); 
						     fillX++) {

							returnExpandedPieceMap[fillY][fillX] = true;
						}
					}
				}
			}
		}
		return returnExpandedPieceMap;
	}	

	private static byte[][] getImageCellCodeMap(boolean[][] expandedPieceMap, int expandedMapWidth, int expandedMapHeight) {
		byte[][] returnImageCellCodeMap	= new byte[expandedMapHeight][expandedMapWidth];
		for (int expandedMapY = 0; expandedMapY < expandedMapHeight; expandedMapY++) {
			for (int expandedMapX = 0; expandedMapX < expandedMapWidth; expandedMapX++) {
				returnImageCellCodeMap[expandedMapY][expandedMapX] = 
					getCellImageCode(expandedPieceMap, expandedMapX, expandedMapY);
			}
		}
		return returnImageCellCodeMap;
	}
	
	private static int getImageIDFromCode(byte cellCode) {
		int imageID = -1;
		switch (cellCode) { // (byte) conversions -> more Java no unsigned lameness.
			case (byte)0b11111111: imageID = F; break;
			case       0b01111111: imageID = ITL; break;
			case (byte)0b11011111: imageID = ITR; break;
			case (byte)0b11110111: imageID = IBR; break;
			case (byte)0b11111101: imageID = IBL; break;
			case (byte)0b10011100: 
			case       0b00011100: imageID = OTL; break;
			case       0b00100111:
			case       0b00000111: imageID = OTR; break;
			case (byte)0b11001001:
			case (byte)0b11000001: imageID = OBR; break;
			case       0b01110010:
			case       0b01110000: imageID = OBL; break;
			default:
				switch((byte)(cellCode & 0b01010101)) {
					case 0b00010101: imageID = T; break;
					case 0b01000101: imageID = R; break;
					case 0b01010001: imageID = B; break;
					case 0b01010100: imageID = L; break;
				};
		}
		return imageID;
	}

	private static BufferedImage drawPieceImage(byte[][] imageCellCodeMap, int expandedMapWidth, int expandedMapHeight) {
		int pieceDisplayWidth = expandedMapWidth * 4;
		int pieceDisplayHeight = expandedMapHeight * 4;
				
		BufferedImage returnPieceImage = new BufferedImage(pieceDisplayWidth, 
		                                                   pieceDisplayHeight, 
		                                                   BlockRenderer.BUFF_IMAGE_TYPE);
		Graphics pieceGraphics = returnPieceImage.createGraphics();
		
		for (int expandedMapY = 0, drawY = 0; 
			 expandedMapY < expandedMapHeight; 
			 expandedMapY++, drawY += 4) {

			for (int expandedMapX = 0, drawX = 0; 
				 expandedMapX < expandedMapWidth; 
				 expandedMapX++, drawX += 4) {

				byte cellCode = imageCellCodeMap[expandedMapY][expandedMapX];
				int imageID = getImageIDFromCode(cellCode);
				if (imageID >= 0) {
					pieceGraphics.drawImage(images[imageID], 
											drawX, drawY, 
											4, 4, 
											null);
				}
			}
		}
		
		return returnPieceImage;
	}

	/* Decorate each piece custom.  For now just adds color filter. */
	// TODO: Old code. Improve.  Not a high priority.
	private static void decoratePiece(BufferedImage image, int imageID) {
		int rgba;
		double luminance, weight;
		int red, green, blue, alpha;
		Color xyz;
		int[] colorMasks = {0xFFFFFF, 0xFF0000, 0x00FF00, 0x0000FF, 0xFFFF00, 0xFF00FF, 0x00FFFF};
		boolean hasRed, hasGreen, hasBlue;
			
		int displayWidth = image.getWidth();
		int displayHeight = image.getHeight();

		for(int j = 0; j < displayWidth; j++) {
			for(int k = 0; k < displayHeight; k++) {
				rgba = image.getRGB(j, k);

				luminance = rgba & 0xFF;

				alpha = rgba >>> 24;
				if (alpha != 0x00) {
					rgba &= colorMasks[imageID];

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
					image.setRGB(j, k, xyz.getRGB());
				}
			}
		}
	}
	
	// Rotate piece coordinates 90 degrees clockwise
	private static void rotatePiece(int[][] pieceCoords, int[] max) {
		int dummy;
		for (int[] blockCoords: pieceCoords) {
			dummy = blockCoords[X];
			blockCoords[X] = max[Y] - blockCoords[Y];
			blockCoords[Y] = dummy;
		}
		
		dummy = max[Y];
		max[Y] = max[X];
		max[X] = dummy;
	}

	
	public static void main(String argv[]) {
		int maxSpriteSheetHeight = 0;
		int[][][] pieceTemplate = Game.TETRIS_PIECE_DATA.pieceTemplate;
		int numOfPieces = pieceTemplate.length;

		BufferedImage[] spriteSheet = new BufferedImage[numOfPieces];

		int pieceID = 0;
		for (int[][] pieceCoords: pieceTemplate) {
			BufferedImage[] pieceImages = new BufferedImage[BlockRenderer.NUM_OF_ORIENTATIONS];
				
			int spriteSheetHeight = pieceCoords.length * BLOCK_DISPLAY_HEIGHT;
			if (spriteSheetHeight > maxSpriteSheetHeight) {
				maxSpriteSheetHeight = spriteSheetHeight;
			}

			spriteSheet[pieceID] = new BufferedImage(SHEET_DISPLAY_WIDTH, 
			                                         spriteSheetHeight, 
			                                         BlockRenderer.BUFF_IMAGE_TYPE);
			Graphics spriteSheetGraphics = spriteSheet[pieceID].createGraphics();
			
			int[] max = new int[2];
			for (int[] blockCoords: pieceCoords) {
				if (blockCoords[X] > max[X]) max[X] = blockCoords[X];
				if (blockCoords[Y] > max[Y]) max[Y] = blockCoords[Y];
			}

			for (int orient = 0; orient < BlockRenderer.NUM_OF_ORIENTATIONS; orient++) {
				int mapWidth = max[X] + 1;
				int mapHeight = max[Y] + 1;

				boolean[][] pieceMap = new boolean[mapHeight][mapWidth];
				for (int[] blockCoords: pieceCoords) {
					pieceMap[blockCoords[Y]][blockCoords[X]] = true;
				}
				
				int expandedMapHeight = mapHeight * BlockRenderer.IMAGES_PER_BLOCK; 
				int expandedMapWidth = mapWidth * BlockRenderer.IMAGES_PER_BLOCK;

				boolean[][] expandedPieceMap = getExpandedPieceMap(pieceMap, 
				                                                   expandedMapWidth, 
				                                                   expandedMapHeight);
						
				byte[][] imageCellCodeMap = getImageCellCodeMap(expandedPieceMap, 
				                                                expandedMapWidth, 
				                                                expandedMapHeight);
				
				pieceImages[orient] = drawPieceImage(imageCellCodeMap, 
				                                     expandedMapWidth, 
				                                     expandedMapHeight);
						
				decoratePiece(pieceImages[orient], pieceID);
				
				int yDrawPos = 0;
				for (int[] blockCoords: pieceCoords) {
					spriteSheetGraphics.drawImage(
						pieceImages[orient].getSubimage(
							blockCoords[X] * BLOCK_DISPLAY_WIDTH, 
							blockCoords[Y] * BLOCK_DISPLAY_HEIGHT,
							BLOCK_DISPLAY_WIDTH,
							BLOCK_DISPLAY_HEIGHT),
						orient * BLOCK_DISPLAY_WIDTH, 
						yDrawPos, 
						BLOCK_DISPLAY_WIDTH,
						BLOCK_DISPLAY_HEIGHT,
						null);

					yDrawPos += BLOCK_DISPLAY_WIDTH;
				}
				
				rotatePiece(pieceCoords, max);
			}

			pieceID++;
		}
		
		BufferedImage imageToFile = new BufferedImage(pieceID * SHEET_DISPLAY_WIDTH, 
		                                              maxSpriteSheetHeight,
		                                              BlockRenderer.BUFF_IMAGE_TYPE);
		Graphics imageToFileGraphics = imageToFile.createGraphics();
		for (int i = 0; i < numOfPieces; i++) {
			imageToFileGraphics.drawImage(spriteSheet[i], 
			                              i * SHEET_DISPLAY_WIDTH, 0, 
			                              spriteSheet[i].getHeight(), SHEET_DISPLAY_WIDTH, 
			                              null);
		}

		try {
			ImageIO.write(imageToFile, "png", new File(BlockRenderer.blockImageFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
