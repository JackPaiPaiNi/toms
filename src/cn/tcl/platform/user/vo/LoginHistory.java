package cn.tcl.platform.user.vo;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("LoginHistory")
public class LoginHistory 
{
	//登陆账号
	private String userLoginId;
	//登陆时间
	private Timestamp loginDatetime;
	//登录使用密码
	private String passwordUsed;
	//是否成功登录(0为成功，1为失败)
	private char successfulLogin;
	//组织编码
	private String partyId;
	//登录备注
	private String comments;

	public String getUserLoginId() {
		return userLoginId;
	}

	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}

	public Timestamp getLoginDatetime() {
		return loginDatetime;
	}

	public void setLoginDatetime(Timestamp loginDatetime) {
		this.loginDatetime = loginDatetime;
	}

	public String getPasswordUsed() {
		return passwordUsed;
	}

	public void setPasswordUsed(String passwordUsed) {
		this.passwordUsed = passwordUsed;
	}

	public char getSuccessfulLogin() {
		return successfulLogin;
	}

	public void setSuccessfulLogin(char successfulLogin) {
		this.successfulLogin = successfulLogin;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
