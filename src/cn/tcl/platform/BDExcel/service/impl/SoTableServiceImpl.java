package cn.tcl.platform.BDExcel.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.BDExcel.dao.IBDSCSoTableDao;
import cn.tcl.platform.BDExcel.service.ISoTableService;
import cn.tcl.platform.BDExcel.vo.BDTable;


@Service("BDSCsoTableService")
public class SoTableServiceImpl implements ISoTableService {
	@Autowired
	private IBDSCSoTableDao soTableDao;
	
	private final String IS_TRUE = "true";

	
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat sdw = new SimpleDateFormat("E");
	static SimpleDateFormat formatEn = new SimpleDateFormat("MMMM.dd,yyyy",
			Locale.ENGLISH);

	static SimpleDateFormat mm = new SimpleDateFormat("MMMM", Locale.ENGLISH);
	static SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.ENGLISH);






	
	public String getDiffBigSql (int Year){
		if(Year >= 2018){
			return " and pr.`size` > CAST('55' as SIGNED) ";
		}else{
			return " and pr.`size` > CAST('48' as SIGNED) ";
		}
	}


	@Override
	public JSONObject selectSpecPro(Map<String, Object> whereMap) {
		LinkedList<HashMap<String, Object>> dataList = new LinkedList<HashMap<String, Object>>();
		
		List<HashMap<String, Object>> bigReg = soTableDao
				.selectBigReg(whereMap);
		
		whereMap.put("spec","%BASIC LED%" );
		List<HashMap<String, Object>> specyByParty = soTableDao.selectModelListBySpecParty(whereMap);

		for (int a = 0; a < bigReg.size(); a++) {
			BigDecimal Qty=BigDecimal.ZERO;
			
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("reg",bigReg.get(a).get("PARTY_NAME") );
			dataMap.put("spec","BASIC LED");
			for (int i = 0; i < specyByParty.size(); i++) {
				String party=","+specyByParty.get(i).get("PARTY_ID")+",";
				if(bigReg.get(a).get("small").toString().contains(party)){
					BigDecimal bd=new BigDecimal(specyByParty.get(i).get("quantity").toString());
					Qty=Qty.add(bd);
				}
			}
			dataMap.put("qty",Math.round(Qty.doubleValue()));
			dataList.add(dataMap);
		}
			
		
		
		
		whereMap.put("spec","%DIGITAL%" );
		specyByParty = soTableDao.selectModelListBySpecParty(whereMap);

		for (int a = 0; a < bigReg.size(); a++) {
			BigDecimal Qty=BigDecimal.ZERO;
			
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("reg",bigReg.get(a).get("PARTY_NAME") );
			dataMap.put("spec","DIGITAL");
			for (int i = 0; i < specyByParty.size(); i++) {
				String party=","+specyByParty.get(i).get("PARTY_ID")+",";
				if(bigReg.get(a).get("small").toString().contains(party)){
					BigDecimal bd=new BigDecimal(specyByParty.get(i).get("quantity").toString());
					Qty=Qty.add(bd);
				}
			}
			dataMap.put("qty",Math.round(Qty.doubleValue()));
			dataList.add(dataMap);
		}
			
		
		whereMap.put("spec","%INTERNET%" );
		specyByParty = soTableDao.selectModelListBySpecParty(whereMap);

		for (int a = 0; a < bigReg.size(); a++) {
			BigDecimal Qty=BigDecimal.ZERO;
			
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("reg",bigReg.get(a).get("PARTY_NAME") );
			dataMap.put("spec","INTERNET");
			for (int i = 0; i < specyByParty.size(); i++) {
				String party=","+specyByParty.get(i).get("PARTY_ID")+",";
				if(bigReg.get(a).get("small").toString().contains(party)){
					BigDecimal bd=new BigDecimal(specyByParty.get(i).get("quantity").toString());
					Qty=Qty.add(bd);
				}
			}
			dataMap.put("qty",Math.round(Qty.doubleValue()));
			dataList.add(dataMap);
		}
			
		
		whereMap.put("spec","%SMART%" );
		specyByParty = soTableDao.selectModelListBySpecParty(whereMap);

		for (int a = 0; a < bigReg.size(); a++) {
			BigDecimal Qty=BigDecimal.ZERO;
			
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("reg",bigReg.get(a).get("PARTY_NAME") );
			dataMap.put("spec","SMART");
			for (int i = 0; i < specyByParty.size(); i++) {
				String party=","+specyByParty.get(i).get("PARTY_ID")+",";
				if(bigReg.get(a).get("small").toString().contains(party)){
					BigDecimal bd=new BigDecimal(specyByParty.get(i).get("quantity").toString());
					Qty=Qty.add(bd);
				}
			}
			dataMap.put("qty",Math.round(Qty.doubleValue()));
			dataList.add(dataMap);
		}
			
		
		whereMap.put("spec","%UHD%" );
		specyByParty = soTableDao.selectModelListBySpecParty(whereMap);

		for (int a = 0; a < bigReg.size(); a++) {
			BigDecimal Qty=BigDecimal.ZERO;
			
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("reg",bigReg.get(a).get("PARTY_NAME") );
			dataMap.put("spec","UHD");
			for (int i = 0; i < specyByParty.size(); i++) {
				String party=","+specyByParty.get(i).get("PARTY_ID")+",";
				if(bigReg.get(a).get("small").toString().contains(party)){
					BigDecimal bd=new BigDecimal(specyByParty.get(i).get("quantity").toString());
					Qty=Qty.add(bd);
				}
			}
			dataMap.put("qty",Math.round(Qty.doubleValue()));
			dataList.add(dataMap);
		}
			
		
		whereMap.put("spec","%CURVED%" );
		specyByParty = soTableDao.selectModelListBySpecParty(whereMap);

		for (int a = 0; a < bigReg.size(); a++) {
			BigDecimal Qty=BigDecimal.ZERO;
			
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("reg",bigReg.get(a).get("PARTY_NAME") );
			dataMap.put("spec","CURVED");
			for (int i = 0; i < specyByParty.size(); i++) {
				String party=","+specyByParty.get(i).get("PARTY_ID")+",";
				if(bigReg.get(a).get("small").toString().contains(party)){
					BigDecimal bd=new BigDecimal(specyByParty.get(i).get("quantity").toString());
					Qty=Qty.add(bd);
				}
			}
			dataMap.put("qty",Math.round(Qty.doubleValue()));
			dataList.add(dataMap);
		}
			
		
		//查询DIGITAL BASIC LED
		Map<String, BigDecimal> mapResultDBL = new HashMap<String, BigDecimal>();
		for (int i = 0; i < dataList.size(); i++) {
			HashMap<String, Object> dataMap = dataList.get(i);
			if(dataMap.get("spec").equals("BASIC LED") || dataMap.get("spec").equals("DIGITAL")){
				BigDecimal numOne = mapResultDBL.get(dataMap.get("PARTY_NAME").toString());
				 if (numOne == null) {
					  numOne = BigDecimal.ZERO;
				  }
				  BigDecimal b=new BigDecimal(dataMap.get("qty").toString());
				  mapResultDBL.put(dataMap.get("PARTY_NAME").toString(),numOne.add(b)) ;
			}
				 
		}
			
		
		//查询Smart Tv
		Map<String, BigDecimal> mapResultSmart = new HashMap<String, BigDecimal>();
		for (int i = 0; i < dataList.size(); i++) {
			HashMap<String, Object> dataMap = dataList.get(i);
			if(dataMap.get("spec").equals("SMART") || dataMap.get("spec").equals("UHD")
					|| dataMap.get("spec").equals("CURVED")){
				BigDecimal numOne = mapResultSmart.get(dataMap.get("PARTY_NAME").toString());
				 if (numOne == null) {
					  numOne = BigDecimal.ZERO;
				  }
				  BigDecimal b=new BigDecimal(dataMap.get("qty").toString());
				  mapResultSmart.put(dataMap.get("PARTY_NAME").toString(),numOne.add(b)) ;
			}
				 
		}
			
		//查询Flat UHD
		Map<String, BigDecimal> mapResultFlatUHD = new HashMap<String, BigDecimal>();
		for (int i = 0; i < dataList.size(); i++) {
			HashMap<String, Object> dataMap = dataList.get(i);
			if(dataMap.get("spec").equals("SMART") || dataMap.get("spec").equals("UHD")
					|| dataMap.get("spec").equals("CURVED")){
				BigDecimal numOne = mapResultFlatUHD.get(dataMap.get("PARTY_NAME").toString());
				 if (numOne == null) {
					  numOne = BigDecimal.ZERO;
				  }
				  BigDecimal b=new BigDecimal(dataMap.get("qty").toString());
				  mapResultFlatUHD.put(dataMap.get("PARTY_NAME").toString(),numOne.add(b)) ;
			}
				 
		}
			
		
		
		//查询CURVED FHD
		Map<String, BigDecimal> mapResultCURVEDFHD = new HashMap<String, BigDecimal>();
		for (int i = 0; i < dataList.size(); i++) {
			HashMap<String, Object> dataMap = dataList.get(i);
			if(dataMap.get("spec").equals("SMART") 
					|| dataMap.get("spec").equals("CURVED")){
				BigDecimal numOne = mapResultCURVEDFHD.get(dataMap.get("PARTY_NAME").toString());
				 if (numOne == null) {
					  numOne = BigDecimal.ZERO;
				  }
				  BigDecimal b=new BigDecimal(dataMap.get("qty").toString());
				  mapResultCURVEDFHD.put(dataMap.get("PARTY_NAME").toString(),numOne.add(b)) ;
			}
				 
		}
			
		
		
		//查询CURVED UHD
		Map<String, BigDecimal> mapResultCURVEDUHD = new HashMap<String, BigDecimal>();
		for (int i = 0; i < dataList.size(); i++) {
			HashMap<String, Object> dataMap = dataList.get(i);
			if(dataMap.get("spec").equals("SMART") || dataMap.get("spec").equals("UHD")
					|| dataMap.get("spec").equals("CURVED")){
				BigDecimal numOne = mapResultCURVEDUHD.get(dataMap.get("PARTY_NAME").toString());
				 if (numOne == null) {
					  numOne = BigDecimal.ZERO;
				  }
				  BigDecimal b=new BigDecimal(dataMap.get("qty").toString());
				  mapResultCURVEDUHD.put(dataMap.get("PARTY_NAME").toString(),numOne.add(b)) ;
			}
				 
		}
			
		
		

		return null;
	}

	@Override
	public JSONObject selectModelByMonth(Map<String, Object> whereMap) {
		List<HashMap<String, Object>> modelList = soTableDao.selectModelByMonth(whereMap);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		
		String model="";
		if(modelList.size()>=1){
			model=modelList.get(0).get("model").toString();
		}
		String month=whereMap.get("endDate").toString().split("-")[1];
		int [] val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		for (int i = 0; i < modelList.size(); i++) {
			if(WebPageUtil.isStringNullAvaliable(model) && model.equals(modelList.get(i).get("model").toString())){
				String date=modelList.get(i).get("month").toString();
				int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
				BigDecimal bd=new BigDecimal(modelList.get(i).get("qty").toString());
				int qty=(int)Math.round(bd.doubleValue());
				
				val[dte -1] = qty;
				
				if(modelList.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("model",model);
					jsonObject.accumulate("arr", val);
					array.add(jsonObject);
				}
				
			}else{
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("model",model);
					jsonObject.accumulate("arr", val);
					array.add(jsonObject);
					model = modelList.get(i).get("model").toString();
					val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
					i--;

				
			}
		}
		object.accumulate("data", array);
		return object;
	}

	public int []  getQty(int length){
		int[] a= new int[length];
		for (int i = 0; i < a.length; i++) {
			a[i] = 0;
		}
		return a;
	}

	
	public int []  getAmt(int length){
		int[] a= new int[length];
		for (int i = 0; i < a.length; i++) {
			a[i] = 0;
		}
		return a;
	}

	@Override
	public List<HashMap<String, Object>> selectPartyCus(
			Map<String, Object> whereMap) {
		return soTableDao.selectPartyCus(whereMap);
	}


	@Override
	public List<HashMap<String, Object>> selectXCPLine(
			Map<String, Object> whereMap) {
		return soTableDao.selectXCPLine(whereMap);
	}


	@Override
	public JSONObject selectGrowthByCountry(Map<String, Object> whereMap) {
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();

		
		String[] toYearBegin = whereMap.get("beginDate").toString().split("-");
		String[] toYearEnd = whereMap.get("endDate").toString().split("-");
		int laYear = Integer.parseInt(toYearBegin[0].toString()) - 1;
		whereMap.put("beginDate", laYear + "-" + toYearBegin[1] + "-"+ toYearBegin[2]);
		//whereMap.put("endDate", laYear + "-" + toYearEnd[1] + "-"+ toYearEnd[2]);
	
		
		List<HashMap<String, Object>> countryToDatas = soTableDao
				.selectGrowthByCountry(whereMap);
		
		
		/*List<HashMap<String, Object>> countryLaDatas = soTableDao
				.selectGrowthByCountry(whereMap);*/
		
		
		String year="";
		if(countryToDatas.size()>=1){
			year=countryToDatas.get(0).get("year").toString();
		}
		String month=whereMap.get("endDate").toString().split("-")[1];
		int [] valQty = getQty(12);
		int [] valAmt = getAmt(12);
		for (int i = 0; i < countryToDatas.size(); i++) {
			if(WebPageUtil.isStringNullAvaliable(year) && year.equals(countryToDatas.get(i).get("year").toString())){
				String date=countryToDatas.get(i).get("month").toString();
				int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
				BigDecimal bd=new BigDecimal(countryToDatas.get(i).get("qty").toString());
				int qty=(int)Math.round(bd.doubleValue());
				bd=new BigDecimal(countryToDatas.get(i).get("amt").toString());
				int amt=(int)Math.round(bd.doubleValue());
				valQty[dte -1] = qty;
				valAmt[dte -1] = amt;
				if(countryToDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("year",year);
					jsonObject.accumulate("arrQty", valQty);
					jsonObject.accumulate("arrAmt", valAmt);
					array.add(jsonObject);
				}
				
			}else{

				JSONObject jsonObject=new JSONObject();
				jsonObject.accumulate("year",year);
				jsonObject.accumulate("arrQty", valQty);
				jsonObject.accumulate("arrAmt", valAmt);
				array.add(jsonObject);
				year = countryToDatas.get(i).get("year").toString();
				valQty = getQty(12);
				valAmt = getAmt(12);
				i--;
			}
		}
		
		
		
		JSONObject jsonObject=new JSONObject();
		jsonObject.accumulate("data",array);
		return jsonObject;
	}


	@Override
	public JSONObject selectDataByAreaChain(Map<String, Object> whereMap) {
		

		List<HashMap<String, Object>> bigReg = soTableDao
				.selectBigReg(whereMap);
		
		List<HashMap<String, Object>> re = soTableDao
				.selectRegionalHeadByParty(whereMap);
		
		List<HashMap<String, Object>> areaDatas = soTableDao
				.selectDataByAreaChain(whereMap);
		
		
		
		

		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		JSONArray arrayTT=new JSONArray();
		String party="";
		if(areaDatas.size()>=1){
			party=areaDatas.get(0).get("PARTY_ID").toString();
		}
		String month=whereMap.get("endDate").toString().split("-")[1];
		int [] val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		int [] valAmt = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		for (int i = 0; i < areaDatas.size(); i++) {
			if(WebPageUtil.isStringNullAvaliable(party) && party.equals(areaDatas.get(i).get("PARTY_ID").toString())){
				String date=areaDatas.get(i).get("month").toString();
				int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
				BigDecimal bd=new BigDecimal(areaDatas.get(i).get("saleQty").toString());
				int qty=(int)Math.round(bd.doubleValue());
				
				bd=new BigDecimal(areaDatas.get(i).get("saleSum").toString());
				int amt=(int)Math.round(bd.doubleValue());
				
				val[dte -1] = qty;
				valAmt[dte -1] = amt;
				if(areaDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("party",party);
					jsonObject.accumulate("arrQty", val);
					jsonObject.accumulate("arrAmt", valAmt);
					array.add(jsonObject);
				}
				
			}else{
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("party",party);
					jsonObject.accumulate("arrQty", val);
					jsonObject.accumulate("arrAmt", valAmt);
					array.add(jsonObject);
					party = areaDatas.get(i).get("PARTY_ID").toString();
					val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
					valAmt = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
					i--;

				
			}
		}
		
		for (int a = 0; a < bigReg.size(); a++) {
			
			
			JSONObject dataMap = new JSONObject();
			for (int i = 0; i < re.size(); i++) {
				if (bigReg.get(a).get("PARTY_ID")
						.equals(re.get(i).get("PARTY_ID"))) {
					dataMap.put("RegionalHead", re.get(i).get("userName"));
				}
			}
			
			dataMap.put("AREA", bigReg.get(a).get("PARTY_NAME"));
			dataMap.put("NUM",a+1);
		
			
			int [] arrQty = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
			int [] arrAmt = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj=(JSONObject) array.get(i);
				String pa=","+obj.get("party")+",";
				if(bigReg.get(a).get("small").toString().contains(pa)){
					JSONArray one=(JSONArray) obj.get("arrQty");
					JSONArray two= (JSONArray) obj.get("arrAmt");
					for (int j = 0; j < one.size(); j++) {
						arrQty[j]+=one.getInt(j);
					}
					for (int j = 0; j < two.size(); j++) {
						arrAmt[j]+=two.getInt(j);
					}
					
				
				
				}
			}
			
			
			BigDecimal amt=BigDecimal.ZERO;
			for (int i = 0; i < arrAmt.length; i++) {
				amt=amt.add(new BigDecimal(arrAmt[i]));
			}
			
			dataMap.put("Total",  amt);
			dataMap.put("arrQty", arrQty);
			dataMap.put("arrAmt", arrAmt);

			arrayTT.add(dataMap);

		}
		
		BDDateUtil.OrderAmt(arrayTT,"Total");
		
		object.accumulate("data", arrayTT);
		return object;
	}


	@Override
	public JSONObject selectSalemanDataByChain(Map<String, Object> whereMap) {
		
		
		List<HashMap<String, Object>> saleDatas = soTableDao
				.selectSalemanDataByChain(whereMap);

		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		
		String user="";
		if(saleDatas.size()>=1){
			user=saleDatas.get(0).get("userId").toString();
		}
		String month=whereMap.get("endDate").toString().split("-")[1];
		int [] val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		int [] valAmt = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		for (int i = 0; i < saleDatas.size(); i++) {
			if(WebPageUtil.isStringNullAvaliable(user) && user.equals(saleDatas.get(i).get("userId").toString())){
				String date=saleDatas.get(i).get("month").toString();
				int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
				BigDecimal bd=new BigDecimal(saleDatas.get(i).get("saleQty").toString());
				int qty=(int)Math.round(bd.doubleValue());
				
				bd=new BigDecimal(saleDatas.get(i).get("saleSum").toString());
				int amt=(int)Math.round(bd.doubleValue());
				
				val[dte -1] = qty;
				valAmt[dte -1] = amt;
				if(saleDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("user",user);
					jsonObject.accumulate("userName",saleDatas.get(i).get("userName").toString());
					jsonObject.accumulate("arrQty", val);
					jsonObject.accumulate("arrAmt", valAmt);
					array.add(jsonObject);
				}
				
			}else{
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("user",user);
					jsonObject.accumulate("userName",saleDatas.get(i).get("userName").toString());
					jsonObject.accumulate("arrQty", val);
					jsonObject.accumulate("arrAmt", valAmt);
					array.add(jsonObject);
					user = saleDatas.get(i).get("userId").toString();
					val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
					valAmt = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
					i--;

				
			}
		}
		
	
		
		
		
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject=(JSONObject) array.get(i);
			jsonObject.accumulate("num",i+1 );
			JSONArray arrAmt=(JSONArray) jsonObject.get("arrAmt");
			BigDecimal amt=BigDecimal.ZERO;
			for (int j = 0; j < arrAmt.size(); j++) {
				amt=amt.add(new BigDecimal(arrAmt.get(j).toString()));
			}
			
			jsonObject.accumulate("Total",amt);
		}
		BDDateUtil.OrderAmt(array, "Total");
		object.accumulate("data", array);
		return object;
	}


	@Override
	public JSONObject selectAcfoDataByChain(Map<String, Object> whereMap) {
		List<HashMap<String, Object>> saleDatas = soTableDao
				.selectAcfoDataByChain(whereMap);

		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		List<HashMap<String, Object>> Area = soTableDao.selectAreaByUser(whereMap);
		String user="";
		if(saleDatas.size()>=1){
			user=saleDatas.get(0).get("userId").toString();
		}
		String month=whereMap.get("endDate").toString().split("-")[1];
		int [] val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		int [] valAmt = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		for (int i = 0; i < saleDatas.size(); i++) {
			if(WebPageUtil.isStringNullAvaliable(user) && user.equals(saleDatas.get(i).get("userId").toString())){
				String date=saleDatas.get(i).get("month").toString();
				int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
				BigDecimal bd=new BigDecimal(saleDatas.get(i).get("saleQty").toString());
				int qty=(int)Math.round(bd.doubleValue());
				
				bd=new BigDecimal(saleDatas.get(i).get("saleSum").toString());
				int amt=(int)Math.round(bd.doubleValue());
				
				val[dte -1] = qty;
				valAmt[dte -1] = amt;
				if(saleDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("user",user);
					jsonObject.accumulate("userName",saleDatas.get(i).get("userName").toString());
					jsonObject.accumulate("arrQty", val);
					jsonObject.accumulate("arrAmt", valAmt);
				
					
					
					array.add(jsonObject);
				}
				
			}else{
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("user",user);
					jsonObject.accumulate("userName",saleDatas.get(i).get("userName").toString());
					jsonObject.accumulate("arrQty", val);
					jsonObject.accumulate("arrAmt", valAmt);
					array.add(jsonObject);
					user = saleDatas.get(i).get("userId").toString();
					val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
					valAmt = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
					i--;

				
			}
		}
		
		
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject=(JSONObject) array.get(i);
			jsonObject.accumulate("num",i+1 );
			JSONArray arrAmt=(JSONArray) jsonObject.get("arrAmt");
			BigDecimal amt=BigDecimal.ZERO;
			for (int j = 0; j < arrAmt.size(); j++) {
				amt=amt.add(new BigDecimal(arrAmt.get(j).toString()));
			}
			
			jsonObject.accumulate("Total", amt);
		}
		BDDateUtil.OrderAmt(array, "Total");
		object.accumulate("data", array);
		
		
		return object;
	}


	@Override
	public JSONObject selectCountryDataByChain(Map<String, Object> whereMap) {
		JSONArray array=new JSONArray();

	
		
		List<HashMap<String, Object>> countryToDatas = soTableDao
				.selectGrowthByCountry(whereMap);
		
		
		String year="";
		if(countryToDatas.size()>1){
			year=countryToDatas.get(0).get("year").toString();
		}
		String month=whereMap.get("endDate").toString().split("-")[1];
		
		int [] valQty = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		int [] valAmt = getAmt(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		for (int i = 0; i < countryToDatas.size(); i++) {
			if(WebPageUtil.isStringNullAvaliable(year) && year.equals(countryToDatas.get(i).get("year").toString())){
				String date=countryToDatas.get(i).get("month").toString();
				int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
				BigDecimal bd=new BigDecimal(countryToDatas.get(i).get("qty").toString());
				int qty=(int)Math.round(bd.doubleValue());
				bd=new BigDecimal(countryToDatas.get(i).get("amt").toString());
				int amt=(int)Math.round(bd.doubleValue());
				valQty[dte -1] = qty;
				valAmt[dte -1] = amt;
				if(countryToDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("year",year);
					jsonObject.accumulate("arrQty", valQty);
					jsonObject.accumulate("arrAmt", valAmt);
					array.add(jsonObject);
				}
				
			}else{
				JSONObject jsonObject=new JSONObject();
				jsonObject.accumulate("year",year);
				jsonObject.accumulate("arrQty", valQty);
				jsonObject.accumulate("arrAmt", valAmt);
				array.add(jsonObject);
				year = countryToDatas.get(i).get("year").toString();
				valQty = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
				valAmt = getAmt(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
				i--;
			}
		}
		
		
		JSONObject jsonObject=new JSONObject();
		jsonObject.accumulate("data",array);
		return jsonObject;
	}


	@Override
	public List<HashMap<String, Object>> selectXCPModel(
			Map<String, Object> whereMap) {
		return soTableDao.selectXCPModel(whereMap);
	}


	@Override
	public JSONObject selectCountryBigByYear(Map<String, Object> whereMap) {

		
		List<HashMap<String, Object>> saleDatas = soTableDao
				.selectModelByMonth(whereMap);

		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		
		String model="";
		if(saleDatas.size()>=1){
			model=saleDatas.get(0).get("model").toString();
		}
		String month=whereMap.get("endDate").toString().split("-")[1];
		int [] val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		int [] valAmt = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
		for (int i = 0; i < saleDatas.size(); i++) {
			if(WebPageUtil.isStringNullAvaliable(model) && model.equals(saleDatas.get(i).get("model").toString())){
				String date=saleDatas.get(i).get("month").toString();
				int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
				BigDecimal bd=new BigDecimal(saleDatas.get(i).get("qty").toString());
				int qty=(int)Math.round(bd.doubleValue());
				
				bd=new BigDecimal(saleDatas.get(i).get("amt").toString());
				int amt=(int)Math.round(bd.doubleValue());
				
				val[dte -1] = qty;
				valAmt[dte -1] = amt;
				if(saleDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("model",model);
					jsonObject.accumulate("size",saleDatas.get(i).get("size").toString());
					jsonObject.accumulate("spec",saleDatas.get(i).get("spec").toString());
					jsonObject.accumulate("arrQty", val);
					jsonObject.accumulate("arrAmt", valAmt);
					array.add(jsonObject);
				}
				
			}else{
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("model",model);
					jsonObject.accumulate("size",saleDatas.get(i-1).get("size").toString());
					jsonObject.accumulate("spec",saleDatas.get(i-1).get("spec").toString());
					jsonObject.accumulate("arrQty", val);
					jsonObject.accumulate("arrAmt", valAmt);
					array.add(jsonObject);
					model = saleDatas.get(i).get("model").toString();
					val = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
					valAmt = getQty(WebPageUtil.isStringNullAvaliable(month)?Integer.parseInt(month):0);
					i--;

				
			}
		}
		
		object.accumulate("data", array);
		return object;
	}


	@Override
	public List<HashMap<String, Object>> selectCurvedModel(
			Map<String, Object> whereMap) {
		return soTableDao.selectCurvedModel(whereMap);
	}


	@Override
	public XSSFWorkbook read2007Excel(File file,
			String uploadExcelFileName) {
		
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
		

		 
		// 构造 XSSFWorkbook 对象，strPath 传入文件路径
		XSSFWorkbook xwb = null;
		try {
			xwb = new XSSFWorkbook(new FileInputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		// 读取第一章表格内容
		XSSFSheet sheet = xwb.getSheetAt(0);
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		 XSSFSheet exSheet = workbook.createSheet(uploadExcelFileName);
		 
		 exSheet.createFreezePane(0,2,0,2)  ;
		// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
		 exSheet.addMergedRegion(new CellRangeAddress(0, 1,0, 0));
		 exSheet.addMergedRegion(new CellRangeAddress(0, 0,1, 2));
		 exSheet.addMergedRegion(new CellRangeAddress(0, 0,3, 4));
		 exSheet.addMergedRegion(new CellRangeAddress(0, 0,5, 6));
		 exSheet.setColumnWidth(0, 32 *200);  
		 exSheet.setColumnWidth(2, 32 *180);  
		 exSheet.setColumnWidth(4, 32 *180);  
		 exSheet.setColumnWidth(6, 32 *180);  
		 XSSFRow rowhead = exSheet.createRow(0);
         //表头数据
             XSSFCell cellHeadOne = rowhead.createCell(0);    
             cellHeadOne.setCellValue("门店");    
             
         
             XSSFCell cellHeadTwo = rowhead.createCell(1);    
             cellHeadTwo.setCellValue("系统");    
             
             XSSFCell cellHeadThree = rowhead.createCell(3);    
             cellHeadThree.setCellValue("MIS"); 
             
             
             XSSFCell cellHeadFour = rowhead.createCell(5);    
             cellHeadFour.setCellValue("差异(系统-MIS)"); 
             
             
             
            XSSFRow rowheadThree = exSheet.createRow(2);
             //表头数据
                  
                 int ro=1000 ;
                
                 XSSFCell cellHeadTwos = rowheadThree.createCell(1);    
                 cellHeadTwos.setCellValue("SUBTOTAL(9,B4:B" +  ro + ")");
                 cellHeadTwos.setCellType(Cell.CELL_TYPE_FORMULA);
                 cellHeadTwos.setCellFormula("SUBTOTAL(9,B4:B" +  ro + ")");
 				
                 
                 XSSFCell cellHeadThrees = rowheadThree.createCell(2);    
                 cellHeadThrees.setCellValue("SUBTOTAL(9,C4:C" +  ro + ")");
                 cellHeadThrees.setCellType(Cell.CELL_TYPE_FORMULA);
                 cellHeadThrees.setCellFormula("SUBTOTAL(9,C4:C" +  ro + ")");
 				
                 
                 
                 XSSFCell cellHeadFours = rowheadThree.createCell(3);    
                 cellHeadFours.setCellValue("SUBTOTAL(9,D4:D" +  ro + ")");
                 cellHeadFours.setCellType(Cell.CELL_TYPE_FORMULA);
                 cellHeadFours.setCellFormula("SUBTOTAL(9,D4:D" +  ro + ")");
 				
                 
                 XSSFCell cellHeadFives = rowheadThree.createCell(4);    
                 cellHeadFives.setCellValue("SUBTOTAL(9,E4:E" +  ro + ")");
                 cellHeadFives.setCellType(Cell.CELL_TYPE_FORMULA);
                 cellHeadFives.setCellFormula("SUBTOTAL(9,E4:E" +  ro + ")");
 				
                 
                 XSSFCell cellHeadSixs = rowheadThree.createCell(5);    
                 cellHeadSixs.setCellValue("SUBTOTAL(9,F4:F" +  ro + ")");
                 cellHeadSixs.setCellType(Cell.CELL_TYPE_FORMULA);
                 cellHeadSixs.setCellFormula("SUBTOTAL(9,F4:F" +  ro + ")");
 				
                 
                 XSSFCell cellHeadSevens = rowheadThree.createCell(6);    
                 cellHeadSevens.setCellValue("SUBTOTAL(9,G4:G" +  ro + ")");
                 cellHeadSevens.setCellType(Cell.CELL_TYPE_FORMULA);
                 cellHeadSevens.setCellFormula("SUBTOTAL(9,G4:G" +  ro + ")");
 				
                 
             
             XSSFRow rowHeadTwo = exSheet.createRow(1);
             String[] excelHeader = {"QTY","AMT","QTY","AMT","QTY","AMT"};
             
             //表头数据
             for (int i = 0; i < excelHeader.length; i++) {   
                 XSSFCell cellOne = rowHeadTwo.createCell(i+1);    
                 cellOne.setCellValue(excelHeader[i]); 
                 
             }   
             
             XSSFRow rowDate =sheet.getRow(0);
            String [] date= rowDate.getCell(3).getStringCellValue().split("-");
     		Map<String,Object> whereMap = new HashMap<String,Object>();
     		whereMap.put("beginDate", date[0]+"-"+date[1]+"-01");
     		whereMap.put("endDate", date[0]+"-"+date[1]+"-31");
     		whereMap.put("conditions",conditions);
     		//List<HashMap<String, Object>> data =soTableDao.selectSysByMis(whereMap);
     		
     		
     		XSSFCell cellHeadFive = rowhead.createCell(7);    
     		cellHeadFive.setCellValue(date[0]+"-"+date[1]); 
            
     		
    		List<HashMap<String, Object>> salerList =soTableDao.selectSysByMis(whereMap);

			HashMap<String, ArrayList<HashMap<String, Object>>> salerDatas = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < salerList.size(); m++) {
				if (salerDatas.get(salerList.get(m).get("shopName")
						.toString().toUpperCase()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(salerList.get(m));
					salerDatas.put(salerList.get(m).get("shopName")
							.toString().toUpperCase(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = salerDatas
							.get(salerList.get(m).get("shopName").toString().toUpperCase());
					modelList.add(salerList.get(m));
				}

			}
			LinkedList<HashMap<String, Object>> dataListTwo = new LinkedList<HashMap<String, Object>>();

			/*HashMap<String, ArrayList<*/HashMap<String, Object>/*>> */map = new /*HashMap<String, ArrayList<*/HashMap<String, Object>/*>>*/();
			XSSFRow rowOne =sheet.getRow(1);
			BigDecimal misQtySum=BigDecimal.ZERO;
			BigDecimal misAmtSum=BigDecimal.ZERO;
			String shopNames="";
			for (int i =1; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row =sheet.getRow(i);
				
				
				if(row.getCell(0)!=null &&
						row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK 
						){
				
					shopNames=row.getCell(0).getStringCellValue().toUpperCase();
									

					BigDecimal misQty=null;
					BigDecimal misAmt=null;
				
					
					if(row.getCell(1)!=null &&
							row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						misQty=new BigDecimal(row.getCell(1).getNumericCellValue());

					}else{
						misQty=BigDecimal.ZERO;

					}
					
					
					if(row.getCell(2)!=null &&
							row.getCell(2).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						 misAmt=new BigDecimal(row.getCell(2).getNumericCellValue());

					}else{
							misAmt=BigDecimal.ZERO;

					}
					
					
					if (map.get(shopNames) == null) {
						ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
						HashMap<String, Object> dataMap=new HashMap<String, Object> ();
						dataMap.put("shopName",shopNames);
						dataMap.put("qty", misQty);
						dataMap.put("amt", misAmt);
						modelList.add(dataMap);
						map.put(shopNames, modelList);
						dataListTwo.add(dataMap);
						misQtySum=BigDecimal.ZERO;
						misAmtSum=BigDecimal.ZERO;
					
					} else {
						ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String, Object>>) map
									.get(shopNames);
						
						for (int j = 0; j < data.size(); j++) {
							misQtySum=BigDecimal.ZERO;
							misAmtSum=BigDecimal.ZERO;
							BigDecimal sysQty=new BigDecimal(data.get(j).get("qty").toString());
							BigDecimal sysAmt=new BigDecimal(data.get(j).get("amt").toString());
							misQtySum=misQtySum.add(sysQty);
							misAmtSum=misAmtSum.add(sysAmt);
						}
						misQtySum=misQtySum.add(misQty);
						misAmtSum=misAmtSum.add(misAmt);
						
						for (int j = 0; j < dataListTwo.size(); j++) {
							HashMap<String, Object> dataMap=dataListTwo.get(j);
							if(dataMap.get("shopName").toString().equals(shopNames)){
								dataMap.put("qty", misQtySum);
								dataMap.put("amt", misAmtSum);

							}
							
						}
						/*HashMap<String, Object> dataMap=new HashMap<String, Object> ();
						dataMap.put("shopName",shopNames);
						
						dataMap.put("qty", misQtySum);
						dataMap.put("amt", misAmtSum);
						dataListTwo.add(dataMap);*/
					}
					
				}/*else{
					HashMap<String, Object> dataMap = new HashMap<String, Object>();
					dataMap.put("qty", misQtySum);
					dataMap.put("amt", misAmtSum);
					dataMap.put("shopName", shopNames);
					dataListTwo.add(dataMap);
					rowOne =sheet.getRow(i);
					misQtySum=BigDecimal.ZERO;
					misAmtSum=BigDecimal.ZERO;
					i--;
					
				}*/
				
		        }

			
			System.out.println("==============="+dataListTwo);
			
			HashMap<String, ArrayList<HashMap<String, Object>>> salerDatasMIS = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			for (int m = 0; m < dataListTwo.size(); m++) {
				if (salerDatasMIS.get(dataListTwo.get(m).get("shopName")
						.toString().toUpperCase()) == null) {
					ArrayList<HashMap<String, Object>> modelList = new ArrayList<HashMap<String, Object>>();
					modelList.add(dataListTwo.get(m));
					salerDatasMIS.put(dataListTwo.get(m).get("shopName")
							.toString().toUpperCase(), modelList);
				} else {
					ArrayList<HashMap<String, Object>> modelList = salerDatasMIS
							.get(dataListTwo.get(m).get("shopName").toString().toUpperCase());
					modelList.add(dataListTwo.get(m));
				}

			}
			
		
			
			for (int i =0; i <dataListTwo.size(); i++) {
			HashMap<String, Object> dataMaps=dataListTwo.get(i);

					String shopName=dataMaps.get("shopName").toString().toUpperCase();
				
					
					if (salerDatas.get(shopName) != null) {
						ArrayList<HashMap<String, Object>> data = salerDatas
								.get(shopName);
						for (int j = 0; j < data.size(); j++) {
							BigDecimal sysQty=new BigDecimal(data.get(j).get("qty").toString());
							BigDecimal sysAmt=new BigDecimal(data.get(j).get("amt").toString());
							BigDecimal misQty=new BigDecimal(dataMaps.get("qty").toString());
							BigDecimal misAmt=new BigDecimal(dataMaps.get("amt").toString());

							
							
							XSSFRow exRow= exSheet.createRow(i+3); 
							 
				             XSSFCell cell = exRow.createCell(0);
				             cell.setCellValue(shopName);    
				             
				             XSSFCell cellTwo = exRow.createCell(1);
				             cellTwo.setCellValue(Math.round(sysQty.doubleValue()));
				             
				             XSSFCell cellThree = exRow.createCell(2);
				             cellThree.setCellValue(Math.round(sysAmt.doubleValue()));
				             
				             XSSFCell cellFour = exRow.createCell(3);
				             cellFour.setCellValue(Math.round(misQty.doubleValue()));
				             
				             
				             XSSFCell cellFive = exRow.createCell(4);
				             cellFive.setCellValue(Math.round(misAmt.doubleValue()));
				             
				             XSSFCell cellSix = exRow.createCell(5);
				             cellSix.setCellValue(Math.round(sysQty.doubleValue())-Math.round(misQty.doubleValue()));
				             
				             XSSFCell cellSeven = exRow.createCell(6);
				             cellSeven.setCellValue(Math.round(sysAmt.doubleValue())-Math.round(misAmt.doubleValue()));
						}

					}else  {
						BigDecimal misQty=new BigDecimal(dataMaps.get("qty").toString());
						BigDecimal misAmt=new BigDecimal(dataMaps.get("amt").toString());

						
						XSSFRow exRows= exSheet.createRow(i+3); 
			            XSSFCell cell = exRows.createCell(0);
			            cell.setCellValue(shopName);
			            
			            XSSFCell cellTwo = exRows.createCell(1);
			             cellTwo.setCellValue(0);
			             
			             
			             XSSFCell cellThree = exRows.createCell(2);
			             cellThree.setCellValue(0);
			             
			             XSSFCell cellFour = exRows.createCell(3);
			             cellFour.setCellValue(Math.round(misQty.doubleValue()));
			             
			             
			             XSSFCell cellFive = exRows.createCell(4);
			             cellFive.setCellValue(Math.round(misAmt.doubleValue()));
			             
			             XSSFCell cellSix = exRows.createCell(5);
			             cellSix.setCellValue(-Math.round(misQty.doubleValue()));
			             
			             XSSFCell cellSeven = exRows.createCell(6);
			             cellSeven.setCellValue(-Math.round(misAmt.doubleValue()));
			             
					}
					

				
				
			}
			
			
			int row=0;
			for (int j = 0; j < salerList.size(); j++) {
				HashMap<String, Object> dataMap=salerList.get(j);
				String shopName=dataMap.get("shopName").toString().toUpperCase();
				
				
				if (salerDatasMIS.get(shopName) == null) {
					row=row+1;
					BigDecimal sysQty=new BigDecimal(dataMap.get("qty").toString());
					BigDecimal sysAmt=new BigDecimal(dataMap.get("amt").toString());
					XSSFRow exRows= exSheet.createRow(dataListTwo.size()+3+row); 
		            XSSFCell cell = exRows.createCell(0);
		            cell.setCellValue(shopName);
		            
		            XSSFCell cellTwo = exRows.createCell(1);
		             cellTwo.setCellValue(Math.round(sysQty.doubleValue()));
		             
		             
		             XSSFCell cellThree = exRows.createCell(2);
		             cellThree.setCellValue(Math.round(sysAmt.doubleValue()));
		             
		             XSSFCell cellFour = exRows.createCell(3);
		             cellFour.setCellValue(0);
		             
		             
		             XSSFCell cellFive = exRows.createCell(4);
		             cellFive.setCellValue(0);
		             
		             XSSFCell cellSix = exRows.createCell(5);
		             cellSix.setCellValue(Math.round(sysQty.doubleValue()));
		             
		             XSSFCell cellSeven = exRows.createCell(6);
		             cellSeven.setCellValue(Math.round(sysAmt.doubleValue()));
					
				}
			}
			
		
		return workbook;
	}


	@Override
	public JSONObject selectMonthCountryTotal(
			Map<String, Object> whereMap) {
		JSONArray array=new JSONArray();
		String beginDate=whereMap.get("beginDate").toString();
		String endDate=whereMap.get("endDate").toString();
		
		try {
			LinkedList<HashMap<String, Object>> 	dateList=null;

			if((whereMap.get("country").toString().equals("5"))){
				dateList=BDDateUtil.getWeekByPH(beginDate, endDate);
			}else{
				dateList=BDDateUtil.getWeek(beginDate, endDate);
			}
			for (int i = 0; i < dateList.size(); i++) {
				JSONObject object=new JSONObject();
				

				whereMap.put("beginDate", dateList.get(i).get("beginDate").toString());
				whereMap.put("endDate", dateList.get(i).get("endDate").toString());
				List<HashMap<String, Object>>  data=soTableDao.selectMonthCountryTotal(whereMap);
				
				object.accumulate("week", "Week"+(i+1)+"("+ dateList.get(i).get("beginDate").toString().split("-")[2]+"-"+ dateList.get(i).get("endDate").toString().split("-")[2]+")");
				if(data.size()>0){
					BigDecimal qty=new BigDecimal(data.get(0).get("saleQty").toString());
					object.accumulate("arrQty", Math.round(qty.doubleValue()));
					qty=new BigDecimal(data.get(0).get("saleSum").toString());
					object.accumulate("arrAmt",Math.round(qty.doubleValue()));
				}else{
					object.accumulate("arrQty",0);
					object.accumulate("arrAmt", 0);
				}
				array.add(object);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

			JSONObject jsonObject=new JSONObject();
			jsonObject.accumulate("data",array);
			return jsonObject;
	}




	@Override
	public JSONObject selectAreaDataByMonth (Map<String, Object> whereMap) {
		JSONArray arrayTT=new JSONArray();
		try {
		String beginDate=whereMap.get("beginDate").toString();
		String endDate=whereMap.get("endDate").toString();
		

		List<HashMap<String, Object>> bigReg = soTableDao
				.selectBigReg(whereMap);
		
		List<HashMap<String, Object>> re = soTableDao
				.selectRegionalHeadByParty(whereMap);
		
		JSONArray array=new JSONArray();
	
		LinkedList<HashMap<String, Object>> 	dateList=null;

		if((whereMap.get("country").toString().equals("5"))){
			dateList=BDDateUtil.getWeekByPH(beginDate, endDate);
		}else{
			dateList=BDDateUtil.getWeek(beginDate, endDate);
		}
		List week=new ArrayList();

			for (int i = 0; i < dateList.size(); i++) {
				week.add("Week"+(i+1)+"("+ dateList.get(i).get("beginDate").toString().split("-")[2]+"-"+ dateList.get(i).get("endDate").toString().split("-")[2]+")");
				whereMap.put("beginDate", dateList.get(i).get("beginDate").toString());
				whereMap.put("endDate", dateList.get(i).get("endDate").toString());
				List<HashMap<String, Object>> areaDatas = soTableDao
						.selectAreaDataByMonth(whereMap);
				for (int j = 0; j < areaDatas.size(); j++) {
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("week",i+1);
					jsonObject.accumulate("party",areaDatas.get(j).get("PARTY_ID"));
					jsonObject.accumulate("partyName",areaDatas.get(j).get("AREA"));
					BigDecimal qty=new BigDecimal(areaDatas.get(j).get("saleQty").toString());
					jsonObject.accumulate("saleQty",Math.round(qty.doubleValue()) );
					qty=new BigDecimal(areaDatas.get(j).get("saleSum").toString());
					jsonObject.accumulate("saleSum", Math.round(qty.doubleValue()));
					array.add(jsonObject);
					
				}
			}
			
			BDDateUtil.OrderName(array, "partyName");

			
			JSONArray arrayTwo=new JSONArray();
			String party="";
			if(array.size()>=1){
				party=array.getJSONObject(0).getString("party");
			}
			int [] val = getQty(dateList.size());
			int [] valAmt = getQty(dateList.size());
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj=array.getJSONObject(i);
				if(WebPageUtil.isStringNullAvaliable(party) && party.equals(obj.get("party").toString())){
					String date=obj.getString("week");
					int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
					BigDecimal bd=new BigDecimal(obj.get("saleQty").toString());
					int qty=(int)Math.round(bd.doubleValue());
					
					bd=new BigDecimal(obj.get("saleSum").toString());
					int amt=(int)Math.round(bd.doubleValue());
					
					val[dte -1] = qty;
					valAmt[dte -1] = amt;
					if(array.size() - i == 1){
						JSONObject jsonObject=new JSONObject();
						jsonObject.accumulate("party",party);
						jsonObject.accumulate("arrQty", val);
						jsonObject.accumulate("arrAmt", valAmt);
						arrayTwo.add(jsonObject);
					}
					
				}else{
						JSONObject jsonObject=new JSONObject();
						jsonObject.accumulate("party",party);
						jsonObject.accumulate("arrQty", val);
						jsonObject.accumulate("arrAmt", valAmt);
						arrayTwo.add(jsonObject);
						party =obj.get("party").toString();
						val = getQty(dateList.size());
						valAmt = getQty(dateList.size());
						i--;

					
				}
			}
			
			
			


		
		
		for (int a = 0; a < bigReg.size(); a++) {
			
			
			JSONObject dataMap = new JSONObject();
			for (int i = 0; i < re.size(); i++) {
				if (bigReg.get(a).get("PARTY_ID")
						.equals(re.get(i).get("PARTY_ID"))) {
					dataMap.put("RegionalHead", re.get(i).get("userName"));
				}
			}
			
			dataMap.put("AREA", bigReg.get(a).get("PARTY_NAME"));
			dataMap.put("NUM",a+1);
		
			
			
			int [] qty = getQty(dateList.size());
			int [] amt = getQty(dateList.size());
			for (int i = 0; i < arrayTwo.size(); i++) {
				JSONObject obj=(JSONObject) arrayTwo.get(i);
				String pa=","+obj.get("party")+",";
				if(bigReg.get(a).get("small").toString().contains(pa)){
					JSONArray one=(JSONArray) obj.get("arrQty");
					JSONArray two= (JSONArray) obj.get("arrAmt");
					for (int j = 0; j < one.size(); j++) {
							qty[j]+=one.getInt(j);
					}
					for (int j = 0; j < two.size(); j++) {
						
						amt[j]+=two.getInt(j);
					}
					
				
				
				}
			}
			
			
			BigDecimal amts=BigDecimal.ZERO;
			for (int i = 0; i < amt.length; i++) {
				amts=amts.add(new BigDecimal(amt[i]));
			}
			
			dataMap.put("Total", amts);
		
			dataMap.put("week", week);
			
			dataMap.put("arrQty", qty);
			dataMap.put("arrAmt", amt);

			arrayTT.add(dataMap);

		}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject object=new JSONObject();
		BDDateUtil.OrderAmt(arrayTT, "Total");
		object.accumulate("data", arrayTT);
		return object;
		
	}


	@Override
	public JSONObject selectMonthCountryXCP(Map<String, Object> whereMap) {
		JSONArray arrayTwo=new JSONArray();
		try {
		String beginDate=whereMap.get("beginDate").toString();
		String endDate=whereMap.get("endDate").toString();


		JSONArray array=new JSONArray();
	
		LinkedList<HashMap<String, Object>> 	dateList=null;

		if((whereMap.get("country").toString().equals("5"))){
			dateList=BDDateUtil.getWeekByPH(beginDate, endDate);
		}else{
			dateList=BDDateUtil.getWeek(beginDate, endDate);
		}

		
		List week=new ArrayList();
			for (int i = 0; i < dateList.size(); i++) {
				week.add( "Week"+(i+1)+"("+ dateList.get(i).get("beginDate").toString().split("-")[2]+"-"+ dateList.get(i).get("endDate").toString().split("-")[2]+")");

				whereMap.put("beginDate", dateList.get(i).get("beginDate").toString());
				whereMap.put("endDate", dateList.get(i).get("endDate").toString());
				List<HashMap<String, Object>> areaDatas = soTableDao
						.selectXCPModelByMonth(whereMap);
				for (int j = 0; j < areaDatas.size(); j++) {
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("week",i+1);
					jsonObject.accumulate("model",areaDatas.get(j).get("model"));
					BigDecimal qty=new BigDecimal(areaDatas.get(j).get("qty").toString());
					jsonObject.accumulate("saleQty",Math.round(qty.doubleValue()) );
					qty=new BigDecimal(areaDatas.get(j).get("amt").toString());
					jsonObject.accumulate("saleSum", Math.round(qty.doubleValue()));
					jsonObject.accumulate("spec",areaDatas.get(j).get("spec"));
					jsonObject.accumulate("size",areaDatas.get(j).get("size"));
					array.add(jsonObject);
					
				}
			}
			
			BDDateUtil.OrderName(array, "model");

			
			String model="";
			if(array.size()>=1){
				model=array.getJSONObject(0).getString("model");
			}
			int [] val = getQty(dateList.size());
			int [] valAmt = getQty(dateList.size());
		
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj=array.getJSONObject(i);
				if(WebPageUtil.isStringNullAvaliable(model) && model.equals(obj.get("model").toString())){
					String date=obj.getString("week");
					int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
					BigDecimal bd=new BigDecimal(obj.get("saleQty").toString());
					int qty=(int)Math.round(bd.doubleValue());
					
					bd=new BigDecimal(obj.get("saleSum").toString());
					int amt=(int)Math.round(bd.doubleValue());
					val[dte -1] = qty;
					valAmt[dte -1] = amt;
					if(array.size() - i == 1){
						JSONObject jsonObject=new JSONObject();
						jsonObject.accumulate("model",model);
						jsonObject.accumulate("spec",obj.get("spec"));
						jsonObject.accumulate("size",obj.get("size"));
						jsonObject.accumulate("arrQty", val);
						jsonObject.accumulate("arrAmt", valAmt);
						jsonObject.accumulate("week", week);
						arrayTwo.add(jsonObject);
					}
					
				}else{
						obj=array.getJSONObject(i-1);
						JSONObject jsonObject=new JSONObject();
						jsonObject.accumulate("model",model);
						jsonObject.accumulate("spec",obj.get("spec"));
						jsonObject.accumulate("size",obj.get("size"));
						jsonObject.accumulate("arrQty", val);
						jsonObject.accumulate("arrAmt", valAmt);
						jsonObject.accumulate("week", week);
						arrayTwo.add(jsonObject);
						obj=array.getJSONObject(i);
						model =obj.get("model").toString();
						val = getQty(dateList.size());
						valAmt = getQty(dateList.size());
						
						i--;

					
				}
			}
			
			
			


		
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject object=new JSONObject();
		object.accumulate("data", arrayTwo);
		return object;
		
	}


	@Override
	public JSONObject selectSalemanDataByMonth(Map<String, Object> whereMap) {
		JSONArray arrayTwo=new JSONArray();
		try {
		String beginDate=whereMap.get("beginDate").toString();
		String endDate=whereMap.get("endDate").toString();


		JSONArray array=new JSONArray();
	
		LinkedList<HashMap<String, Object>> 	dateList=null;

		if((whereMap.get("country").toString().equals("5"))){
			dateList=BDDateUtil.getWeekByPH(beginDate, endDate);
		}else{
			dateList=BDDateUtil.getWeek(beginDate, endDate);
		}

		
		List week=new ArrayList();
			for (int i = 0; i < dateList.size(); i++) {
				week.add( "Week"+(i+1)+"("+ dateList.get(i).get("beginDate").toString().split("-")[2]+"-"+ dateList.get(i).get("endDate").toString().split("-")[2]+")");

				whereMap.put("beginDate", dateList.get(i).get("beginDate").toString());
				whereMap.put("endDate", dateList.get(i).get("endDate").toString());
				List<HashMap<String, Object>> areaDatas = soTableDao
						.selectSalemanDataByMonth(whereMap);
				for (int j = 0; j < areaDatas.size(); j++) {
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("week",i+1);
					jsonObject.accumulate("userId",areaDatas.get(j).get("userId"));
					jsonObject.accumulate("userName",areaDatas.get(j).get("userName"));
					BigDecimal qty=new BigDecimal(areaDatas.get(j).get("saleQty").toString());
					jsonObject.accumulate("saleQty",Math.round(qty.doubleValue()) );
					qty=new BigDecimal(areaDatas.get(j).get("saleSum").toString());
					jsonObject.accumulate("saleSum", Math.round(qty.doubleValue()));
					
					array.add(jsonObject);
					
				}
			}
			
			BDDateUtil.OrderName(array, "userName");

			
			String user="";
			if(array.size()>=1){
				user=array.getJSONObject(0).getString("userId");
			}
			int [] val = getQty(dateList.size());
			int [] valAmt = getQty(dateList.size());
			System.out.println(array);
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj=array.getJSONObject(i);
				if(WebPageUtil.isStringNullAvaliable(user) && user.equals(obj.get("userId").toString())){
					String date=obj.getString("week");
					int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
					BigDecimal bd=new BigDecimal(obj.get("saleQty").toString());
					int qty=(int)Math.round(bd.doubleValue());
					
					bd=new BigDecimal(obj.get("saleSum").toString());
					int amt=(int)Math.round(bd.doubleValue());
					val[dte -1] = qty;
					valAmt[dte -1] = amt;
					if(array.size() - i == 1){
						JSONObject jsonObject=new JSONObject();
						jsonObject.accumulate("userName",obj.get("userName"));
						jsonObject.accumulate("arrQty", val);
						jsonObject.accumulate("arrAmt", valAmt);
						jsonObject.accumulate("week", week);
						arrayTwo.add(jsonObject);
					}
					
				}else{
						obj=array.getJSONObject(i-1);
						JSONObject jsonObject=new JSONObject();
						jsonObject.accumulate("userName",obj.get("userName"));
						jsonObject.accumulate("arrQty", val);
						jsonObject.accumulate("arrAmt", valAmt);
						jsonObject.accumulate("week", week);
						arrayTwo.add(jsonObject);
						obj=array.getJSONObject(i);
						user =obj.get("userId").toString();
						val = getQty(dateList.size());
						valAmt = getQty(dateList.size());
						
						i--;
						

					
				}
			}
			
			
			


		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < arrayTwo.size(); i++) {
			JSONObject jsonObject=(JSONObject) arrayTwo.get(i);
			jsonObject.accumulate("NUM",i+1 );
			JSONArray arrAmt=(JSONArray) jsonObject.get("arrAmt");
			BigDecimal amt=BigDecimal.ZERO;
			for (int j = 0; j < arrAmt.size(); j++) {
				amt=amt.add(new BigDecimal(arrAmt.get(j).toString()));
			}
			jsonObject.accumulate("Total", amt);

		}
		JSONObject object=new JSONObject();
		BDDateUtil.OrderAmt(arrayTwo, "Total");
		object.accumulate("data", arrayTwo);
	
		return object;
	}


	@Override
	public JSONObject selectAcfoDataByMonth(Map<String, Object> whereMap) {
		JSONArray arrayTwo=new JSONArray();
		try {
		String beginDate=whereMap.get("beginDate").toString();
		String endDate=whereMap.get("endDate").toString();


		JSONArray array=new JSONArray();
	
		LinkedList<HashMap<String, Object>> 	dateList=null;
	
		if((whereMap.get("country").toString().equals("5"))){
			dateList=BDDateUtil.getWeekByPH(beginDate, endDate);
		}else{
			dateList=BDDateUtil.getWeek(beginDate, endDate);
		}

		
		List week=new ArrayList();
			for (int i = 0; i < dateList.size(); i++) {
				week.add( "Week"+(i+1)+"("+ dateList.get(i).get("beginDate").toString().split("-")[2]+"-"+ dateList.get(i).get("endDate").toString().split("-")[2]+")");

				whereMap.put("beginDate", dateList.get(i).get("beginDate").toString());
				whereMap.put("endDate", dateList.get(i).get("endDate").toString());
				List<HashMap<String, Object>> areaDatas = soTableDao
						.selectAcfoDataByMonth(whereMap);
				for (int j = 0; j < areaDatas.size(); j++) {
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("week",i+1);
					jsonObject.accumulate("userId",areaDatas.get(j).get("userId"));
					jsonObject.accumulate("userName",areaDatas.get(j).get("userName"));
					BigDecimal qty=new BigDecimal(areaDatas.get(j).get("saleQty").toString());
					jsonObject.accumulate("saleQty",Math.round(qty.doubleValue()) );
					qty=new BigDecimal(areaDatas.get(j).get("saleSum").toString());
					jsonObject.accumulate("saleSum", Math.round(qty.doubleValue()));
					
					array.add(jsonObject);
					
				}
			}
			
			BDDateUtil.OrderName(array, "userName");

			
			String user="";
			if(array.size()>=1){
				user=array.getJSONObject(0).getString("userId");
			}
			int [] val = getQty(dateList.size());
			int [] valAmt = getQty(dateList.size());
		
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj=array.getJSONObject(i);
				if(WebPageUtil.isStringNullAvaliable(user) && user.equals(obj.get("userId").toString())){
					String date=obj.getString("week");
					int dte = WebPageUtil.isStringNullAvaliable(date)?Integer.parseInt(date) : 0;
					BigDecimal bd=new BigDecimal(obj.get("saleQty").toString());
					int qty=(int)Math.round(bd.doubleValue());
					
					bd=new BigDecimal(obj.get("saleSum").toString());
					int amt=(int)Math.round(bd.doubleValue());
					val[dte -1] = qty;
					valAmt[dte -1] = amt;
					if(array.size() - i == 1){
						JSONObject jsonObject=new JSONObject();
						jsonObject.accumulate("userName",obj.get("userName"));
						jsonObject.accumulate("arrQty", val);
						jsonObject.accumulate("arrAmt", valAmt);
						jsonObject.accumulate("week", week);
						arrayTwo.add(jsonObject);
					}
					
				}else{
						obj=array.getJSONObject(i-1);
						JSONObject jsonObject=new JSONObject();
						jsonObject.accumulate("userName",obj.get("userName"));
						jsonObject.accumulate("arrQty", val);
						jsonObject.accumulate("arrAmt", valAmt);
						jsonObject.accumulate("week", week);
						arrayTwo.add(jsonObject);
						obj=array.getJSONObject(i);
						user =obj.get("userId").toString();
						val = getQty(dateList.size());
						valAmt = getQty(dateList.size());
						
						i--;

					
				}
			}
			
			
			


		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	
		
		
		for (int i = 0; i < arrayTwo.size(); i++) {
			JSONObject jsonObject=(JSONObject) arrayTwo.get(i);
			jsonObject.accumulate("NUM",i+1 );
			JSONArray arrAmt=(JSONArray) jsonObject.get("arrAmt");
			BigDecimal amt=BigDecimal.ZERO;
			for (int j = 0; j < arrAmt.size(); j++) {
				amt=amt.add(new BigDecimal(arrAmt.get(j).toString()));
			}
			jsonObject.accumulate("Total", amt);

		}
		JSONObject object=new JSONObject();
		BDDateUtil.OrderAmt(arrayTwo, "Total");
		object.accumulate("data", arrayTwo);
		return object;
	}


	@Override
	public JSONObject selectHQDataByYear(Map<String, Object> whereMap) {
		List<HashMap<String, Object>> soDatas = soTableDao.selectCountryDataByHQ(whereMap);
		List<HashMap<String, Object>> hqDatas = null;
		List<HashMap<String, Object>> targetDatas=null;

	
		List<HashMap<String, Object>> yearTargetDatas = null;
		if( (whereMap.get("what").toString().contains("UD") || whereMap.get("what").toString().contains("XCP"))) {
			yearTargetDatas = soTableDao.selectYearTargetDataByHQUD(whereMap);
			targetDatas = soTableDao.selectTargetDataByHQUD(whereMap);
		}else {
			yearTargetDatas = soTableDao.selectYearTargetDataByHQ(whereMap);
			targetDatas = soTableDao.selectTargetDataByHQ(whereMap);
		}

		 
		if(targetDatas.size()>soDatas.size()) {
			for (int j = 0; j < targetDatas.size(); j++) {
				for (int i = 0; i < soDatas.size(); i++) {
					if(soDatas.get(i).get("country_id").toString().equals(targetDatas.get(j).get("country_id").toString())
							&& soDatas.get(i).get("datadate").toString().equals(targetDatas.get(j).get("datadate").toString())
							){
						targetDatas.get(j).put("saleQty", soDatas.get(i).get("saleQty"));
					}
				}
			}
			hqDatas=targetDatas;
		}else {
			for (int i = 0; i < soDatas.size(); i++) {
				for (int j = 0; j < targetDatas.size(); j++) {
					if(soDatas.get(i).get("country_id").toString().equals(targetDatas.get(j).get("country_id").toString())
							&& soDatas.get(i).get("datadate").toString().equals(targetDatas.get(j).get("datadate").toString())
							){
						soDatas.get(i).put("targetQty", targetDatas.get(j).get("targetQty"));
					}
				}
			}
			hqDatas=soDatas;
		}
		
		JSONArray array=new JSONArray();
		String country="";
		if(hqDatas.size()>=1){
			country=hqDatas.get(0).get("country").toString();
		}
		BigDecimal saleQtyYear=BigDecimal.ZERO;
		BigDecimal saleQtyQ1=BigDecimal.ZERO;
		BigDecimal saleQtyQ2=BigDecimal.ZERO;
		BigDecimal saleQtyQ3=BigDecimal.ZERO;
		BigDecimal saleQtyQ4=BigDecimal.ZERO;
		
		BigDecimal TargetQtyYear=BigDecimal.ZERO;
		BigDecimal TargetQtyQ1=BigDecimal.ZERO;
		BigDecimal TargetQtyQ2=BigDecimal.ZERO;
		BigDecimal TargetQtyQ3=BigDecimal.ZERO;
		BigDecimal TargetQtyQ4=BigDecimal.ZERO;
		
		
		for (int i = 0; i < hqDatas.size(); i++) {
			HashMap<String, Object> obj=hqDatas.get(i);
			if(WebPageUtil.isStringNullAvaliable(country) && country.equals(obj.get("country").toString())){
				
				int month =Integer.parseInt(obj.get("datadate").toString().split("-")[1]);
				
				BigDecimal bdQty=new BigDecimal(obj.get("saleQty").toString());
				BigDecimal bdTarget=new BigDecimal(obj.get("targetQty").toString());
				saleQtyYear=saleQtyYear.add(bdQty);
				TargetQtyYear=TargetQtyYear.add(bdTarget);
				if(month>=1 && month<=3){
					saleQtyQ1=saleQtyQ1.add(bdQty);
					TargetQtyQ1=TargetQtyQ1.add(bdTarget);
				}else if(month>=4 && month<=6){
					saleQtyQ2=saleQtyQ2.add(bdQty);
					TargetQtyQ2=TargetQtyQ2.add(bdTarget);
				}else if(month>=7 && month<=9){
					saleQtyQ3=saleQtyQ3.add(bdQty);
					TargetQtyQ3=TargetQtyQ3.add(bdTarget);
				}else if(month>=10 && month<=12){
					saleQtyQ4=saleQtyQ4.add(bdQty);
					TargetQtyQ4=TargetQtyQ4.add(bdTarget);
				}
				
				if(hqDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQtyYear", Math.round(saleQtyYear.doubleValue()));
					jsonObject.accumulate("TargetQtyYear", Math.round(TargetQtyYear.doubleValue()));
					if(Math.round(TargetQtyYear.doubleValue())==0){
						jsonObject.accumulate("achYear",100+"%");
						
					}else{
						jsonObject.accumulate("achYear", Math.round(saleQtyYear.doubleValue()/TargetQtyYear.doubleValue()*100)+"%");

					}
					
					jsonObject.accumulate("saleQtyQ1", Math.round(saleQtyQ1.doubleValue()));
					jsonObject.accumulate("TargetQtyQ1", Math.round(TargetQtyQ1.doubleValue()));

					if(Math.round(TargetQtyQ1.doubleValue())==0){
						jsonObject.accumulate("achQ1",100+"%");
						
					}else{
						jsonObject.accumulate("achQ1", Math.round(saleQtyQ1.doubleValue()/TargetQtyQ1.doubleValue()*100)+"%");

					}
					
					
					jsonObject.accumulate("saleQtyQ2", Math.round(saleQtyQ2.doubleValue()));
					jsonObject.accumulate("TargetQtyQ2", Math.round(TargetQtyQ2.doubleValue()));

					if(Math.round(TargetQtyQ2.doubleValue())==0){
						jsonObject.accumulate("achQ2",100+"%");
						
					}else{
						jsonObject.accumulate("achQ2", Math.round(saleQtyQ2.doubleValue()/TargetQtyQ2.doubleValue()*100)+"%");

					}
					
					

					jsonObject.accumulate("saleQtyQ3", Math.round(saleQtyQ3.doubleValue()));
					jsonObject.accumulate("TargetQtyQ3", Math.round(TargetQtyQ3.doubleValue()));

					if(Math.round(TargetQtyQ3.doubleValue())==0){
						jsonObject.accumulate("achQ3",100+"%");
						
					}else{
						jsonObject.accumulate("achQ3", Math.round(saleQtyQ3.doubleValue()/TargetQtyQ3.doubleValue()*100)+"%");

					}
					
					
					

					jsonObject.accumulate("saleQtyQ4", Math.round(saleQtyQ4.doubleValue()));
					jsonObject.accumulate("TargetQtyQ4", Math.round(TargetQtyQ4.doubleValue()));

					if(Math.round(TargetQtyQ4.doubleValue())==0){
						jsonObject.accumulate("achQ4",100+"%");
						
					}else{
						jsonObject.accumulate("achQ4", Math.round(saleQtyQ4.doubleValue()/TargetQtyQ4.doubleValue()*100)+"%");

					}
					
					
					
					array.add(jsonObject);
				}
				
			}else{
					obj=hqDatas.get(i-1);
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQtyYear", Math.round(saleQtyYear.doubleValue()));
					jsonObject.accumulate("TargetQtyYear", Math.round(TargetQtyYear.doubleValue()));
					if(Math.round(TargetQtyYear.doubleValue())==0){
						jsonObject.accumulate("achYear",100+"%");
						
					}else{
						jsonObject.accumulate("achYear", Math.round(saleQtyYear.doubleValue()/TargetQtyYear.doubleValue()*100)+"%");

					}
					
					jsonObject.accumulate("saleQtyQ1", Math.round(saleQtyQ1.doubleValue()));
					jsonObject.accumulate("TargetQtyQ1", Math.round(TargetQtyQ1.doubleValue()));

					if(Math.round(TargetQtyQ1.doubleValue())==0){
						jsonObject.accumulate("achQ1",100+"%");
						
					}else{
						jsonObject.accumulate("achQ1", Math.round(saleQtyQ1.doubleValue()/TargetQtyQ1.doubleValue()*100)+"%");

					}
					
					
					jsonObject.accumulate("saleQtyQ2", Math.round(saleQtyQ2.doubleValue()));
					jsonObject.accumulate("TargetQtyQ2", Math.round(TargetQtyQ2.doubleValue()));

					if(Math.round(TargetQtyQ2.doubleValue())==0){
						jsonObject.accumulate("achQ2",100+"%");
						
					}else{
						jsonObject.accumulate("achQ2", Math.round(saleQtyQ2.doubleValue()/TargetQtyQ2.doubleValue()*100)+"%");

					}
					
					

					jsonObject.accumulate("saleQtyQ3", Math.round(saleQtyQ3.doubleValue()));
					jsonObject.accumulate("TargetQtyQ3", Math.round(TargetQtyQ3.doubleValue()));

					if(Math.round(TargetQtyQ3.doubleValue())==0){
						jsonObject.accumulate("achQ3",100+"%");
						
					}else{
						jsonObject.accumulate("achQ3", Math.round(saleQtyQ3.doubleValue()/TargetQtyQ3.doubleValue()*100)+"%");

					}
					
					
					

					jsonObject.accumulate("saleQtyQ4", Math.round(saleQtyQ4.doubleValue()));
					jsonObject.accumulate("TargetQtyQ4", Math.round(TargetQtyQ4.doubleValue()));

					if(Math.round(TargetQtyQ4.doubleValue())==0){
						jsonObject.accumulate("achQ4",100+"%");
						
					}else{
						jsonObject.accumulate("achQ4", Math.round(saleQtyQ4.doubleValue()/TargetQtyQ4.doubleValue()*100)+"%");

					}
					
					
					array.add(jsonObject);
					obj=hqDatas.get(i);
					country =obj.get("country").toString();
					saleQtyYear=BigDecimal.ZERO;
					saleQtyQ1=BigDecimal.ZERO;
					saleQtyQ2=BigDecimal.ZERO;
					saleQtyQ3=BigDecimal.ZERO;
					saleQtyQ4=BigDecimal.ZERO;
					
					TargetQtyYear=BigDecimal.ZERO;
					TargetQtyQ1=BigDecimal.ZERO;
					TargetQtyQ2=BigDecimal.ZERO;
					TargetQtyQ3=BigDecimal.ZERO;
					TargetQtyQ4=BigDecimal.ZERO;
					
					i--;

				
			}
		}
		JSONArray arrayTwo=new JSONArray();
		for (int j = 0; j < array.size(); j++) {
			JSONObject object=array.getJSONObject(j);
			for (int k = 0; k < yearTargetDatas.size(); k++) {
				if(object.get("country").toString().equals(yearTargetDatas.get(k).get("country").toString())) {
					BigDecimal bd=new BigDecimal(yearTargetDatas.get(k).get("qty").toString());
					object.put("TargetQtyYear",Math.round(bd.doubleValue()));
					saleQtyYear=new BigDecimal(object.get("saleQtyYear").toString());
					if(Math.round(bd.doubleValue())==0){
						object.put("achYear",100+"%");
						
					}else{
						object.put("achYear", Math.round(saleQtyYear.doubleValue()/bd.doubleValue()*100)+"%");
					}
					
					
				}
			}
			arrayTwo.add(object);
		
		}
		
		JSONObject object=new JSONObject();
		//BDDateUtil.OrderName(arrayTwo, "center");
		object.accumulate("data",  arrayTwo);
		return object;
	}




	@Override
	public JSONObject selectHQDataByQuarter(Map<String, Object> whereMap) {
		List<HashMap<String, Object>> soDatas = soTableDao.selectCountryDataByHQ(whereMap);
		List<HashMap<String, Object>> hqDatas = null;

		List<HashMap<String, Object>> targetDatas = null;
		if(whereMap.get("what")!=null && (whereMap.get("what").toString().contains("UD") || whereMap.get("what").toString().contains("XCP"))) {
	
			targetDatas = soTableDao.selectTargetDataByHQUD(whereMap);
		}else {
			targetDatas = soTableDao.selectTargetDataByHQ(whereMap);
		}
		System.out.println("===soDatas==="+soDatas);
		if(targetDatas.size()>soDatas.size()) {
			for (int j = 0; j < targetDatas.size(); j++) {
				for (int i = 0; i < soDatas.size(); i++) {
					if(soDatas.get(i).get("country_id").toString().equals(targetDatas.get(j).get("country_id").toString())
							&& soDatas.get(i).get("datadate").toString().equals(targetDatas.get(j).get("datadate").toString())
							){
						targetDatas.get(j).put("saleQty", soDatas.get(i).get("saleQty"));
					}
				}
			}
			hqDatas=targetDatas;
		}else {
			for (int i = 0; i < soDatas.size(); i++) {
				for (int j = 0; j < targetDatas.size(); j++) {
					if(soDatas.get(i).get("country_id").toString().equals(targetDatas.get(j).get("country_id").toString())
							&& soDatas.get(i).get("datadate").toString().equals(targetDatas.get(j).get("datadate").toString())
							){
						soDatas.get(i).put("targetQty", targetDatas.get(j).get("targetQty"));
					}
				}
			}
			hqDatas=soDatas;
		}
		
		System.out.println("===hqDatas="+hqDatas);
		JSONArray array=new JSONArray();
		
		BigDecimal saleQty=BigDecimal.ZERO;
		BigDecimal saleQty1=BigDecimal.ZERO;
		BigDecimal saleQty2=BigDecimal.ZERO;
		BigDecimal saleQty3=BigDecimal.ZERO;
		
		BigDecimal TargetQty=BigDecimal.ZERO;
		BigDecimal TargetQty1=BigDecimal.ZERO;
		BigDecimal TargetQty2=BigDecimal.ZERO;
		BigDecimal TargetQty3=BigDecimal.ZERO;
		
		String country="";
		if(hqDatas.size()>=1){
			country=hqDatas.get(0).get("country").toString();
		}
	
		
		
		for (int i = 0; i < hqDatas.size(); i++) {
			HashMap<String, Object> obj=hqDatas.get(i);
			if(WebPageUtil.isStringNullAvaliable(country) && country.equals(obj.get("country").toString())){
				
				int month =Integer.parseInt(obj.get("datadate").toString().split("-")[1]);
				
				BigDecimal bdQty=new BigDecimal(obj.get("saleQty").toString());
				BigDecimal bdTarget=new BigDecimal(obj.get("targetQty").toString());
				saleQty=saleQty.add(bdQty);
				TargetQty=TargetQty.add(bdTarget);
				if(month==1 || month==4 || month==7 || month==10 ){
						saleQty1=saleQty1.add(bdQty);
						TargetQty1=TargetQty1.add(bdTarget);

				}else if(month==2 || month==5 || month==8 || month==11 ){
					saleQty2=saleQty2.add(bdQty);
					TargetQty2=TargetQty2.add(bdTarget);

				}else if(month==3 || month==6 || month==9 || month==12 ){
					saleQty3=saleQty3.add(bdQty);
					TargetQty3=TargetQty3.add(bdTarget);

				}
				
				
				if(hqDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQty", Math.round(saleQty.doubleValue()));
					jsonObject.accumulate("TargetQty", Math.round(TargetQty.doubleValue()));
					if(Math.round(TargetQty.doubleValue())==0){
						jsonObject.accumulate("ach", 100+"%");
					}else{
						jsonObject.accumulate("ach", Math.round(saleQty.doubleValue()/TargetQty.doubleValue()*100)+"%");
					}

					jsonObject.accumulate("saleQty1", Math.round(saleQty1.doubleValue()));
					jsonObject.accumulate("TargetQty1", Math.round(TargetQty1.doubleValue()));

					
					if(Math.round(TargetQty1.doubleValue())==0){
						jsonObject.accumulate("ach1",100+"%");
					}else{
						jsonObject.accumulate("ach1", Math.round(saleQty1.doubleValue()/TargetQty1.doubleValue()*100)+"%");
					}
					
					
					jsonObject.accumulate("saleQty2", Math.round(saleQty2.doubleValue()));
					jsonObject.accumulate("TargetQty2", Math.round(TargetQty2.doubleValue()));
					if(Math.round(TargetQty2.doubleValue())==0){
						jsonObject.accumulate("ach2", 100+"%");
					}else{
						jsonObject.accumulate("ach2", Math.round(saleQty2.doubleValue()/TargetQty2.doubleValue()*100)+"%");
					}
					
					
					jsonObject.accumulate("saleQty3", Math.round(saleQty3.doubleValue()));
					jsonObject.accumulate("TargetQty3", Math.round(TargetQty3.doubleValue()));

					if(Math.round(TargetQty3.doubleValue())==0){
						jsonObject.accumulate("ach3", 100+"%");
					}else{
						jsonObject.accumulate("ach3", Math.round(saleQty3.doubleValue()/TargetQty3.doubleValue()*100)+"%");
					}
			
					array.add(jsonObject);
				}
				
			}else{
					obj=hqDatas.get(i-1);
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQty", Math.round(saleQty.doubleValue()));
					jsonObject.accumulate("TargetQty", Math.round(TargetQty.doubleValue()));
					if(Math.round(TargetQty.doubleValue())==0){
						jsonObject.accumulate("ach", 100+"%");
					}else{
						jsonObject.accumulate("ach", Math.round(saleQty.doubleValue()/TargetQty.doubleValue()*100)+"%");
					}

					jsonObject.accumulate("saleQty1", Math.round(saleQty1.doubleValue()));
					jsonObject.accumulate("TargetQty1", Math.round(TargetQty1.doubleValue()));

					
					if(Math.round(TargetQty1.doubleValue())==0){
						jsonObject.accumulate("ach1",100+"%");
					}else{
						jsonObject.accumulate("ach1", Math.round(saleQty1.doubleValue()/TargetQty1.doubleValue()*100)+"%");
					}
					
					
					jsonObject.accumulate("saleQty2", Math.round(saleQty2.doubleValue()));
					jsonObject.accumulate("TargetQty2", Math.round(TargetQty2.doubleValue()));
					if(Math.round(TargetQty2.doubleValue())==0){
						jsonObject.accumulate("ach2", 100+"%");
					}else{
						jsonObject.accumulate("ach2", Math.round(saleQty2.doubleValue()/TargetQty2.doubleValue()*100)+"%");
					}
					
					
					jsonObject.accumulate("saleQty3", Math.round(saleQty3.doubleValue()));
					jsonObject.accumulate("TargetQty3", Math.round(TargetQty3.doubleValue()));

					if(Math.round(TargetQty3.doubleValue())==0){
						jsonObject.accumulate("ach3", 100+"%");
					}else{
						jsonObject.accumulate("ach3", Math.round(saleQty3.doubleValue()/TargetQty3.doubleValue()*100)+"%");
					}
			
					
					array.add(jsonObject);
					obj=hqDatas.get(i);
					country =obj.get("country").toString();
					saleQty=BigDecimal.ZERO;
					saleQty1=BigDecimal.ZERO;
					saleQty2=BigDecimal.ZERO;
					saleQty3=BigDecimal.ZERO;
					
					TargetQty=BigDecimal.ZERO;
					TargetQty1=BigDecimal.ZERO;
					TargetQty2=BigDecimal.ZERO;
					TargetQty3=BigDecimal.ZERO;
					
					i--;

				
			}
		}

		JSONObject object=new JSONObject();
		//BDDateUtil.OrderName(array, "center");
		object.accumulate("data",  array);
		return object;
	}


	@Override
	public JSONObject selectHQChainDataByYear(Map<String, Object> whereMap) {
List<HashMap<String, Object>> hqDatas = soTableDao.selectCountryDataByHQ(whereMap);


		
	
		
		JSONArray array=new JSONArray();
		String country="";
		if(hqDatas.size()>=1){
			country=hqDatas.get(0).get("country").toString();
		}
	
		BigDecimal saleQtyYear=BigDecimal.ZERO;
		BigDecimal saleQtyQ1=BigDecimal.ZERO;
		BigDecimal saleQtyQ2=BigDecimal.ZERO;
		BigDecimal saleQtyQ3=BigDecimal.ZERO;
		BigDecimal saleQtyQ4=BigDecimal.ZERO;
	
		
		
		for (int i = 0; i < hqDatas.size(); i++) {
			HashMap<String, Object> obj=hqDatas.get(i);
			if(WebPageUtil.isStringNullAvaliable(country) && country.equals(obj.get("country").toString())){
				
				int month =Integer.parseInt(obj.get("datadate").toString().split("-")[1]);
				
				BigDecimal bdQty=new BigDecimal(obj.get("saleQty").toString());
				saleQtyYear=saleQtyYear.add(bdQty);
				if(month>=1 && month<=3){
					saleQtyQ1=saleQtyQ1.add(bdQty);
				}else if(month>=4 && month<=6){
					saleQtyQ2=saleQtyQ2.add(bdQty);
				}else if(month>=7 && month<=9){
					saleQtyQ3=saleQtyQ3.add(bdQty);
				}else if(month>=10 && month<=12){
					saleQtyQ4=saleQtyQ4.add(bdQty);
				}
				
				if(hqDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQtyYear", Math.round(saleQtyYear.doubleValue()));
				

					jsonObject.accumulate("saleQtyQ1", Math.round(saleQtyQ1.doubleValue()));
					jsonObject.accumulate("saleQtyQ2", Math.round(saleQtyQ2.doubleValue()));
					
					jsonObject.accumulate("saleQtyQ3", Math.round(saleQtyQ3.doubleValue()));
					jsonObject.accumulate("saleQtyQ4", Math.round(saleQtyQ4.doubleValue()));
					
					jsonObject.accumulate("gro1","");

					if( Math.round(saleQtyQ1.doubleValue())==0){
						jsonObject.accumulate("gro2", "100%");

					}else if( Math.round(saleQtyQ2.doubleValue())==0){
						jsonObject.accumulate("gro2", "-100%");
					}else{
						double gro=(saleQtyQ2.doubleValue()-saleQtyQ1.doubleValue())/saleQtyQ1.doubleValue();
						jsonObject.accumulate("gro2", Math.round(gro*100)+"%");
						
					}
					
					
					if( Math.round(saleQtyQ2.doubleValue())==0){
						jsonObject.accumulate("gro3", "100%");

					}else if( Math.round(saleQtyQ3.doubleValue())==0){
						jsonObject.accumulate("gro3", "-100%");
					}else{
						double gro=(saleQtyQ3.doubleValue()-saleQtyQ2.doubleValue())/saleQtyQ2.doubleValue();
						jsonObject.accumulate("gro3", Math.round(gro*100)+"%");
						
					}
					
					
					
					if( Math.round(saleQtyQ3.doubleValue())==0){
						jsonObject.accumulate("gro4", "100%");

					}else if( Math.round(saleQtyQ4.doubleValue())==0){
						jsonObject.accumulate("gro4", "-100%");
					}else{
						double gro=(saleQtyQ4.doubleValue()-saleQtyQ3.doubleValue())/saleQtyQ3.doubleValue();
						jsonObject.accumulate("gro4", Math.round(gro*100)+"%");
						
					}
					
					
					array.add(jsonObject);
				}
				
			}else{
					obj=hqDatas.get(i-1);
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQtyYear", Math.round(saleQtyYear.doubleValue()));
					
					jsonObject.accumulate("saleQtyQ1", Math.round(saleQtyQ1.doubleValue()));
					jsonObject.accumulate("saleQtyQ2", Math.round(saleQtyQ2.doubleValue()));
					
					jsonObject.accumulate("saleQtyQ3", Math.round(saleQtyQ3.doubleValue()));
					jsonObject.accumulate("saleQtyQ4", Math.round(saleQtyQ4.doubleValue()));
					
					jsonObject.accumulate("gro1","");

					if( Math.round(saleQtyQ1.doubleValue())==0){
						jsonObject.accumulate("gro2", "100%");

					}else if( Math.round(saleQtyQ2.doubleValue())==0){
						jsonObject.accumulate("gro2", "-100%");
					}else{
						double gro=(saleQtyQ2.doubleValue()-saleQtyQ1.doubleValue())/saleQtyQ1.doubleValue();
						jsonObject.accumulate("gro2", Math.round(gro*100)+"%");
						
					}
					
					
					if( Math.round(saleQtyQ2.doubleValue())==0){
						jsonObject.accumulate("gro3", "100%");

					}else if( Math.round(saleQtyQ3.doubleValue())==0){
						jsonObject.accumulate("gro3", "-100%");
					}else{
						double gro=(saleQtyQ3.doubleValue()-saleQtyQ2.doubleValue())/saleQtyQ2.doubleValue();
						jsonObject.accumulate("gro3", Math.round(gro*100)+"%");
						
					}
					
					
					
					if( Math.round(saleQtyQ3.doubleValue())==0){
						jsonObject.accumulate("gro4", "100%");

					}else if( Math.round(saleQtyQ4.doubleValue())==0){
						jsonObject.accumulate("gro4", "-100%");
					}else{
						double gro=(saleQtyQ4.doubleValue()-saleQtyQ3.doubleValue())/saleQtyQ3.doubleValue();
						jsonObject.accumulate("gro4", Math.round(gro*100)+"%");
						
					}
					
				
					array.add(jsonObject);
					obj=hqDatas.get(i);
					country =obj.get("country").toString();
					saleQtyYear=BigDecimal.ZERO;
					saleQtyQ1=BigDecimal.ZERO;
					saleQtyQ2=BigDecimal.ZERO;
					saleQtyQ3=BigDecimal.ZERO;
					saleQtyQ4=BigDecimal.ZERO;
					
					
					i--;

				
			}
		}

		JSONObject object=new JSONObject();
		//BDDateUtil.OrderName(array, "center");
		object.accumulate("data",  array);
		return object;
	}


	@Override
	public JSONObject selectHQChainDataByHalf(Map<String, Object> whereMap) {
		String [] begin=whereMap.get("beginDate").toString().split("-");
		String hlaf="";
		if(Integer.parseInt(begin[1])==1){
			hlaf="H1";
		}else{
			whereMap.put("beginDate", "2017-04-01");
			hlaf="H2";
		}
		
		List<HashMap<String, Object>> hqDatas = soTableDao.selectCountryDataByHQ(whereMap);

		
	
	
		JSONArray array=new JSONArray();
		String country="";
		if(hqDatas.size()>=1){
			country=hqDatas.get(0).get("country").toString();
		}
		BigDecimal saleQtyYear=BigDecimal.ZERO;
		BigDecimal saleQtyH1=BigDecimal.ZERO;
		BigDecimal saleQtyH2=BigDecimal.ZERO;
		
		
		BigDecimal saleQtylast=BigDecimal.ZERO;
		
		for (int i = 0; i < hqDatas.size(); i++) {
			HashMap<String, Object> obj=hqDatas.get(i);
			if(WebPageUtil.isStringNullAvaliable(country) && country.equals(obj.get("country").toString())){
				
				int month =Integer.parseInt(obj.get("datadate").toString().split("-")[1]);
				
				BigDecimal bdQty=new BigDecimal(obj.get("saleQty").toString());
				saleQtyYear=saleQtyYear.add(bdQty);
				if(hlaf.equals("H1")){
					if(month<=3){
						saleQtyH1=saleQtyH1.add(bdQty);
					}else{
						saleQtyH2=saleQtyH2.add(bdQty);
					}
				}else{
					if(month<=6){
						saleQtylast=saleQtylast.add(bdQty);
					}else if(month>6 &&month<=9){
						saleQtyH1=saleQtyH1.add(bdQty);
					}else{
						saleQtyH2=saleQtyH2.add(bdQty);
					}
				}
				
				
				if(hqDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQtyYear", Math.round(saleQtyYear.doubleValue()));
					
					jsonObject.accumulate("saleQtyH1", Math.round(saleQtyH1.doubleValue()));
					jsonObject.accumulate("saleQtyH2", Math.round(saleQtyH2.doubleValue()));
					
					jsonObject.accumulate("saleQtylast", Math.round(saleQtylast.doubleValue()));
					
					double gro=(saleQtyH2.doubleValue()-saleQtyH1.doubleValue())/saleQtyH1.doubleValue();

					if(hlaf.equals("H1")){
						jsonObject.accumulate("gro1","");
						
					}else{
						double gro2=(saleQtyH1.doubleValue()-saleQtylast.doubleValue())/saleQtylast.doubleValue();
						if(Math.round(saleQtyH1.doubleValue())==0){
							jsonObject.accumulate("gro1","-100%");
						}else if(Math.round(saleQtylast.doubleValue())==0){
							jsonObject.accumulate("gro1","100%");
						}else{
							jsonObject.accumulate("gro1",Math.round(gro2*100)+"%");
						}
						
						
					}
					
			
					if(Math.round(saleQtyH1.doubleValue())==0){
						jsonObject.accumulate("gro2","100%");
					}else if(Math.round(saleQtyH2.doubleValue())==0){
						jsonObject.accumulate("gro2","-100%");
					}else{
						jsonObject.accumulate("gro2",Math.round(gro*100)+"%");
					}
					
					array.add(jsonObject);
				}
				
			}else{
					obj=hqDatas.get(i-1);
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQtyYear", Math.round(saleQtyYear.doubleValue()));
					jsonObject.accumulate("saleQtyH1", Math.round(saleQtyH1.doubleValue()));
					jsonObject.accumulate("saleQtyH2", Math.round(saleQtyH2.doubleValue()));
					jsonObject.accumulate("saleQtylast", Math.round(saleQtylast.doubleValue()));

					double gro=(saleQtyH2.doubleValue()-saleQtyH1.doubleValue())/saleQtyH1.doubleValue();

					if(hlaf.equals("H1")){
						jsonObject.accumulate("gro1","");
						
					}else{
						double gro2=(saleQtyH1.doubleValue()-saleQtylast.doubleValue())/saleQtylast.doubleValue();
						if(Math.round(saleQtyH1.doubleValue())==0){
							jsonObject.accumulate("gro1","-100%");
						}else if(Math.round(saleQtylast.doubleValue())==0){
							jsonObject.accumulate("gro1","100%");
						}else{
							jsonObject.accumulate("gro1",Math.round(gro2*100)+"%");
						}
						
						
					}
					
			
					if(Math.round(saleQtyH1.doubleValue())==0){
						jsonObject.accumulate("gro2","100%");
					}else if(Math.round(saleQtyH2.doubleValue())==0){
						jsonObject.accumulate("gro2","-100%");
					}else{
						jsonObject.accumulate("gro2",Math.round(gro*100)+"%");
					}
					
					array.add(jsonObject);
					obj=hqDatas.get(i);
					country =obj.get("country").toString();
					saleQtyYear=BigDecimal.ZERO;
					saleQtyH1=BigDecimal.ZERO;
					saleQtyH2=BigDecimal.ZERO;
					saleQtylast=BigDecimal.ZERO;
					
					i--;

				
			}
		}

		JSONObject object=new JSONObject();
	//	BDDateUtil.OrderName(array, "center");
		object.accumulate("data",  array);
		return object;
	}


	@Override
	public JSONObject selectHQChainDataByQuarter(Map<String, Object> whereMap) {

		String [] beg=whereMap.get("beginDate").toString().split("-");
		
		String [] end=whereMap.get("endDate").toString().split("-");


		if(Integer.parseInt(beg[1].toString())==4){
			whereMap.put("beginDate", "2017-03-01");
		}else if(Integer.parseInt(beg[1].toString())==7){
			whereMap.put("beginDate", "2017-06-01");
		}else if(Integer.parseInt(beg[1].toString())==10){
			whereMap.put("beginDate", "2017-09-01");
		}
		List<HashMap<String, Object>> hqDatas = soTableDao.selectCountryDataByHQ(whereMap);

	
		JSONArray array=new JSONArray();
		
		BigDecimal saleQty=BigDecimal.ZERO;
		BigDecimal saleQty1=BigDecimal.ZERO;
		BigDecimal saleQty2=BigDecimal.ZERO;
		BigDecimal saleQty3=BigDecimal.ZERO;
		
		BigDecimal saleQtylast=BigDecimal.ZERO;
		
		String country="";
		if(hqDatas.size()>=1){
			country=hqDatas.get(0).get("country").toString();
		}
	
		
		
		for (int i = 0; i < hqDatas.size(); i++) {
			HashMap<String, Object> obj=hqDatas.get(i);
			if(WebPageUtil.isStringNullAvaliable(country) && country.equals(obj.get("country").toString())){
				
				int month =Integer.parseInt(obj.get("datadate").toString().split("-")[1]);
				
				BigDecimal bdQty=new BigDecimal(obj.get("saleQty").toString());
				saleQty=saleQty.add(bdQty);
				
					
				if(Integer.parseInt(end[1].toString())==3){
					if(month==1){
							saleQty1=saleQty1.add(bdQty);
	
					}else if(month==2){
						saleQty2=saleQty2.add(bdQty);
					}else if(month==3){
						saleQty3=saleQty3.add(bdQty);
	
					}
						
				}else if(Integer.parseInt(end[1].toString())==6){
					if(month==4){
						saleQty1=saleQty1.add(bdQty);
					}else if(month==5){
						saleQty2=saleQty2.add(bdQty);
					}else if(month==6){
						saleQty3=saleQty3.add(bdQty);
	
					}else if(month==3){
						saleQtylast=saleQtylast.add(bdQty);
	
					}
					
				}else if(Integer.parseInt(end[1].toString())==9){
					
					if(month==7){
						saleQty1=saleQty1.add(bdQty);
					}else if(month==8){
						saleQty2=saleQty2.add(bdQty);
					}else if(month==9){
						saleQty3=saleQty3.add(bdQty);
	
					}else if(month==6){
						saleQtylast=saleQtylast.add(bdQty);
	
					}
					
					
				}else if(Integer.parseInt(end[1].toString())==12){

					if(month==10){
						saleQty1=saleQty1.add(bdQty);
					}else if(month==11){
						saleQty2=saleQty2.add(bdQty);
					}else if(month==12){
						saleQty3=saleQty3.add(bdQty);
	
					}else if(month==9){
						saleQtylast=saleQtylast.add(bdQty);
	
					}
				}
				
				
				if(hqDatas.size() - i == 1){
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQty", Math.round(saleQty.doubleValue()));
					
					jsonObject.accumulate("saleQty1", Math.round(saleQty1.doubleValue()));
					jsonObject.accumulate("saleQty2", Math.round(saleQty2.doubleValue()));
					jsonObject.accumulate("saleQty3", Math.round(saleQty3.doubleValue()));
					
					jsonObject.accumulate("saleQtylast", Math.round(saleQtylast.doubleValue()));
					
					double gro2=(saleQty2.doubleValue()-saleQty1.doubleValue())/saleQty1.doubleValue();
					double gro3=(saleQty3.doubleValue()-saleQty2.doubleValue())/saleQty2.doubleValue();

					
					if(Integer.parseInt(end[1].toString())==3){
						jsonObject.accumulate("gro1", "");
						
					}else{
						double gro1=(saleQty1.doubleValue()-saleQtylast.doubleValue())/saleQtylast.doubleValue();
						
						if( Math.round(saleQty1.doubleValue())==0){
							jsonObject.accumulate("gro1", "-100%");
						}else if( Math.round(saleQtylast.doubleValue())==0){
							jsonObject.accumulate("gro1", "100%");
						}else{
							jsonObject.accumulate("gro1", Math.round(gro1*100)+"%");

						}
						
					}
					

					
					if( Math.round(saleQty2.doubleValue())==0){
						jsonObject.accumulate("gro2", "-100%");
					}else if( Math.round(saleQty1.doubleValue())==0){
						jsonObject.accumulate("gro1", "100%");
					}else{
						jsonObject.accumulate("gro2", Math.round(gro2*100)+"%");

					}
					
					if( Math.round(saleQty2.doubleValue())==0){
						jsonObject.accumulate("gro3", "100%");
					}else if( Math.round(saleQty3.doubleValue())==0){
						jsonObject.accumulate("gro3", "-100%");
					}else{
						jsonObject.accumulate("gro3", Math.round(gro3*100)+"%");

					}
					
					
					array.add(jsonObject);
				}
				
			}else{
					obj=hqDatas.get(i-1);
					JSONObject jsonObject=new JSONObject();
					jsonObject.accumulate("center",obj.get("center"));
					jsonObject.accumulate("country", obj.get("country"));
					jsonObject.accumulate("saleQty", Math.round(saleQty.doubleValue()));
					
					jsonObject.accumulate("saleQty1", Math.round(saleQty1.doubleValue()));
					jsonObject.accumulate("saleQty2", Math.round(saleQty2.doubleValue()));
					jsonObject.accumulate("saleQty3", Math.round(saleQty3.doubleValue()));
					jsonObject.accumulate("saleQtylast", Math.round(saleQtylast.doubleValue()));

					double gro2=(saleQty2.doubleValue()-saleQty1.doubleValue())/saleQty1.doubleValue();
					double gro3=(saleQty3.doubleValue()-saleQty2.doubleValue())/saleQty2.doubleValue();

					
					
					if(Integer.parseInt(end[1].toString())==3){
						jsonObject.accumulate("gro1", "");
						
					}else{
						double gro1=(saleQty1.doubleValue()-saleQtylast.doubleValue())/saleQtylast.doubleValue();
						
						if( Math.round(saleQty1.doubleValue())==0){
							jsonObject.accumulate("gro1", "-100%");
						}else if( Math.round(saleQtylast.doubleValue())==0){
							jsonObject.accumulate("gro1", "100%");
						}else{
							jsonObject.accumulate("gro1", Math.round(gro1*100)+"%");

						}
						
					}
					

					
					if( Math.round(saleQty2.doubleValue())==0){
						jsonObject.accumulate("gro2", "-100%");
					}else if( Math.round(saleQty1.doubleValue())==0){
						jsonObject.accumulate("gro1", "100%");
					}else{
						jsonObject.accumulate("gro2", Math.round(gro2*100)+"%");

					}
					
					if( Math.round(saleQty2.doubleValue())==0){
						jsonObject.accumulate("gro3", "100%");
					}else if( Math.round(saleQty3.doubleValue())==0){
						jsonObject.accumulate("gro3", "-100%");
					}else{
						jsonObject.accumulate("gro3", Math.round(gro3*100)+"%");

					}
					
					
					
					
					array.add(jsonObject);
					obj=hqDatas.get(i);
					country =obj.get("country").toString();
					saleQty=BigDecimal.ZERO;
					saleQty1=BigDecimal.ZERO;
					saleQty2=BigDecimal.ZERO;
					saleQty3=BigDecimal.ZERO;
					saleQtylast=BigDecimal.ZERO;
					
					i--;

				
			}
		}

		JSONObject object=new JSONObject();
		//BDDateUtil.OrderName(array, "center");
		object.accumulate("data",  array);
		return object;
	}

	

	@Override
	public List<HashMap<String, Object>> queryStateTimeSalesBycountry(Map<String, Object> map) throws Exception {
		if("YEAR".equals(map.get("timeType")) ){
			return countryYearSaleInfo(map);
		}else{
			return countryQuarterSaleInfo(map);
		}
	};
	

	public LinkedList<HashMap<String, Object>> countryQuarterSaleInfo(Map<String, Object> map) throws Exception{
		LinkedList<HashMap<String,Object>> resultList = new LinkedList<HashMap<String,Object>>();
		
		String thisYearStartDate = (String) map.get("startDate");
		String thisYearEndDate = (String) map.get("endDate");
		
		int yearInt = Integer.parseInt(((String) map.get("startDate")).split("-")[0]);//获取今年年份
		if(Contents.BIG_TAB.equals(map.get("tab"))){
			map.put("searchStr", getHqDiffBigSql(yearInt));
		};
		
		List<BDTable> listHqStaTable =soTableDao.queryStateTimeSalesBycountry(map);
		List<HashMap<String,Object>> thisYearList = getQuarterStatistics(listHqStaTable,map);//今年销量信息
		
		int lastInt = Integer.parseInt(((String) map.get("startDate")).split("-")[0]) - 1;//获取去年年份
		if(Contents.BIG_TAB.equals(map.get("tab"))){
			map.put("searchStr", getHqDiffBigSql(lastInt));
		};
		map.put("startDate", getLastYearDate((String) map.get("startDate")));
		map.put("endDate", getLastYearDate((String) map.get("endDate")));
		listHqStaTable =soTableDao.queryStateTimeSalesBycountry(map);
		List<HashMap<String,Object>> lastYearList = getQuarterStatistics(listHqStaTable,map);//去年销量信息
		
		map.put("thisYearStartDate", thisYearStartDate);
		map.put("thisYearEndDate", thisYearEndDate);
		map.put("isSub","true");
		List<BDTable> couList = soTableDao.querySaleCountry(map);//有销量的国家
		
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
	 * 季度计算统计
	 * @param listSta
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> getQuarterStatistics(List<BDTable> listSta,Map<String, Object> map) throws Exception{
		
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
	
	public LinkedList<HashMap<String, Object>> countryYearSaleInfo(Map<String, Object> map) throws Exception{
		LinkedList<HashMap<String,Object>> resultList = new LinkedList<HashMap<String,Object>>();
		
		String thisYearStartDate = (String) map.get("startDate");
		String thisYearEndDate = (String) map.get("endDate");
		
		String minSaleDate = getLastYearDate((String) map.get("startDate"));//最小时间
		String maxSaleDate = (String) map.get("endDate");//最大时间
		
		int yearInt = Integer.parseInt(((String) map.get("startDate")).split("-")[0]);//获取今年年份
		if(Contents.BIG_TAB.equals(map.get("tab"))){
			map.put("searchStr", getHqDiffBigSql(yearInt));
		};
		
		List<BDTable> listHqStaTable =soTableDao.queryStateTimeSalesBycountry(map);
		List<HashMap<String,Object>> thisYearList =  getYearStatistics(listHqStaTable);//今年销量信息
		
		int lastInt = Integer.parseInt(((String) map.get("startDate")).split("-")[0]) - 1;//获取去年年份
		if(Contents.BIG_TAB.equals(map.get("tab"))){
			map.put("searchStr", getHqDiffBigSql(lastInt));
		};
		map.put("startDate", getLastYearDate((String) map.get("startDate")));
		map.put("endDate", getLastYearDate((String) map.get("endDate")));
		listHqStaTable = soTableDao.queryStateTimeSalesBycountry(map);
		List<HashMap<String,Object>> lastYearList =  getYearStatistics(listHqStaTable);//去年销量信息
		

			map.put("startDate", minSaleDate);
			map.put("endDate", maxSaleDate);
		
		List<BDTable> couList = soTableDao.querySaleCountry(map);//有销量的国家
		
		for (int i = 0; i < couList.size(); i++) {
			HashMap<String,Object> couMap = new HashMap<String,Object>();
			couMap.put("country", couList.get(i).getCountry());
			couMap.put("center", couList.get(i).getCenter());
			//去年销售状况
			for (int j = 0; j < lastYearList.size(); j++) {
				if(couList.get(i).getCountry().equals(lastYearList.get(j).get("country"))){
					couMap.put("last_year_qty",  lastYearList.get(j).get("this_qty"));//去年总销量
					couMap.put("last_year_Q1",  lastYearList.get(j).get("this_Q1"));
					couMap.put("last_year_Q2",  lastYearList.get(j).get("this_Q2"));
					couMap.put("last_year_Q3",  lastYearList.get(j).get("this_Q3"));
					couMap.put("last_year_Q4",  lastYearList.get(j).get("this_Q4"));
					break;
				}
			}
			
			//今年销售状况
			for (int j = 0; j < thisYearList.size(); j++) {
				if(couList.get(i).getCountry().equals(thisYearList.get(j).get("country"))){
					couMap.put("this_year_qty",  thisYearList.get(j).get("this_qty"));//今年总销量
					couMap.put("this_year_Q1",  thisYearList.get(j).get("this_Q1"));
					couMap.put("this_year_Q2",  thisYearList.get(j).get("this_Q2"));
					couMap.put("this_year_Q3",  thisYearList.get(j).get("this_Q3"));
					couMap.put("this_year_Q4",  thisYearList.get(j).get("this_Q4"));
					break;
				}
			}
			resultList.add(couMap);
		}
		return statGrowth(resultList);
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

	
	public String getHqDiffBigSql (int Year){
		if(Year >= 2018){
			return " and pt.`size` > CAST('55' as SIGNED) ";
		}else{
			return " and pt.`size` > CAST('48' as SIGNED) ";
		}
	}
	
	/**
	 * 年度计算统计
	 * @param listSta
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> getYearStatistics(List<BDTable> listSta) throws Exception{
		
		List<HashMap<String,Object>> thisYearList = new ArrayList<HashMap<String,Object>>();//今年销量信息
		if(listSta.size() > 0){
			String county  = listSta.get(0).getCountry();//储存第一个国家
			HashMap<String,Object> couMap = new HashMap<String,Object>();//储存国家对象
			
			BigDecimal thisYear = BigDecimal.ZERO;//国家今年总销量
			BigDecimal thisQ1 = BigDecimal.ZERO;
			BigDecimal thisQ2 = BigDecimal.ZERO;
			BigDecimal thisQ3 = BigDecimal.ZERO;
			BigDecimal thisQ4 = BigDecimal.ZERO;
			
			for (int i = 0; i < listSta.size(); i++) {
				if (county.equals(listSta.get(i).getCountry())) {
					if( Integer.parseInt(listSta.get(i).getMoth()) >=1&&
							Integer.parseInt(listSta.get(i).getMoth())<=3   ){
						thisQ1 = thisQ1.add(new BigDecimal(listSta.get(i).getQty()));//累加国家上半年销量
					}else if( Integer.parseInt(listSta.get(i).getMoth())  >=4&&
							Integer.parseInt(listSta.get(i).getMoth())<=6  ){
						thisQ2 = thisQ2.add(new BigDecimal(listSta.get(i).getQty()));//累加国家上半年销量
					}else if( Integer.parseInt(listSta.get(i).getMoth())  >=7 &&
							Integer.parseInt(listSta.get(i).getMoth())<=9  ){
						thisQ3 = thisQ3.add(new BigDecimal(listSta.get(i).getQty()));//累加国家上半年销量
					}else if( Integer.parseInt(listSta.get(i).getMoth())  >=10 &&
							Integer.parseInt(listSta.get(i).getMoth())<=12  ){
						thisQ4 = thisQ4.add(new BigDecimal(listSta.get(i).getQty()));//累加国家上半年销量
					}
					thisYear = thisYear.add(new BigDecimal(listSta.get(i).getQty()));//累加国家全年销量
					
					//存储最后一个国家
					if(listSta.size() - i == 1){
						couMap.put("center", listSta.get(i).getCenter());//存储业务中心
						couMap.put("country", listSta.get(i).getCountry());//存储国家
						couMap.put("this_Q1", Math.round(thisQ1.doubleValue()));
						couMap.put("this_Q2", Math.round(thisQ2.doubleValue()));
						couMap.put("this_Q3",  Math.round(thisQ3.doubleValue()));
						couMap.put("this_Q4",  Math.round(thisQ4.doubleValue()));
						couMap.put("this_qty", thisYear.toString());//存储国家今年总销量
						thisYearList.add(couMap);
					}
					
				}else{
					couMap.put("center", listSta.get(i-1).getCenter());//存储业务中心
					couMap.put("country", listSta.get(i-1).getCountry());//存储国家
					couMap.put("this_Q1", Math.round(thisQ1.doubleValue()));
					couMap.put("this_Q2", Math.round(thisQ2.doubleValue()));
					couMap.put("this_Q3",  Math.round(thisQ3.doubleValue()));
					couMap.put("this_Q4",  Math.round(thisQ4.doubleValue()));
					couMap.put("this_qty", thisYear.toString());//存储国家今年总销量
					thisYearList.add(couMap);
					
					//初始化数据
					county  = listSta.get(i).getCountry();//储存第一个国家
					couMap = new HashMap<String,Object>();
					thisYear = BigDecimal.ZERO;
					thisQ1 = BigDecimal.ZERO;
					thisQ2 = BigDecimal.ZERO;
					thisQ3 = BigDecimal.ZERO;
					thisQ4 = BigDecimal.ZERO;
					i --;
				}
			}
		}
		return thisYearList;
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
			String lastQ1Qty = new BigDecimal(isNullReturnZero(l.get(i).get("last_year_Q1"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			String thisQ1Qty = new BigDecimal(isNullReturnZero(l.get(i).get("this_year_Q1"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			l.get(i).put("Q1_growth", getGrowthRate(Integer.parseInt(lastQ1Qty),Integer.parseInt(thisQ1Qty)));
			String lastQ2Qty = new BigDecimal(isNullReturnZero(l.get(i).get("last_year_Q2"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			String thisQ2Qty = new BigDecimal(isNullReturnZero(l.get(i).get("this_year_Q2"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			l.get(i).put("Q2_growth", getGrowthRate(Integer.parseInt(lastQ2Qty),Integer.parseInt(thisQ2Qty)));
			String lastQ3Qty = new BigDecimal(isNullReturnZero(l.get(i).get("last_year_Q3"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			String thisQ3Qty = new BigDecimal(isNullReturnZero(l.get(i).get("this_year_Q3"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			l.get(i).put("Q3_growth", getGrowthRate(Integer.parseInt(lastQ3Qty),Integer.parseInt(thisQ3Qty)));
			String lastQ4Qty = new BigDecimal(isNullReturnZero(l.get(i).get("last_year_Q4"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			String thisQ4Qty = new BigDecimal(isNullReturnZero(l.get(i).get("this_year_Q4"))).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			l.get(i).put("Q4_growth", getGrowthRate(Integer.parseInt(lastQ4Qty),Integer.parseInt(thisQ4Qty)));
		}
		return l;
	};
	
}


