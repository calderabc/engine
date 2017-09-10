package engine.puzzle;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import engine.GraphicsEngine;
import engine.Screen;
import engine.puzzle.tetris.ScoreCalculator;
import engine.swing.Swing;

public class Game {
	static public final PieceData TETRIS_PIECE_DATA = new PieceData();

	static public Game me;

	public GraphicsEngine engine;
	public Screen screen;
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

			isPieceLanded = false;
			startFalling();
			
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
				PieceAction.resetAll();
				board.landPiece(piece);
				board.tryRemoveBlocks();
				
				isPieceLanded = true;
			}
		}
	}
	
	private void scheduleFuture(PieceAction action, boolean startNow) {
		action.isMoving = true;
		action.future = 
			scheduler.scheduleAtFixedRate(action.runner, 
		                                  (startNow) ? 0 : action.getDelay(), 
		                                  action.getDelay(), 
		                                  TimeUnit.NANOSECONDS);
	}
	
	private void cancelFuture(PieceAction action) {
		action.future.cancel(false);
		action.isMoving = false;
	}

	private void startFalling() {
		synchronized(piece) {
			if (!PieceAction.FALL.isMoving) {
				PieceAction.FALL.runner = () -> {
					if (!tryToMovePiece(PieceAction.FALL)) {
						stopFalling();
						landPiece();
					}
				};
				scheduleFuture(PieceAction.FALL, false);
			}
		}
	}

	private void stopFalling() {
		synchronized(piece) {
			if (PieceAction.FALL.isMoving) {
				cancelFuture(PieceAction.FALL);
			}
		}
	}
	
	public void warpDown() {
		synchronized(piece) {
			if (!PieceAction.WARP.isMoving) {
				stopFalling();

				PieceAction.WARP.runner = () -> {
					if (!tryToMovePiece(PieceAction.WARP)) {
						PieceAction.WARP.isMoving = false;
						startFalling();
						PieceAction.WARP.future.cancel(true);
					}
				};
				
				scheduleFuture(PieceAction.WARP, true);
			}
		}
	}

	public void startPieceAction(PieceAction action) {
		synchronized(piece) {
			if (!action.isPressed) {
				action.isPressed = true;
				
				if (action.opposite.isPressed) {
					cancelFuture(action.opposite);
				}
				
				scheduleFuture(action, true);
			}			
		}
	}

	public void stopPieceAction(PieceAction action) {
		synchronized(piece) {
			cancelFuture(action);
			action.isPressed = false;

			if (action.opposite.isPressed) {
				startPieceAction(action.opposite);
			}
		}
	}

	public void startMovingDown() {
		synchronized(piece) {
			if (!PieceAction.DOWN.isPressed) {
				PieceAction.DOWN.isPressed = true;
				
				if (!PieceAction.DOWN.isMoving) {
					stopFalling();
					
					PieceAction.DOWN.runner = () -> {
						if (!Game.me.tryToMovePiece(PieceAction.DOWN)) {
							landPiece();
							cancelFuture(PieceAction.DOWN);
						}
					};
			
					scheduleFuture(PieceAction.DOWN, true);
				}
			}
		}
	}

	public void stopMovingDown() {
		synchronized(piece) {
			cancelFuture(PieceAction.DOWN);
			PieceAction.DOWN.isPressed = false;
			startFalling();
		}
	}

}