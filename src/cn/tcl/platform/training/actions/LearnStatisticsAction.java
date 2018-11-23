package cn.tcl.platform.training.actions;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.training.service.LearnStatisticsService;
import cn.tcl.platform.training.vo.LearnStatistics;
@SuppressWarnings("all")
public class LearnStatisticsAction extends BaseAction{
	@Autowired
	@Qualifier("LearnService")
	private LearnStatisticsService learnService;
	
	public String loadLearnPage(){
		try {
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	//查询学习计划与总结
	public void loadLearnStatistics(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		
		try {
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		String pageStr = request.getParameter("page");
		String levelOneTypeId = request.getParameter("levelOneTypeId");
		String levelTwoTypeId = request.getParameter("levelTwoTypeId");
		String levelThreeTypeId = request.getParameter("levelThreeTypeId");
		String regionId = request.getParameter("region");
		String countryId = request.getParameter("country");
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		
		
		int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
		String rowStr=request.getParameter("rows");
		int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
		int start = (page-1)*limit;
		
		
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		String conditions = "";
		if(!WebPageUtil.isHAdmin())
		{
			if(null!=userPartyIds && !"".equals(userPartyIds)){
				conditions += "ul.party_id IN (select distinct tt.country_id from party tt where tt.party_id in ("+userPartyIds+"))";
			}else{
				conditions += "1=2";
			}
		}
		else
		{
			conditions += "  1=1";
		}
		Map<String, Object> map = learnService.selectLearnList(start, limit, order, sort,conditions,levelOneTypeId,levelTwoTypeId,levelThreeTypeId,regionId,countryId,startdate,enddate);
		int total = (Integer)map.get("total");
		List<LearnStatistics> list = (List<LearnStatistics>) map.get("rows");
		JSONArray jsonArray = JSONArray.fromObject(list);
		String rows = jsonArray.toString();
		result.accumulate("rows", rows);
		result.accumulate("total", total);
		result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	//查询俄罗斯学习统计汇总
	public void loadRussiaLearnStatistics(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		
		try {
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		String pageStr = request.getParameter("page");
		String levelOneTypeId = request.getParameter("levelOneTypeId");
		String levelTwoTypeId = request.getParameter("levelTwoTypeId");
		String levelThreeTypeId = request.getParameter("levelThreeTypeId");
		String regionId = request.getParameter("region");
		String countryId = request.getParameter("country");
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		
		
		int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
		String rowStr=request.getParameter("rows");
		int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
		int start = (page-1)*limit;
		
		
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		String conditions = "";
		if(!WebPageUtil.isHAdmin())
		{
			if(null!=userPartyIds && !"".equals(userPartyIds)){
				conditions += "ul.party_id IN (select distinct tt.country_id from party tt where tt.party_id in ("+userPartyIds+"))";
			}else{
				conditions += "1=2";
			}
		}
		else
		{
			conditions += "  1=1";
		}
		Map<String, Object> map = learnService.selectRussiaLearnList(start, limit, order, sort,conditions,levelOneTypeId,levelTwoTypeId,levelThreeTypeId,regionId,countryId,startdate,enddate);
		int total = (Integer)map.get("total");
		List<LearnStatistics> list = (List<LearnStatistics>) map.get("rows");
		JSONArray jsonArray = JSONArray.fromObject(list);
		String rows = jsonArray.toString();
		result.accumulate("rows", rows);
		result.accumulate("total", total);
		result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	//导出学习统计汇总
	public String getExportExcelName(String name){
		String excelName=""; 
		try{
		Date  d = new Date(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		excelName =name + sdf.format(d) + ".xlsx";
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		return excelName;		
	}
	
	
	//导出学习摘要
	public void exportLearnExcel(){
		try{
		String levelOneTypeId = request.getParameter("levelOneTypeId");
		String levelTwoTypeId = request.getParameter("levelTwoTypeId");
		String levelThreeTypeId = request.getParameter("levelThreeTypeId");
		String regionId = request.getParameter("region");
		String countryId = request.getParameter("country");
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		System.out.println(levelOneTypeId+"levelOneTypeId");
		System.out.println(levelTwoTypeId+"levelTwoTypeId");
		System.out.println(levelThreeTypeId+"levelThreeTypeId");
		System.out.println(regionId+"regionId");
		System.out.println(countryId+"countryId");
		System.out.println(startdate+"startdate");
		System.out.println(enddate+"enddate");
		String title="Statistical Summary";
		String fileName = getExportExcelName(title);
		final  String userAgent = request.getHeader("USER-AGENT");
		if(null!=userAgent){
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
		//权限
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		String conditions = "";
		if(!WebPageUtil.isHAdmin()){
			if(null!=userPartyIds && !"".equals(userPartyIds))
			{
				conditions += "v_s.party_id IN (select distinct tt.country_id from party tt where tt.party_id in ("+userPartyIds+"))";
			}
			else
			{
				conditions += " 1=2 ";
			}
		}else{
				conditions += " 1=1 ";
		}
		
		//国际化列表表头
		String[] excelHeader={
				getText("train.learn.country"),getText("train.learn.userlogin"),
				getText("train.learn.username"),getText("train.learn.userwc"),
				getText("train.learn.rolename"),getText("train.learn.userworknum"),
				getText("train.learn.visit"),getText("train.learn.online"),
				getText("train.learn.coursetitle"),getText("train.learn.operationScore"),
				getText("train.learn.isRead"),getText("train.learn.cont"),
				getText("train.learn.percent")				
		};
		XSSFWorkbook workbook = learnService.exportLearn(conditions, excelHeader, title, levelOneTypeId, levelTwoTypeId, levelThreeTypeId, regionId, countryId, startdate, enddate);
		response.setContentType("application/vnd.ms-excel");   
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        OutputStream ouputStream = response.getOutputStream();    
        workbook.write(ouputStream);    
        ouputStream.flush();    
        ouputStream.close();  
		}catch(Exception e){
			e.printStackTrace();	
			log.error(e.getMessage(), e);
		}
	}
	
	//导出俄罗斯学习统计
	public void exportRusLearnExcel(){
		try{
		String levelOneTypeId = request.getParameter("levelOneTypeId");
		String levelTwoTypeId = request.getParameter("levelTwoTypeId");
		String levelThreeTypeId = request.getParameter("levelThreeTypeId");
		String regionId = request.getParameter("region");
		String countryId = request.getParameter("country");
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		System.out.println(levelOneTypeId+"levelOneTypeId");
		System.out.println(levelTwoTypeId+"levelTwoTypeId");
		System.out.println(levelThreeTypeId+"levelThreeTypeId");
		System.out.println(regionId+"regionId");
		System.out.println(countryId+"countryId");
		System.out.println(startdate+"startdate");
		System.out.println(enddate+"enddate");
		String title="Statistical Summary";
		String fileName = getExportExcelName(title);
		final  String userAgent = request.getHeader("USER-AGENT");
		if(null!=userAgent){
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
		//权限
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		String conditions = "";
		if(!WebPageUtil.isHAdmin()){
			if(null!=userPartyIds && !"".equals(userPartyIds))
			{
				conditions += "v_s.party_id IN (select distinct tt.country_id from party tt where tt.party_id in ("+userPartyIds+"))";
			}
			else
			{
				conditions += " 1=2 ";
			}
		}else{
				conditions += " 1=1 ";
		}
		
		//国际化列表表头
		String[] excelHeader={
				getText("train.learn.country"),getText("train.learn.userlogin"),
				getText("train.learn.username"),getText("train.learn.userwc"),
				getText("train.learn.customerName"),
				getText("train.learn.rolename"),getText("train.learn.userworknum"),
				getText("train.learn.visit"),getText("train.learn.online"),
				getText("train.learn.coursetitle"),getText("train.learn.operationScore"),
				getText("train.learn.isRead"),getText("train.learn.cont"),
				getText("train.learn.percent")				
		};
		XSSFWorkbook workbook = learnService.exportRusLearn(conditions, excelHeader, title, levelOneTypeId, levelTwoTypeId, levelThreeTypeId, regionId, countryId, startdate, enddate);
		response.setContentType("application/vnd.ms-excel");   
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        OutputStream ouputStream = response.getOutputStream();    
        workbook.write(ouputStream);    
        ouputStream.flush();    
        ouputStream.close();  
		}catch(Exception e){
			e.printStackTrace();	
			log.error(e.getMessage(), e);
		}
	}
}
