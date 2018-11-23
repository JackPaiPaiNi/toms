package cn.tcl.platform.permission.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tcl.common.TreeNode;
import cn.tcl.common.TreeNodeAttribute;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.permission.dao.IPermissionDAO;
import cn.tcl.platform.permission.service.IPermissionService;
import cn.tcl.platform.permission.vo.Permission;

@Service("permissionService")
public class PermissionService implements IPermissionService 
{
	@Autowired
	private IPermissionDAO permissionDAO;

	@Override
	public List<TreeNode> selectPermissionByPId(String parentId) throws Exception
	{
		List<TreeNode> resultList = new ArrayList<TreeNode>();
		List<Permission> pList = permissionDAO.selectPermissionByPId(parentId);
		for(Permission p : pList)
		{
			TreeNode tn = new TreeNode();
			tn.setId(p.getPermissionId());
			tn.setText(p.getPermissionName());
			tn.setState("closed");
			
			TreeNodeAttribute tna = new TreeNodeAttribute();
			tn.setAttributes(tna);
			
			resultList.add(tn);
		}
		
		return resultList;
	}

	@Override
	public Permission selectPermissionById(String permissionId) throws Exception
	{
		return permissionDAO.selectPermissionById(permissionId);
	}

	@Override
	@Transactional
	public void deletePermissionById(String permissionId) throws Exception 
	{
		permissionDAO.deletePermissionById(permissionId);
	}

	@Override
	@Transactional
	public String insertPermission(Permission p) throws Exception 
	{
		String errorMsgCode = "";
		String permissionId = p.getPermissionId();
		Permission yp = permissionDAO.selectPermissionById(permissionId);
		if(null!=yp)
		{
			errorMsgCode = "exist";
		}
		else
		{
			permissionDAO.insertPermission(p);
		}
		return errorMsgCode;
	}

	@Override
	@Transactional
	public void updatePermission(Permission p) throws Exception 
	{
		permissionDAO.updatePermissionById(p);
	}
	
	@Override
	public List<TreeNode> selectRolePermission(String roleId) throws Exception 
	{
		List<Permission> pList = permissionDAO.selectRolePermission(roleId);
		String parentId = TreeNode.ROOT_NODE_PID;
		return initTreeNodeMain(pList, parentId);
	}
	
	@Override
	public List<TreeNode> selectPermissionByUserId(String userLoginId) throws Exception
	{
		List<Permission> pList = permissionDAO.selectPermissionByUserId(userLoginId);
		String parentId = "999";
		return initTreeNodeMain(pList, parentId);
	}
	
	/**
	 * 递归组合权限层次结构的入口
	 * @return
	 * @throws Exception
	 */
	private List<TreeNode> initTreeNodeMain(List<Permission> pList,String parentId) throws Exception
	{
		List<TreeNode> rootListT = new ArrayList<TreeNode>();
		//根据父ID分组，组装成pId、对象集合结构数据
		Map<String,List<Permission>> temMap = new HashMap<String,List<Permission>>();
		for(int i=0;null!=pList&&i<pList.size();i++)
		{
			Permission p = pList.get(i);
			String pId = p.getParentPermissionId();
			List<Permission> temList;
			if(temMap.containsKey(pId))
			{
				temList = temMap.get(pId);
			}
			else
			{
				temList = new ArrayList<Permission>();
			}
			temList.add(p);
			temMap.put(pId, temList);
		}
		//获取根节点权限对象
		String rootId = parentId;
		List<Permission> rootListP = temMap.get(rootId);
		for(int i=0;null!=rootListP&&i<rootListP.size();i++)
		{
			Permission p = rootListP.get(i);
			TreeNode tn = new TreeNode();
			String permissionId = p.getPermissionId();
			tn.setId(permissionId);
			tn.setText(p.getPermissionName());
			tn.setState("open");
			
			TreeNodeAttribute tnr = new TreeNodeAttribute();
			tnr.setUrl(p.getPermissionUrl());
			tnr.setCode(p.getPermissionCode());
			tnr.setLabelKey(p.getLabelKey());
			tn.setAttributes(tnr);
			
			String checkState = p.getCheckState();
			if(TreeNode.CHECKED_NODES.equals(checkState))
			{
				tn.setChecked(true);
			}
			List<TreeNode> children = loadTreeNodeCircle(permissionId, temMap);
			tn.setChildren(children);
			rootListT.add(tn);
		}
		return rootListT;
	}
	
	/**
	 * 根据父ID，所有权限MAP数据，递归组织TreeNode数据
	 * @param pId
	 * @param temMap
	 * @return
	 */
	private List<TreeNode> loadTreeNodeCircle(String pId,Map<String,List<Permission>> temMap) throws Exception
	{
		List<TreeNode> result = new ArrayList<TreeNode>();
		
		List<Permission> pListTemp = temMap.get(pId);
		for(int i=0;null!=pListTemp&&i<pListTemp.size();i++)
		{
			Permission p = pListTemp.get(i);
			TreeNode tn = new TreeNode();
			String permissionId = p.getPermissionId();
			tn.setId(permissionId);
			tn.setText(p.getPermissionName());
			tn.setState("open");
			
			TreeNodeAttribute tnr = new TreeNodeAttribute();
			tnr.setUrl(p.getPermissionUrl());
			tnr.setCode(p.getPermissionCode());
			tnr.setLabelKey(p.getLabelKey());
			tn.setAttributes(tnr);
			
			String checkState = p.getCheckState();
			if(TreeNode.CHECKED_NODES.equals(checkState))
			{
				tn.setChecked(true);
			}
			if(temMap.containsKey(permissionId))
			{
				List<TreeNode> children = loadTreeNodeCircle(permissionId, temMap);
				tn.setChildren(children);
			}
			result.add(tn);
		}
		return result;
	}
}
