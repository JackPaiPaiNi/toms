package cn.tcl.platform.message.vo;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;
@Alias("SendMessageUser")
public class SendMessageUser implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userLoginId;
	private String msgRoleId;
	//消息状态 0未读 1已读
	private String msgState;
	private String userName;
	public String getUserLoginId() {
		return userLoginId;
	}
	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}
	public String getMsgRoleId() {
		return msgRoleId;
	}
	public void setMsgRoleId(String msgRoleId) {
		this.msgRoleId = msgRoleId;
	}
	public String getMsgState() {
		return msgState;
	}
	public void setMsgState(String msgState) {
		this.msgState = msgState;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}	
}
