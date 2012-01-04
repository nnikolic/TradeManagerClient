package actions.generic;

import generic.MessagePanel;
import generic.form.FormStateEnum;
import generic.form.FormType;
import generic.form.GenericForm;
import hibernate.entityBeans.StockDocument;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import localization.Local;
import model.metadata.EntityMetadata;
import remotes.RemotesManager;
import util.ServerResponse;
import utils.ReflectUtil;
import actions.CustomAbstractAction;
import app.Appliction;

public class DeleteAction extends CustomAbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4230488905073808669L;

	public DeleteAction() {
		KeyStroke ks = KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE);
		putValue(ACCELERATOR_KEY, ks);
		putValue(NAME, Local.getString("DELETE"));
		putValue(SHORT_DESCRIPTION, Local.getString("DELETE_ACTION_DESCRIPTION"));
		putValue(LARGE_ICON_KEY, loadIcon("delete.png"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GenericForm gf = Appliction.getInstance().getCurrentForm();
		if(gf == null){
			return;
		}
		if(gf.getState() != FormStateEnum.ViewState){
			return;
		}
		if(gf.getGenericViewForm().getTable().getSelectedRow()<0){
			return;
		}
		Object[] options = {Local.getString("YES"),
				Local.getString("NO")};
		int result = JOptionPane.showOptionDialog(Appliction.getInstance().getMainFrame(), Local.getString("DELETE_YES_NO_QUESTION"), Local.getString("CONFIRMATION"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (result == JOptionPane.YES_OPTION){
			if(gf.getTable().getSelectedRow()>-1){
				Object entity = gf.getGenericViewForm().getTableModel().getEntities().get(gf.getTable().getSelectedRow());
				Object id = null;
				try {
					id = ReflectUtil.getMethod(entity, "getID").invoke(entity);
					
					ServerResponse response = null;
					if(gf.getFormType()==FormType.Normal || gf.getFormType()==FormType.Zoom){
						if(gf.getEntityMetadata().isCustomDelete()){
							response = callCustomDelete(gf.getEntityMetadata(), entity);
						}else{
							response = RemotesManager.getInstance().getGenericPersistenceRemote().deleteEntity(entity, id);
						}					
					}else if(gf.getFormType()==FormType.Next){
						gf.removeNextChild(entity);
						response = RemotesManager.getInstance().getGenericPersistenceRemote().updateEntity(gf.getParentObject());
					}
					MessagePanel messPanel = new MessagePanel(Local.getString(response.getResponseCode()), response.getResponseMessage());
					if(response.getSeverity()==ServerResponse.INFO){
						gf.loadData();
						JOptionPane.showMessageDialog(gf, messPanel, Local.getString("INFO"), JOptionPane.INFORMATION_MESSAGE);
					}else{
						JOptionPane.showMessageDialog(gf, messPanel, Local.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
					}
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	private ServerResponse callCustomDelete(EntityMetadata em, Object entity){
		if(em.getEntityName().equals("StockDocument")){
			return RemotesManager.getInstance().getStockDocumentRemote().deleteStockDocument((StockDocument) entity);
		}
		return null;
	}
}
