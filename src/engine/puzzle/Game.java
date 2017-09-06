package engine.puzzle;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import engine.Coordinates;
import engine.GraphicsEngine;
import engine.Screen;
import engine.puzzle.tetris.ScoreCalculator;
import engine.swing.Swing;

public class Game {
	static public final PieceData TETRIS_PIECE_DATA = new PieceData();

	static public Game me;

	public GraphicsEngine engine;
	private Screen screen;
	private Board board;
	private Piece piece;
	private Score score;
	
	Level level;
	 
	private boolean isPieceLanded;
	
	private ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(2);


	public static void main(String argv[]) {
		me = new Game(TETRIS_PIECE_DATA);
		me.run();

		System.exit(0);
	}
	
	public Game(PieceData pieceData) {
		board = new Board(pieceData);
	}
	
	public void run() {
		scheduler.setRemoveOnCancelPolicy(true);
		
		
		// TODO: Here I would choose between graphical engines.
		engine = new Swing(this);

		//PieceAction.FALL.setSpeed(2 / 1.0e9);
		
		screen = engine.newScreen(this);

		score = new Score(ScoreCalculator.NINTENDO, 10, 0);
		level = new Level(1);
		
		
		piece = board.startNewPiece();
		while (board.doesPieceFit(piece)) {		
			screen.addParts(piece.getBlocks());
			screen.update();
					System.out.println("*****asl;dkfja;lsdkf ending here ********** alkfl;askjdf;l");

			isPieceLanded = false;
			startFalling();
			
					System.out.println("****************************************");
			
			// TODO: Make this thread (main) stop and wait for an interrupt.
			// Polling isPieceLanded wastes CPU processing time. Interrupt, or signal 
			// of some sort from the thread which lands the piece would be better.
			do {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (!isPieceLanded);

			stopFalling();

			piece = board.startNewPiece();
		}
				
	}

	boolean tryToMovePiece(PieceAction action) {
		synchronized(piece) {
			if (!isPieceLanded) {
				/** 
				 * TODO:  Add code to slide the piece left or right if there is room
				 * for the rotated piece to fit but the act of rotating would cause the right
				 * or left edge of the piece to overlap the edges or other blocks.
				 * For instance if the 4 straight long piece is vertical and on either right
				 * or left edge it should still be able to rotate.  Simple rotation would put
				 * the piece over the edge once horizontal so shifting the piece will allow it
				 * to rotate.  If a piece is rotated this way rotating back should return
				 * the piece to its original position.  Perhaps add as an optional feature
				 * if not present in games people are used to playing.
				 */			
				Piece testPiece =  new Piece(piece).move(action);
				
				if (board.doesPieceFit(testPiece)) {
					piece.move(action);

					piece.updateVisual();
					screen.update();

					return true;
				}
			}
		}
		return false;
	}
	
	private void landPiece() {
		synchronized(piece) {
			if (!isPieceLanded) {
				board.landPiece(piece);
				
				PieceAction.resetAll();
				
				isPieceLanded = true;
			}
		}
	}
	
	private void startFalling() {
		synchronized(piece) {
			System.out.println(SwingUtilities.isEventDispatchThread());
			if (!PieceAction.FALL.isMoving) {
				PieceAction.FALL.future = 
					scheduler.scheduleAtFixedRate(makePieceFall, 
												  PieceAction.FALL.getDelay(), 
												  PieceAction.FALL.getDelay(), 
												  TimeUnit.NANOSECONDS);
				PieceAction.FALL.isMoving = true;
			}
		}
	}
	
	public void startMovingDown() {
		// Run by swing event dispatch thread.
		synchronized(piece) {
			stopFalling();
			if (!PieceAction.DOWN.isPressed) {
				PieceAction.DOWN.isPressed = true;
				
				if (!PieceAction.DOWN.isMoving) {
					stopFalling();
				
					PieceAction.DOWN.future = 
						scheduler.scheduleAtFixedRate(PieceAction.DOWN.runner, 
													  0, 
													  PieceAction.DOWN.getDelay(), 
													  TimeUnit.NANOSECONDS);
					PieceAction.DOWN.isMoving = true;
				}
			}
		}
	}

	private void stopFalling() {
			PieceAction.FALL.future.cancel(false);
		synchronized(piece) {
		if (PieceAction.FALL.isMoving) {
			PieceAction.FALL.future.cancel(false);
			PieceAction.FALL.isMoving = false;
		}
		}
	}
	
	public void warpDown() {
		synchronized(piece) {
			// this if prevents multiple warp downs 
			// each warp would reset the falling delay so multiple warps
			// would prevent the piece from landing allowing the piece
			// to be moved left and right indefinitely
			if (!PieceAction.WARP.isMoving) {
				stopFalling();
				
				while (tryToMovePiece(PieceAction.WARP)) { }
			
				startFalling();
				
				PieceAction.WARP.isMoving = true;
			}
		}
	}
	
	public void stopMovingDown() {
		synchronized(piece) {
			PieceAction.DOWN.isPressed = false;
			startFalling();
		}
	}
	
	public void startPieceAction(PieceAction action) {
		// This is run by the swing event dispatch thread.
			if (!action.isPressed) {
				action.isPressed = true;
				
				if (action.opposite.isPressed) {
					action.opposite.future.cancel(false);
					action.opposite.isMoving = false;
				}
				
				if (action.isMoving) {
					action.future.cancel(false);
				}
				action.future = 
					scheduler.scheduleAtFixedRate(action.runner, 
												  0, 
												  action.getDelay(), 
												  TimeUnit.NANOSECONDS);
				action.isMoving = true;
			}			
	}

	public void stopPieceAction(PieceAction action) {
		synchronized(action) {
			action.isPressed = false;
			
			if (action.opposite.isPressed) {
				startPieceAction(action.opposite);
			}
		}
	}

	private final Runnable makePieceFall = () -> { 
		// Not run by the swing event dispatch thread.
		if (!tryToMovePiece(PieceAction.FALL)) {
			landPiece();
		}
	};

}