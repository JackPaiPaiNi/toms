package cn.tcl.platform.statable.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.statable.service.IStaTableService;
import net.sf.json.JSONObject;
/**
 * @author Fay
 * 2017年11月7日10:14:20
 */
public class StaTableAction  extends BaseAction{
	
	private static final long serialVersionUID = 1L;
	@Autowired(required = false)
	@Qualifier("staService")
	private IStaTableService staTableService;
	
	public String loadStaTablePage(){
		try{
			return SUCCESS;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}

	public void monthlySalesPerformance() {
		response.setHeader("Content-Type", "application/json");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String coeff = request.getParameter("coeff");
		
		String conditions="";
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		
		if (!WebPageUtil.isHAdmin()) {
			if (null != userPartyIds && !"".equals(userPartyIds)) {
				conditions = "  pa.party_id in (" + userPartyIds + ")  ";
			} else {
				conditions = "  1=2  ";
			}
		} else {
			conditions = " 1=1 ";
	
		}
		
		JSONObject jo = new JSONObject();
		Map<String,Object> whereMap = new HashMap<String,Object>();
		whereMap.put("beginDate", beginDate);
		whereMap.put("endDate", endDate);
		whereMap.put("partys", conditions);
		whereMap.put("coeff", coeff);
		
		List<HashMap<String,Object>> l = null;
		try {
				l = staTableService.monthlySalesPerformance(whereMap);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("=======sumTwo================"+l);
		
		jo.accumulate("data", l);
		WebPageUtil.writeBack(jo.toString());
	}

	public void regionalGrowthPerformance() {
		response.setHeader("Content-Type", "application/json");
		
		String sBeginDate = request.getParameter("beginDate");
		String year = WebPageUtil.isStringNullAvaliable(sBeginDate) ? sBeginDate.split("-")[0] : "2008";
		String sEngDate = request.getParameter("engDate");
		
		String [] sBeginDateArr = sBeginDate.split("-");
		String eBeginDate = (Integer.parseInt(year) - 1) +"-"+sBeginDateArr[1] +"-"+sBeginDateArr[2];
		
		String [] sEngDateArr = sEngDate.split("-");
		String eEngDate = (Integer.parseInt(year) - 1) + "-" + sEngDateArr[1] + "-" + sEngDateArr[2];
		
		String xcpWhere = request.getParameter("xcpWhere");
		String tab = request.getParameter("tab");
		String big = request.getParameter("big");
		
		String conditions="";
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		 String coeff = request.getParameter("coeff");
		if (!WebPageUtil.isHAdmin()) {
			if (null != userPartyIds && !"".equals(userPartyIds)) {
				conditions = "  pa.party_id in (" + userPartyIds + ")  ";
			} else {
				conditions = "  1=2  ";
			}
		} else {
			conditions = " 1=1 ";
	
		}
		Map<String,Object> whereMap = new HashMap<String,Object>();
		String sqlWhere = "";
		
		if(Contents.XCP_TAB.equals(tab)){
			if(xcpWhere!=null && !xcpWhere.equals("")){
				sqlWhere +=" and pr.`product_line` like '%"+ xcpWhere +"%'" ;
			}else{
				sqlWhere +=" and (pr.`product_line` like '%X%' or pr.`product_line` like '%C%' or pr.`product_line` like '%P%' ) ";
			}
			
			
		}else if(Contents.BIG_TAB.equals(tab)){
			whereMap.put("big", big);
		}else {
			sqlWhere += getWhereSql(tab);
		}
		
		whereMap.put("partys", conditions);
		whereMap.put("sBeginDate", sBeginDate);
		whereMap.put("sEngDate", sEngDate);
		whereMap.put("coeff", coeff);
		whereMap.put("thisYear", Integer.parseInt(year));
		whereMap.put("tab", tab);
		whereMap.put("eBeginDate", eBeginDate);
		whereMap.put("eEngDate", eEngDate);
		whereMap.put("listWhe", sqlWhere);
		
		List<HashMap<String,Object>> l = null;
		try {
				l = staTableService.regionalGrowthPerformance(whereMap);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("=======sumTwo================"+l);
		
		JSONObject j = new JSONObject();
		j.accumulate("data", l);
		WebPageUtil.writeBack(j.toString());
	}

	public void regionalGrowthPerformanceByPartyId() {
		response.setHeader("Content-Type", "application/json");
		
		String sBeginDate = request.getParameter("beginDate");
		String year = WebPageUtil.isStringNullAvaliable(sBeginDate) ? sBeginDate.split("-")[0] : "2008";
		String sEngDate = request.getParameter("engDate");
		
		String [] sBeginDateArr = sBeginDate.split("-");
		String eBeginDate = (Integer.parseInt(year) - 1) +"-"+sBeginDateArr[1] +"-"+sBeginDateArr[2];
		
		String [] sEngDateArr = sEngDate.split("-");
		String eEngDate = (Integer.parseInt(year) - 1) + "-" + sEngDateArr[1] + "-" + sEngDateArr[2];
		
		String xcpWhere = request.getParameter("xcpWhere");
		String tab = request.getParameter("tab");
		String big = request.getParameter("big");
		String coeff = request.getParameter("coeff");
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		String conditions="";
		if (!WebPageUtil.isHAdmin()) {
			if (null != userPartyIds && !"".equals(userPartyIds)) {
				conditions = "  pr.party_id in (" + userPartyIds + ")  ";
			} else {
				conditions = "  1=2  ";
			}
		} else {
			conditions = " 1=1 ";
		
		}
		
		Map<String,Object> whereMap = new HashMap<String,Object>();
		
		if(Contents.XCP_TAB.equals(tab)){
			if(xcpWhere!=null && !xcpWhere.equals("")){
				conditions +=" and pr.`product_line` like '%"+ xcpWhere +"%'" ;
			}else{
				conditions +=" and (pr.`product_line` like '%X%' or pr.`product_line` like '%C%' or pr.`product_line` like '%P%' ) ";
			}
			
		}else if(Contents.BIG_TAB.equals(tab)){
			whereMap.put("big", big);
		}else {
			conditions += getWhereSql(tab);
		}
		
		whereMap.put("sBeginDate", sBeginDate);
		whereMap.put("sEngDate", sEngDate);
		whereMap.put("eBeginDate", eBeginDate);
		whereMap.put("eEngDate", eEngDate);
		whereMap.put("coeff", coeff);
		whereMap.put("thisYear", Integer.parseInt(year));
		whereMap.put("tab", tab);
		whereMap.put("partys", conditions);
		whereMap.put("countryId", WebPageUtil.getLoginedUser().getPartyId());
		
		
		List<HashMap<String,Object>> l = null;
		try {
				l = staTableService.regionalGrowthPerformanceByPartyId(whereMap);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("=======sumTwo================"+l);
		WebPageUtil.writeBack(new JSONObject().accumulate("data", l).toString());
	}

	public void PSeriesSalesStatus() {
		response.setHeader("Content-Type", "application/json");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String whiles = request.getParameter("whiles");
		String coeff = request.getParameter("coeff");
		String conditions="";
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		
		if (!WebPageUtil.isHAdmin()) {
			if (null != userPartyIds && !"".equals(userPartyIds)) {
				conditions = "  pa.party_id in (" + userPartyIds + ")  ";
			} else {
				conditions = "  1=2  ";
			}
		} else {
			conditions = " 1=1 ";
	
		}
		
		Map<String,Object> whereMap = new HashMap<String,Object>();
		whereMap.put("beginDate", beginDate);
		whereMap.put("endDate", endDate);
		whereMap.put("partys", conditions);
		if(whiles!=null && !whiles.equals("")){
			whereMap.put("whiles",  "pr.product_line LIKE '%"+ whiles +"%'");
		}else{
			whereMap.put("whiles"," (pr.product_line  LIKE '%X%' OR pr.product_line  LIKE '%C%' OR pr.product_line  LIKE '%P%')");
		}
		
		whereMap.put("coeff", coeff);
		whereMap.put("countryId", WebPageUtil.getLoginedUser().getPartyId());
		
		JSONObject j = null;
		try {
				j = staTableService.PSeriesSalesStatus(whereMap);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("=======sumTwo================"+j);
		WebPageUtil.writeBack(j.toString());
	}
	
	public void queryBigSaleInfo() {
		response.setHeader("Content-Type", "application/json");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String big = request.getParameter("big");
		String series = request.getParameter("series");
		String coeff = request.getParameter("coeff");
		String conditions="";
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		
		if (!WebPageUtil.isHAdmin()) {
			if (null != userPartyIds && !"".equals(userPartyIds)) {
				conditions = "  pa.party_id in (" + userPartyIds + ")  ";
			} else {
				conditions = "  1=2  ";
			}
		} else {
			conditions = " 1=1 ";
			
		}
		
		Map<String,Object> whereMap = new HashMap<String,Object>();
		whereMap.put("beginDate", beginDate);
		whereMap.put("endDate", endDate);
		whereMap.put("partys", conditions);
		whereMap.put("coeff", coeff);
		whereMap.put("big", Integer.parseInt(big));
		whereMap.put("series", series);
		
		JSONObject j = null;
		try {
			j = staTableService.queryBigSaleInfo(whereMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("=======sumTwo================"+j);
		WebPageUtil.writeBack(j.toString());
	}
	
	public String getWhereSql(String tab){
		if(Contents.UD_TAB.equals(tab)){
			return " and (pr.`product_line` like '%X%' or pr.`product_line` like '%C%' or pr.`product_line` like '%P%' ) ";
		}else if(Contents.SMART_TAB.equals(tab)){
			return " and pr.`PRODUCT_SPEC_ID` like '%smart%' ";
		}else if(Contents.CURVED_TAB.equals(tab)){
			return " and pr.`PRODUCT_SPEC_ID` like '%curved%' ";
		}
		return " and 1=1";
	};
	
	public void queryStateTimeSalesBycountry() {
		response.setHeader("Content-Type", "application/json");
		
		String subXcp = request.getParameter("subXcp");
		String seriesXcp = request.getParameter("seriesXcp");
		
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String timeType = request.getParameter("timeType");
//		String xcpWhere = request.getParameter("xcpWhere");
		String tab = request.getParameter("tab");
		String coeff = request.getParameter("coeff");
		String halfAYear = request.getParameter("selectHAY");
		
		Map<String,Object> whereMap = new HashMap<String,Object>();
		String sqlWhere = "";
		
		if(Contents.XCP_TAB.equals(tab)){
			sqlWhere += getXCPWhereSql(subXcp,seriesXcp);
		}else if(Contents.BIG_TAB.equals(tab)){
		}else {
			sqlWhere += getHqWhereSql(tab);
		}
		
		whereMap.put("startDate", startDate);
		whereMap.put("endDate", endDate);
		whereMap.put("timeType", timeType);
		whereMap.put("tab", tab);
		whereMap.put("listWhe", sqlWhere);
		whereMap.put("coeff", coeff);
		whereMap.put("halfAYear", halfAYear);
		
		List<HashMap<String,Object>> l = null;
		try {
				l = staTableService.queryStateTimeSalesBycountry(whereMap);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject j = new JSONObject();
		j.accumulate("data", l);
		WebPageUtil.writeBack(j.toString());
	}
	
	public String getHqWhereSql(String tab){
		if(Contents.UD_TAB.equals(tab)){
			return " and (sr.`line` like '%X%' or sr.`line` like '%C%' or sr.`line` like '%P%' ) ";
		}else if(Contents.SMART_TAB.equals(tab)){
			return " and sr.`spec` like '%smart%' ";
		}else if(Contents.CURVED_TAB.equals(tab)){
			return " and sr.`spec` like '%curved%' ";
		}
		return " and 1=1";
	};
	
	public String getXCPWhereSql(String subXcp,String seriesXcp){
		String xcpWhere = "";
		if(WebPageUtil.isStringNullAvaliable(subXcp)){
			xcpWhere = " and sr.`line` like '%"+ subXcp +"%'";
		}else{
			if(WebPageUtil.isStringNullAvaliable(seriesXcp)){
				xcpWhere = " and sr.`line` like '%"+ seriesXcp +"%'";
			}else{
				xcpWhere = " and (sr.`line` like '%X%' or sr.`line` like '%C%' or sr.`line` like '%P%' ) ";
			}
		}
		return xcpWhere;
	};
}

