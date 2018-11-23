package cn.tcl.platform.examination.vo;

public class Statistics {
	private Integer allFractions;//试卷总分值
	private Integer fractions;//考试得分
	private String userName;//考试用户
	private String subTime;//试卷提交时间
	private String stas;//状态:及格、不及格、缺考
	
	public String getStas() {
		return stas;
	}
	public void setStas(String stas) {
		this.stas = stas;
	}
	public Integer getAllFractions() {
		return allFractions;
	}
	public void setAllFractions(Integer allFractions) {
		this.allFractions = allFractions;
	}
	public Integer getFractions() {
		return fractions;
	}
	public void setFractions(Integer fractions) {
		this.fractions = fractions;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSubTime() {
		return subTime;
	}
	public void setSubTime(String subTime) {
		this.subTime = subTime;
	}
}
