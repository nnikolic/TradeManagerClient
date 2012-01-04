package actions.generic;

import generic.MessagePanel;
import generic.form.FormStateEnum;
import generic.form.GenericForm;
import generic.tools.MessageObject;
import generic.tools.PopupManager;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import util.ServerResponse;

import localization.Local;
import actions.CustomAbstractAction;
import app.Appliction;

public class DoSearchAction extends CustomAbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4458988219668259021L;

	public DoSearchAction() {
		putValue(NAME, Local.getString("SEARCH"));
		putValue(SHORT_DESCRIPTION, Local.getString("SEARCH_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("search.png"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GenericForm gf = Appliction.getInstance().getCurrentForm();
		
		MessageObject validateObject =	gf.getGenericSearchForm().validateInput();
		if(validateObject.getSeverity()!=MessageObject.NONE){
			PopupManager.showMessage(validateObject, gf);
			return;
		}
		
		ServerResponse response = gf.getGenericSearchForm().search();
		MessagePanel messPanel = new MessagePanel(Local.getString(response.getResponseCode()), response.getResponseMessage());
		if(response.getSeverity()==ServerResponse.INFO){
			//JOptionPane.showMessageDialog(gf, messPanel, Local.getString("INFO"), JOptionPane.INFORMATION_MESSAGE);
			gf.getGenericViewForm().getTableModel().setEntities((List<Object>) response.getData());
			gf.setState(FormStateEnum.ViewState);
		}else{
			JOptionPane.showMessageDialog(gf, messPanel, Local.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
		}
	}
}
