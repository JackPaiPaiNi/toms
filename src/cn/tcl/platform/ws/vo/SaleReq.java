package cn.tcl.platform.ws.vo;

import java.io.Serializable;
import java.util.Date;

public class SaleReq  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3971088779041256316L;
	//门店code
	private String shopCode;
	//门店名称
	private String shopName;
	//so日期
	private Date soDate;
	//产品型号
	private String model;
	//产品序列号
	private String sn;
	//消费者名称
	private String consumerName;
	//手机号码
	private String phoneNumber;
	//计数
	private int count;
	
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Date getSoDate() {
		return soDate;
	}
	public void setSoDate(Date soDate) {
		this.soDate = soDate;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getConsumerName() {
		return consumerName;
	}
	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

}
