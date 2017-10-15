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

	// TODO: This could be made more efficient by only trying to add visuals to
	// new parts.  Perhaps a queue for new parts which need visuals.
	public final void initVisuals() {
		for (Part part : visualParts) {
			if (part.visual == null) {
				part.visual = Reflection.newVisual(part);
			}
		}
	};

	public abstract void update();
}
