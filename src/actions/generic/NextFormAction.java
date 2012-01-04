package actions.generic;

import generic.form.FormType;
import generic.form.GenericForm;

import java.awt.event.ActionEvent;

import localization.Local;
import util.EntityObject;
import actions.ActionManager;
import actions.CustomAbstractAction;
import app.Appliction;

public class NextFormAction extends CustomAbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2456919348513825864L;
	private String targetEntityName, parentEntityName, childId;
	public NextFormAction(String ten, String parentEntityName, String childId) {
		this.targetEntityName = ten;
		this.childId = childId;
		this.parentEntityName = parentEntityName;
		putValue(NAME, Local.getString(targetEntityName.substring(0, targetEntityName.length()-4).toUpperCase()));
		putValue(SHORT_DESCRIPTION, Local.getString("NEXT_ACTION_DESCRIPTION"));
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		GenericForm gf = Appliction.getInstance().getCurrentForm();
		int selectedIndex = gf.getTable().getSelectedRow();
		if(selectedIndex>-1){
			Object par = gf.getGenericViewForm().getTableModel().getEntities().get(selectedIndex);
			String idStr=Integer.toString(((EntityObject)par).getID()), nameStr=((EntityObject)par).getName();
			String title = Local.getString(parentEntityName.toUpperCase())+" ("+Local.getString("ID")+" "+idStr+", "+nameStr+") ->"+Local.getString(targetEntityName.substring(0, targetEntityName.length()-4).toUpperCase());
			GenericForm newForm = new GenericForm(targetEntityName, FormType.Next, gf.getEntityMetadata(), par, childId, "");
			newForm.loadData();
			ActionManager.getInstance().setAccelerators(newForm);
			Appliction.getInstance().getMainFrame().addNewTab(title, newForm);
		}
	}
}
