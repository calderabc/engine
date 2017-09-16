package engine;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;


public final class FileIO {
	private FileIO() {};

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