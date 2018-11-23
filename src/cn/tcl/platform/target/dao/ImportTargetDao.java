package cn.tcl.platform.target.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.customer.vo.Customer;
import cn.tcl.platform.shop.vo.Shop;
import cn.tcl.platform.target.vo.Target;

public interface ImportTargetDao {
	public int saveChannelTarget(Target target)throws Exception;
	
	public String selectChannel(@Param("name")String name)throws Exception;
	

	public String selectReg(@Param("name")String name)throws Exception;
	
	public int selectCount(@Param("classId")String classId,@Param("targetId")String targetId)throws Exception;
	public int updateTarget(Target target)throws Exception;
	
	
	public String selectUser(@Param("name")String name,@Param("role")String role)throws Exception;
	
	public List<Customer> selectCustomer(@Param("partyId")String partyId) throws Exception;
	
	public List<Shop> selectShop(@Param("partyId")String partyId) throws Exception;
	
	public List<Shop> selectSale(Map<String,Object> map) throws Exception;
	
	public List<Shop> selectManager(@Param("partyId")String partyId) throws Exception;
	
	public List<Shop> selectProduct(@Param("partyId")String partyId) throws Exception;
	
	public List<Target> chooseRegion(@Param("partyId")String partyId) throws Exception;
	
	public List<Target> chooseOffice(@Param("partyId")String partyId) throws Exception;
}
