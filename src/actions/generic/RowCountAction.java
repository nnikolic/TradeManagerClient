package actions.generic;

import java.awt.event.ActionEvent;

import localization.Local;

import actions.CustomAbstractAction;
import app.Appliction;

public class RowCountAction extends CustomAbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1278132028092391301L;
	
	public RowCountAction() {
		putValue(SHORT_DESCRIPTION, Local.getString("ROW_COUNT_ACTION_DESCRIPTION"));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Appliction.getInstance().getCurrentForm().loadData();
	}
}
