package engine.puzzle;

import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import engine.Part;


public class Number extends Part {
	private long value;
	private int size;
	private final long max;
	private List<Digit> digits = new Vector<Digit>();
	
	public Number(int newX, int newY, int newSize, int newValue) {
		size = newSize;
		value = newValue;
		
		max = (long) Math.pow((double) size, 10) - 1;
		
		//children = new Vector<Part>();
		
		for (int i = 0; i < size; i++) {
			digits.add(new Digit(i, 0, 0));
		}
		
		assignDigits();
	}
	
	public final long add(long offset) {
		value += offset;
		
		assignDigits();
		
		return value;
	}
	
	private final void assignDigits() {
		if (value <= max && value >= 0 && !digits.isEmpty() ) {
			
			ListIterator<Digit> iterator = 
				digits.listIterator(digits.size());
			
			int lastDigitValue;
			for (long quotient = value; 
				 quotient > 0; 
				 quotient = (quotient - lastDigitValue) / 10
			) {
				
				lastDigitValue = (int) quotient % 10;
				
				((Digit) iterator.previous()).setValue(lastDigitValue);
			}
		}
	}
	
	
	public long getValue() {
		return value;
	}
	
}
