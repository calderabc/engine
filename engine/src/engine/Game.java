package engine;


public abstract class Game {
	public final String gameName;
	public String gameTypeName;
	public String engineName;
	public String engineTypeName;

	private Coordinates scoreBoardDimensions;

	public Screen screen;

	protected Game(String gameName,
	               String gameTypeName,
	               String engineName,
	               String engineTypeName) {
		this.gameName = gameName;
		this.gameTypeName = gameTypeName;
		this.engineName = engineName;
		this.engineTypeName = engineTypeName;

	}


	public Coordinates getScoreBoardDimensions() {
		return scoreBoardDimensions;
	}

	protected void setScoreBoardDimensions(Iterable<Part> parts) {
		int maxX = 0;
		int maxY = 0;
		for (Part part : parts) {
			if (part instanceof Symbol) {
				if (part.pos.x > maxX) maxX = part.pos.x;
				if (part.pos.y > maxY) maxY = part.pos.y;
			}
		}
		// +1 to convert from max value to width/height.
		scoreBoardDimensions = new Coordinates(maxX + 1, maxY + 1);
	}
}
