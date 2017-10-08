package engine;

import java.lang.reflect.InvocationTargetException;

public final class Reflection {
	// Don't instantiate me!
	private Reflection() {}

	private static String[] getCanonicalNames(String a, String b, String c) {
		String[] canons = new String[2];
		// Different places to search for the class.
		canons[0] = "engine."
		            + a.toLowerCase() + "."
		            + b.toLowerCase() + "."
		            + b + c;
		canons[1] = "engine."
		            + a.toLowerCase() + "."
		            + c;
		return canons;
	}

	private static Class<?> getClass(String a, String b, String c) {
		String[] canonicalNames = getCanonicalNames(a, b, c);
		// Return the first class in canonicalNames which exists.
		for (String canonicalName : canonicalNames)
			try {
				return Class.forName(canonicalName);
			} catch (ClassNotFoundException e) { }// Continue loop on exception.

		return null; // If canonicalNames is empty or class cannot be found.
	}

	private static Class<?> getClass(String a, String b, String c, String d) {
		return getClass(a, b, c + d);
	}

	private static Object newInstance(String a, String b, String c) {
		try {
			return getClass(a, b, c).getDeclaredConstructor().newInstance();
		} catch (InstantiationException | InvocationTargetException
		         | IllegalAccessException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null; // If there is an exception.
	}

	private static Object newInstance(String a, String b, String c, String d) {
		return newInstance(a, b, c + d);
	}

	// Locate and call the object's copy constructor.
	static Object newInstance(Object object) {
		// This is to allow for Parts with a null visual.
		if (object == null) return null;
		try {
			return object.getClass().getConstructor(object.getClass()).newInstance(object);
		} catch (InstantiationException | IllegalAccessException
		         | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;  // If there is an exception.
	}

	static Visual newVisual(Game game, Part part, Visual.Id id) {
		try {
			getClass(game.gameTypeName,
				     game.gameName,
			         part.getClass().getSimpleName(),
			         game.screen.visualName)
				.getConstructor(Visual.Id.class)
				.newInstance(id);
		} catch (InstantiationException | IllegalAccessException
		         | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null; // If there is an exception.
	}

	static Screen newScreen(Game game) {
		return (Screen)newInstance(game.engineTypeName, game.engineName, "screen");
	}

	public static void instantiateGameField(Game game, String fieldClassName) {
		try {                  // Holy Reflection Batman!
			game.getClass()
			    .getField(fieldClassName.toLowerCase())
			    .set( game, newInstance(game.gameTypeName,
			                            game.gameName,
			                            fieldClassName) );
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
}

