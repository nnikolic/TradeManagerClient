package custom.forms.stockdocuments;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import generic.events.ItemsSelectedEvent;
import generic.events.TableDataChangedEvent;
import generic.listeners.ItemsSelectedListener;
import generic.listeners.TableDataChangedListener;
import generic.tablerenderers.DoubleRenderer;
import generic.tablerenderers.IntegerRenderer;
import generic.tablerenderers.PriceRenderer;
import generic.tablerenderers.StringRenderer;
import hibernate.entityBeans.StockDocument;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;

import localization.Local;
import actions.ActionManager;
import app.Appliction;

public class StockDocumentItemsPanel extends JPanel implements ActionListener, 
		TableDataChangedListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9050764076853396527L;

	private DocumentItemsTableModel tableModel = null;

	private StockDocument stockDocument = null;

	private JTable table = null;
	
	private JButton deleteButton = null;

	private JToolBar toolbar = null;


	private ArrayList<ItemsSelectedListener> itemsSelectedListeners = null;
	
	private List<TableDataChangedListener> tableChangeListeners = null;

	public StockDocumentItemsPanel() {
		itemsSelectedListeners = new ArrayList<ItemsSelectedListener>();
		tableChangeListeners = new ArrayList<TableDataChangedListener>();
	}

	public StockDocumentItemsPanel(StockDocument stockDocument) {
		this();
		this.stockDocument = stockDocument;
		initComponents();
		addComponents();
	}
	
	public void addItemsSelectedListener(ItemsSelectedListener listener) {
		itemsSelectedListeners.add(listener);
	}
	
	public void addTableDataChangedListener( TableDataChangedListener tdcl){
		tableChangeListeners.add(tdcl);
	}

	private void initComponents() {
		tableModel = new DocumentItemsTableModel(stockDocument);
		final PriceRenderer pr = new PriceRenderer();
		final DoubleRenderer dr1 = new DoubleRenderer();
		final DoubleRenderer dr2 = new DoubleRenderer();
		final DoubleRenderer dr3 = new DoubleRenderer();
		final StringRenderer sr1 = new StringRenderer();
		final StringRenderer sr2 = new StringRenderer();
		final StringRenderer sr3 = new StringRenderer();
		table = new JTable(tableModel){
			public TableCellRenderer getCellRenderer(int row, int col) {
				if(col==0){
					return sr1;
				}
				if(col==1){
					return sr2;
				}
				if(col==2){
					return dr1;
				}
				if(col==3){
					return dr2;
				}
				if(col==4){
					return dr3;
				}
				if(col==5){
					return pr;
				}
				return sr3;
			}
		};
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);

		ListSelectionListener lsl = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				ArrayList<Object> objects = new ArrayList<Object>();
				Object[] allObjects = tableModel.getStockDocument().getItems()
						.toArray();
				int first = e.getFirstIndex();
				int last = e.getLastIndex();
				if (first >= 0 && last >= 0) {
					for (int i = first; i <= last; i++) {
						if (lsm.isSelectedIndex(i))
							objects.add(allObjects[i]);
					}
					ItemsSelectedEvent event = new ItemsSelectedEvent(this,
							objects);
					for (ItemsSelectedListener l : itemsSelectedListeners) {
						l.onItemsSelected(event);
					}
				}
			}
		};

		table.getSelectionModel().addListSelectionListener(lsl);
		
		deleteButton = new JButton(ActionManager.getInstance().getAddAction()
				.loadIcon("delete.png"));
		deleteButton.addActionListener(this);
		deleteButton.setToolTipText(Local
				.getString("STOCKDOCUMENTITEM.DELETEITEMTOOLTIP"));

		
		toolbar = new JToolBar();
		toolbar.add(deleteButton);
		toolbar.setFloatable(false);

		setLayout(new BorderLayout());
	}

	public void setStockDocument(StockDocument stockDocument) {
		this.stockDocument = stockDocument;
	}

	public void bindData() {
		tableModel.setStockDocument(stockDocument);
		tableModel.fireTableDataChanged();
	}

	private void addComponents() {
		JScrollPane sp = new JScrollPane(table);
		add(toolbar, BorderLayout.NORTH);
		add(sp, BorderLayout.CENTER);
	}

	@Override
	public void updateTable(TableDataChangedEvent tbce) {
		tableModel.fireTableDataChanged();
		for(TableDataChangedListener tdcl: tableChangeListeners){
			tdcl.updateTable(tbce);
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == deleteButton) {
			int[] selectedRows = table.getSelectedRows();
			Object[] items = stockDocument.getItems().toArray();
			if (selectedRows.length > 0) {
				Object[] options = { Local.getString("YES"),
						Local.getString("NO") };
				int result = JOptionPane
						.showOptionDialog(Appliction.getInstance()
								.getMainFrame(), Local
								.getString("DELETE_YES_NO_QUESTION"), Local
								.getString("CONFIRMATION"),
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[0]);
				if (result == JOptionPane.YES_OPTION) {
					for (int i = selectedRows.length - 1; i >= 0; i--) {
						stockDocument.getItems().remove(
								items[selectedRows[i]]);
					}
				}
				tableModel.fireTableDataChanged();
				TableDataChangedEvent tdce = new TableDataChangedEvent(this);
				for(TableDataChangedListener tdcl: tableChangeListeners){
					tdcl.updateTable(tdce);
				}
			}
		}
	}
}
