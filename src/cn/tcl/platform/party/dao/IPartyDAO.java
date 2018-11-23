package cn.tcl.platform.party.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.party.vo.Party;

public interface IPartyDAO 
{
	/**
	 * 根据父ID找到直接下一层的子节点
	 * @param parentPartyId
	 * @return
	 * @throws Exception
	 */
	public List<Party> selectPartyByPId(@Param(value="parentPartyId") String parentPartyId) throws Exception;
	//根据机构名称获取某个机构-精确查找-lill20160503
	public Party getOnePartyByName(String partyName);
	/**
	 * 根据当前的主键ID，获取当前节点信息
	 * @param partyId
	 * @return
	 * @throws Exception
	 */
	public Party selectPartyById(@Param(value="partyId") String partyId) throws Exception;
	
	/**
	 * 插入一个节点
	 * @param party
	 * @throws Exception
	 */
	public void insertParty(Party party) throws Exception;
	
	/**
	 * 根据主键修改一个节点
	 * @param party
	 * @throws Exception
	 */
	public void updatePartyById(Party party) throws Exception;
	
	/**
	 * 软删除当前节点
	 * @param partyId
	 * @throws Exception
	 */
	public void deletePartyById(@Param(value="partyId") String partyId,@Param(value="operatingUser") String operatingUser) throws Exception;
	
	/**
	 * 查询满足条件的所有节点
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public List<Party> selectPartyByConds(@Param(value="conditions") String conditions) throws Exception;
	
	/**
	 * 查询角色的机构权限
	 * @param permissionType
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public List<Party> selectRoleParty(@Param(value="permissionType") String permissionType,@Param(value="roleId") String roleId) throws Exception;
	
	/**
	 * 查询用户的机构权限
	 * @param conditions 数据权限的条件
	 * @return
	 * @throws Exception
	 */
	public List<Party> selectUserParty(@Param(value="conditions") String conditions) throws Exception;
	
	/**
	 * 加载用户能查看到的所有属于国家的机构列表
	 * @param conditions 数据权限的条件
	 * @return
	 * @throws Exception
	 */
	public List<Party> selectUserPartyCountry(@Param(value="conditions") String conditions) throws Exception;
	
	/**
	 * 获取所有机构信息
	 * @return
	 * @throws Exception
	 */
	public List<Party> selectAllPartyData() throws Exception;
	
	public Party getCountPartyId(@Param(value="parentPartyId") String parentPartyId) throws Exception;
	
	public  List<Party> getCountryByPartyId(@Param(value="countryId") String parentPartyId) throws Exception;
	
	/**
	 * 获取所有的业务中心列表
	 * @return
	 * @throws Exception
	 */
	public List<Party> getAllSCPartyData() throws Exception;
	/**
	 * 根据父级机构id获取下一层次机构列表
	 * @param partyId
	 * @return
	 * @throws Exception
	 */
	public List<Party> getPartyByPId(@Param(value="partyId")String partyId) throws Exception;
}
