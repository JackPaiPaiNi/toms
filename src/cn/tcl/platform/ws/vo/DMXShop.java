package cn.tcl.platform.ws.vo;


public class DMXShop {
	private String shopCode;
	private String shopName;
	private String ctime;
	
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
	
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	
	public void ResetNull()
	{
		this.setShopCode(null);
		this.setShopName(null);
		this.setCtime(null);
	}
}
