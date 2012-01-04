package custom.forms.stockdocuments;

import hibernate.entityBeans.StockDocument;
import hibernate.entityBeans.StockDocumentItem;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class SDPrintProcessor implements JRDataSource{
	private StockDocument doc = null;
	private boolean done = true;
	private int currentItemIdx = -1;
	
	public SDPrintProcessor(StockDocument doc){
		this.doc = doc;
	}
	
	@Override
	public Object getFieldValue(JRField field) throws JRException {
		String fieldName = field.getDescription();
		if(fieldName.equals("CCName")){
			return doc.getCompanyCode().getName();
		}else if(fieldName.equals("CCAccountNo")){
			return doc.getCompanyCode().getAccountNo();
		}else if(fieldName.equals("CCPIB")){
			return doc.getCompanyCode().getPib();
		}else if(fieldName.equals("CCTelephone")){
			return doc.getCompanyCode().getTelephone();
		}else if(fieldName.equals("CCFax")){
			return doc.getCompanyCode().getFax();
		}else if(fieldName.equals("ProductName")){
			return ((StockDocumentItem)doc.getItems().toArray()[currentItemIdx]).getName();
		}
		return "";
	}

	@Override
	public boolean next() throws JRException {
		if(currentItemIdx<doc.getItems().size()){
			currentItemIdx++;
			return true;
		}
		return false;
	}

}
