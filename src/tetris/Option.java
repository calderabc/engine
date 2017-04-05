package tetris; 

public enum Option {
	// TODO: Make it so the package names aren't hardcoded like this.
	// It's something that can't be refactored by an IDE if I rename the package names.
	SWING (Flag.DISPLAY, "engine.swing"),
	JAVA3D (Flag.DISPLAY, "engine.java3d"),
	OPENGL (Flag.DISPLAY, "engine.opengl"),
	
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
