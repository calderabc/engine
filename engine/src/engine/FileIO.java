package engine;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;


public final class FileIO {
	private FileIO() {}

	public static final class GameProperties extends Properties {
		private void load(String fileName) {
			try {
				load(new FileInputStream(fileName + ".ini"));
				storeToXML(new FileOutputStream(fileName + ".xml"), null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public GameProperties(String fileName) {
			load(fileName);
		}

		public GameProperties(String fileName, Properties defaultProperties) {
			super(defaultProperties);
			load(fileName);
		}

		private String[] split(String string) {
			String[] returnArray = string.split(",");
			for (int i = 0; i < returnArray.length; i++) {
				returnArray[i] = returnArray[i].trim();
			}
			return returnArray;
		}

		public String[] getArray(String key) {
			return split(getProperty(key));
		}

		public int[] getArrayInteger(String key) {
			String property = getProperty(key);
			if (property == null)
				return null;
			String[] stringArray = split(getProperty(key));
			int[] intArray = new int[stringArray.length];
			for (int i = 0; i < stringArray.length; i++) {
				intArray[i] = Integer.valueOf(stringArray[i]);
			}
			return intArray;
		}

		public Coordinates getCoordinates(String key) {
			try {
				int[] intArray = getArrayInteger(key);
				if (intArray == null)
					return null;
				return new Coordinates(intArray);
			} catch (NullPointerException e) {
				return null;
			}
		}

		public String[] getArrayLowerCase(String key) {
			String property = getProperty(key);
			if (property == null)
				return null;
			return split(property.toLowerCase());
		}

		public String[] getArrayUpperCase(String key) {
			String property = getProperty(key);
			if (property == null)
				return null;
			return split(property.toUpperCase());
		}
	}

	// Wonder why the Java designers didn't just build these methods in.
	// Boilerplate code for reading/writing serializable objects from/to a file.
	public static Object load(String fileName) {
		Object obj = null;
		try {
			ObjectInputStream in = new ObjectInputStream(
				new FileInputStream(fileName)
			);
			obj = in.readObject();
			in.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static void save(String fileName, Object obj) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(
				new FileOutputStream(fileName)
			);
			out.writeObject(obj);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static Object[] load(String fileName, short numOfObjects) {
		Object[] objs = new Object[numOfObjects];

		try {
			ObjectInputStream in = new ObjectInputStream(
				new FileInputStream(fileName)
			);
			for (int i = 0; i < numOfObjects; i++) {
				objs[i] = in.readObject();
			}
			in.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return objs;
	}

	public static void save(String fileName, Object... objs) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(
				new FileOutputStream(fileName)
			);
			for (Object obj : objs) {
				out.writeObject(obj);
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}