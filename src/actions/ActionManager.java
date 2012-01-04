package actions;

import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import actions.generic.AboutAction;
import actions.generic.AddAction;
import actions.generic.CancelAction;
import actions.generic.ChildrenPopupAction;
import actions.generic.DeleteAction;
import actions.generic.DoSearchAction;
import actions.generic.DownAction;
import actions.generic.EditAction;
import actions.generic.PrintDetailsAction;
import actions.generic.PrintListAction;
import actions.generic.RowCountAction;
import actions.generic.SaveAction;
import actions.generic.SearchAction;
import actions.generic.SelectFirstAction;
import actions.generic.SelectLastAction;
import actions.generic.UpAction;

/**
 * @author Nenad Nikolic, indeks: 10/06
 *
 */

public class ActionManager implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -674583262500649407L;

	private static ActionManager instance = null;
	private AddAction addAction = null;
	private EditAction editAction = null;
	private CancelAction cancelAction = null;
	private SaveAction saveAction = null;
	private DeleteAction deleteAction = null;
	private SelectFirstAction selectFirstAction = null;
	private SelectLastAction selectLastAction = null;
	private UpAction upAction = null;
	private DownAction downAction = null;
	private RowCountAction rowCountAction = null;
	private ChildrenPopupAction childrenPopupAction = null;
	private SearchAction searchAction = null;
	private DoSearchAction doSearchAction = null;
	private PrintDetailsAction printDetailsAction = null;
	private AboutAction aboutAction = null;
	private PrintListAction printListAction = null;
	
	private ActionManager() {
	}
	
	public static ActionManager getInstance(){
		if(instance == null){
			instance = new ActionManager(); 
			instance.init();
		}
		return instance;
	}
	
	public void init(){
		addAction = new AddAction();
		editAction = new EditAction();
		cancelAction = new CancelAction();
		saveAction = new SaveAction();
		deleteAction = new DeleteAction();
		selectFirstAction = new SelectFirstAction();
		selectLastAction = new SelectLastAction();
		upAction = new UpAction();
		downAction = new DownAction();
		rowCountAction = new RowCountAction();
		childrenPopupAction = new ChildrenPopupAction();
		doSearchAction = new DoSearchAction();
		searchAction = new SearchAction();
		printDetailsAction = new PrintDetailsAction();
		aboutAction = new AboutAction();
		printListAction = new PrintListAction();
	}
	
	public ChildrenPopupAction getChildrenPopupAction() {
		return childrenPopupAction;
	}
	
	public AddAction getAddAction() {
		return addAction;
	}
	
	public EditAction getEditAction() {
		return editAction;
	}
	
	public CancelAction getCancelAction() {
		return cancelAction;
	}
	
	public SaveAction getSaveAction() {
		return saveAction;
	}
	
	public DeleteAction getDeleteAction() {
		return deleteAction;
	}
	
	public SelectFirstAction getSelectFirstAction() {
		return selectFirstAction;
	}
	
	public SelectLastAction getSelectLastAction() {
		return selectLastAction;
	}
	
	public DownAction getDownAction() {
		return downAction;
	}
	
	public UpAction getUpAction() {
		return upAction;
	}
	
	public RowCountAction getRowCountAction() {
		return rowCountAction;
	}
	
	public SearchAction getSearchAction() {
		return searchAction;
	}
	
	public DoSearchAction getDoSearchAction() {
		return doSearchAction;
	}
	
	public PrintDetailsAction getPrintDetailsAction() {
		return printDetailsAction;
	}
	
	public AboutAction getAboutAction() {
		return aboutAction;
	}
	
	public PrintListAction getPrintListAction() {
		return printListAction;
	}
	
	public void setAccelerators(JPanel p){
//		JPanel p = ((JPanel)Appliction.getInstance().getMainFrame().getContentPane());
		p.registerKeyboardAction(saveAction, (KeyStroke)saveAction.getValue(CustomAbstractAction.ACCELERATOR_KEY), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		p.registerKeyboardAction(deleteAction, (KeyStroke)deleteAction.getValue(CustomAbstractAction.ACCELERATOR_KEY), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		p.registerKeyboardAction(editAction, (KeyStroke)editAction.getValue(CustomAbstractAction.ACCELERATOR_KEY), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		p.registerKeyboardAction(addAction, (KeyStroke)addAction.getValue(CustomAbstractAction.ACCELERATOR_KEY), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		p.registerKeyboardAction(cancelAction, (KeyStroke)cancelAction.getValue(CustomAbstractAction.ACCELERATOR_KEY), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}
}
