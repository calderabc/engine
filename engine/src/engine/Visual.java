package engine;

import java.util.Hashtable;
import java.util.Map;

public abstract class Visual {
	public static final class Id {
		private static byte unique = 0;
		// TODO: Memory wasteful.  Work out a better way.
		private static Map<String, Byte> idMap = new Hashtable<>();

		private static byte getUnique(String key) {
			key = key.toLowerCase();
			synchronized(Id.class) {
			//<><> If map has entry return value otherwise return new value.
				Byte value = idMap.get(key);
				if (value != null) {
					return value;
				}
				value = ++unique;
				idMap.put(key, value);
				return value;
				//<><>
			}
		}

		private final int datum; 
		public Id(String keyString, byte... values) {
			int newDatum = getUnique(keyString);
			for(byte value : values) {
				newDatum = (newDatum << 8) + value;
			}
			datum = newDatum;
		}

		public Id(Id id, byte append) {
			datum = (id.datum << 8) + append;
		}

		@Override 
		public boolean equals(Object obj) {
			return datum == ((Id)obj).datum;
		}
		
		@Override
		public int hashCode() {
			return ((Integer)datum).hashCode();
		}
	}

	protected Part part;

	protected Visual(Part newPart) {
		part = newPart;
	}

	protected Visual(Visual other) {
		part = other.part;
	}

	public abstract void update(Part part);
	public abstract void rotate(int offset);
}
