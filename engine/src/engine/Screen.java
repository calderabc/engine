package engine;

import java.util.Collection;

public abstract class Screen {
	public String visualName;

	public abstract void update();
	public abstract void addParts(Collection<? extends Part> parts);
	public abstract void addPart(Part part);
	public abstract void removePart(Part terminalPart);

	protected Screen(String visualName) {
		this.visualName = visualName;
	}

	public abstract void setScale(Field field, Visual Visual);
}
