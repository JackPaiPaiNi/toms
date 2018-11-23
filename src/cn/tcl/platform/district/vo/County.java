package cn.tcl.platform.district.vo;

public class County {
	private String countyId;
	private String countyName;
	private String countyEn;
	private String countyLocal;
	private String cityId;
	private Boolean status;
	public String getCountyId() {
		return countyId;
	}
	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public String getCountyEn() {
		return countyEn;
	}
	public void setCountyEn(String countyEn) {
		this.countyEn = countyEn;
	}
	public String getCountyLocal() {
		return countyLocal;
	}
	public void setCountyLocal(String countyLocal) {
		this.countyLocal = countyLocal;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
}
