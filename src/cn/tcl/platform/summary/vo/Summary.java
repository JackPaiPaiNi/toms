package cn.tcl.platform.summary.vo;

import java.util.Date;

/**
 * 
 * @author fzl
 *
 */
public class Summary {
	//总结id
	private int summaryId;
	//类型id
	private int typeId;
	//区域id
	private int regionId;
	//国家id
	private String countryId;
	//总结标题
	private String summaryTitle;
	//摘要
	private String summary;
	//封面地址
	private String coverUrl;
	//总结内容
	private String summaryContent;
	//轮播推送的状态(0是不推送,1是推送)
	private  int state;
	//总结消息类型
	private int summaryType;
	//创建日期
	private Date createDate;
	//创建人
	private String createBy;
	//创建的角色
	private    String   createrRoleId;
	//资料归属组织机构id
	private    String   createrPartyId;
	
	private String partyName;
	private String partyId;
	private String msgRoleId;
	
	private String typeName;
	public int getSummaryId() {
		return summaryId;
	}
	public void setSummaryId(int summaryId) {
		this.summaryId = summaryId;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public int getRegionId() {
		return regionId;
	}
	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getSummaryTitle() {
		return summaryTitle;
	}
	public void setSummaryTitle(String summaryTitle) {
		this.summaryTitle = summaryTitle;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getCoverUrl() {
		return coverUrl;
	}
	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}
	public String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getSummaryType() {
		return summaryType;
	}
	public void setSummaryType(int summaryType) {
		this.summaryType = summaryType;
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
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getMsgRoleId() {
		return msgRoleId;
	}
	public void setMsgRoleId(String msgRoleId) {
		this.msgRoleId = msgRoleId;
	}		
}
