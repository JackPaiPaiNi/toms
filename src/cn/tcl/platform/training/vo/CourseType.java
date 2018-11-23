package cn.tcl.platform.training.vo;

import java.util.Date;

public class CourseType {
	//类别id
	private 	int 	typeId;
	//类别名称
	private 	String  typeName;
	//类别代码
	private     String  typeCode;
	//状态
	private     char    status;
	//创建日期
	private     Date    createDate;
	//创建人
	private     String  createBy;
	//最后修改日期
	private     Date    lastModifyDate;
	//最后修改人
	private     String  lastModifyUser;
	//类别层级
	private     char    typeLevel;
	//父类别id
	private     int     parentTypeId;
	
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
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
	public Date getLastModifyDate() {
		return lastModifyDate;
	}
	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	public String getLastModifyUser() {
		return lastModifyUser;
	}
	public void setLastModifyUser(String lastModifyUser) {
		this.lastModifyUser = lastModifyUser;
	}
	public char getTypeLevel() {
		return typeLevel;
	}
	public void setTypeLevel(char typeLevel) {
		this.typeLevel = typeLevel;
	}
	public int getParentTypeId() {
		return parentTypeId;
	}
	public void setParentTypeId(int parentTypeId) {
		this.parentTypeId = parentTypeId;
	}
}
