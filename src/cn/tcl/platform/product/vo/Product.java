package cn.tcl.platform.product.vo;

import java.util.Date;

public class Product {
	private String productId;
	private String partyId;
	private String partyName;
	private String productTypeId;
	private Date introductionDate;
	private Date salesDiscontinuationDate;
	private Date supportDiscontinuationDate;
	private String productName;
	private String manufacturerPartyId;
	private String internalName;
	private String comments;
	private String description;
	private String descriptionEn;
	private String quantityUomId;
	private String colorId;
	private String volume;
	private Boolean status;
	private String brandId;
	private String categoryId;
	private String productSpecId;
	private String productFuncId;
	private String productScreenId;
	private String modelName;
	private String photo;
	private String productType;
	private String size;
	private String display;
	private String ratio;
	private String power;
	private String powerOn;
	private String powerWait;
	private String netweight;
	private String weightInclude;
	private String weight;
	private String interFace;
	private String network;
	private String os;
	private String filePath;
	private String fileName;
	private String headType;
	private String catena;
	private String catenaName;
	private String series;
	private String attribute;
	private Integer id;
	private String gasType;
	//伪删除人员
	private String delUserId;
	
	public String getGasType() {
		return gasType;
	}
	public void setGasType(String gasType) {
		this.gasType = gasType;
	}
	public String getCatenaName() {
		return catenaName;
	}
	public void setCatenaName(String catenaName) {
		this.catenaName = catenaName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getCatena() {
		return catena;
	}
	public void setCatena(String catena) {
		this.catena = catena;
	}
	public String getHeadType() {
		return headType;
	}
	public void setHeadType(String headType) {
		this.headType = headType;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	public String getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(String productTypeId) {
		this.productTypeId = productTypeId;
	}
	public Date getIntroductionDate() {
		return introductionDate;
	}
	public void setIntroductionDate(Date introductionDate) {
		this.introductionDate = introductionDate;
	}
	public Date getSalesDiscontinuationDate() {
		return salesDiscontinuationDate;
	}
	public void setSalesDiscontinuationDate(Date salesDiscontinuationDate) {
		this.salesDiscontinuationDate = salesDiscontinuationDate;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getManufacturerPartyId() {
		return manufacturerPartyId;
	}
	public void setManufacturerPartyId(String manufacturerPartyId) {
		this.manufacturerPartyId = manufacturerPartyId;
	}
	public String getInternalName() {
		return internalName;
	}
	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescriptionEn() {
		return descriptionEn;
	}
	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}
	public String getQuantityUomId() {
		return quantityUomId;
	}
	public void setQuantityUomId(String quantityUomId) {
		this.quantityUomId = quantityUomId;
	}
	public String getColorId() {
		return colorId;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getProductSpecId() {
		return productSpecId;
	}
	public void setProductSpecId(String productSpecId) {
		this.productSpecId = productSpecId;
	}
	public String getProductFuncId() {
		return productFuncId;
	}
	public void setProductFuncId(String productFuncId) {
		this.productFuncId = productFuncId;
	}
	public String getProductScreenId() {
		return productScreenId;
	}
	public void setProductScreenId(String productScreenId) {
		this.productScreenId = productScreenId;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	public String getPowerOn() {
		return powerOn;
	}
	public void setPowerOn(String powerOn) {
		this.powerOn = powerOn;
	}
	public String getPowerWait() {
		return powerWait;
	}
	public void setPowerWait(String powerWait) {
		this.powerWait = powerWait;
	}
	public String getNetweight() {
		return netweight;
	}
	public void setNetweight(String netweight) {
		this.netweight = netweight;
	}
	public String getWeightInclude() {
		return weightInclude;
	}
	public void setWeightInclude(String weightInclude) {
		this.weightInclude = weightInclude;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getInterFace() {
		return interFace;
	}
	public void setInterFace(String interFace) {
		this.interFace = interFace;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Date getSupportDiscontinuationDate() {
		return supportDiscontinuationDate;
	}
	public void setSupportDiscontinuationDate(Date supportDiscontinuationDate) {
		this.supportDiscontinuationDate = supportDiscontinuationDate;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDelUserId() {
		return delUserId;
	}
	public void setDelUserId(String delUserId) {
		this.delUserId = delUserId;
	}
}
