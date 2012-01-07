package custom.forms.stockdocuments.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import localization.Local;
import remotes.RemotesManager;
import util.EntityObject;
import util.ServerResponse;
import actions.generic.LookupAction;
import app.Appliction;

import generic.MessagePanel;
import generic.events.LookupEvent;
import generic.form.components.LookupTextInput;
import generic.listeners.LookupListener;
import hibernate.entityBeans.SalesPrice;
import hibernate.entityBeans.SalesPriceItem;
import hibernate.entityBeans.StockDocument;

public class PriceLookupComponent extends LookupTextInput {
	private SalesPrice salesPrice = null;
	public PriceLookupComponent(SalesPrice sp, String entityFileName, String fieldName,
			String dataType, Object parent, String siblingId,
			LookupListener ll, boolean filteredLoad,
			Map<String, Object> searchMap) {
		super(entityFileName, fieldName, dataType, parent, siblingId, ll, filteredLoad,
				searchMap);
		this.salesPrice = sp;
	}
	
	@Override
	protected void doLookup() {
		String[] arr = new String[]{"salesPriceItems"};
		ServerResponse sr = RemotesManager.getInstance().getGenericPersistenceRemote().selectFetchEntity(salesPrice.getClass(), metadata.getPrimKeyFieldDBName(), salesPrice.getID(), arr);
		MessagePanel messPanel = new MessagePanel(Local.getString(sr.getResponseCode()), sr.getResponseMessage());
		if(sr.getSeverity() == ServerResponse.INFO){
			salesPrice = (SalesPrice) sr.getData();
			SalesPriceItem item = null;
			for(SalesPriceItem currItem : salesPrice.getSalesPriceItems()){
				if(currItem.getProduct().getCode().equals(lookupTextButton.getText())){
					item = currItem;
					break;
				}
			}
			
			if(item==null){
				lookupBtn.doClick();
			}else{
				LookupEvent le = new LookupEvent(this, item, lookupAction.getSiblingId());
				((LookupAction)lookupBtn.getAction()).getLookupListener().lookupComplete(le);
			}
		}else{
			JOptionPane.showMessageDialog(Appliction.getInstance().getMainFrame(), messPanel, Local.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void setNewPriceLookupAction(SalesPrice sp, String entityFileName, String fieldName,
			Object parent, String siblingId, LookupListener ll,
			boolean filteredLoad, Map<String, Object> searchMap) {
		// TODO Auto-generated method stub
		super.setNewLookupAction(entityFileName, fieldName, parent, siblingId, ll,
				filteredLoad, searchMap);
		this.salesPrice = sp;
	}
}
