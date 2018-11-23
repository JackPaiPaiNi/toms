package cn.tcl.platform.permission.vo;

import org.apache.ibatis.type.Alias;

@Alias("Permission")
public class Permission 
{
	//权限点编号
	private String permissionId;
	//上级权限
	private String parentPermissionId;
	//权限名称
	private String permissionName;
	//是否菜单(1为菜单，0为非菜单)
	private int isMenu;
	//备注
	private String comments;
	//权限点简码
	private String permissionCode;
	//该权限点对应的URL
	private String permissionUrl;
	//页面元素ID
	private String buttonId;
	//权限序号
	private int permissionSeq;
	//选中状态(checked:选择，unchecked:未选择，indetermin:半选择)
	private String checkState;
	//国际化KEY
	private String labelKey;
	public String getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}
	public String getParentPermissionId() {
		return parentPermissionId;
	}
	public void setParentPermissionId(String parentPermissionId) {
		this.parentPermissionId = parentPermissionId;
	}
	public String getPermissionName() {
		return permissionName;
	}
	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	public int getIsMenu() {
		return isMenu;
	}
	public void setIsMenu(int isMenu) {
		this.isMenu = isMenu;
	}
	public String getPermissionCode() {
		return permissionCode;
	}
	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}
	public String getPermissionUrl() {
		return permissionUrl;
	}
	public void setPermissionUrl(String permissionUrl) {
		this.permissionUrl = permissionUrl;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getButtonId() {
		return buttonId;
	}
	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}
	public int getPermissionSeq() {
		return permissionSeq;
	}
	public void setPermissionSeq(int permissionSeq) {
		this.permissionSeq = permissionSeq;
	}
	public String getCheckState() {
		return checkState;
	}
	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}
	public String getLabelKey() {
		return labelKey;
	}
	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}
}
