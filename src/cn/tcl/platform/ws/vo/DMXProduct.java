package cn.tcl.platform.ws.vo;

public class DMXProduct {
	private String model;
	private String ctime;
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	
	public void ResetNull()
	{
		this.setModel(null);
		this.setCtime(null);
	}
}
