package cn.tcl.platform.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.shop.vo.ShopParty;
import cn.tcl.platform.user.vo.LoginHistory;
import cn.tcl.platform.user.vo.UserLevel;
import cn.tcl.platform.user.vo.UserLogin;


public interface IUserLoginDAO {
	
	/**
	 * 账号是否存在
	 * @param conditions 
	 * @return
	 * @throws Exception
	 */
	public List<UserLogin> selectLoginUserId(Map<String,Object> paramMap) throws Exception;
	
	/**
	 * 账号密码是否存在
	 * @param conditions 
	 * @return
	 * @throws Exception
	 */
	public List<UserLogin> selectLoginUserIdAndPass(Map<String,Object> paramMap) throws Exception;
	
	/**
	 * 鏍规嵁鏉′欢鏌ヨ鐢ㄦ埛
	 * @param conditions 鏌ヨ鏉′欢锛屼互and寮�澶寸殑sql瀛楃涓�
	 * @return
	 * @throws Exception
	 */
	public List<UserLogin> selectUserLogin(Map<String,Object> paramMap) throws Exception;
	
	/**
	 * 鏂板鐢ㄦ埛
	 * @param ul UserLogin瀵硅薄
	 * @throws Exception
	 */
	public void insertUserLogin(UserLogin ul) throws Exception;
	/**
	 * 删除用户
	 * @param ul
	 * @return
	 * @throws Exception
	 */
	public void deleteUserLogin(UserLogin ul) throws Exception ;
	
	/**
	 * 鏍规嵁userId淇敼鐢ㄦ埛
	 * @param ul UserLogin瀵硅薄
	 * @return
	 * @throws Exception
	 */
	public int updateUserLoginById(UserLogin ul) throws Exception;
	
	/**
	 * 鍒嗛〉鏌ヨ鐢ㄦ埛
	 * @param conditions 鏌ヨ鏉′欢
	 * @param startIndex 寮�濮嬭绱㈠紩
	 * @param endIndex 缁撴潫琛岀储寮�
	 * @param orderBy 鎺掑簭瀛楃涓�
	 * @return
	 * @throws Exception
	 */
	public List<UserLogin> selectUserLoginWithPage(Map<String,Object> paramMap) throws Exception;
	
	/**
	 * 鏍规嵁鏉′欢鏌ヨ鎬绘潯鏁�
	 * @param conditions 鏌ヨ鏉′欢
	 * @return 鎬绘潯鏁�
	 * @throws Exception
	 */
	public int selectUserLoginCount(Map<String,Object> paramMap) throws Exception;
	
	/**
	 * 涓虹粍琛ㄥ崟鎻愪緵鎵�閫夋嫨鐨勭敤鎴锋暟鎹�
	 * @param conditions:鏌ヨ鏉′欢
	 * @param roleId:瑙掕壊ID
	 * @return
	 * @throws Exception
	 */
	public List<UserLogin> selectUserLoginForRole(Map<String,Object> paramMap) throws Exception;
	
	/**
	 * 淇濆瓨鐧诲綍璁板綍
	 * @param lh
	 * @throws Exception
	 */
	public void insertLoginHistory(LoginHistory lh) throws Exception;
	
	//鍙栧綋鍓嶇敤鎴锋墍灞炴満鏋勪笅鐨勬墍鏈夐攢鍞憳淇冮攢鍛�
	public List<UserLogin> selectSalerListData(@Param(value="conditions") String conditions,
			@Param(value="type") int type,@Param(value="shopId") String shopId) throws Exception;
	
	public List<UserLogin> selectSalerList(@Param(value="conditions") String conditions,
			@Param(value="type") int type,@Param(value="shopId") String shopId) throws Exception;
	
	/**
	 * 鑾峰彇鐢ㄦ埛鏁版嵁鏉冮檺鏁版嵁
	 * @param permissionType 鏁版嵁鏉冮檺绫诲瀷
	 * @param userLoginId 褰撳墠鐢ㄦ埛ID
	 * @return
	 * @throws Exception
	 */
	public List<String> selectDataPermissionValues(@Param(value="permissionType") String permissionType,@Param(value="userLoginId") String userLoginId) throws Exception;
	
	public List<UserLogin> getUserLoginList(@Param(value="shopId") String shopId) throws Exception;
	
	
	public void updateUserLoginTableId(Map<String,String> map) throws Exception;
	public void updateUserRoleTableId(Map<String,String> map) throws Exception;
	public void updateUserSalerTableId(Map<String,String> map) throws Exception;
	public Integer selectUserLoginId(String newUserLoginId) throws Exception;
	
	
	public List<String> selectUserParty(@Param(value="userId") String userId) throws Exception;

	
	public List<UserLogin> selectParty(@Param(value="countryId") String countryId) throws Exception;
	public String selectRoleByName(@Param(value="roleName") String roleName) throws Exception;
	
	//显示所有的星级
	public List<UserLevel> selectUserLevel() throws Exception;
	//根据导入的模版星级获取星级id
	public String selectIdByValue(@Param("level")String level) throws Exception;
	
	//添加门店时，根据国家添加加载用户对应关系
	public List<UserLogin> selectSalerListDataBycondition(@Param(value="conditions") String conditions,
			@Param(value="type") int type,@Param(value="shopId") String shopId) throws Exception;
}
