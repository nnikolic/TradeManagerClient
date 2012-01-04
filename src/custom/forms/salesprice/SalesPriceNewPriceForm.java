package custom.forms.salesprice;

import generic.MessagePanel;
import generic.events.ItemsSelectedEvent;
import generic.events.LookupEvent;
import generic.events.TableDataChangedEvent;
import generic.form.components.LookupTextInput;
import generic.listeners.ItemsSelectedListener;
import generic.listeners.LookupListener;
import generic.listeners.TableDataChangedListener;
import hibernate.entityBeans.Product;
import hibernate.entityBeans.SalesPrice;
import hibernate.entityBeans.SalesPriceItem;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import layouts.RiverLayout;
import localization.Local;
import utils.MathUtil;

public class SalesPriceNewPriceForm extends JPanel implements ActionListener, LookupListener, ItemsSelectedListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1368741648772690911L;

	private JLabel productIdLabel, productNameLable, wholesalePriceLabel, taxRateLabel;
	private JTextField  productNameField, wholesalePriceField, taxRateField;
	
	private JButton saveButton, resetButton;
	
	private LookupTextInput productLookup = null;
	
	private SalesPrice salesPrice;
	private SalesPriceItem salesPriceItem;
	
	private List<TableDataChangedListener> tableChangeListeners = null;
	
	public SalesPriceNewPriceForm(){
		tableChangeListeners = new ArrayList<TableDataChangedListener>();
		initComponents();
		addComponents();
	}
	
	private void reset(){
		salesPriceItem = new SalesPriceItem();
		bindData();
	}
	
	public void addTableChangeListeners(TableDataChangedListener tdcl){
		tableChangeListeners.add(tdcl);
	}
	
	public void bindData(){
		productLookup.setText(salesPriceItem.getProduct()==null?"" : Integer.toString(salesPriceItem.getProduct().getID()));
		productNameField.setText(salesPriceItem.getProduct()==null?"":salesPriceItem.getProduct().getName());
		wholesalePriceField.setText(Double.toString(salesPriceItem.getWholesalePrice()));
		taxRateField.setText(Double.toString(salesPriceItem.getTaxRate()));
	}
	
	public SalesPriceNewPriceForm(SalesPrice salesPrice, SalesPriceItem spi){
		this();
		this.salesPrice = salesPrice;
		this.salesPriceItem = spi;
		bindData();
	}
	
	private void initComponents(){
		productNameField = SalesPriceInputForm.getTextField("String", false);
		wholesalePriceField = SalesPriceInputForm.getTextField("Double", true);
		taxRateField = SalesPriceInputForm.getTextField("Double", true);
		
//		lookupBtn = new JButton(new LookupAction(
//				"Product.xml", null, "1", this));
		
		productLookup = new LookupTextInput("Product.xml", "ID", "Integer", null, "1", this, false, null);
		
		productIdLabel = new JLabel(Local.getString("SALESPRICE.NEWPRICEFORM.PRODUCTID"));
		productIdLabel.setMinimumSize(new Dimension(80, productIdLabel.getPreferredSize().height));
		productIdLabel.setPreferredSize(productIdLabel.getMinimumSize());
		productNameLable = new JLabel(Local.getString("SALESPRICE.NEWPRICEFORM.PRODUCTNAME"));
		wholesalePriceLabel = new JLabel(Local.getString("SALESPRICE.NEWPRICEFORM.WHOLASALEPRICE"));
		taxRateLabel = new JLabel(Local.getString("SALESPRICE.NEWPRICEFORM.TAXRATE"));
		
		saveButton = new JButton(Local.getString("SALESPRICE.NEWPRICEFORM.SAVEBUTTON"));
		saveButton.addActionListener(this);
		resetButton = new JButton(Local.getString("SALESPRICE.NEWPRICEFORM.RESETBUTTON"));
		resetButton.addActionListener(this);
		
		setLayout(new RiverLayout());
		setBorder(BorderFactory.createTitledBorder(Local.getString("SALESPRICE.NEWPRICEFORM.TITLE")));
	}
	
	private void addComponents(){
		add("p left", productIdLabel);
		add("tab hfill",productLookup);
		add("br", productNameLable);
		add("tab hfill",productNameField);
		add("br", wholesalePriceLabel);
		add("tab hfill",wholesalePriceField);
		add("br", taxRateLabel);
		add("tab hfill",taxRateField);
		add("br center", saveButton);
		add("center", resetButton);
		
	}
	
	private void unbindData(){
		BigDecimal wp, tr;
		try {
			wp = new BigDecimal(Double.parseDouble(wholesalePriceField.getText()));
		} catch (NumberFormatException e) {
			wp = new BigDecimal(0.0);
		}
		wp.setScale(2, BigDecimal.ROUND_CEILING);
		try {
			tr = new BigDecimal(Double.parseDouble(taxRateField.getText()));
		} catch (NumberFormatException e) {
			tr = new BigDecimal(0.0);
		}
		tr.setScale(2, BigDecimal.ROUND_CEILING);
		
		salesPriceItem.setWholesalePrice(wp.doubleValue());
		salesPriceItem.setTaxRate(tr.doubleValue());
	}
	
	public void setSalesPriceItem(SalesPriceItem salesPriceItem) {
		this.salesPriceItem = salesPriceItem;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource()==saveButton){
			if(salesPriceItem.getProduct() == null){
				JOptionPane.showMessageDialog(this.getParent().getParent(), new MessagePanel(Local.getString("SALESPRICE.NEWPRICEFORM.SALES_PRICES_ITEM_PRODUCT_INPUT_W"), Local.getString("Product imput is empty.")), Local.getString("WARN"), JOptionPane.WARNING_MESSAGE);
				return;
			}
			salesPrice.setDirty(true);
			unbindData();
			if(!salesPrice.getSalesPriceItems().contains(salesPriceItem)){
				salesPrice.getSalesPriceItems().add(salesPriceItem);
			}
			salesPriceItem = new SalesPriceItem();
			bindData();
			TableDataChangedEvent e = new TableDataChangedEvent(this);
			for(TableDataChangedListener l: tableChangeListeners){
				l.updateTable(e);
			}
		}else{
			reset();
		}
	}
	
	@Override
	public void lookupComplete(LookupEvent le) {
		Product p = (Product) le.getData();
		salesPriceItem.setProduct(p);
		bindData();
	}
	
	public void setSalesPrice(SalesPrice salesPrice) {
		this.salesPrice = salesPrice;
	}

	@Override
	public void onItemsSelected(ItemsSelectedEvent event) {
		ArrayList<Object> objects = event.getSelectedObjects();
		if(objects.size()==1){
			salesPriceItem = (SalesPriceItem)objects.get(0);
			bindData();
		}
	}
}
