package cn.tcl.platform.role.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.role.vo.Role;
import cn.tcl.platform.role.vo.Role2User;
import cn.tcl.platform.role.vo.RoleDataPermission;

public interface IRoleService 
{
	/**
	 * 根据查询条件查询角色列表数据
	 * @param searchMsg 查询条件
	 * @param startRow 开始行索引
	 * @param pageSize 每页显示条数
	 * @param order 排序方式
	 * @param sort 排序字段
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectRoleGridData(String searchMsg, String startRow, String pageSize, String order, String sort) throws Exception;
	
	/**
	 * 根据条件查询角色数据
	 * @param conditions 查询条件，以and开头的sql字符串
	 * @return
	 * @throws Exception
	 */
	public List<Role> selectRole(String conditions) throws Exception;
	
	/**
	 * 插入一条分组数据
	 * @param role 角色对象
	 * @param allUserStr 分组初始所有用户
	 * @throws Exception
	 */
	public void insertRole(Role role,String allUserStr) throws Exception;
	
	/**
	 * 修改一条分组数据
	 * @param role 角色对象
	 * @param allUserStr 分组初始所有用户
	 * @throws Exception
	 */
	public void updateRole(Role role,String allUserStr) throws Exception;
	
	/**
	 * 更新角色和菜单权限的对应关系
	 * @param permissionRoleId
	 * @param checkedNodesParam
	 * @param uncheckedNodesParam
	 * @param indeterminateNodesParam
	 * @throws Exception
	 */
	public void updateRole2Permission(String permissionRoleId,String checkedNodesParam,String uncheckedNodesParam,String indeterminateNodesParam) throws Exception;
	
	/**
	 * 更新角色和机构权限的对应关系
	 * @param permissionRoleId
	 * @param checkedNodesParam
	 * @throws Exception
	 */
	public void updateRole2Party(String permissionRoleId,String checkedNodesParam) throws Exception;
	
	/**
	 * 根据当前用户的机构权限获取可选择的角色列表
	 * @return
	 * @throws Exception
	 */
	public List<Role> selectRoleForSelect() throws Exception;
	
	/**
	 * 根据条件查询角色的数据权限数据
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public List<RoleDataPermission> selectRoleDataPermission(String conditions) throws Exception;
	
	/**
	 * 根据条件查询用户和角色对应关系数据
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public List<Role2User> selectRole2User(String conditions) throws Exception;
	
	/**
	 * 查询角色名是否重复
	 * @return
	 * @throws Exception
	 */
	public List<Role> searchRoleNameSame(String roleName) throws Exception;
	
	//根据区域获取角色
	public List<Role> selectRoleByPartyId(String partyId) throws Exception;
	
	/**
	 * 根据角色ID软删除该角色
	 * @param roleId
	 * @throws Exception
	 */
	public void deleteRole(String roleId) throws Exception;
	
	public Role getRoleName(String roleId) throws Exception;
}
