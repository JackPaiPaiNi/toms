package cn.tcl.platform.user.vo;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("UserLogin")
public class UserLogin implements Serializable{
  
	/**
     * 登陆账号
     */
    private String userLoginId;

    /**
     * 登陆账号名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码提醒
     */
    private String passwordHint;
    
    /**
     * 是否可用(1为启用，0为停用)
     */
    private String enabled;
    
    /**
     * 是否有效(1为默认有效，-1为无效)
     */
    private String status;

    /**
     * 账号失效日期
     */
    private Date disabledDateTime;
    
    /**
     * 密码验证方式("LDAP":LDAP;"LOCAL":本地验证）
     */
    private String loginType;

    /**
     * 账号创建人员
     */
    private String createBy;
    
    /**
     * 是否只是修改密码
     */
    private String isUpdatePass;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 用户邮箱
     */
    private String userEmail;
    
    /**
     * 工号
     */
    private String userWorkNum;
    
    /**
     * 微信号
     */
    private String userMcId;
    
    /**
     * 电话号
     */
    private String userTelNum;
    
    /**
     * 用户语言种类
     */
    private String userLocale;
    
    /**
     * 用户语言种类描述
     */
    private String userLocaleDesc;
    
    /**
     * 为form表单准备
     * update：修改
     * 其他：新增
     */
    private String addOrEdit;
    
    /**
     * 为组表单准备，left：在左边选择框，right：在右边选择框
     */
    private String leftOrRight;
    
    /**
     * 机构ID
     */
    private String partyId;
    
    /**
     * 所属国家
     */
    private String partyName;
    
    /**
     * 角色id
     */
    private String roleId;
    
    /**
     * 所属角色
     */
    private String roleName;
    
    /**
     * 删除操作用户
     */
    private String operatingUser;
    
    /**
     * 删除的用户
     */
    private String falseUserId;

    private  String updatePassword;
    
    //用户的星级
    private String level;
    
    //修改时间
    private Date updateTime;
    
    public String getUpdatePassword() {
		return updatePassword;
	}

	public void setUpdatePassword(String updatePassword) {
		this.updatePassword = updatePassword;
	}
	

	public String getFalseUserId() {
		return falseUserId;
	}

	public void setFalseUserId(String falseUserId) {
		this.falseUserId = falseUserId;
	}

	/**
     * fieldName : T_USER_LOGIN.USER_LOGIN_ID
     * description : 登陆账号
     */
    public String getUserLoginId() {
        return userLoginId;
    }

    /**
     * fieldName : T_USER_LOGIN.USER_LOGIN_ID
     * description : 登陆账号
     */
    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    /**
     * fieldName : T_USER_LOGIN.USER_NAME
     * description : 登陆账号名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * fieldName : T_USER_LOGIN.USER_NAME
     * description : 登陆账号名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * fieldName : T_USER_LOGIN.PASSWORD
     * description : 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * fieldName : T_USER_LOGIN.PASSWORD
     * description : 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * fieldName : T_USER_LOGIN.PASSWORD_HINT
     * description : 密码提醒
     */
    public String getPasswordHint() {
        return passwordHint;
    }

    /**
     * fieldName : T_USER_LOGIN.PASSWORD_HINT
     * description : 密码提醒
     */
    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    /**
     * fieldName : T_USER_LOGIN.ENABLED
     * description : 是否可用(1为启用，0为停用)
     */
    public String getEnabled() {
        return enabled;
    }

    /**
     * fieldName : T_USER_LOGIN.ENABLED
     * description : 是否可用(1为启用，0为停用)
     */
    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    /**
     * fieldName : T_USER_LOGIN.DISABLED_DATE_TIME
     * description : 账号失效日期
     */
    public Date getDisabledDateTime() {
        return disabledDateTime;
    }

    /**
     * fieldName : T_USER_LOGIN.DISABLED_DATE_TIME
     * description : 账号失效日期
     */
    public void setDisabledDateTime(Date disabledDateTime) {
        this.disabledDateTime = disabledDateTime;
    }

    /**
     * fieldName : T_USER_LOGIN.LOGIN_TYPE
     * description : 密码验证方式("LDAP":LDAP;"LOCAL":本地验证）
     */
    public String getLoginType() {
        return loginType;
    }

    /**
     * fieldName : T_USER_LOGIN.LOGIN_TYPE
     * description : 密码验证方式("LDAP":LDAP;"LOCAL":本地验证）
     */
    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    /**
     * fieldName : T_USER_LOGIN.CREATE_BY
     * description : 账号创建人员
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * fieldName : T_USER_LOGIN.CREATE_BY
     * description : 账号创建人员
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * fieldName : T_USER_LOGIN.CREATE_DATE
     * description : 创建日期
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * fieldName : T_USER_LOGIN.CREATE_DATE
     * description : 创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	public String getUserWorkNum() {
		return userWorkNum;
	}

	public void setUserWorkNum(String userWorkNum) {
		this.userWorkNum = userWorkNum;
	}

	public String getUserMcId() {
		return userMcId;
	}

	public void setUserMcId(String userMcId) {
		this.userMcId = userMcId;
	}
	
	public String getOperatingUser() {
		return operatingUser;
	}

	public void setOperatingUser(String operatingUser) {
		this.operatingUser = operatingUser;
	}

	public String getUserTelNum() {
		return userTelNum;
	}

	public void setUserTelNum(String userTelNum) {
		this.userTelNum = userTelNum;
	}
	public String getAddOrEdit() {
		return addOrEdit;
	}
	public void setAddOrEdit(String addOrEdit) {
		this.addOrEdit = addOrEdit;
	}
	public String getLeftOrRight() {
		return leftOrRight;
	}
	public void setLeftOrRight(String leftOrRight) {
		this.leftOrRight = leftOrRight;
	}
	public String getUserLocale() {
		return userLocale;
	}
	public void setUserLocale(String userLocale) {
		this.userLocale = userLocale;
	}
	public String getUserLocaleDesc() {
		return userLocaleDesc;
	}
	public void setUserLocaleDesc(String userLocaleDesc) {
		this.userLocaleDesc = userLocaleDesc;
	}
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getIsUpdatePass() {
		return isUpdatePass;
	}

	public void setIsUpdatePass(String isUpdatePass) {
		this.isUpdatePass = isUpdatePass;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}