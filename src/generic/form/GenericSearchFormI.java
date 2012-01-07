package generic.form;

import generic.tools.MessageObject;
import util.ServerResponse;

public interface GenericSearchFormI {
	public ServerResponse search();
	public void reset();
	public MessageObject validateInput();
	public void onShow();
}
