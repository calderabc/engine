package tetris; 

public enum Option {
	SWING (Flag.DISPLAY, "tetris.engine.display.swing"),
	JAVA3D (Flag.DISPLAY, "tetris.engine.display.java3d"),
	OPENGL (Flag.DISPLAY, "tetris.engine.display.opengl"),
	
	LAND_BLOCKS (Flag.LAND),
	LAND_PIECES (Flag.LAND),
	
	REMOVE_BLOCKS (Flag.REMOVE),
	REMOVE_PIECES (Flag.REMOVE);
	
	private final Flag flag;
	private final String handle;
	
	private Option(Flag newFlag) {
		this(newFlag, "");
	}
	
	private Option(Flag newFlag, String newHandle) {
		flag = newFlag;
		
		handle = newHandle;
	}
	
	public final Flag getFlag() {
		return flag;
	}
	
	public final String getHandle() {
		return handle;
	}
}
