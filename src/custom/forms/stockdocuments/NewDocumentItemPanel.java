package custom.forms.stockdocuments;

import generic.events.ItemsSelectedEvent;
import generic.events.LookupEvent;
import generic.events.TableDataChangedEvent;
import generic.form.components.LookupTextInput;
import generic.listeners.ItemsSelectedListener;
import generic.listeners.LookupListener;
import generic.listeners.TableDataChangedListener;
import hibernate.entityBeans.SalesPriceItem;
import hibernate.entityBeans.StockDocument;
import hibernate.entityBeans.StockDocumentItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import layouts.RiverLayout;
import localization.Local;
import model.metadata.EntityMetadata;

public class NewDocumentItemPanel extends JPanel implements LookupListener, ActionListener, ItemsSelectedListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4701411776603225343L;
	
	private JLabel priceIDLabel, productNameLabel, quantityLabel, discountLabel;
	
	private JTextField  productNameField, quantityField, discountField; //priceIDField;
	
	private List<TableDataChangedListener> tableChangeListeners = null;
	
	private JButton saveButton, resetButton;//, lookupBtn = null;
	
	private EntityMetadata metadata = null;
	
	private StockDocument stockDocument = null;
	
	private StockDocumentItem currItem = null;
	
	private LookupTextInput lookupComp = null;
	
	private Double discount;
	
	
	public NewDocumentItemPanel(StockDocument sd, EntityMetadata em){
		discount = 0.0;
		metadata = em;
		this.stockDocument = sd;
		tableChangeListeners = new ArrayList<TableDataChangedListener>();
		initComponents();
		addComponents();
	}
	
	private void initComponents(){
		priceIDLabel = new JLabel(Local.getString("STOCKDOCUMENT.NEWITEMFORM.PRICEID"));
		productNameLabel = new JLabel(Local.getString("STOCKDOCUMENT.NEWITEMFORM.PRODUCTNAME"));
		quantityLabel = new JLabel(Local.getString("STOCKDOCUMENT.NEWITEMFORM.QUANTITY"));
		discountLabel = new JLabel(Local.getString("STOCKDOCUMENT.NEWITEMFORM.DISCOUNT_LABEL"));
		
//		lookupAction = new LookupAction(
//				"SalesPriceItem.xml", null, "price", this);
		lookupComp = new LookupTextInput("SalesPriceItem.xml", "ID", "Integer", null, "price", this, false, null);
		lookupComp.setEnabled(false);
//		lookupBtn = new JButton(lookupAction);
//		lookupBtn.setEnabled(false);
		
//		priceIDField = StockDocumentInputForm.getTextField("Integer", true);
		productNameField = StockDocumentInputForm.getTextField("String", false);
		quantityField = StockDocumentInputForm.getTextField("Double", true);
		discountField = StockDocumentInputForm.getTextField("Double", true);
		
		saveButton = new JButton(Local.getString("STOCKDOCUMENT.NEWITEMFORM.SAVEBUTTON"));
		saveButton.addActionListener(this);
		resetButton = new JButton(Local.getString("STOCKDOCUMENT.NEWITEMFORM.RESETBUTTON"));
		resetButton.addActionListener(this);
		
		currItem = new StockDocumentItem();
		
		setLayout(new RiverLayout());
		setBorder(BorderFactory.createTitledBorder(Local.getString("STOCKDOCUMENT.NEWITEMFORM.TITLE")));
	}
	
	public void addTableDataChangedListener(TableDataChangedListener tdcl){
		tableChangeListeners.add(tdcl);
	}
	
	private void addComponents(){
		add("p left", priceIDLabel);
		add("tab hfill",lookupComp);
		//add("", lookupBtn);
		add("br", productNameLabel);
		add("tab hfill",productNameField);
		add("br", quantityLabel);
		add("tab hfill",quantityField);
		add("br", discountLabel);
		add("tab hfill",discountField);
		add("br center", saveButton);
		add("center", resetButton);
	}
	
	public void reset(){
		currItem = new StockDocumentItem();
		discount = 0.0;
		bindData();
	}
	
	public void setCurrItem(StockDocumentItem currItem) {
		this.currItem = currItem;
	}
	
	public void bindData(){
		lookupComp.setText(currItem.getSalesPriceItem()!=null? Integer.toString(currItem.getSalesPriceItem().getID()) : "");
		productNameField.setText(currItem.getSalesPriceItem()!=null? currItem.getSalesPriceItem().getProduct().getName() : "");
		quantityField.setText(Double.toString(currItem.getQuantity()));
		discountField.setText(Double.toString(discount));
	}
	
	private void unbindData(){
		try{
			discount = Double.parseDouble(discountField.getText());
		}catch (Exception e) {}
		currItem.setQuantity(Double.parseDouble(quantityField.getText()));
		currItem.setDiscount(discount);
	}
	
	public void setStockDocument(StockDocument stockDocument) {
		this.stockDocument = stockDocument;
		if(stockDocument != null && stockDocument.getSalesPrice()!=null){
			lookupComp.setEnabled(true);
		}
	}

	@Override
	public void lookupComplete(LookupEvent le) {
		if(le.getSiblindKey().equals("price")){
			currItem.setSalesPriceItem((SalesPriceItem) le.getData());
			bindData();
			quantityField.selectAll();
			quantityField.requestFocus();
		}else{
			Map<String, Object> searchMap = new HashMap<String, Object>();
			searchMap.put("#salesPriceItems#", stockDocument.getSalesPrice());
			lookupComp.setNewLookupAction("SalesPriceItem.xml", "ID", null, "price", this, true, searchMap);
			lookupComp.setEnabled(true);
//			lookupBtn.setEnabled(true);
//			lookupBtn.setAction(lookupAction);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==resetButton){
			currItem = new StockDocumentItem();
		}else{
			unbindData();
			if(currItem.getSalesPriceItem()!=null){
				currItem.calculate();
				stockDocument.getItems().add(currItem);
				TableDataChangedEvent tdce = new TableDataChangedEvent(this);
				for(TableDataChangedListener tdcl : tableChangeListeners){
					tdcl.updateTable(tdce);
				}
				currItem = new StockDocumentItem();
			}
		}
		bindData();
	}

	@Override
	public void onItemsSelected(ItemsSelectedEvent event) {
		ArrayList<Object> objects = event.getSelectedObjects();
		if(objects.size()==1){
			currItem = (StockDocumentItem)objects.get(0);
			discount = currItem.getDiscount();
			bindData();
		}
	}
}
