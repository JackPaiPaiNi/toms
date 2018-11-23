package cn.tcl.platform.permission.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.permission.vo.Permission;

public interface IPermissionDAO 
{
	/**
	 * 根据父ID找到直接下一层的子节点
	 * @param parentPermissionId
	 * @return
	 * @throws Exception
	 */
	public List<Permission> selectPermissionByPId(@Param(value="parentPermissionId") String parentPermissionId) throws Exception;
	
	/**
	 * 根据当前的主键ID，获取当前节点信息
	 * @param PermissionId
	 * @return
	 * @throws Exception
	 */
	public Permission selectPermissionById(@Param(value="permissionId") String permissionId) throws Exception;
	
	/**
	 * 插入一个节点
	 * @param Permission
	 * @throws Exception
	 */
	public void insertPermission(Permission permission) throws Exception;
	
	/**
	 * 根据主键修改一个节点
	 * @param Permission
	 * @throws Exception
	 */
	public void updatePermissionById(Permission permission) throws Exception;
	
	/**
	 * 删除当前节点及所有下面层级的子节点
	 * @param permissionId
	 * @throws Exception
	 */
	public void deletePermissionById(@Param(value="permissionId") String permissionId) throws Exception;
	
	/**
	 * 查询满足条件的所有节点
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public List<Permission> selectPermissionByConds(@Param(value="conditions") String conditions) throws Exception;
	
	/**
	 * 根据角色ID查询权限数据
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public List<Permission> selectRolePermission(@Param(value="roleId") String roleId) throws Exception;
	
	/**
	 * 获取用户的所有菜单数据
	 * @param userLoginId
	 * @return
	 * @throws Exception
	 */
	public List<Permission> selectPermissionByUserId(@Param(value="userLoginId") String userLoginId) throws Exception;
}
