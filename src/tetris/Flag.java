package tetris;

public enum Flag {
	LAND (false), 
	REMOVE (false), 
	DISPLAY (false),
	BLOCKS_PER_PIECE (true),
	BOARD_WIDTH (true), 
	BOARD_HEIGHT (true);
	
	private boolean isValueFlag;
	private int value;
	
	private Flag (boolean newIsOptionFlag) { 
		isValueFlag = newIsOptionFlag;
	}
	
	public boolean isValueFlag() {
		return isValueFlag;
	}
	
	public boolean isOptionFlag() {
		return !isValueFlag;
	}
	
}
