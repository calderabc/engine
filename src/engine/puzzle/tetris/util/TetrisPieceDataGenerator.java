package engine.puzzle.tetris.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import engine.Coordinates;
import engine.puzzle.tetris.TetrisPieceData;

public class TetrisPieceDataGenerator {

	public static final int[][][] pieceTemplate =
		{{{0, 0}, {1, 0}, {0, 1}, {1, 1}},  //  ##
											//  ##
		 {{0, 0}, {1, 0}, {2, 0}, {1, 1}},  //     ###
											//      #
		 {{0, 0}, {1, 0}, {2, 0}, {2, 1}},  //  ###
											//    #
		 {{0, 0}, {1, 0}, {2, 0}, {0, 1}},  //      ###
											//      #
		 {{0, 0}, {1, 0}, {2, 0}, {3, 0}},  // ####
											//
		 {{0, 0}, {1, 0}, {2, 1}, {1, 1}},  //    ##
											//     ##
		 {{0, 1}, {1, 0}, {2, 0}, {1, 1}}}; //  ##
											// ##
	
	/* These values are twice their intended values so that floating point half
	 * fractions can be represented as integers. */
	public static final int[][] pieceCenter = {{1, 1, 1, 1},
	                                           {2, 0, 2, 0},
	                                           {2, 0, 2, 0}, 
	                                           {2, 0, 2, 0}, 
	                                           {3, 0, 4,-1},
	                                           {2, 1, 3, 0},
	                                           {2, 1, 3, 0}};

	public static final Coordinates pieceStartPos = new Coordinates(3, 0);

	public static void main(String argv[]) {
		TetrisPieceData data = new TetrisPieceData(pieceTemplate, pieceCenter, pieceStartPos);
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(
				new FileOutputStream(TetrisPieceData.FILE_NAME)
			);
			out.writeObject(data);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
