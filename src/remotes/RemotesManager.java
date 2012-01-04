package remotes;

import hibernate.remotes.GenericPersistenceFacadeRemote;
import hibernate.remotes.MetadataFacadeRemote;
import hibernate.remotes.ReportingFacadeRemote;
import hibernate.remotes.SessionBeanRemote;
import hibernate.remotes.salesprice.SalesPriceFacadeRemote;
import hibernate.remotes.stockdocument.StockDocumentFacadeRemote;
import utils.BeanPool;

public class RemotesManager {
	private static RemotesManager instance = null;
	
	private SessionBeanRemote sessionRemote = null;
	private GenericPersistenceFacadeRemote genericPersistenceRemote = null;
	private MetadataFacadeRemote metadataRemote = null;
	private SalesPriceFacadeRemote salesPriceRemote = null;
	private StockDocumentFacadeRemote stockDocumentRemote = null;
	private ReportingFacadeRemote reportingRemote = null;
	
	public RemotesManager(){

		sessionRemote = BeanPool.getInstance().getBean(SessionBeanRemote.class);
		genericPersistenceRemote = BeanPool.getInstance().getBean(GenericPersistenceFacadeRemote.class);
		metadataRemote = BeanPool.getInstance().getBean(MetadataFacadeRemote.class);
		salesPriceRemote = BeanPool.getInstance().getBean(SalesPriceFacadeRemote.class);
		stockDocumentRemote = BeanPool.getInstance().getBean(StockDocumentFacadeRemote.class);
		reportingRemote = BeanPool.getInstance().getBean(ReportingFacadeRemote.class);
		
	}
	
	public static RemotesManager getInstance(){
		if(instance==null){
			instance = new RemotesManager();
		}
		return instance;
	}
	
	public SessionBeanRemote getSessionRemote() {
		return sessionRemote;
	}
	
	public GenericPersistenceFacadeRemote getGenericPersistenceRemote() {
		return genericPersistenceRemote;
	}
	
	public SalesPriceFacadeRemote getSalesPriceRemote() {
		return salesPriceRemote;
	}
	
	public StockDocumentFacadeRemote getStockDocumentRemote() {
		return stockDocumentRemote;
	}
	
	public MetadataFacadeRemote getMetadataRemote() {
		return metadataRemote;
	}
	
	public ReportingFacadeRemote getReportingRemote() {
		return reportingRemote;
	}
}
