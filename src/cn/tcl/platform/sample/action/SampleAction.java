package cn.tcl.platform.sample.action;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.sample.service.ISampleService;
import cn.tcl.platform.sample.vo.Sample;
import net.sf.json.JSONObject;

public class SampleAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	
	@Autowired(required = false)
	@Qualifier("sampleService")
	private ISampleService service;
	
	public String loadEquipmentPage(){
		try {
			return "success";
		} catch (Exception e) {
			return "error";
		}
	}
	
	public void exportSamplesExcel(){
			
		String searchDate = request.getParameter("searchDate");
		String searchPatry = request.getParameter("searchPatry");
		String searchCustomer = request.getParameter("searchCustomer");
		String searchShop = request.getParameter("searchShop");
		String searchModel = request.getParameter("searchModel");
		String searchHqModel = request.getParameter("searchHqModel");
	
		String userId = (String) request.getSession().getAttribute("loginUserId");
		String classes = "2";
		
		String pageStr=request.getParameter("page");
		int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
		String rowStr=request.getParameter("rows");
		int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
		int start = (page-1)*limit;
		
		
		Integer claInt = classes == ""?0:Integer.parseInt(classes);
		
		Map<String,Object> samMap = new HashMap<String,Object>();
		samMap.put("classes", claInt);
		samMap.put("userId", userId);
		samMap.put("limit", limit);
		samMap.put("start", start);
		
		samMap.put("searchDate", searchDate);
		samMap.put("searchPatry", searchPatry);
		samMap.put("searchCustomer", searchCustomer);
		samMap.put("searchShop", searchShop);
		samMap.put("searchModel", searchModel);
		samMap.put("searchHqModel", searchHqModel);
		
		try {
			String title = getText("samples.export.file.name");
			String fileName = getExportExcelName(title);
			final String userAgent = request.getHeader("USER-AGENT");
	      if (null != userAgent){    
                if (-1 != userAgent.indexOf("Firefox")) {//Firefox    
                	fileName = new String(fileName.getBytes(), "ISO8859-1");    
                }else if (-1 != userAgent.indexOf("Chrome")) {//Chrome    
                	fileName = new String(fileName.getBytes(), "ISO8859-1");    
                } else {//IE7+    
                	fileName = URLEncoder.encode(fileName, "UTF-8");    
                	fileName = StringUtils.replace(fileName, "+", "%20");//鏇挎崲绌烘牸    
                }    
            } else {    
            	fileName = fileName;    
            } 
			
			//权限
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += " data_party_id in ("+userPartyIds+")";
				}
				else
				{
					conditions += " 1=2 ";
				}
			}
			else
			{
				conditions += " 1=1 ";
			}
			
			samMap.put("conditions", conditions);
			
			//国际化列表头
			String[] excelHeader = {getText("samples.export.th.party"),getText("samples.export.th.customer"),
					getText("samples.export.th.shop"),getText("samples.export.th.hqModel"),
					getText("samples.export.th.bModel"),getText("samples.export.th.quantity"),
					getText("samples.export.th.userName"),getText("samples.export.th.date")};
			
			HSSFWorkbook workbook = service.exporSamples(conditions,excelHeader,title,samMap);
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
			
			excelName = name + sdf.format(d)+".xls";
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		return excelName;
	}
	
	
	public void selectSample(){
		
		String searchDate = request.getParameter("searchDate");
		String searchPatry = request.getParameter("searchPatry");
		String searchCustomer = request.getParameter("searchCustomer");
		String searchShop = request.getParameter("searchShop");
		String searchModel = request.getParameter("searchModel");
		String searchHqModel = request.getParameter("searchHqModel");
	
		String userId = (String) request.getSession().getAttribute("loginUserId");
		String classes = request.getParameter("classes");
		
		String pageStr=request.getParameter("page");
		int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
		String rowStr=request.getParameter("rows");
		int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
		int start = (page-1)*limit;
		
		
		Integer claInt = classes == ""?0:Integer.parseInt(classes);
		
		Map<String,Object> samMap = new HashMap<String,Object>();
		samMap.put("classes", claInt);
		samMap.put("userId", userId);
		samMap.put("limit", limit);
		samMap.put("start", start);
		
		samMap.put("searchDate", searchDate);
		samMap.put("searchPatry", searchPatry);
		samMap.put("searchCustomer", searchCustomer);
		samMap.put("searchShop", searchShop);
		samMap.put("searchModel", searchModel);
		samMap.put("searchHqModel", searchHqModel);
		
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		
		List<Sample> sampleList = null;
		try {
			sampleList = service.selectSampleList(samMap);
			Integer total = service.selectSampleListCount(samMap);
			result.accumulate("rows", sampleList);
			result.accumulate("total", total);
			result.accumulate("success", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
	public void selectShopId(){
		/*String userId = (String) request.getSession().getAttribute("loginUserId");*/
		String userId = "yingwh";
		JSONObject result = new JSONObject();
		List<Sample> sampleList = null;
		try {
			sampleList = service.selectShopId(userId);
			result.accumulate("result", sampleList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
}
