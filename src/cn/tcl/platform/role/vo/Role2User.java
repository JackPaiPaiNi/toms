package cn.tcl.platform.role.vo;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("Role2User")
public class Role2User implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userLoginId;
	
	private String roleId;

	public String getUserLoginId() {
		return userLoginId;
	}

	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
}
