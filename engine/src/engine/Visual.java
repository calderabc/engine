/*
This is a tile-matching puzzle video game engine.
Copyright (C) 2018 Aaron Calder

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
*/

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
