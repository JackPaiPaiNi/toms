package cn.tcl.platform.excel.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.excel.vo.Excel;
import cn.tcl.platform.excel.vo.ImportExcel;
import cn.tcl.platform.sellIn.vo.SellIn;
import cn.tcl.platform.shop.vo.Shop;

public interface ImportExcelDao {

	public String getPartyIdByName(@Param(value = "partyName") String partyName)
			throws Exception;

	
	public List<HashMap<String, Object>>  selectModel(
			@Param(value="searchStr") String searchStr,
			@Param(value="conditions") String conditions,
			@Param(value="spec") String spec
			) throws Exception;
	
	
	public String  selectSoType(
			@Param(value="countryId") String countryId
			) throws Exception;
	
	
	
	public List<HashMap<String, Object>>  selectModelByCustomer(
			@Param(value="searchStr") String searchStr,
			@Param(value="conditions") String conditions,
			@Param(value="spec") String spec
			) throws Exception;
	
	
	public String selectDisplayCountByRE(@Param(value = "shopId") String shopId,
			@Param(value = "model") String model,
			@Param(value = "datadate") String datadate) throws Exception;
	
	public void deleteDisplayCountByRE(@Param(value = "shopId") String shopId,
			@Param(value = "model") String model,
			@Param(value = "datadate") String datadate) throws Exception;
	
	
	
	public void deleteDisplayCountByRECountry(@Param(value = "countryId") String shopId,
			@Param(value = "model") String model,
			@Param(value = "datadate") String datadate) throws Exception;
	
	
	
	
	
	
	public void deleteDisplayCountByRECUS(@Param(value = "customer_id") String shopId,
			@Param(value = "model") String model,
			@Param(value = "datadate") String datadate) throws Exception;
	
	
	
	
	public String selectStockCountByRE(@Param(value = "shopId") String shopId,
			@Param(value = "model") String model,
			@Param(value = "datadate") String datadate) throws Exception;
	

	public void deleteStockCountByRE(@Param(value = "shopId") String shopId,
			@Param(value = "model") String model,
			@Param(value = "datadate") String datadate) throws Exception;
	public void deleteStockCountByRECUS(@Param(value = "customer_id") String shopId,
			@Param(value = "model") String model,
			@Param(value = "datadate") String datadate) throws Exception;
	
	
	
	public void deleteStockCountByRECountry(@Param(value = "countryId") String shopId,
			@Param(value = "model") String model,
			@Param(value = "datadate") String datadate) throws Exception;
	
	
	
	
	public int selectStockCount(@Param(value = "shopId") String shopId,
			@Param(value = "model") String model) throws Exception;

	public int selectDisPlayCount(@Param(value = "shopId") String shopId,
			@Param(value = "model") String model) throws Exception;

	
	public int selectDisPlayCountCUS(@Param(value = "customer_id") String shopId,
			@Param(value = "model") String model) throws Exception;

	
	
	/*public int saveSales(ImportExcel excel) throws Exception;
	 */

	public int saveSales(List<ImportExcel > reddemCodeList) throws Exception;
	/*public int updateStocks(ImportExcel excel) throws Exception;

	public int saveDisPlays(ImportExcel excel) throws Exception;

	public int saveStocks(ImportExcel excel) throws Exception;

	public int updateDisPlay(ImportExcel excel) throws Exception*/;
	public int updateStocks(List<ImportExcel > reddemCodeList) throws Exception;
	
	public int updateStocksByCustomer(List<ImportExcel > reddemCodeList) throws Exception;

	public int updateStocksByCountry(List<ImportExcel > reddemCodeList) throws Exception;

	public int updateStocksByShop(List<ImportExcel > reddemCodeList) throws Exception;


	public int saveDisPlays(List<ImportExcel > reddemCodeList) throws Exception;

	
	public int saveDisPlaysByRe(List<ImportExcel > reddemCodeList) throws Exception;

	
	public int saveStocks(List<ImportExcel > reddemCodeList) throws Exception;

	public int updateDisPlay(List<ImportExcel > reddemCodeList) throws Exception;
	public int updateDisPlayRE(List<ImportExcel > reddemCodeList) throws Exception;
	public int updateStockRE(List<ImportExcel > reddemCodeList) throws Exception;

	
	
	public void deleteCore(@Param(value = "countryId") String countryId) throws Exception;
	
	public void insertCore(@Param(value = "countryId") String countryId, 
			@Param(value = "line") String line)throws Exception;

	
	public List<HashMap<String, Object>> selectCore(
			@Param(value = "countryId") String countryId) throws Exception;
	
	public List<HashMap<String, Object>> selectAllCore() throws Exception;
	public String selectCountry(@Param(value = "country") String country)
			throws Exception;
	
	public Float  selectExchange(@Param(value = "country") String country,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate")String endDate,
			@Param(value = "date")String date);
	
	public Float  selectPrice(@Param(value = "country") String country,
			@Param(value = "model")String model);
	
	public Float  selectPriceByChannel(@Param(value = "country") String country,
			@Param(value = "model")String model);
	
	public void  saleRe(@Param(value = "country") String country,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate")String endDate);
	
	public void  insertTVSaleVive(@Param(value = "country") String country,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate")String endDate);
	
	
	public void  insertACSaleVive(@Param(value = "country") String country,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate")String endDate);
	
	public String  selectCustomerByName(@Param(value = "customerName") String customerName,@Param(value = "countryId")String countryId);
	
	public List<HashMap<String, Object>> selectCustomer(
			@Param(value="conditions") String conditions,@Param(value="searchStr") String searchStr);

	public List<HashMap<String, Object>> selectCountryList(
			@Param(value="conditions") String conditions,@Param(value="searchStr") String searchStr);

	public Excel  selectCustomerModel(@Param(value = "channelModel")String channelModel,
			@Param(value = "countryId")String countryId,
			@Param(value = "customerId")String customerId) throws Exception;

	public Excel getShopByNames(@Param(value="shopName") String shopName) throws Exception;

}
