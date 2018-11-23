package cn.tcl.platform.performance.vo;

public class Policy {
	
	private String id;
	//起始日期
	private String startDate;
	//失效日期
	private String expirationDate;	
	//创建人
	private String userId;
	//政策类型  1是奖励，2.重点产品奖励，3.首销奖
	private String classId;
	//任务数量完成率
	private String qtyCompletionRate;
	//任务金额完成率
	private String amtCompletionRate;
	//产品系列
	private String productLine;
	//排名
	private int ranking;
	//人民币奖励金额;
	private Double hAmtReWard;
	//奖励金额
	private String AmtReWard;
	
	private String startDateTwo;
	private String expirationDateTwo;
	private String qtyCompletionRateTwo;
	private String amtReWardTwo;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getQtyCompletionRate() {
		return qtyCompletionRate;
	}
	public void setQtyCompletionRate(String qtyCompletionRate) {
		this.qtyCompletionRate = qtyCompletionRate;
	}
	public String getAmtCompletionRate() {
		return amtCompletionRate;
	}
	public void setAmtCompletionRate(String amtCompletionRate) {
		this.amtCompletionRate = amtCompletionRate;
	}
	public String getProductLine() {
		return productLine;
	}
	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	public Double gethAmtReWard() {
		return hAmtReWard;
	}
	public void sethAmtReWard(Double hAmtReWard) {
		this.hAmtReWard = hAmtReWard;
	}
	public String getAmtReWard() {
		return AmtReWard;
	}
	public void setAmtReWard(String amtReWard) {
		AmtReWard = amtReWard;
	}
	public String getStartDateTwo() {
		return startDateTwo;
	}
	public void setStartDateTwo(String startDateTwo) {
		this.startDateTwo = startDateTwo;
	}
	public String getExpirationDateTwo() {
		return expirationDateTwo;
	}
	public void setExpirationDateTwo(String expirationDateTwo) {
		this.expirationDateTwo = expirationDateTwo;
	}
	public String getQtyCompletionRateTwo() {
		return qtyCompletionRateTwo;
	}
	public void setQtyCompletionRateTwo(String qtyCompletionRateTwo) {
		this.qtyCompletionRateTwo = qtyCompletionRateTwo;
	}
	public String getAmtReWardTwo() {
		return amtReWardTwo;
	}
	public void setAmtReWardTwo(String amtReWardTwo) {
		this.amtReWardTwo = amtReWardTwo;
	}	
}
