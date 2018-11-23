package cn.tcl.platform.examination.actions;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.examination.service.IGradeService;
import cn.tcl.platform.examination.vo.Examination;
import cn.tcl.platform.examination.vo.Paper;
import cn.tcl.platform.examination.vo.Statistics;
import cn.tcl.platform.message.vo.Message;
import cn.tcl.platform.qrcode.QRCodeUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings("all")
public class GradeAction extends BaseAction{
	
	@Autowired(required = false)
	@Qualifier("grService")
    private IGradeService grService;
	
	/**
	 * 考试(扫码登录)统计页面
	 * @return
	 */
	public String loadEemporaryExamPage(){
		try {
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	/**
	 * 查看扫码考试数据
	 */
	public void queryTempGreadData(){
		String pageStr=request.getParameter("page");
		int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
		String rowStr=request.getParameter("rows");
		int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
		int start = (page-1)*limit;
		
		String levelOne = request.getParameter("levelOneTypeId");
		String levelTwo = request.getParameter("levelTwoTypeId");
		String levelThree = request.getParameter("levelThreeTypeId");
		String country = request.getParameter("country");
		String region = request.getParameter("region");
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		
		Map<String,Object> map = new HashMap<String,Object>();
		JSONObject result = new JSONObject();
		map.put("start", start);
		map.put("limit", limit);
		map.put("condition", getqueryGreadCondition(levelOne, levelTwo, levelThree, country, region, startdate, enddate));
		
		try {
			Map<String,Object> resultMap = grService.queryTempGreadData(map);
			result.accumulate("rows", JSONArray.fromObject(resultMap.get("rows")).toString());
			result.accumulate("total", resultMap.get("total"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	/**
	 * 获取临时考试条件
	 * @return
	 */
	public String getqueryGreadCondition(String levelOne,String levelTwo,String levelThree
			,String country,String region,String startdate,String enddate){
		String conditions = "";
		if(WebPageUtil.isStringNullAvaliable(levelOne)){
			conditions += " and oep.`categories` = CAST("+ levelOne +" as SIGNED) ";
		}
		if(WebPageUtil.isStringNullAvaliable(levelTwo)){
			conditions += " and oep.`medium` = CAST("+ levelTwo +" as SIGNED) ";
		}
		
		if(WebPageUtil.isStringNullAvaliable(levelThree)){
			conditions += " and oep.`sma_class` = CAST("+ levelThree +" as SIGNED) ";
		}
		
		if(WebPageUtil.isHQRole()){
			if(WebPageUtil.isStringNullAvaliable(country)){
				conditions += " and tg.`user_country` = "+ country +" ";
			}else if(WebPageUtil.isStringNullAvaliable(region)){
				if("999".equals(region)){
					conditions += " and tg.`user_country` = "+ region +" ";
				}else{
					conditions += " and tg.`user_country` in (SELECT p.`PARTY_ID` FROM `party` p WHERE p.`PARENT_PARTY_ID` = "+ region  +" ) ";
				}
			}
		}else{
			conditions += " and tg.`user_country` = "+ WebPageUtil.getLoginedUser().getPartyId() +" ";
		}
		
		if(WebPageUtil.isStringNullAvaliable(startdate)){
			conditions += " and tg.`sub_time` >= '"+ startdate +"' ";
		}
		if(WebPageUtil.isStringNullAvaliable(enddate)){
			conditions += " and tg.`sub_time` <= '"+ enddate +"' ";
		}
		return WebPageUtil.isStringNullAvaliable(conditions)?conditions : " and 1=1 ";
	};
	
	/**
	 * 扫码考试数据导出
	 */
	public void exportTempGreadExcel(){
		try {
			String title = getText("excel.paper.form");
			String fileName = getExportExcelName(title);
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
	      	
	      	String pageStr=request.getParameter("page");
			int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
			String rowStr=request.getParameter("rows");
			int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
			int start = (page-1)*limit;
	      
	      	String levelOne = request.getParameter("levelOneTypeId");
			String levelTwo = request.getParameter("levelTwoTypeId");
			String levelThree = request.getParameter("levelThreeTypeId");
			String country = request.getParameter("country");
			String region = request.getParameter("region");
			String startdate = request.getParameter("startdate");
			String enddate = request.getParameter("enddate");
			
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		
			//国家化列表头
			String[] excelHeader = {getText("excel.paper.form.userName"),
					getText("excel.paper.form.country"),
					getText("excel.paper.form.region"),
					getText("excel.paper.form.office"),
					getText("excel.paper.form.title"),
					getText("excel.paper.form.score"),
					getText("excel.paper.form.subTime")};
			
			HSSFWorkbook workbook = grService.exporSale(getqueryGreadCondition(levelOne, levelTwo, levelThree, 
					country, region, startdate, enddate)
					,excelHeader,title,start,limit);
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
}
