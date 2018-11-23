package cn.tcl.platform.statable.vo;

public class StaTable {
	String saleman;//业务员
	String name;//区域
	Integer noOfShops;//门店数量
	Integer noOfFps;//促销员
	Double ttlTVSOQtyD;
	Integer ttlTVSOQty;//销售数量
	Double TTlTVSOAmt;//销售金额
	Double targetAmt;//金额目标
	Integer targetQty;//数量目标
	String userName;
	String partyId;
	String partyName;
	String partyIds;
	String shopId;//门店编号
	String model;;
	String month;
	String func;
	String size;
	
	public Double getTtlTVSOQtyD() {
		return ttlTVSOQtyD;
	}
	public void setTtlTVSOQtyD(Double ttlTVSOQtyD) {
		this.ttlTVSOQtyD = ttlTVSOQtyD;
	}
	public String getFunc() {
		return func;
	}
	public void setFunc(String func) {
		this.func = func;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getPartyIds() {
		return partyIds;
	}
	public void setPartyIds(String partyIds) {
		this.partyIds = partyIds;
	}
	public String getSaleman() {
		return saleman;
	}
	public void setSaleman(String saleman) {
		this.saleman = saleman;
	}
	public String getName() {
		return name;
	}
	public Integer getNoOfShops() {
		return noOfShops;
	}
	public void setNoOfShops(Integer noOfShops) {
		this.noOfShops = noOfShops;
	}
	public Integer getNoOfFps() {
		return noOfFps;
	}
	public void setNoOfFps(Integer noOfFps) {
		this.noOfFps = noOfFps;
	}
	public Integer getTtlTVSOQty() {
		return ttlTVSOQty;
	}
	public void setTtlTVSOQty(Integer ttlTVSOQty) {
		this.ttlTVSOQty = ttlTVSOQty;
	}
	public Double getTTlTVSOAmt() {
		return TTlTVSOAmt;
	}
	public void setTTlTVSOAmt(Double tTlTVSOAmt) {
		TTlTVSOAmt = tTlTVSOAmt;
	}
	public Double getTargetAmt() {
		return targetAmt;
	}
	public void setTargetAmt(Double targetAmt) {
		this.targetAmt = targetAmt;
	}
	public Integer getTargetQty() {
		return targetQty;
	}
	public void setTargetQty(Integer targetQty) {
		this.targetQty = targetQty;
	}
	public void setName(String name) {
		this.name = name;
	}
}
