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

package engine.graphics2d;

import engine.*;
import engine.Symbol;
import engine.puzzle.Block;
import engine.puzzle.PuzzleGame;

import java.util.Arrays;

public class Sprite extends Visual {
	protected Object[] images;
	public Coordinates position;
	protected int currImage;

	protected Coordinates origin = Coordinates.ORIGIN;

	private final Coordinates dimensions;
	private final Coordinates positionScaleFactor;


	public Sprite(Part newPart, String label) {
		super(newPart);

		Game game = part.game;

		// TODO: Improve this.
		Coordinates gridDimensions = (part instanceof Block)
		                             ? ((PuzzleGame)part.game).board.dimensions
		                             : game.getScoreBoardDimensions();

		// TODO: This is an inaccurate hack.  Find better solution.
		if (part instanceof Symbol) {
			int x = ((Graphics2dScreen)part.game.screen).dimensions.x / 2;
			origin = new Coordinates(x);
		}

		ImageType imageType = ImageType.newImageType(part, label);


		dimensions = imageType.dimensions.clone();

		Object[] sourceImages = ImageType.imageListMap.get(part.visualId);

		// If position scale factor is not given set it to the same dimensions
		// large enough to hold any image in the image array.
		positionScaleFactor = (imageType.translationFactor == null)
			? Coordinates.max(
				(Coordinates[])Arrays.stream(sourceImages)
									 .parallel()
									 .map(imageType::imageSize)
				                     .toArray()
			)
			: imageType.translationFactor.clone();

		// Scale images and positioning of images to match screens size.
		// Note 'positionScaleFactor' and 'dimensions' will be resized
		// (scaled) by the actions of this method.
		images = imageType.scaleImageArray(
			sourceImages,
			positionScaleFactor,
			dimensions,
			((Graphics2dScreen)part.game.screen).dimensions,
			gridDimensions
		);

		update(part);
		currImage = 0;
	}

	protected Sprite(Visual other) {
		super(other);
		images = ((Sprite)other).images;
		position = ((Sprite)other).position;
		dimensions = ((Sprite)other).dimensions;
		positionScaleFactor = ((Sprite)other).positionScaleFactor;
		origin = ((Sprite)other).origin;
		currImage = ((Sprite)other).currImage;
	}


	@Override
	public void update(Part part) {
		position = Coordinates.multiply(part.pos, positionScaleFactor).move(origin);
	}

	@Override
	public void rotate(int offset) {
		// TODO: For now the default sprite rotation does nothing.
		// Eventually add rotation by degrees functionality.
	}

	public int getHeight() {
		return dimensions.y;
	}

	public Object getCurrImage() {
		if (images == null) {
			images = ImageType.imageListMap.get(part.visualId); // Save memory by always using the same images.
		}
		if (images == null) {
			return null;
		}
		return images[currImage];
	}

}