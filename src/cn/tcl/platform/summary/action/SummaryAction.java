package cn.tcl.platform.summary.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.message.service.MessageService;
import cn.tcl.platform.message.vo.Message;
import cn.tcl.platform.summary.service.SummaryService;
import cn.tcl.platform.summary.vo.Summary;

@SuppressWarnings("all")
public class SummaryAction extends BaseAction{
	@Autowired
	@Qualifier("summaryService")
	private SummaryService summaryService;
	
	@Autowired
	@Qualifier("messageService")
	private MessageService messageService;
	
	public String loadSummaryPage(){
		try {
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;	
		}
	}
	
	public String loadSummaryListPage(){
		try {
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	
	//创建计划与总结
	public void insertSummary(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String typeId = request.getParameter("typeId");
		String summaryType = request.getParameter("messageType");
		String summaryTitle = request.getParameter("courseTitle");
		String summary = request.getParameter("courseSummary");
		String coverImgUrl = request.getParameter("coverImgUrl");
		coverImgUrl=coverImgUrl.replace("/var/www/topsale/topsale/", "/topsale/");
//		coverImgUrl=coverImgUrl.replace("\\","/").replace("d:/apache-tomcat-7.0.75/webapps/toms/cover/", "/topsale/");
		String partyId=request.getParameter("partyId");
		String regionId=request.getParameter("regionId");
		String countryId=request.getParameter("countryId");
		String summaryContent = request.getParameter("courseContent");
		summaryContent=summaryContent.replace("/var/www/topsale/topsale/","/topsale/");
		
		
		
		try {
			Summary sum = new Summary();
			sum.setTypeId(Integer.valueOf(typeId));
			sum.setRegionId(Integer.valueOf(regionId));
			sum.setCountryId(countryId);
			sum.setSummaryTitle(java.net.URLDecoder.decode(summaryTitle,"UTF-8"));
			sum.setSummary(java.net.URLDecoder.decode(summary,"UTF-8"));
			sum.setCoverUrl(coverImgUrl.replaceAll(" ", ""));
			sum.setState(0);
			sum.setSummaryContent(summaryContent);
			sum.setSummaryType(Integer.valueOf(summaryType));
			sum.setCreateDate(new Date());
			sum.setCreateBy(WebPageUtil.getLoginedUserId());
			sum.setCreaterPartyId(WebPageUtil.getLoginedUser().getPartyId());
			sum.setCreaterRoleId(WebPageUtil.getLoginedUser().getRoleId());
			summaryService.insetrSummary(sum);
			
			String allUserStr = request.getParameter("allUserStr");
//			String roleType = request.getParameter("roleTypeName");
			String roleId =UUID.randomUUID().toString().replace("-", "").toLowerCase();
//			roleId = roleType + "_" + roleId;

			
			//创建消息
			Message msg = new Message();
			
			msg.setMsgType("3");
			msg.setMsgTitle(java.net.URLDecoder.decode(summaryTitle,"UTF-8"));
			msg.setMsgComment(summaryContent);
			msg.setMsgTitleUrl("<a href=reminderSummarydetail.jsp?summaryId="+sum.getSummaryId()+">");
			msg.setCreateBy(WebPageUtil.getLoginedUserId());
			msg.setCreateTime(new Date());
			msg.setCreateCountryId(countryId);
			msg.setCreatePartyId(WebPageUtil.getLoginedUser().getPartyId());
			msg.setMsgRoleId(roleId);
			msg.setMsgSummary(java.net.URLDecoder.decode(summary,"UTF-8"));
			msg.setMsgCoverUrl(coverImgUrl.replaceAll(" ", ""));
			msg.setSummaryId(String.valueOf(sum.getSummaryId()));
			messageService.insertSummaryMessage(msg,allUserStr);
			
			result.accumulate("msg","success");	
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", true);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
	//计划与总结列表
	public void loadSummaryListData(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		String pageStr=request.getParameter("page");
		int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
		String rowStr=request.getParameter("rows");
		int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
		int start = (page-1)*limit;
		
		
		String searchStr = "1=1 ";
		String partyId = request.getParameter("partyId");
		String typeId = request.getParameter("typeId");
		String MessagetypeId = request.getParameter("MessagetypeId");
		String searchKey = request.getParameter("searchKey");
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		
		if(searchKey!=null && !searchKey.equals("")){
			searchStr+="and ts.summary_title like CONCAT('%','"+searchKey+"','%')";
		}
		
		String conditions = "";
		if(!WebPageUtil.isHQRole())
		{
			if(null!=userPartyIds && !"".equals(userPartyIds)){
				conditions += "ts.country_id IN (select distinct tt.country_id from party tt where tt.party_id in ("+userPartyIds+"))";
			}else{
				conditions += " and 1=2 ";
			}
		}
		else
		{	
			if(WebPageUtil.isSaleCenter()){
				conditions += "ts.country_id IN (select distinct tt.party_id from party tt where tt.party_id in ("+userPartyIds+"))";
			}else{
				conditions += "  1=1";
			}
		}
		
		try {
			Map<String, Object> map = summaryService.selectSummaryList(start, limit, searchStr,partyId, typeId, MessagetypeId, order, sort, conditions);
			int total = (Integer)map.get("total");
			List<Summary> list = (ArrayList<Summary>) map.get("rows");
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
	
	
	public void updateSummary(){
		JSONObject result = new JSONObject();
		String summaryId = request.getParameter("editId");
		if(summaryId!=null && summaryId!=""){
			String typeId = request.getParameter("typeId");
			String summaryType = request.getParameter("summaryType");
			String summaryTitle = request.getParameter("summaryTitle");
			String summary = request.getParameter("summary");
			String coverImgUrl = request.getParameter("coverUrl");
			System.out.println(coverImgUrl+"--------------------------");
			coverImgUrl=coverImgUrl.replace("/var/www/topsale/topsale/", "/topsale/");
//			coverImgUrl=coverImgUrl.replace("\\","/").replace("d:/apache-tomcat-7.0.75/webapps/toms/cover/", "/toms/cover/");
			String partyId=request.getParameter("partyId");
			String regionId=request.getParameter("regionId");
			System.out.println(regionId+"--------------------------------------999");
			String countryId=request.getParameter("countryId");
			String state = request.getParameter("state");
			String summaryContent = request.getParameter("courseContents");
			String allUserStr = request.getParameter("param");
		
			Summary sum;
			try {
				sum = summaryService.getSummaryById(summaryId);
				sum.setTypeId(Integer.valueOf(typeId));
				sum.setSummaryType(Integer.valueOf(summaryType));
				sum.setSummaryTitle(java.net.URLDecoder.decode(summaryTitle,"UTF-8"));
				sum.setSummary(java.net.URLDecoder.decode(summary,"UTF-8"));
				sum.setCoverUrl(coverImgUrl.replaceAll(" ", ""));
				sum.setRegionId(Integer.valueOf(regionId));
				sum.setCountryId(countryId);
				sum.setSummaryContent(summaryContent);
				sum.setState(Integer.valueOf(state));
				
				String msgRoleBySummaryId = messageService.getMsgRoleBySummaryId(summaryId);
				Message msg = new Message();
				msg.setMsgRoleId(msgRoleBySummaryId);
				msg.setSummaryId(summaryId);
				msg.setMsgTitle(java.net.URLDecoder.decode(summaryTitle,"UTF-8"));
				msg.setCreateCountryId(countryId);
				msg.setMsgSummary(java.net.URLDecoder.decode(summary,"UTF-8"));
				msg.setCreateTime(new Date());
				msg.setMsgCoverUrl(coverImgUrl.replaceAll(" ", ""));
				msg.setMsgComment(summaryContent);
				
				summaryService.updateSummary(sum,msg,allUserStr);
				
				result.accumulate("success", true);
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
	
	public void deleteSummary(){
		JSONObject result = new JSONObject();
		String summaryId = request.getParameter("summaryId");
		Summary sum = new Summary();
		sum.setSummaryId(Integer.valueOf(summaryId));
		try {
			Message msg =new Message();
			msg.setSummaryId(summaryId);
			msg.setMsgTitleUrl("<a href=../train/commentRemove.jsp?summaryId="+summaryId+">");
			
			int readNum = messageService.getReadBySummaryId(summaryId);
			if(WebPageUtil.isHAdmin()){
				summaryService.deleteSummary(sum,msg);
				messageService.deleteReadSummary(summaryId);
				result.accumulate("success", true);
			}else{
				if(readNum>0){
					result.accumulate("num", true);
				}else{
					summaryService.deleteSummary(sum,msg);
					result.accumulate("success", true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
}
