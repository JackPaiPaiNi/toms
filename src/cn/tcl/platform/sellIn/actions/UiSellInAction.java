package cn.tcl.platform.sellIn.actions;

import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;

import com.opensymphony.xwork2.ActionContext;


import cn.tcl.common.BaseAction;
import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.excel.imports.ExcelImportUtil;
import cn.tcl.platform.sale.service.ISaleService;
import cn.tcl.platform.sale.vo.Sale;
import cn.tcl.platform.sale.vo.SaleTarget;
import cn.tcl.platform.sale.vo.SampleDevice;
import cn.tcl.platform.sale.vo.TerminalPhoto;
import cn.tcl.platform.sample.vo.SampleTarget;
import cn.tcl.platform.sellIn.service.ISellInService;
import cn.tcl.platform.sellIn.vo.SellIn;
import cn.tcl.platform.shop.vo.Shop;
import cn.tcl.platform.user.vo.UserLogin;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings("all")
public class UiSellInAction  extends BaseAction{
	@Autowired(required = false)
	@Qualifier("sellInService")
	private ISellInService sellInService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public String loadSellInByHqPage(){
		try{
			return SUCCESS;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	
	public String loadReturnPage(){
		try{
			return SUCCESS;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	
	public String loadSellInPage(){
		try{
			return SUCCESS;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	
	public void exportSellInTemplate(){
		try {
			String title ="DATA";
			String fileName = getExportExcelName("Sell In");
			final String userAgent = request.getHeader("USER-AGENT");
	      if (null != userAgent){    
	            if (-1 != userAgent.indexOf("Firefox")) {//Firefox    
	            	fileName = new String(fileName.getBytes(), "ISO8859-1");    
	            }else if (-1 != userAgent.indexOf("Chrome")) {//Chrome    
	            	fileName = new String(fileName.getBytes(), "ISO8859-1");    
	            } else {//IE7+    
	            	fileName = URLEncoder.encode(fileName, "UTF-8");    
	            	fileName = StringUtils.replace(fileName, "+", "%20");//替换空格    
	            }    
	        } else {    
	        	fileName = fileName;    
	        }  
			
	      String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds)){
					conditions += "  pa.party_id IN ("+userPartyIds+") ";
				}else{
					conditions += "  1=2 ";
				}
			}
			else
			{
				conditions += "  1=1 ";
			}
			
			//国家化列表头
			String[] excelHeader = {"Country","Store","Model","Order Number","Date","Quantity"};
			XSSFWorkbook workbook = sellInService.exportExcel(conditions, excelHeader, title);
			response.setContentType("application/vnd.ms-excel");   
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
	        OutputStream ouputStream = response.getOutputStream();    
	        workbook.write(ouputStream);    
	        ouputStream.flush();    
	        ouputStream.close();   
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
	}
	

private String getExportExcelName(String name){
	String excelName = "";
	try {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		excelName = name+".xlsx";
	} catch (Exception e) {
		e.printStackTrace();
		log.error(e.getMessage(),e);
	}
	return excelName;
}




public void importSellIn() {
	try {

		String errorMsg = sellInService.readExcel(uploadExcel,
				uploadExcelFileName);

		if ("".equals(errorMsg)) {
			WebPageUtil.writeBack("success");
		} else {
			WebPageUtil.writeBack(errorMsg);
		}
	} catch (Exception e) {
		String errorMsg = e.getMessage();
		if (null == errorMsg || "".equals(errorMsg)) {
			errorMsg = this.getText("import.error.exist");
		}
		log.error(e.getStackTrace());
		e.printStackTrace();
		WebPageUtil.writeBack(errorMsg);
	}
}




public void importReturn() {
	try {

		String errorMsg = sellInService.readExcelByReturn(uploadExcel,
				uploadExcelFileName);

		if ("".equals(errorMsg)) {
			WebPageUtil.writeBack("success");
		} else {
			WebPageUtil.writeBack(errorMsg);
		}
	} catch (Exception e) {
		String errorMsg = e.getMessage();
		if (null == errorMsg || "".equals(errorMsg)) {
			errorMsg = this.getText("import.error.exist");
		}
		log.error(e.getStackTrace());
		e.printStackTrace();
		WebPageUtil.writeBack(errorMsg);
	}
}


public void selectSellInByHq() {
	System.out.println("===========selectSellInByHq==========");
	response.setHeader("Content-Type", "application/json");
	String beginDate=request.getParameter("beginDate");
	String endDate=request.getParameter("endDate");
	String center=request.getParameter("center");
	String country=request.getParameter("country");
	String line=request.getParameter("line");
	System.out.println("============beginDate========="+beginDate);
	System.out.println("============endDate========="+endDate);
	Map<String,Object> sellInMap = new HashMap<String,Object>();
	sellInMap.put("beginDate", beginDate);
	sellInMap.put("endDate", endDate);
	sellInMap.put("center",center);
	sellInMap.put("country",country);
	sellInMap.put("line",line);
	JSONObject object = null;
	try {
		object = sellInService.selectSellInByHq(sellInMap);
	} catch (Exception e) {
		e.printStackTrace();
	}
	System.out.println(object.toString());
	WebPageUtil.writeBack(object.toString());
}


public void loadModelByHQ(){
	response.setHeader("Content-Type", "application/json");
	JSONObject result = new JSONObject(); 
	
	String rows = "";
		
		
	try {
		request.setCharacterEncoding("utf-8");
		String countryId = request.getParameter("countryId");
		List<SampleTarget> shopList = sellInService.selectModelByHq(countryId);
		rows = JSONArray.fromObject(shopList).toString();
		result.accumulate("rows", rows);
	} catch (Exception e) {
		e.printStackTrace();
	}
	System.out.println(result.toString());
	WebPageUtil.writeBack(result.toString());
}




public void selectReturn() {
	JSONObject result = new JSONObject();
	response.setHeader("Content-Type", "application/json");
	String sort = request.getParameter("sort");
	String order = request.getParameter("order");
	int page = Integer.valueOf(request.getParameter("page"));
	int limit = Integer.valueOf(request.getParameter("rows"));
	int start = (page-1)*limit;
	
	

	
	
	String searchCountry=request.getParameter("searchCountry");
	String searchPatry=request.getParameter("searchPatry");
	String searchCustomer=request.getParameter("searchCustomer");
	String beginDate=request.getParameter("beginDate");
	String endDate=request.getParameter("endDate");
	String searchLine=request.getParameter("searchLine");

	Date dt=new Date();
    SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
    String  dates=matter1.format(dt);
	if(beginDate==null || beginDate.equals("")){
		String [] date=dates.split("-");
		beginDate=date[0]+"-"+date[1]+"-01";
	}
	if(endDate==null || endDate.equals("")){
		endDate=dates;
	}
	System.out.println("=======1========searchLine==============="+searchLine);
	if(searchLine!=null && !searchLine.equals("\"\"")  && searchLine!="\"\""   &&   !searchLine.equals("")){
		searchLine="  AND pt.product_line IN ( "+searchLine+")";
	}else{
		searchLine="";
	}
	
	Map<String,Object> sellInMap = new HashMap<String,Object>();
	sellInMap.put("sort", sort);
	sellInMap.put("order", order);
	sellInMap.put("page", page);
	sellInMap.put("limit", limit);
	sellInMap.put("start", start);
	sellInMap.put("sort", sort);
	sellInMap.put("isHq",WebPageUtil.isHQRole());
	sellInMap.put("beginDate", beginDate);
	sellInMap.put("endDate", endDate);
	sellInMap.put("searchCountry",searchCountry);
	sellInMap.put("searchPatry",searchPatry);
	sellInMap.put("searchCustomer",searchCustomer);
	sellInMap.put("searchLine",searchLine);
	String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
	if(!WebPageUtil.isHAdmin()){
		if(userPartyIds!=null){
			sellInMap.put("conditions", "  pa.party_id in ("+userPartyIds+")");
		}
		
			
	}else{
		sellInMap.put("conditions", "  1=1 ");
	}
	try {
		Map<String, Object> map = sellInService.selectReturn(sellInMap);
		int total = (Integer)map.get("total");
		List<Shop> list = (ArrayList<Shop>)map.get("rows");
		JSONArray jsonArray = JSONArray.fromObject(list);
		String rows = jsonArray.toString();
		result.accumulate("rows", rows);
		result.accumulate("total", total);
		result.accumulate("success", true);
	} catch (Exception e) {
		e.printStackTrace();
		log.error(e.getMessage(),e);
		String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
		result.accumulate("rows", new JSONArray());
		result.accumulate("total", 0);
		result.accumulate("success", true);
		result.accumulate("msg", msg);
	}
	WebPageUtil.writeBack(result.toString());
}

public void selectSellInByTable() {
	JSONObject result = new JSONObject();
	response.setHeader("Content-Type", "application/json");
	String sort = request.getParameter("sort");
	String order = request.getParameter("order");
	int page = Integer.valueOf(request.getParameter("page"));
	int limit = Integer.valueOf(request.getParameter("rows"));
	int start = (page-1)*limit;
	
	

	
	
	String searchCountry=request.getParameter("searchCountry");
	String searchPatry=request.getParameter("searchPatry");
	String searchCustomer=request.getParameter("searchCustomer");
	String beginDate=request.getParameter("beginDate");
	String endDate=request.getParameter("endDate");
	String searchLine=request.getParameter("searchLine");

	Date dt=new Date();
    SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
    String  dates=matter1.format(dt);
	if(beginDate==null || beginDate.equals("")){
		String [] date=dates.split("-");
		beginDate=date[0]+"-"+date[1]+"-01";
	}
	if(endDate==null || endDate.equals("")){
		endDate=dates;
	}
	System.out.println("=======1========searchLine==============="+searchLine);
	if(searchLine!=null && !searchLine.equals("\"\"")  && searchLine!="\"\""   &&   !searchLine.equals("")){
		searchLine="  AND pt.product_line IN ( "+searchLine+")";
	}else{
		searchLine="";
	}
	
	Map<String,Object> sellInMap = new HashMap<String,Object>();
	sellInMap.put("sort", sort);
	sellInMap.put("order", order);
	sellInMap.put("page", page);
	sellInMap.put("limit", limit);
	sellInMap.put("start", start);
	sellInMap.put("sort", sort);
	sellInMap.put("isHq",WebPageUtil.isHQRole());
	sellInMap.put("beginDate", beginDate);
	sellInMap.put("endDate", endDate);
	sellInMap.put("searchCountry",searchCountry);
	sellInMap.put("searchPatry",searchPatry);
	sellInMap.put("searchCustomer",searchCustomer);
	sellInMap.put("searchLine",searchLine);
	String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
	if(!WebPageUtil.isHAdmin()){
		if(userPartyIds!=null){
			sellInMap.put("conditions", "  pa.party_id in ("+userPartyIds+")");
		}
		
			
	}else{
		sellInMap.put("conditions", "  1=1 ");
	}
	try {
		Map<String, Object> map = sellInService.selectSellInByTable(sellInMap);
		int total = (Integer)map.get("total");
		List<Shop> list = (ArrayList<Shop>)map.get("rows");
		JSONArray jsonArray = JSONArray.fromObject(list);
		String rows = jsonArray.toString();
		result.accumulate("rows", rows);
		result.accumulate("total", total);
		result.accumulate("success", true);
	} catch (Exception e) {
		e.printStackTrace();
		log.error(e.getMessage(),e);
		String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
		result.accumulate("rows", new JSONArray());
		result.accumulate("total", 0);
		result.accumulate("success", true);
		result.accumulate("msg", msg);
	}
	WebPageUtil.writeBack(result.toString());
}

}
