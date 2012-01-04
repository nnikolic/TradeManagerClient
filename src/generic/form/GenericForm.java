package generic.form;

import generic.listeners.LookupListener;

import java.awt.CardLayout;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JTable;

import model.metadata.ChildMetadata;
import model.metadata.EntityMetadata;
import remotes.RemotesManager;
import util.EntityObject;
import util.ServerResponse;
import utils.ReflectUtil;
import custom.forms.CustomFormsFactory;

public class GenericForm extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private FormStateEnum state;
	private GenericInputForm genericInputForm = null;
	private GenericViewForm genericViewForm = null;
	private GenericSearchForm genericSearchForm = null;
	
	private GenericSearchFormI genericSearchFormI = null;
	
	private GenericInputFormI genericInputFormI = null;
	
	private FormType formType;
	private EntityMetadata parentEntity = null, entityMetadata = null;
	private Object parent = null;
	private String childId, siblingId;
	
	private boolean filteredLoad = false;
	
	private Map<String, Object> filterMap;
	
	public GenericForm(String entityClass, FormType ft, EntityMetadata pem, Object parent, String childId, String siblingId){
		this.parent = parent;
		this.childId = childId;
		this.siblingId = siblingId;
		init(entityClass, ft, pem);
		
		if(parent != null && parentEntity != null && childId != null){
			Map<String, Object> filterMap = new HashMap<String, Object>();
			filterMap.put("#"+parentEntity.getChildMetadataById(childId).getEntityName().substring(0, 1).toLowerCase()+parentEntity.getChildMetadataById(childId).getEntityName().substring(1)+"#", parent);
			setFilteredLoad(true);
			setFilterMap(filterMap);
		}
	}
	
	public boolean isFilteredLoad() {
		return filteredLoad;
	}
	
	public void setFilteredLoad(boolean filteredLoad) {
		this.filteredLoad = filteredLoad;
	}
	
	public void setFilterMap(Map<String, Object> filterMap) {
		this.filterMap = filterMap;
		genericSearchForm.setDefaultSearchMap(this.filterMap);
	}
	
	public String getSiblingId() {
		return siblingId;
	}
	
	public ServerResponse saveAction(){
		return genericInputFormI.saveEntity();
	}
	
	public void addLookupListener(LookupListener ll){
		genericViewForm.addLookupListener(ll);
	}
	
	public void init(String entityClass, FormType ft, EntityMetadata pem){
		state = FormStateEnum.ViewState;
		formType = ft;
		parentEntity = pem;
		
		initComponents(entityClass);
		setLayout(new CardLayout());
		if(!entityMetadata.isCustomGui()){
			add(genericViewForm, "ViewForm");
			add(genericInputForm, "EditForm");
			add(genericSearchForm, "SearchForm");
		}else{
			if(entityMetadata.getViewForm().equals("")){
				add(genericViewForm, "ViewForm");
			}else{
				add(CustomFormsFactory.getFormByName(entityMetadata.getViewForm(),entityMetadata, null, formType), "ViewForm");
			}
			if(entityMetadata.getInsertForm().equals("")){
				add(genericInputForm, "EditForm");
			}else{
				genericInputFormI = (GenericInputFormI) CustomFormsFactory.getFormByName(entityMetadata.getInsertForm(), entityMetadata, null, formType);
				add((JPanel)genericInputFormI, "EditForm");
			}
			
			if(entityMetadata.getSearchForm().equals("")){
				add(genericSearchForm, "SearchForm");
			}else{
				genericSearchFormI = (GenericSearchFormI) CustomFormsFactory.getFormByName(entityMetadata.getSearchForm(), entityMetadata, null, formType);
				add((JPanel)genericSearchFormI, "SearchForm");
			}
		}
		setComponentsByState();
	}
	
	public void setState(FormStateEnum fse){
		if(fse==FormStateEnum.EditState && genericViewForm.getTable().getSelectedRow()==-1){
			return;
		}
		this.state = fse;
		setComponentsByState();
	}
	
	public JTable getTable() {
		return genericViewForm.getTable();
	}
	
	public Object getParentObject(){
		return parent;
	}
	
	public void removeNextChild(Object child){
		ChildMetadata cm = parentEntity.getChildMetadataById(childId);
		String getter = "get"+cm.getEntityName();
		Method m = ReflectUtil.getMethod(parent, getter);
		Object id = null;
		try {
			Object childsObj = m.invoke(parent);
			if(childsObj instanceof Set){
				Set<Object> set = (Set<Object>)childsObj;
				set.remove(child);
			}else{
				List<Object> list = ((List<Object>)childsObj);
				list.remove(child);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public void addNextChild(Object child){
		ChildMetadata cm = parentEntity.getChildMetadataById(childId);
		String getter = "get"+cm.getEntityName();
		Method m = ReflectUtil.getMethod(parent, getter);
		Object id = null;
		try {
			Object childsObj = m.invoke(parent);
			if(childsObj instanceof Set){
				Set<Object> set = (Set<Object>)childsObj;
				set.add(child);
			}else{
				((List<Object>)childsObj).add(child);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public void setComponentsByState(){
		CardLayout cl = (CardLayout)(getLayout());
		try {
			switch (state) {
			case ViewState:
				cl.show(this, "ViewForm");
				break;
			case EditState:
				genericInputFormI.reset();
				int selectedIndex = genericViewForm.getTable().getSelectedRow();
				Object selectedObject = genericViewForm.getTableModel().getEntities().get(selectedIndex);
				String[] arr = new String[entityMetadata.getChildrenList().size()];
				int i=0;
				String fetchName = "";
				for(ChildMetadata cm: entityMetadata.getChildrenList()){
					fetchName=cm.getEntityName();
					fetchName =  (fetchName.charAt(0)+"").toLowerCase()+fetchName.substring(1);
					arr[i]=fetchName;
					i++;
				}
				ServerResponse sr = RemotesManager.getInstance().getGenericPersistenceRemote().selectFetchEntity(Class.forName(getEntityMetadata().getEntityClassPath()), getEntityMetadata().getPrimKeyFieldDBName(), ((EntityObject)selectedObject).getID(), arr);
				genericInputFormI.populateData(sr.getData());
				cl.show(this, "EditForm");
				break;
			case SearchState:
				//genericSearchFormI.reset();
				cl.show(this, "SearchForm");
				break;
			default: // insert state
					genericInputFormI.populateData(Class.forName(getEntityMetadata().getEntityClassPath()).newInstance());
					genericInputFormI.reset();
				cl.show(this, "EditForm");
				break;
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public GenericInputFormI getGenericInputForm() {
		return genericInputFormI;
	}
	
	public GenericSearchFormI getGenericSearchForm() {
		return genericSearchFormI;
	}
	
	public void setParentObj(Object parent) {
		this.parent = parent;
	}
	
	public FormType getFormType() {
		return formType;
	}
	
	public FormStateEnum getState() {
		return state;
	}
	
	public void loadData(){
		genericViewForm.loadData(formType, parentEntity, parent, childId, this, filteredLoad, filterMap);
	}
	
	public String getChildId() {
		return childId;
	}
	
	public GenericViewForm getGenericViewForm() {
		return genericViewForm;
	}
	
	public EntityMetadata getEntityMetadata() {
		return entityMetadata;
	}
	
	private void initComponents(String entityClass){
		ServerResponse metaResponse = RemotesManager.getInstance().getMetadataRemote().getEntityXml(entityClass);
		EntityMetadata em = new EntityMetadata((String)metaResponse.getData());
		entityMetadata = em;
		//tableModel = new GenericTableModel(, new ArrayList<Object>());
		if(!entityMetadata.isCustomGui()){
			genericInputForm = new GenericInputForm(em, parentEntity, formType, parent, childId);
			genericViewForm = new GenericViewForm(em, formType);
			genericInputFormI = genericInputForm;
			genericSearchForm = new GenericSearchForm(em, formType);
			genericSearchFormI = genericSearchForm;
		}else{
			if(entityMetadata.getViewForm().equals("")){
				genericViewForm = new GenericViewForm(em, formType);
			}
			if(entityMetadata.getInsertForm().equals("")){
				genericInputForm = new GenericInputForm(em, parentEntity, formType, parent, childId);
				genericInputFormI = genericInputForm;
			}
			
			if(entityMetadata.getSearchForm().equals("")){
				genericSearchForm = new GenericSearchForm(em, formType);
				genericSearchFormI = genericSearchForm;
			}
		}
	}
}
