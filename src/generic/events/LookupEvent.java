package generic.events;


import java.util.EventObject;

public class LookupEvent extends EventObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 50494336931372791L;
	private Object data = null;
	private String siblindKey;
	
	public LookupEvent(Object source, Object data, String siblingKey){
		super(source);
		this.siblindKey = siblingKey;
		this.data = data;
	}
	
	public String getSiblindKey() {
		return siblindKey;
	}
	
	public Object getData() {
		return data;
	}
}
