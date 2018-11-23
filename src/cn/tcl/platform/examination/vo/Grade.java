package cn.tcl.platform.examination.vo;

public class Grade {
	
	private String userName;//用户名称
	private String partyName;//国家名称
	private String officeName;//办事处名称
	private String regionName;//区域名称
	private Integer totalScore;//试卷总分
	private Integer grade;//考试分数
	private String subTime;//提交时间
	private String paperHead;//试卷标题
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public Integer getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	public String getSubTime() {
		return subTime;
	}
	public void setSubTime(String subTime) {
		this.subTime = subTime;
	}
	public String getPaperHead() {
		return paperHead;
	}
	public void setPaperHead(String paperHead) {
		this.paperHead = paperHead;
	}
}
