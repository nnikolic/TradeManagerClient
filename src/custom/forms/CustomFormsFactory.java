package custom.forms;

import generic.form.FormType;
import hibernate.entityBeans.SalesPrice;
import hibernate.entityBeans.StockDocument;

import javax.swing.JPanel;

import model.metadata.EntityMetadata;

import custom.forms.salesprice.SalesPriceInputForm;
import custom.forms.stockdocuments.StockDocumentInputForm;

public class CustomFormsFactory {
	public static JPanel getFormByName(String name, EntityMetadata em, Object data, FormType ft){
		if(name.equals("InsertSalesPriceForm")){
			if(data==null){
				data = new SalesPrice();
			}
			return new SalesPriceInputForm(em, (SalesPrice)data, ft);
		}else if(name.equals("InsertStockDocumentForm")){
			if(data==null){
				data = new StockDocument();
			}
			StockDocumentInputForm sdif = new StockDocumentInputForm(em, (StockDocument) data);
			return sdif;
		}
		return null;
	}
}
