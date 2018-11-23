package cn.tcl.platform.permission.service;

import java.util.List;

import cn.tcl.common.TreeNode;
import cn.tcl.platform.permission.vo.Permission;

public interface IPermissionService 
{
	/**
	 * 查询机构节点，根据父节点ID查询下一层所有节点信息
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<TreeNode> selectPermissionByPId(String parentId) throws Exception;
	
	/**
	 * 根据主键查询一条记录
	 * @param permissionId
	 * @return
	 * @throws Exception
	 */
	public Permission selectPermissionById(String permissionId) throws Exception;
	
	/**
	 * 删除节点及其下所有层次的子节点
	 * @param permissionId
	 * @throws Exception
	 */
	public void deletePermissionById(String permissionId) throws Exception;
	
	/**
	 * 插入一个节点
	 * @param p
	 * @return 返回错误信息
	 * @throws Exception
	 */
	public String insertPermission(Permission p) throws Exception;
	
	/**
	 * 修改一条节点信息
	 * @param p
	 * @throws Exception
	 */
	public void updatePermission(Permission p) throws Exception;
	
	/**
	 * 根据角色ID获取角色对应的权限树数据
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public List<TreeNode> selectRolePermission(String roleId) throws Exception;
	
	/**
	 * 获取当前登录用户的所有菜单数据
	 * @return
	 * @throws Exception
	 */
	public List<TreeNode> selectPermissionByUserId(String userLoginId) throws Exception;
}
