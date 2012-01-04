package actions.generic;

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import localization.Local;
import actions.CustomAbstractAction;
import app.Appliction;

public class AboutAction  extends CustomAbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7302160095473095571L;
	
	public AboutAction() {
		putValue(NAME, Local.getString("ABOUT"));
		putValue(SHORT_DESCRIPTION, Local.getString("ABOUT_ACTION_DESCRIPTION"));
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Icon icon = loadIcon("../images/About.png");
		JOptionPane.showMessageDialog(Appliction.getInstance().getMainFrame(), null, "About",
				JOptionPane.INFORMATION_MESSAGE, icon);
	}
}
