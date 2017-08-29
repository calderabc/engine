package engine.puzzle;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JComponent;
import engine.Part;
import engine.puzzle.tetris.ScoreCalculator;
import engine.puzzle.tetris.swing.Keyboard;
import engine.swing.Screen;
import engine.swing.Sprite;

public class Game {
	private Screen screen;
	private Board board;
	private Piece piece;
	private Score score;
	private List<Part> spriteParts = new Vector<Part>();

	
	Level level;
	 
	private boolean isPieceLanded;
	
	private ScheduledExecutorService scheduler = 
		Executors.newScheduledThreadPool(2);
	
	
	public Game(PieceData pieceData) {

		//PieceAction.FALL.setSpeed(2 / 1.0e9);
		
		screen = new Screen();
		board = new Board(pieceData);
		score = new Score(ScoreCalculator.NINTENDO, 10, 0);
		level = new Level(1);
		
		JComponent component = (JComponent)screen.getContentPane();
		Keyboard.initInputMap(component.getInputMap());
		Keyboard.initActionMap(component.getActionMap());
		
		while (true) {		
			piece = board.startNewPiece();
			spriteParts.addAll(piece.getBlocks());

			isPieceLanded = false;
			startFalling();
			
				//board.addChild(piece);
				
				if (!board.doesPieceFit(piece)) {
					if (!board.doesPieceFit(piece)) {
					board.landPiece();
					
					System.out.println("*****asl;dkfja;lsdkf ending here ********** alkfl;askjdf;l");
					break;
					}
				}
			
			//	System.out.println("EventDispatchThread: " + javax.swing.SwingUtilities.isEventDispatchThread());
				synchronized(piece) {
					try {
						piece.wait();
					} catch (InterruptedException e) {
						
						
					}
				}
			//stopFalling();
				
			// TODO: Fix!!
			int rowCount = 0;
			//int rowCount = board.removeRows(board.getFullRows());
			
			if (rowCount > 0) {
				score.updateScore(level, rowCount);
				
				/*
				if (score.isLevelUp(level, rowsCleared)) {
				System.out.println("**************************************************************************");
					level.next();
					
					((ScreenRenderer) screen.getRenderer()).changeColorFilter();
				}
				*/
			}
		}
				
	}
	
	private boolean tryToMovePiece(int offsetX, int offsetY) {
		if (!isPieceLanded) {
			Piece testPiece = new Piece(piece);
			
			testPiece.move(offsetX, offsetY);
			
			if (board.doesPieceFit(testPiece)) {
				piece.move(offsetX, offsetY);
				
				return true;
			}
		}
		return false;
	}
	
	private boolean tryToRotatePiece(PieceAction action) {
		if (!isPieceLanded) {
			Piece testPiece = new Piece(piece);
			
			testPiece.rotate(action);
			System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
			/*
			for (Block currBlock: piece.getBlocks()) {
				currBlock.printInfo();
			}
			*/
			System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
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
				piece.rotate(action);
				System.out.println("ssssssssssssssssssssssssssssssssssss");
				/*
				for (Block currBlock: piece.getBlocks()) {
					currBlock.printInfo();
				}
				*/
				System.out.println("ttttttttttttttttttttttttttttttt");
				
				return true;
			}
		} 
			
		return false;
	}
	
	private void landPiece() {
		synchronized(piece) {
			if (!isPieceLanded) {
				board.landPiece();
				
				PieceAction.resetAll();
				
				isPieceLanded = true;
			}
		}
	}
	
	private void startFalling() {
		if (!PieceAction.FALL.isMoving) {
			PieceAction.FALL.future = 
				scheduler.scheduleAtFixedRate(makePieceFall, 
											  PieceAction.FALL.getDelay(), 
											  PieceAction.FALL.getDelay(), 
											  TimeUnit.NANOSECONDS);
			PieceAction.FALL.isMoving = true;
		}
	}
	
	private void stopFalling() {
		if (PieceAction.FALL.isMoving) {
			PieceAction.FALL.future.cancel(false);
			PieceAction.FALL.isMoving = false;
		}
	}
	
	private void warpDown() {
		// this if prevents multiple warp downs 
		// each warp would reset the falling delay so multiple warps
		// would prevent the piece from landing allowing the piece
		// to be moved left and right indefinitely
		if (!PieceAction.WARP.isMoving) {
			stopFalling();
			
			while (tryToMovePiece(0, 1)) { }
		
			startFalling();
			
			PieceAction.WARP.isMoving = true;
		}
	}
	
	private void startMovingDown() {
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
	
	private void stopMovingDown() {
		synchronized(PieceAction.DOWN) {
			PieceAction.DOWN.isPressed = false;
			startFalling();
		}
	}
	
	private void startMovingLeft() {
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
	
	private void stopMovingLeft() {
		synchronized(PieceAction.LEFT) {
			PieceAction.LEFT.isPressed = false;
			
			if (PieceAction.RIGHT.isPressed) {
				startMovingRight();
			}
		}
	}
	
	private void startMovingRight() {
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
	
	private void stopMovingRight() {
		synchronized(PieceAction.RIGHT) {
			PieceAction.RIGHT.isPressed = false;
			
			if (PieceAction.LEFT.isPressed) {
				startMovingLeft();
			}
		}
	}
	
	private void startRotatingClockwise() {
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
	
	private void stopRotatingClockwise() {
		synchronized(PieceAction.CLOCKWISE) {
			PieceAction.CLOCKWISE.isPressed = false;
			
			if (PieceAction.COUNTERCLOCKWISE.isPressed) {
				startRotatingCounterClockwise();
			}
		}
	}
	
	private void startRotatingCounterClockwise() {
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
	
	private void stopRotatingCounterClockwise() {
		synchronized(PieceAction.COUNTERCLOCKWISE) {
			PieceAction.COUNTERCLOCKWISE.isPressed = false;
			
			if (PieceAction.CLOCKWISE.isPressed) {
				startRotatingClockwise();
			}
		}
	}

	private final Runnable makePieceFall = new Runnable() {
		public void run() {
			if (!tryToMovePiece(0, 1)) {
				landPiece();
			
				synchronized (piece) {
					piece.notifyAll();
				}
			}
		}
	};
	
	private final Runnable movePieceDown = new Runnable() {
		public void run() {
			synchronized (PieceAction.DOWN) {
				if (PieceAction.DOWN.isPressed) {
					if (!tryToMovePiece(0, 1)) {
						// "false", I don't want to interrupt the thread 
						// since it's the thread I'm now currently running.
						PieceAction.DOWN.future.cancel(false);
						PieceAction.DOWN.isMoving = false;
					}
				}
			}
		}
	};
	
	private final Runnable movePieceRight = new Runnable() {
		public void run() {
			synchronized (PieceAction.RIGHT) {
				if (PieceAction.RIGHT.isPressed) {
					if (!tryToMovePiece(1, 0)) {
						PieceAction.RIGHT.future.cancel(false);
						PieceAction.RIGHT.isMoving = false;
					}
				}
			}
		}
	};
	
	private final Runnable movePieceLeft = new Runnable() {
		public void run() {
			synchronized (PieceAction.LEFT) {
				if (PieceAction.LEFT.isPressed) {
					if (!tryToMovePiece(-1, 0)) {
						PieceAction.LEFT.future.cancel(false);
						PieceAction.LEFT.isMoving = false;
					}
				}
			}
		}
	};
	
	private final Runnable rotatePieceClockwise = new Runnable() {
		public void run() {
			synchronized (PieceAction.CLOCKWISE) {
				if (PieceAction.CLOCKWISE.isPressed) {
					if (!tryToRotatePiece(PieceAction.CLOCKWISE)) {
						PieceAction.CLOCKWISE.future.cancel(false);
						PieceAction.CLOCKWISE.isMoving = false;
					}
				}
			}
		}
	};
	
	private final Runnable rotatePieceCounterClockwise = new Runnable() {
		public void run() {
			synchronized (PieceAction.COUNTERCLOCKWISE) {
				if (PieceAction.COUNTERCLOCKWISE.isPressed) {
					if (!tryToRotatePiece(PieceAction.COUNTERCLOCKWISE)) {
						PieceAction.COUNTERCLOCKWISE.future.cancel(false);
						PieceAction.COUNTERCLOCKWISE.isMoving = false;
					}
				}
			}
		}
	};
}