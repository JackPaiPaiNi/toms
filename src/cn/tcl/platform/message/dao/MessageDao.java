package cn.tcl.platform.message.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.message.vo.Message;
import cn.tcl.platform.message.vo.SendMessageUser;
import cn.tcl.platform.user.vo.UserLogin;
/**
 * 
 * @author fzl
 *
 */
public interface MessageDao {
	
	//获取所有消息
	public List<Message> SelectMessageList(
			@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="order") String order,
			@Param(value="sort") String sort,
			@Param(value="conditions") String conditions) throws Exception;
	
	//统计所有消息
	public int countMessage(
			@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="conditions") String conditions) throws Exception;
	
	//创建课件信息
	public void insertMessage(Message msg) throws Exception;
	//创建考试消息
	public void insertPaperMessage(Message msg) throws Exception;
	
	//删除消息
	public void deleteMessage(Message msg) throws Exception;
	
	//获取所有的user
	public List<UserLogin> selectAllUser(Map<String, Object> map) throws Exception;
	
	//根据roleName获取用户
	public List<UserLogin> getUserByRoleName(@Param("searchStr")String searchStr,@Param("countryId")String countryId) throws Exception;
	
	//添加发送消息和人员的对应关系
	public void insertSendMessageUser(List<SendMessageUser> list) throws Exception;
	
	//根据条件查询userLoginId
	public List<SendMessageUser> selectUserByCondition(@Param("roleId")String roleId) throws Exception;
	
	/**
	 * 删除消息关系映射未阅读的用户
	 * @param smu
	 * @throws Exception
	 */
//	public void deleteMessageByUser(Message msgRoleId) throws Exception;
	public void deleteMessageByUser(String msgRoleId) throws Exception;
	
	/**
	 * 根据考试id修改消息
	 */
	public void updateMsgRoleId(Message msg) throws Exception;
	
	/**
	 * 根据培训课程修改消息
	 * @param msg
	 * @throws Exception
	 */
	public void updateMsgRoleIdByCourseId(Message msg) throws Exception;
	
	/**
	 * 根据总结summary修改消息
	 * @param msg
	 * @throws Exception
	 */
	public void updateMsgBySummaryId(Message msg) throws Exception;
	
	/**
	 * 根据用户名查询用户id
	 *  
	 */
	public String selectUserLoginId(String userName) throws Exception;
	
	/**
	 * 根据课件id改修跳转地址
	 * @param courseId
	 * @throws Exception
	 */
	public void deleteMsgUrl(Message courseId) throws Exception;
	
	/**
	 * 根据培训课件courseId，查询msg_role_id
	 * 获取msgRoleId
	 * @return
	 * @throws Exception
	 */
//	public Message getMsgRoleId(String courseId) throws Exception;
	
	/**
	 * 根据考试paperId,查询msg_role_id
	 * @param integer
	 * @return
	 * @throws Exception
	 */
//	public Message getMsgRoleIdByPaperId(Integer integer) throws Exception;
	
	/**
	 * 根据courseId，获取msgRoleId
	 * @param courseId
	 * @return
	 * @throws Exception
	 */
	public String getMsgRoleIdByCourseId(String courseId) throws Exception;
	
	/**
	 * 编辑时根据关系映射msgRoleId/userLoginId,获取userLoginId
	 * @param userLoginId
	 * @param msgRoleId
	 * @return
	 * @throws Exception
	 */
	public String getUserLoginByMsgRoleId(@Param("userLoginId")String userLoginId,@Param("msgRoleId")String msgRoleId) throws Exception;
	
	/**
	 * 根据paperId，获取msgRoleId
	 * @param paperId
	 * @return
	 * @throws Exception
	 */
	public String getMsgRoleIdByPaperId(String paperId) throws Exception;
	
	//创建学习与总结
	public void insertSummaryMessage(Message msg) throws Exception;
	
	/**
	 * 根据summary_id，获取msgRoleId
	 * @param summaryId
	 * @return
	 * @throws Exception
	 */
	public String getMsgRoleBySummaryId(String summaryId) throws Exception;
	
	/**
	 * 删除已阅读的课件
	 * @param courseId
	 * @throws Exception
	 */
	public void deleteReadCourse(String courseId) throws Exception;
	
	/**
	 * 获取阅读人的数量
	 * @param courseId
	 * @return
	 * @throws Exception
	 */
	public int getReadByCourseId(String courseId) throws Exception;
	
	/**
	 * 根据summaryId删除计划与总结
	 * @param summaryId
	 * @throws Exception
	 */
	public void deleteMsgUrlBySummaryId(Message summaryId) throws Exception;
	
	/**
	 * 删除已阅读的计划与总结
	 * @param summaryId
	 * @throws Exception
	 */
	public void deleteReadSummary(String summaryId) throws Exception;
	
	
	/**
	 * 获取阅读人的数量
	 * @param summaryId
	 * @return
	 * @throws Exception
	 */
	public int getReadBySummaryId(String summaryId) throws Exception;
	
	/**
	 * 删除消息关系映射表已阅读的用户
	 * @param msgRoleId
	 * @throws Exception
	 */
	public void deleteMessageByUser2(@Param("userLoginId")String userLoginId,@Param("msgRoleId")String msgRoleId) throws Exception;
	
	/**
	 * 查看阅读该课件的阅读数
	 * @param userId
	 * @param courseId
	 * @return
	 * @throws Exception
	 */
	public Integer userIsReadCourse(@Param("courseId")String courseId,@Param("userLoginId")String userLoginId) throws Exception;
	
	/**
	 * 获取阅读计划与总结的阅读数
	 * @param summaryId
	 * @param userLoginId
	 * @return
	 * @throws Exception
	 */
	public Integer userIsReadSummary(@Param("summaryId")String summaryId,@Param("userLoginId")String userLoginId) throws Exception;
	
	/**
	 * 查询obc所有用户列表
	 * @return
	 * @throws Exception
	 */
	public List<UserLogin> selectAllUsers() throws Exception;
	
	/**
	 * 根据业务区域获取用户列表
	 * @param regionId
	 * @return
	 * @throws Exception
	 */
	public List<UserLogin> getUserByRegion(@Param("regionId")String regionId) throws Exception;
	
	/**
	 * 选择业务区域，国家为all,筛选角色类型
	 * @return
	 */
	public List<UserLogin> getUserByconditions(@Param("regionId")String regionId,@Param("roleName")String roleName) throws Exception;
	
	/**
	 * 根据国家获取用户列表
	 * @param countryId
	 * @return
	 * @throws Exception
	 */
	public List<UserLogin> getUserByCondition(@Param("roleName")String roleName) throws Exception;
}
