package engine;

import java.util.Collection;

public interface Screen {
	void update();
	void addParts(Collection<? extends Part> parts);
}
