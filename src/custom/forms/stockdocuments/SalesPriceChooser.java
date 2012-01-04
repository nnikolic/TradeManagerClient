package custom.forms.stockdocuments;

import generic.components.GenericLabel;
import generic.events.LookupEvent;
import generic.form.components.LookupTextInput;
import generic.listeners.LookupListener;
import generic.tools.MessageObject;
import hibernate.entityBeans.SalesPrice;
import hibernate.entityBeans.StockDocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import layouts.RiverLayout;
import localization.Local;
import actions.generic.LookupAction;

public class SalesPriceChooser extends JPanel implements LookupListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6926878446580214002L;
	
	private StockDocument stockDocument;
	
	private GenericLabel salesPriceIDLabel, salesPriceNameLabel;
	
	private JTextField salesPriceNameField;
	
//	private JButton lookupBtn = null;
	
	private LookupTextInput productLookup = null;
	
	private ArrayList<LookupListener> lookupListeners = null;
	
	public SalesPriceChooser(){
		lookupListeners = new ArrayList<LookupListener>();
		initComponents();
		addComponents();
	}
	
	public void addLookupListener(LookupListener ll){
		lookupListeners.add(ll);
	}
	
	private void initComponents(){
		salesPriceIDLabel = new GenericLabel(Local.getString("STOCKDOCUMENT.SALESPRICEID"));
		salesPriceNameLabel = new GenericLabel(Local.getString("STOCKDOCUMENT.SALESPRICENAME"));
		
		salesPriceIDLabel.setRequired(true);
		salesPriceNameLabel.setRequired(true);
		
		salesPriceNameField = StockDocumentInputForm.getTextField("String", false);
	
		Map<String, Object> filtermap = new HashMap<String, Object>();
		filtermap.put("Oppened", "true");
		
		productLookup = new LookupTextInput("SalesPrice.xml", "ID", "Integer", null, "s1", this, true, filtermap);
		
//		lookupBtn =  new JButton(new LookupAction(
//				"SalesPrice.xml", null, "s1", this, true, filtermap));
		
		setLayout(new RiverLayout());
		setBorder(BorderFactory.createTitledBorder(Local.getString("STOCKDOCUMENT.TITLE")));
	}

	private void addComponents(){
		add("p left", salesPriceIDLabel);
		add("tab hfill",productLookup);
//		add("", lookupBtn);
		add("br", salesPriceNameLabel);
		add("tab hfill",salesPriceNameField);
	}
	
	public void setStockDocument(StockDocument stockDocument) {
		this.stockDocument = stockDocument;
	}
	
	public void bindData(){
		productLookup.setText(stockDocument.getSalesPrice()!=null ? stockDocument.getSalesPrice().getID().toString() : "");
		salesPriceNameField.setText(stockDocument.getSalesPrice()!=null ? stockDocument.getSalesPrice().getName() : "");
	}
	
	@Override
	public void lookupComplete(LookupEvent le) {
		stockDocument.setSalesPrice((SalesPrice) le.getData());
		for(LookupListener ll: lookupListeners){
			ll.lookupComplete(le);
		}
		bindData();
	}
	
	public MessageObject validateData(){
		MessageObject message = new MessageObject();
		message.setSeverity(MessageObject.NONE);
		
		if(stockDocument.getSalesPrice()==null){
			message.setMessage("Sales prices is null");
			message.setSeverity(MessageObject.WARN);
			message.setMessageCode("STOCKDOCUMENT.SALESPRICES_W");
		}
		
		return message;
	}
}
