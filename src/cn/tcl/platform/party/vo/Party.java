package cn.tcl.platform.party.vo;

import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("Party")
public class Party 
{
	//组织编码
	private String partyId;
	//组织名称
	private String partyName;
	//税务号
	private String federalTaxId;
	//状态(1为启用，0为停用)
	private char status;
	//创建日期
	private Date createDate;
	//创建人员
	private String createBy;
	//修改人
	private String lastModifyUser;
	//最近更新日期
	private Date lastModifyDate;
	//组织简称
	private String groupNameAbbr;
	//归属法人机构编码
	private String partyIdLayer;
	//上级机构编码
	private String parentPartyId;
	//所属国家(机构)
	private String countryId;
	//判断是否国家
	private String isCountry;
	//备注
	private String comments;
	//角色数据权限部分,是否选中
	private String checkState;
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getFederalTaxId() {
		return federalTaxId;
	}
	public void setFederalTaxId(String federalTaxId) {
		this.federalTaxId = federalTaxId;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
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
	public String getGroupNameAbbr() {
		return groupNameAbbr;
	}
	public void setGroupNameAbbr(String groupNameAbbr) {
		this.groupNameAbbr = groupNameAbbr;
	}
	public String getPartyIdLayer() {
		return partyIdLayer;
	}
	public void setPartyIdLayer(String partyIdLayer) {
		this.partyIdLayer = partyIdLayer;
	}
	public String getParentPartyId() {
		return parentPartyId;
	}
	public void setParentPartyId(String parentPartyId) {
		this.parentPartyId = parentPartyId;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getCheckState() {
		return checkState;
	}
	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getIsCountry() {
		return isCountry;
	}
	public void setIsCountry(String isCountry) {
		this.isCountry = isCountry;
	}
}
