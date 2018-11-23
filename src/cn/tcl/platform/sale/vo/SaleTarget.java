package cn.tcl.platform.sale.vo;

import java.sql.Date;

public class SaleTarget {
	private Integer saleTargetId;
//	private String userId;
//	private String userName;
	private Long quantity;
	private String amount;
	private String ctime;
	private String shopId;
	private String shopName;
	private String customerName;
	private String customerId;
	private String partyId;
	private String partyName;
	private Long tzQuantity;
	private String tzAmount;
	private String targetId;
	private String classId;
	private Double hAmount;
	private Double hTzAmount;
	private String datadate;
	private Double exchange;
	private String countryId;
	private int hQuantity;
	private int hTzQuantity;
	private int type;
	private String clas;
	
	public String getClas() {
		return clas;
	}
	public void setClas(String clas) {
		this.clas = clas;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public Integer getSaleTargetId() {
		return saleTargetId;
	}
	public void setSaleTargetId(Integer saleTargetId) {
		this.saleTargetId = saleTargetId;
	}
//	public String getUserId() {
//		return userId;
//	}
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}
//	public String getUserName() {
//		return userName;
//	}
//	public void setUserName(String userName) {
//		this.userName = userName;
//	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
	public Long getTzQuantity() {
		return tzQuantity;
	}
	public void setTzQuantity(Long tzQuantity) {
		this.tzQuantity = tzQuantity;
	}
	public String getTzAmount() {
		return tzAmount;
	}
	public void setTzAmount(String tzAmount) {
		this.tzAmount = tzAmount;
	}
	public Double gethAmount() {
		return hAmount;
	}
	public void sethAmount(Double hAmount) {
		this.hAmount = hAmount;
	}
	public Double gethTzAmount() {
		return hTzAmount;
	}
	public void sethTzAmount(Double hTzAmount) {
		this.hTzAmount = hTzAmount;
	}	
	public String getDatadate() {
		return datadate;
	}
	public void setDatadate(String datadate) {
		this.datadate = datadate;
	}
	public Double getExchange() {
		return exchange;
	}
	public void setExchange(Double exchange) {
		this.exchange = exchange;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public int gethQuantity() {
		return hQuantity;
	}
	public void sethQuantity(int hQuantity) {
		this.hQuantity = hQuantity;
	}
	public int gethTzQuantity() {
		return hTzQuantity;
	}
	public void sethTzQuantity(int hTzQuantity) {
		this.hTzQuantity = hTzQuantity;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}	
}
