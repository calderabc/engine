package engine.puzzle;

import engine.Coordinates;

public class PieceData {
	// TODO: Make this load from a file instead of hard coding.
		@SuppressWarnings("unused")
		public final int[][][] pieceTemplate =
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
		@SuppressWarnings("unused")
		public final int[][] pieceCenter = {{1, 1, 1, 1},
		                                    {2, 0, 2, 0},
		                                    {2, 0, 2, 0}, 
		                                    {2, 0, 2, 0}, 
		                                    {3, 0, 4,-1},
		                                    {2, 1, 3, 0},
		                                    {2, 1, 3, 0}};

		public final Coordinates pieceStartPos = new Coordinates(3, 0);
	
	public final Coordinates getCurrCenter(int pieceID) {
		return new Coordinates(pieceCenter[pieceID][0], pieceCenter[pieceID][1]);
	}

	public final Coordinates getDestCenter(int pieceID) {
		return new Coordinates(pieceCenter[pieceID][2], pieceCenter[pieceID][3]);
	}
}
