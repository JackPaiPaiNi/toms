package cn.tcl.platform.permission.actions;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.TreeNode;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.permission.service.IPermissionService;
import cn.tcl.platform.permission.vo.Permission;

public class PermissionAction  extends BaseAction
{
	@Autowired(required = false)
	@Qualifier("permissionService")
	private IPermissionService permissionService;
	
	/**
	 * 加载机构树页面
	 * @return
	 */
	public String loadPermissionTreePage()
	{
		return SUCCESS;
	}
	
	/**
	 * 根据父节点加载所有子节点
	 */
	public void loadPermissionTreeNodeData()
	{
		JSONArray result = new JSONArray();
		try
		{
			String parentId = request.getParameter("node")==null?"":request.getParameter("node").trim();
			List<TreeNode> tnList = permissionService.selectPermissionByPId(parentId);
			result = JSONArray.fromObject(tnList);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 获取待修改的节点信息
	 */
	public void loadUpdatePermissionData()
	{
		JSONObject result = new JSONObject();
		try
		{
			String permissionId = request.getParameter("permissionId");
			Permission p = permissionService.selectPermissionById(permissionId);
			result = JSONObject.fromObject(p);
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
	 * 删除节点及其下所有层次的子节点
	 */
	public void deletePermission()
	{
		JSONObject result = new JSONObject();
		try
		{
			String permissionId = request.getParameter("permissionId");
			permissionService.deletePermissionById(permissionId);
			result.accumulate("success", "true");
			result.accumulate("msg", "success");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", "true");
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 插入一条节点信息
	 */
	public void insertPermission()
	{
		JSONObject result = new JSONObject();
		try
		{
			String permissionId = request.getParameter("permissionId");
			String parentPermissionId = request.getParameter("parentPermissionId");
			String permissionName = request.getParameter("permissionName");
			String isMenu = request.getParameter("isMenu");
			String comments = request.getParameter("comments");
			String permissionCode = request.getParameter("permissionCode");
			String permissionUrl = request.getParameter("permissionUrl");
			String buttonId = request.getParameter("buttonId");
			String labelKey = request.getParameter("labelKey");
			
			Permission p = new Permission();
			p.setPermissionId(permissionId);
			p.setParentPermissionId(parentPermissionId);
			p.setPermissionName(permissionName);
			p.setIsMenu(Integer.valueOf(isMenu));
			p.setComments(comments);
			p.setPermissionCode(permissionCode);
			p.setPermissionUrl(permissionUrl);
			p.setButtonId(buttonId);
			p.setLabelKey(labelKey);
			
			String errorMsgCode = permissionService.insertPermission(p);
			if("exist".equals(errorMsgCode))
			{
				result.accumulate("success", "true");
				result.accumulate("msg", getText("permission.form.save.exist"));
			}
			else
			{
				result.accumulate("success", "true");
				result.accumulate("msg", "success");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", "true");
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 修改一条节点信息
	 */
	public void updatePermission()
	{
		JSONObject result = new JSONObject();
		try
		{
			String permissionId = request.getParameter("permissionId");
			String parentPermissionId = request.getParameter("parentPermissionId");
			String permissionName = request.getParameter("permissionName");
			String isMenu = request.getParameter("isMenu");
			String comments = request.getParameter("comments");
			String permissionCode = request.getParameter("permissionCode");
			String permissionUrl = request.getParameter("permissionUrl");
			String buttonId = request.getParameter("buttonId");
			String labelKey = request.getParameter("labelKey");
			
			Permission p = new Permission();
			p.setPermissionId(permissionId);
			p.setPermissionName(parentPermissionId);
			p.setPermissionName(permissionName);
			p.setIsMenu(Integer.valueOf(isMenu));
			p.setComments(comments);
			p.setPermissionCode(permissionCode);
			p.setPermissionUrl(permissionUrl);
			p.setButtonId(buttonId);
			p.setLabelKey(labelKey);
			
			permissionService.updatePermission(p);
			
			result.accumulate("success", "true");
			result.accumulate("msg", "success");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", "true");
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 加载角色对应的权限树数据
	 */
	public void selectRolePermission()
	{
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try
		{
			String roleId = request.getParameter("roleId")==null?"":request.getParameter("roleId").trim();
			List<TreeNode> tnList = permissionService.selectRolePermission(roleId);
			result = JSONArray.fromObject(tnList);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
}
