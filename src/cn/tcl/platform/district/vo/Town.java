package cn.tcl.platform.district.vo;

public class Town {
	private String townId;
	private String townName;
	private String townEn;
	private String townLocal;
	private String countyId;
	private Boolean status;
	public String getTownId() {
		return townId;
	}
	public void setTownId(String townId) {
		this.townId = townId;
	}
	public String getTownName() {
		return townName;
	}
	public void setTownName(String townName) {
		this.townName = townName;
	}
	public String getTownEn() {
		return townEn;
	}
	public void setTownEn(String townEn) {
		this.townEn = townEn;
	}
	public String getTownLocal() {
		return townLocal;
	}
	public void setTownLocal(String townLocal) {
		this.townLocal = townLocal;
	}
	public String getCountyId() {
		return countyId;
	}
	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
}
