package actions.generic;

import java.awt.event.ActionEvent;

import localization.Local;
import actions.CustomAbstractAction;
import app.Appliction;

public class PrintDetailsAction extends CustomAbstractAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3307938510202439720L;

	public PrintDetailsAction() {
		//putValue(NAME, Local.getString("PRINT"));
		putValue(SHORT_DESCRIPTION, Local.getString("PRINT_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("print.png"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Appliction.getInstance().getPopupProgressBar().setText(Local.getString("PRINTING"));
		Appliction.getInstance().getPopupProgressBar().setLocationRelativeTo(Appliction.getInstance().getMainFrame());
		Appliction.getInstance().getPopupProgressBar().setVisible(true);
		
		Appliction.getInstance().getPopupProgressBar().repaint();
		
		Appliction.getInstance().getCurrentForm().getGenericInputForm().print();
		
	}
}
