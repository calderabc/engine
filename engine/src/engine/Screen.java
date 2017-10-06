package engine;


import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public abstract class Screen {
	public abstract void update();
	public abstract void addParts(Collection<? extends Part> parts);
	public abstract void addPart(Part part);
	public abstract void removePart(Part terminalPart);

	protected abstract Class<?> getVisualClass(Game game, Part part);

	public final Visual newVisual(Game game, Part part, Visual.Id id) {
		try {
			return (Visual)getVisualClass(game, part)
			       .getConstructor(part.getClass(), Visual.Id.class)
			       .newInstance(part, id);
		} catch (InstantiationException | IllegalAccessException
		         | IllegalArgumentException | InvocationTargetException
		         | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public final Visual newVisual(Visual visual) {
		if (visual == null) return null;
		try {
			return visual.getClass().getConstructor(Visual.class).newInstance(visual);
		} catch (InstantiationException | IllegalAccessException
		         | IllegalArgumentException | InvocationTargetException
		         | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
}
