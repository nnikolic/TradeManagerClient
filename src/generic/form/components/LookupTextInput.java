package generic.form.components;

import generic.MessagePanel;
import generic.events.LookupEvent;
import generic.listeners.LookupListener;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import layouts.RiverLayout;
import localization.Local;
import model.metadata.EntityMetadata;
import remotes.RemotesManager;
import util.ServerResponse;
import actions.generic.LookupAction;
import actions.generic.textfieldValidators.DoubleValidator;
import actions.generic.textfieldValidators.IntegerValidator;
import app.Appliction;

public class LookupTextInput extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 399991515249344222L;
	
	protected JButton lookupBtn = null;
	
	protected JTextField lookupTextButton = null;
	
	protected LookupAction lookupAction = null;
	
	protected String fieldName = "", dataType = "";
	protected EntityMetadata metadata = null;
	
	public LookupTextInput(String entityFileName, String fieldName, String dataType, Object parent, String siblingId, LookupListener ll, boolean filteredLoad, Map<String, Object> searchMap){
		this.fieldName = fieldName;
		this.dataType = dataType;
		
		ServerResponse metaResponse = RemotesManager.getInstance().getMetadataRemote().getEntityXml(entityFileName);
		MessagePanel messPanel = new MessagePanel(Local.getString(metaResponse.getResponseCode()), metaResponse.getResponseMessage());
		if(metaResponse.getSeverity() == ServerResponse.INFO){
			metadata = new EntityMetadata((String)metaResponse.getData());
		}else{
			JOptionPane.showMessageDialog(Appliction.getInstance().getMainFrame(), messPanel, Local.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
		}
		
		lookupAction = new LookupAction(entityFileName, parent, siblingId, ll, filteredLoad, searchMap);
		initComponents();
		addComponents();
	}
	
	public void setLookupTextInputSize(Dimension d){
		lookupTextButton.setSize(d);
		lookupTextButton.setPreferredSize(d);
	}
	
	public void setEnabled(boolean enabled){
		lookupBtn.setEnabled(enabled);
		lookupTextButton.setEnabled(enabled);
	}
	
	private void initComponents(){
		setLayout(new RiverLayout(0, 0));
		
		lookupTextButton = new JTextField();
		lookupTextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(lookupTextButton.getText().length() > 0)
					doLookup();
			}
		});
//		lookupTextButton.addFocusListener(new FocusListener() {
//			
//			@Override
//			public void focusLost(FocusEvent e) {
//				if(lookupTextButton.getText().length()>0)
//					doLookup();
//			}
//			
//			@Override
//			public void focusGained(FocusEvent e) {}
//		});
		if(dataType.equals("Integer")){
			lookupTextButton.setDocument(new IntegerValidator());
			lookupTextButton.setHorizontalAlignment(JTextField.RIGHT);
		}else if(dataType.equals("Double")){
			lookupTextButton.setDocument(new DoubleValidator());
			lookupTextButton.setHorizontalAlignment(JTextField.RIGHT);
		}
		
		lookupBtn = new JButton(lookupAction);
	}
	
	public void setNewLookupAction(String entityFileName, String fieldName, Object parent, String siblingId, LookupListener ll, boolean filteredLoad, Map<String, Object> searchMap){
		this.fieldName = fieldName;
		lookupAction = new LookupAction(entityFileName, parent, siblingId, ll, filteredLoad, searchMap);
		lookupBtn.setAction(lookupAction);
	}
	
	protected void doLookup(){
		Map<String, Object> searchMap = new HashMap<String, Object>();
		if(lookupAction.getFilterMap()!=null){
			for(String key: lookupAction.getFilterMap().keySet()){
				searchMap.put(key, lookupAction.getFilterMap().get(key));
			}
		}
		searchMap.put(fieldName, "="+lookupTextButton.getText());
		ServerResponse response = RemotesManager.getInstance().getGenericPersistenceRemote().searchSelect(metadata, searchMap, 10, null, null);
		MessagePanel messPanel = new MessagePanel(Local.getString(response.getResponseCode()), response.getResponseMessage());
		if(response.getSeverity() == ServerResponse.INFO){
			List<Object> result = (List<Object>) response.getData();
			if(result==null || result.size()!=1){
				lookupBtn.doClick();
			}else{
				LookupEvent le = new LookupEvent(this, result.get(0), lookupAction.getSiblingId());
				((LookupAction)lookupBtn.getAction()).getLookupListener().lookupComplete(le);
			}
		}else{
			JOptionPane.showMessageDialog(Appliction.getInstance().getMainFrame(), messPanel, Local.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void setText(String text){
		lookupTextButton.setText(text);
	}
	
	public String getText(){
		return lookupTextButton.getText();
	}
	
	private void addComponents(){
		add("p left hfill", lookupTextButton);
		add("", lookupBtn);
	}

}
