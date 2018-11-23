package cn.tcl.platform.statement.vo;

public class Statement {
	
	private String catena;
	private String levels;//门店等级
	private String t;//月
	private String func;//功能或尺寸
	private Long quan;//销售数量
	private String id;
	private String name;
	private String userId;//用户id
	private String userName;//用户名称
	private String partyId;//区域Id
	private Long amou;//分公司金额
	private Long HAmou;//本部换算金额
	private String yearT;//年份
	private String dataDate;//月份
	private String modelName;//月份
	private String bhName;//月份
	private String customerId;//渠道编号
	private String customerName;//渠道名称
	private String partyName;//party名称
	private String proportion;//party名称
	private String value;//产品属性值
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCatena() {
		return catena;
	}
	public void setCatena(String catena) {
		this.catena = catena;
	}
	public String getLevels() {
		return levels;
	}
	public void setLevels(String levels) {
		this.levels = levels;
	}
	public String getProportion() {
		return proportion;
	}
	public void setProportion(String proportion) {
		this.proportion = proportion;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getBhName() {
		return bhName;
	}
	public void setBhName(String bhName) {
		this.bhName = bhName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getDataDate() {
		return dataDate;
	}
	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}
	public Long getAmou() {
		return amou;
	}
	public void setAmou(Long amou) {
		this.amou = amou;
	}
	public Long getHAmou() {
		return HAmou;
	}
	public void setHAmou(Long hAmou) {
		HAmou = hAmou;
	}
	public String getYearT() {
		return yearT;
	}
	public void setYearT(String yearT) {
		this.yearT = yearT;
	}
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	public String getFunc() {
		return func;
	}
	public void setFunc(String func) {
		this.func = func;
	}
	public Long getQuan() {
		return quan;
	}
	public void setQuan(Long quan) {
		this.quan = quan;
	}
	
	
	private String countryId;
	private String countryName;

	private Long volume;
	private Long revenue;
	private String quarter;
	private Long target;
	private Long trueS;
	private String size;
	private String assistCountryId;
	private String shopId;
	private String shopName;
	private String allCoeffinient;
	private String coedCoeffinient;
	private String exchange;
	private Long HRevenue;
	
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Long getHRevenue() {
		return HRevenue;
	}
	public void setHRevenue(Long hRevenue) {
		HRevenue = hRevenue;
	}
	
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public String getAllCoeffinient() {
		return allCoeffinient;
	}
	public void setAllCoeffinient(String allCoeffinient) {
		this.allCoeffinient = allCoeffinient;
	}
	public String getCoedCoeffinient() {
		return coedCoeffinient;
	}
	public void setCoedCoeffinient(String coedCoeffinient) {
		this.coedCoeffinient = coedCoeffinient;
	}
	
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	private double vol;
	private double rev;
	
	public double getVol() {
		return vol;
	}
	public void setVol(double vol) {
		this.vol = vol;
	}
	public double getRev() {
		return rev;
	}
	public void setRev(double rev) {
		this.rev = rev;
	}
	public String getAssistCountryId() {
		return assistCountryId;
	}
	public void setAssistCountryId(String assistCountryId) {
		this.assistCountryId = assistCountryId;
	}
	
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public Long getTarget() {
		return target;
	}
	public void setTarget(Long target) {
		this.target = target;
	}
	public Long getTrueS() {
		return trueS;
	}
	public void setTrueS(Long trueS) {
		this.trueS = trueS;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public Long getVolume() {
		return volume;
	}
	public void setVolume(Long volume) {
		this.volume = volume;
	}
	public Long getRevenue() {
		return revenue;
	}
	public void setRevenue(Long revenue) {
		this.revenue = revenue;
	}
	
	public String getQuarter() {
		return quarter;
	}
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
}
