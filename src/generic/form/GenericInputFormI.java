package generic.form;

import generic.tools.MessageObject;
import util.ServerResponse;

public interface GenericInputFormI {
	public Object getData();
	public void populateData(Object entity);
	public void unbindData();
	public void reset();
	
	public MessageObject validateInput();
	
	public ServerResponse saveEntity();
	public void print();
}
