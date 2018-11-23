package cn.tcl.platform.role.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.role.vo.Role;
import cn.tcl.platform.role.vo.Role2Permission;
import cn.tcl.platform.role.vo.Role2User;
import cn.tcl.platform.role.vo.RoleDataPermission;

public interface IRoleDAO 
{
	/**
	 * 根据查询条件查询角色
	 * @param conditions,比如： and .....
	 * @return
	 * @throws Exception
	 */
	public List<Role> selectRole(@Param(value="conditions") String conditions) throws Exception;
	
	/**
	 * 根据条件查询总记录条数
	 * @param conditions 查询条件
	 * @return
	 * @throws Exception
	 */
	public int selectRoleCount(@Param(value="conditions") String conditions) throws Exception;
	
	/**
	 * 分页查询角色
	 * @param conditions 查询条件
	 * @param orderBy 排序条件 
	 * @param startIndex 开始索引
	 * @param endIndex 结束索引
	 * @return
	 * @throws Exception
	 */
	public List<Role> selectRoleWithPage(@Param(value="conditions") String conditions,
										 @Param(value="orderBy") String orderBy,
										 @Param(value="startIndex") int startIndex,
										 @Param(value="endIndex") int endIndex) throws Exception;
	
	
	/**
	 * 插入角色
	 * @param role
	 * @throws Exception
	 */
	public void insertRole(Role role) throws Exception;
	
	/**
	 * 根据ID修改角色
	 * @param role
	 * @throws Exception
	 */
	public void updateRoleById(Role role) throws Exception;
	
	/**
	 * 根据角色ID删除角色和人员的对应关系
	 * @param roleId
	 * @throws Exception
	 */
	public void deleteRole2User(@Param(value="roleId") String roleId) throws Exception;
	
	/**
	 * 根据角色ID软删除该角色
	 * @param roleId
	 * @throws Exception
	 */
	public void deleteRole(@Param(value="roleId") String roleId,@Param(value="operatingUser") String operatingUser) throws Exception;
	
	/**
	 * 根据角色ID删除角色和人员的对应关系
	 * @param roleId
	 * @throws Exception
	 */
	public void deleteRole2UserByUserId(@Param(value="userLoginId") String userLoginId) throws Exception;
	
	/**
	 * 添加角色和人员的对应关系
	 * @throws Exception
	 */
	public void insertRole2User(Role2User ru) throws Exception;
	
	/**
	 * 根据角色ID删除角色和菜单权限的对应关系
	 * @param roleId
	 * @throws Exception
	 */
	public void deleteRole2Permission(@Param(value="roleId") String roleId) throws Exception;
	
	/**
	 * 添加角色和菜单权限的对应关系
	 * @throws Exception
	 */
	public void insertRole2Permission(@Param(value="rpList") List<Role2Permission> rpList) throws Exception;
	
	/**
	 * 根据角色ID和数据权限类型删除角色相应类型的数据权限
	 * @param roleId
	 * @param permissionType
	 * @throws Exception
	 */
	public void deleteRoleDataPermission(@Param(value="roleId") String roleId,@Param(value="permissionType") String permissionType) throws Exception;
	
	/**
	 * 添加角色和数据权限的对应关系
	 * @throws Exception
	 */
	public void insertRoleDataPermission(@Param(value="rdpList") List<RoleDataPermission> rdpList) throws Exception;
	
	/**
	 * 根据当前用户的机构权限获取可选择的角色列表
	 * @param permissionType
	 * @param userLoginId
	 * @return
	 * @throws Exception
	 */
	public List<Role> selectRoleForSelect(@Param(value="permissionType") String permissionType, @Param(value="userLoginId") String userLoginId, @Param(value="conditions") String conditions) throws Exception;
	
	/**
	 * 根据条件查询角色的数据权限数据
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public List<RoleDataPermission> selectRoleDataPermission(@Param(value="conditions") String conditions) throws Exception;
	
	/**
	 * 根据条件查询用户和角色对应关系数据
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public List<Role2User> selectRole2User(@Param(value="conditions") String conditions) throws Exception;
	
	/**
	 * 判断 角色名是否重名
	 * @param roleName
	 * @return
	 * @throws Exception
	 */
	public List<Role> searchRoleNameSame(@Param(value="roleName") String roleName) throws Exception;
	
	//根据区域获取角色
	public List<Role> selectRoleByPartyId(@Param(value="partyId") String partyId) throws Exception;
	
	
	public Role getRoleName(String roleId) throws Exception;
}
