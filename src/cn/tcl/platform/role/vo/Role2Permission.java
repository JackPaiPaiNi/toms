package cn.tcl.platform.role.vo;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("Role2Permission")
public class Role2Permission implements Serializable {

	private static final long serialVersionUID = 1L;

	private String permissionId;
	
	private String roleId;
	
	private String checkState;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}
}
