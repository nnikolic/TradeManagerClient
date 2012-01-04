package actions.generic;

import generic.form.FormType;
import generic.form.GenericForm;
import generic.listeners.LookupListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.event.AncestorListener;

import localization.Local;
import actions.ActionManager;
import actions.CustomAbstractAction;
import app.Appliction;

public class LookupAction extends CustomAbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4986749229317028219L;
	private String lookupEntityName;
	private Object parent;
	private boolean filteredLoad = false;
	private Map<String, Object> filterMap;
	private String siblingId;
	private LookupListener lookupListener=null;
	public LookupAction(String lookupEntityName, Object parent, String siblingId) {
		this.lookupEntityName = lookupEntityName;
		this.siblingId = siblingId;
		this.parent = parent;
		putValue(NAME, "...");
		putValue(SHORT_DESCRIPTION, Local.getString("LOOKUP_ACTION_DESCRIPTION"));
	}
	
	public LookupAction(String lookupEntityName, Object parent, String siblingId, LookupListener ll) {
		this(lookupEntityName, parent, siblingId);
		lookupListener = ll;
	}
	
	public LookupAction(String lookupEntityName, Object parent, String siblingId, LookupListener ll, boolean filteredLoad, Map<String, Object> searchMap) {
		this(lookupEntityName, parent, siblingId, ll);
		this.filteredLoad = filteredLoad;
		this.filterMap = searchMap;
	}
	
	public LookupListener getLookupListener() {
		return lookupListener;
	}
	
	public String getSiblingId() {
		return siblingId;
	}
	
	public Map<String, Object> getFilterMap() {
		return filterMap;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
			String title = Local.getString(lookupEntityName.substring(0, lookupEntityName.length()-4).toUpperCase());
			//Appliction.getInstance().getMainFrame().getCurrentForm().getGenericInputForm().unbindData();
			GenericForm newForm = new GenericForm(lookupEntityName, FormType.Zoom, null, null, "", siblingId);
			if(filteredLoad){
				newForm.setFilteredLoad(filteredLoad);
				newForm.setFilterMap(filterMap);
			}
			newForm.loadData();
			if(lookupListener!=null)
				newForm.addLookupListener(lookupListener);
			
			ActionManager.getInstance().setAccelerators(newForm);
			Appliction.getInstance().getMainFrame().addZoomNextDialog(newForm, title);
	}
}
