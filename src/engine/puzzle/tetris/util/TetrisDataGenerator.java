package engine.puzzle.tetris.util;


import engine.Coordinates;
import engine.FileIO;
import engine.puzzle.tetris.TetrisPieceData;

public class TetrisDataGenerator {

	public static final byte[][][] pieceTemplate =
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
	public static final byte[][] pieceCenter = {{1, 1, 1, 1},
	                                           {2, 0, 2, 0},
	                                           {2, 0, 2, 0}, 
	                                           {2, 0, 2, 0}, 
	                                           {3, 0, 4,-1},
	                                           {2, 1, 3, 0},
	                                           {2, 1, 3, 0}};

	public static final Coordinates pieceStartPos = new Coordinates(3, 0);


	public static void main(String argv[]) {
		TetrisPieceData pieceData = new TetrisPieceData(pieceTemplate, pieceCenter, pieceStartPos);

		FileIO.save(TetrisPieceData.FILE_NAME, pieceData);
	}
}
