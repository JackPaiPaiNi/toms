package cn.tcl.platform.training.vo;
/**
 * 学习汇总实体类
 * @author fzl
 *
 */
public class LearnStatistics {
	private String partyName;
	private String userLogin;
	private String userName;
	private String userWC;
	private String roleName;
	private String userWorkNum;
	private String visitTime;
	private String operationType;
	private String operationScore;
	private int courseId;
	private String courseTittle;
	private String readState;
	private String time;
	//应阅读人数
	private String isRead;
	//实际阅读人数
	private String cont;
	//阅读时长
	private String onlineTime;
	//学习率
	private String percent;
	//渠道名
	private String customerName;
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserWC() {
		return userWC;
	}
	public void setUserWC(String userWC) {
		this.userWC = userWC;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getUserWorkNum() {
		return userWorkNum;
	}
	public void setUserWorkNum(String userWorkNum) {
		this.userWorkNum = userWorkNum;
	}
	public String getVisitTime() {
		return visitTime;
	}
	public void setVisitTime(String visitTime) {
		this.visitTime = visitTime;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getOperationScore() {
		return operationScore;
	}
	public void setOperationScore(String operationScore) {
		this.operationScore = operationScore;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public String getCourseTittle() {
		return courseTittle;
	}
	public void setCourseTittle(String courseTittle) {
		this.courseTittle = courseTittle;
	}
	public String getReadState() {
		return readState;
	}
	public void setReadState(String readState) {
		this.readState = readState;
	}
	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	public String getCont() {
		return cont;
	}
	public void setCont(String cont) {
		this.cont = cont;
	}	
	public String getOnlineTime() {
		return onlineTime;
	}
	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime;
	}
	public String getPercent() {
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}	
}
