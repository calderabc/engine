package engine;

public abstract class Part implements Movable {
	public Visual visual = null; 
	public Coordinates pos = null;

	public Part(Coordinates newPosition) {
		pos = new Coordinates(newPosition);
	}

	public Part(Part other) {
		pos = new Coordinates(other.pos);
		visual = (Visual)Reflection.newInstance(other.visual);
	}	

	public void initVisual(Visual.Id newId) {
		visual = Reflection.newVisual(Game.me, this, newId);
		visual.update(this);
	}
	
	public void terminate() {
		if (visual != null) { 
			Game.me.screen.removePart(this);
		}
		visual = null;
	}

	// Implement the Movable interface.
	@Override
	public final Movable move(Coordinates offset) {
		pos.move(offset);
		
		return this;
	}
}