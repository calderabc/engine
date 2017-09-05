package engine.puzzle;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
	
	private ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(2);

	public static void main(String argv[]) {
		me = new Game(TETRIS_PIECE_DATA);
		me.run();

		System.exit(0);
	}
	
	public Game(PieceData pieceData) {
		board = new Board(pieceData);
	}
	
	public void run() {
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
	
	private boolean tryToMovePiece(Coordinates offset) {
		System.out.println("tryToMovePiece");
		if (!isPieceLanded) {
			Piece testPiece = new Piece(piece);
			
			testPiece.move(offset);
			
			if (board.doesPieceFit(testPiece)) {
				piece.move(offset);
				System.out.println("tryToMovePiece before piece.updateVisual");
				piece.updateVisual();
				System.out.println("tryToMovePiece after piece.updateVisual");
				screen.update();
				System.out.println("tryToMovePiece after screen.update()");
				return true;
			}
		}
		return false;
	}
	
	private boolean tryToRotatePiece(PieceAction action) {
		if (!isPieceLanded) {
			Piece testPiece = new Piece(piece).rotate(action);
			
			System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
			/*
			for (Block currBlock: piece.getBlocks()) {
				currBlock.printInfo();
			}
			*/
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
			
			if (board.doesPieceFit(testPiece)) {
				piece = testPiece; // Intentional copy by reference.
				System.out.println("ssssssssssssssssssssssssssssssssssss");
				/*
				for (Block currBlock: piece.getBlocks()) {
					currBlock.printInfo();
				}
				*/
				return true;
			}
		} 
			
		return false;
	}
	
	private void landPiece() {
			if (!isPieceLanded) {
				board.landPiece(piece);
				
				PieceAction.resetAll();
				
				isPieceLanded = true;
			}
	}
	
	private void startFalling() {
		System.out.println("startFalling");
		if (!PieceAction.FALL.isMoving) {
			System.out.println("startFalling if true");
			PieceAction.FALL.future = 
				scheduler.scheduleAtFixedRate(makePieceFall, 
											  PieceAction.FALL.getDelay(), 
											  PieceAction.FALL.getDelay(), 
											  TimeUnit.NANOSECONDS);
			PieceAction.FALL.isMoving = true;
		}
	}
	
	public void startMovingDown() {
		synchronized(PieceAction.DOWN) {
			if (!PieceAction.DOWN.isPressed) {
				PieceAction.DOWN.isPressed = true;
				
				if (!PieceAction.DOWN.isMoving) {
					stopFalling();
				
					PieceAction.DOWN.future = 
						scheduler.scheduleAtFixedRate(movePieceDown, 
													  0, 
													  PieceAction.DOWN.getDelay(), 
													  TimeUnit.NANOSECONDS);
					PieceAction.DOWN.isMoving = true;
				}
			}
		}
	}

	private void stopFalling() {
		if (PieceAction.FALL.isMoving) {
			PieceAction.FALL.future.cancel(true);
			PieceAction.FALL.isMoving = false;
		}
	}
	
	public void warpDown() {
		// this if prevents multiple warp downs 
		// each warp would reset the falling delay so multiple warps
		// would prevent the piece from landing allowing the piece
		// to be moved left and right indefinitely
		if (!PieceAction.WARP.isMoving) {
			stopFalling();
			
			while (tryToMovePiece(new Coordinates(0, 1))) { }
		
			startFalling();
			
			PieceAction.WARP.isMoving = true;
		}
	}
	
	public void stopMovingDown() {
		synchronized(PieceAction.DOWN) {
			PieceAction.DOWN.isPressed = false;
			startFalling();
		}
	}
	
	public void startMovingLeft() {
		synchronized(PieceAction.LEFT) {
			if (!PieceAction.LEFT.isPressed) {
				PieceAction.LEFT.isPressed = true;
				
				if (PieceAction.RIGHT.isPressed) {
					PieceAction.RIGHT.future.cancel(false);
					PieceAction.RIGHT.isMoving = false;
				}
				
				if (PieceAction.LEFT.isMoving) {
					PieceAction.LEFT.future.cancel(true);
				}
					
				PieceAction.LEFT.future = 
					scheduler.scheduleAtFixedRate(movePieceLeft, 
												  0, 
												  PieceAction.LEFT.getDelay(), 
												  TimeUnit.NANOSECONDS);
				
				PieceAction.LEFT.isMoving = true;
			}
		}
	}
	
	public void stopMovingLeft() {
		synchronized(PieceAction.LEFT) {
			PieceAction.LEFT.isPressed = false;
			
			if (PieceAction.RIGHT.isPressed) {
				startMovingRight();
			}
		}
	}
	
	public void startMovingRight() {
		synchronized(PieceAction.RIGHT) {
			if (!PieceAction.RIGHT.isPressed) {
				PieceAction.RIGHT.isPressed = true;
				
				if (PieceAction.LEFT.isPressed) {
					PieceAction.LEFT.future.cancel(false);
					PieceAction.LEFT.isMoving = false;
				}
				
				if (PieceAction.RIGHT.isMoving) {
					PieceAction.RIGHT.future.cancel(true);
				}
				PieceAction.RIGHT.future = 
					scheduler.scheduleAtFixedRate(movePieceRight, 
												  0, 
												  PieceAction.RIGHT.getDelay(), 
												  TimeUnit.NANOSECONDS);
				
				PieceAction.RIGHT.isMoving = true;
			}			
		}
	}
	
	public void stopMovingRight() {
		synchronized(PieceAction.RIGHT) {
			PieceAction.RIGHT.isPressed = false;
			
			if (PieceAction.LEFT.isPressed) {
				startMovingLeft();
			}
		}
	}
	
	public void startRotatingClockwise() {
		synchronized(PieceAction.CLOCKWISE) {
			if (!PieceAction.CLOCKWISE.isPressed) {
				PieceAction.CLOCKWISE.isPressed = true;
				
				if (PieceAction.COUNTERCLOCKWISE.isPressed) {
					PieceAction.COUNTERCLOCKWISE.future.cancel(false);
					PieceAction.COUNTERCLOCKWISE.isMoving = false;
				}
				
				if (PieceAction.CLOCKWISE.isMoving) {
					PieceAction.CLOCKWISE.future.cancel(true);
				}
				PieceAction.CLOCKWISE.future = 
					scheduler.scheduleAtFixedRate(rotatePieceClockwise, 
												  0, 
												  PieceAction.CLOCKWISE.getDelay(), 
												  TimeUnit.NANOSECONDS);
				PieceAction.CLOCKWISE.isMoving = true;
			}			
		}
	}
	
	public void stopRotatingClockwise() {
		synchronized(PieceAction.CLOCKWISE) {
			PieceAction.CLOCKWISE.isPressed = false;
			
			if (PieceAction.COUNTERCLOCKWISE.isPressed) {
				startRotatingCounterClockwise();
			}
		}
	}
	
	public void startRotatingCounterClockwise() {
		synchronized(PieceAction.COUNTERCLOCKWISE) {
			if (!PieceAction.COUNTERCLOCKWISE.isPressed) {
				PieceAction.COUNTERCLOCKWISE.isPressed = true;
				
				if (PieceAction.CLOCKWISE.isPressed) {
					PieceAction.CLOCKWISE.future.cancel(false);
					PieceAction.CLOCKWISE.isMoving = false;
				}
				
				if (PieceAction.COUNTERCLOCKWISE.isMoving) {
					PieceAction.COUNTERCLOCKWISE.future.cancel(true);
				}
				PieceAction.COUNTERCLOCKWISE.future = 
					scheduler.scheduleAtFixedRate(rotatePieceCounterClockwise, 
												  0, 
												  PieceAction.COUNTERCLOCKWISE.getDelay(), 
												  TimeUnit.NANOSECONDS);
				PieceAction.COUNTERCLOCKWISE.isMoving = true;
			}			
		}
	}
	
	public void stopRotatingCounterClockwise() {
		synchronized(PieceAction.COUNTERCLOCKWISE) {
			PieceAction.COUNTERCLOCKWISE.isPressed = false;
			
			if (PieceAction.CLOCKWISE.isPressed) {
				startRotatingClockwise();
			}
		}
	}

	private final Runnable makePieceFall = () -> { 
		System.out.println("Game.makePieceFall()");
		if (!tryToMovePiece(new Coordinates(0, 1))) {
			System.out.println("Before landing");
			landPiece();
		}
	};
	
	private final Runnable movePieceDown = () -> {
		synchronized (PieceAction.DOWN) {
			if (PieceAction.DOWN.isPressed) {
				if (!tryToMovePiece(new Coordinates(0, 1))) {
					// "false", I don't want to interrupt the thread 
					// since it's the thread I'm now currently running.
					PieceAction.DOWN.future.cancel(false);
					PieceAction.DOWN.isMoving = false;
				}
			}
		}
	};
	
	private final Runnable movePieceRight = () -> { 
		synchronized (PieceAction.RIGHT) {
			if (PieceAction.RIGHT.isPressed) {
				if (!tryToMovePiece(new Coordinates(1, 0))) {
					PieceAction.RIGHT.future.cancel(false);
					PieceAction.RIGHT.isMoving = false;
				}
			}
		}
	};
	
	private final Runnable movePieceLeft = () -> { 
		synchronized (PieceAction.LEFT) {
			if (PieceAction.LEFT.isPressed) {
				if (!tryToMovePiece(new Coordinates(-1, 0))) {
					PieceAction.LEFT.future.cancel(false);
					PieceAction.LEFT.isMoving = false;
				}
			}
		}
	};
	
	private final Runnable rotatePieceClockwise = () -> {
		synchronized (PieceAction.CLOCKWISE) {
			if (PieceAction.CLOCKWISE.isPressed) {
				if (!tryToRotatePiece(PieceAction.CLOCKWISE)) {
					PieceAction.CLOCKWISE.future.cancel(false);
					PieceAction.CLOCKWISE.isMoving = false;
				}
			}
		}
	};
	
	private final Runnable rotatePieceCounterClockwise = () -> {
		synchronized (PieceAction.COUNTERCLOCKWISE) {
			if (PieceAction.COUNTERCLOCKWISE.isPressed) {
				if (!tryToRotatePiece(PieceAction.COUNTERCLOCKWISE)) {
					PieceAction.COUNTERCLOCKWISE.future.cancel(false);
					PieceAction.COUNTERCLOCKWISE.isMoving = false;
				}
			}
		}
	};
	
}