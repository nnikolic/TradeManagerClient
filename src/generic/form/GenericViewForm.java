package generic.form;

import generic.MessagePanel;
import generic.events.LookupEvent;
import generic.form.printProcessors.GenericPrintProcessor;
import generic.listeners.LookupListener;
import generic.tablemodel.GenericTableModel;
import generic.tablerenderers.BooleanRenderer;
import generic.tablerenderers.DateRenderer;
import generic.tablerenderers.DoubleRenderer;
import generic.tablerenderers.IntegerRenderer;
import generic.tablerenderers.PriceRenderer;
import generic.tablerenderers.StringRenderer;
import hibernate.facades.MetadataFacade;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;

import localization.Local;
import model.metadata.ChildMetadata;
import model.metadata.EntityField;
import model.metadata.EntityMetadata;
import model.metadata.SiblingMetadata;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import remotes.RemotesManager;
import util.ServerResponse;
import utils.ReflectUtil;
import actions.ActionManager;
import app.Appliction;

public class GenericViewForm extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6800781024536335697L;
	private GenericFormToolbar toolbar = null;
	private JTable table = null;
	private JPanel statusPanel = null;
	private GenericTableModel tableModel = null;
	private FormType formType;

	private List<LookupListener> lookupListeners = null;

	public GenericViewForm(EntityMetadata em, FormType ft) {
		formType = ft;
		lookupListeners = new ArrayList<LookupListener>();
		init(em);
	}

	public void init(EntityMetadata em) {
		initComponents(em);
		setLayout(new BorderLayout());
		add(toolbar, BorderLayout.NORTH);
		add(new JScrollPane(table));
		add(statusPanel, BorderLayout.SOUTH);
	}

	public void addLookupListener(LookupListener ll) {
		lookupListeners.add(ll);
	}

	public JTable getTable() {
		return table;
	}

	public GenericTableModel getTableModel() {
		return tableModel;
	}
	
	public void loadData(FormType formType, EntityMetadata parentEntity,
			Object parent, String childId, GenericForm parentForm,
			boolean filteredLoad, Map<String, Object> filterMap) {
		ServerResponse dataResponse = null;
		try {
			if (formType == FormType.Normal) {
				if (filteredLoad) {
					dataResponse = RemotesManager.getInstance()
							.getGenericPersistenceRemote().searchSelect(
									tableModel.getEntity(), filterMap,
									toolbar.getMaxRowCount(), "id", null);
				} else {
					dataResponse = RemotesManager.getInstance()
							.getGenericPersistenceRemote().selectEntities(
									Class.forName(tableModel.getEntity()
											.getEntityClassPath()),
									toolbar.getMaxRowCount(), "id", null);
				}
				if (dataResponse.getSeverity() != ServerResponse.INFO) {
					throw new Exception("GenericViewForm - podaci nisu ucitani");
				}
				tableModel.setEntities((ArrayList<Object>) dataResponse
						.getData());
			} else if (formType == FormType.Zoom) {
				if (filteredLoad) {
					dataResponse = RemotesManager.getInstance()
							.getGenericPersistenceRemote().searchSelect(
									tableModel.getEntity(), filterMap,
									toolbar.getMaxRowCount(), "id", null);
				} else {
					dataResponse = RemotesManager.getInstance()
							.getGenericPersistenceRemote().selectEntities(
									Class.forName(tableModel.getEntity()
											.getEntityClassPath()),
									toolbar.getMaxRowCount(), "id", null);
				}
				if (dataResponse.getSeverity() != ServerResponse.INFO) {
					throw new Exception("GenericViewForm - podaci nisu ucitani");
				}
				tableModel.setEntities((ArrayList<Object>) dataResponse
						.getData());
			} else if (formType == FormType.Next) {
				String getter = "get" + parentEntity.getPrimKeyFieldName();
				Method m = ReflectUtil.getMethod(parent, getter);
				Object id = null;
				try {
					id = m.invoke(parent);
					String primKeyField = parentEntity.getPrimKeyFieldDBName();
					ChildMetadata childMeta = parentEntity
							.getChildMetadataById(childId);
					String fetchName = childMeta.getEntityName();
					if (fetchName.length() > 0) {
						fetchName = (fetchName.charAt(0) + "").toLowerCase()
								+ fetchName.substring(1);
					}
					dataResponse = RemotesManager.getInstance()
							.getGenericPersistenceRemote().selectFetchEntity(
									Class.forName(parentEntity
											.getEntityClassPath()),
									primKeyField, id,
									new String[] { fetchName });
					if (dataResponse.getSeverity() != ServerResponse.INFO) {
						throw new Exception(
								"GenericViewForm - podaci nisu ucitani");
					}
					parent = dataResponse.getData();
					parentForm.setParentObj(parent);
					getter = "get" + childMeta.getEntityName();
					m = ReflectUtil.getMethod(parent, getter);
					tableModel.setEntities((Collection<Object>) m
							.invoke(parent));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} else {

			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			if (dataResponse.getSeverity() != ServerResponse.INFO) {
				MessagePanel messPanel = new MessagePanel(Local
						.getString(dataResponse.getResponseCode()),
						dataResponse.getResponseMessage());
				JOptionPane.showMessageDialog(this, messPanel, Local
						.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public GenericFormToolbar getToolbar() {
		return toolbar;
	}

	private void doLookupAction() {
		int selectedIndex = getTable().getSelectedRow();
		Object selectedItem = getTableModel().getEntities().get(selectedIndex);

		if (lookupListeners.size() > 0) {
			LookupEvent le = new LookupEvent(this, selectedItem, Appliction
					.getInstance().getCurrentForm().getSiblingId());
			for (LookupListener ll : lookupListeners) {
				ll.lookupComplete(le);
			}
			Appliction.getInstance().getMainFrame().removeLastZoomNextDialog(
					true);
			return;
		}

		GenericForm parentForm = Appliction.getInstance().getMainFrame()
				.getPenultimateForm();
		GenericForm currentForm = Appliction.getInstance().getMainFrame()
				.getCurrentForm();
		SiblingMetadata sibling = parentForm.getEntityMetadata()
				.getSiblingByID(currentForm.getSiblingId());
		Object parentObj = parentForm.getGenericInputForm().getData();
		String getter = "set" + sibling.getSiblingName();
		Method m = ReflectUtil.getMethod(parentObj, getter);
		try {
			m.invoke(parentObj, selectedItem);
			Appliction.getInstance().getMainFrame().removeLastZoomNextDialog(
					true);
			parentForm.getGenericInputForm().populateData(parentObj);
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
	}
	
	public void print(){
		if(Appliction.getInstance().getCurrentForm().getEntityMetadata().isListPrintable()){
			new Thread(){
				public void run() {
					GenericPrintProcessor process = new GenericPrintProcessor(tableModel.getEntities());
					try {
						JasperReport report = RemotesManager.getInstance().getReportingRemote().getJasperReport(Appliction.getInstance().getCurrentForm().getEntityMetadata().getEntityName()+"_list.jrxml");
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("MetadataURL", MetadataFacade.METADATA_URL);
						JasperPrint print = RemotesManager.getInstance().getReportingRemote().fillJasperReport(report, map, process);
						JasperViewer jrViewer = new JasperViewer(print, false);
						jrViewer.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
						
						jrViewer.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					} finally{
						Appliction.getInstance().getPopupProgressBar().setVisible(false);
					}
				}
			}.start();
		}else{
			Appliction.getInstance().getPopupProgressBar().setVisible(false);
		}
	}

	private void initComponents(EntityMetadata em) {
		statusPanel = new JPanel();
		tableModel = new GenericTableModel(em, new ArrayList<Object>(), formType);
		toolbar = new GenericFormToolbar(tableModel.getEntity());
		toolbar.setViewMode();
		table = new JTable(tableModel) {
			public TableCellRenderer getCellRenderer(int row, int col) {
				int counter = 0;
				for (EntityField ef : tableModel.getEntityFields()) {
					if (counter == col) {
						if (ef.getFieldType().equals("Date")) {
							return new DateRenderer();
						}
						if (ef.getFieldType().equals("Integer")) {
							return new IntegerRenderer();
						}
						if (ef.getFieldType().equals("Double")) {
							return new DoubleRenderer();
						}
						if (ef.getFieldType().equals("Price")) {
							return new PriceRenderer();
						}
						if (ef.getFieldType().equals("Boolean")) {
							return new BooleanRenderer();
						}
					}
					counter++;
				}
				return new StringRenderer();
				// return super.getCellRenderer(row, column)
			};

			@Override
			protected void processKeyEvent(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (table.getSelectedRow() > -1) {
						if (formType == FormType.Zoom) {
							doLookupAction();
						}
						return;
					}
				}
				super.processKeyEvent(e);
			}
		};

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (table.getSelectedRow() > -1) {
						if (formType == FormType.Zoom) {
							doLookupAction();
						} else {
							new JButton(ActionManager.getInstance()
									.getEditAction()).doClick();
						}
					}
				}
			}
		});
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(20);
	}
}
