package engine;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

public abstract class Screen {
	final String visualName;

	protected final List<Part> visualParts = new Vector<>();


	public final void addParts(Collection<? extends Part> parts) {
		synchronized(visualParts) {
			for (Part part : parts) {
				addPart(part);
			}
		}
	}

	public void addPart(Part part) {
		synchronized(visualParts) {
			visualParts.add(part);
		}
	}

	public void removePart(Part terminalPart) {
		synchronized(visualParts) {
			visualParts.remove(terminalPart);
		}
	}

	protected Screen(String visualName) {
		this.visualName = visualName;
	}

	public final void newVisual(Part part) {
		part.visual = Reflection.newVisual(part);
	}

	public final void initVisuals() {
		for (Part part : visualParts) {
			newVisual(part);
		}
	};

	public abstract void update();
}
