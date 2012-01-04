package custom.forms.stockdocuments;

import hibernate.entityBeans.StockDocument;
import hibernate.entityBeans.StockDocumentItem;

import javax.swing.table.AbstractTableModel;

import localization.Local;

public class DocumentItemsTableModel extends AbstractTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2589743336273730169L;

	private StockDocument stockDocument = null;
	
	private String[] columnNames = {"STOCKDOCUMENTITEM.PRODUCTCODE", "STOCKDOCUMENTITEM.PRODUCTNAME", "STOCKDOCUMENTITEM.QUANTITY", "STOCKDOCUMENTITEM.DISCOUNT", "STOCKDOCUMENTITEM.PDVPRICE", "STOCKDOCUMENTITEM.BASICPRICE"};
	
	public DocumentItemsTableModel(StockDocument sd){
		stockDocument = sd;
	}
	
	public void setStockDocument(StockDocument stockDocument) {
		this.stockDocument = stockDocument;
	}
	
	@Override
	public String getColumnName(int column) {
		return Local.getString(columnNames[column]);
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return stockDocument.getItems().size();
	}
	
	public StockDocument getStockDocument() {
		return stockDocument;
	}

	@Override
	public Object getValueAt(int v, int k) {
		StockDocumentItem item = ((StockDocumentItem)stockDocument.getItems().toArray()[v]);
		if(k==0){
			return item.getSalesPriceItem().getProduct().getCode();	
		}
		if(k==1){
			return item.getSalesPriceItem().getProduct().getName();
		}
		if(k==2){
			return item.getQuantity();
		}
		if(k==3){
			return item.getDiscount();
		}
		if(k==4){
			return item.getPdvPrice();
		}
		if(k==5){
			return item.getBasicPrice();
		}
		return "Error";
	}

}
