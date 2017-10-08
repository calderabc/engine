package engine.puzzle.columns;

import java.util.Random;

import engine.Coordinates;
import engine.Visual;
import engine.puzzle.Block;
import engine.puzzle.Piece;

public class ColumnsPiece extends Piece {
	
	public ColumnsPiece() {
		this(3, 6);
	}

	private ColumnsPiece(int newBlockCount, int typeCount) {
		super(newBlockCount);
		for(int i = 0; i < newBlockCount; i++) {
			byte type = (byte) new Random().nextInt(typeCount);
			blocks[i] = new Block(new Coordinates(3, i),
			                      new Visual.Id((byte)3, type), 
			                      type);
		}
	}
	
	public ColumnsPiece(ColumnsPiece other) {
		super(other);
	}
	
	@Override
	protected Piece rotate(Coordinates offset) {
		int length = blocks.length;
		int shift = offset.x;

		int topPosition = 40000;
		for (int i = 0; i < length; i++) {
			int blockPos = blocks[i].pos.y;
			if (blockPos < topPosition) topPosition = blockPos;
		}

		// TODO: There's got to be a simpler way.  Work on it.
		for (int i = 0; i < length; i++) {
			int ordinal = blocks[i].pos.y - topPosition + shift;
			if (ordinal < 0) {
				ordinal += length;
			} else if (ordinal >= length) {
				ordinal -= length;
			}

			blocks[i].pos.y = topPosition + ordinal;
		}

		return this;
	}

}
