package engine.swing;

import java.awt.Color;
import java.awt.image.BufferedImage;

import engine.Part;
import engine.puzzle.Block;
import engine.puzzle.Piece;

@SuppressWarnings("serial")
public class PieceRenderer extends ImageRenderer {
	private BufferedImage images[];
	
	Piece piece;
	
	private final void loadImages(Iterable<Block> blocks) {
		for (Block currBlock: blocks) {
			drawImageOnMe(((BlockRenderer) currBlock.getRenderer()), currBlock.pos.x, currBlock.pos.y);
		}
	
	}
	
	
	public PieceRenderer(Part<?> newPiece) {
/* 		System.out.println("PieceRenderer"); */
		piece = (Piece) newPiece;
		
		this.setOpaque(false);
		this.setBackground(Color.GREEN);  // for testing
		this.setLayout(null);
		//this.setDoubleBuffered(true);
	
		this.setFocusable(false);
		//update();
		
		
		
		//this.setBounds(0, 0, 2000, 2000);
	}
	
	@Override 
	public void init() {
		
		// get width/height from difference of edge coordinates by adding 1
		newImage(piece.getWidth() * BlockRenderer.getDisplayWidth(),
				 piece.getHeight() * BlockRenderer.getDisplayHeight());
		
		for (Block currBlock: piece.getBlocks()) {
			drawImageOnMe(((BlockRenderer) currBlock.getRenderer()),
						  (currBlock.pos.x - piece.pos.x) * BlockRenderer.getDisplayWidth(),
						  (currBlock.pos.y - piece.pos.y) * BlockRenderer.getDisplayHeight());
		}
		
	}
	
	@Override
	public void update() {
		//System.out.println("Update PieceRenderer");
		
		this.setBounds(piece.pos.x * BlockRenderer.getDisplayWidth(), 
					   piece.pos.y * BlockRenderer.getDisplayHeight(),
					   getWidth(),
					   getHeight());
		
	}
}
