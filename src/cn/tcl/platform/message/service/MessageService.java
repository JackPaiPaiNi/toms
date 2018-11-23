package cn.tcl.platform.message.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.message.vo.Message;
import cn.tcl.platform.message.vo.SendMessageUser;
import cn.tcl.platform.user.vo.UserLogin;

public interface MessageService {
	
	public Map<String, Object> SelectMessageList(int start,int  limit,String order,String sort,String conditions) throws Exception;
	
	public void insertMessage(Message msg,String allUserStr) throws Exception; 
	
	public void insertPaperMessage(Message msg,String allUserStr) throws Exception;
	
	public void deleteMessage(Message msg) throws Exception;
	
	public List<UserLogin> selectAllUser(String conditions,String msgId) throws Exception;
	
	public List<UserLogin> getUserByRoleName(String searchStr,String countryId) throws Exception;
	
	public List<SendMessageUser> selectUserByCondition(@Param("roleId")String roleId) throws Exception;
		
//	public Message getMsgRoleId(String courseId) throws Exception;
	
//	public Message getMsgRoleIdByPaperId(Integer paperId) throws Exception;
	
	public String getMsgRoleIdByCourseId(String courseId) throws Exception;
	
	public String getMsgRoleIdByPaperId(String courseId) throws Exception;
	
//	public void deleteMessageByUser(Message msgRoleId) throws Exception;
	public void deleteMessageByUser(String msgRoleId) throws Exception;
	
	public void insertSummaryMessage(Message msg,String allUserStr) throws Exception;
	
	public String getMsgRoleBySummaryId(String summaryId) throws Exception;
	
	public void deleteReadCourse(String courseId) throws Exception;
	
	public int getReadByCourseId(String courseId) throws Exception;
	
	public void deleteReadSummary(String summaryId) throws Exception;
	
	public int getReadBySummaryId(String summaryId) throws Exception;
	
	public List<Map<String,Object>> userIsReadCourse(String courseId,String userLoginId) throws Exception;
	
	public List<Map<String,Object>> userIsReadSummary(String summaryId,String userLoginId) throws Exception;

	public List<UserLogin> selectAllUsers() throws Exception;
	
	public List<UserLogin> getUserByCondition(String roleName) throws Exception;
	
	public List<UserLogin> getUserByRegion(String regionId) throws Exception;
	
	public List<UserLogin> getUserByconditions(String regionId,String roleName) throws Exception;
}
