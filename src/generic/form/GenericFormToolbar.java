package generic.form;


import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import model.metadata.ChildMetadata;
import model.metadata.EntityMetadata;
import actions.ActionManager;
import actions.generic.NextFormAction;

public class GenericFormToolbar extends JToolBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int DEFAULT_ROW_COUNT = 100;
	
	private JTextField maxRowCountField = null;
	
	private EntityMetadata metadata = null;
	
	private JPopupMenu popupManu = null;
	
	private JButton nextButton = null;
	
	public GenericFormToolbar(EntityMetadata metadata){
		this.metadata = metadata;
		setFloatable(false);
		maxRowCountField = new JTextField(6);
		maxRowCountField.setText(Integer.toString(DEFAULT_ROW_COUNT));
		maxRowCountField.setAction(ActionManager.getInstance().getRowCountAction());
		
		popupManu = new JPopupMenu();
		
		nextButton = new JButton();
		
		if(metadata.getChildrenList().size()==0){
			
		}else{
			for(ChildMetadata child: metadata.getChildrenList()){
				if(child.isIncludeInNext())
					popupManu.add(new NextFormAction(child.getFileName(), metadata.getEntityName(), child.getId()));
			}
		}
	}
		
	public void showPopup(){
		if(metadata.getChildrenList().size()>0)
			popupManu.show(getComponentAtIndex(17), 0, getComponentAtIndex(17).getHeight());
	}
	
	public void setEditMode(){
		try{removeAll();}catch(Exception e){}
		add(ActionManager.getInstance().getSaveAction());
		add(ActionManager.getInstance().getCancelAction());
		if(metadata.isPrintable()){
			add(ActionManager.getInstance().getPrintDetailsAction());
		}
	}
	
	public void setSearchMode(){
		try{removeAll();}catch(Exception e){}
		add(ActionManager.getInstance().getDoSearchAction());
		add(ActionManager.getInstance().getCancelAction());
	}
	
	public void setInsertMode(){
		try{removeAll();}catch(Exception e){}
		add(ActionManager.getInstance().getSaveAction());
		add(ActionManager.getInstance().getCancelAction());
	}
	
	public void setEditability(){
		ActionManager.getInstance().getAddAction().setEnabled(metadata.isAddable());
		ActionManager.getInstance().getEditAction().setEnabled(metadata.isEditable());
		ActionManager.getInstance().getDeleteAction().setEnabled(metadata.isDeletable());
	}

	public void setViewMode(){
		try{removeAll();}catch(Exception e){}
		
		add(ActionManager.getInstance().getAddAction());
		add(ActionManager.getInstance().getEditAction());
		add(ActionManager.getInstance().getDeleteAction());
		if(metadata.isListPrintable())
			add(ActionManager.getInstance().getPrintListAction());
		if(metadata.hasSearchFields()){
			add(ActionManager.getInstance().getSearchAction());
		}
		add(Box.createHorizontalStrut(10));
		add(new JSeparator(JSeparator.VERTICAL));
		add(Box.createHorizontalStrut(10));
		add(ActionManager.getInstance().getSelectFirstAction());
		add(ActionManager.getInstance().getUpAction());
		add(ActionManager.getInstance().getDownAction());
		add(ActionManager.getInstance().getSelectLastAction());
		add(Box.createHorizontalStrut(10));
		add(new JSeparator(JSeparator.VERTICAL));
		add(Box.createHorizontalStrut(10));
		add(maxRowCountField);
		add(Box.createHorizontalStrut(10));
		add(new JSeparator(JSeparator.VERTICAL));
		add(Box.createHorizontalStrut(10));
		if(metadata.getChildrenList().size()>0){
			add(ActionManager.getInstance().getChildrenPopupAction());
		}
	}
	
	public int getMaxRowCount() {
		int mrc;
		try{
			mrc = Integer.parseInt(maxRowCountField.getText());
		}catch(Exception e){
			mrc = DEFAULT_ROW_COUNT;
		}
		return mrc;
	}
}
