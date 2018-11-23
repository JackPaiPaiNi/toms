package cn.tcl.platform.message.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tcl.platform.message.dao.MessageDao;
import cn.tcl.platform.message.service.MessageService;
import cn.tcl.platform.message.vo.Message;
import cn.tcl.platform.message.vo.SendMessageUser;
import cn.tcl.platform.user.vo.UserLogin;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service("messageService")
public class MessageServiceImpl implements MessageService{
	@Autowired
	private MessageDao messageDao;
	
	
	@Override
	public Map<String, Object> SelectMessageList(int start, int limit,
			String order, String sort, String conditions) throws Exception {
		List<Message> list = messageDao.SelectMessageList(start, limit, order, sort, conditions);
		int count = messageDao.countMessage(start, limit, conditions);
		Map<String, Object> map= new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total",count);
		return map;
	}


	@Override
	public List<UserLogin> selectAllUser(String conditions,String msgId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("conditions", conditions);
		map.put("msgId", msgId);
		return messageDao.selectAllUser(map);
	}




	@Override
	public void deleteMessage(Message msg) throws Exception {
		messageDao.deleteMessage(msg);
	}


	@Override
	@Transactional
	public void insertMessage(Message msg,String allUserStr) throws Exception {
		messageDao.insertMessage(msg);
		String userLoginId="";
//		String [] allUser = allUserStr.split(";");
		//把allUserStr转换为json数组
		JSONArray jsonArray = JSONArray.fromObject(allUserStr);  
//		String [] allUser = new String[jsonArray.size()];
//		int userLoginIndex = 0;
//		@SuppressWarnings("unchecked")
//		Iterator <Object> it = jsonArray.iterator();
//		while (it.hasNext()) {
//			JSONObject ob = (JSONObject) it.next();
//			allUser[userLoginIndex] = (String) ob.get("userId");
//			userLoginIndex ++;
//		}
		List<SendMessageUser> list = new ArrayList<SendMessageUser>();
//		for (int i = 0; i < allUser.length; i++) {
		for(int i=0;i<jsonArray.size();i++){
//			String userLoginId=allUser[i];
			JSONObject obj = (JSONObject) jsonArray.get(i);
			userLoginId = obj.getString("userId");
			
			SendMessageUser smu = new SendMessageUser();
			smu.setUserLoginId(userLoginId);
			smu.setMsgRoleId(msg.getMsgRoleId());
			smu.setMsgState("0");
			list.add(smu);
		}
		if(userLoginId!=null && !userLoginId.equals("")){
			messageDao.insertSendMessageUser(list);
		}
		
	}


	@Override
	public List<UserLogin> getUserByRoleName(String searchStr,String countryId) throws Exception {
		return messageDao.getUserByRoleName(searchStr,countryId);
	}


	@Override
	public void insertPaperMessage(Message msg, String allUserStr)
			throws Exception {
		messageDao.insertPaperMessage(msg);
		
		
		JSONArray jsonArray = JSONArray.fromObject(allUserStr);  
		String [] allUser = new String[jsonArray.size()];
		int userLoginIndex = 0;
		String userLoginId="";
		@SuppressWarnings("unchecked")
		Iterator <Object> it = jsonArray.iterator();
		while (it.hasNext()) {
			JSONObject ob = (JSONObject) it.next();
			allUser[userLoginIndex] = (String) ob.get("userId");
			userLoginIndex ++;
		}
		
		List<SendMessageUser> list = new ArrayList<SendMessageUser>();
		for (int i = 0; i < allUser.length; i++) {
		     userLoginId=allUser[i];
			SendMessageUser smu = new SendMessageUser();
			smu.setUserLoginId(userLoginId);
			smu.setMsgRoleId(msg.getMsgRoleId());
			smu.setMsgState("0");
			list.add(smu);
		}
		if(userLoginId!=null && !userLoginId.equals("")){
			messageDao.insertSendMessageUser(list);
		}
	}


	@Override
	public List<SendMessageUser> selectUserByCondition(String roleId) throws Exception {
			return messageDao.selectUserByCondition(roleId);
	}


//	@Override
//	public Message getMsgRoleId(String courseId) throws Exception {
//		return messageDao.getMsgRoleId(courseId);
//	}


//	@Override
//	public void deleteMessageByUser(Message msgRoleId) throws Exception {
//		messageDao.deleteMessageByUser(msgRoleId);
//	}
	@Override
	public void deleteMessageByUser(String msgRoleId) throws Exception {
		messageDao.deleteMessageByUser(msgRoleId);
	}
	

//	@Override
//	public Message getMsgRoleIdByPaperId(Integer paperId) throws Exception {
//		return messageDao.getMsgRoleIdByPaperId(paperId);
//	}


	@Override
	public String getMsgRoleIdByCourseId(String courseId) throws Exception {
		return messageDao.getMsgRoleIdByCourseId(courseId);
	}


	@Override
	public String getMsgRoleIdByPaperId(String paperId) throws Exception {
		return messageDao.getMsgRoleIdByPaperId(paperId);
	}


	@Override
	public void insertSummaryMessage(Message msg, String allUserStr)
			throws Exception {
		messageDao.insertSummaryMessage(msg);
		String userLoginId="";
		JSONArray jsonArray = JSONArray.fromObject(allUserStr); 
		List<SendMessageUser> list = new ArrayList<SendMessageUser>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = (JSONObject)jsonArray.get(i);
			 userLoginId = obj.getString("userId");
			 SendMessageUser smu = new SendMessageUser();
			 	smu.setUserLoginId(userLoginId);
				smu.setMsgRoleId(msg.getMsgRoleId());
				smu.setMsgState("0");
				list.add(smu);
		}
		if(userLoginId!=null && !userLoginId.equals("")){
			messageDao.insertSendMessageUser(list);
		}	
		
//		String [] allUser = allUserStr.split(";");
//		for (int i = 0; i < allUser.length; i++) {
//			String userLoginId=allUser[i];
//			SendMessageUser smu = new SendMessageUser();
//			smu.setUserLoginId(userLoginId);
//			smu.setMsgRoleId(msg.getMsgRoleId());
//			smu.setMsgState("0");
//			List<SendMessageUser> list = new ArrayList<SendMessageUser>();
//			if(userLoginId!=null && !userLoginId.equals("")){
//				messageDao.insertSendMessageUser(list);
//			}
//		}
	}


	@Override
	public String getMsgRoleBySummaryId(String summaryId) throws Exception {
		return messageDao.getMsgRoleBySummaryId(summaryId);
	}


	@Override
	public void deleteReadCourse(String courseId) throws Exception {
		messageDao.deleteReadCourse(courseId);
	}


	@Override
	public int getReadByCourseId(String courseId) throws Exception {
		return messageDao.getReadByCourseId(courseId);
	}


	@Override
	public void deleteReadSummary(String summaryId) throws Exception {
			messageDao.deleteReadSummary(summaryId);
		
	}


	@Override
	public int getReadBySummaryId(String summaryId) throws Exception {
		return messageDao.getReadBySummaryId(summaryId);
	}


	@Override
	public List<Map<String, Object>> userIsReadCourse(String courseId, String userLoginId)
			throws Exception {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		JSONArray jsonArray = JSONArray.fromObject(userLoginId);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = (JSONObject)jsonArray.get(i);
			int count = messageDao.userIsReadCourse(courseId, obj.getString("userLoginId"));
			if(count>0){
				Map<String,Object> map= new HashMap<String,Object>();
				map.put("userName", obj.getString("userName"));
				list.add(map);
			}
		}
		return list;
	}


	@Override
	public List<Map<String, Object>> userIsReadSummary(String summaryId,
			String userLoginId) throws Exception {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		JSONArray jsonArray = JSONArray.fromObject(userLoginId);
		for(int i = 0;i < jsonArray.size();i++){
			JSONObject obj = (JSONObject) jsonArray.get(i);
			int count = messageDao.userIsReadSummary(summaryId, obj.getString("userLoginId"));
			if(count > 0){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("userName", obj.getString("userName"));
				list.add(map);
			}
		}
		return list;
	}


	@Override
	public List<UserLogin> selectAllUsers() throws Exception {
		return messageDao.selectAllUsers();
	}


	@Override
	public List<UserLogin> getUserByCondition(String roleName)
			throws Exception {
		
		return messageDao.getUserByCondition(roleName);
	}


	@Override
	public List<UserLogin> getUserByRegion(String regionId) throws Exception {
		return messageDao.getUserByRegion(regionId);
	}


	@Override
	public List<UserLogin> getUserByconditions(String regionId, String roleName)
			throws Exception {
		return messageDao.getUserByconditions(regionId, roleName);
	}





}
