package cn.tcl.platform.user.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.role.vo.Role2User;
import cn.tcl.platform.user.vo.LoginHistory;
import cn.tcl.platform.user.vo.UserLevel;
import cn.tcl.platform.user.vo.UserLogin;


public interface IUserLoginService 
{
	/**
	 * 查询用户登录信息
	 * @param conditions 
	 * @return
	 * @throws Exception
	 */
	public List<UserLogin> selectUserLoginInfo(String userId,String pass,boolean account) throws Exception;
	
	/**
	 * 鏍规嵁鏉′欢鏌ヨ鐢ㄦ埛
	 * @param conditions 鏌ヨ鏉′欢锛屼互and寮�澶寸殑sql瀛楃涓�
	 * @return
	 * @throws Exception
	 */
	public List<UserLogin> selectUserLogin(String conditions) throws Exception;
	
	/**
	 * 鏂板鐢ㄦ埛
	 * @param ul UserLogin瀵硅薄
	 * @param ruList 鐢ㄦ埛瑙掕壊瀵瑰簲鍏崇郴
	 * @throws Exception
	 */
	public void insertUserLogin(UserLogin ul,List<Role2User> ruList) throws Exception;
	
	/**
	 * 鏍规嵁鏌ヨ鏉′欢鏌ヨ鐢ㄦ埛鍒楄〃鏁版嵁
	 * @param searchMsg 鏌ヨ鏉′欢
	 * @param startRow 寮�濮嬭绱㈠紩
	 * @param pageSize 姣忛〉鏄剧ず鏉℃暟
	 * @param order 鎺掑簭鏂瑰紡
	 * @param sort 鎺掑簭瀛楁
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> selectUserLoginGridData(String searchMsg,String startRow,String pageSize,String order,String sort,String selectQuertyPartyId) throws Exception;
	
	/**
	 * 鏍规嵁userId淇敼鐢ㄦ埛
	 * @param ul UserLogin瀵硅薄
	 * @param ruList 鐢ㄦ埛瑙掕壊瀵瑰簲鍏崇郴
	 * @return
	 * @throws Exception
	 */
	public int updateUserLoginById(UserLogin ul,List<Role2User> ruList) throws Exception;
	
	public void deleteUserLogin(UserLogin ul) throws Exception;
	
	/**
	 * 鏍规嵁userLoginId 淇敼鐢ㄦ埛鐨勫瘑鐮�
	 * @param ul userLogind 瀵硅薄 
	 * @throws Exception
	 */
	public void updateUserPassword(UserLogin ul) throws Exception;
	
	/**
	 * 涓虹粍琛ㄥ崟鎻愪緵鎵�閫夋嫨鐨勭敤鎴锋暟鎹�
	 * @param conditions:鏌ヨ鏉′欢
	 * @param roleId:瑙掕壊ID
	 * @return
	 * @throws Exception
	 */
	public List<UserLogin> selectUserLoginForRole(String conditions,String roleId) throws Exception;
	
	/**
	 * 瀵煎叆鐢ㄦ埛鍔熻兘-娴嬭瘯瀵煎叆
	 * @param ul
	 * @throws Exception
	 */
	public void importUser(List<UserLogin> ulList) throws Exception;
	
	/**
	 * 淇濆瓨鐧诲綍璁板綍
	 * @param lh
	 * @throws Exception
	 */
	public void insertLoginHistory(LoginHistory lh) throws Exception;
	
	//鍙栧綋鍓嶇敤鎴锋墍灞炴満鏋勪笅鐨勬墍鏈夐攢鍞憳,涓氬姟鍛�,鐫ｅ鍛�
	public List<UserLogin> selectSalerListData(String conditions,int type,String shopId) throws Exception;
	
	
	public List<UserLogin> selectSalerList(String conditions,int type,String shopId) throws Exception;
	/**
	 * 鑾峰彇鐢ㄦ埛鏁版嵁鏉冮檺鏁版嵁
	 * @param permissionType 鏁版嵁鏉冮檺绫诲瀷
	 * @param userLoginId 褰撳墠鐢ㄦ埛ID
	 * @return
	 * @throws Exception
	 */
	public List<String> selectDataPermissionValues(String permissionType,String userLoginId) throws Exception;
	
	//鏌ヨ鐢ㄦ埛
	public List<UserLogin> getUserLoginList(String shopId) throws Exception;
	
	
	//修改微信号
	public void updateUserLoginTableId(Map<String,String> map) throws Exception;
	public void updateUserRoleTableId(Map<String,String> map) throws Exception;
	public void updateUserSalerTableId(Map<String,String> map) throws Exception;
	public Integer selectUserLoginId(String newUserLoginId) throws Exception;
	
	
	
	public List<String> selectUserParty( String userId) throws Exception;
	
	public List<UserLogin> selectParty(String countryId) throws Exception;
	
	public String readExcel(File file, String fileName) throws IOException;

	public String read2007Excel(File file) throws IOException;
	
	public List<UserLevel> selectUserLevel() throws Exception;
	
	//添加门店时，根据国家添加加载用户对应关系
	public List<UserLogin> selectSalerListDataBycondition(String conditions,int type,String shopId) throws Exception;
}
