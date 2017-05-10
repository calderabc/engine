package tetris.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.swing.ImageRenderer;
import engine.swing.ImageType;
import tetris.TetrisPiecePool;

public class BitmapGenerator {
	private static final int X=0;
	private static final int Y=1;
	
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
	
	private static int getImageIDFromCode(byte cellCode) {
		int imageID = -1;
		switch (cellCode) { // 
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
	
	public static void main(String argv[]) {
		final int IMAGES_PER_BLOCK = 8;
		final int BLOCK_DISPLAY_WIDTH = IMAGES_PER_BLOCK * ImageType.BLOCK.WIDTH;
		final int BLOCK_DISPLAY_HEIGHT = IMAGES_PER_BLOCK * ImageType.BLOCK.HEIGHT;

		int[][][] pieceTemplate = TetrisPiecePool.tetrisPieceTemplate;
		boolean[][] pieceMap;

		final BufferedImage[] images = ImageRenderer.loadImages(ImageType.BLOCK);

		BufferedImage[] pieceImages = new BufferedImage[pieceTemplate.length];

		int pieceID = 0;
		for (int[][] pieceCoords: pieceTemplate) {
			int maxX = 0, maxY = 0;

			for (int[] blockCoords: pieceCoords) {
				if (blockCoords[X] > maxX) {
					maxX = blockCoords[X];
				}

				if (blockCoords[Y] > maxY) {
					maxY = blockCoords[Y];
				}
			}
			
			int mapWidth = (maxX + 1);
			int mapHeight = (maxY + 1);


			pieceMap = new boolean[mapHeight][mapWidth];
			for (int[] blockCoords: pieceCoords) {
				pieceMap[blockCoords[Y]][blockCoords[X]] = true;
			}
			
			int expandedMapHeight = mapHeight * IMAGES_PER_BLOCK; 
			int expandedMapWidth = mapWidth * IMAGES_PER_BLOCK;

			boolean[][] expandedPieceMap = new boolean[expandedMapHeight][expandedMapWidth];
			for (int mapY = 0; mapY < mapHeight; mapY++) {
				for (int mapX = 0; mapX < mapWidth; mapX++) {
					int originY = mapY * IMAGES_PER_BLOCK;
					int originX = mapX * IMAGES_PER_BLOCK; 
					
					if (pieceMap[mapY][mapX]) {
						for (int fillY = originY; fillY < (originY + IMAGES_PER_BLOCK); fillY++) {
							for (int fillX = originX; fillX < (originX + IMAGES_PER_BLOCK); fillX++) {
								expandedPieceMap[fillY][fillX] = true;
							}
						}
					}
				}
			}
			
			
			byte[][] imageCellCodeMap = new byte[expandedMapHeight][expandedMapWidth];
			for (int expandedMapY = 0; expandedMapY < expandedMapHeight; expandedMapY++) {
				for (int expandedMapX = 0; expandedMapX < expandedMapWidth; expandedMapX++) {
					imageCellCodeMap[expandedMapY][expandedMapX] = 
						getCellImageCode(expandedPieceMap, expandedMapX, expandedMapY);
				}
			}
			
			int pieceDisplayWidth = BLOCK_DISPLAY_WIDTH * mapWidth;
			int pieceDisplayHeight = BLOCK_DISPLAY_HEIGHT * mapHeight;
			
			pieceImages[pieceID] = new BufferedImage(pieceDisplayWidth, 
			                                         pieceDisplayHeight, 
			                                         BufferedImage.TYPE_4BYTE_ABGR);
			Graphics pieceGraphics = pieceImages[pieceID].createGraphics();
			
			for (int expandedMapY = 0, drawY = 0; 
			     expandedMapY < expandedMapHeight; 
			     expandedMapY++, drawY += ImageType.BLOCK.HEIGHT) {

				for (int expandedMapX = 0, drawX = 0; 
				     expandedMapX < expandedMapWidth; 
				     expandedMapX++, drawX += ImageType.BLOCK.WIDTH) {

					byte cellCode = imageCellCodeMap[expandedMapY][expandedMapX];
					int imageID = getImageIDFromCode(cellCode);
					if (imageID >= 0) {
						pieceGraphics.drawImage(images[imageID], 
						                        drawX, drawY, 
						                        ImageType.BLOCK.WIDTH, ImageType.BLOCK.HEIGHT, 
						                        null);
					}
				}
			}

			try {
				ImageIO.write(pieceImages[pieceID], "png", new File("piece_image" + pieceID + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			pieceID++;
		}


	}
}
