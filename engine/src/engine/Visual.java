package engine;

public abstract class Visual {
	public static final class Id {
		private static byte unique = 0;

		public static byte getUnique() {
			synchronized(Id.class) {
				return ++unique;
			}
		}

		private final int datum; 
		public Id(byte... values) {
			int newDatum = 0;
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

	public abstract void update(Part part);
	public abstract void rotate(int offset);
}
