package tetris.engine.display.swing;

import java.awt.Color;
import java.awt.Component;

import tetris.engine.display.Renderer;
import tetris.parts.Board;
import tetris.parts.Part;

@SuppressWarnings("serial")
public class BoardRenderer extends Renderer {
	private Board board;
	
	
	public static int padding = 10;
	public static int verticalPadding;
	
	public static int width;
	public static int height;
	
	public BoardRenderer(Part<?> newBoard) {
		//System.out.println("BoardRenderer");
		board = (Board) newBoard;
		
		this.setOpaque(true);
		this.setBackground(Color.DARK_GRAY);
		
		this.setLayout(null);
		//this.setMinimumSize(new Dimension(board.getWidth() * BlockRenderer.BLOCK_WIDTH,
							  			  //board.getHeight() * BlockRenderer.BLOCK_HEIGHT));
		//this.setMaximumSize(new Dimension(board.getWidth() * BlockRenderer.BLOCK_WIDTH,
							  			  //board.getHeight() * BlockRenderer.BLOCK_HEIGHT));
		//this.setSize(board.getWidth() * BlockRenderer.BLOCK_WIDTH - 30,
					 //board.getHeight() * BlockRenderer.BLOCK_HEIGHT);			
		
		height = BlockRenderer.getDisplayHeight() * board.getHeight();
		width = BlockRenderer.getDisplayWidth() * board.getWidth(); 
		
		
		
		
		this.setBounds(padding, padding, width, height); 
					 
		//this.setDoubleBuffered(true);
	}
	

	
	@Override
	public void update() {
		System.out.println("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
		height = this.getParent().getHeight() - 2 * padding;
		
		
		BlockRenderer.setDisplayHeight((int) (((double) height) / board.getHeight()));
		BlockRenderer.setDisplayWidth(BlockRenderer.getDisplayHeight());
		
		int adjustedHeight = BlockRenderer.getDisplayHeight() * board.getHeight();
		
		verticalPadding = (height - adjustedHeight) / 2;
		height = adjustedHeight;
		
		
		
		
		width = board.getWidth() * BlockRenderer.getDisplayWidth(); 
		
		this.setBounds(padding, padding + verticalPadding, width, height); 
		
		for (Component currRenderer: this.getComponents()) {
			if (currRenderer instanceof Renderer) {
				((Renderer) currRenderer).update();
			}
		}
		
		super.update();
	}
}
