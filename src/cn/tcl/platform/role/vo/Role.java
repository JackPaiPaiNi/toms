package cn.tcl.platform.role.vo;

import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("Role")
public class Role 
{
	//角色ID
	private String roleId;
	//角色名称
	private String roleName;
	//创建人
	private String createBy;
	//修改人
	private String updateBy;
	//创建日期
	private Date createDate;
	//修改日期
	private Date updateDate;
	/**
     * 为form表单准备
     * update：修改
     * 其他：新增
     */
    private String addOrEdit;
    
    /**
     * 不存数据库的角色类型
     */
    private String roleType;
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getAddOrEdit() {
		return addOrEdit;
	}
	public void setAddOrEdit(String addOrEdit) {
		this.addOrEdit = addOrEdit;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
}
