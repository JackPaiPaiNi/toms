package cn.tcl.platform.training.vo;

import java.util.Date;

public class TrainingCourse {
	//课程id
	private    int      courseId;
	//类别id
	private    int      typeId;
	//对应机构id
	private    String   correspondingPartyId;
	//对应业务区域id
	private    String   correspondingRegionId;  
	//对应国家id 
	private    String   correspondingCountryId;
	//课程标题
	private    String   courseTitle;
	//课程摘要
	private    String   courseSummary;
	//课程封面图片存放URL
	private    String   coverImgUrl;
	//存放的附件URL
	private    String   attachUrl;
	//课程内容
	private    String   courseContent;
	//消息类型
	private    char     messageType;
	//创建日期
	private    Date     createDate;
	//创建人
	private    String   createBy;
	//创建人角色id
	private    String   createrRoleId;
	//资料归属组织机构id
	private    String   createrPartyId;
	//最后修改人
	private    String   lastModifyUser;
	//最后修改日期
	private    Date     lastModifyDate;
	//关键字
	private    String 	keyword;
	//推送
	private String state;
	//一级节点
	private int levelOneTypeId;
	//二级节点
	private int levelTwoTypeId;
	//三级节点
	private int levelThreeTypeId;
	//国家
	private String partyName;
	private String partyId;
	//消息角色id
	private String msgRoleId;
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public String getCorrespondingPartyId() {
		return correspondingPartyId;
	}
	public void setCorrespondingPartyId(String correspondingPartyId) {
		this.correspondingPartyId = correspondingPartyId;
	}
	public String getCorrespondingRegionId() {
		return correspondingRegionId;
	}
	public void setCorrespondingRegionId(String correspondingRegionId) {
		this.correspondingRegionId = correspondingRegionId;
	}
	public String getCorrespondingCountryId() {
		return correspondingCountryId;
	}
	public void setCorrespondingCountryId(String correspondingCountryId) {
		this.correspondingCountryId = correspondingCountryId;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getCourseTitle() {
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	public String getCourseSummary() {
		return courseSummary;
	}
	public void setCourseSummary(String courseSummary) {
		this.courseSummary = courseSummary;
	}
	public String getCoverImgUrl() {
		return coverImgUrl;
	}
	public void setCoverImgUrl(String coverImgUrl) {
		this.coverImgUrl = coverImgUrl;
	}
	public String getAttachUrl() {
		return attachUrl;
	}
	public void setAttachUrl(String attachUrl) {
		this.attachUrl = attachUrl;
	}
	public String getCourseContent() {
		return courseContent;
	}
	public void setCourseContent(String courseContent) {
		this.courseContent = courseContent;
	}
	public char getMessageType() {
		return messageType;
	}
	public void setMessageType(char messageType) {
		this.messageType = messageType;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreaterRoleId() {
		return createrRoleId;
	}
	public void setCreaterRoleId(String createrRoleId) {
		this.createrRoleId = createrRoleId;
	}
	public String getCreaterPartyId() {
		return createrPartyId;
	}
	public void setCreaterPartyId(String createrPartyId) {
		this.createrPartyId = createrPartyId;
	}
	public String getLastModifyUser() {
		return lastModifyUser;
	}
	public void setLastModifyUser(String lastModifyUser) {
		this.lastModifyUser = lastModifyUser;
	}
	public Date getLastModifyDate() {
		return lastModifyDate;
	}
	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getLevelOneTypeId() {
		return levelOneTypeId;
	}
	public void setLevelOneTypeId(int levelOneTypeId) {
		this.levelOneTypeId = levelOneTypeId;
	}
	public int getLevelTwoTypeId() {
		return levelTwoTypeId;
	}
	public void setLevelTwoTypeId(int levelTwoTypeId) {
		this.levelTwoTypeId = levelTwoTypeId;
	}
	public int getLevelThreeTypeId() {
		return levelThreeTypeId;
	}
	public void setLevelThreeTypeId(int levelThreeTypeId) {
		this.levelThreeTypeId = levelThreeTypeId;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	public String getMsgRoleId() {
		return msgRoleId;
	}
	public void setMsgRoleId(String msgRoleId) {
		this.msgRoleId = msgRoleId;
	}	
}
