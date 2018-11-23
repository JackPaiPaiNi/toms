package cn.tcl.platform.message.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.message.service.MessageService;
import cn.tcl.platform.message.vo.Message;
import cn.tcl.platform.message.vo.SendMessageUser;
import cn.tcl.platform.training.vo.TrainingCourse;
import cn.tcl.platform.user.vo.UserLogin;
@SuppressWarnings("all")
public class MessageAction extends BaseAction{
	@Autowired(required=false)
	@Qualifier("messageService")
	private MessageService messageService;
	
	public String loadMessageTagPage(){
		try {
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	public void LoadMessageListData(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try {
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		String pageStr=request.getParameter("page");
		int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
		String rowStr=request.getParameter("rows");
		int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
		int start = (page-1)*limit;
		
		String conditions = "";
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		if(!WebPageUtil.isHAdmin())
		{
			if(null!=userPartyIds && !"".equals(userPartyIds)){
				conditions += "tmt.msg_country_id IN (select distinct tt.country_id from party tt where tt.party_id in ("+userPartyIds+"))";
			}else{
				conditions += " and 1=2 ";
			}
		}else{
			conditions +="1=1";
		}
			Map<String, Object> map = messageService.SelectMessageList(start, limit, order, sort, conditions);
			int total = (Integer)map.get("total");
			List<Message> list = (ArrayList<Message>) map.get("rows");
			JSONArray jsonArray = JSONArray.fromObject(list);
			String rows=jsonArray.toString();
			result.accumulate("rows", rows);
			result.accumulate("total", total);
			result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
	public void deleteMessage(){
		JSONObject result = new JSONObject();
		try {
			String id = request.getParameter("id");
			Message msg = new Message();
			msg.setId(Integer.parseInt(id));
			messageService.deleteMessage(msg);
			result.accumulate("success", true);
		} catch (Exception e) {			
			e.printStackTrace();
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void getUserAll(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		
		try {
			String msgId = request.getParameter("msgId");
			String searchUser = request.getParameter("searchUser");
			String conditions=(searchUser.equals("") || null == searchUser)?"":"";
			List<UserLogin> list = messageService.selectAllUser(conditions, msgId);
			JSONArray jsonArray = JSONArray.fromObject(list);
			String rows = jsonArray.toString();
			result.accumulate("rows", rows);
			result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", true);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void getUserByRoleName(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String roleName = request.getParameter("roleName");
		String countryId = request.getParameter("countryId");
		System.out.println(countryId+".......................");
		try {
			String searchStr="";
			if(!roleName.equals("") && roleName != null ){
//				searchStr +=" and ul.USER_LOGIN_ID  in (select urm.USER_LOGIN_ID from user_role_mapping urm where urm.role_id like concat ('%','"+roleName+"','%') )	";
				searchStr +="join (select urm.USER_LOGIN_ID from user_role_mapping urm where urm.role_id like concat ('%','"+roleName+"','%') )t";
				searchStr +="	on ul.USER_LOGIN_ID=t.user_login_id";
			}
//			String conditions="";
//			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
//			if(!WebPageUtil.isHQRole()){
//				if(null!=userPartyIds && !"".equals(userPartyIds)){
//					conditions +=" ul.party_id in (select distinct tt.country_id from party tt where tt.party_id in ("+userPartyIds+"))";
//				}else{
//					conditions+="1=2";
//				}
//			}else{
//				conditions +="1=1";
//			}
			List<UserLogin> list = messageService.getUserByRoleName(searchStr,countryId);
			String rows = JSONArray.fromObject(list).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectUserBycondition(){
		JSONObject result = new JSONObject();
		String roleId = request.getParameter("msgRoleId");
		try {
			List<SendMessageUser> list = messageService.selectUserByCondition(roleId);
			JSONArray jsonArray = JSONArray.fromObject(list);
			String rows = jsonArray.toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	//查看阅读该课件的阅读数
	public void userIsReadCourse(){
		JSONObject result = new JSONObject();
		String courseId = request.getParameter("courseId");
		String userLoginId = request.getParameter("userLoginId");
		try {
			List<Map<String, Object>> list = messageService.userIsReadCourse(courseId,userLoginId);
			String rows = JSONArray.fromObject(list).toString();
			result.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 查询阅读计划与总结的阅读数
	 */
	public void userIsReadSummary(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String summaryId = request.getParameter("summaryId");
		String userLoginId = request.getParameter("userLoginId");
		
		try {
			List<Map<String,Object>> list = messageService.userIsReadSummary(summaryId, userLoginId);
			String rows = JSONArray.fromObject(list).toString();
			result.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 列出所有OBC用户
	 */
	public void selectAllUsers(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try {
			List<UserLogin> list = messageService.selectAllUsers();
			String rows = JSONArray.fromObject(list).toString();
			result.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	//据国家获取用户列表
	public void getUserByCondition(){
		JSONObject result = new JSONObject();
		String roleName = request.getParameter("roleName");
		try {
			if(!roleName.equals("") && roleName != null ){
				List<UserLogin> list = messageService.getUserByCondition(roleName);
				String rows = JSONArray.fromObject(list).toString();
				result.put("rows", rows);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	//根据业务区域获取用户列表
	public void getUserByRegion(){
		JSONObject result = new JSONObject();
		String regionId = request.getParameter("regionId");
		
		try {
			if(regionId!=null && !regionId.equals("")){
				List<UserLogin> list = messageService.getUserByRegion(regionId);
				String rows = JSONArray.fromObject(list).toString();
				result.accumulate("rows", rows);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	//选择业务区域，国家为all,筛选角色类型
	public void getUserByCon(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String regionId = request.getParameter("regionId");
		String roleName = request.getParameter("roleName");

		try {
			List<UserLogin> list = messageService.getUserByconditions(regionId, roleName);
			String rows = JSONArray.fromObject(list).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
}


