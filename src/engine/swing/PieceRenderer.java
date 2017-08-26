package engine.swing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Collection;

import engine.Coordinates;
import engine.Part;
import engine.puzzle.Block;
import engine.puzzle.Piece;

@SuppressWarnings("serial")
public class PieceRenderer extends ImageRenderer {
	//private BufferedImage images[];
	//Piece piece;
	
	private final Collection<BlockRenderer> blockRenderers;
	private final int blockWidth;
	private final int blockHeight;
	
	/*
	private final void drawBlocks() {
		for (BlockRenderer currBlockRend: blockRenderers) {
			drawImageOnMe(currBlockRend,
						  (currBlock.pos.x - piece.pos.x) * ((BlockRenderer)currBlock.getRenderer()).getDisplayWidth(),
						  (currBlock.pos.y - piece.pos.y) * (currBlock.getRenderer()).getDisplayHeight());
		}
	
	}
	*/
	
	public PieceRenderer(Part<?> myPiece) {
/* 		System.out.println("PieceRenderer"); */
		BlockRenderer bRend;
		for (Block pieceBlock : ((Piece)myPiece).getBlocks()) {
			bRend = (BlockRenderer)pieceBlock.getRenderer();
			blockRenderers.add(bRend);
		}
		
		blockWidth = bRend.getWidth();
		blockHeight = bRend.getHeight();
		
		this.setOpaque(false);
		this.setBackground(Color.GREEN);  // for testing
		this.setLayout(null);
		//this.setDoubleBuffered(true);
		this.setFocusable(false);
		
		// get width/height from difference of edge coordinates by adding 1
		newImage(((Piece)myPiece).getWidth() * blockWidth,
				 ((Piece)myPiece).getHeight() * blockHeight);
		
		drawBlocks(piece.getBlocks());
	}
	
	@Override
	public void update(Coordinates partPosition) {
		//System.out.println("Update PieceRenderer");
		this.setBounds(partPosition.x * blockWidth, 
					   partPosition.y * blockHeight,
					   getWidth(),
					   getHeight());
		
		drawBlocks(piece.getBlocks());
	}
}
