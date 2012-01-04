package actions.generic;

import generic.form.GenericForm;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.JTable;

import localization.Local;
import actions.CustomAbstractAction;
import app.Appliction;

public class SelectLastAction extends CustomAbstractAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6438332260882565103L;
	public SelectLastAction(){
		putValue(NAME, Local.getString("SELECT_LAST"));
		putValue(SHORT_DESCRIPTION, Local.getString("SELECT_LAST_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("end.png"));
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		GenericForm gf = Appliction.getInstance().getCurrentForm();
		if(gf.getGenericViewForm().getTableModel().getEntities().size()>0){
			gf.getTable().setRowSelectionInterval(gf.getGenericViewForm().getTableModel().getEntities().size()-1, gf.getGenericViewForm().getTableModel().getEntities().size()-1);
			gf.getTable().scrollRectToVisible(new Rectangle(0, gf.getTable().getRowHeight()*(gf.getTable().getSelectedRow()),gf.getTable().getWidth(),gf.getTable().getRowHeight()));
		}
	}
}
