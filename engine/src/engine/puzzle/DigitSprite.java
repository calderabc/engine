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

package engine.puzzle;

import engine.*;
import engine.graphics2d.Sprite;

public class DigitSprite extends Sprite {
	public DigitSprite(Part part) {
		// TODO: Come up with a better pass through mechanism.
		super(part, null);
	}

	public DigitSprite(Visual other) {
		super(other);
	}

	@Override
	public void update(Part part) {
		super.update(part);
		currImage = ((Digit)part).get();
	}
}
