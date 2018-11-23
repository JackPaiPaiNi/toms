package cn.tcl.platform.party.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.TreeNode;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.party.service.IPartyService;
import cn.tcl.platform.party.vo.Party;

public class PartyAction  extends BaseAction
{
	@Autowired(required = false)
	@Qualifier("partyService")
	private IPartyService partyService;
	
	/**
	 * 加载机构树页面
	 * @return
	 */
	public String loadPartyTreePage()
	{
		return SUCCESS;
	}
	
	/**
	 * 根据父节点加载所有子节点
	 */
	public void loadPartyTreeNodeData()
	{
		JSONArray result = new JSONArray();
		try
		{
			String parentId = request.getParameter("node")==null?"":request.getParameter("node").trim();
			List<TreeNode> tnList = partyService.selectPartyByPId(parentId);
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
	public void loadUpdatePartyData()
	{
		JSONObject result = new JSONObject();
		try
		{
			String partyId = request.getParameter("partyId");
			Party p = partyService.selectPartyById(partyId);
			if("".equals(p.getCountryId()) || p.getCountryId() == null){
				
				p.setIsCountry("0");
			}else{
				if(p.getPartyId().equals(p.getCountryId())){
					p.setIsCountry("1");
				}else{
					p.setIsCountry("0");
				}
			}
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
	public void deleteParty()
	{
		JSONObject result = new JSONObject();
		try
		{
			String partyId = request.getParameter("partyId");
			partyService.deletePartyById(partyId);
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
	public void insertParty()
	{
		JSONObject result = new JSONObject();
		try
		{
			String partyId = request.getParameter("partyId");
			String partyName = request.getParameter("partyName");
			String federalTaxId = request.getParameter("federalTaxId");
			String status = request.getParameter("status");
			String groupNameAbbr = request.getParameter("groupNameAbbr");
			String partyIdLayer = request.getParameter("partyIdLayer");
			String parentPartyId = request.getParameter("parentPartyId");
			String comments = request.getParameter("comments");
			String isCountry = request.getParameter("isCountry");
			
			Party p = new Party();
			p.setPartyId(partyId);
			p.setPartyName(partyName);
			p.setFederalTaxId(federalTaxId);
			p.setStatus(status.charAt(0));
			p.setGroupNameAbbr(groupNameAbbr);
			p.setPartyIdLayer(partyIdLayer);
			p.setParentPartyId(parentPartyId);
			p.setComments(comments);

			String _country = "";
			if("0".equals(isCountry)){
				_country = partyService.getCountryByParty(parentPartyId);
				if("".equals(_country)){
					p.setCountryId(null);
				}else{
					p.setCountryId(_country);
				}
			}
			String errorMsgCode = partyService.insertParty(p);
			if("1".equals(isCountry)){
				p.setCountryId(p.getPartyId());
				partyService.updateParty(p);
			} 
			if("exist".equals(errorMsgCode))
			{
				result.accumulate("success", "true");
				result.accumulate("msg", getText("party.form.save.exist"));
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
	public void updateParty()
	{
		JSONObject result = new JSONObject();
		try
		{
			String partyId = request.getParameter("partyId");
			String partyName = request.getParameter("partyName");
			String federalTaxId = request.getParameter("federalTaxId");
			String status = request.getParameter("status");
			String groupNameAbbr = request.getParameter("groupNameAbbr");
			String partyIdLayer = request.getParameter("partyIdLayer");
			String parentPartyId = request.getParameter("parentPartyId");
			String comments = request.getParameter("comments");
			String isCountry = request.getParameter("isCountry");
			
			Party p = new Party();
			p.setPartyId(partyId);
			p.setPartyName(partyName);
			p.setFederalTaxId(federalTaxId);
			p.setStatus(status.charAt(0));
			p.setGroupNameAbbr(groupNameAbbr);
			p.setPartyIdLayer(partyIdLayer);
			p.setParentPartyId(parentPartyId);
			p.setComments(comments);
			
			String _country = "";
			if("0".equals(isCountry)){
				_country = partyService.getCountryByParty(parentPartyId);
				if("".equals(_country)){
					p.setCountryId(null);
				}else{
					p.setCountryId(_country);
				}
			}
			if("1".equals(isCountry)){
				p.setCountryId(partyId);
			}
			partyService.updateParty(p);
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
	 * 加载角色的机构权限信息
	 */
	public void selectRoleParty()
	{
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try
		{
			String roleId = request.getParameter("roleId")==null?"":request.getParameter("roleId").trim();
			List<TreeNode> tnList = partyService.selectRoleParty(roleId);
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
	 * 加载用户所能查看的机构列表
	 */
	public void selectUserParty()
	{
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try
		{
			List<Party> pList = partyService.selectUserParty();
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
	 * 加载用户能查看到的所有属于国家的机构列表
	 */
	public void selectUserPartyCountry()
	{
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try
		{
			List<Party> pList = partyService.selectUserPartyCountry();
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
	 * 获取所有机构信息
	 */
	public void loadPartyTreeData(){
		response.setHeader("Content-Type", "application/json");
		JSONArray result = new JSONArray();
		try
		{
			List<Party> tnList = partyService.selectAllPartyData();
			List<Map<String, String>> mlist = new ArrayList<Map<String,String>>();
			for (Party p : tnList) {
				Map<String,String> map = new HashMap<String,String>();
				map.put("partyId", p.getPartyId());
				map.put("partyName", p.getPartyName());
				mlist.add(map);
			}
			result = JSONArray.fromObject(mlist);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 获取所有业务区域信息
	 */
	public void loadSCPartyData(){
		response.setHeader("Content-Type","application/json");
		JSONArray result=new JSONArray();
		try{
			List<Party> pList=partyService.getAllSCPartyData();
			result=JSONArray.fromObject(pList);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 根据业务区域获取国家信息
	 */
	public void loadCountryBySC()
	{
		response.setHeader("Content-Type","application/json");
		JSONArray result=new JSONArray();
		try{
			String partyId=request.getParameter("partyId");
			List<Party> pList=partyService.getPartyByPId(partyId);
			result=JSONArray.fromObject(pList);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
}
