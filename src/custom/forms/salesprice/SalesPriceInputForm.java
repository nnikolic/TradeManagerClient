package custom.forms.salesprice;

import generic.form.FormType;
import generic.form.GenericFormToolbar;
import generic.form.GenericInputFormI;
import generic.form.printProcessors.GenericPrintProcessor;
import generic.tools.MessageObject;
import hibernate.entityBeans.SalesPrice;
import hibernate.entityBeans.SalesPriceItem;
import hibernate.facades.MetadataFacade;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalExclusionType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import layouts.RiverLayout;
import model.custom.DocumentTypeEnum;
import model.metadata.EntityMetadata;
import remotes.RemotesManager;
import util.ServerResponse;
import actions.generic.textfieldValidators.DoubleValidator;
import actions.generic.textfieldValidators.IntegerValidator;
import app.Appliction;

public class SalesPriceInputForm extends JPanel implements GenericInputFormI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 226018052112418429L;
	
	private SalesPrice salesPrice;
	
	private GenericFormToolbar toolbar = null;
	
	private EntityMetadata entityMetadata= null;
	
	private SalesPriceBasicInfoForm basicInfoForm = null;
	
	private SalesPriceNewPriceForm newPriceForm = null;
	
	private SalesPricePricesForm pricesForem = null;
	
	private JPanel leftPanel = null;
	
	private FormType formType;
	
	public SalesPriceInputForm(){
		initComponents();
		addComponents();
	}
	
	public SalesPriceInputForm(EntityMetadata entityMetadata, SalesPrice salesPrice, FormType ft){
		this.entityMetadata = entityMetadata;
		this.salesPrice = salesPrice;
		formType = ft;
		initComponents();
		addComponents();
	}
	
	private void initComponents(){
		toolbar = new GenericFormToolbar(entityMetadata);
		toolbar.setEditMode();
		
		leftPanel = new JPanel();
		leftPanel.setLayout(new RiverLayout());
		
		basicInfoForm = new SalesPriceBasicInfoForm(salesPrice);
		
		newPriceForm = new SalesPriceNewPriceForm(salesPrice, new SalesPriceItem());
		
		pricesForem = new SalesPricePricesForm(salesPrice);
		
		pricesForem.addItemsSelectedListener(newPriceForm);
		
		newPriceForm.addTableChangeListeners(pricesForem);
	}
	
	private void addComponents(){
		setLayout(new BorderLayout());
		
		leftPanel.add("p hfill", basicInfoForm);
		leftPanel.add("br hfill", newPriceForm);
		
		add(toolbar, BorderLayout.NORTH);
		add(leftPanel, BorderLayout.WEST);
		add(pricesForem, BorderLayout.CENTER);
	}
	
	public void reset(){
		
	}

	@Override
	public Object getData() {
		return salesPrice;
	}

	@Override
	public void populateData(Object entity) {
		salesPrice = (SalesPrice) entity;
		basicInfoForm.setSalesPrice(salesPrice);
		basicInfoForm.bindData();
		
		newPriceForm.setSalesPriceItem(new SalesPriceItem());
		newPriceForm.setSalesPrice(salesPrice);
		newPriceForm.bindData();
		
		pricesForem.setSalesPrice(salesPrice);
		pricesForem.bindData();
	}
	
	@Override
	public void unbindData() {
		basicInfoForm.unbindData();
	}
	
	@Override
	public ServerResponse saveEntity() {
		unbindData();
		ServerResponse response = null;
		if(salesPrice.isDirty())
			response = RemotesManager.getInstance().getSalesPriceRemote().updateSalesPrice(salesPrice);
		else
			response = RemotesManager.getInstance().getGenericPersistenceRemote().updateEntity(salesPrice);
		
		return response;
	}
	
	public static JTextField getTextField(String dataType, boolean enabled) {
		JTextField tf = new JTextField(25);
		tf.setEnabled(enabled);
		if(dataType.equals("Integer")){
			tf.setDocument(new IntegerValidator());
			tf.setHorizontalAlignment(JTextField.RIGHT);
		}else if(dataType.equals("Double")){
			tf.setDocument(new DoubleValidator());
			tf.setHorizontalAlignment(JTextField.RIGHT);
		}
		return tf;
	}

	@Override
	public MessageObject validateInput() {
		MessageObject message = new MessageObject();
		message.setSeverity(MessageObject.NONE);
		
		message = basicInfoForm.validateData();
		if(message.getSeverity()==MessageObject.NONE){
			message = pricesForem.validateData(); 
		}
		
		return message;
	}
	
	@Override
	public void print() {
		if(salesPrice.getID() != null){
			new Thread(){
				public void run() {
					List<Object> stockdocList = new ArrayList<Object>();
					stockdocList.add(salesPrice);
					GenericPrintProcessor process = new GenericPrintProcessor(stockdocList);
					GenericPrintProcessor itemsProcessor = new GenericPrintProcessor(new ArrayList<Object>(salesPrice.getSalesPriceItems()));
					try {
						JasperReport report = RemotesManager.getInstance().getReportingRemote().getJasperReport("SPReport.jrxml");
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("ItemsDataSource", itemsProcessor);
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
}