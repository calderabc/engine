package tetris;

import engine.GameData;

public class TetrisGameData implements GameData {

	private static final GameData tetrisGameData;
	private static final String dataFile = "tetris_data.dat";
	private static final String

	private void TetrisGameData() {
		tetrisGameData
	}

	@Override
	public GameData getGameData() {
		if (tetrisGameData == null) {
			tetrisGameData =  
		}
		return tetrisGameData;
	}

}
