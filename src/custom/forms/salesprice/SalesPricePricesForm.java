package custom.forms.salesprice;

import generic.events.ItemsSelectedEvent;
import generic.events.TableDataChangedEvent;
import generic.listeners.ItemsSelectedListener;
import generic.listeners.TableDataChangedListener;
import generic.tablerenderers.DoubleRenderer;
import generic.tablerenderers.IntegerRenderer;
import generic.tablerenderers.PriceRenderer;
import generic.tablerenderers.StringRenderer;
import generic.tools.MessageObject;
import hibernate.entityBeans.SalesPrice;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

public class SalesPricePricesForm extends JPanel implements ActionListener,
		TableDataChangedListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7160049701705922615L;
	private JTable pricesTable = null;

	private PricesTableModel tableModel = null;

	private JButton deleteButton = null;

	private JToolBar toolbar = null;

	private SalesPrice salesPrice = null;

	private ArrayList<ItemsSelectedListener> itemsSelectedListeners = null;

	public SalesPricePricesForm() {
		itemsSelectedListeners = new ArrayList<ItemsSelectedListener>();
	}

	public SalesPricePricesForm(SalesPrice salesPrice) {
		this();
		this.salesPrice = salesPrice;
		initComponents();
		addComponents();
	}

	private void addComponents() {
		JScrollPane scrollPane = new JScrollPane(pricesTable);
		add(toolbar, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
	}

	public void addItemsSelectedListener(ItemsSelectedListener listener) {
		itemsSelectedListeners.add(listener);
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		toolbar = new JToolBar();
		tableModel = new PricesTableModel(salesPrice);
		pricesTable = new JTable(tableModel){
			public TableCellRenderer getCellRenderer(int row, int col) {
				if(col==0 || col==1){
					return new IntegerRenderer();
				}
				if(col==3){
					return new PriceRenderer();
				}
				if(col==4){
					return new DoubleRenderer();
				}
				return new StringRenderer();
			}
		};
		pricesTable
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		pricesTable.getTableHeader().setReorderingAllowed(false);

		ListSelectionListener lsl = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				ArrayList<Object> objects = new ArrayList<Object>();
				Object[] allObjects = tableModel.getSalesPrice()
						.getSalesPriceItems().toArray();
				int first = e.getFirstIndex();
				int last = e.getLastIndex();
				if (first >= 0 && last >= 0) {
					for (int i = first; i <= last; i++) {
						if(lsm.isSelectedIndex(i))
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

		pricesTable.getSelectionModel().addListSelectionListener(lsl);

		deleteButton = new JButton(ActionManager.getInstance().getAddAction()
				.loadIcon("delete.png"));
		deleteButton.addActionListener(this);
		deleteButton.setToolTipText(Local
				.getString("SALESPRICE.DELETEPRICETOOLTIP"));

		toolbar.add(deleteButton);
		toolbar.setFloatable(false);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == deleteButton) {
			int[] selectedRows = pricesTable.getSelectedRows();
			Object[] prices = salesPrice.getSalesPriceItems().toArray();
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
						salesPrice.getSalesPriceItems().remove(
								prices[selectedRows[i]]);
					}
				}
				tableModel.fireTableStructureChanged();
			}
		}
	}
	
	public MessageObject validateData(){
		MessageObject message = new MessageObject();
		message.setSeverity(MessageObject.NONE);
		
		if(salesPrice.getSalesPriceItems().size()==0){
			message.setMessage("Sales prices items empty");
			message.setSeverity(MessageObject.WARN);
			message.setMessageCode("SALESPRICE.ITEMS_W");
			return message;
		}
		
		return message;
	}
	
	public void setSalesPrice(SalesPrice salesPrice) {
		this.salesPrice = salesPrice;
	}
	
	public void bindData(){
		tableModel.setSalesPrice(salesPrice);
		tableModel.fireTableStructureChanged();
	}
	
	@Override
	public void updateTable(TableDataChangedEvent tbce) {
		tableModel.fireTableStructureChanged();
	}
}
