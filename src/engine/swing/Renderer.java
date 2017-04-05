package engine.swing;

import javax.swing.JComponent;
import javax.swing.JPanel;

import engine.Part;
import engine.Renderable;

@SuppressWarnings("serial")
public abstract class Renderer extends JPanel implements Renderable {
	
	public void add(Part<?> owner) {
		if (owner.isVisible()) {
			add(owner.getRenderer());
		} else {
			// if owner not visible then try to add the renderers of
			// any children the owner might have.
			for(Part<?> childOfOwner: owner.getChildren()) {
				add(childOfOwner);
			}
		}
	}
	
	public void remove(Part<?> owner) {
		if (owner.isVisible()) {
			JComponent component = owner.getRenderer();
			component.removeAll();
			remove(component);
		
		} else {
			// since owner does not have renderer remove the renderers
			// of any children owner may have
			// TODO: should I do this or just leave children's renderers intact?
			for(Part<?> currChildOfOwner: owner.getChildren()) {
				remove(currChildOfOwner);
			}
		}
	}
	
	@Override
	public void init() {
		
	}
	
	@Override
	public void update() {
		//this.getTopLevelAncestor().repaint();
		
	}
}