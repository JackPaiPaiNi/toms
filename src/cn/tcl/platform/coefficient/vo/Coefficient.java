package cn.tcl.platform.coefficient.vo;

public class Coefficient {
	private   String country;
	private String countryName;
	private double all;
	private double core;
	private String file;
	private String user;
	private String ctime;
	private String isUsing;
	
	
	
	public String getIsUsing() {
		return isUsing;
	}
	public void setIsUsing(String isUsing) {
		this.isUsing = isUsing;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	
	public double getAll() {
		return all;
	}
	public void setAll(double all) {
		this.all = all;
	}
	public double getCore() {
		return core;
	}
	public void setCore(double core) {
		this.core = core;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	
}
