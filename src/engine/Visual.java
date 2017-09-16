package engine;

public abstract class Visual {
	public static final class Id {
		private final int datum; 
		public Id(byte... values) {
			int newDatum = 0;
			for(byte value : values) {
				newDatum = (newDatum << 8) + value;
			}
			datum = newDatum;
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

	public abstract void update(MovablePart part);
	public abstract void rotate(int offset);
}
