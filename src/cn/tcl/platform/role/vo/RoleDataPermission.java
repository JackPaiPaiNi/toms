package cn.tcl.platform.role.vo;

import org.apache.ibatis.type.Alias;

@Alias("RoleDataPermission")
public class RoleDataPermission 
{
	private String roleId;
	private String permissionType;
	private String permissionValue;
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getPermissionType() {
		return permissionType;
	}
	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}
	public String getPermissionValue() {
		return permissionValue;
	}
	public void setPermissionValue(String permissionValue) {
		this.permissionValue = permissionValue;
	}
}
