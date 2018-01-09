/*
This is a tile-matching puzzle video game engine.
Copyright (C) 2018 Aaron Calder

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
*/

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
		TetrisPieceData pieceData =
			new TetrisPieceData(pieceTemplate, 
			                    pieceCenter, 
			                    pieceStartPos);

		FileIO.save(TetrisPieceData.FILE_NAME, pieceData);
	}
}
