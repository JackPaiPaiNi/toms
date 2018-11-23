package cn.tcl.platform.statement.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.statement.service.IStatementService;
import cn.tcl.platform.statement.vo.Statement;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class StatementAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	
	@Autowired(required = false)
	@Qualifier("statementService")
	private IStatementService statementService;
	
	public String loadStatementPage(){
		return "success";
	}
	
	public String loadACStatementPage(){
		return "success";
	}
	
	public void selectCountry(){
		String userId = request.getParameter("id");
		JSONObject result = new JSONObject();
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.selectCountry(userId)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectBusinessCenter(){
		JSONObject result = new JSONObject(); 
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.selectBusinessCenter()).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void businessCenterByUserId(){
		
		String userId = request.getParameter("userId");
		JSONObject result = new JSONObject(); 
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.businessCenterByUserId(userId)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectAllCountry(){
		String partyId = request.getParameter("id");
		JSONObject result = new JSONObject(); 
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.selectAllCountry(partyId)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectSalerType(){
		String partyId = request.getParameter("partyId");
		String salerType = request.getParameter("saleType");
		String isCountry = request.getParameter("isCountry");
		
		Map<String,Object> staMap = new HashMap<String,Object>();
		staMap.put("partyId", partyId);
		staMap.put("saleType", salerType);
		staMap.put("isCountry", isCountry);
		
		JSONObject result = new JSONObject(); 
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.selectSalertype(staMap)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectPartyManage(){
		String partyId = request.getParameter("partyId");
		String isCountry = request.getParameter("isCountry");
		
		Map<String,Object> staMap = new HashMap<String,Object>();
		staMap.put("partyId", partyId);
		staMap.put("isCountry", isCountry);
		
		JSONObject result = new JSONObject(); 
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.selectPartyManage(staMap)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
	public void selectUserSale (){
		String userId = request.getParameter("userId");
		String saleTime = request.getParameter("saleTime");
		String country = WebPageUtil.isHQRole()?request.getParameter("country"):WebPageUtil.getLoginedUser().getPartyId();
		String isSOrSal = request.getParameter("isSOrSal");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		
		Map<String,Object> userMap = new HashMap<String,Object>();
		userMap.put("userId", userId);
		userMap.put("pType", pType);
		userMap.put("level", level);
		userMap.put("saleTime", saleTime);
		userMap.put("startTime", startTime);
		userMap.put("endTime", endTime);
		userMap.put("isHQRole", WebPageUtil.isHQRole()?"IS":"");
		JSONObject result = new JSONObject(); 
		String rows = "";
		String coefficient = "";
		try {
			if("sup".equals(isSOrSal)){
				userMap.put("userList", "'"+userId+"'");
				 List<Statement> countryList = statementService.selectCountryByUserId(userMap);
				 StringBuffer sb = new StringBuffer();
				 if(countryList.size() > 0){
					 sb.append("'"+countryList.get(0).getCountryId()+"'");
					 for(int i = 1;i<countryList.size();i++){
						 sb.append(",'"+countryList.get(i).getCountryId()+"'");
					 }
					 userMap.put("partyList", sb.toString());
				 }else{
					 userMap.put("partyList", "''");
				 }
				 
				 rows = JSONArray.fromObject(statementService.selecPartySalerInfo(userMap)).toString();
				 
			}else{
				rows = JSONArray.fromObject(statementService.selectUserSale(userMap)).toString();
			}
			coefficient = JSONArray.fromObject(statementService.selectCoefficientByPartyId(country)).toString();
			
			result.accumulate("coefficient", coefficient);
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selecPartySalerTar(){
		String level = request.getParameter("level");
		String partyId = request.getParameter("id");
		String saleTime = request.getParameter("saleTime");
		String country = WebPageUtil.isHQRole()?request.getParameter("country"):WebPageUtil.getLoginedUser().getPartyId();
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String isSelectCountry = request.getParameter("isSelectCountry");
		String pType = request.getParameter("pType");
		
		
		Map<String,Object> partyMap = new HashMap<String,Object>();
		partyMap.put("partyId", partyId);
		partyMap.put("saleTime", saleTime);
		partyMap.put("isHQRole", WebPageUtil.isHQRole()?"IS":"");
		partyMap.put("startTime", startTime);
		partyMap.put("endTime", endTime);
		partyMap.put("isCountry", isSelectCountry);
		partyMap.put("pType", pType);
		partyMap.put("level", level);
		JSONObject result = new JSONObject(); 
		String rows = "";
		String coefficient = "";
		try {
			rows = JSONArray.fromObject(statementService.selecPartySalerTar(partyMap)).toString();
			coefficient = JSONArray.fromObject(statementService.selectCoefficientByPartyId(country)).toString();
			
			result.accumulate("rows", rows);
			result.accumulate("coefficient", coefficient);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectSaleRank(){
		
		String partyId = request.getParameter("partyId");
		String saleType = request.getParameter("saleType");
		String year = request.getParameter("year");//年
		String quarter = request.getParameter("quarter");//季度
		String month = request.getParameter("month");//月
		String week = request.getParameter("week");//周
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String country = WebPageUtil.isHQRole()?request.getParameter("country"):WebPageUtil.getLoginedUser().getPartyId();
		String isSorS = request.getParameter("isSorS");
		String type = request.getParameter("type");
		
		String targMonStart = request.getParameter("targMonStart");
		String targMonEnd = request.getParameter("targMonEnd");
		
		String targQuarterStart = request.getParameter("targQuarterStart");
		String targQuarterEnd = request.getParameter("targQuarterEnd");
		
		String targYearStart = request.getParameter("targYearStart");
		String targYearEnd = request.getParameter("targYearEnd");
		String isSelectCountry = request.getParameter("isSelectCountry");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("partyId", partyId);
		map.put("saleType", saleType);
		map.put("year", year);
		map.put("quarter", quarter);
		map.put("month", month);
		map.put("week", week);
		map.put("userId", userId);
		map.put("userName", userName);
		map.put("isSorS", isSorS);
		map.put("country", country);
		map.put("type", type);
		map.put("pType", pType);
		map.put("level", level);
		
		map.put("targMonStart", targMonStart);
		map.put("targMonEnd", targMonEnd);
		map.put("targQuarterStart", targQuarterStart);
		map.put("targQuarterEnd", targQuarterEnd);
		map.put("targYearStart", targYearStart);
		map.put("targYearEnd", targYearEnd);
		map.put("isCountry", isSelectCountry);
	
		JSONObject result = new JSONObject(); 
		String rows = "";
		String coefficient = "";
		
		try {
			
			rows = JSONArray.fromObject(statementService.selectSaleRank(map)).toString();
			coefficient = JSONArray.fromObject(statementService.selectCoefficientByPartyId(country)).toString();
			result.accumulate("rows", rows);
			result.accumulate("coefficient", coefficient);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectAttr(){
		JSONObject result = new JSONObject(); 
		String rows = "";
		
		try {
			rows = JSONArray.fromObject(statementService.selectAttr()).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectSeries(){
		String attrId = request.getParameter("attrId");
		String partyId = request.getParameter("partyId");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("attrId", attrId);
		map.put("partyId", partyId);
		
		JSONObject result = new JSONObject(); 
		String rows = "";
		
		try {
			rows = JSONArray.fromObject(statementService.selectSeriesByAttr(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectSizeBySeries(){
		String series = request.getParameter("series");
		JSONObject result = new JSONObject(); 
		String rows = "";
		
		try {
			rows = JSONArray.fromObject(statementService.selectSizeBySeries(series)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectModelBy(){
		String size = request.getParameter("size");
		String series = request.getParameter("series");
		String functions = request.getParameter("functions");
		String country = WebPageUtil.isHQRole()?request.getParameter("country"):WebPageUtil.getLoginedUser().getPartyId();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("country", country);
		map.put("size", WebPageUtil.isStringNullAvaliable(size) ? Integer.parseInt(size) : -1);
		map.put("series", series);
		map.put("isHQRole", WebPageUtil.isHQRole());
		map.put("functions", functions);
		JSONObject result = new JSONObject(); 
		
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.selectModelBy(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectItemSaleInfo(){
		String endTime = request.getParameter("endTime");
		String partyId = request.getParameter("partyId");
		String timeModelStr = request.getParameter("timeModelStr");
		String userId = request.getParameter("userId");
		String startTime = request.getParameter("startTime");
		String tDate = request.getParameter("tDate");
		String isSorS = request.getParameter("isSorS");
		String isCode = request.getParameter("isCode");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		String country = WebPageUtil.isHQRole()?request.getParameter("country"):WebPageUtil.getLoginedUser().getPartyId();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tDate", tDate);
		map.put("pType", pType);
		map.put("level", level);
		map.put("country", country);
		map.put("isHQRole", WebPageUtil.isHQRole());
		
		map.put("endTime", endTime);
		map.put("startTime", startTime);
		map.put("partyId", partyId);
		map.put("userId", (!"".equals(userId) && userId != null)? userId:"");
		if(timeModelStr != null && !"".equals(timeModelStr)){
			map.put("timeModelStr", timeModelStr);
		}else{
			map.put("timeModelStr", "''");
		}
		
		JSONObject result = new JSONObject(); 
		
		String rows = "";
		String model = "";
		try {
			if(!"".equals(userId) && userId != null){
				
				if("sup".equals(isSorS)){
					
					map.put("userList", "'"+userId+"'");
					List<Statement> countryList = statementService.selectCountryByUserId(map);
					StringBuffer countrySB  = new StringBuffer();
					if(countryList.size() > 0){
						countrySB.append("'"+countryList.get(0).getCountryId()+"'");
						for(int i = 1;i<countryList.size();i++){
							countrySB.append(",'"+countryList.get(i).getCountryId()+"'");
						}
						map.put("partyList", countrySB.toString());
					}else{
						map.put("partyList","''");
					}
					
					if(WebPageUtil.isStringNullAvaliable(isCode)){
						rows = JSONArray.fromObject(statementService.focusOnRegionalSales(map)).toString();
					}else{
						rows = JSONArray.fromObject(statementService.selectCountrysaleInfo(map)).toString();
					}
					
				}else{
					if(WebPageUtil.isStringNullAvaliable(isCode)){
						rows = JSONArray.fromObject(statementService.focusOnPersonalSelling(map)).toString();
					}else{
						rows = JSONArray.fromObject(statementService.pSaleInfo(map)).toString();
					}
				}
			}else{
				if(WebPageUtil.isStringNullAvaliable(isCode)){
					rows = JSONArray.fromObject(statementService.codeItemTypeSaleInfo(map)).toString();
				}else{
					rows = JSONArray.fromObject(statementService.itemTypeSaleInfo(map)).toString();
				}
			}
			model = JSONArray.fromObject(statementService.selectCountrysaleModel(map)).toString();
			result.accumulate("rows", rows);
			result.accumulate("model", model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectSizeByFunction(){
		String function = request.getParameter("function");
		JSONObject result = new JSONObject(); 
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.selectSizeByFunction(function)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectRegion(){
		String partyId = request.getParameter("partyId");
		
		JSONObject result = new JSONObject(); 
		String rows = "";
		
		try {
			rows = JSONArray.fromObject(statementService.selectRegion(partyId)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 获取区域下地区
	 */
	public void selectSubdomainByArea(){
		String partyId = request.getParameter("partyId");
		String isCountry = request.getParameter("isCountry");
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("partyId", partyId);
		map.put("isCountry", isCountry);
		
		JSONObject result = new JSONObject(); 
		try {
			String rows = JSONArray.fromObject(statementService.selectSubdomainByArea(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	/**
	 * 获取所有功能
	 */
	public void queryAllFunction() {
		JSONObject result = new JSONObject(); 
		try {
			String rows = JSONArray.fromObject(statementService.queryAllFunction()).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 区域获取所有功能销售信息
	 */
	public void queryFunctionSaleInfo() {
		
		String partyId = request.getParameter("partyId");
		String isCountry = request.getParameter("isCountry");
		String sType = request.getParameter("sType");
		String userId = request.getParameter("userId");
		
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("partyId", partyId);
		map.put("isCountry", isCountry);
		map.put("sType", sType);
		map.put("userList", "'" + userId + "'");
		
		map.put("startTime", startTime);
		map.put("level", level);
		map.put("pType", pType);
		map.put("endTime", endTime);
		JSONObject result = new JSONObject(); 
		try {
			map.put("partyList", jointPartyId(map));
			String rows = JSONArray.fromObject(statementService.queryFunctionSaleInfo(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 个人获取所有功能销售信息
	 */
	public void queryUsesrFunctionSaleInfo() {
		String userId = request.getParameter("userId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", "'" + userId + "'");
		map.put("pType", pType);
		map.put("level", level);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		JSONObject result = new JSONObject(); 
		try {
			String rows = JSONArray.fromObject(statementService.queryUsesrFunctionSaleInfo(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 获取所有尺寸
	 */
	public void queryAllSize() {
		JSONObject result = new JSONObject(); 
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.queryAllSize()).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(rows);
	}
	
	/**
	 * 区域获取所有尺寸销售信息
	 */
	public void querySizeSaleInfo() {
		
		String partyId = request.getParameter("partyId");
		String isCountry = request.getParameter("isCountry");
		String sType = request.getParameter("sType");
		String userId = request.getParameter("userId");
		
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("partyId", partyId);
		map.put("isCountry", isCountry);
		map.put("sType", sType);
		map.put("userList", "'" + userId + "'");
		
		map.put("pType", pType);
		map.put("level", level);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		JSONObject result = new JSONObject(); 
		try {
			map.put("partyList", jointPartyId(map));
			String rows = JSONArray.fromObject(statementService.querySizeSaleInfo(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 个人获取所有尺寸销售信息
	 */
	public void queryUserSizeSaleInfo() {
		String userId = request.getParameter("userId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("pType", pType);
		map.put("level", level);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		JSONObject result = new JSONObject(); 
		try {
			String rows = JSONArray.fromObject(statementService.queryUserSizeSaleInfo(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 根据业务经理获取区域 
	 */
	public void selectCountryByUserId() {
		String userId = request.getParameter("userId");
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userList", userId);
		
		JSONObject result = new JSONObject(); 
		try {
			String rows = JSONArray.fromObject(statementService.selectCountryByUserId(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 指定区域督导业务员
	 */
	public void selectPartySupeAndSale() {
		String countryList = request.getParameter("countryList");
		String type = request.getParameter("type");
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("countryList", countryList);
		map.put("type", type);
		
		JSONObject result = new JSONObject(); 
		try {
			String rows = JSONArray.fromObject(statementService.selectPartySupeAndSale(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 角色销售年份对比
	 */
	public void queryManagerOfSalesYear() {
		
		String partyId = request.getParameter("partyId");
		String isCountry = request.getParameter("isCountry");
		String sType = request.getParameter("sType");
		String userId = request.getParameter("userId");
		
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("partyId", partyId);
		map.put("isCountry", isCountry);
		map.put("sType", sType);
		map.put("userId", "'" + userId + "'");
		
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("pType", pType);
		map.put("level", level);
		JSONObject result = new JSONObject(); 
		try {
			map.put("countryList", jointPartyId(map));
			String rows = JSONArray.fromObject(statementService.queryManagerOfSalesYear(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 角色销售年份对比
	 */
	public void queryRoleOfSalesYear() {
		String userList = request.getParameter("userList");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userList", "'" + userList + "'");
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("pType", pType);
		map.put("level", level);
		JSONObject result = new JSONObject(); 
		try {
			String rows = JSONArray.fromObject(statementService.queryRoleOfSalesYear(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 业务经理
	 */
	public void businessManagerByPartyId(){
		String countryList = request.getParameter("countryList");
		JSONObject result = new JSONObject(); 
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("countryList", countryList);
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.businessManagerByPartyId(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
	/*业务经理
	public void businessManagerByPartyId(){
		
		String partyId = request.getParameter("partyId");
		String isCountry = request.getParameter("isCountry");
		String sType = request.getParameter("sType");
		String userId = request.getParameter("userId");
		JSONObject result = new JSONObject(); 
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("partyId", partyId);
		map.put("isCountry", isCountry);
		map.put("sType", sType);
		map.put("userId", userId);
		String rows = "";
		try {
			map.put("countryList", jointPartyId(map));
			rows = JSONArray.fromObject(statementService.businessManagerByPartyId(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}*/
	
	/**
	 * 区域重点产品销售数量
	 */
	public void regionalSalesOfKeyProductsInfo(){
		
		String partyId = request.getParameter("partyId");
		String isCountry = request.getParameter("isCountry");
		String sType = request.getParameter("sType");
		String userId = request.getParameter("userId");
				
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String country = WebPageUtil.isHQRole()?request.getParameter("country"):WebPageUtil.getLoginedUser().getPartyId();
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		
		JSONObject result = new JSONObject(); 
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("partyId", partyId);
		map.put("isCountry", isCountry);
		map.put("sType", sType);
		map.put("userId", "'" + userId + "'");
		
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("country", country);
		map.put("pType", pType);
		map.put("level", level);
		String rows = "";
		try {
			map.put("partyList", jointPartyId(map));
			rows = JSONArray.fromObject(statementService.regionalSalesOfKeyProductsInfo(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 个人重点产品销售数量
	 */
	public void personalSalesOfKeyProductsInfo(){
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String country = WebPageUtil.isHQRole()?request.getParameter("country"):WebPageUtil.getLoginedUser().getPartyId();
		String userId = request.getParameter("userId");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		
		JSONObject result = new JSONObject(); 
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("country", country);
		map.put("pType", pType);
		map.put("level", level);
		map.put("userId", userId);
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.personalSalesOfKeyProductsInfo(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void personalItemSalesInfo() {
		
		String userId = request.getParameter("userId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		JSONObject result = new JSONObject(); 
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("pType", pType);
		map.put("level", level);
		map.put("isHQRole", WebPageUtil.isHQRole());
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.personalItemSalesInfo(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}

	public void regionalSalesItemInfo() {
		
		String partyId = request.getParameter("partyId");
		String isCountry = request.getParameter("isCountry");
		String sType = request.getParameter("sType");
		String userId = request.getParameter("userId");
		
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		JSONObject result = new JSONObject(); 
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("partyId", partyId);
		map.put("isCountry", isCountry);
		map.put("sType", sType);
		map.put("userId", "'" + userId + "'");
		
		map.put("pType", pType);
		map.put("level", level);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("isHQRole", WebPageUtil.isHQRole());
		String rows = "";
		try {
			map.put("partyList", jointPartyId(map));
			rows = JSONArray.fromObject(statementService.regionalSalesItemInfo(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	/**
	 * 用户销售数据
	 */
	public void personalSalesData() {
		String pType = request.getParameter("pType");
		if(Contents.AC.equals(pType)){
			personalSalesACData();
		}else{
			personalSalesTvData();
		}
	};
	
	
	/**
	 * 用户销售数据
	 */
	public void personalSalesTvData() {
		String attr = request.getParameter("attr");
		String size = request.getParameter("size");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		JSONObject result = new JSONObject(); 
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("attr", attr);
		map.put("pType", pType);
		map.put("level",level );
		map.put("sizes", size);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.personalSalesData(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	/**
	 * 用户销售数据
	 */
	public void personalSalesACData() {
		String acType = request.getParameter("acType");
		String catena = request.getParameter("catena");
		String size = request.getParameter("size");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		JSONObject result = new JSONObject(); 
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("catena", catena);
		map.put("pType", pType);
		map.put("acType", acType);
		map.put("level",level );
		map.put("sizes", size);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.personalSalesACData(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	/**
	 * 用户销售数据
	 */
	public void personalManagerSalesData() {
		String pType = request.getParameter("pType");
		if(Contents.AC.equals(pType)){
			personalManagerSalesACData();
		}else{
			personalManagerSalesTvData();
		}
	};
	
	/**
	 * 用户销售数据
	 */
	public void personalManagerSalesTvData() {
		
		String attr = request.getParameter("attr");
		String size = request.getParameter("size");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		JSONObject result = new JSONObject(); 
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("attr", attr);
		map.put("pType", pType);
		map.put("level", level);
		map.put("sizes", size);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.personalManagerSalesData(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	/**
	 * 用户销售数据
	 */
	public void personalManagerSalesACData() {
		
		String acType = request.getParameter("acType");
		String catena = request.getParameter("catena");
		String size = request.getParameter("size");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		JSONObject result = new JSONObject(); 
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("catena", catena);
		map.put("pType", pType);
		map.put("level", level);
		map.put("sizes", size);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("acType", acType);
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.personalManagerSalesACData(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	/**
	 * 指定区域督导业务员
	 */
	public void selectPartySupeAndSaless() {
		String countryList = request.getParameter("countryList");
		String saleType = request.getParameter("saleType");
		
		JSONObject result = new JSONObject(); 
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("countryList", countryList);
		map.put("saleType", saleType);
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.selectPartySupeAndSaless(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	/*区域销售数据 */
	public void eachRegionalSalesQuantity() {
		
		String partyId = request.getParameter("partyId");
		String isCountry = request.getParameter("isCountry");
		String isRegion = request.getParameter("isRegion");
		
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("partyId", partyId);
		map.put("isCountry", isCountry);
		map.put("isRegion", isRegion);
		
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("pType", pType);
		map.put("level", level);
		JSONObject result = new JSONObject(); 
		String rows = "";
		try {
			map.put("partyList", jointPartyId(partyId,isCountry,isRegion));
			rows = JSONArray.fromObject(statementService.eachRegionalSalesQuantity(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	/*门店销售数据 */
	public void eachStoresSalesQuantity() {
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("startTime", startTime);
		map.put("pType", pType);
		map.put("level", level);
		map.put("endTime", endTime);
		JSONObject result = new JSONObject(); 
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.eachStoresSalesQuantity(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	/*渠道销售数据 */
	public void eachChannelsSalesQuantity() {
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("pType", pType);
		map.put("level", level);
		JSONObject result = new JSONObject(); 
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.eachChannelsSalesQuantity(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	/*区域下渠道 */
	public void selectCustomerByPartyList() {
		String partyId = request.getParameter("partyId");
		String isCountry = request.getParameter("isCountry");
		String isRegion = request.getParameter("isRegion");
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		JSONObject result = new JSONObject(); 
		String rows = "";
		try {
			String countryList = jointPartyId(partyId,isCountry,isRegion);
			map.put("countryList", countryList);
			rows = JSONArray.fromObject(statementService.selectCustomerByPartyList(countryList)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	/*区域下门店 */
	public void selectShopByPartyList() {
		String level = request.getParameter("level");
		Map<String,Object> map = new HashMap<String,Object>();
		
		String partyId = request.getParameter("partyId");
		String isCountry = request.getParameter("isCountry");
		String isRegion = request.getParameter("isRegion");
		
		map.put("level", level);
		JSONObject result = new JSONObject(); 
		String rows = "";
		try {
			map.put("countryList", jointPartyId(partyId,isCountry,isRegion));
			rows = JSONArray.fromObject(statementService.selectShopByPartyList(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	/*个别区域销售数量 */
	public void eachSingleAreaSalesQuantity() {
		
		String userId = request.getParameter("userId");
		String sType = request.getParameter("sType");
		
		//String partyList = request.getParameter("partyList");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("userId", "'" + userId + "'");
		map.put("sType", sType);
		
		
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("pType", pType);
		map.put("level", level);
		JSONObject result = new JSONObject(); 
		String rows = "";
		try {
			String partyList = jointPartyId(map);
			map.put("partyList", partyList);
			
			rows = JSONArray.fromObject(statementService.eachSingleAreaSalesQuantity(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	/*角色销售数量(督导、业务员) */
	public void eachRoleSalesQuantity() {
		String userId = request.getParameter("userId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pType = request.getParameter("pType");
		String level = request.getParameter("level");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("pType", pType);
		map.put("level", level);
		JSONObject result = new JSONObject(); 
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.eachRoleSalesQuantity(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	/*门店等级*/
	public void storeLevelQuery() {
		String country = request.getParameter("country");
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.storeLevelQuery(country)).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(rows);
	};
	
	/*AC系列*/
	public void selectACCatena() {
		String acType = request.getParameter("acType");
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.selectACCatena(acType)).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(rows);
	};
	
	/*AC尺寸*/
	public void selectACSize() {
		String acType = request.getParameter("acType");
		String catena = request.getParameter("catena");
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.selectACSize(catena,acType)).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(rows);
	};
	
	/*AC分体and窗体*/
	public void selectAcMachine() {
		String rows = "";
		try {
			rows = JSONArray.fromObject(statementService.selectAcMachine()).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(rows);
	};
	
	public String jointPartyId(String partyId,String isCountry,String isRegion) throws Exception{
		String partyList = "";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("isRegion", isRegion);
		map.put("country", partyId);
		map.put("partyId", partyId);
		map.put("isCountry", isCountry);
		
		List<Statement> list  = statementService.selectAreaByPartyId(map);
		if(list != null && list.size() > 0){
			for (Statement statement : list) {
				partyList += ",'" + statement.getCountryId() +"'";
			}
			partyList = partyList.substring(1,partyList.length());
		}else{
			partyList = "''";
		}
		return partyList;
	};
	
	/*
	 * 根据区域经理查询所在区域 
	 */
	public void selectPartyIdByUserManager() {
		String userId = request.getParameter("userId");
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", "'" + userId + "'");
		
		JSONObject result = new JSONObject(); 
		try {
			String rows = JSONArray.fromObject(statementService.selectPartyIdByUserManager(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * areaByAreaManagerAndSelectPartyList
	 */
	public void selectPartyIdByUserManagerAnd() {
		
		String partyId = request.getParameter("partyId");
		String isCountry = request.getParameter("isCountry");
		String sType = request.getParameter("sType");
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String isUser = request.getParameter("isUser");
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("partyId", partyId);
		map.put("isCountry", isCountry);
		map.put("sType", sType);
		
		JSONObject result = new JSONObject(); 
		try {
			List<Statement> list = null;
			if("TRUE".equals(isUser)){
				list = new ArrayList<Statement>();
				Statement s = new Statement();
				s.setUserId(userId);
				s.setUserName(userName);
				list.add(s);
				map.put("userId", "'" + userId + "'");
			}else{
				String partyList = jointPartyId(map);
				String userList = "";
				map.put("countryList", partyList);
				list = statementService.businessManagerByPartyId(map);
				StringBuffer sb = new StringBuffer();
				if(list.size() > 0){
					for (Statement s : list) {
						sb.append(",'" + s.getUserId() + "'");
					}
					userList = sb.toString().substring(1,sb.toString().length());
				}else{
					userList = "''";
				}
				map.put("userId", userList);
			}
			
			String rows = JSONArray.fromObject(statementService.selectPartyIdByUserManager(map)).toString();
			String busRows = JSONArray.fromObject(list).toString();
			result.accumulate("busRows", busRows);
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectPartySupeAndSalessAnd() {
		
		String partyId = request.getParameter("partyId");
		String isCountry = request.getParameter("isCountry");
		String sType = request.getParameter("sType");
		String saleType = request.getParameter("saleType");
		
		JSONObject result = new JSONObject(); 
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("saleType", saleType);
		map.put("partyId", partyId);
		map.put("isCountry", isCountry);
		map.put("sType", sType);
		
		
		String rows = "";
		try {
			String partyList = jointPartyId(map);
			map.put("countryList", partyList);
			rows = JSONArray.fromObject(statementService.selectPartySupeAndSaless(map)).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public String jointPartyId(Map<String,Object> map) throws Exception{
		String partyList = "";
		
		List<Statement> list  = null;
		if ("SELECTSUBDOMAINBYAREA".equals(map.get("sType"))) {
			list = statementService.selectSubdomainByArea(map);
		}else if("SELECTPARTYIDBYUSERMANAGER".equals(map.get("sType"))){
			list = statementService.selectPartyIdByUserManager(map);
			
			if(list != null && list.size() > 0){
				for (Statement statement : list) {
					partyList += ",'" + statement.getPartyId() +"'";
				}
				partyList = partyList.substring(1,partyList.length());
			}else{
				partyList = "''";
			}
			return partyList;
			
			
		}else if("SELECTCOUNTRYBYUSERID".equals(map.get("sType"))){
			list = statementService.selectCountryByUserId(map);
		}
		if(list != null && list.size() > 0){
			for (Statement statement : list) {
				partyList += ",'" + statement.getCountryId() +"'";
			}
			partyList = partyList.substring(1,partyList.length());
		}else{
			partyList = "''";
		}
		return partyList;
	};
}
