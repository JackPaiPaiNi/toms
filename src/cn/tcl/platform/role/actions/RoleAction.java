package cn.tcl.platform.role.actions;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.role.service.IRoleService;
import cn.tcl.platform.role.vo.Role;
import cn.tcl.platform.user.service.IUserLoginService;
import cn.tcl.platform.user.vo.UserLogin;

public class RoleAction extends BaseAction 
{
	@Autowired(required = false)
	@Qualifier("roleService")
	private IRoleService roleService;
	@Autowired(required = false)
	@Qualifier("userLoginService")
	private IUserLoginService userLoginService;
	
	/**
	 * 加载角色页面
	 * @return
	 */
	public String loadRoleListPage()
	{
		return SUCCESS;
	}
	
	/**
	 * 加载角色列表数据
	 */
	public void loadRoleDataForGrid()
	{
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try
		{
			String sort = request.getParameter("sort");
			sort = WebPageUtil.tansBeanFiledToDbField(sort);
			String order = request.getParameter("order");
			String searchMsg = request.getParameter("searchMsg");
			String pageIndex = request.getParameter("page");
			String pageSize = request.getParameter("rows");
			String startRow = (Integer.valueOf(pageIndex)-1)*Integer.valueOf(pageSize) + "";
			
			Map<String, Object> map = roleService.selectRoleGridData(searchMsg, startRow, pageSize, order, sort);
			int total = (Integer)map.get("total");
			List<Role> list = (List<Role>)map.get("list");
			for(Role r : list)
			{
				String roleType = r.getRoleType();
				String _roleType = this.getText("role.form.title.roleType."+roleType);
				r.setRoleType(_roleType);
			}
			result.accumulate("rows", list);
			result.accumulate("total", total);
			result.accumulate("success", true);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", true);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 获得修改角色的数据
	 */
	public void loadUpdateRoleData()
	{
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try 
		{
			String roleId = request.getParameter("roleId");
			String conditions = " and a.ROLE_ID = '" + roleId + "'";
			List<Role> roleList = roleService.selectRole(conditions);
			if(roleList.size()>0)
			{
				Role role = roleList.get(0);
				role.setAddOrEdit("update");
				result = JSONObject.fromObject(role);
			}
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 插入角色
	 */
	public void insertRole()
	{
		JSONObject result = new JSONObject();
		try 
		{
			//String roleId = request.getParameter("roleId");
			String roleId =UUID.randomUUID().toString().replace("-", "").toLowerCase();
			String roleName = request.getParameter("roleName");
			String allUserStr = request.getParameter("allUserStr");
			String createBy = WebPageUtil.getLoginedUserId();
			Timestamp createDate = new Timestamp(new Date().getTime());
			
			String roleType = request.getParameter("roleType");
			roleId = roleType + "_" + roleId;
			
			String conditions = " and a.ROLE_ID = '" + roleId + "'";
			List<Role> roleList = roleService.selectRole(conditions);
			if(null==roleList || roleList.size()<=0)
			{
				Role role = new Role();
				role.setRoleId(roleId);
				role.setRoleName(roleName);
				role.setCreateBy(createBy);
				role.setCreateDate(createDate);
				
				roleService.insertRole(role,allUserStr);
				result.accumulate("success", true);
				result.accumulate("msg", "success");
			}
			else
			{
				result.accumulate("success", true);
				result.accumulate("msg", "exists");
			}
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 修改角色
	 */
	public void updateRole()
	{
		JSONObject result = new JSONObject();
		try
		{
			String roleId = request.getParameter("roleId");
			String roleName = request.getParameter("roleName");
			String allUserStr = request.getParameter("allUserStr");
			String updateBy = WebPageUtil.getLoginedUserId();
			Timestamp updateDate = new Timestamp(new Date().getTime());
			
			Role role = new Role();
			role.setRoleId(roleId);
			role.setRoleName(roleName);
			role.setUpdateBy(updateBy);
			role.setUpdateDate(updateDate);
			
			roleService.updateRole(role,allUserStr);
			result.accumulate("success", true);
			result.accumulate("msg", "success");
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 加载角色和人员对应关系
	 */
	public void loadRoleUserData()
	{
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try
		{
			String searchUser = request.getParameter("searchUser");
			String roleId = request.getParameter("roleId");
			String conditions = ("".equals(searchUser) || null==searchUser)?"":" and (a.USER_LOGIN_ID like '%"+searchUser+"%' or a.USER_NAME like '%"+searchUser+"%')";
			conditions += "and (a.LEFT_OR_RIGHT = 'right' or a.user_login_id not in (select user_login_id from user_role_mapping where user_login_id is not null))";
			if(!WebPageUtil.isHAdmin()){
				String partyId = WebPageUtil.getLoginedUser().getPartyId();
				conditions += "AND a.`USER_LOGIN_ID` IN (SELECT ul.`USER_LOGIN_ID` FROM user_login ul,party pa "
						+ "WHERE ul.`PARTY_ID` = pa.`PARTY_ID`"
						+ "	AND pa.`COUNTRY_ID` = "+ partyId +")";
			}
			List<UserLogin> ulList = userLoginService.selectUserLoginForRole(conditions, roleId);
			result.accumulate("rows", JSONArray.fromObject(ulList));
			result.accumulate("success", true);
			result.accumulate("msg", "success");
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", true);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 保存角色菜单权限信息
	 */
	public void insertRolePermission()
	{
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try
		{
			String permissionRoleId = request.getParameter("permissionRoleId");
			String checkedNodesParam = request.getParameter("checkedNodesParam");
			String uncheckedNodesParam = request.getParameter("uncheckedNodesParam");
			String indeterminateNodesParam = request.getParameter("indeterminateNodesParam");
			
			roleService.updateRole2Permission(permissionRoleId, checkedNodesParam, uncheckedNodesParam, indeterminateNodesParam);
			
			result.accumulate("success", true);
			result.accumulate("msg", "success");
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", true);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 保存角色机构权限信息
	 */
	public void insertRoleParty()
	{
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try
		{
			String permissionRoleId = request.getParameter("permissionRoleId");
			String checkedNodesParam = request.getParameter("checkedNodesParam");
			
			roleService.updateRole2Party(permissionRoleId, checkedNodesParam);
			
			result.accumulate("success", true);
			result.accumulate("msg", "success");
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", true);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 加载用户所能查看的机构列表
	 */
	public void selectRoleForSelect()
	{
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try
		{
			List<Role> pList = roleService.selectRoleForSelect();
			result = JSONArray.fromObject(pList);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	} 
	
	/**
	 * 判断 角色名是否有重名
	 */
	public void isRoleNameSame(){
		JSONObject result=new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try {
			String roleName = request.getParameter("roleName");
			List<Role> roleList = roleService.searchRoleNameSame(roleName);
			for (Role role : roleList) {
				result.put("roleName",role.getRoleName());
			}
			result.put("roleNum",roleList.size());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 删除角色
	 */
	public void deleteRole(){
		JSONObject result=new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try {
			String roleId = request.getParameter("roleId");
			roleService.deleteRole(roleId);
			result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	//根据区域获取角色
	public void selectRoleByPartyId(){
		String partyId  = request.getParameter("partyId");
		
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try
		{
			List<Role> pList = roleService.selectRoleByPartyId(partyId);
			result = JSONArray.fromObject(pList);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
}
