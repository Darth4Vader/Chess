package ChessUIJavaFX;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLayeredPane;

public interface RemoveDragListener {
	
	public JLayeredPane removeWhenDragged();
	
	public void addAfterDragged();
	
	public static MouseListener createDefaultDragListener() {
		return new MouseAdapter() {
			
			private RemoveDragListener mainContainer;
			private JLayeredPane dragOn;

			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					Component component = e.getComponent();
					Container container = component.getParent();
					if(container instanceof RemoveDragListener) {
						Point point = component.getLocation();
						mainContainer = (RemoveDragListener) container;
						dragOn = ((RemoveDragListener)container).removeWhenDragged();
						//JLayeredPane
						dragOn.add(component);
						Component comp = container;
						while(comp != null && !comp.equals(dragOn)) {
							point.x += comp.getX();point.y += comp.getY();
							comp = comp.getParent();
						}
						component.setLocation(point);
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					Component component = e.getComponent();
					if(mainContainer != null && dragOn != null) {
						dragOn.remove(component);
						mainContainer.addAfterDragged();
					}
				}
			}
			
		};
	}
}
