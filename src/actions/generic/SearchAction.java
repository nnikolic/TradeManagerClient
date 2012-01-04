package actions.generic;

import generic.form.FormStateEnum;

import java.awt.event.ActionEvent;

import localization.Local;
import actions.CustomAbstractAction;
import app.Appliction;

public class SearchAction extends CustomAbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3238833158840515717L;
	public SearchAction() {
		putValue(NAME, Local.getString("SEARCH"));
		putValue(SHORT_DESCRIPTION, Local.getString("SEARCH_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("search.png"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Appliction.getInstance().getCurrentForm().setState(FormStateEnum.SearchState);
	}
}
