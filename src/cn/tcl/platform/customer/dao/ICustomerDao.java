package cn.tcl.platform.customer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.customer.vo.Customer;
import cn.tcl.platform.customer.vo.CustomerUser;
import cn.tcl.platform.party.vo.Party;
import cn.tcl.platform.user.vo.UserLogin;

public interface ICustomerDao {
	public List<Customer> selectCustomers(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="countryId") String countryId,
			@Param(value="provinceId") String provinceId,
			@Param(value="order") String order,
			@Param(value="sort") String sort,
			@Param(value="conditions") String conditions,
			@Param(value="bt") String bt) throws Exception;
	public int countCustomers(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="countryId") String countryId,
			@Param(value="provinceId") String provinceId,
			@Param(value="conditions") String conditions) throws Exception;
	//保存客户
	public void saveCustomer(Customer customer) throws Exception;
	//查找某个产品
	public Customer getCustomer(String cid) throws Exception;
	//编辑修改产品
	public void editCustomer(Customer customer) throws Exception;
	//根据名称找到对应客户的维一ID
	public Customer getOneCustomerByName(String customerName) throws Exception;
	//根据用户ID获取party
	
	public void deleteCustomer(Customer customer) throws Exception;
	
	public String getCountryByName(String name) throws Exception;
	
	public String getProvinceByName(String name) throws Exception;
	
	public String getCityByName(String name) throws Exception;
	
	public String getCountyByName(String name) throws Exception;
	
	public String getTownByName(String name) throws Exception;
	
	public String getChannelByName(String name) throws Exception;
	
	public Customer getRepeatByCustomerCode(String code) throws Exception;
	
	public int getCountByChannel(String channelType) throws Exception;
	
	//国家省市县镇
	public int getCountryByNameCount(String searchName) throws Exception;
	
	public int getProvinceByNameCount(String searchName) throws Exception;
	
	public int getCityByNameCount(String searchName) throws Exception;
	
	public int getCountyByNameCount(String searchName) throws Exception;
	
	public int getTownByNameCount(String searchName) throws Exception;
	
	public List<Customer> getCustomerByName(@Param("CustomerName")String CustomerName) throws Exception;
	
	
	public List<Customer> selectCustomerName(
			@Param(value="searchStr") String searchStr,
			@Param(value="conditions") String conditions) throws Exception;
	
	public List<Party> selectParty(@Param("countryId")String countryId) throws Exception;
	
	//获取渠道业务员与督导的对应关系
	public List<UserLogin> selectCustomerSaler(@Param("conditions")String conditions,@Param("type")int type,@Param("customerId")String customerId) throws Exception;
	
	//添加业务员与督导对应关系
	public void insertSalers(@Param("customerId")Integer customerId,@Param("userLoginId")String userLoginId,@Param("salertype")int salertype) throws Exception;
	
	//删除渠道与用户的所有对应关系
	public void deleteRelation(Integer customerId) throws Exception;

	//获取渠道与人员的对应关系
	public List<CustomerUser> getCustomerUserRelations(@Param("partyId")String partyId,@Param("customerId")String customerId) throws Exception;
	
	//添加新渠道时获取渠道业务员与督导的对应关系
	public List<UserLogin> selectCustomerSalerData(@Param("conditions")String conditions,@Param("type")int type,@Param("customerId")String customerId) throws Exception;
	
	/**
	 * 根据国家ID获取所属渠道
	 * @param countryId
	 * @return
	 * @throws Exception
	 */
	public List<CustomerUser> getCustomerByCountry(String countryId) throws Exception;
}
