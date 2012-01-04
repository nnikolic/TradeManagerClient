package generic.tools;

import java.awt.Component;

import javax.swing.JOptionPane;

import localization.Local;
import generic.MessagePanel;

public class PopupManager {
	public static void showMessage(MessageObject mo, Component parrent){
		MessagePanel mp = new MessagePanel(Local.getString(mo.getMessageCode()), mo.getMessage());
		if(mo.getSeverity()==MessageObject.INFO){
			JOptionPane.showMessageDialog(parrent, mp, Local.getString("INFO"), JOptionPane.INFORMATION_MESSAGE);
		}else if(mo.getSeverity()==MessageObject.WARN){
			JOptionPane.showMessageDialog(parrent, mp, Local.getString("WARN"), JOptionPane.WARNING_MESSAGE);
		}else if(mo.getSeverity()==MessageObject.ERROR){
			JOptionPane.showMessageDialog(parrent, mp, Local.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
		}
	}
}
