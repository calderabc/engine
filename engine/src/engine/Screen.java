package engine;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.function.Supplier;

public abstract class Screen {
	final String visualName;

	protected final List<Part> visualParts = new Vector<>();


	public void addPart(Part part) {
		synchronized(visualParts) {
			visualParts.add(part);
		}
	}

	public final void addParts(Iterable<? extends Part> parts) {
		for (Part part : parts) {
			addPart(part);
		}
	}

	public final void addParts(Part... parts) {
		for (Part part : parts) {
			addPart(part);
		}
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
