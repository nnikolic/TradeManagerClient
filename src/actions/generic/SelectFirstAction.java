package actions.generic;

import generic.form.GenericForm;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import localization.Local;
import actions.CustomAbstractAction;
import app.Appliction;

public class SelectFirstAction extends CustomAbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5138970089155394117L;
	
	public SelectFirstAction(){
		putValue(NAME, Local.getString("SELECT_FIRST"));
		putValue(SHORT_DESCRIPTION, Local.getString("SELECT_FIRST_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("begining.png"));
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		GenericForm gf = Appliction.getInstance().getCurrentForm();
		if(gf.getGenericViewForm().getTableModel().getEntities().size()>0){
			gf.getTable().setRowSelectionInterval(0, 0);
			gf.getTable().scrollRectToVisible(new Rectangle(0, 0, gf.getTable().getWidth(), gf.getTable().getRowHeight()));
		}
	}
}
