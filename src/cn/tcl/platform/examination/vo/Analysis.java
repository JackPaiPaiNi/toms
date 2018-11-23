package cn.tcl.platform.examination.vo;

public class Analysis {
	private Integer testTime;//考试时间
	private Integer score;//分数
	private Integer nops;//计划考试人数
	private Integer anos;//实际考试人数
	private String countryName;//国家名称
	private String userId;//用户ID
	private String userName;//用户名称 
	private String wechat;//微信号
	private String roleName;//角色名称
	private String workNum;//工号
	private String startTest;//开始考试时间
	private String endTest;//结束考试时间
	private String title;//试卷标题
	private String subTimes;//交卷时间
	private String customerName;//渠道名
	
	public String getSubTimes() {
		return subTimes;
	}
	public void setSubTimes(String subTimes) {
		this.subTimes = subTimes;
	}
	public Integer getTestTime() {
		return testTime;
	}
	public void setTestTime(Integer testTime) {
		this.testTime = testTime;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Integer getNops() {
		return nops;
	}
	public void setNops(Integer nops) {
		this.nops = nops;
	}
	public Integer getAnos() {
		return anos;
	}
	public void setAnos(Integer anos) {
		this.anos = anos;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getWechat() {
		return wechat;
	}
	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getWorkNum() {
		return workNum;
	}
	public void setWorkNum(String workNum) {
		this.workNum = workNum;
	}
	public String getStartTest() {
		return startTest;
	}
	public void setStartTest(String startTest) {
		this.startTest = startTest;
	}
	public String getEndTest() {
		return endTest;
	}
	public void setEndTest(String endTest) {
		this.endTest = endTest;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}	
}
