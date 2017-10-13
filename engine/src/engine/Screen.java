package engine;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

public abstract class Screen {
	final String visualName;

	protected final List<Visual> visuals = new Vector<>();

	public final void addParts(Collection<? extends Part> parts) {
		synchronized(visuals) {
			for (Part part : parts) {
				addPart(part);
			}
		}
	}

	public void addPart(Part part) {
		synchronized(visuals) {
			visuals.add(part.visual);
		}
	}

	public void removePart(Part terminalPart) {
		synchronized(visuals) {
			visuals.remove(terminalPart.visual);
		}
	}

	protected Screen(String visualName) {
		this.visualName = visualName;
	}

	public abstract void update();
	public abstract void setScale(Field field, Visual Visual);
}
