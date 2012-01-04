package custom.forms.stockdocuments;

import generic.components.GenericLabel;
import generic.events.LookupEvent;
import generic.form.components.LookupTextInput;
import generic.listeners.LookupListener;
import generic.tools.MessageObject;
import hibernate.entityBeans.BusinessPartner;
import hibernate.entityBeans.DocumentType;
import hibernate.entityBeans.StockDocument;
import hibernate.entityBeans.Warehouse;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import layouts.RiverLayout;
import localization.Local;
import model.custom.DocumentTypeEnum;
import remotes.RemotesManager;
import util.ServerResponse;

public class DocumentSubjectsChooser extends JPanel implements LookupListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8344776027064112618L;
	
	private JTextField inBusinessPartnerNameField, outBusinessPartnerNameField, outWarehouseNameField, inWarehouseNameField;
	private GenericLabel inwardLabel, outwardLabel, documentTypeLabel, inBusinessPartnerIDLabel, inBusinessPartnerNameLabel, outBusinessPartnerIDLabel, outBusinessPartnerNameLabel, outWarehouseIDLabel, outWarehouseNameLabel, inWarehouseIDLabel, inWarehouseNameLabel, noteLabel;
	private JTextArea noteTxtArea = null;
	
	private JPanel inputDirectionPanel = null;
	private JPanel outputDirectionPanel = null;
	
	private ComboModel comboModel = null;
	
	private JComboBox combo = null;
	
	private LookupTextInput outBusinessPartnerLookup, inBusinessPartnerLookup, inWarehouseLookup, outWarehouseLookup;
	
	private StockDocument stockDocument;
	
	public DocumentSubjectsChooser(){
		initComponents();
		addComponents();
	}
	
	private void addComponents(){
		
		JPanel ip1 = new JPanel(new RiverLayout(0, 0));
		JPanel ip2 = new JPanel(new RiverLayout(0, 0));
		JPanel op1 = new JPanel(new RiverLayout(0, 0));
		JPanel op2 = new JPanel(new RiverLayout(0, 0));
		JPanel noteTextPanel = new JPanel(new RiverLayout(0, 0));
		
		JScrollPane sp = new JScrollPane(noteTxtArea);
		sp.setPreferredSize(new Dimension(300,100));
		sp.setMaximumSize(new Dimension(300,100));
		
		noteTextPanel.add("p left vtop", noteLabel);
		noteTextPanel.add("", Box.createHorizontalStrut(10));
		noteTextPanel.add("tab hfill vfill", sp);
		
		ip1.add("p left", inBusinessPartnerIDLabel);
		ip1.add("tab hfill", inBusinessPartnerLookup);
		ip1.add("br left", inBusinessPartnerNameLabel);
		ip1.add("tab hfill", inBusinessPartnerNameField);
		
		ip2.add("p left", inWarehouseIDLabel);
		ip2.add("tab hfill", inWarehouseLookup);
		ip2.add("br left", inWarehouseNameLabel);
		ip2.add("tab hfill", inWarehouseNameField);
		
		op1.add("p left", outBusinessPartnerIDLabel);
		op1.add("tab hfill", outBusinessPartnerLookup);
		op1.add("br left", outBusinessPartnerNameLabel);
		op1.add("tab hfill", outBusinessPartnerNameField);
		
		op2.add("p left", outWarehouseIDLabel);
		op2.add("tab hfill", outWarehouseLookup);
		op2.add("br left", outWarehouseNameLabel);
		op2.add("tab hfill", outWarehouseNameField);
		
		inputDirectionPanel.add(ip1, "Business");
		inputDirectionPanel.add(ip2, "Warehouse");
		inputDirectionPanel.add(noteTextPanel, "Note");
		
		outputDirectionPanel.add(op1, "Business");
		outputDirectionPanel.add(op2, "Warehouse");
		
		//add("center", titleLabel);
		add("p left", documentTypeLabel);
		add("tab hfill",combo);
		add("br center", inwardLabel);
		add("br left hfill", inputDirectionPanel);
		add("br center", outwardLabel);
		add("br left hfill",outputDirectionPanel);
	}
	
	private void initComponents(){
		documentTypeLabel = new GenericLabel(Local.getString("DOCUMENTSUBJECTCHOOSER.DOCUMENTTYPE"));
		outBusinessPartnerIDLabel = new GenericLabel(Local.getString("DOCUMENTSUBJECTCHOOSER.PARTNERID"));
		outBusinessPartnerIDLabel.setRequired(true);
		outBusinessPartnerNameLabel = new GenericLabel(Local.getString("DOCUMENTSUBJECTCHOOSER.PARTNERNAME"));
		outBusinessPartnerNameLabel.setRequired(true);
		inBusinessPartnerIDLabel = new GenericLabel(Local.getString("DOCUMENTSUBJECTCHOOSER.PARTNERID"));
		inBusinessPartnerIDLabel.setRequired(true);
		inBusinessPartnerNameLabel = new GenericLabel(Local.getString("DOCUMENTSUBJECTCHOOSER.PARTNERNAME"));
		inBusinessPartnerNameLabel.setRequired(true);
		outWarehouseIDLabel = new GenericLabel(Local.getString("DOCUMENTSUBJECTCHOOSER.OUTWAREHOUSEID"));
		outWarehouseIDLabel.setRequired(true);
		outWarehouseNameLabel = new GenericLabel(Local.getString("DOCUMENTSUBJECTCHOOSER.OUTWAREHOUSENAME"));
		outWarehouseNameLabel.setRequired(true);
		inWarehouseIDLabel = new GenericLabel(Local.getString("DOCUMENTSUBJECTCHOOSER.INWAREHOUSEID"));
		inWarehouseIDLabel.setRequired(true);
		inWarehouseNameLabel = new GenericLabel(Local.getString("DOCUMENTSUBJECTCHOOSER.INWAREHOUSENAME"));
		inWarehouseNameLabel.setRequired(true);
		
		noteLabel = new GenericLabel(Local.getString("DOCUMENTSUBJECTCHOOSER.NOTE"));
		noteTxtArea = new JTextArea();
		noteTxtArea.setWrapStyleWord(true);
		noteTxtArea.setLineWrap(true);
		
		inwardLabel = new GenericLabel(Local.getString("DOCUMENTSUBJECTCHOOSER.INWARD")); 
		outwardLabel = new GenericLabel(Local.getString("DOCUMENTSUBJECTCHOOSER.OUTWARD"));
		
		outBusinessPartnerNameField = StockDocumentInputForm.getTextField("String", false);
		
		inBusinessPartnerNameField = StockDocumentInputForm.getTextField("String", false);
		
		outWarehouseNameField = StockDocumentInputForm.getTextField("String", false);
		
		inWarehouseNameField = StockDocumentInputForm.getTextField("String", false);
	
		outBusinessPartnerLookup = new LookupTextInput("BusinessPartner.xml", "ID", "Integer", null, "s5", this, false, null);
		inBusinessPartnerLookup = new LookupTextInput("BusinessPartner.xml", "ID", "Integer", null, "s5", this, false, null);
		
		inWarehouseLookup = new LookupTextInput("Warehouse.xml", "ID", "Integer", null, "s4", this, false, null);
		outWarehouseLookup = new LookupTextInput("Warehouse.xml", "ID", "Integer", null, "s3", this, false, null);
		
		inputDirectionPanel = new JPanel(new CardLayout(0,0));
		outputDirectionPanel = new JPanel(new CardLayout(0,0));
		
		comboModel = new ComboModel(new ArrayList<DocumentType>());
		
		combo = new JComboBox(comboModel);
		
		combo.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent ie) {
				DocumentType type = (DocumentType) ie.getItem();
				CardLayout input = (CardLayout) inputDirectionPanel.getLayout();
				CardLayout output = (CardLayout) outputDirectionPanel.getLayout();
				
				if(type.getID()==DocumentTypeEnum.INWARD){
					input.show(inputDirectionPanel, "Warehouse");
					output.show(outputDirectionPanel, "Business");
				}else if(type.getID()==DocumentTypeEnum.OUTWARD || type.getID()==DocumentTypeEnum.DISCOUNT_BILL){
					input.show(inputDirectionPanel, "Business");
					output.show(outputDirectionPanel, "Warehouse");
				}else if(type.getID()==DocumentTypeEnum.TRANSFER){
					input.show(inputDirectionPanel, "Warehouse");
					output.show(outputDirectionPanel, "Warehouse");
				}else if(type.getID()==DocumentTypeEnum.EXPENSE){
					input.show(inputDirectionPanel, "Note");
					output.show(outputDirectionPanel, "Warehouse");
				}
				stockDocument.setDocumentType(type);
			}
		});
		
		setLayout(new RiverLayout());
		setBorder(BorderFactory.createTitledBorder(Local.getString("DOCUMENTSUBJECTCHOOSER.TITLE")));
	}
	
	public void unbindData(){
		stockDocument.setNote(noteTxtArea.getText());
	}
	
	public void bindData(){
		Warehouse inW = stockDocument.getWarehouseIn();
		Warehouse outW = stockDocument.getWarehouseOut();
		BusinessPartner bp = stockDocument.getBusinessPartner();
		DocumentType dt = stockDocument.getDocumentType();
		
		noteTxtArea.setText(stockDocument.getNote());
		
		if(dt == null){
			if(comboModel.data.size()>0){
				combo.setSelectedIndex(0);
				stockDocument.setDocumentType((DocumentType) combo.getSelectedItem());
			}
		}else{
			combo.setSelectedItem(comboModel.getTypeByID(dt.getID().intValue()));
		}
		
		if(inW!=null){
			inWarehouseLookup.setText(Integer.toString(inW.getID()));
			inWarehouseNameField.setText(inW.getName());
		}else{
			inWarehouseLookup.setText("");
			inWarehouseNameField.setText("");
		}
		if(outW!=null){
			outWarehouseLookup.setText(Integer.toString(outW.getID()));
			outWarehouseNameField.setText(outW.getName());
		}else{
			outWarehouseLookup.setText("");
			outWarehouseNameField.setText("");
		}
		if(bp!=null){
			inBusinessPartnerLookup.setText(Integer.toString(bp.getID()));
			inBusinessPartnerNameField.setText(bp.getName());
			
			outBusinessPartnerLookup.setText(Integer.toString(bp.getID()));
			outBusinessPartnerNameField.setText(bp.getName());
		}else{
			inBusinessPartnerLookup.setText("");
			inBusinessPartnerNameField.setText("");
			
			outBusinessPartnerLookup.setText("");
			outBusinessPartnerNameField.setText("");
		}
	}

	@Override
	public void lookupComplete(LookupEvent le) {
		unbindData();
		if(le.getSiblindKey().equals("s5")){
			stockDocument.setBusinessPartner((BusinessPartner) le.getData());
		}else if(le.getSiblindKey().equals("s4")){
			stockDocument.setWarehouseIn((Warehouse) le.getData());
		}else if(le.getSiblindKey().equals("s3")){
			stockDocument.setWarehouseOut((Warehouse) le.getData());
		}
		bindData();
	}
	
	public void setStockDocument(StockDocument stockDocument) {
		this.stockDocument = stockDocument;
		ServerResponse sr = RemotesManager.getInstance().getGenericPersistenceRemote().selectEntities(DocumentType.class, 100, "id", null);
		List<DocumentType> types = (List<DocumentType>) sr.getData();
		comboModel.data = types;
		combo.updateUI();
	}
	
	public MessageObject validateData(){
		MessageObject message = new MessageObject();
		message.setSeverity(MessageObject.NONE);
		
		if(stockDocument.getDocumentType().getID()==DocumentTypeEnum.INWARD){
			if(stockDocument.getWarehouseIn()==null){
				message.setSeverity(MessageObject.WARN);
				message.setMessage("WarehouseIn is null");
				message.setMessageCode("STOCKDOCUMENT.WAREHOUSEIN_W");
			}else if(stockDocument.getBusinessPartner()==null){
				message.setSeverity(MessageObject.WARN);
				message.setMessage("BusinessPartner is null");
				message.setMessageCode("STOCKDOCUMENT.BUSINESSPARTNER_W");
			}else{
				stockDocument.setWarehouseOut(null);
			}
		}else if(stockDocument.getDocumentType().getID()==DocumentTypeEnum.OUTWARD || stockDocument.getDocumentType().getID()==DocumentTypeEnum.DISCOUNT_BILL){
			if(stockDocument.getWarehouseOut()==null){
				message.setSeverity(MessageObject.WARN);
				message.setMessage("WarehouseOut is null");
				message.setMessageCode("STOCKDOCUMENT.WAREHOUSEOUT_W");
			}else if(stockDocument.getBusinessPartner()==null){
				message.setSeverity(MessageObject.WARN);
				message.setMessage("BusinessPartner is null");
				message.setMessageCode("STOCKDOCUMENT.BUSINESSPARTNER_W");
			}else{
				stockDocument.setWarehouseIn(null);
			}
		}else if(stockDocument.getDocumentType().getID()==DocumentTypeEnum.TRANSFER){
			if(stockDocument.getWarehouseOut()==null){
				message.setSeverity(MessageObject.WARN);
				message.setMessage("WarehouseOut is null");
				message.setMessageCode("STOCKDOCUMENT.WAREHOUSEOUT_W");
			}else if(stockDocument.getWarehouseIn()==null){
				message.setSeverity(MessageObject.WARN);
				message.setMessage("WarehouseIn is null");
				message.setMessageCode("STOCKDOCUMENT.WAREHOUSEIN_W");
			}else{
				stockDocument.setBusinessPartner(null);
			}
		}else{
			if(stockDocument.getWarehouseOut()==null){
				message.setSeverity(MessageObject.WARN);
				message.setMessage("WarehouseOut is null");
				message.setMessageCode("STOCKDOCUMENT.WAREHOUSEOUT_W");
			}else{
				stockDocument.setBusinessPartner(null);
				stockDocument.setWarehouseIn(null);
			}
		}
		
		return message;
	}

	private class ComboModel extends AbstractListModel implements ComboBoxModel{

		private List<DocumentType> data = null;
		
		private DocumentType selectedType = null;
		
		public ComboModel(List<DocumentType> data){
			this.data = data;
		}
		
		public DocumentType getTypeByID(int id){
			for(DocumentType dt: data){
				if(dt.getID().intValue()==id){
					return dt;
				}
			}
			return null;
		}
		
		@Override
		public Object getElementAt(int i) {
			return data.get(i);
		}

		@Override
		public int getSize() {
			return data.size();
		}

		@Override
		public Object getSelectedItem() {
			return selectedType;
		}

		@Override
		public void setSelectedItem(Object anItem) {
			selectedType = (DocumentType) anItem;
		}
	}
}
