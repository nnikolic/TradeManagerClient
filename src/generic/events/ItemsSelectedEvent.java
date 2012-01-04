package generic.events;

import java.util.ArrayList;
import java.util.EventObject;

public class ItemsSelectedEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7273678388155414711L;

	private ArrayList<Object> selectedObjects = null;
	
	public ItemsSelectedEvent(Object source, ArrayList<Object> objects) {
		super(source);
		if(objects == null){
			objects = new ArrayList<Object>();
		}
		selectedObjects = objects;
	}

	public ArrayList<Object> getSelectedObjects() {
		return selectedObjects;
	}
}
