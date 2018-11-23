package cn.tcl.platform.performance.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.BDExcel.vo.BDSCImportExcel;
import cn.tcl.platform.modelmap.vo.ModelMap;
import cn.tcl.platform.performance.vo.Performance;


public interface IPerformanceDao {

	
	
	public String selectCustomerByName(
			@Param(value = "shopId") String countryId,
			@Param(value = "customerName") String customerName);
	
	public String selectShopByName(
			@Param(value = "countryId") String countryId,
			@Param(value = "shopName") String shopName);
	
	
	
	
	
	public String selectPCByShop(
			@Param(value = "shopId") String shopId,
			@Param(value = "userName") String userName);
	
	
	
	
	
	
	
	
	
	public Float  selectExchange(@Param(value = "country") String country,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate")String endDate,
			@Param(value = "date")String date);
	
	
	public List<HashMap<String, Object>> selectPCTarget(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="order") String order,
			@Param(value="sort") String sort,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions
			) throws Exception;
	
	
	public int  selectPCTargetCount(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions
			) throws Exception;
	
	
	
	//======================
	
	public int savePCTarget(List<Performance > reddemCodeList) throws Exception;
	public int deletePCTarget(List<Performance > reddemCodeList) throws Exception;

	
	
	public List<HashMap<String, Object>> selectPartyNameByuser(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions);


	

	public String selectPartyByUser(@Param(value = "userId") String userId);

	
	public String selectCountryByUser(@Param(value = "userId") String userId);
	public int selectCountByLine(@Param(value="line") String line) throws Exception;
	public String selectCountry(@Param(value = "country") String country)
			throws Exception;
	
	public List<HashMap<String,Object>> selectBDSCTarget(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "type") String type
			);

	
}
