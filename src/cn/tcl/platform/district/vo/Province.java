package cn.tcl.platform.district.vo;

public class Province {
	private String provinceId;
	private String provinceName;
	private String provinceEn;
	private String provinceLocal;
	private String countryId;
	private Boolean status;
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getProvinceEn() {
		return provinceEn;
	}
	public void setProvinceEn(String provinceEn) {
		this.provinceEn = provinceEn;
	}
	public String getProvinceLocal() {
		return provinceLocal;
	}
	public void setProvinceLocal(String provinceLocal) {
		this.provinceLocal = provinceLocal;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
}
