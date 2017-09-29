package engine.columns;

import engine.puzzle.SimpleBoard;

public class ColumnsBoard extends SimpleBoard {

	public ColumnsBoard() {
		super(6, 13);
	}

	@Override
	public int tryRemoveBlocks() {
		return 0;
	}

}
