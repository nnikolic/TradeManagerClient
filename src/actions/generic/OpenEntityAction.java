package actions.generic;

import generic.form.FormStateEnum;
import generic.form.FormsHolder;
import generic.form.GenericForm;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import localization.Local;
import actions.ActionManager;
import actions.CustomAbstractAction;
import app.Appliction;

public class OpenEntityAction extends CustomAbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1420972673657479348L;
	private String label, entityName;
	public OpenEntityAction(String label, String entityName, String imgString) {
		putValue(NAME, Local.getString(label));
		if(imgString != null && !imgString.equals(""))
			putValue(AbstractAction.SMALL_ICON, loadIcon("menu/"+imgString));
		this.label = label;
		this.entityName = entityName;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GenericForm newForm = FormsHolder.getInstance().getFormByEntityName(entityName);
		Appliction.getInstance().getMainFrame().addNewTab(Local.getString(label), newForm);
		newForm.setState(FormStateEnum.ViewState);
		ActionManager.getInstance().setAccelerators(newForm);
		newForm.loadData();
	}
}
