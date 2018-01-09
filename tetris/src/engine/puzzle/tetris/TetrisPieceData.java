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

package engine.puzzle.tetris;

import java.io.Serializable;

import engine.Coordinates;
import engine.puzzle.PieceData;

public class TetrisPieceData extends PieceData implements Serializable {
	private static final long serialVersionUID = -9105193668883492424L;

	public static final String FILE_NAME = "tetris_piece.dat";

	public final byte[][][] pieceTemplate;
	public final byte[][] pieceCenter;
	public final Coordinates pieceStartPos;

	
	public TetrisPieceData(byte[][][] newPieceTemplate, 
	                       byte[][] newPieceCenter, 
	                       Coordinates newPieceStartPos) {
		pieceTemplate = newPieceTemplate;
		pieceCenter = newPieceCenter;
		pieceStartPos = newPieceStartPos;
	}

	public final Coordinates getCurrCenter(int pieceID) {
		return new Coordinates(pieceCenter[pieceID][0], 
		                       pieceCenter[pieceID][1]);
	}

	public final Coordinates getDestCenter(int pieceID) {
		return new Coordinates(pieceCenter[pieceID][2], 
		                       pieceCenter[pieceID][3]);
	}
}
