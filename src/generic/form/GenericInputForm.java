package generic.form;

import generic.components.GenericLabel;
import generic.events.LookupEvent;
import generic.form.components.LookupTextInput;
import generic.listeners.LookupListener;
import generic.tools.MessageObject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import layouts.RiverLayout;
import localization.Local;
import model.metadata.EntityField;
import model.metadata.EntityMetadata;
import model.metadata.FieldTypeEnum;
import model.metadata.SiblingMetadata;
import remotes.RemotesManager;
import util.DateUtil;
import util.EntityObject;
import util.ServerResponse;
import utils.ReflectUtil;
import actions.generic.textfieldValidators.DoubleValidator;
import actions.generic.textfieldValidators.IntegerValidator;
import app.Appliction;

import com.toedter.calendar.JDateChooser;

public class GenericInputForm extends JPanel implements GenericInputFormI, LookupListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7960573470324229961L;

	private EntityMetadata entityMetadata = null, parentEntityMeta=null;
	private ArrayList<JPanel> panels = new ArrayList<JPanel>();
	private int columns = 1;
	private Map<String, JComponent> fieldMap = null;
	private Object data = null;
	private GenericFormToolbar toolbar = null;
	private FormType formType;
	private Object parent;
	private String childId;
	private JComponent focuseComp = null;
	
	public GenericInputForm(EntityMetadata entityMetadata, EntityMetadata parentMeta, FormType ft, Object p, String childId) {
		this.entityMetadata = entityMetadata;
		parent = p;
		this.childId = childId;
		this.formType = ft;
		this.columns = entityMetadata.getInputPanelsCount();
		parentEntityMeta = parentMeta;
		setLayout(new BorderLayout());
		fieldMap = new HashMap<String, JComponent>();
		toolbar = new GenericFormToolbar(entityMetadata);
		toolbar.setEditMode();
		add(toolbar, BorderLayout.NORTH);
		
		init();
		reset();
		//add(toolbar, BorderLayout.NORTH);
	}
	
	public void init() {
		JPanel centralPanel = new JPanel(new RiverLayout());
		centralPanel.setLayout(new GridLayout(1, columns));
		for (int i = 0; i < columns; i++) {
			JPanel panel = new JPanel();
			panel.setLayout(new RiverLayout());
			panels.add(panel);
			centralPanel.add("",panel);
		}

		int count = 0, length = entityMetadata.getFields().size();
		for (EntityField ef : entityMetadata.getFields()) {
			if (ef.isGui() && !ef.isHidden()) {
				JPanel p = new JPanel(new RiverLayout(0,0));
				GenericLabel l = new GenericLabel(Local.getString(ef.getFieldLabel()));
				l.setRequired(ef.isRequired());
				l.setSize(150, 40);
				l.setPreferredSize(new Dimension(l.getSize()));
				JComponent component = null;
				if(ef.getFieldType().equals(FieldTypeEnum.DATE)){
					component = getDateChooser();
				}else if(!ef.isLookup()){
					component = getTextField(ef);
				}
				
				p.add("p left",l);
				if (ef.isLookup()) {
					SiblingMetadata sibling = entityMetadata.getSiblingByID(ef.getSiblingId());
					if(sibling!=null){
						component = new LookupTextInput(sibling.getSiblingEntityFilename(), sibling.getRealFieldName(ef.getFieldName()), ef.getFieldType(), data, sibling.getId(), this, false, null);
						((LookupTextInput)component).setLookupTextInputSize(new Dimension(350, 40));
						component.setEnabled(ef.isEditable());
						if(parentEntityMeta!=null && ef.getSiblingId().equals(parentEntityMeta.getChildMetadataById(childId).getRelId())){
							component.setEnabled(false);
						}
					}
				}
				fieldMap.put(ef.getFieldName(), component);
				p.add("tab hfill",component);
				if(ef.isRelField() && !ef.isLookup()){
					component.setEnabled(false);
				}
				panels.get((int) Math.round(Math.floor((count * columns)/length))).add(
						"br", p);
				count++;
				if(focuseComp == null && component.isEnabled()){
					focuseComp = component;
				}
			}
		}
		add(centralPanel, BorderLayout.CENTER);
	}

	/*
	 * Setuje parent objekat ako za to postoje uslovi
	 */
	private void setParentObj(){
		if(parent != null && parentEntityMeta != null){
			String setter = "set"+entityMetadata.getSiblingByID(parentEntityMeta.getChildMetadataById(childId).getRelId()).getSiblingName();
			Method m = ReflectUtil.getMethod(data, setter);
			if(m!=null){
				try {
					m.invoke(data, parent);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void populateData(Object entity) {
		this.data = entity;
		//ovo je potrebno da bi se sacuvao parent objekat ako smo dovde stigli iz Next opcije
		setParentObj();
		for (EntityField ef : entityMetadata.getFields()) {
			if (ef.isGui() && !ef.isHidden()) {
				String getter = "";
				SiblingMetadata sm = entityMetadata.getSiblingByID(ef
						.getSiblingId());
				if (ef.isRelField()) {
					getter = "get" + sm.getSiblingName();
				} else {
					getter = "get" + ef.getFieldName();
				}
				Method m = ReflectUtil.getMethod(entity, getter);
				if (m != null) {
					try {
						Object o = m.invoke(entity);
						if (ef.isRelField()) {
							if (o != null) {
								getter = "get" + sm.getRealFieldName(ef.getFieldName());
								m = ReflectUtil.getMethod(o, getter);
								o = m.invoke(o);
							}
						}
						setComponentData(o, ef);

					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void unbindData() {
		for (EntityField ef : entityMetadata.getFields()) {
			String getter = "set" + ef.getFieldName();
			Method m = ReflectUtil.getMethod(data, getter);
			if (ef.isGui() && !ef.isRelField()) {
				if (m != null) {
					try {
						m.invoke(data, ReflectUtil.getRealValue(
								ef.getFieldType(),
								fieldMap.get(ef.getFieldName())));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public Object getData() {
		return data;
	}

	@Override
	public ServerResponse saveEntity() {
		ServerResponse response = null;
		Object id = ((EntityObject)getData()).getID();
		if(formType==FormType.Normal || formType==FormType.Zoom){
			if(id==null){
				response = RemotesManager.getInstance().getGenericPersistenceRemote().insertEntity(getData());
			}else{
				response = RemotesManager.getInstance().getGenericPersistenceRemote().updateEntity(getData());
			}
		}else if(formType==FormType.Next){
			GenericForm gf = Appliction.getInstance().getCurrentForm();
			if(id==null){
				gf.addNextChild(gf.getGenericInputForm().getData());
			}
			response = RemotesManager.getInstance().getGenericPersistenceRemote().updateEntity(gf.getParentObject());
		}
		return response;
	}
	
	private void setComponentData(Object data, EntityField ef){
		if(ef.getFieldType().equals(FieldTypeEnum.DATE)){
			JDateChooser field = (JDateChooser) fieldMap.get(ef.getFieldName());
			field.setDate(data == null || data instanceof String ? new Date() : (Date) data);
		}else if(ef.getFieldType().equals(FieldTypeEnum.BOOLEAN)){
			JCheckBox check = (JCheckBox) fieldMap.get(ef.getFieldName());
			check.setSelected((Boolean) (data != null ? data: false));
		}else if(ef.isLookup()){
			LookupTextInput input = (LookupTextInput) fieldMap.get(ef.getFieldName());
			input.setText(data != null ? data.toString() : "");
		}else{
			JTextField field = (JTextField) fieldMap.get(ef.getFieldName());
			field.setText(data != null ? data.toString() : "");
		}
	}
	
	public void reset() {
		for (Entry<String, JComponent> tf : fieldMap.entrySet()) {
			EntityField ef = entityMetadata.getFieldByName(tf.getKey());
			if(ef.isRelField() && parentEntityMeta!=null){
				SiblingMetadata sibling = entityMetadata.getSiblingByID(ef.getSiblingId());
				if(parentEntityMeta.getChildMetadataById(childId).getRelId().equals(ef.getSiblingId())){
					String getter = "get"+sibling.getRealFieldName(tf.getKey());
					Method m = ReflectUtil.getMethod(parent, getter);
					if(m!=null){
						try {
							Object value = m.invoke(parent);
							setComponentData(value, ef);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
			}else{
				setComponentData("", ef);
			}
		}
	}

	private JDateChooser getDateChooser(){
		JDateChooser chooser = new JDateChooser();
		chooser.setLocale(new Locale(Local.getLocale()));
		chooser.getJCalendar().setWeekOfYearVisible(false);
		chooser.setDateFormatString(DateUtil.DATE_FORMAT);
		chooser.setSize(350, 40);
		chooser.setPreferredSize(new Dimension(350, 40));
		return chooser;
	}
	
	private JTextField getTextField(EntityField ef) {
		JTextField tf = new JTextField();
		tf.setSize(350, 40);
		tf.setPreferredSize(new Dimension(350, 40));
		tf.setEnabled(ef.isEditable());
		if(ef.getFieldType().equals("Integer")){
			tf.setDocument(new IntegerValidator());
			tf.setHorizontalAlignment(JTextField.RIGHT);
		}else if(ef.getFieldType().equals("Double") || ef.getFieldType().equals("Price")){
			tf.setDocument(new DoubleValidator());
			tf.setHorizontalAlignment(JTextField.RIGHT);
		}
		return tf;
	}

	@Override
	public MessageObject validateInput() {
		MessageObject message = new MessageObject();
		message.setSeverity(MessageObject.NONE);
		for (EntityField ef : entityMetadata.getFields()) {
			if (ef.isGui() && !ef.isHidden()) {
				JComponent comp = fieldMap.get(ef.getFieldName());
				if(comp instanceof LookupTextInput){
					if(ef.isRequired() && ((LookupTextInput)comp).getText().length()==0){
						message.setMessage("Imput empty: "+ef.getFieldName());
						message.setSeverity(MessageObject.WARN);
						message.setMessageCode("GENERIC_VALIDATE.IMPUT_EMPTY_W");
						comp.requestFocus();
						break;
					}
				}else if(comp instanceof JTextField){
					if(ef.isRequired() && ((JTextField)comp).getText().length()==0){
						message.setMessage("Imput empty: "+ef.getFieldName());
						message.setSeverity(MessageObject.WARN);
						message.setMessageCode("GENERIC_VALIDATE.IMPUT_EMPTY_W");
						comp.requestFocus();
						break;
					}
				}
			}
		}
		return message;
	}
	
	@Override
	public void print() {
		// TODO Auto-generated method stub
	}

	@Override
	public void lookupComplete(LookupEvent le) {
		unbindData();
		SiblingMetadata sibling = entityMetadata.getSiblingByID(le.getSiblindKey());
		Method m = ReflectUtil.getMethod(data, "set"+sibling.getSiblingName());
		if(m!=null){
			try {
				m.invoke(data, le.getData());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		populateData(data);
	}

	public JComponent getRequestComp() {
		return focuseComp;
	}

	public void setRequestComp(JComponent requestComp) {
		this.focuseComp = requestComp;
	}

	@Override
	public void onShow() {
		getRequestComp().requestFocus();
	}
	
}
