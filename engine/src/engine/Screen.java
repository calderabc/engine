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

import java.util.List;
import java.util.Vector;
import java.util.function.Supplier;

public abstract class Screen {
	final String visualName;
	// TODO: Is Vector right for the job?
	protected final List<Part> visualParts = new Vector<>();

	public void addPart(Part part) {
		synchronized(visualParts) {
			visualParts.add(part);
		}
	}

	public final void addParts(Iterable<? extends Part> parts) {
		parts.forEach(this::addPart);
	}

	public final void addParts(Part... parts) {
		for (Part part : parts) {
			addPart(part);
		}
	}

	public List<Part> getParts() {
		return visualParts;
	}

	public final void addParts(Supplier<Part[]> partSupplier) {
		addParts(partSupplier.get());
	}

	public void removePart(Part terminalPart) {
		synchronized(visualParts) {
			visualParts.remove(terminalPart);
		}
	}

	protected Screen(String visualName) {
		this.visualName = visualName;
	}

	private void initVisual(Part part) {
		part.visual = Reflection.newVisual(part);
	}

	// TODO: This could be made more efficient by only trying to add visuals to
	// new parts.  Perhaps a queue for new parts which need visuals.
	public final void initVisuals() {
		// TODO: Determine if this is faster/slower than traditional foreach loop.
		synchronized(visualParts) {
			visualParts.parallelStream()
			           .filter(part -> part.visual == null)
			           .forEach(this::initVisual);

		}
	};

	protected final void reloadVisuals() {
		synchronized (visualParts) {
			visualParts.forEach(this::initVisual);
		}
	}

	public abstract void update();
}
