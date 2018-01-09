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

package engine.graphics2d.swing;

import java.awt.*;
import java.io.File;

import engine.Coordinates;
import engine.graphics2d.ImageType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public final class SwingImageType extends ImageType {
	public SwingImageType(String configFileName,
	                      String defaultConfigFileName,
	                      String partLabel) {
		super(configFileName,
		      defaultConfigFileName,
		      partLabel,
		      BufferedImage.class);
	}

	@Override
	protected BufferedImage loadImageFromFile(String fileName) {
		try {
			return ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null; // In case of exception.
	}

	@Override
	protected BufferedImage getSubimage(Object image,
	                                    Coordinates position,
	                                    Coordinates dimensions) {
		return ((BufferedImage)image).getSubimage(position.x, position.y,
		                              dimensions.x, dimensions.y);
	}

	@Override
	public BufferedImage getScaledImage(Object image, Coordinates newDimensions) {
		BufferedImage newImage = new BufferedImage(newDimensions.x,
		                                           newDimensions.y,
		                                           ((BufferedImage) image).getType());
		newImage.createGraphics().drawImage((Image)image, 0, 0,
		                                    newDimensions.x, newDimensions.y,
		                                    null);
		return newImage;
	}

	@Override
	protected Coordinates imageSize(Object image) {
		return new Coordinates( ((BufferedImage)image).getWidth(),
		                        ((BufferedImage)image).getHeight() );
	}
}
