package cn.tcl.platform.customer.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.tcl.platform.customer.vo.Customer;
import cn.tcl.platform.customer.vo.CustomerUser;
import cn.tcl.platform.party.vo.Party;
import cn.tcl.platform.product.vo.Product;
import cn.tcl.platform.user.vo.UserLogin;

public interface ICustomerService {
	//查看列表
	public Map<String, Object> selectCustomersData(int start,int  limit,
			String searchStr,String countryId,String provinceId,
			String order,String sort,String conditions,String bt) throws Exception;
	//保存客户
//	public void saveCustomer(Customer customer) throws Exception;
	
	public void saveCustomer(Customer customer,String salers,String pros,String supers) throws Exception;
	
	//查找某个产品
	public Customer getCustomer(String cid) throws Exception;
	//编辑修改产品
//	public void editCustomer(Customer customer) throws Exception;
	
	public void editCustomer(Customer customer,String salers,String pros,String supers) throws Exception;
	
	//导入客户
	public void importCustomer(List<Customer> customers) throws Exception;
	
	
	
	public void deleteCustomer(Customer customer) throws Exception;
	
	public Customer getRepeatByCustomerCode(String code) throws Exception;
	
	public Party getOnePartyByName(String partyName) throws Exception;
	
	public int getCountByChannel(String channelType) throws Exception;
	
	//国家省市县镇
	
	public int getCountryByName(String searchName) throws Exception;
	
	public int getProvinceByName(String searchName) throws Exception;
	
	public int getCityByName(String searchName) throws Exception;
	
	public int getCountyByName(String searchName) throws Exception;
	
	public int getTownByName(String searchName) throws Exception;
	
	public String getCountryByNames(String searchName) throws Exception ;
		
	public XSSFWorkbook exporDealerName(String conditions,String[] excelHeader,String title) throws Exception;
	
	public List<Party> selectParty(String countryId) throws Exception;
	
	public List<UserLogin> selectCustomerSaler(String conditions,int type,String customerId) throws Exception;

	public List<CustomerUser> getCustomerUserRelations(String partyId,String customerId) throws Exception;
	
	public List<UserLogin> selectCustomerSalerData(String conditions,int type,String customerId) throws Exception;
	
	/**
	 * 根据国家ID获取所属渠道
	 * @param countryId
	 * @return
	 * @throws Exception
	 */
	public List<CustomerUser> getCustomerByCountry(String countryId) throws Exception;
}
