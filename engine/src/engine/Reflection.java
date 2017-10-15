package engine;

import java.lang.reflect.InvocationTargetException;

public final class Reflection {
	// Don't instantiate me!
	private Reflection() {}


	public static class ClassAndObject {
		final Class<?> objectClass;
		final Object object;
		public ClassAndObject(Class<?> newObjectClass, Object newObject) {
			objectClass = newObjectClass;
			object = newObject;
		}
		public ClassAndObject(Object newObject) {
			this(newObject.getClass(), newObject);
		}
	}

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
		// Result the first class in canonicalNames which exists.
		for (String canonicalName : canonicalNames)
			try {
				return Class.forName(canonicalName);
			} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			}// Continue loop on exception.

		return null; // If canonicalNames is empty or class cannot be found.
	}


	private static Class<?> getClass(String... strings) {
		switch (strings.length) {
			case 4 : strings[2] += strings[3];
			case 3 : return getClass(strings[0], strings[1], strings[2]);
			default : assert false : "Error: Invalid arguments for getClass().";
		}
		return null;
	}


	private static Object newInstance(String... strings) {
		try {
			return getClass(strings).getDeclaredConstructor().newInstance();
		} catch (InstantiationException | InvocationTargetException
		         | IllegalAccessException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null; // If there is an exception.
	}

	private static Object newInstance(Class<?> objectClass, Object... parameters) {
		Class<?>[] parameterClasses = new Class<?>[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			parameterClasses[i] = parameters[i].getClass();
		}
		try {
			return objectClass.getConstructor(parameterClasses).newInstance(parameters);
		} catch (InstantiationException | IllegalAccessException
		         | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static Object newInstance(Class<?> objectClass, ClassAndObject... parameterMeta) {
		Class<?>[] parameterClasses = new Class<?>[parameterMeta.length];
		Object[] parameters = new Object[parameterMeta.length];
		for (int i = 0; i < parameters.length; i++) {
			parameterClasses[i] = parameterMeta[i].objectClass;
			parameters[i] = parameterMeta[i].object;
		}
		try {
			return objectClass.getConstructor(parameterClasses).newInstance(parameters);
		} catch (InstantiationException | IllegalAccessException
		         | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object newInstance(String[] strings, ClassAndObject... parameterMeta) {
		return newInstance(getClass(strings), parameterMeta);
	}

	static Object newInstance(Object object, ClassAndObject... parameterMeta) {
		return newInstance(object.getClass(), parameterMeta);
	}

	private static Object newInstance(Object object, Object... parameters) {
		return newInstance(object.getClass(), parameters);
	}

	// Locate and call the object's copy constructor.
	static Object newInstance(Object object) {
		// This is to allow for Parts with a null visual.
		if (object == null)
			return null;
		return newInstance(object, object);
	}

	static Visual newVisual(Part part) {
		return (Visual)newInstance( getClass(part.game.gameTypeName,
		                                     part.game.gameName,
                                             part.getClass().getSimpleName(),
                                             part.game.screen.visualName),
		                            new ClassAndObject(Part.class, part) );
	}


	public static Screen newScreen(Game game) {
		return (Screen)newInstance( getClass(game.engineTypeName,
		                                     game.engineName,
		                                     "Screen"),
		                             new ClassAndObject(Game.class, game) );
		}


	// TODO: Name this more precisely.
	public static void instantiateGameField(Game game, String fieldClassName) {
		try {                  // Holy Reflection Batman!
			game.getClass()
			    .getField(fieldClassName.toLowerCase())
			    .set(game,
			         newInstance( getClass(game.gameTypeName,
			                               game.gameName,
			                               fieldClassName),
			                      new ClassAndObject(Game.class, game) )
			    );
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	public static Object newGameField(Game game, String fieldClassName) {
		return newInstance(game.gameTypeName,
						   game.gameName,
						   fieldClassName);
	}

}

