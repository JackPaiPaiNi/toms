package cn.tcl.platform.statable.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.excel.actions.DateUtil;
import cn.tcl.platform.statable.dao.IStaHQTableDao;
import cn.tcl.platform.statable.dao.IStaTableDao;
import cn.tcl.platform.statable.service.IStaTableService;
import cn.tcl.platform.statable.vo.StaHQTable;
import cn.tcl.platform.statable.vo.StaTable;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("staService")
public class StaTableServiceImpl implements IStaTableService{
	
	private final String IS_TRUE = "true";
	
	@Autowired
	private IStaTableDao daot;
	
	@Autowired
	private IStaHQTableDao hqDaot;
	@Override
	public List<HashMap<String,Object>> monthlySalesPerformance(Map<String, Object> map) throws Exception {
		LinkedList<HashMap<String,Object>> resultList = new LinkedList<HashMap<String,Object>>();
		List<StaTable> list = null;
		
		if(IS_TRUE.equals(map.get("coeff"))){
			list = daot.monthlySalesPerformance(map);
		}else{
			list = daot.monthlySalesPerformanceCheck(map);
		}
		
		String userName = null;//储存第一个用户
		if(list.size() > 0){
			userName = list.get(0).getSaleman();
		};
		StringBuffer sb = new StringBuffer();//储存拼接区域、
		Integer qty = new Integer(0);//数量
		Integer shop = new Integer(0);//门店数量
		Integer fps = new Integer(0);//促销员数量
		Double amt = 0d;//金额
		Double tAmt = 0d;//目标金额
	    
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getSaleman().equals(userName)){
				qty += list.get(i).getTtlTVSOQty();
				shop += list.get(i).getNoOfShops();
				fps += list.get(i).getNoOfFps();
				amt += list.get(i).getTTlTVSOAmt();
				tAmt += list.get(i).getTargetAmt();
				sb.append(list.get(i).getName() +",");//拼接区域
				if(list.size() - i == 1){
					setMonthlySalesPerformance(resultList,userName,sb,shop,qty,fps,amt,tAmt);
				}
			}else{
				setMonthlySalesPerformance(resultList,userName,sb,shop,qty,fps,amt,tAmt);
				
				userName = list.get(i).getSaleman();//改变
				
				sb = new StringBuffer();//储存拼接区域
				qty = new Integer(0);//数量
				shop = new Integer(0);//门店数量
				fps = new Integer(0);//促销员数量
				amt = 0d;//金额
				tAmt = 0d;//目标金额
				i--;
			}
		}
		return DateUtil.Order(resultList, "sortByAch");
	};
	
	public void setMonthlySalesPerformance(LinkedList<HashMap<String,Object>> resultList ,String userName,StringBuffer sb,Integer shop,Integer qty,Integer fps,Double amt,Double tAmt){
		HashMap<String,Object> result = new HashMap<String,Object>();
		result.put("Saleman", userName);
		result.put("Account", sb.toString());
		result.put("No_of_Shops", shop);
		result.put("NO_of_FPS", fps);
		result.put("TTL_TV_SO_Qty", qty);
		result.put("TTL_TV_SO_Amt", amt);
		result.put("Basic_TV_Target", tAmt);
		String ach = ((amt != 0 && tAmt != 0) ? amt/tAmt*100 :0) +"";
		result.put("sortByAch", ach + "%");
		result.put("Ach", ach);
		result.put("Ave_SO_per_FPS_Qty", (qty != 0 && fps != 0) ? qty/fps :0);
		result.put("Ave_SO_per_FPS_Amount", (amt != 0 && fps != 0) ? amt/fps :0);
		resultList.add(result);
	}
	
	public List<StaTable> getlistLastYearDataAcfo (String coeff,Map<String, Object> map) throws Exception{
		List<StaTable> listLastYearData = null;
		if(IS_TRUE.equals(coeff)){
			listLastYearData = daot.regionalGrowthPerformance(map);
		}else{
			listLastYearData = daot.regionalGrowthPerformanceCheck(map);
		}
		return listLastYearData;
	}

	@Override
	public List<HashMap<String, Object>> regionalGrowthPerformance(Map<String, Object> map) throws Exception {
		
		LinkedList<HashMap<String,Object>> resultList = new LinkedList<HashMap<String,Object>>();
		
		map.put("beginDate", map.get("eBeginDate"));
		map.put("endDate", map.get("eEngDate"));
		int thisYear = (Integer) map.get("thisYear") -1;
		if(Contents.BIG_TAB.equals(map.get("tab"))){
			map.put("listWhe",  getDiffBigSql(thisYear));
		}
		List<StaTable> list = getlistLastYearDataAcfo((String) map.get("coeff"),map);
		
		map.put("beginDate", map.get("sBeginDate"));
		map.put("endDate",  map.get("sEngDate"));
		thisYear = (Integer) map.get("thisYear");
		if(Contents.BIG_TAB.equals(map.get("tab"))){
			map.put("listWhe",  getDiffBigSql(thisYear));
		}
		List<StaTable> listTwo = getlistLastYearDataAcfo((String) map.get("coeff"),map);
		
		String userName = null;//储存第一个用户
		if(list.size() > 0){
			userName = list.get(0).getSaleman();
		};
		int lastYearFPS = 0;//每个用户去年促销员
		Double lastYearAmt = 0d;//每个用户去年销售金额
		int lastYearQty = 0;//每个用户去年销售数量
		
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getSaleman().equals(userName)){
				lastYearFPS += list.get(i).getNoOfFps();
				lastYearAmt += list.get(i).getTTlTVSOAmt();
				lastYearQty += list.get(i).getTtlTVSOQty();
				//sb.append(list.get(i).getName() +",");//拼接区域
				if(list.size() - i == 1){
					setRegionalGrowthPerformance(resultList,userName,lastYearFPS,lastYearAmt,lastYearQty);
				}
			}else{
				setRegionalGrowthPerformance(resultList,userName,lastYearFPS,lastYearAmt,lastYearQty);
				userName = list.get(i).getSaleman();//改变
				lastYearFPS = 0;//每个用户去年促销员
				lastYearAmt = 0d;//每个用户去年销售金额
				lastYearQty = 0;//每个用户去年销售数量
				i--;
			}
		}
		
		int YearFPS = 0;//每个用户今年促销员
		Double YearAmt = 0d;//每个用户今年销售金额
		int YearQty = 0;//每个用户今年销售数量
		for (int i = 0; i < resultList.size(); i++) {
			for (int j = 0; j < listTwo.size(); j++) {
				if(resultList.get(i).get("ACFO").equals(listTwo.get(j).getSaleman())){
					YearFPS += listTwo.get(j).getNoOfFps();
					YearAmt += listTwo.get(j).getTTlTVSOAmt();
					YearQty += listTwo.get(j).getTtlTVSOQty();
				}
			}
			resultList.get(i).put("YearFPS", YearFPS);
			resultList.get(i).put("YearAmt", YearAmt);
			resultList.get(i).put("YearQty", YearQty);
			
			Integer lyQty =  (Integer) resultList.get(i).get("TOTAL_TV_Qty_lastYear");
			String qGrowth = getBranchGrowthRate((double)YearQty,(double)lyQty);
			resultList.get(i).put("Qty_Growth", qGrowth );
			resultList.get(i).put("sort_Qty_Growth", qGrowth + "%");
			
			Double lyAmt = (Double) resultList.get(i).get("TOTAL_TV_Amt_lastYear");
			String tGrowth = getBranchGrowthRate(YearAmt,lyAmt);
			resultList.get(i).put("Amt_Growth", tGrowth);
		}
		
		
		return DateUtil.Order(resultList, "sort_Qty_Growth");
	};
	
	public void setRegionalGrowthPerformance(LinkedList<HashMap<String,Object>> resultList,String userName,int lastYearFPS,Double lastYearAmt,int lastYearQty){
		HashMap<String,Object> result = new HashMap<String,Object>();
		result.put("ACFO", userName);
		result.put("FPS_YR_lastYear", lastYearFPS);
		result.put("TOTAL_TV_Qty_lastYear", lastYearQty);
		result.put("TOTAL_TV_Amt_lastYear", lastYearAmt);
		resultList.add(result);
	}
	
	public String getDiffBigSql (int Year){
		if(Year >= 2018){
			return " and pr.`size` > CAST('55' as SIGNED) ";
		}else{
			return " and pr.`size` > CAST('48' as SIGNED) ";
		}
	}
	
	public List<StaTable> getlistLastYearData (String coeff,Map<String, Object> map) throws Exception{
		List<StaTable> listLastYearData = null;
		if(IS_TRUE.equals(coeff)){
			listLastYearData = daot.regionalGrowthPerformanceByPartyId(map);
		}else{
			listLastYearData = daot.regionalGrowthPerformanceByPartyIdCheck(map);
		}
		return listLastYearData;
	}
	
	@Override
	public List<HashMap<String, Object>> regionalGrowthPerformanceByPartyId(Map<String, Object> map) throws Exception {
		LinkedList<HashMap<String,Object>> resultList = new LinkedList<HashMap<String,Object>>();
		List<StaTable> listInfo = daot.getTheFirstLevel(map);
		
		map.put("beginDate", map.get("eBeginDate"));
		map.put("endDate", map.get("eEngDate"));
		int thisYear = (Integer) map.get("thisYear") -1;
		String con = (String) map.get("partys");
		if(Contents.BIG_TAB.equals(map.get("tab"))){
			map.put("partys", ( con + getDiffBigSql(thisYear)));
		}
		List<StaTable> listLastYearData = getlistLastYearData((String) map.get("coeff"),map);
		
		map.put("beginDate", map.get("sBeginDate"));
		map.put("endDate",  map.get("sEngDate"));
		thisYear = (Integer) map.get("thisYear");
		if(Contents.BIG_TAB.equals(map.get("tab"))){
			map.put("partys", ( con + getDiffBigSql(thisYear)));
		}
		List<StaTable> listThisYearData = getlistLastYearData((String) map.get("coeff"),map);;
		
		for (int i = 0; i < listInfo.size(); i++) {
			
			int YearFPS = 0;//每个用户今年促销员
			Double YearAmt = 0d;//每个用户今年销售金额
			int YearQty = 0;//每个用户今年销售数量
			for (int j = 0; j < listThisYearData.size(); j++) {//今年信息
				if(listInfo.get(i).getPartyIds().indexOf((","+listThisYearData.get(j).getPartyId()+",")) >= 0){
					YearFPS += listThisYearData.get(j).getNoOfFps();
					YearAmt += listThisYearData.get(j).getTTlTVSOAmt();
					YearQty += listThisYearData.get(j).getTtlTVSOQty();
				}
			}
			
			int lastYearFPS = 0;//每个用户去年促销员
			Double lastYearAmt = 0d;//每个用户去年销售金额
			int lastYearQty = 0;//每个用户去年销售数量
			for (int j = 0; j < listLastYearData.size(); j++) {//去年信息
				if(listInfo.get(i).getPartyIds().indexOf((","+listLastYearData.get(j).getPartyId()+",")) >= 0){
					lastYearFPS += listLastYearData.get(j).getNoOfFps();
					lastYearAmt += listLastYearData.get(j).getTTlTVSOAmt();
					lastYearQty += listLastYearData.get(j).getTtlTVSOQty();
				}
			}
			
			HashMap<String,Object> result = new HashMap<String,Object>();
			result.put("REGIONAL_HEAD", listInfo.get(i).getUserName());
			result.put("REGION", listInfo.get(i).getPartyName());
			result.put("FPS_YR_LASTYEAR", lastYearFPS);
			result.put("FPS_YR_THISYEAR", YearFPS);
			result.put("TT_QTY_LASTYEAR", lastYearQty);
			result.put("TT_QTY_THISYEAR", YearQty);
			String qtyGrowth = getBranchGrowthRate((double)YearQty,(double)lastYearQty);
			
			result.put("TT_QTY_GROWTH", qtyGrowth );
			result.put("SORT_TT_QTY_GROWTH", qtyGrowth + "%");
			result.put("TT_AMT_LASTYEAR", lastYearAmt);
			result.put("TT_AMT_THISYEAR", YearAmt);
			
			String amtGrowth = getBranchGrowthRate(YearAmt,lastYearAmt);
			result.put("TT_AMT_GROWTH", amtGrowth);
			resultList.add(result);
		}
		return DateUtil.Order(resultList, "SORT_TT_QTY_GROWTH");
	}

	@Override
	public JSONObject PSeriesSalesStatus(Map<String, Object> map) throws Exception {
		
		List<StaTable> getTFL = daot.getTheFirstLevels(map);
		List<StaTable> PSeriesSaless = null;
		
		if(IS_TRUE.equals(map.get("coeff"))){
			PSeriesSaless = daot.PSeriesSalesStatus(map);
		}else{
			PSeriesSaless = daot.PSeriesSalesStatusCheck(map);
		}
		
		String modelName = null;
		if(PSeriesSaless.size() > 0){
			modelName = PSeriesSaless.get(0).getModel();
		}
		
		JSONArray ja = new JSONArray();
		double[] val = new double[getTFL.size()];
		for (int i = 0; i < PSeriesSaless.size(); i++) {
			if(PSeriesSaless.get(i).getModel().equals(modelName)){
				for (int j = 0; j < getTFL.size(); j++) {
					if(getTFL.get(j).getPartyIds().indexOf(PSeriesSaless.get(i).getPartyId()) >= 0){
						val[j] += PSeriesSaless.get(i).getTtlTVSOQtyD();
						break;
					}
				}
				
				if(PSeriesSaless.size() - i == 1){
					JSONObject o = new JSONObject();
					o.accumulate("model", modelName);
					o.accumulate("val", val);
					ja.add(o);
				}
			}else{
				JSONObject o = new JSONObject();
				o.accumulate("model", modelName);
				o.accumulate("val", val);
				ja.add(o);
				
				modelName = PSeriesSaless.get(i).getModel();
				val = new double[getTFL.size()];
				i --;
			}
		}
		
		JSONObject resultJO = new JSONObject();
		resultJO.accumulate("TFL", getTFL);
		resultJO.accumulate("val", ja);
		return resultJO;
	}

	@Override
	public JSONObject queryBigSaleInfo(Map<String, Object> map) throws Exception {
		JSONObject o = new JSONObject();
		List<StaTable> saleInfo = null;
		if(IS_TRUE.equals(map.get("coeff"))){
			o.accumulate("month", daot.querysaleInfoByMonth(map)); //所有功能月度销售中总和
			o.accumulate("series", daot.querysaleInfoBySeries(map)); //指定功能销售总和
			saleInfo = daot.querysaleInfoByWhere(map);//销售基本信息
		}else{
			o.accumulate("month", daot.querysaleInfoByMonthCheck(map)); //所有功能月度销售中总和
			o.accumulate("series", daot.querysaleInfoBySeriesCheck(map)); //指定功能销售总和
			saleInfo = daot.querysaleInfoByWhereCheck(map);//销售基本信息
		}
		
		List<Map<String,Object>> resultMap = new ArrayList<Map<String,Object>>();
		int [] monthSaleNum = new int[12];
		String model = "";//储存第一个型号
		if(saleInfo.size() > 0){
			model = saleInfo.get(0).getModel();
		}
		for (int i = 0; i < saleInfo.size(); i++) {
			if(model.equals(saleInfo.get(i).getModel())){
				int indexMonth = WebPageUtil.isStringNullAvaliable(saleInfo.get(i).getMonth())?Integer.parseInt(saleInfo.get(i).getMonth()):0;
				monthSaleNum[indexMonth-1] += saleInfo.get(i).getTtlTVSOQty();
				if(saleInfo.size() - i == 1){
					setBigMap(resultMap,saleInfo.get(i).getModel(),saleInfo.get(i).getFunc(),saleInfo.get(i).getSize(),monthSaleNum);
				}
			}else{
				setBigMap(resultMap,saleInfo.get(i-1).getModel(),saleInfo.get(i-1).getFunc(),saleInfo.get(i-1).getSize(),monthSaleNum);
				monthSaleNum = new int[12];
				model = saleInfo.get(i).getModel();
				i--;
			}
		}
		o.accumulate("saleInfo", resultMap);
		return o;
	};
	
	public void setBigMap(List<Map<String, Object>> resultMap, String model, String func, String size, int[] monthSaleNum){
		Map<String, Object> m = new HashMap<String,Object>();
		m.put("model", model);
		m.put("func", func);
		m.put("size", size);
		m.put("monthVal", monthSaleNum);
		resultMap.add(m);
	}

	@Override
	public List<HashMap<String, Object>> queryStateTimeSalesBycountry(Map<String, Object> map) throws Exception {
		if("YEAR".equals(map.get("timeType")) || "HALF_A_YEAR".equals(map.get("timeType"))){
			return countryYearSaleInfo(map);
		}else{
			return countryQuarterSaleInfo(map);
		}
	};
	
	public String getHqDiffBigSql (int Year){
		if(Year >= 2018){
			return " and sr.`size` > CAST('55' as SIGNED) ";
		}else{
			return " and sr.`size` > CAST('48' as SIGNED) ";
		}
	}
	
	public LinkedList<HashMap<String, Object>> countryYearSaleInfo(Map<String, Object> map) throws Exception{
		LinkedList<HashMap<String,Object>> resultList = new LinkedList<HashMap<String,Object>>();
		
		String thisYearStartDate = (String) map.get("startDate");
		String thisYearEndDate = (String) map.get("endDate");
		
		String minSaleDate = getLastYearDate((String) map.get("startDate"));//最小时间
		String maxSaleDate = (String) map.get("endDate");//最大时间
		
		int yearInt = Integer.parseInt(((String) map.get("startDate")).split("-")[0]);//获取今年年份
		if(Contents.BIG_TAB.equals(map.get("tab"))){
			map.put("listWhe", getHqDiffBigSql(yearInt));
		};
		
		List<StaHQTable> listHqStaTable = IS_TRUE.equals(map.get("coeff"))? hqDaot.queryStateTimeSalesBycountryReduction(map):hqDaot.queryStateTimeSalesBycountry(map);
		List<HashMap<String,Object>> thisYearList = "YEAR".equals(map.get("timeType"))? getYearStatistics(listHqStaTable):getHalfAYearStatistics(listHqStaTable,map);//今年销量信息
		
		int lastInt = Integer.parseInt(((String) map.get("startDate")).split("-")[0]) - 1;//获取去年年份
		if(Contents.BIG_TAB.equals(map.get("tab"))){
			map.put("listWhe", getHqDiffBigSql(lastInt));
		};
		map.put("startDate", getLastYearDate((String) map.get("startDate")));
		map.put("endDate", getLastYearDate((String) map.get("endDate")));
		listHqStaTable = IS_TRUE.equals(map.get("coeff"))? hqDaot.queryStateTimeSalesBycountryReduction(map):hqDaot.queryStateTimeSalesBycountry(map);
		List<HashMap<String,Object>> lastYearList = "YEAR".equals(map.get("timeType"))? getYearStatistics(listHqStaTable):getHalfAYearStatistics(listHqStaTable,map);//去年销量信息
		
		if("HALF_A_YEAR".equals(map.get("timeType"))){
			map.put("thisYearStartDate", thisYearStartDate);
			map.put("thisYearEndDate", thisYearEndDate);
			map.put("isSub","true");
		}else{
			map.put("startDate", minSaleDate);
			map.put("endDate", maxSaleDate);
		}
		
		List<StaHQTable> couList = hqDaot.querySaleCountry(map);//有销量的国家
		
		for (int i = 0; i < couList.size(); i++) {
			HashMap<String,Object> couMap = new HashMap<String,Object>();
			couMap.put("country", couList.get(i).getCountry());
			couMap.put("center", couList.get(i).getCenter());
			//去年销售状况
			for (int j = 0; j < lastYearList.size(); j++) {
				if(couList.get(i).getCountry().equals(lastYearList.get(j).get("country"))){
					couMap.put("last_year_qty",  lastYearList.get(j).get("this_qty"));//去年总销量
					couMap.put("last_year_H1",  lastYearList.get(j).get("this_H1"));//去年上半年总销量
					couMap.put("last_year_H2",  lastYearList.get(j).get("this_H2"));//去年下半年总销量
					break;
				}
			}
			
			//今年销售状况
			for (int j = 0; j < thisYearList.size(); j++) {
				if(couList.get(i).getCountry().equals(thisYearList.get(j).get("country"))){
					couMap.put("this_year_qty",  thisYearList.get(j).get("this_qty"));//今年总销量
					couMap.put("this_year_H1",  thisYearList.get(j).get("this_H1"));//今年上半年总销量
					couMap.put("this_year_H2",  thisYearList.get(j).get("this_H2"));//今年下半年总销量
					break;
				}
			}
			resultList.add(couMap);
		}
		return statGrowth(resultList);
	};
	
	public LinkedList<HashMap<String, Object>> countryQuarterSaleInfo(Map<String, Object> map) throws Exception{
		LinkedList<HashMap<String,Object>> resultList = new LinkedList<HashMap<String,Object>>();
		
		String thisYearStartDate = (String) map.get("startDate");
		String thisYearEndDate = (String) map.get("endDate");
		
		int yearInt = Integer.parseInt(((String) map.get("startDate")).split("-")[0]);//获取今年年份
		if(Contents.BIG_TAB.equals(map.get("tab"))){
			map.put("listWhe", getHqDiffBigSql(yearInt));
		};
		
		List<StaHQTable> listHqStaTable = IS_TRUE.equals(map.get("coeff"))? hqDaot.queryStateTimeSalesBycountryReduction(map):hqDaot.queryStateTimeSalesBycountry(map);
		List<HashMap<String,Object>> thisYearList = getQuarterStatistics(listHqStaTable,map);//今年销量信息
		
		int lastInt = Integer.parseInt(((String) map.get("startDate")).split("-")[0]) - 1;//获取去年年份
		if(Contents.BIG_TAB.equals(map.get("tab"))){
			map.put("listWhe", getHqDiffBigSql(lastInt));
		};
		map.put("startDate", getLastYearDate((String) map.get("startDate")));
		map.put("endDate", getLastYearDate((String) map.get("endDate")));
		listHqStaTable = IS_TRUE.equals(map.get("coeff"))? hqDaot.queryStateTimeSalesBycountryReduction(map):hqDaot.queryStateTimeSalesBycountry(map);
		List<HashMap<String,Object>> lastYearList = getQuarterStatistics(listHqStaTable,map);//去年销量信息
		
		map.put("thisYearStartDate", thisYearStartDate);
		map.put("thisYearEndDate", thisYearEndDate);
		map.put("isSub","true");
		List<StaHQTable> couList = hqDaot.querySaleCountry(map);//有销量的国家
		
		for (int i = 0; i < couList.size(); i++) {
			HashMap<String,Object> couMap = new HashMap<String,Object>();
			couMap.put("country", couList.get(i).getCountry());
			couMap.put("center", couList.get(i).getCenter());
			//去年销售状况
			for (int j = 0; j < lastYearList.size(); j++) {
				if(couList.get(i).getCountry().equals(lastYearList.get(j).get("country"))){
					couMap.put("last_year_qty",  lastYearList.get(j).get("this_qty"));//去年单季度总销量
					couMap.put("last_Q1",  lastYearList.get(j).get("this_Q1"));//去年1月总销量
					couMap.put("last_Q2",  lastYearList.get(j).get("this_Q2"));//去年2月总销量
					couMap.put("last_Q3",  lastYearList.get(j).get("this_Q3"));//去年3月总销量
					break;
				}
			}
			
			//今年销售状况
			for (int j = 0; j < thisYearList.size(); j++) {
				if(couList.get(i).getCountry().equals(thisYearList.get(j).get("country"))){
					couMap.put("this_year_qty",  thisYearList.get(j).get("this_qty"));//去年单季度总销量
					couMap.put("this_Q1",  thisYearList.get(j).get("this_Q1"));//去年1月总销量
					couMap.put("this_Q2",  thisYearList.get(j).get("this_Q2"));//去年2月总销量
					couMap.put("this_Q3",  thisYearList.get(j).get("this_Q3"));//去年3月总销量
					break;
				}
			}
			resultList.add(couMap);
		}
		return statQuarterGrowth(resultList);
	};
	
	/**
	 * 统计增长率
	 * @param l
	 * @return
	 */
	public LinkedList<HashMap<String,Object>> statGrowth(LinkedList<HashMap<String,Object>> l){
		for (int i = 0; i < l.size(); i++) {
			String lastYearQty = new BigDecimal(isNullReturnZero(l.get(i).get("last_year_qty"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			String thisYearQty = new BigDecimal(isNullReturnZero(l.get(i).get("this_year_qty"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			l.get(i).put("year_growth", getGrowthRate(Integer.parseInt(lastYearQty) ,Integer.parseInt(thisYearQty)));
			String lastH1Qty = new BigDecimal(isNullReturnZero(l.get(i).get("last_year_H1"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			String thisH1Qty = new BigDecimal(isNullReturnZero(l.get(i).get("this_year_H1"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			l.get(i).put("H1_growth", getGrowthRate(Integer.parseInt(lastH1Qty),Integer.parseInt(thisH1Qty)));
			String lastH2Qty = new BigDecimal(isNullReturnZero(l.get(i).get("last_year_H2"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			String thisH2Qty = new BigDecimal(isNullReturnZero(l.get(i).get("this_year_H2"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			l.get(i).put("H2_growth", getGrowthRate(Integer.parseInt(lastH2Qty),Integer.parseInt(thisH2Qty)));
		}
		return l;
	};
	
	/**
	 * 季度统计增长率
	 * @param l
	 * @return
	 */
	public LinkedList<HashMap<String,Object>> statQuarterGrowth(LinkedList<HashMap<String,Object>> l){
		for (int i = 0; i < l.size(); i++) {
			String lastYearQty = new BigDecimal(isNullReturnZero(l.get(i).get("last_year_qty"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			String thisYearQty = new BigDecimal(isNullReturnZero(l.get(i).get("this_year_qty"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			l.get(i).put("year_growth", getGrowthRate(Integer.parseInt(lastYearQty) ,Integer.parseInt(thisYearQty)));
			
			String lastQ1Qty = new BigDecimal(isNullReturnZero(l.get(i).get("last_Q1"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			String thisQ1Qty = new BigDecimal(isNullReturnZero(l.get(i).get("this_Q1"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			l.get(i).put("Q1_growth", getGrowthRate(Integer.parseInt(lastQ1Qty),Integer.parseInt(thisQ1Qty)));
			
			String lastQ2Qty = new BigDecimal(isNullReturnZero(l.get(i).get("last_Q2"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			String thisQ2Qty = new BigDecimal(isNullReturnZero(l.get(i).get("this_Q2"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			l.get(i).put("Q2_growth", getGrowthRate(Integer.parseInt(lastQ2Qty),Integer.parseInt(thisQ2Qty)));
			
			String lastQ3Qty = new BigDecimal(isNullReturnZero(l.get(i).get("last_Q3"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			String thisQ3Qty = new BigDecimal(isNullReturnZero(l.get(i).get("this_Q3"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			l.get(i).put("Q3_growth", getGrowthRate(Integer.parseInt(lastQ3Qty),Integer.parseInt(thisQ3Qty)));
		}
		return l;
	};
	
	/**
	 * 入参为null返回“0”
	 * @param object.toString()
	 * @return
	 */
	public String isNullReturnZero(Object object){
		return object == null ? "0" : object.toString();
	}
	
	/**
	 * 计算增长率(今年-去年)/去年*100
	 * @return
	 */
	public String getGrowthRate(int lastQty,int thisQty){
		if(lastQty == 0 && thisQty == 0){
			return "0";
		}else if(lastQty == 0){
			return "100";
		}else if(thisQty == 0){
			return "-100";
		}else{
			double d = ((double)thisQty - (double)lastQty)/(double)lastQty * 100;
			return d+"";
		}
	};
	
	/**
	 * 计算增长率(今年-去年)/去年*100
	 * @return
	 */
	public String getBranchGrowthRate(Double yearAmt,Double lyAmt){
		if(yearAmt != 0 && lyAmt == 0){
			return "1";
		}else if(yearAmt == 0 && lyAmt != 0){
			return "-1";
		}else if(yearAmt == 0 && lyAmt == 0){
			return "0";
		}else{
			double d = ((double)yearAmt - (double)lyAmt)/(double)lyAmt;
			return "" + d;
		}
	};
	
	/**
	 * 获取去年指定时间
	 * @return
	 */
	public String getLastYearDate(String d){
		String [] dateArr = d.split("-");
		int lastYearInt = Integer.parseInt(dateArr[0]) - 1;
		return lastYearInt + "-" + dateArr[1] + "-" + dateArr[2];
	};
	
	/**
	 * 季度计算统计
	 * @param listSta
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> getQuarterStatistics(List<StaHQTable> listSta,Map<String, Object> map) throws Exception{
		
		List<HashMap<String,Object>> thisYearList = new ArrayList<HashMap<String,Object>>();//今年销量信息
		if(listSta.size() > 0){
			String county  = listSta.get(0).getCountry();//储存第一个国家
			HashMap<String,Object> couMap = new HashMap<String,Object>();//储存国家对象
			
			BigDecimal thisYear = BigDecimal.ZERO;//国家今年总销量
			BigDecimal thisQ1 = BigDecimal.ZERO;//国家今年1x月总销量
			BigDecimal thisQ2 = BigDecimal.ZERO;//国家今年2x月总销量
			BigDecimal thisQ3 = BigDecimal.ZERO;//国家今年3x月总销量
			
			int [] dateSub = getMonthByQuarter((String)map.get("halfAYear"));
			
			for (int i = 0; i < listSta.size(); i++) {
				if (county.equals(listSta.get(i).getCountry())) {
					if(Integer.parseInt(listSta.get(i).getMoth()) == dateSub[0]){
						thisQ1 = thisQ1.add(new BigDecimal(listSta.get(i).getQty()));//累加国家1x月总销量
					}else if(Integer.parseInt(listSta.get(i).getMoth()) == dateSub[1]){
						thisQ2 = thisQ2.add(new BigDecimal(listSta.get(i).getQty()));//累加国家2x月总销量
					}else{
						thisQ3 = thisQ3.add(new BigDecimal(listSta.get(i).getQty()));//累加国家3x月总销量
					}
					thisYear = thisYear.add(new BigDecimal(listSta.get(i).getQty()));//累加国家全年销量
					
					//存储最后一个国家
					if(listSta.size() - i == 1){
						couMap.put("center", listSta.get(i).getCenter());//存储业务中心
						couMap.put("country", listSta.get(i).getCountry());//存储国家
						couMap.put("this_Q1", thisQ1.toString());//存储国家1x月总销量
						couMap.put("this_Q2", thisQ2.toString());//存储国家2x月总销量
						couMap.put("this_Q3", thisQ3.toString());//存储国家3x月总销量
						couMap.put("this_qty", thisYear.toString());//存储国家今年总销量
						thisYearList.add(couMap);
					}
					
				}else{
					couMap.put("center", listSta.get(i-1).getCenter());//存储业务中心
					couMap.put("country", listSta.get(i-1).getCountry());//存储国家
					couMap.put("this_Q1", thisQ1.toString());//存储国家1x月总销量
					couMap.put("this_Q2", thisQ2.toString());//存储国家2x月总销量
					couMap.put("this_Q3", thisQ3.toString());//存储国家3x月总销量
					couMap.put("this_qty", thisYear.toString());//存储国家今年总销量
					thisYearList.add(couMap);
					
					//初始化数据
					county  = listSta.get(i).getCountry();//储存第一个国家
					couMap = new HashMap<String,Object>();
					thisYear = new BigDecimal("0");
					thisQ1 = new BigDecimal("0");
					thisQ2 = new BigDecimal("0");
					thisQ3 = new BigDecimal("0");
					i --;
				}
			}
		}
		return thisYearList;
	};
	
	public int[] getMonthByQuarter(String q){
		if("Q1".equals(q)){
			return new int[]{1,2,3};
		}else if("Q2".equals(q)){
			return new int[]{4,5,6};
		}else if("Q3".equals(q)){
			return new int[]{7,8,9};
		}else{
			return new int[]{10,11,12};
		}
	};
	
	/**
	 * 半年度计算统计
	 * @param listSta
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> getHalfAYearStatistics(List<StaHQTable> listSta,Map<String, Object> map) throws Exception{
		
		List<HashMap<String,Object>> thisYearList = new ArrayList<HashMap<String,Object>>();//今年销量信息
		if(listSta.size() > 0){
			String county  = listSta.get(0).getCountry();//储存第一个国家
			HashMap<String,Object> couMap = new HashMap<String,Object>();//储存国家对象
			
			BigDecimal thisYear = BigDecimal.ZERO;//国家今年总销量
			BigDecimal thisH1 = BigDecimal.ZERO;//国家今年上半年总销量
			BigDecimal thisH2 = BigDecimal.ZERO;//国家今年下半年总销量
			int dateSub = "H1".equals(map.get("halfAYear"))? 4 : 10;//上、下半年季度分割线
			
			for (int i = 0; i < listSta.size(); i++) {
				if (county.equals(listSta.get(i).getCountry())) {
					if(Integer.parseInt(listSta.get(i).getMoth()) < dateSub){
						thisH1 = thisH1.add(new BigDecimal(listSta.get(i).getQty()));//累加国家上半年销量
					}else{
						thisH2 = thisH2.add(new BigDecimal(listSta.get(i).getQty()));//累加国家下半年销量
					}
					thisYear = thisYear.add(new BigDecimal(listSta.get(i).getQty()));//累加国家全年销量
					
					//存储最后一个国家
					if(listSta.size() - i == 1){
						couMap.put("center", listSta.get(i).getCenter());//存储业务中心
						couMap.put("country", listSta.get(i).getCountry());//存储国家
						couMap.put("this_H1", thisH1.toString());//存储国家今年上半年总销量
						couMap.put("this_H2", thisH2.toString());//存储国家今年下半年总销量
						couMap.put("this_qty", thisYear.toString());//存储国家今年总销量
						thisYearList.add(couMap);
					}
					
				}else{
					couMap.put("center", listSta.get(i-1).getCenter());//存储业务中心
					couMap.put("country", listSta.get(i-1).getCountry());//存储国家
					couMap.put("this_H1", thisH1.toString());//存储国家今年上半年总销量
					couMap.put("this_H2", thisH2.toString());//存储国家今年下半年总销量
					couMap.put("this_qty", thisYear.toString());//存储国家今年总销量
					thisYearList.add(couMap);
					
					//初始化数据
					county  = listSta.get(i).getCountry();//储存第一个国家
					couMap = new HashMap<String,Object>();
					thisYear = new BigDecimal("0");
					thisH1 = new BigDecimal("0");
					thisH2 = new BigDecimal("0");
					i --;
				}
			}
		}
		return thisYearList;
	};

	/**
	 * 年度计算统计
	 * @param listSta
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> getYearStatistics(List<StaHQTable> listSta) throws Exception{
		
		List<HashMap<String,Object>> thisYearList = new ArrayList<HashMap<String,Object>>();//今年销量信息
		if(listSta.size() > 0){
			String county  = listSta.get(0).getCountry();//储存第一个国家
			HashMap<String,Object> couMap = new HashMap<String,Object>();//储存国家对象
			
			BigDecimal thisYear = BigDecimal.ZERO;//国家今年总销量
			BigDecimal thisH1 = BigDecimal.ZERO;//国家今年上半年总销量
			BigDecimal thisH2 = BigDecimal.ZERO;//国家今年下半年总销量
			
			for (int i = 0; i < listSta.size(); i++) {
				if (county.equals(listSta.get(i).getCountry())) {
					if(Integer.parseInt(listSta.get(i).getMoth()) < 7){
						thisH1 = thisH1.add(new BigDecimal(listSta.get(i).getQty()));//累加国家上半年销量
					}else{
						thisH2 = thisH2.add(new BigDecimal(listSta.get(i).getQty()));//累加国家下半年销量
					}
					thisYear = thisYear.add(new BigDecimal(listSta.get(i).getQty()));//累加国家全年销量
					
					//存储最后一个国家
					if(listSta.size() - i == 1){
						couMap.put("center", listSta.get(i).getCenter());//存储业务中心
						couMap.put("country", listSta.get(i).getCountry());//存储国家
						couMap.put("this_H1", thisH1.toString());//存储国家今年上半年总销量
						couMap.put("this_H2", thisH2.toString());//存储国家今年下半年总销量
						couMap.put("this_qty", thisYear.toString());//存储国家今年总销量
						thisYearList.add(couMap);
					}
					
				}else{
					couMap.put("center", listSta.get(i-1).getCenter());//存储业务中心
					couMap.put("country", listSta.get(i-1).getCountry());//存储国家
					couMap.put("this_H1", thisH1.toString());//存储国家今年上半年总销量
					couMap.put("this_H2", thisH2.toString());//存储国家今年下半年总销量
					couMap.put("this_qty", thisYear.toString());//存储国家今年总销量
					thisYearList.add(couMap);
					
					//初始化数据
					county  = listSta.get(i).getCountry();//储存第一个国家
					couMap = new HashMap<String,Object>();
					thisYear = new BigDecimal("0");
					thisH1 = new BigDecimal("0");
					thisH2 = new BigDecimal("0");
					i --;
				}
			}
		}
		return thisYearList;
	};
}




