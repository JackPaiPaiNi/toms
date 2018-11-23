package cn.tcl.platform.role.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tcl.common.Contents;
import cn.tcl.common.TreeNode;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.role.dao.IRoleDAO;
import cn.tcl.platform.role.service.IRoleService;
import cn.tcl.platform.role.vo.Role;
import cn.tcl.platform.role.vo.Role2Permission;
import cn.tcl.platform.role.vo.Role2User;
import cn.tcl.platform.role.vo.RoleDataPermission;

@Service("roleService")
public class RoleService implements IRoleService 
{
	@Autowired
	private IRoleDAO roleDAO;

//	@Override
//	public Map<String, Object> selectRoleGridData(String searchMsg, String startRow, String pageSize, String order, String sort) throws Exception
//	{
//		Map<String,Object> result = new HashMap<String,Object>();
//		
//		pageSize = "".equals(pageSize)?"0":pageSize;
//		startRow = "".equals(startRow)?"0":startRow;
//		String conditions = ("".equals(searchMsg) || null==searchMsg)?"":" and (a.ROLE_ID like '%"+searchMsg+"%' or a.ROLE_NAME like '%"+searchMsg+"%')";
//		String orderBy= " order by a." + sort + " " + order;
//		
//		int startIndex = Integer.valueOf(startRow);
//		int endIndex = Integer.valueOf(pageSize);
//		
//		List<Role> roleList = roleDAO.selectRoleWithPage(conditions, orderBy, startIndex, endIndex);
//		int total = roleDAO.selectRoleCount(conditions);
//		result.put("total", total);
//		result.put("list", roleList);
//		
//		return result;
//	}
	
	@Override
	public Map<String, Object> selectRoleGridData(String searchMsg, String startRow, String pageSize, String order, String sort) throws Exception
	{
		Map<String,Object> result = new HashMap<String,Object>();
		
		pageSize = "".equals(pageSize)?"0":pageSize;
		startRow = "".equals(startRow)?"0":startRow;
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		String conditions = "";
		if(!WebPageUtil.isHAdmin())
		{
			conditions+="and urm.role_id not like '%H%'";
//			conditions += " and ul.USER_LOGIN_ID not in (select tt.USER_LOGIN_ID from user_role_mapping tt where tt.role_id like '%"+Contents.ROLE_TYPE_HADMIN+"%')";
			if(null!=userPartyIds && !"".equals(userPartyIds))
			{
//				conditions += " and ul.USER_LOGIN_ID in " +
//						"(select DISTINCT m.USER_LOGIN_ID from user_role_mapping m,role_data_permission n " +
//						"where m.ROLE_ID = n.ROLE_ID and n.PERMISSION_TYPE = '"+Contents.ROLE_DATA_PERMISSION_PARTY+"'	and n.permission_value in (" +
//						userPartyIds + "))";
				conditions+="and ul.user_login_id in "+"(select DISTINCT ul.USER_LOGIN_ID from user_login ul where ul.party_id in("+userPartyIds+"))";
			}
			else
			{
				conditions += " and 1=2 ";
			}
		}
		else
		{
			conditions += " and 1=1 ";
		}
		/*conditions +=("".equals(searchMsg) || null==searchMsg)?"":" and (a.ROLE_ID like '%"+searchMsg+"%' or a.ROLE_NAME like '%"+searchMsg+"%')";*/
		conditions +=("".equals(searchMsg) || null==searchMsg)?"":" and a.ROLE_NAME like '%"+searchMsg+"%'";
		String orderBy= " order by a." + sort + " " + order;
		
		int startIndex = Integer.valueOf(startRow);
		int endIndex = Integer.valueOf(pageSize);
		
		List<Role> roleList = roleDAO.selectRoleWithPage(conditions, orderBy, startIndex, endIndex);
		int total = roleDAO.selectRoleCount(conditions);
		result.put("total", total);
		result.put("list", roleList);
		
		return result;
	}

	@Override
	public List<Role> selectRole(String conditions) throws Exception 
	{
		return roleDAO.selectRole(conditions);
	}

	@Override
	@Transactional
	public void insertRole(Role role, String allUserStr) throws Exception 
	{
		String [] allUser = allUserStr.split(";");
		roleDAO.insertRole(role);
		for(int i=0;i<allUser.length;i++)
		{
			String userLoginId = allUser[i];
			Role2User ru = new Role2User();
			ru.setRoleId(role.getRoleId());
			ru.setUserLoginId(userLoginId);
			if(!"".equals(userLoginId) && userLoginId != null){
				roleDAO.insertRole2User(ru);
			}
		}
	}

	@Override
	@Transactional
	public void updateRole(Role role, String allUserStr) throws Exception 
	{
		String [] allUser = allUserStr.split(";");
		roleDAO.updateRoleById(role);
		roleDAO.deleteRole2User(role.getRoleId());
		for(int i=0;i<allUser.length;i++)
		{
			String userLoginId = allUser[i];
			Role2User ru = new Role2User();
			ru.setRoleId(role.getRoleId());
			ru.setUserLoginId(userLoginId);
			if(!"".equals(userLoginId) && userLoginId != null){
				roleDAO.insertRole2User(ru);
			}
		}
	}

	@Override
	@Transactional
	public void updateRole2Permission(String permissionRoleId,
			String checkedNodesParam, String uncheckedNodesParam,
			String indeterminateNodesParam) throws Exception 
	{
		List<Role2Permission> rpList = new ArrayList<Role2Permission>();
		
		if(null!=checkedNodesParam && !"".equals(checkedNodesParam))
		{
			String [] checkedNodesParamArray = checkedNodesParam.split(";");
			for(String checkedNode : checkedNodesParamArray)
			{
				Role2Permission rp = new Role2Permission();
				rp.setCheckState(TreeNode.CHECKED_NODES);
				rp.setRoleId(permissionRoleId);
				rp.setPermissionId(checkedNode);
				rpList.add(rp);
			}
		}
		if(null!=uncheckedNodesParam && !"".equals(uncheckedNodesParam))
		{
			String [] uncheckedNodesParamArray = uncheckedNodesParam.split(";");
			for(String unCheckedNode : uncheckedNodesParamArray)
			{
				Role2Permission rp = new Role2Permission();
				rp.setCheckState(TreeNode.UNCHECKED_NODES);
				rp.setRoleId(permissionRoleId);
				rp.setPermissionId(unCheckedNode);
				rpList.add(rp);
			}
		}
		if(null!=checkedNodesParam && !"".equals(checkedNodesParam))
		{
			String [] indeterminateNodesParamArray = indeterminateNodesParam.split(";");
			for(String indeterminateNodes : indeterminateNodesParamArray)
			{
				Role2Permission rp = new Role2Permission();
				rp.setCheckState(TreeNode.INDETERMINATE_NODES);
				rp.setRoleId(permissionRoleId);
				rp.setPermissionId(indeterminateNodes);
				rpList.add(rp);
			}
		}
		roleDAO.deleteRole2Permission(permissionRoleId);
		roleDAO.insertRole2Permission(rpList);
	}

	@Override
	public void updateRole2Party(String permissionRoleId, String checkedNodesParam) throws Exception 
	{
		roleDAO.deleteRoleDataPermission(permissionRoleId, Contents.ROLE_DATA_PERMISSION_PARTY);
		
		if(null!=checkedNodesParam && !"".equals(checkedNodesParam))
		{
			List<RoleDataPermission> rdpList = new ArrayList<RoleDataPermission>();
			String [] checkedNodesParamArray = checkedNodesParam.split(";");
			for(String checkedNode : checkedNodesParamArray)
			{
				RoleDataPermission rdp = new RoleDataPermission();
				rdp.setRoleId(permissionRoleId);
				rdp.setPermissionType(Contents.ROLE_DATA_PERMISSION_PARTY);
				rdp.setPermissionValue(checkedNode);
				rdpList.add(rdp);
			}
			roleDAO.insertRoleDataPermission(rdpList);
		}
	}

	@Override
	public List<Role> selectRoleForSelect() throws Exception 
	{
		//机构权限控制，用户表没有机构ID字段，需要从角色中获取机构，再从机构中获取所有角色，再获取所有角色对应的用户信息,admin不需要控制权限
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();//此句话没用，但是不能删除，方便以后查询数据权限涉及的位置
		String conditions = "";
		if(!WebPageUtil.isHAdmin())
		{
			//如果不是总部管理员，这里排除总部的角色
			conditions += " and m.ROLE_ID not like '%"+Contents.ROLE_TYPE_HADMIN+"%'";
			conditions += " and m.ROLE_ID not like '%"+Contents.ROLE_TYPE_HLEADER+"%'";
			conditions += " and m.ROLE_ID not like '%"+Contents.ROLE_TYPE_HTEAMER+"%'";
		}
		else
		{
			conditions += " and 1=1";
		}
		
		return roleDAO.selectRoleForSelect(Contents.ROLE_DATA_PERMISSION_PARTY, WebPageUtil.getLoginedUserId(), conditions);
	}
	
	@Override
	public List<RoleDataPermission> selectRoleDataPermission(String conditions) throws Exception 
	{
		return roleDAO.selectRoleDataPermission(conditions);
	}

	@Override
	public List<Role2User> selectRole2User(String conditions) throws Exception 
	{
		return roleDAO.selectRole2User(conditions);
	}

	@Override
	public List<Role> searchRoleNameSame(String roleName) throws Exception {
		return roleDAO.searchRoleNameSame(roleName);
	}
	
	@Override
	public List<Role> selectRoleByPartyId(String partyId) throws Exception {
		return roleDAO.selectRoleByPartyId(partyId);
	}

	@Override
	public void deleteRole(String roleId) throws Exception {
		roleDAO.deleteRole(roleId,WebPageUtil.getLoginedUserId());
	}

	@Override
	public Role getRoleName(String roleId) throws Exception {
		return roleDAO.getRoleName(roleId);
	}
	
}
