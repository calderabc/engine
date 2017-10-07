package engine.javafx;

import engine.graphics2d.Sprite;

import engine.Game;
import engine.Part;
import engine.Screen;
import engine.Visual;
import engine.Field;
import engine.puzzle.Board;
import engine.puzzle.PuzzleGame;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.geom.AffineTransform;


public class JavaFXScreen extends Screen {
	public Application application = new Application() {
		private GraphicsContext gc;
		@Override
		public void start(Stage stage) {
			Group root = new Group();
			Scene scene = new Scene(root, 800, 800);
			scene.setFill(Color.BLACK);

			Canvas canvas = new Canvas(300, 300);
			gc  = canvas.getGraphicsContext2D();
			gc.setFill(Color.BLACK);

			root.getChildren().add(canvas);
		}

		private void draw(Sprite visual, GraphicsContext canvas) {
			/*
			AffineTransform transformer =
			AffineTransform.getTranslateInstance(scale * visual.position.x,
			scale * visual.position.y);
			transformer.concatenate(AffineTransform.getScaleInstance(scale, scale));
			gc.drawImage(visual.images.get(visual.currImage),
			transformer,
			null);
			*/

		}
	};

	/*
	public JPanel panel = new JPanel(true) {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			// Synchronized to avoid exception where another thread alters
			// displayedParts' structure while this thread is iterating through it.
			synchronized (displayedParts) {
				for (Part displayPart : displayedParts) {

					draw((Sprite)displayPart.visual, g2d);
				}
			}
		}

		private void draw(Sprite visual, Graphics2D canvas) {
			// TODO: Likely due to floating point arithmetic with
			// conversion to integers the width and/or height of the
			// displayed sprite are sometimes one pixel too small.
			// For Tetris the result is pieces with sometimes visible gaps
			// between blocks.  Figure out how to fix.
			AffineTransform transformer =
			AffineTransform.getTranslateInstance(scale * visual.position.x,
			scale * visual.position.y);
			transformer.concatenate(AffineTransform.getScaleInstance(scale, scale));
			canvas.drawImage(visual.images.get(visual.currImage),
			transformer,
			null);

		}
	};
	*/

	private double scale;

	private final List<Part> displayedParts = new Vector<>();

	@Override
	public Class<?> getVisualClass(Game game, Part part) {

		try {
			return Class.forName("engine."
								 + ((PuzzleGame)game).name.toLowerCase() + "."
								 + "swing."
								 + part.getClass().getSimpleName() + "Sprite");
		} catch (ClassNotFoundException e) {
			try {
				return Class.forName(
				"engine.swing." + part.getClass().getSimpleName() + "Sprite");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}

		}
		return null;
	}

	public JavaFXScreen() {
		// TODO: Do this method call on another thread.
		//Keyboard.initInputActionMaps(panel.getInputMap(), panel.getActionMap());

		Group root = new Group();

		frame = new JFrame("Tetris");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Make fullscreen.
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);

		frame.setFocusable(true);
		frame.requestFocusInWindow();

		// Init JPanel
		panel.setOpaque(true);
		frame.setContentPane(panel);


		// This method causes the event dispatch thread to paint.
		frame.setVisible(true);
		// TODO: There's got to be a better way to make sure this panel always has focus.
		panel.grabFocus();

		System.out.println(frame.getHeight());
		System.out.println("hello");
	}

	@Override
	public void setScale(Field field, Visual visual) {
		scale = ((double)panel.getHeight())
				/ (((Board)field).getHeight() * ((Sprite)visual).getHeight());
	}

	@Override
	public void update() {
		try {
			// TODO: This repaints the whole screen.
			// TODO: Make it repaint only the area which has changed.
			//
			// Repaint right now!
			javax.swing.SwingUtilities.invokeAndWait(
			() -> panel.paintImmediately(0, 0, panel.getWidth() - 1, panel.getHeight() - 1)
			);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) { }

		// This fixes a bug I've wasted many hours on.  Apparently my
		// development systems were buffering graphic writes causing an erratic
		// delay in visuals being displayed. The piece appeared to fall at a
		// noticeably inconsistent rate even though swing painting happened
		// correctly at constant intervals. The piece would fall at a constant
		// rate when the mouse was moving around in front of the window (I'm
		// assuming because the moving mouse graphic forced buffer refresh.
		//
		// Oracle/Sun, you suck. I should not have to call some cryptic method
		// to force my system to refresh the window's display when something is
		// drawn by swing. It should be done automatically by the JVM!
		Toolkit.getDefaultToolkit().sync();
	}

	@Override
	public void addParts(Collection<? extends Part> parts) {
		synchronized(displayedParts) {
			displayedParts.addAll(parts);
		}
	}

	@Override
	public void addPart(Part part) {
		synchronized(displayedParts) {
			displayedParts.add(part);
		}
	}

	@Override
	public void removePart(Part terminalPart) {
		synchronized(displayedParts) {
			displayedParts.remove(terminalPart);
		}
	}
}
