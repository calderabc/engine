package engine.columns;

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
			blocks[i] = new Block(new Coordinates(4, i),
			                      new Visual.Id((byte)3, type), 
			                      type);
		}
	}
	
	public ColumnsPiece(ColumnsPiece other) {
		super(other);
	}
	
	@Override
	protected Piece rotate(Coordinates offset) {
		// This would be easier with a Vector.
		// TODO: Is it worth the overhead of a Vector?
		int length = blocks.length;
		int shift = offset.x();
		if (shift == -1) {
			shift += length;
		}
		Block[] shiftedBlocks = new Block[length];
		int j = shift;
		for (int i = 0; i < length; i++) {
			if (j >= length) {
				j -= length;
			}
			shiftedBlocks[i] = blocks[j++];
		}
		for (int i = 0; i < length; i++) {
			blocks[i] = shiftedBlocks[i];
		}
		
		return this;
	}

}
