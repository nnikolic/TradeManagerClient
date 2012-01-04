package custom.forms.stockdocuments;

import java.util.ArrayList;

import hibernate.entityBeans.StockDocumentItem;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class SDIPrintProcessor implements JRDataSource{

	private ArrayList<StockDocumentItem> items;
	
	private int currentIdx = 0;
	
	public SDIPrintProcessor(ArrayList<StockDocumentItem> items){
		this.items = items;
	}
	
	@Override
	public Object getFieldValue(JRField field) throws JRException {
		StockDocumentItem item = items.get(currentIdx);
		if("ProductID".equals(field.getDescription())){
			return item.getSalesPriceItem().getProduct().getID();
		}
		if("ProductName".equals(field.getDescription())){
			return item.getSalesPriceItem().getProduct().getName();
		}
		if("Quantity".equals(field.getDescription())){
			return item.getQuantity();
		}
		if("Price".equals(field.getDescription())){
			return item.getSalesPriceItem().getWholesalePrice();
		}
		if("Total".equals(field.getDescription())){
			currentIdx++;
			return (item.getBasicPrice()+item.getPdvPrice())+"";
		}
		return null;
	}

	@Override
	public boolean next() throws JRException {
		if(currentIdx < items.size()){
			return true;
		}
		return false;
	}

}
