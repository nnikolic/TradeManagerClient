package actions.generic;

import generic.form.FormStateEnum;

import java.awt.event.ActionEvent;

import localization.Local;
import actions.CustomAbstractAction;
import app.Appliction;

public class ChildrenPopupAction extends CustomAbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8202664332976716151L;
	public ChildrenPopupAction() {
		//putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E,ActionEvent.ALT_MASK));
		putValue(NAME, Local.getString("CHILDREN_POPUP"));
		putValue(SHORT_DESCRIPTION, Local.getString("CHILDREN_POPUP_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("next.png"));
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Appliction.getInstance().getCurrentForm().getGenericViewForm().getToolbar().showPopup();
	}
}
