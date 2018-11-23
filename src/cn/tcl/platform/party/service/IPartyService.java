package cn.tcl.platform.party.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.common.TreeNode;
import cn.tcl.platform.party.vo.Party;

public interface IPartyService 
{
	/**
	 * 查询机构节点，根据父节点ID查询下一层所有节点信息
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<TreeNode> selectPartyByPId(String parentId) throws Exception;
	
	/**
	 * 根据主键查询一条记录
	 * @param partyId
	 * @return
	 * @throws Exception
	 */
	public Party selectPartyById(String partyId) throws Exception;
	
	/**
	 * 删除节点及其下所有层次的子节点
	 * @param partyId
	 * @throws Exception
	 */
	public void deletePartyById(String partyId) throws Exception;
	
	/**
	 * 插入一个节点
	 * @param p
	 * @return 返回错误信息
	 * @throws Exception
	 */
	public String insertParty(Party p) throws Exception;
	
	/**
	 * 修改一条节点信息
	 * @param p
	 * @throws Exception
	 */
	public void updateParty(Party p) throws Exception;
	
	/**
	 * 根据角色ID获取角色对应的机构权限数据
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public List<TreeNode> selectRoleParty(String roleId) throws Exception; 
	
	/**
	 * 查询用户的机构权限
	 * @return
	 * @throws Exception
	 */
	public List<Party> selectUserParty() throws Exception;
	
	/**
	 * 加载用户能查看到的所有属于国家的机构列表
	 * @return
	 * @throws Exception
	 */
	public List<Party> selectUserPartyCountry() throws Exception;
	
	/**
	 * 获取所有机构信息
	 */
	public List<Party> selectAllPartyData() throws Exception;
	
	/**
	 * 通过子节点 查询父节点
	 * 并且判断 所属国家
	 * @return
	 * @throws Exception
	 */
	public String getCountryByParty(String parentPartyId) throws Exception;
	public List<Party> getCountryByPartyId(String countryId) throws Exception;
	/**
	 * 获取所有的业务中心列表
	 * @return
	 * @throws Exception
	 */
	public List<Party> getAllSCPartyData() throws Exception;
	/**
	 * 根据父Id获取下一层次机构列表
	 * @param partyId
	 * @return
	 * @throws Exception
	 */
	public List<Party> getPartyByPId(String partyId) throws Exception;
}
