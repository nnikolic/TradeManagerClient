package actions.generic;

import generic.MessagePanel;
import generic.form.FormStateEnum;
import generic.form.GenericForm;
import generic.tools.MessageObject;
import generic.tools.PopupManager;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import localization.Local;
import util.ServerResponse;
import actions.CustomAbstractAction;
import app.Appliction;

public class SaveAction extends CustomAbstractAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8296180664197140699L;

	public SaveAction() {
		KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK);
		putValue(ACCELERATOR_KEY, ks);
		putValue(NAME, Local.getString("SAVE"));
		putValue(SHORT_DESCRIPTION, Local.getString("SAVE_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("ok.png"));
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		GenericForm gf = Appliction.getInstance().getCurrentForm();
		if(gf.getState() != FormStateEnum.EditState && gf.getState() != FormStateEnum.InsertState){
			return;
		}
		gf.getGenericInputForm().unbindData();
		try {
			ServerResponse response = null;
			MessageObject validateObject = gf.getGenericInputForm().validateInput();
			if(validateObject.getSeverity()!=MessageObject.NONE){
				PopupManager.showMessage(validateObject, gf);
				return;
			}
			response = gf.saveAction();
			MessagePanel messPanel = new MessagePanel(Local.getString(response.getResponseCode()), response.getResponseMessage());
			if(response.getSeverity()==ServerResponse.INFO){
				if (gf.getEntityMetadata().isPrintable()) {
					JOptionPane.showMessageDialog(gf, messPanel,
							Local.getString("INFO"),
							JOptionPane.INFORMATION_MESSAGE);
					Object[] options = { Local.getString("YES"),
							Local.getString("NO") };
					int result = JOptionPane.showOptionDialog(Appliction
							.getInstance().getMainFrame(), Local
							.getString("PRINT_DOCUMENT_YES_NO_QUESTION"), Local
							.getString("CONFIRMATION"),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[0]);
					if (result == JOptionPane.YES_OPTION) {
						Appliction.getInstance().getPopupProgressBar().setText(Local.getString("PRINTING"));
						Appliction.getInstance().getPopupProgressBar().setLocationRelativeTo(Appliction.getInstance().getMainFrame());
						Appliction.getInstance().getPopupProgressBar().setVisible(true);
						gf.getGenericInputForm().print();
					}
				}
				gf.loadData();
				gf.setState(FormStateEnum.ViewState);
			}else{
				JOptionPane.showMessageDialog(gf, messPanel, Local.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
			}
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} 
	}
}
