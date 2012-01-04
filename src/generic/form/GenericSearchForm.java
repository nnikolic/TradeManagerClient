package generic.form;

import generic.tools.MessageObject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import layouts.RiverLayout;
import localization.Local;
import model.metadata.EntityField;
import model.metadata.EntityMetadata;
import model.metadata.FieldTypeEnum;
import remotes.RemotesManager;
import util.DateUtil;
import util.ServerResponse;

public class GenericSearchForm extends JPanel implements GenericSearchFormI{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7455915763174566537L;
	
	private EntityMetadata entityMetadata = null;

	private ArrayList<JPanel> panels = new ArrayList<JPanel>();
	private int columns = 1;
	private Map<String, JComponent> fieldMap = null;
	private GenericFormToolbar toolbar = null;
	private FormType formType;

	private Map<String, Object> defaultSearchMap = null;
	 
	public GenericSearchForm(EntityMetadata em, FormType formType){
		this.entityMetadata = em;
		this.formType = formType;
		iniComponents();
		addComponents();
	}
	
	public void setDefaultSearchMap(Map<String, Object> defaultSearchMap) {
		this.defaultSearchMap = defaultSearchMap;
	}
	
	private void iniComponents(){
		
		defaultSearchMap = new HashMap<String, Object>();
		
		setLayout(new BorderLayout());
		
		toolbar = new GenericFormToolbar(entityMetadata);
		toolbar.setSearchMode();
		
		fieldMap = new HashMap<String, JComponent>();
		
		for(int i=0; i<columns; i++){
			JPanel panel = new JPanel();
			panel.setLayout(new RiverLayout());
			panels.add(panel);
		}
		
		int counter = 0, length = entityMetadata.getFields().size();
		for(EntityField ef: entityMetadata.getFields()){
			if(ef.isSearchable()){
				JPanel p = new JPanel(new BorderLayout());
				JLabel l = new JLabel();
				l.setText(Local.getString(ef.getFieldLabel()));
				l.setSize(130, 40);
				l.setPreferredSize(new Dimension(l.getSize()));
				JComponent comp = null;
				if(ef.getFieldType().equals(FieldTypeEnum.BOOLEAN)){
					comp = new JCheckBox();
				}else{
					comp = getTextField(ef);
				}
				fieldMap.put(ef.getFieldName(), comp);
				p.add(l, BorderLayout.WEST);
				p.add(comp, BorderLayout.CENTER);
				panels.get((int) Math.round(Math.floor((counter * columns)/length))).add(
						"br", p);
				counter++;
			}
		}
	}
	
	private void addComponents(){
		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new GridLayout(1, columns));
		for(JPanel p: panels){
			centralPanel.add(p);
		}
		add(toolbar, BorderLayout.NORTH);
		add(centralPanel, BorderLayout.CENTER);
	}
	
	
	@Override
	public ServerResponse search() {
		Map<String, Object> searchMap = new HashMap<String, Object>();
		
		for(String k: defaultSearchMap.keySet()){
			searchMap.put(k, defaultSearchMap.get(k));
		}
		
		for(String k: fieldMap.keySet()){
			EntityField ef = entityMetadata.getFieldByName(k);
			String text = "";
			if(ef.getFieldType().equals(FieldTypeEnum.BOOLEAN)){
				text = ((JCheckBox)fieldMap.get(k)).isSelected() ? "true":"false";
			}else{
				text = ((JTextField)fieldMap.get(k)).getText();
			}
			if(text.length()>0)
				searchMap.put(ef.getFieldName(), text);
		}
		
		ServerResponse response = RemotesManager.getInstance().getGenericPersistenceRemote().searchSelect(entityMetadata, searchMap, toolbar.getMaxRowCount(), "id", null);
		return response;
	}
	
	public MessageObject validateInput(){
		MessageObject message = new MessageObject();
		message.setSeverity(MessageObject.NONE);
		for(String k: fieldMap.keySet()){
			EntityField ef = entityMetadata.getFieldByName(k);
			if(!ef.getFieldType().equals(FieldTypeEnum.BOOLEAN)){
				String s = ((JTextField)fieldMap.get(k)).getText();
				if(s.length()>0){
					if(ef.getFieldType().equals(FieldTypeEnum.DATE)){
						if(s.startsWith(">=") || s.startsWith("<=")){
							s=s.substring(2);
						}else if(s.startsWith(">") || s.startsWith("<") || s.startsWith("=")){
							s=s.substring(1);
						}
						try {
							new SimpleDateFormat(DateUtil.DATE_FORMAT).parse(s);
						} catch (Exception e) {
							message.setMessage("Search form input is incorrect.");
							message.setSeverity(MessageObject.WARN);
							message.setMessageCode("GENERICSEARCH.INPUTDATA_W");
						}
					}else if(ef.getFieldType().equals(FieldTypeEnum.STRING)){
						continue;
					}else{
						if(s.startsWith(">=") || s.startsWith("<=")){
							s=s.substring(2);
						}else if(s.startsWith(">") || s.startsWith("<") || s.startsWith("=")){
							s=s.substring(1);
						}
						try {
							if(ef.getFieldType().equals(FieldTypeEnum.INTEGER)){
								Integer.parseInt(s);
							}else if(ef.getFieldType().equals(FieldTypeEnum.DOUBLE)){
								Double.parseDouble(s);
							}
						} catch (Exception e) {
							message.setMessage("Search form input is incorrect.");
							message.setSeverity(MessageObject.WARN);
							message.setMessageCode("GENERICSEARCH.INPUTDATA_W");
						}
					}
				}
			}
		}
		return message;
	}
	
	@Override
	public void reset() {
		Object[] keys = fieldMap.keySet().toArray();
		for(Object key: keys){
			if(defaultSearchMap.get(key)!=null){
				Object data = defaultSearchMap.get(key);
				String text = "";
				if(data instanceof Integer){
					text = Integer.toString(((Integer)data));
				}else if(data instanceof Double){
					text = Double.toString(((Double)data));
				}
				if(fieldMap.get(key) instanceof JCheckBox){
					((JCheckBox)fieldMap.get(key)).setSelected((Boolean) data);
				}else{
					((JTextField)fieldMap.get(key)).setText(text);
				}
			}else{
				if(fieldMap.get(key) instanceof JCheckBox){
					((JCheckBox)fieldMap.get(key)).setSelected(false);
				}else{
					((JTextField)fieldMap.get(key)).setText("");
				}
			}
		}
	}

	public EntityMetadata getEntityMetadata() {
		return entityMetadata;
	}
	
	public void setEntityMetadata(EntityMetadata entityMetadata) {
		this.entityMetadata = entityMetadata;
	}
	
	private JTextField getTextField(EntityField ef) {
		JTextField tf = new JTextField(30);
		return tf;
	}
}
