package custom.forms.salesprice;

import hibernate.entityBeans.SalesPrice;
import hibernate.entityBeans.SalesPriceItem;

import javax.swing.table.AbstractTableModel;

import localization.Local;

public class PricesTableModel extends AbstractTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7838546388787991892L;
	private String[] columnNames = {"SALESPRICEITEM.ID", "SALESPRICEITEM.PRODUCTID", "SALESPRICEITEM.PRODUCTNAME", "SALESPRICEITEM.WHOLASALEPRICE", "SALESPRICEITEM.TAXRATE"};
	
	private SalesPrice salesPrice = null;
	
	public PricesTableModel(SalesPrice salesPrice){
		this.salesPrice = salesPrice;
	}
	
	public SalesPrice getSalesPrice() {
		return salesPrice;
	}
	
	@Override
	public String getColumnName(int i) {
		return Local.getString(columnNames[i]);
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return salesPrice.getSalesPriceItems().size();
	}

	@Override
	public Object getValueAt(int v, int k) {
		SalesPriceItem item = ((SalesPriceItem)salesPrice.getSalesPriceItems().toArray()[v]);
		if(k==0){
			return item.getID();
		}
		if(k==1){
			return item.getProduct().getID();	
		}
		if(k==2){
			return item.getProduct().getName();
		}
		if(k==3){
			return item.getWholesalePrice();
		}
		if(k==4){
			return item.getTaxRate();
		}
		return "Error";
	}

	public void setSalesPrice(SalesPrice salesPrice) {
		this.salesPrice = salesPrice;
	}
}
