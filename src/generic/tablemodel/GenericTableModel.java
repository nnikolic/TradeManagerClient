package generic.tablemodel;

import generic.form.FormType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import app.Appliction;

import localization.Local;
import model.metadata.EntityField;
import model.metadata.EntityMetadata;
import model.metadata.SiblingMetadata;
import utils.ReflectUtil;

public class GenericTableModel extends AbstractTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EntityMetadata entity = null;
	private List<EntityField> entityFields = null;
	private List<Object> entities = null;
	
	public GenericTableModel(EntityMetadata em, ArrayList<Object> data, FormType formType){
		this.entity = em;
		entityFields = new ArrayList<EntityField>();
		for(EntityField ef: entity.getFields()){
			if(!ef.isHidden() && ef.isGui()){
				if(formType == FormType.Zoom){
					if(ef.isPopupDislpayList())
						entityFields.add(ef);
				}else if(ef.isDisplayList()){
					entityFields.add(ef);
				}
			}
		}
		entities = data;
	}
	
	@Override
	public String getColumnName(int column) {
		return Local.getString(entityFields.get(column).getFieldLabel());
	}
	
	@Override
	public int getColumnCount() {
		return entityFields.size();
	}

	@Override
	public int getRowCount() {
		return entities!=null ? entities.size() : 0;
	}
	
	@Override
	public Object getValueAt(int v, int k) {
		String getter = "";
		SiblingMetadata sm = entity.getSiblingByID(entityFields.get(k).getSiblingId());
		if (entityFields.get(k).isRelField()) {
			getter = "get" + sm.getSiblingName();
		} else {
			getter = "get" + entityFields.get(k).getFieldName();
		}
		Method method = ReflectUtil.getMethod(entities.get(v), getter);
		if(method!=null){
			try {
				Object o = method.invoke(entities.get(v));
				if(entityFields.get(k).isRelField()){
					if (o != null) {
						getter = "get" + sm.getRealFieldName(entityFields.get(k).getFieldName());
						method = ReflectUtil.getMethod(o, getter);
						o = method.invoke(o);
					}
				}
				return o == null ? "" : o;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	public EntityMetadata getEntity() {
		return entity;
	}
	
	public List<Object> getEntities() {
		return entities;
	}
	public void setEntities(List<Object> entities) {
		this.entities = entities;
		fireTableStructureChanged();
	}
	
	public List<EntityField> getEntityFields() {
		return entityFields;
	}
	
	public void setEntities(Collection<Object> entities) {
		this.entities = new ArrayList<Object>(entities);
		fireTableStructureChanged();
	}
}
