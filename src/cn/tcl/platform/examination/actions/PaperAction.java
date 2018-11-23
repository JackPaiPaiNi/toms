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
import cn.tcl.platform.barcode.service.IBarcodeService;
import cn.tcl.platform.examination.service.IExaminationService;
import cn.tcl.platform.examination.service.IPaperService;
import cn.tcl.platform.examination.vo.Examination;
import cn.tcl.platform.examination.vo.Paper;
import cn.tcl.platform.message.service.MessageService;
import cn.tcl.platform.message.vo.Message;
import cn.tcl.platform.qrcode.QRCodeUtil;
import cn.tcl.platform.role.service.IRoleService;
import cn.tcl.platform.role.vo.Role;
import cn.tcl.platform.user.vo.UserLogin;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings("all")
public class PaperAction extends BaseAction{
	
	@Autowired(required = false)
	@Qualifier("paService")
    private IPaperService paService;
	
	@Autowired(required=false)
	@Qualifier("messageService")
	private MessageService messageService;
	
	@Autowired(required=false)
	@Qualifier("roleService")
	private IRoleService IRoleService;
	
	
	/**
	 * 页面
	 * @return
	 */
	public String loadPaperPage(){
		try {
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	/**
	 * 页面
	 * @return
	 */
	public String loadSummaryOfExaminationPage(){
		try {
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	public void selectPaperData(){
		String keyword = request.getParameter("keyword");
		String countryId = WebPageUtil.isHQRole()?request.getParameter("countryId"):WebPageUtil.getLoginedUser().getPartyId();
		String pageStr=request.getParameter("page");
		int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
		String rowStr=request.getParameter("rows");
		int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
		int start = (page-1)*limit;
		
		Map<String,Object> map = new HashMap<String,Object>();
		JSONObject result = new JSONObject();
		map.put("start", start);
		map.put("limit", limit);
		map.put("language", WebPageUtil.getLoginedUser().getUserLocaleDesc());
		map.put("countryId", WebPageUtil.getLoginedUser().getPartyId());
		map.put("isHq", "" + WebPageUtil.isHQRole());
		map.put("userLoginId", WebPageUtil.getLoginedUser().getUserLoginId());
		map.put("keyword", keyword);
		map.put("countryId", countryId);
		
		try {
			Map<String,Object> resultMap = paService.selectPaperData(map);
			
			result.accumulate("rows", JSONArray.fromObject(resultMap.get("rows")).toString());
			result.accumulate("total", resultMap.get("total"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	public void insertPaperData(){
		JSONObject result = new JSONObject();
		try {
		String categories = request.getParameter("categories");
		String mediums = request.getParameter("mediumsId");
		String smaClass = request.getParameter("smaClassId");
		String headline = request.getParameter("headline");
		String judgement = request.getParameter("judNum");
		String exChoice = request.getParameter("sinNum");
		String mulChoice = request.getParameter("muiUum");
		String testTime = request.getParameter("testTime");
		String sTime = request.getParameter("sTime");
		String eTime = request.getParameter("eTime");
		String countryId = request.getParameter("countryId");
		String createTime = request.getParameter("createTime");
		
		String examIdStr = request.getParameter("examIdStr");
		String isAutomatic = request.getParameter("isAutomatic");
		
		String allUserStr = request.getParameter("param");
//		String roleType = request.getParameter("roleTypeName");
		String roleId =UUID.randomUUID().toString().replace("-", "").toLowerCase();
//		roleId = roleType + "_" + roleId;

		Paper p = new Paper();
		p.setCategoriesId(categories);
		p.setMediumsId(mediums);
		p.setSmaClassId(smaClass);
		p.setCreateTime(createTime);
		p.setHeadline(headline);
		p.setExamIdStr(examIdStr);
		p.setIsAutomatic(isAutomatic);
		
		p.setJudNum(WebPageUtil.isStringNullAvaliable(judgement) ? Integer.parseInt(judgement) : 0);
		p.setMuiUum(WebPageUtil.isStringNullAvaliable(mulChoice) ? Integer.parseInt(mulChoice) : 0);
		p.setSinNum(WebPageUtil.isStringNullAvaliable(exChoice) ? Integer.parseInt(exChoice) : 0);
		p.setTestTime(testTime);
		p.setsTime(sTime);
		p.seteTime(eTime);
		p.setUserId(WebPageUtil.getLoginedUserId());
		p.setCountryId(WebPageUtil.getLoginedUser().getPartyId());
		paService.insertPaperData(p);
		
		//创建试题对应的二维码
		//String text="http://obctop.tclking.com/topsale/m/examination/information.jsp?pId="+p.getId();
		String text="http://obctop.tcl.com.cn/topsale/m/examination/information.jsp?pId="+p.getId();
		System.out.println(text+"=====================text============");
		//二维码存放到服务器的文件夹地址
		String url="/var/www/topsale/topsale/train/qrcode/";
//		p.setQRCode("http://localhost:8080/toms/qrcode/"+QRCodeUtil.encode(text, "", "D:/apache-tomcat-7.0.75/webapps/toms/qrcode", true));
		p.setQRCode(url.replace("/var/www/topsale/topsale/train/qrcode/", "/topsale/train/qrcode/")+QRCodeUtil.encode(text, "", url, true));
		p.setP_id(p.getId());
		p.setCodeUrl(text);
		paService.insertQRcode(p);
		
		Message msg = new Message();
		msg.setMsgType("2");
		msg.setMsgTitle(headline);
		msg.setMsgTitleUrl("<a href='javascript:void(0)' onclick=limitTest("+p.getId()+","+p.getCountryId()+")>");
		msg.setCreateBy(WebPageUtil.getLoginedUserId());
		msg.setCreateTime(new Date());
		//msg.setCreateCountryId(countryId);
		msg.setCreateCountryId(WebPageUtil.getLoginedUser().getPartyId());
		msg.setCreatePartyId(WebPageUtil.getLoginedUser().getPartyId());
		msg.setMsgRoleId(roleId);
		msg.setMsgSummary(headline);
		msg.setPaperId(String.valueOf(p.getId()));
		messageService.insertPaperMessage(msg,allUserStr);
			result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	public void updatePaperData(){
		String id = request.getParameter("id");
		String categories = request.getParameter("categories");
		String mediums = request.getParameter("mediumsId");
		String smaClass = request.getParameter("smaClassId");
		String headline = request.getParameter("headline");
		String judgement = request.getParameter("judNum");
		String exChoice = request.getParameter("sinNum");
		String mulChoice = request.getParameter("muiUum");
		String testTime = request.getParameter("testTime");
		String sTime = request.getParameter("sTime");
		String examIdStr = request.getParameter("examIdStr");
		String eTime = request.getParameter("eTime");
		String countryId = request.getParameter("countryId");
		String allUserStr = request.getParameter("param");
		String userParam = request.getParameter("userParam");
		String currentTime = request.getParameter("currentTime");
		String isOvertime = request.getParameter("isOvertime");
		
		System.out.println(allUserStr+"-----------------allUserStr");
		System.out.println(userParam+"-----------------userParam");
		Paper p = new Paper();
		p.setId(Integer.parseInt(id));
		p.setCategoriesId(categories);
		p.setMediumsId(mediums);
		p.setSmaClassId(smaClass);
		p.setHeadline(headline);
		p.setJudNum(WebPageUtil.isStringNullAvaliable(judgement) ? Integer.parseInt(judgement) : 0);
		p.setMuiUum(WebPageUtil.isStringNullAvaliable(mulChoice) ? Integer.parseInt(mulChoice) : 0);
		p.setSinNum(WebPageUtil.isStringNullAvaliable(exChoice) ? Integer.parseInt(exChoice) : 0);
		p.setTestTime(testTime);
		p.setsTime(sTime);
		p.setExamIdStr(examIdStr);
		p.seteTime(eTime);
		p.setUserId(WebPageUtil.getLoginedUserId());
		p.setCountryId(WebPageUtil.getLoginedUser().getPartyId());
		
		String roleType = request.getParameter("roleTypeName");
		String roleId =UUID.randomUUID().toString().replace("-", "").toLowerCase();
		roleId = roleType + "_" + roleId;
		System.out.println(roleId+"----------"+roleType);
		
		JSONObject result = new JSONObject();
		try {
			String msgRoleId = messageService.getMsgRoleIdByPaperId(id);
			Message msg = new Message();
			msg.setMsgRoleId(msgRoleId);
			msg.setMsgTitle(headline);
			//msg.setCreateCountryId(countryId);
			msg.setCreateCountryId(WebPageUtil.getLoginedUser().getPartyId());
			msg.setMsgSummary(headline);
			msg.setPaperId(id);
			msg.setCreateTime(new Date());
			result.accumulate("success", paService.updatePaperData(p, msg, allUserStr, userParam,currentTime));
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	public void updateEndTime(){
		String id = request.getParameter("id");
		String eTime = request.getParameter("eTime");
		
		Paper p = new Paper();
		p.setId(Integer.parseInt(id));
		p.seteTime(eTime);
		p.setUserId(WebPageUtil.getLoginedUserId());
		
		JSONObject result = new JSONObject();
		try {
			result.accumulate("success", paService.updateEndTime(p));
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	public void isSpecifiedEndTime(){
		String pId = request.getParameter("pId");
		String eTime = request.getParameter("eTime");
		
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("currentTime", eTime);
		m.put("p_id", pId);
		
		JSONObject result = new JSONObject();
		try {
			result.accumulate("success", paService.isSpecifiedEndTime(m));
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	/**
	 * 考试是否开始
	 */
	public void isSpecifiedTime(){
		String pId = request.getParameter("pId");
		String start = request.getParameter("start");
		
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("currentTime", start);
		m.put("p_id", pId);
		
		JSONObject result = new JSONObject();
		try {
			result.accumulate("success", paService.isSpecifiedTime(m));
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	public void deletePaperData(){
		String id = request.getParameter("id");
		String currentTime = request.getParameter("currentTime");
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("p_id", Integer.parseInt(id));
		map.put("currentTime", currentTime);
		JSONObject result = new JSONObject();
		try {
			result.accumulate("success", paService.deletePaperData(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	public void selectTopicTypeCount(){
		
		String type = request.getParameter("type");
		String categoriesId = request.getParameter("categoriesId");
		String mediumss = request.getParameter("mediumss");
		String smaClasss = request.getParameter("smaClasss");
		//String country = request.getParameter("countryId");
		String country = WebPageUtil.getLoginedUser().getPartyId();
		
		Paper p = new Paper();
		p.setMediumsId(mediumss);
		p.setSmaClassId(smaClasss);
		p.setCategoriesId(categoriesId);
		JSONObject result = new JSONObject();
		String rows = "";
		try {
			rows = JSONArray.fromObject(paService.selectTopicTypeCount(type,country,p)).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(rows);
	};
	
	public void selectTopicByPaperId(){
		String id = request.getParameter("id");
		JSONObject result = new JSONObject();
		String rows = "";
		try {
			rows = JSONArray.fromObject(paService.selectTopicByPaperId(id)).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(rows);
	};
	
	public void selectExaminationSituation(){
		String keyword = request.getParameter("keyword");
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
		
		
		JSONObject result = new JSONObject();
		
		try {
			String partyIdsByUserId = WebPageUtil.loadPartyIdsByUserId();
			if(WebPageUtil.isHQRole()){
				if(WebPageUtil.isStringNullAvaliable(country)){
					conditions += " and ul.`PARTY_ID` = "+ country +" ";
				}else if(WebPageUtil.isStringNullAvaliable(region)){
					if("999".equals(region)){
						conditions += " and ul.`PARTY_ID` = "+ region +" ";
					}else if("2".equals(region)){
						conditions += " and ul.`PARTY_ID` in (SELECT p.`PARTY_ID` FROM `party` p WHERE p.`PARENT_PARTY_ID` in (select pa.PARTY_ID from party pa where pa.PARENT_PARTY_ID= "+ region  +") ) ";
					}else{
						conditions += " and ul.`PARTY_ID` in (SELECT p.`PARTY_ID` FROM `party` p WHERE p.`PARENT_PARTY_ID` = "+ region  +" ) ";
					}
				}
				if(WebPageUtil.isHAdmin()){
					conditions +="  and 1=1";
				}else{
					conditions +=" and ul.`PARTY_ID` in (select distinct tt.country_id from party tt where tt.party_id in ("+partyIdsByUserId+"))";
				}

			}else{
				conditions += " and ul.`PARTY_ID` = "+ WebPageUtil.getLoginedUser().getPartyId() +" ";
			}
			
			if(WebPageUtil.isStringNullAvaliable(startdate)){
				conditions += " and og.`sub_time` >= '"+ startdate +"' ";
			}
			if(WebPageUtil.isStringNullAvaliable(enddate)){
				conditions += " and og.`sub_time` <= '"+ enddate +"' ";
			}
			conditions = WebPageUtil.isStringNullAvaliable(conditions)?conditions : " and 1=1 ";
			
			Map<String,Object> resultMap = paService.selectExaminationSituation(conditions, start, limit);
			
			result.accumulate("rows", JSONArray.fromObject(resultMap.get("rows")).toString());
			result.accumulate("total", resultMap.get("total"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	public void exportExamTestExcel(){
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
//					conditions += " and oep.`country_id` = "+ country +" ";
					conditions += " and ul.PARTY_ID =  "+ country +" ";
				}else if(WebPageUtil.isStringNullAvaliable(region)){
					if("999".equals(region)){
						conditions += " and ul.`PARTY_ID` = "+ region +" ";
					}else{
						conditions += " and ul.`PARTY_ID` in (SELECT p.`PARTY_ID` FROM `party` p WHERE p.`PARENT_PARTY_ID` = "+ region  +" ) ";
					}
				}
			}else{
				conditions += " and ul.PARTY_ID = "+ WebPageUtil.getLoginedUser().getPartyId() +" ";
			}
			
			if(WebPageUtil.isStringNullAvaliable(startdate)){
				conditions += " and og.`sub_time` >= '"+ startdate +"' ";
			}

			if(WebPageUtil.isStringNullAvaliable(enddate)){
				conditions += " and og.`sub_time` <= '"+ enddate +"' ";
			}
			conditions = WebPageUtil.isStringNullAvaliable(conditions)?conditions : " and 1=1 ";
			
	  	
		
		
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		
			//国家化列表头
			String[] excelHeader = {getText("excel.paper.form.country"),
					getText("excel.paper.form.userId"),getText("excel.paper.form.userName"),
					getText("excel.paper.form.wechat"),getText("excel.paper.form.roleName"),
					getText("excel.paper.form.workNum"),getText("excel.paper.form.subTime"),
					getText("excel.paper.form.title"),getText("excel.paper.form.score"),
					getText("excel.paper.form.ptn"),getText("excel.paper.form.anof"),
					getText("excel.paper.form.percent")};
			
			HSSFWorkbook workbook = paService.exporSale(conditions,excelHeader,title,start,limit);
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
	
	
	public void selectExamByTypeAndCountry(){
		String categories = request.getParameter("categories");
		String mediums = request.getParameter("mediumsId");
		String smaClass = request.getParameter("smaClassId");
		String countryId = request.getParameter("countryId");
		
		String conditions = "";
		if(WebPageUtil.isStringNullAvaliable(categories)){
			conditions += " and oqb.`categories` = CAST("+ categories +" as SIGNED) ";
		}
		if(WebPageUtil.isStringNullAvaliable(mediums)){
			conditions += " and oqb.`medium` = CAST("+ mediums +" as SIGNED) ";
		}
		if(WebPageUtil.isStringNullAvaliable(smaClass)){
			conditions += " and oqb.`sma_class` = CAST("+ smaClass +" as SIGNED) ";
		}
		
		countryId = WebPageUtil.getLoginedUser().getPartyId();
		conditions = WebPageUtil.isStringNullAvaliable(conditions)?conditions : " and 1=1 ";
		JSONObject result = new JSONObject();
		
		try {
			List<Examination> ex = paService.selectExamByTypeAndCountry(countryId,conditions);
			result.accumulate("rows", JSONArray.fromObject(ex).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	public void selectExamSelectedInfo(){
		String categories = request.getParameter("categoriesId");
		String mediums = request.getParameter("mediumsId");
		String smaClass = request.getParameter("smaClassId");
		String paperId = request.getParameter("paperId");
		String countryId = request.getParameter("countryId");
		
		String conditions = "";
		if(WebPageUtil.isStringNullAvaliable(categories)){
			conditions += " and oqb.`categories` = CAST("+ categories +" as SIGNED) ";
		}
		if(WebPageUtil.isStringNullAvaliable(smaClass)){
			conditions += " and oqb.`sma_class` = CAST("+ smaClass +" as SIGNED) ";
		}
		if(WebPageUtil.isStringNullAvaliable(mediums)){
			conditions += " and oqb.`medium` = CAST("+ mediums +" as SIGNED) ";
		}
		
		conditions = WebPageUtil.isStringNullAvaliable(conditions)?conditions : " and 1=1 ";
		JSONObject result = new JSONObject();
		
		try {
			Map<String,Object> m = paService.selectExamSelectedInfo(paperId,countryId,conditions);
			result.accumulate("rowsSele", JSONArray.fromObject(m.get("rowsSele")).toString());
			result.accumulate("rowsFrus", JSONArray.fromObject(m.get("rowsFrus")).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	/**
	 * 用户是否已经考完试
	 */
	public void selectUserIsExam(){
		String userId = request.getParameter("userId");
		String paperId = request.getParameter("paperId");
		
		JSONObject result = new JSONObject();
		try {
			result.accumulate("rows", paService.selectUserIsExam(userId,paperId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	//查询俄罗斯考试记录
	public void selectRusExaminationSituation(){
		String keyword = request.getParameter("keyword");
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
				conditions += " and ul.`PARTY_ID` = "+ country +" ";
			}else if(WebPageUtil.isStringNullAvaliable(region)){
				if("999".equals(region)){
					conditions += " and ul.`PARTY_ID` = "+ region +" ";
				}else{
					conditions += " and ul.`PARTY_ID` in (SELECT p.`PARTY_ID` FROM `party` p WHERE p.`PARENT_PARTY_ID` = "+ region  +" ) ";
				}
			}
		}else{
			conditions += " and ul.`PARTY_ID` = "+ WebPageUtil.getLoginedUser().getPartyId() +" ";
		}
		
		if(WebPageUtil.isStringNullAvaliable(startdate)){
			conditions += " and og.`sub_time` >= '"+ startdate +"' ";
		}
		if(WebPageUtil.isStringNullAvaliable(enddate)){
			conditions += " and og.`sub_time` <= '"+ enddate +"' ";
		}
		conditions = WebPageUtil.isStringNullAvaliable(conditions)?conditions : " and 1=1 ";
		JSONObject result = new JSONObject();
		
		try {
			Map<String,Object> resultMap = paService.selectRusExaminationSituation(conditions, start, limit);
			
			result.accumulate("rows", JSONArray.fromObject(resultMap.get("rows")).toString());
			result.accumulate("total", resultMap.get("total"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	};
	
	//导出考试记录
	public void exportRusExamTestExcel(){
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
			
			String partyIdsByUserId = WebPageUtil.loadPartyIdsByUserId();
			if(WebPageUtil.isHQRole()){				
					if(WebPageUtil.isStringNullAvaliable(country)){
//						conditions += " and oep.`country_id` = "+ country +" ";
						conditions += " and ul.PARTY_ID =  "+ country +" ";
					}else if(WebPageUtil.isStringNullAvaliable(region)){
						if("999".equals(region)){
							conditions += " and ul.`PARTY_ID` = "+ region +" ";
						}else if("2".equals(region)){
							conditions += " and ul.`PARTY_ID` in (SELECT p.`PARTY_ID` FROM `party` p WHERE p.`PARENT_PARTY_ID` in (select pa.PARTY_ID from party pa where pa.PARENT_PARTY_ID= "+ region  +") ) ";
						}else{
							conditions += " and ul.`PARTY_ID` in (SELECT p.`PARTY_ID` FROM `party` p WHERE p.`PARENT_PARTY_ID` = "+ region  +" ) ";
						}
					}
				if(WebPageUtil.isHAdmin()){	
					conditions += " and 1=1";
				}else{
					conditions +=" and ul.`PARTY_ID` in (select distinct tt.country_id from party tt where tt.party_id in ("+partyIdsByUserId+"))";
				}
			}else{
				conditions += " and ul.PARTY_ID = "+ WebPageUtil.getLoginedUser().getPartyId() +" ";
			}
			
			if(WebPageUtil.isStringNullAvaliable(startdate)){
				conditions += " and og.`sub_time` >= '"+ startdate +"' ";
			}

			if(WebPageUtil.isStringNullAvaliable(enddate)){
				conditions += " and og.`sub_time` <= '"+ enddate +"' ";
			}
			conditions = WebPageUtil.isStringNullAvaliable(conditions)?conditions : " and 1=1 ";
			
	  	
		
		
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		
			//国家化列表头
			String[] excelHeader = {getText("excel.paper.form.country"),
					getText("excel.paper.form.userId"),getText("excel.paper.form.userName"),
					getText("excel.paper.form.wechat"),getText("excel.paper.form.customerName"),
					getText("excel.paper.form.roleName"),
					getText("excel.paper.form.workNum"),getText("excel.paper.form.subTime"),
					getText("excel.paper.form.title"),getText("excel.paper.form.score"),
					getText("excel.paper.form.ptn"),getText("excel.paper.form.anof"),
					getText("excel.paper.form.percent")};
			
			HSSFWorkbook workbook = paService.exporRusSale(conditions,excelHeader,title,start,limit);
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
}
