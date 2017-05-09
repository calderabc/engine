package engine.puzzle;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import engine.Part;
import engine.swing.ScreenRenderer;
import tetris.Profile;
import tetris.RowsCleared;
import tetris.ScoreCalculator;
import tetris.TetrisPiecePool;

public class Game extends Part<Part<?>> implements Runnable {
	
	public static Profile profile;
	
	private Screen screen;
	private Board board;
	private Info info;
	private Piece piece;
	private Score score;

	
	Level level;
	RowsCleared rowsCleared;
	 
	private boolean isPieceLanded;
	
	private ScheduledExecutorService scheduler = 
		Executors.newScheduledThreadPool(2);
	
	public Game(PiecePool newPieces) {
		super(false);
		board = new Board(newPieces);

		Thread abc = new Thread(this);
		abc.isDaemon();
		abc.start();

		try {
			abc.join();
		} catch (InterruptedException e) { }

	}
	
	public void initInputMap(InputMap map) {
		map.put(KeyStroke.getKeyStroke("UP"), "warp down");
		map.put(KeyStroke.getKeyStroke("DOWN"), "move down");
		map.put(KeyStroke.getKeyStroke("released DOWN"), "stop move down");
		map.put(KeyStroke.getKeyStroke("LEFT"), "move left");
		map.put(KeyStroke.getKeyStroke("released LEFT"), "stop move left");
		map.put(KeyStroke.getKeyStroke("RIGHT"), "move right");
		map.put(KeyStroke.getKeyStroke("released RIGHT"), "stop move right");
		map.put(KeyStroke.getKeyStroke("ESCAPE"), "quit");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, KeyEvent.CTRL_DOWN_MASK), "rotate counterclockwise");
		map.put(KeyStroke.getKeyStroke("released CONTROL"), "stop rotate counterclockwise");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ALT, KeyEvent.ALT_DOWN_MASK), "rotate clockwise");
		map.put(KeyStroke.getKeyStroke("released ALT"), "stop rotate clockwise");
	}
	
	@SuppressWarnings("serial")
	private void initActionMap(ActionMap aMap) {
	
		aMap.put("warp down", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				warpDown();
			}
		});
		
		aMap.put("move down", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (PieceAction.DOWN.isLegitKeyPress()) {
					startMovingDown();
				}
				
			}
		});
		
		aMap.put("stop move down", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				PieceAction.DOWN.setReleaseTime();
				stopMovingDown();
			}
		});
		
		aMap.put("move left", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (PieceAction.LEFT.isLegitKeyPress()) {
					startMovingLeft();
				}
			}
		});
		
		aMap.put("stop move left", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				PieceAction.LEFT.setReleaseTime();
				stopMovingLeft();
			}
		});
		
		aMap.put("move right", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (PieceAction.RIGHT.isLegitKeyPress()) {
					startMovingRight();
				}
			}
		});
		
		aMap.put("stop move right", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				PieceAction.RIGHT.setReleaseTime(); 
				
				stopMovingRight();
			}
		});
		
		aMap.put("rotate clockwise", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
	//			if (PieceAction.CLOCKWISE.isLegitKeyPress()) { 
					startRotatingClockwise();
	//			}
			}
		});
		
		aMap.put("stop rotate clockwise", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				PieceAction.CLOCKWISE.setReleaseTime();
				stopRotatingClockwise();
			}
		});
		
		aMap.put("rotate counterclockwise", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
	//			if (PieceAction.COUNTERCLOCKWISE.isLegitKeyPress()) { 
					startRotatingCounterClockwise();
	//			}
			}
		});
		
		aMap.put("stop rotate counterclockwise", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				PieceAction.COUNTERCLOCKWISE.setReleaseTime();
				stopRotatingCounterClockwise();
			}
		});
		
		aMap.put("quit", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
	}
	
	
	public void run() {
		//PieceAction.FALL.setSpeed(2 / 1.0e9);
		
		score = new Score(ScoreCalculator.NINTENDO, 10, 0);
		level = new Level(1);
		rowsCleared = new RowsCleared();
		screen = new Screen();
		
		JComponent component = ((JComponent) screen.getRenderer());
		initInputMap(component.getInputMap());
		initActionMap(component.getActionMap());
		
		// always add the board to the screen first because all the other
		// elements depend on the size of the board for their positioning
		screen.addChild(board);
		info = new Info();
		info.addChild(score);
		info.addChild(level);
		info.addChild(rowsCleared);
		screen.addChild(info);
		//screen.addChild(score);
		//screen.addChild(level);
		//screen.addChild(rowsCleared);
		
		this.addChild(screen);
		
		screen.getRenderer().update();
		
		System.out.println(board.getRenderer().getWidth());
		System.out.println(board.getRenderer().getHeight());
		System.out.println(board.getRenderer().getSize());
		//System.exit(0);
		
		while (true) {		
			/*
			PieceAction.RIGHT.setSpeed(
				PieceAction.LEFT.setSpeed(
					PieceAction.DOWN.setSpeed(PieceAction.FALL.getSpeed() * 4)
				)
			);
			*/
			
			piece = board.startNewPiece();
			board.update();
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
				
				rowsCleared.add(rowCount);
					
				if (score.isLevelUp(level, rowsCleared)) {
				System.out.println("**************************************************************************");
					level.next();
					
					((ScreenRenderer) screen.getRenderer()).changeColorFilter();
				}
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
			for (Block currBlock: piece.getBlocks()) {
				currBlock.printInfo();
			}
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
				for (Block currBlock: piece.getBlocks()) {
					currBlock.printInfo();
				}
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