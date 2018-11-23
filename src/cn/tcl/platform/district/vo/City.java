package cn.tcl.platform.district.vo;

public class City {
	private String cityId;
	private String cityName;
	private String cityEn;
	private String cityLocal;
	private String provinceId;
	private Boolean status;
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityEn() {
		return cityEn;
	}
	public void setCityEn(String cityEn) {
		this.cityEn = cityEn;
	}
	public String getCityLocal() {
		return cityLocal;
	}
	public void setCityLocal(String cityLocal) {
		this.cityLocal = cityLocal;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
}
