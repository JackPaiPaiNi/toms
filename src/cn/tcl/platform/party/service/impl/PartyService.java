package cn.tcl.platform.party.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tcl.common.Contents;
import cn.tcl.common.TreeNode;
import cn.tcl.common.TreeNodeAttribute;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.party.dao.IPartyDAO;
import cn.tcl.platform.party.service.IPartyService;
import cn.tcl.platform.party.vo.Party;

@Service("partyService")
public class PartyService implements IPartyService 
{
	@Autowired
	private IPartyDAO partyDAO;

	@Override
	public List<TreeNode> selectPartyByPId(String parentId) throws Exception
	{
		List<TreeNode> resultList = new ArrayList<TreeNode>();
		List<Party> pList = partyDAO.selectPartyByPId(parentId);
		for(Party p : pList)
		{
			TreeNode tn = new TreeNode();
			tn.setId(p.getPartyId());
			tn.setText(p.getPartyName());
			tn.setState("closed");
			
			TreeNodeAttribute tna = new TreeNodeAttribute();
			tn.setAttributes(tna);
			
			resultList.add(tn);
		}
		
		return resultList;
	}

	@Override
	public Party selectPartyById(String partyId) throws Exception
	{
		return partyDAO.selectPartyById(partyId);
	}

	@Override
	@Transactional
	public void deletePartyById(String partyId) throws Exception 
	{
		partyDAO.deletePartyById(partyId,WebPageUtil.getLoginedUserId());
	}

	@Override
	@Transactional
	public String insertParty(Party p) throws Exception 
	{
		String errorMsgCode = "";
		String partyId = p.getPartyId();
		Party yp = partyDAO.selectPartyById(partyId);
		if(null!=yp)
		{
			errorMsgCode = "exist";
		}
		else
		{
			Date date = new Date();
			String userLoginId = WebPageUtil.getLoginedUserId();
			p.setCreateDate(date);
			p.setCreateBy(userLoginId);
			
			partyDAO.insertParty(p);
		}
		return errorMsgCode;
	}

	@Override
	@Transactional
	public void updateParty(Party p) throws Exception 
	{
		Date date = new Date();
		String userLoginId = WebPageUtil.getLoginedUserId();
		p.setLastModifyDate(date);
		p.setLastModifyUser(userLoginId);
		partyDAO.updatePartyById(p);
	}

	@Override
	public List<TreeNode> selectRoleParty(String roleId) throws Exception 
	{
		List<Party> pList = partyDAO.selectRoleParty(Contents.ROLE_DATA_PERMISSION_PARTY, roleId);
		String parentId = TreeNode.ROOT_NODE_PID;
		return initTreeNodeMain(pList, parentId);
	}
	
	private List<TreeNode> initTreeNodeMain(List<Party> pList,String parentId) throws Exception
	{
		List<TreeNode> rootListT = new ArrayList<TreeNode>();
		Map<String,List<Party>> temMap = new HashMap<String,List<Party>>();
		if(null!=pList)
		{
			for(Party p : pList)
			{
				String pId = p.getParentPartyId();
				List<Party> temList;
				if(temMap.containsKey(pId))
				{
					temList = temMap.get(pId);
				}
				else
				{
					temList = new ArrayList<Party>();
				}
				temList.add(p);
				temMap.put(pId, temList);
			}
		}
		//获取根节点权限对象
		String rootId = parentId;
		List<Party> rootListP = temMap.get(rootId);
		if(null!=rootListP)
		{
			for(Party p : rootListP)
			{
				TreeNode tn = new TreeNode();
				String partyId = p.getPartyId();
				tn.setId(partyId);
				tn.setText(p.getPartyName());
				tn.setState("open");
				String checkState = p.getCheckState();
				if(null!=checkState && !"".equals(checkState))
				{
					tn.setChecked(true);
				}
				List<TreeNode> children = loadTreeNodeCircle(partyId, temMap);
				tn.setChildren(children);
				rootListT.add(tn);
			}
		}
		return rootListT;
	}
	
	/**
	 * 根据父ID，所有机构MAP数据，递归组织TreeNode数据
	 * @param pId
	 * @param temMap
	 * @return
	 */
	private List<TreeNode> loadTreeNodeCircle(String pId,Map<String,List<Party>> temMap) throws Exception
	{
		List<TreeNode> result = new ArrayList<TreeNode>();
		
		List<Party> pListTemp = temMap.get(pId);
		if(null!=pListTemp)
		{
			for(Party p : pListTemp)
			{
				TreeNode tn = new TreeNode();
				String partyId = p.getPartyId();
				tn.setId(partyId);
				tn.setText(p.getPartyName());
				tn.setState("open");
				String checkState = p.getCheckState();
				if(null!=checkState && !"".equals(checkState))
				{
					tn.setChecked(true);
				}
				if(temMap.containsKey(partyId))
				{
					List<TreeNode> children = loadTreeNodeCircle(partyId, temMap);
					tn.setChildren(children);
				}
				result.add(tn);
			}
		}
		return result;
	}

	@Override
	public List<Party> selectUserParty() throws Exception 
	{
		//String conditions = " and " + WebPageUtil.buildDataPermissionSql("t.PARTY_ID",Contents.ROLE_DATA_PERMISSION_PARTY);
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		String conditions = "";
		if(!WebPageUtil.isHAdmin())
		{
			if(null!=userPartyIds && !"".equals(userPartyIds))
			{
				conditions += " and t.PARTY_ID in ("+userPartyIds+")";
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
		return partyDAO.selectUserParty(conditions);
	}

	@Override
	public List<Party> selectAllPartyData() throws Exception {
		return partyDAO.selectAllPartyData();
	}

	@Override
	public String getCountryByParty(String parentPartyId) throws Exception {
		
		//当前节点的父节点
		Party p = partyDAO.getCountPartyId(parentPartyId);
		while(p != null){
			if("".equals(p.getCountryId()) || p.getCountryId() == null){
				p = partyDAO.getCountPartyId(p.getParentPartyId());
			}else{
				break;
			}
		}
		if(p == null){
			return null;
		}else{
			String country = p.getCountryId() == null ? "" : p.getCountryId();
			return country;
		}
	}

	@Override
	public List<Party> selectUserPartyCountry() throws Exception {
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		String conditions = "";
		if(!WebPageUtil.isHAdmin())
		{
			if(null!=userPartyIds && !"".equals(userPartyIds))
			{
				conditions += " and a.PARTY_ID in ("+userPartyIds+")";
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
//		String conditions = " and " + WebPageUtil.buildDataPermissionSql("t.PARTY_ID",Contents.ROLE_DATA_PERMISSION_PARTY);
		return partyDAO.selectUserPartyCountry(conditions);
	}

	@Override
	public List<Party> getCountryByPartyId(String countryId) throws Exception {
		return partyDAO.getCountryByPartyId(countryId);
	}

	@Override
	public List<Party> getAllSCPartyData() throws Exception {
		// TODO Auto-generated method stub
		return partyDAO.getAllSCPartyData();
	}

	@Override
	public List<Party> getPartyByPId(String partyId) throws Exception {
		// TODO Auto-generated method stub
		return partyDAO.getPartyByPId(partyId);
	}
}
