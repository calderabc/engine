package engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import engine.puzzle.Game;
import engine.swing.Renderer;
import tetris.Flag;
import tetris.FlagException;


@SuppressWarnings("serial")
public abstract class Part<T extends Part<?>> implements Renderable, Positionable {
	
		
	private static final Map<String, String> PART_TO_RENDERER_MAP = 
		Collections.unmodifiableMap(
			new HashMap<String, String>() {
				{
					put("Block",       "Block");
					put("Piece",       "Piece");
					put("Digit",       "Digit");
					put("Score",       "Number");
					put("Level",       "Number");
					put("RowsCleared", "Number");
					put("Info",        "Info");
					put("Board",       "Board");
					put("Screen",      "Screen");
				}
			});
	
	private Collection<T> children;
	
	private Renderer renderer;
	
	private boolean visible = true;
	
	public final boolean isVisible() {
		return visible;
	}

	protected final void setVisible(boolean visible) {
		this.visible = visible;
	}

	private Position pos;

	protected Part(){
		// If pos hasn't been set it will still be null.
		// TODO: I should require subclasses to initialize the 'pos' value.
		// TODO: Or I should set 'pos' to a default value if subclass does not set.

		children = new Vector<T>();
	}

	public Part(boolean isVisible) {
		this();
		visible = isVisible;
	}

	public Part(int newX, int newY) {
		this();
		pos = new Position(newX, newY);
	}
	
	public Part(Coordinates newPosition) {
		this();
		pos = new Position(newPosition); 
	}
	
	public Part(Part<T> other) {
		this();
		pos = new Position(other.getPosition());
	}	
	
	/*
	 * TODO: Figure out if there is a way to do this.  The problem is I don't know of
	 * a way to do a generic deep copy of a child class here within the parent
	 * abstract class.  I don't know of a way to instantiate whichever child class
	 * based on what type of class they are.  Maybe there is a way to instantiate
	 * a new class based on what type of class is already there.  Research.
	 */
	/*
	public Part(Part<T> other) {
		children = new Vector<T>();
		for (Part currPart : other.children) {
			// This was my second attempt but no success.  Is there a way to
			// create a new class based on what type of class the child is?
			children.add(new Part(currPart));
		}
		// This code does not work because it does not do a deep copy.  It simply
		// adds the existing objects in other.children to this.children without
		// doing a deep copy.
		//children.addAll(other.children);
	}
	*/
	
	public final boolean newRendererIfNull() {
		
		if (!visible) return false;
		
		if (renderer == null) {
			String partClassName = null;
			String packageName = this.getClass().getPackage().getName() + ".";
			String fullClassName = this.getClass().getCanonicalName();
				
			for (String className: PART_TO_RENDERER_MAP.keySet()) {
				if (fullClassName.equals(packageName + className)) {
					partClassName = PART_TO_RENDERER_MAP.get(className);
					break;
				}
			}
				
			try {
				if (partClassName == null) {
					throw new NoRendererException();
				}

				System.out.println(partClassName);
				System.out.println(Game.profile.getOption(Flag.DISPLAY).getHandle() );

				Constructor<?> con = 
					Class.forName(Game.profile.getOption(Flag.DISPLAY).getHandle() 
								  + "." 
								  + partClassName 
								  + "Renderer")
						 .getConstructor(new Class[] {Part.class});
				
				renderer = (Renderer) con.newInstance(new Object[] {this});
			
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace(); 
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (NoRendererException e) {
				e.printStackTrace();
			} catch (FlagException e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	public final Part<T> addChild(T newChild) {
		System.out.println(children);
		children.add(newChild);
		
		if (newRendererIfNull()) {
			renderer.add(newChild);
		}
			
		return this;
	}
	
	
	public Part<T> addChildren(Collection<? extends T> newParts) {
		for (T currPart: newParts) {
			addChild(currPart);
		}
	
		return this;
	}
	
	
	public final boolean removeChild(T testChild) {
		boolean result = false;
		
		for(Part<?> child: getChildren()) {
			if (child.equals(testChild)) {
				if (renderer != null) {
					renderer.remove(child);
				}
				
				getChildren().remove(child);
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	
	public final Renderer getRenderer() {
		newRendererIfNull();
		
		return renderer;
	}
	
	
	@Override 
	public void init() {
		if (newRendererIfNull()) {
			renderer.init();
		}
	}
	
	@Override
	public void update() {
		if (newRendererIfNull()) {
			renderer.update();
		}
	}
	
	
	
	public Collection<T> getChildren() {
		return children;
	}
	
	
	
	
	@Override
	public int getX() {
		return pos.x;
	}

	@Override
	public int getY() {
		return pos.y;
		
	}

	@Override
	public Positionable setX(int newX) {
		pos.x = newX;
		return this;
	}

	@Override
	public Positionable setY(int newY) {
		pos.y = newY;
		return this;
	}

	@Override
	public Position getPosition() {
		return pos;
	}

	@Override
	public Positionable setPosition(int newX, int newY) {
		setX(newX);
		setY(newY);
		
		return this;
	}
	
}