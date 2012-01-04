package custom.forms.salesprice;

import generic.components.GenericLabel;
import generic.tools.MessageObject;
import hibernate.entityBeans.SalesPrice;

import java.awt.Dimension;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import layouts.RiverLayout;
import localization.Local;
import util.DateUtil;

import com.toedter.calendar.JDateChooser;

public class SalesPriceBasicInfoForm extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1462168885963370867L;
	
	private JTextField idField, nameField;
	private GenericLabel idLabel, nameLabel, validFromLabel, validToLabel, openedLabel;
	private JDateChooser validFromPicker = null, validToPicker;
	private JCheckBox openedCheck = null;
	
	private SalesPrice salesPrice;
	
	public SalesPriceBasicInfoForm(SalesPrice sp){
		this.salesPrice = sp;
		initComponents();
		addComponents();
	}
	
	public SalesPriceBasicInfoForm(){
		initComponents();
		addComponents();
	}
	
	public void setSalesPrice(SalesPrice salesPrice) {
		this.salesPrice = salesPrice;
		bindData();
	}
	
	private void initComponents(){
		idField = SalesPriceInputForm.getTextField("Integer", false);
		nameField = SalesPriceInputForm.getTextField("String", true);
		
		validFromPicker = new JDateChooser();
		validFromPicker.setLocale(new Locale(Local.getLocale()));
		validFromPicker.getJCalendar().setWeekOfYearVisible(false);
		validFromPicker.setDateFormatString(DateUtil.DATE_FORMAT);
		
		validToPicker = new JDateChooser();
		validToPicker.setLocale(new Locale(Local.getLocale()));
		validToPicker.getJCalendar().setWeekOfYearVisible(false);
		validToPicker.setDateFormatString(DateUtil.DATE_FORMAT);
		
		openedCheck = new JCheckBox();
		
		idLabel = new GenericLabel(Local.getString("SALESPRICE.ID"));
		idLabel.setMinimumSize(new Dimension(80, idLabel.getPreferredSize().height));
		idLabel.setPreferredSize(idLabel.getMinimumSize());
		nameLabel = new GenericLabel(Local.getString("SALESPRICE.NAME"));
		nameLabel.setRequired(true);
		validFromLabel = new GenericLabel(Local.getString("SALESPRICE.VALIDFROM"));
		validFromLabel.setRequired(true);
		validToLabel = new GenericLabel(Local.getString("SALESPRICE.VALIDTO"));
		openedLabel = new GenericLabel(Local.getString("SALESPRICE.OPENED"));
		
		setLayout(new RiverLayout());
		setBorder(BorderFactory.createTitledBorder(Local.getString("SALESPRICEFORM.TITLE")));
	}
	
	public void bindData(){
		idField.setText(salesPrice.getID()==null?"":Integer.toString(salesPrice.getID()));
		nameField.setText(salesPrice.getName());
		validFromPicker.setDate(salesPrice.getValidFrom()==null ? null:salesPrice.getValidFrom());
		validToPicker.setDate(salesPrice.getValidTo()==null ? null:salesPrice.getValidTo());
		openedCheck.setSelected(salesPrice.getOppened()==null ? true:salesPrice.getOppened());
	}
	
	public void unbindData(){
		salesPrice.setName(nameField.getText());
		salesPrice.setValidFrom(validFromPicker.getDate());
		salesPrice.setValidTo(validToPicker.getDate());
		salesPrice.setOppened(openedCheck.isSelected());
	}
	
	public MessageObject validateData(){
		MessageObject message = new MessageObject();
		message.setSeverity(MessageObject.NONE);
		
		if(salesPrice.getName().equals("")){
			message.setMessage("Sales prices name empty");
			message.setSeverity(MessageObject.WARN);
			message.setMessageCode("SALESPRICE.NAME_W");
			return message;
		}
		
		if(salesPrice.getValidFrom()==null){
			message.setMessage("Sales prices valid from empty");
			message.setSeverity(MessageObject.WARN);
			message.setMessageCode("SALESPRICE.VALIDFROM_W");
			return message;
		}
		
		return message;
	}
	
	private void addComponents(){
		add("p left", idLabel);
		add("tab hfill",idField);
		add("br", nameLabel);
		add("tab hfill",nameField);
		add("br", validFromLabel);
		add("tab hfill",validFromPicker);
		add("br", validToLabel);
		add("tab hfill",validToPicker);
		add("br", openedLabel);
		add("tab hfill",openedCheck);
	}

}