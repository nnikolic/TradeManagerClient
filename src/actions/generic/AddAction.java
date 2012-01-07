package actions.generic;

import generic.form.FormStateEnum;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import localization.Local;
import actions.CustomAbstractAction;
import app.Appliction;

public class AddAction extends CustomAbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9086023992034936436L;
	public AddAction() {
		KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK);
		putValue(ACCELERATOR_KEY, ks);
		putValue(NAME, Local.getString("ADD"));
		putValue(SHORT_DESCRIPTION, Local.getString("ADD_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("add.png"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(Appliction.getInstance().getCurrentForm() == null){
			return;
		}
		if(Appliction.getInstance().getCurrentForm().getState()!=FormStateEnum.ViewState){
			return;
		}
		Appliction.getInstance().getCurrentForm().setState(FormStateEnum.InsertState);
		Appliction.getInstance().getCurrentForm().getGenericInputForm().onShow();
	}
}
