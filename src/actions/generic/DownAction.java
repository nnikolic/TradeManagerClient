package actions.generic;

import generic.form.GenericForm;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import localization.Local;
import actions.CustomAbstractAction;
import app.Appliction;

public class DownAction extends CustomAbstractAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7355635303206165102L;
	public DownAction(){
		putValue(NAME, Local.getString("DOWN"));
		putValue(SHORT_DESCRIPTION, Local.getString("DOWN_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("down.png"));
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
				if(rowCount-1==selectedIndex){
					gf.getTable().setRowSelectionInterval(0, 0);
				}else{
					gf.getTable().setRowSelectionInterval(selectedIndex+1, selectedIndex+1);
				}
				rect = new Rectangle(0, gf.getTable().getRowHeight()*(gf.getTable().getSelectedRow()),gf.getTable().getWidth(),gf.getTable().getRowHeight());
			}
			gf.getTable().scrollRectToVisible(rect);
		}
	}
}
