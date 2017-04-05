package engine.puzzle;

import java.util.ListIterator;
import java.util.Vector;

import engine.Part;
import engine.StaticPart;


public class Number extends StaticPart<Part<?>> {
	private long value;
	private int size;
	private final long max;
	
	public Number(int newX, int newY, int newSize, int newValue) {
		super(newX, newY);
		size = newSize;
		value = newValue;
		
		max = (long) Math.pow((double) size, 10) - 1;
		
		//children = new Vector<Part>();
		
		for (int i = 0; i < size; i++) {
			this.addChild(new Digit(i, 0, 0));
		}
		
		assignDigits();
	}
	
	public final long add(long offset) {
		value += offset;
		
		assignDigits();
		
		return value;
	}
	
	private final void assignDigits() {
		if (value <= max && value >= 0 && !getChildren().isEmpty() ) {
			
			ListIterator<Digit> iterator = 
				((Vector) getChildren()).listIterator(getChildren().size());
			
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
