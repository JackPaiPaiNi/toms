package cn.tcl.platform.modelmap.vo;

public class ModelMap {
	
	/**
	 * id
	 */
	private String id;
	/**
	 * 分公司型号
	 */
	private String branchModel;
	/**
	 * 总部型号
	 */
	private String hqModel;
	/**
	 * 渠道型号
	 */
	private String channelModel;
	/**
	 * 所属国家
	 */
	private String partyId;
	/**
	 * 创建时间
	 */
	private String ctime;
	
	/**
	 * 渠道序号
	 */
	private String customerId;
	/**
	 * 所属渠道
	 */
	private String customerName;
	
	
	private Float price;
	
	/**
	 * sql入参条件
	 */
	private String condition;
	
	/**
	 * 翻译所属国家名称
	 */
	private String partyName;
	
	private Long ChannelPrice;
	
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	private String delUserId;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Long getChannelPrice() {
		return ChannelPrice;
	}
	public void setChannelPrice(Long channelPrice) {
		ChannelPrice = channelPrice;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getChannelModel() {
		return channelModel;
	}
	public void setChannelModel(String channelModel) {
		this.channelModel = channelModel;
	}
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
	public String getHqModel() {
		return hqModel;
	}
	public void setHqModel(String hqModel) {
		this.hqModel = hqModel;
	}
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((branchModel == null) ? 0 : branchModel.hashCode());
		result = prime * result + ((channelModel == null) ? 0 : channelModel.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((customerName == null) ? 0 : customerName.hashCode());
		result = prime * result + ((partyId == null) ? 0 : partyId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelMap other = (ModelMap) obj;
		if (branchModel == null) {
			if (other.branchModel != null)
				return false;
		} else if (!branchModel.equals(other.branchModel))
			return false;
		if (channelModel == null) {
			if (other.channelModel != null)
				return false;
		} else if (!channelModel.equals(other.channelModel))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (customerName == null) {
			if (other.customerName != null)
				return false;
		} else if (!customerName.equals(other.customerName))
			return false;
		if (partyId == null) {
			if (other.partyId != null)
				return false;
		} else if (!partyId.equals(other.partyId))
			return false;
		return true;
	}
	public String getDelUserId() {
		return delUserId;
	}
	public void setDelUserId(String delUserId) {
		this.delUserId = delUserId;
	}
}
