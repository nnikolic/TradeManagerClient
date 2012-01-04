package actions.generic;

import generic.form.GenericForm;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import localization.Local;
import actions.CustomAbstractAction;
import app.Appliction;

public class UpAction extends CustomAbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8319374458937080824L;
	public UpAction(){
		putValue(NAME, Local.getString("UP"));
		putValue(SHORT_DESCRIPTION, Local.getString("UP_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("up.png"));
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		GenericForm gf = Appliction.getInstance().getCurrentForm();
		if(gf.getGenericViewForm().getTableModel().getEntities().size()>0){
			int selectedIndex = gf.getTable().getSelectedRow();
			Rectangle rect = null;
			if(selectedIndex==-1){
				gf.getTable().setRowSelectionInterval(0, 0);
				rect = new Rectangle(0, 0, gf.getTable().getWidth(), gf.getTable().getRowHeight());
			}else{
				int rowCount = gf.getGenericViewForm().getTableModel().getEntities().size();
				switch(selectedIndex){
					case 0:
						gf.getTable().setRowSelectionInterval(rowCount-1, rowCount-1);
						break;
					default:
						gf.getTable().setRowSelectionInterval(selectedIndex-1, selectedIndex-1);
						break;
				}
				rect = new Rectangle(0, gf.getTable().getRowHeight()*(gf.getTable().getSelectedRow()),gf.getTable().getWidth(),gf.getTable().getRowHeight());
			}
			gf.getTable().scrollRectToVisible(rect);
		}
	}
}
