package tetris;

import engine.Coordinates;
import engine.puzzle.Game;
import engine.puzzle.PieceData;

public class Tetris extends Game {

	public static final PieceData TETRIS_PIECE_DATA = new PieceData() {
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
	};
	

	public Tetris() {
		super(TETRIS_PIECE_DATA);
	}

	public static void main(String argv[]) {
		Tetris.profile = new Profile(Profile.option1);
		@SuppressWarnings("unused")
		Game tetrisGame = new Tetris();

		System.exit(0);
	}
}
