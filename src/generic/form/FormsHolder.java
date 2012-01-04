package generic.form;

public class FormsHolder {
	private static FormsHolder instance=null;
	
	private GenericForm productFamilyForm = null,
	productGroupForm = null, productTypeForm = null, productForm=null,
	measureUnitForm = null, warehouseTypeForm=null, warehouseForm=null,
	businessPartnerForm = null, companyCodeForm = null, documentTypeForm = null,
	salesPriceForm = null, stockDocumentForm = null, usersForm = null;
	
	public FormsHolder(){
		productFamilyForm = new GenericForm("ProductFamily.xml", FormType.Normal, null, null, "", "");
		productGroupForm = new GenericForm("ProductGroup.xml", FormType.Normal, null, null, "", "");
		productTypeForm = new GenericForm("ProductType.xml", FormType.Normal, null, null, "", "");
		productForm = new GenericForm("Product.xml", FormType.Normal, null, null, "", "");
		measureUnitForm = new GenericForm("MeasureUnit.xml", FormType.Normal, null, null, "", "");
		warehouseTypeForm = new GenericForm("WarehouseType.xml", FormType.Normal, null, null, "", "");
		warehouseForm = new GenericForm("Warehouse.xml", FormType.Normal, null, null, "", "");
		businessPartnerForm = new GenericForm("BusinessPartner.xml", FormType.Normal, null, null, "", "");
		companyCodeForm = new GenericForm("CompanyCode.xml", FormType.Normal, null, null, "", "");
		documentTypeForm = new GenericForm("DocumentType.xml", FormType.Normal, null, null, "", "");
		salesPriceForm = new GenericForm("SalesPrice.xml", FormType.Normal, null, null, "", "");
		stockDocumentForm = new GenericForm("StockDocument.xml", FormType.Normal, null, null, "", "");
		usersForm = new GenericForm("User.xml", FormType.Normal, null, null, "", "");
	}
	
	public static FormsHolder getInstance(){
		if(instance==null){
			instance = new FormsHolder();
		}
		return instance;
	}
	
	public GenericForm getFormByEntityName(String entityFileName){
		if(entityFileName.equals("ProductFamily.xml")){
			return productFamilyForm;
		}
		if(entityFileName.equals("ProductGroup.xml")){
			return productGroupForm;
		}
		if(entityFileName.equals("ProductType.xml")){
			return productTypeForm;
		}
		if(entityFileName.equals("Product.xml")){
			return productForm;
		}
		if(entityFileName.equals("MeasureUnit.xml")){
			return measureUnitForm;
		}
		if(entityFileName.equals("WarehouseType.xml")){
			return warehouseTypeForm;
		}
		if(entityFileName.equals("Warehouse.xml")){
			return warehouseForm;
		}
		if(entityFileName.equals("BusinessPartner.xml")){
			return businessPartnerForm;
		}
		if(entityFileName.equals("CompanyCode.xml")){
			return companyCodeForm;
		}
		if(entityFileName.equals("CompanyCode.xml")){
			return companyCodeForm;
		}
		if(entityFileName.equals("DocumentType.xml")){
			return documentTypeForm;
		}
		if(entityFileName.equals("SalesPrice.xml")){
			return salesPriceForm;
		}
		if(entityFileName.equals("StockDocument.xml")){
			return stockDocumentForm;
		}
		if(entityFileName.equals("User.xml")){
			return usersForm;
		}
		return null;
	}
	
	public GenericForm getMeasureUnitForm() {
		return measureUnitForm;
	}
	
	public GenericForm getProductFamilyForm() {
		return productFamilyForm;
	}
	
	public GenericForm getProductForm() {
		return productForm;
	}
	
	public GenericForm getProductGroupForm() {
		return productGroupForm;
	}
	
	public GenericForm getProductTypeForm() {
		return productTypeForm;
	}
	
	public GenericForm getWarehouseTypeForm() {
		return warehouseTypeForm;
	}
	
	public GenericForm getWarehouseForm() {
		return warehouseForm;
	}
	
	public GenericForm getUsersForm() {
		return usersForm;
	}
}
