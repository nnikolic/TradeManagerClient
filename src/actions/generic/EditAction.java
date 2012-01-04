package actions.generic;

import generic.form.FormStateEnum;
import generic.form.FormType;
import generic.form.GenericForm;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import localization.Local;
import actions.CustomAbstractAction;
import app.Appliction;

public class EditAction extends CustomAbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7302160095473095571L;
	
	public EditAction() {
		KeyStroke ks = KeyStroke.getKeyStroke((char) KeyEvent.VK_ENTER);
		putValue(ACCELERATOR_KEY, ks);
		putValue(NAME, Local.getString("EDIT"));
		putValue(SHORT_DESCRIPTION, Local.getString("EDIT_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("edit.png"));
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		GenericForm gf = Appliction.getInstance().getCurrentForm();
		if(gf==null){
			return;
		}
		
		Appliction.getInstance().getCurrentForm().setState(FormStateEnum.EditState);
	}
}
