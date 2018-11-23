package cn.tcl.platform.barcode.vo;

public class Barcode {
	
	private String id;
	private String barcode;
	private String hqModel;
	private String ctime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	public String getHqModel() {
		return hqModel;
	}
	public void setHqModel(String hqModel) {
		this.hqModel = hqModel;
	}

}
