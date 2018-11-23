package cn.tcl.platform.shop.vo;

public class ShopUser {
	public Integer shopId;
	public String userLoginId;
	public Integer salerType;
	private  String shopName;
	private String roleId;
	
	
	
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	public String getUserLoginId() {
		return userLoginId;
	}
	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}
	public Integer getSalerType() {
		return salerType;
	}
	public void setSalerType(Integer salerType) {
		this.salerType = salerType;
	}
}
