package tetris;

import java.util.EnumMap;
import java.util.EnumSet;

public class Profile {
	private EnumMap<Flag, Option> optionFlags = new EnumMap<Flag, Option>(Flag.class);
	private EnumMap<Flag, Integer> valueFlags = new EnumMap<Flag, Integer>(Flag.class);
	
	private static final EnumSet<Option> DEFAULT = EnumSet.of(Option.SWING, Option.LAND_BLOCKS, Option.REMOVE_BLOCKS);
	public static final EnumSet<Option> option1 = EnumSet.of(Option.LAND_PIECES);
	public static final EnumSet<Option> option2 = EnumSet.of(Option.LAND_BLOCKS);
	
	public Profile() {
		setOptions(DEFAULT);
	} 
	
	public Profile(Iterable<Option> newOptions) {
		this();
		
		setOptions(newOptions);
	}
	
	public void setOptions(Iterable<Option> newOptions) {
		for (Option currOption: newOptions) {
			setOption(currOption);
		}
	}
	
	public void setOption(Option newOption) {
		//System.out.println(newOption.getFlag());
		optionFlags.put(newOption.getFlag(), newOption);
	}
	
	
	public boolean isOptionSet(Option testOption) {
		return testOption == optionFlags.get(testOption.getFlag());
	}
	
	public Option getOption(Flag flag) throws FlagException {
		if (!flag.isOptionFlag()) {
			throw new FlagException();
		}
		return optionFlags.get(flag);
	}
	
	public void setValue(Flag flag, int newValue) throws FlagException {
		if (flag.isOptionFlag()) {
			throw new FlagException();
		}
		
		valueFlags.put(flag, newValue);
	}
	
	public int getValue(Flag flag) throws FlagException {
		if (flag.isOptionFlag()) {
			throw new FlagException();
		}
		
		return valueFlags.get(flag);
	}
}
