package cn.tcl.platform.incentive.vo;

import java.util.Date;

/**
 * 
 * @author fzl
 *
 */
public class Incentive {
	
	//提成id
	private String id;
	//销售型号
	private String branchModel;
	//销售尺寸
	private String size;
	//零售价格
	private String retailPrice;
	//提成
	private String incentive;
	//销售数量
	private String quantity;
	//创建时间
	private String creatDate;
	//国家id
	private String partyId;
	//国家名
	private String partyName;
	
	private String remark;
	//0未删除，-1已删除
	private String flag;
	
	private String date;
		
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBranchModel() {
		return branchModel;
	}
	public void setBranchModel(String branchModel) {
		this.branchModel = branchModel;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(String retailPrice) {
		this.retailPrice = retailPrice;
	}
	public String getIncentive() {
		return incentive;
	}
	public void setIncentive(String incentive) {
		this.incentive = incentive;
	}		
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getCreatDate() {
		return creatDate;
	}
	public void setCreatDate(String creatDate) {
		this.creatDate = creatDate;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
