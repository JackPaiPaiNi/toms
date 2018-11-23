package cn.tcl.platform.message.vo;

import java.util.Date;

public class Message {
	// 消息的ID
	private int id;
	// 消息的类型
	private String msgType;
	// 消息的标题
	private String msgTitle;
	// 消息内容
	private String msgComment;
	// 消息的地址
	private String msgTitleUrl;
	// 创建消息的人
	private String createBy;
	// 创建消息的时间
	private Date createTime;
	// 创建消息的国家id
	private String createCountryId;
	// 创建人所属区域
	private String createPartyId;
	// 创建消息的国家
	private String countryName;
	// 消息角色id
	private String msgRoleId;
	// 消息摘要
	private String msgSummary;
	// 消息封面地址
	private String msgCoverUrl;
	// 课程id
	private String courseId;
	// 考试的id
	private String paperId;
	// 计划与总结的id
	private String summaryId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	public String getMsgComment() {
		return msgComment;
	}

	public void setMsgComment(String msgComment) {
		this.msgComment = msgComment;
	}

	public String getMsgTitleUrl() {
		return msgTitleUrl;
	}

	public void setMsgTitleUrl(String msgTitleUrl) {
		this.msgTitleUrl = msgTitleUrl;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateCountryId() {
		return createCountryId;
	}

	public void setCreateCountryId(String createCountryId) {
		this.createCountryId = createCountryId;
	}

	public String getCreatePartyId() {
		return createPartyId;
	}

	public void setCreatePartyId(String createPartyId) {
		this.createPartyId = createPartyId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getMsgRoleId() {
		return msgRoleId;
	}

	public void setMsgRoleId(String msgRoleId) {
		this.msgRoleId = msgRoleId;
	}

	public String getMsgSummary() {
		return msgSummary;
	}

	public void setMsgSummary(String msgSummary) {
		this.msgSummary = msgSummary;
	}

	public String getMsgCoverUrl() {
		return msgCoverUrl;
	}

	public void setMsgCoverUrl(String msgCoverUrl) {
		this.msgCoverUrl = msgCoverUrl;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getPaperId() {
		return paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}

	public String getSummaryId() {
		return summaryId;
	}

	public void setSummaryId(String summaryId) {
		this.summaryId = summaryId;
	}
}
