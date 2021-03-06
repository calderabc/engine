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

package engine;


public abstract class Game {
	public final String gameName;
	public String gameTypeName;
	public String engineName;
	public String engineTypeName;

	private Coordinates scoreBoardDimensions;

	public Screen screen;

	protected Game(String gameName,
	               String gameTypeName,
	               String engineName,
	               String engineTypeName) {
		this.gameName = gameName;
		this.gameTypeName = gameTypeName;
		this.engineName = engineName;
		this.engineTypeName = engineTypeName;

	}


	public Coordinates getScoreBoardDimensions() {
		return scoreBoardDimensions;
	}

	protected void setScoreBoardDimensions(Iterable<Part> parts) {
		int maxX = 0;
		int maxY = 0;
		for (Part part : parts) {
			if (part instanceof Symbol) {
				if (part.pos.x > maxX) maxX = part.pos.x;
				if (part.pos.y > maxY) maxY = part.pos.y;
			}
		}
		// +1 to convert from max value to width/height.
		scoreBoardDimensions = new Coordinates(maxX + 1, maxY + 1);
	}
}
