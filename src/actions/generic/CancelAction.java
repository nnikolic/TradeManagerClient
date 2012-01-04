package actions.generic;

import generic.form.FormStateEnum;
import generic.form.GenericForm;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import localization.Local;
import actions.CustomAbstractAction;
import app.Appliction;

public class CancelAction extends CustomAbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3808456543715884981L;
	public CancelAction() {
		KeyStroke ks = KeyStroke.getKeyStroke((char) KeyEvent.VK_ESCAPE);
		putValue(ACCELERATOR_KEY, ks);
		putValue(NAME, Local.getString("CANCEL"));
		putValue(SHORT_DESCRIPTION, Local.getString("CANCEL_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("no.png"));
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		GenericForm currForm = Appliction.getInstance().getCurrentForm();
		if(currForm == null){
			return;
		}
		if(currForm.getState() == FormStateEnum.ViewState){
			return;
		}
		currForm.setState(FormStateEnum.ViewState);
	}
}
