package cn.tcl.platform.sample.action;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.excel.actions.DateUtil;
import cn.tcl.platform.sample.service.ISampleTargetService;
import cn.tcl.platform.sample.vo.SampleTarget;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SampleTargetAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	
	@Autowired(required = false)
	@Qualifier("sampleTargetService")
	private ISampleTargetService service;
	
	public String loadSampleTargetPage(){
		try {
			return "success";
		} catch (Exception e) {
			return "error";
		}
	}
	
	public String loadSampleTargetAchPage(){
		try {
			return "success";
		} catch (Exception e) {
			return "error";
		}
	}

	
	
	public void selectSampleTarget(){
		String searchCountry=request.getParameter("searchCountry");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String searchPatry = request.getParameter("searchPatry");
		String searchCustomer = request.getParameter("searchCustomer");
		String searchShop = request.getParameter("searchShop");
		String searchModel = request.getParameter("searchModel");
		String searchHqModel = request.getParameter("searchHqModel");
		String  searchLine=request.getParameter("searchLine");
		String userId = (String) request.getSession().getAttribute("loginUserId");
		
		String pageStr=request.getParameter("page");
		int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
		String rowStr=request.getParameter("rows");
		int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
		int start = (page-1)*limit;
		
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
			searchLine=searchLine.replaceAll(",","\',\'");
			searchLine="  AND pt.product_line IN (\'"+searchLine+"\')";
		}else{
			searchLine="";
		}
		System.out.println("=======2========searchLine==============="+searchLine);
		Map<String,Object> samMap = new HashMap<String,Object>();
		samMap.put("userId", userId);
		samMap.put("limit", limit);
		samMap.put("start", start);
		samMap.put("searchCountry", searchCountry);
		samMap.put("beginDate", beginDate);
		samMap.put("endDate", endDate);
		samMap.put("searchPatry", searchPatry);
		samMap.put("searchCustomer", searchCustomer);
		samMap.put("searchShop", searchShop);
		samMap.put("searchModel", searchModel);
		samMap.put("searchHqModel", searchHqModel);
		samMap.put("searchLine", searchLine);
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		if(!WebPageUtil.isHAdmin()){
			if(userPartyIds!=null){
				samMap.put("conditions", "  pa.party_id in ("+userPartyIds+")");
			}
			
				
		}else{
			samMap.put("conditions", "  1=1 ");
		}
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		System.out.println("============="+samMap);
		List<SampleTarget> sampleList = null;
		try {
			sampleList = service.selectSampleTargetListByLine(samMap);
			Integer total = service.selectSampleTargetListCountByLine(samMap);
				result.accumulate("rows", sampleList);
				result.accumulate("total", total);
				result.accumulate("success", true);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void importSampleTarget() {
		try {

			String errorMsg = service.readExcel(uploadExcel,
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
	
	public void exportSampleTTemplate(){
		try {
			String title ="DATA";
			String fileName = getExportExcelName("Sample Target");
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
			String[] excelHeader = {"Country","Store","Date"};
			XSSFWorkbook workbook = service.exporExcel(conditions, excelHeader, title);
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



public void loadShopByParty(){
	
	JSONArray result = new JSONArray();
	response.setHeader("Content-Type", "application/json");
	try {
		request.setCharacterEncoding("utf-8");
		String countryId = request.getParameter("countryId");
		String partyId = request.getParameter("partyId");
		List<SampleTarget> shopList = service.selectShopByParty(countryId, partyId);
		result = JSONArray.fromObject(shopList);
	} catch (Exception e) {
		e.printStackTrace();
	}
	WebPageUtil.writeBack(result.toString());
}
	

public void loadModelByParty(){
	
	JSONArray result = new JSONArray();
	response.setHeader("Content-Type", "application/json");
	try {
		request.setCharacterEncoding("utf-8");
		String countryId = request.getParameter("countryId");
		List<SampleTarget> shopList = service.selectModelByCountry(countryId);
		result = JSONArray.fromObject(shopList);
	} catch (Exception e) {
		e.printStackTrace();
	}
	WebPageUtil.writeBack(result.toString());
}


public void editSampleTargetById(){
	JSONObject result = new JSONObject();
	String id=request.getParameter("editId");
	if(id!=null && id.trim()!=""){
		try {
			String userId = (String) request.getSession().getAttribute("loginUserId");
			
			String shopId=request.getParameter("shopId");
			String model=request.getParameter("model");
			String quan=request.getParameter("quantity");
			int quantity=Integer.parseInt("".equals(quan)?"0":quan);
			String dataDate=request.getParameter("dataDate");
			
			SampleTarget target=new SampleTarget();
			target.setId(Integer.parseInt(id));
			target.setShopId(shopId);
			target.setQuantity(quantity);
			target.setModel(model);
			target.setDataDate(dataDate);
			target.setUserId(userId);
			
			int row =service.updateSampleTargetById(target);
			
			
			if(row>0){
				result.accumulate("success", true);
			}else{
				result.accumulate("success", false);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
	}
	
	WebPageUtil.writeBack(result.toString());
}


public void deleteSampleTarget(){
	JSONObject result = new JSONObject();
	Integer id=WebPageUtil.p2Int(request.getParameter("id"), null);
	if(id==null){
		result.accumulate("success", false);
		result.accumulate("msg", "lost parameter Id!");
	}else{
		try {
			service.deleteSampleTarget(id);
			result.accumulate("success", true);
			result.accumulate("msg", "success delete");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
	}
	WebPageUtil.writeBack(result.toString());
}



public void selectSampleAch() throws ParseException{
	String searchCountry=request.getParameter("searchCountry");
	String beginDate = request.getParameter("beginDate");
	String endDate = request.getParameter("endDate");
	String searchPatry = request.getParameter("searchPatry");
	String searchCustomer = request.getParameter("searchCustomer");
	String searchShop = request.getParameter("searchShop");
	String searchModel = request.getParameter("searchModel");
	String searchHqModel = request.getParameter("searchHqModel");
	String searchLine = request.getParameter("searchLine");
	String achs = request.getParameter("ach");
	String soAch = request.getParameter("soAch");
	String userId = (String) request.getSession().getAttribute("loginUserId");
	
	String pageStr=request.getParameter("page");
	int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
	String rowStr=request.getParameter("rows");
	int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
	int start = (page-1)*limit;
	 Date dt=new Date();
     SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
     String  dates=matter1.format(dt);
     String lastBeginDate="";
     String lastEndDate="";
	if(beginDate==null || beginDate.equals("")){
		String [] date=dates.split("-");
		beginDate=date[0]+"-"+date[1]+"-01";
		int day=Integer.parseInt(date[1])-1;
		lastBeginDate=matter1.format(matter1.parse(date[0]+"-"+String.valueOf(day)+"-01"));
		lastEndDate=DateUtil.getLastDayOfMonth(Integer.parseInt(date[0]), day);
	}else{
		String [] date=beginDate.split("-");
		int day=Integer.parseInt(date[1])-1; 
		lastBeginDate=matter1.format(matter1.parse(date[0]+"-"+String.valueOf(day)+"-01"));
		lastEndDate=DateUtil.getLastDayOfMonth(Integer.parseInt(date[0]), day);

	}
	if(endDate==null || endDate.equals("")){
		endDate=dates;
		String [] date=dates.split("-");
		int day=Integer.parseInt(date[1])-1;
		lastEndDate=matter1.format(matter1.parse(date[0]+"-"+String.valueOf(day)+"-31"));
	}
	if(searchLine!=null && !searchLine.equals("\"\"")  && searchLine!="\"\""   &&   !searchLine.equals("")){
		searchLine=searchLine.replaceAll(",","\',\'");
		searchLine="  AND pt.product_line IN (\'"+searchLine+"\')";
	}else{
		searchLine="";
	}
	
	System.out.println("===========beginDate================"+beginDate);
	System.out.println("===========endDate================"+endDate);
	System.out.println("===========lastBeginDate================"+lastBeginDate);
	System.out.println("===========lastEndDate================"+lastEndDate);
	String TargetbeginDate="";
	String TargetendDate="";
	if(beginDate!=null && !beginDate.equals("")){
		String [] date=beginDate.split("-");
		TargetbeginDate=date[0]+"-"+date[1]+"-01";
		TargetendDate=date[0]+"-"+date[1]+"-31";
	}
	
	

	
	Map<String,Object> samMap = new HashMap<String,Object>();
	if(achs!=null && !achs.equals("")){
		samMap.put("ach", Double.parseDouble(achs));
	}
	if(soAch!=null && !soAch.equals("")){
  		samMap.put("soAch", Double.parseDouble(soAch));
  	}
	samMap.put("userId", userId);
	samMap.put("limit", limit);
	samMap.put("start", start);
	samMap.put("searchCountry", searchCountry);
	samMap.put("beginDate", beginDate);
	samMap.put("endDate", endDate);
	samMap.put("lastBeginDate", lastBeginDate);
	samMap.put("lastEndDate", lastEndDate);
	samMap.put("TargetbeginDate", TargetbeginDate);
	samMap.put("TargetendDate", TargetendDate);
	samMap.put("searchPatry", searchPatry);
	samMap.put("searchCustomer", searchCustomer);
	samMap.put("searchShop", searchShop);
	samMap.put("searchModel", searchModel);
	samMap.put("searchHqModel", searchHqModel);
	samMap.put("searchLine", searchLine);
	
	
	String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
	if(!WebPageUtil.isHAdmin()){
		if(userPartyIds!=null){
			samMap.put("conditions", "  pa.party_id in ("+userPartyIds+")");
		}
		
			
	}else{
		samMap.put("conditions", "  1=1 ");
	}
	
	JSONObject result = new JSONObject();
	response.setHeader("Content-Type", "application/json");
	System.out.println("============="+samMap);
	List<SampleTarget> sampleList = null;
	try {
		sampleList = service.selectSampleAchListByLine(samMap);
		//Integer total = service.selectSampleAchListCountByLine(samMap);
		System.out.println("================total==================="+sampleList.size());
			result.accumulate("rows", sampleList);
			result.accumulate("total", sampleList.size());
			result.accumulate("success", true);
		
		
	} catch (Exception e) {
		e.printStackTrace();
		log.error(e.getMessage(),e);
	}
	WebPageUtil.writeBack(result.toString());
}

public void exportTargetAch(){
	try {
		String title ="DATA";
		String fileName = getExportExcelName("Target Ach");
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
		
      String searchCountry=request.getParameter("searchCountry");
  	String beginDate = request.getParameter("beginDate");
  	String endDate = request.getParameter("endDate");
  	String searchPatry = request.getParameter("searchPatry");
  	String searchCustomer = request.getParameter("searchCustomer");
  	String searchShop = request.getParameter("searchShop");
  	String searchModel = request.getParameter("searchModel");
  	String searchHqModel = request.getParameter("searchHqModel");
  	String achs = request.getParameter("ach");
  	String soAch = request.getParameter("soAch");
  	String userId = (String) request.getSession().getAttribute("loginUserId");
  	String searchLine = request.getParameter("searchLine");
  	String pageStr=request.getParameter("page");
  	int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
  	String rowStr=request.getParameter("rows");
  	int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
  	int start = (page-1)*limit;
  	 Date dt=new Date();
       SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
       String  dates=matter1.format(dt);
       String lastBeginDate="";
       String lastEndDate="";
       if(beginDate==null || beginDate.equals("")){
   		String [] date=dates.split("-");
   		beginDate=date[0]+"-"+date[1]+"-01";
   		int day=Integer.parseInt(date[1])-1;
		lastBeginDate=matter1.format(matter1.parse(date[0]+"-"+String.valueOf(day)+"-01"));
   		lastEndDate=DateUtil.getLastDayOfMonth(Integer.parseInt(date[0]), day);
   	}else{
   		String [] date=beginDate.split("-");
   		int day=Integer.parseInt(date[1])-1; 
   		lastBeginDate=matter1.format(matter1.parse(date[0]+"-"+String.valueOf(day)+"-01"));
   		lastEndDate=DateUtil.getLastDayOfMonth(Integer.parseInt(date[0]), day);

   	}
   	if(endDate==null || endDate.equals("")){
   		endDate=dates;
   		String [] date=dates.split("-");
   		int day=Integer.parseInt(date[1])-1;
   		lastEndDate=matter1.format(matter1.parse(date[0]+"-"+String.valueOf(day)+"-31"));
   	}
  	if(searchLine!=null && !searchLine.equals("\"\"")  && searchLine!="\"\"" &&   !searchLine.equals("")){
  		searchLine=searchLine.replaceAll(",","\',\'");
		searchLine="  AND pt.product_line IN (\'"+searchLine+"\')";
	}else{
		searchLine="";
	}
  	System.out.println("===========beginDate================"+beginDate);
  	System.out.println("===========endDate================"+endDate);
  	String TargetbeginDate="";
  	String TargetendDate="";
  	if(beginDate!=null && !beginDate.equals("")){
  		String [] date=beginDate.split("-");
  		TargetbeginDate=date[0]+"-"+date[1]+"-01";
  		TargetendDate=date[0]+"-"+date[1]+"-31";
  	}
  	
  	Map<String,Object> samMap = new HashMap<String,Object>();
  	if(achs!=null && !achs.equals("")){
  		samMap.put("ach", Double.parseDouble(achs));
  	}
  	if(soAch!=null && !soAch.equals("")){
  		samMap.put("soAch", Double.parseDouble(soAch));
  	}
  	
  	samMap.put("userId", userId);
  	samMap.put("limit", limit);
  	samMap.put("start", start);
  	samMap.put("searchCountry", searchCountry);
  	samMap.put("beginDate", beginDate);
  	samMap.put("endDate", endDate);
  	samMap.put("lastBeginDate", lastBeginDate);
	samMap.put("lastEndDate", lastEndDate);
  	samMap.put("TargetbeginDate", TargetbeginDate);
  	samMap.put("TargetendDate", TargetendDate);
  	samMap.put("searchPatry", searchPatry);
  	samMap.put("searchCustomer", searchCustomer);
  	samMap.put("searchShop", searchShop);
  	samMap.put("searchModel", searchModel);
  	samMap.put("searchHqModel", searchHqModel);
  	samMap.put("searchLine", searchLine);
  	String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
  	if(!WebPageUtil.isHAdmin()){
  		if(userPartyIds!=null){
  			samMap.put("conditions", "  pa.party_id in ("+userPartyIds+")");
  		}
  		
  			
  	}else{
  		samMap.put("conditions", "  1=1 ");
  	}
		System.out.println("=============samMap====================="+samMap);
		//国家化列表头
		
		
		/*String[] excelHeader = {"国家","所属机构","客户","门店","系列","总部型号","分公司型号","当月上样目标","当月上样达成","当月上样完成率"
		,"累计上样目标","累计上样达成","累计上样完成率","当月SO","当月样机效率","日期"		
		};
				*/	
		String[] excelHeader = {"Country","Party","Dealer","Store","Series","HQ Model",
				"Branch Model","Sample Penetration Target","Sample Penetration Achievement",
				"Achievement Rate"
				,"Month-to-month changes of Sample Penetration","Current SO",
				"Sample Efficiency","Date "		
				};
							 		

		XSSFWorkbook workbook = service.exporTargetAch(samMap, excelHeader, title);
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



public void exportSampleTarget(){
	try {
		String title ="DATA";
		String fileName = getExportExcelName("Sample Target");
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
		
      String searchCountry=request.getParameter("searchCountry");
  	String beginDate = request.getParameter("beginDate");
  	String endDate = request.getParameter("endDate");
  	String searchPatry = request.getParameter("searchPatry");
  	String searchCustomer = request.getParameter("searchCustomer");
  	String searchShop = request.getParameter("searchShop");
  	String searchModel = request.getParameter("searchModel");
  	String searchHqModel = request.getParameter("searchHqModel");
  	String achs = request.getParameter("ach");
  	String soAch = request.getParameter("soAch");
  	String userId = (String) request.getSession().getAttribute("loginUserId");
  	String searchLine = request.getParameter("searchLine");
  	String pageStr=request.getParameter("page");
  	int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
  	String rowStr=request.getParameter("rows");
  	int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
  	int start = (page-1)*limit;
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
  	if(searchLine!=null && !searchLine.equals("\"\"")  && searchLine!="\"\""   &&   !searchLine.equals("")){
  		searchLine=searchLine.replaceAll(",","\',\'");
		searchLine="  AND pt.product_line IN (\'"+searchLine+"\')";
	}else{
		searchLine="";
	}
  	
  	String TargetbeginDate="";
  	String TargetendDate="";
  	if(beginDate!=null && !beginDate.equals("")){
  		String [] date=beginDate.split("-");
  		TargetbeginDate=date[0]+"-"+date[1]+"-01";
  		TargetendDate=date[0]+"-"+date[1]+"-31";
  	}
  	
  	Map<String,Object> samMap = new HashMap<String,Object>();
  	if(achs!=null && !achs.equals("")){
  		samMap.put("ach", Double.parseDouble(achs));
  	}
  	if(soAch!=null && !soAch.equals("")){
  		samMap.put("soAch", Double.parseDouble(soAch));
  	}
  	
  	samMap.put("userId", userId);
  	samMap.put("limit", limit);
  	samMap.put("start", start);
  	samMap.put("searchCountry", searchCountry);
  	samMap.put("beginDate", beginDate);
  	samMap.put("endDate", endDate);
  	samMap.put("TargetbeginDate", TargetbeginDate);
  	samMap.put("TargetendDate", TargetendDate);
  	samMap.put("searchPatry", searchPatry);
  	samMap.put("searchCustomer", searchCustomer);
  	samMap.put("searchShop", searchShop);
  	samMap.put("searchModel", searchModel);
  	samMap.put("searchHqModel", searchHqModel);
  	samMap.put("searchLine", searchLine);
  	String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
  	if(!WebPageUtil.isHAdmin()){
  		if(userPartyIds!=null){
  			samMap.put("conditions", "  pa.party_id in ("+userPartyIds+")");
  		}
  		
  			
  	}else{
  		samMap.put("conditions", "  1=1 ");
  	}
		System.out.println("=============samMap====================="+samMap);
		//国家化列表头
		//String[] excelHeader = {"国家","所属机构","客户","门店","系列","总部型号","分公司型号","当月上样目标","累计上样目标","首次上样时间"};
		String[] excelHeader = {"Country","Party","Dealer","Store","Series","HQ Model",
				"Branch Model","Sample Penetration Target","Sample Launch Date"};

		XSSFWorkbook workbook = service.exporSampleTarget(samMap, excelHeader, title);
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



public void selectSampleTargetSumListByLine(){
	String searchCountry=request.getParameter("searchCountry");
	String beginDate = request.getParameter("beginDate");
	String endDate = request.getParameter("endDate");
	String searchPatry = request.getParameter("searchPatry");
	String searchCustomer = request.getParameter("searchCustomer");
	String searchShop = request.getParameter("searchShop");
	String searchModel = request.getParameter("searchModel");
	String searchHqModel = request.getParameter("searchHqModel");
	String  searchLine=request.getParameter("searchLine");
	String userId = (String) request.getSession().getAttribute("loginUserId");
	
	String pageStr=request.getParameter("page");
	int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
	String rowStr=request.getParameter("rows");
	int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
	int start = (page-1)*limit;
	
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
	if(searchLine!=null && !searchLine.equals("\"\"")  && searchLine!="\"\""   &&   !searchLine.equals("")){
		searchLine=searchLine.replaceAll(",","\',\'");
		searchLine="  AND pt.product_line IN (\'"+searchLine+"\')";
	}else{
		searchLine="";
	}
	Map<String,Object> samMap = new HashMap<String,Object>();
	samMap.put("userId", userId);
	samMap.put("limit", limit);
	samMap.put("start", start);
	samMap.put("searchCountry", searchCountry);
	samMap.put("beginDate", beginDate);
	samMap.put("endDate", endDate);
	samMap.put("searchPatry", searchPatry);
	samMap.put("searchCustomer", searchCustomer);
	samMap.put("searchShop", searchShop);
	samMap.put("searchModel", searchModel);
	samMap.put("searchHqModel", searchHqModel);
	samMap.put("searchLine", searchLine);
	String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
	if(!WebPageUtil.isHAdmin()){
		if(userPartyIds!=null){
			samMap.put("conditions", "  pa.party_id in ("+userPartyIds+")");
		}
		
			
	}else{
		samMap.put("conditions", "  1=1 ");
	}
	JSONObject result = new JSONObject();
	response.setHeader("Content-Type", "application/json");
	System.out.println("============="+samMap);
	List<SampleTarget> sampleList = null;
	try {
		sampleList = service.selectSampleTargetSumListByLine(samMap);
		Integer total = service.selectSampleTargetSumListCountByLine(samMap);
		System.out.println("================total==================="+total);
			result.accumulate("rows", sampleList);
			result.accumulate("total", total);
			result.accumulate("success", true);
		
		
	} catch (Exception e) {
		e.printStackTrace();
		log.error(e.getMessage(),e);
	}
	WebPageUtil.writeBack(result.toString());
}




public void selectSampleAchSumListByLine() throws ParseException{
	
	String searchCountry=request.getParameter("searchCountry");
	String beginDate = request.getParameter("beginDate");
	String endDate = request.getParameter("endDate");
	String searchPatry = request.getParameter("searchPatry");
	String searchCustomer = request.getParameter("searchCustomer");
	String searchShop = request.getParameter("searchShop");
	String searchModel = request.getParameter("searchModel");
	String searchHqModel = request.getParameter("searchHqModel");
	String searchLine = request.getParameter("searchLine");
	String achs = request.getParameter("ach");
	String soAch = request.getParameter("soAch");
	String userId = (String) request.getSession().getAttribute("loginUserId");
	
	String pageStr=request.getParameter("page");
	int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
	String rowStr=request.getParameter("rows");
	int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
	int start = (page-1)*limit;
	 Date dt=new Date();
     SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
     String  dates=matter1.format(dt);
     String lastBeginDate="";
     String lastEndDate="";
     if(beginDate==null || beginDate.equals("")){
 		String [] date=dates.split("-");
 		beginDate=date[0]+"-"+date[1]+"-01";
 		int day=Integer.parseInt(date[1])-1;
		lastBeginDate=matter1.format(matter1.parse(date[0]+"-"+String.valueOf(day)+"-01"));
 		lastEndDate=DateUtil.getLastDayOfMonth(Integer.parseInt(date[0]), day);
 	}else{
 		String [] date=beginDate.split("-");
 		int day=Integer.parseInt(date[1])-1; 
 		lastBeginDate=matter1.format(matter1.parse(date[0]+"-"+String.valueOf(day)+"-01"));
 		lastEndDate=DateUtil.getLastDayOfMonth(Integer.parseInt(date[0]), day);

 	}
 	
	if(endDate==null || endDate.equals("")){
		endDate=dates;
		String [] date=dates.split("-");
		int day=Integer.parseInt(date[1])-1;
		lastEndDate=matter1.format(matter1.parse(date[0]+"-"+String.valueOf(day)+"-31"));
	}
	if(searchLine!=null && !searchLine.equals("\"\"")  && searchLine!="\"\""  &&   !searchLine.equals("")) {
		searchLine=searchLine.replaceAll(",","\',\'");
		searchLine="  AND pt.product_line IN (\'"+searchLine+"\')";
	}else{
		searchLine="";
	}
	System.out.println("===========beginDate================"+beginDate);
	System.out.println("===========endDate================"+endDate);
	String TargetbeginDate="";
	String TargetendDate="";
	if(beginDate!=null && !beginDate.equals("")){
		String [] date=beginDate.split("-");
		TargetbeginDate=date[0]+"-"+date[1]+"-01";
		TargetendDate=date[0]+"-"+date[1]+"-31";
	}
	
	Map<String,Object> samMap = new HashMap<String,Object>();
	if(achs!=null && !achs.equals("")){
		samMap.put("ach", Double.parseDouble(achs));
	}
	if(soAch!=null && !soAch.equals("")){
  		samMap.put("soAch", Double.parseDouble(soAch));
  	}
	samMap.put("userId", userId);
	samMap.put("limit", limit);
	samMap.put("start", start);
	samMap.put("searchCountry", searchCountry);
	samMap.put("beginDate", beginDate);
	samMap.put("endDate", endDate);
	samMap.put("lastBeginDate", lastBeginDate);
	samMap.put("lastEndDate", lastEndDate);
	samMap.put("TargetbeginDate", TargetbeginDate);
	samMap.put("TargetendDate", TargetendDate);
	samMap.put("searchPatry", searchPatry);
	samMap.put("searchCustomer", searchCustomer);
	samMap.put("searchShop", searchShop);
	samMap.put("searchModel", searchModel);
	samMap.put("searchHqModel", searchHqModel);
	samMap.put("searchLine", searchLine);
	
	
	String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
	if(!WebPageUtil.isHAdmin()){
		if(userPartyIds!=null){
			samMap.put("conditions", "  pa.party_id in ("+userPartyIds+")");
		}
		
			
	}else{
		samMap.put("conditions", "  1=1 ");
	}
	JSONObject result = new JSONObject();
	response.setHeader("Content-Type", "application/json");
	System.out.println("============="+samMap);
	List<SampleTarget> sampleList = null;
	try {
		sampleList = service.selectSampleAchSumListByLine(samMap);
		Integer total = sampleList.size();
		System.out.println("================total==================="+total);
			result.accumulate("rows", sampleList);
			result.accumulate("total", total);
			result.accumulate("success", true);
		
		
	} catch (Exception e) {
		e.printStackTrace();
		log.error(e.getMessage(),e);
	}
	WebPageUtil.writeBack(result.toString());
	
	
	
}










}
