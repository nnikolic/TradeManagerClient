package actions.generic;

import java.awt.event.ActionEvent;

import localization.Local;
import actions.CustomAbstractAction;
import app.Appliction;

public class PrintListAction extends CustomAbstractAction{
	
	public PrintListAction() {
		//putValue(NAME, Local.getString("PRINT"));
		putValue(SHORT_DESCRIPTION, Local.getString("PRINT_LIST_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("print.png"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Appliction.getInstance().getPopupProgressBar().setText(Local.getString("PRINTING"));
		Appliction.getInstance().getPopupProgressBar().setLocationRelativeTo(Appliction.getInstance().getMainFrame());
		Appliction.getInstance().getPopupProgressBar().setVisible(true);
		
		Appliction.getInstance().getPopupProgressBar().repaint();
		
		Appliction.getInstance().getCurrentForm().getGenericViewForm().print();
	}
}
