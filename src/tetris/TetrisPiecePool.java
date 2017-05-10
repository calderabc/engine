package tetris;

import engine.puzzle.PiecePool;

public final class TetrisPiecePool extends PiecePool {
	// TODO: Make this load from a file instead of hard coding.
	public static final int[][][] tetrisPieceTemplate =
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
	
	/* These values are twice their intended values so that floating point half
	 * fractions can be represented as integers. */
	public static final int[][] tetrisPieceCenter = {{1, 1, 1, 1},
	                                                 {2, 0, 2, 0},
	                                                 {2, 0, 2, 0},
	                                                 {2, 0, 2, 0},
	                                                 {3, 0, 4,-1},
	                                                 {2, 1, 3, 0},
	                                                 {2, 1, 3, 0}};

	public TetrisPiecePool() {
		super(tetrisPieceTemplate, tetrisPieceCenter);
	};
}
