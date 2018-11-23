package cn.tcl.platform.examination.vo;

public class Paper {
	
	private Integer Id;//序号
	private String headline;//标题
	private String userId;//用户Iid
	private String userName;//用户名称
	private String testTime;//考试时间
	private String sTime;//考试时间
	private String eTime;//结束时间
	private String status;//状态
	private String categories;//大类
	private String mediums;//中类
	private String smaClass;//小类
	private String categoriesId;//大类编号
	private String mediumsId;//中类编号
	private String smaClassId;//小类编号
	private Integer muiUum;//多选题数量
	private Integer sinNum;//单选题数量
	private Integer judNum;//判断题数量
	private String examIdStr;//题目Id字符串
	private Integer topicId;//题目id
	private String countryId;//国家区域
	private String partyName;//国家名称
	private String isAutomatic;//是否为手动添加
	private String createTime;//试卷创建时间
	//二维码
	private String QRCode;
	//二维码对应的考试id
	private Integer p_id;
	//考试角色用户的id
	private String msgRoleId;
	private String codeUrl;//二维码扫出来的路径
	
	
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCodeUrl() {
		return codeUrl;
	}
	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}
	public String getIsAutomatic() {
		return isAutomatic;
	}
	public void setIsAutomatic(String isAutomatic) {
		this.isAutomatic = isAutomatic;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public Integer getTopicId() {
		return topicId;
	}
	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}
	public String getExamIdStr() {
		return examIdStr;
	}
	public void setExamIdStr(String examIdStr) {
		this.examIdStr = examIdStr;
	}
	public Integer getMuiUum() {
		return muiUum;
	}
	public void setMuiUum(Integer muiUum) {
		this.muiUum = muiUum;
	}
	public Integer getSinNum() {
		return sinNum;
	}
	public void setSinNum(Integer sinNum) {
		this.sinNum = sinNum;
	}
	public Integer getJudNum() {
		return judNum;
	}
	public void setJudNum(Integer judNum) {
		this.judNum = judNum;
	}
	public String getCategoriesId() {
		return categoriesId;
	}
	public void setCategoriesId(String categoriesId) {
		this.categoriesId = categoriesId;
	}
	public String getMediumsId() {
		return mediumsId;
	}
	public void setMediumsId(String mediumsId) {
		this.mediumsId = mediumsId;
	}
	public String getSmaClassId() {
		return smaClassId;
	}
	public void setSmaClassId(String smaClassId) {
		this.smaClassId = smaClassId;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTestTime() {
		return testTime;
	}
	public void setTestTime(String testTime) {
		this.testTime = testTime;
	}
	public String getsTime() {
		return sTime;
	}
	public void setsTime(String sTime) {
		this.sTime = sTime;
	}
	public String geteTime() {
		return eTime;
	}
	public void seteTime(String eTime) {
		this.eTime = eTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public String getMediums() {
		return mediums;
	}
	public void setMediums(String mediums) {
		this.mediums = mediums;
	}
	public String getSmaClass() {
		return smaClass;
	}
	public void setSmaClass(String smaClass) {
		this.smaClass = smaClass;
	}
	public String getQRCode() {
		return QRCode;
	}
	public void setQRCode(String qRCode) {
		QRCode = qRCode;
	}
	public Integer getP_id() {
		return p_id;
	}
	public void setP_id(Integer p_id) {
		this.p_id = p_id;
	}
	public String getMsgRoleId() {
		return msgRoleId;
	}
	public void setMsgRoleId(String msgRoleId) {
		this.msgRoleId = msgRoleId;
	}	
}	
