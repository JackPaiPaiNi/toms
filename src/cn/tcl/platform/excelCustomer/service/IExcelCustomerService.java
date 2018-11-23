package cn.tcl.platform.excelCustomer.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.excel.vo.Excel;
import cn.tcl.platform.excelCustomer.vo.ExcelCustomer;

public interface IExcelCustomerService {
	// 查看列表
	public List<ExcelCustomer> selectDatas(String searchStr, String conditions)
			throws Exception;

	public List<HashMap<String, Object>> selectModelList(String beginDate,
			String endDate, String searchStr, String conditions, boolean isHq)
			throws Exception;

	public List<HashMap<String, Object>> selectModel(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq)
			throws Exception;

	public List<HashMap<String, Object>> selectModelTotal(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq)
			throws Exception;

	public  List<HashMap<String, Object>>  selectTargetByshop(String searchStr, String conditions,String tBeginDate,String tEndDate, boolean isHq,int type);

	public List<HashMap<String, Object>>  selectSaleDataByshop(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq);

	public List<HashMap<String, Object>>  selectSaleDataByshopByAc(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq);
	
	

	public List<HashMap<String, Object>>  selectSalerDatas(String searchStr, String conditions) throws Exception;

	public List<HashMap<String, Object>> selectStockBymodel(String spec,
			String searchStr, String conditions,String beginDate, String endDate,boolean isHq) throws Exception;

	
	public List<HashMap<String, Object>> selectStockBymodelByAc(String spec,
			String searchStr, String conditions,String beginDate, String endDate,boolean isHq) throws Exception;

	
	
	public List<ExcelCustomer> selectSaleDataBySum(String beginDate, String endDate,
			String searchStr, String conditions,boolean isHq);

	
	public List<ExcelCustomer> selectSaleDataBySumByAc(String beginDate, String endDate,
			String searchStr, String conditions,boolean isHq);

	
	
	public List<ExcelCustomer> selectTargetDataBySum(String beginDate, String endDate,
			String searchStr, String conditions,String tBeginDate,String tEndDate,boolean isHq,int type);

	public List<HashMap<String, Object>> selectDataByArea(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq)
			throws Exception;
	
	public List<HashMap<String, Object>> selectDataByAreaByAc(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq)
			throws Exception;
	
	

	public List<HashMap<String, Object>> selectTargetByArea( String searchStr, String conditions,String tBeginDate,String tEndDate,boolean isHq,int type)
			throws Exception;

	public List<HashMap<String, Object>> selectTargetBySaleman(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq,String tBeginDate,String tEndDate) throws Exception;
	
	public List<HashMap<String, Object>> selectTargetBySalemanByAc(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq,String tBeginDate,String tEndDate) throws Exception;
	
	
	

	public List<HashMap<String, Object>> selectModelBySpec(String beginDate, String endDate,String spec,
			String searchStr, String conditions,boolean isHq) throws Exception;
	
	public List<HashMap<String, Object>> selectModelBySpecByAc(String beginDate, String endDate,String spec,
			String searchStr, String conditions,boolean isHq) throws Exception;
	
	

	public List<HashMap<String, Object>> selectModelListBySpec(String spec,
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;
	

	public List<HashMap<String, Object>> selectModelListBySpecByAc(String spec,
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;
	
	
	

	public List<HashMap<String, Object>> selectModelBySpecTotal(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectModelBySpecTotalByAc(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	
	
	
	public List<HashMap<String, Object>> selectModelTotalBySpec(String spec,
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectStockByTotal(String searchStr,
			String conditions,String beginDate,String endDate) throws Exception;
	
	public List<HashMap<String, Object>> selectStockByTotalByAc(String searchStr,
			String conditions,String beginDate,String endDate) throws Exception;
	
	
	
	

	public List<HashMap<String, Object>> selectSelloutByDealer(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq,String tBeginDate,String tEndDate) throws Exception;
	
	
	public List<HashMap<String, Object>> selectSelloutByDealerByAc(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq,String tBeginDate,String tEndDate) throws Exception;
	public List<HashMap<String, Object>> selectDisplayBymodel(String spec,
			String searchStr, String conditions,String beginDate, String endDate,boolean isHq) throws Exception;
	
	
	public List<HashMap<String, Object>> selectDisplayBymodelByAc(String spec,
			String searchStr, String conditions,String beginDate, String endDate,boolean isHq) throws Exception;
	
	
	

	public List<HashMap<String, Object>> selectStockByData(String spec,
			String searchStr, String conditions,String beginDate, String endDate,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectDisplayByData(String spec,
			String searchStr, String conditions,String beginDate, String endDate,boolean isHq) throws Exception;

	
	public List<HashMap<String, Object>> selectStockByDataByAc(String spec,
			String searchStr, String conditions,String beginDate, String endDate,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectDisplayByDataByAc(String spec,
			String searchStr, String conditions,String beginDate, String endDate,boolean isHq) throws Exception;

	
	
	public List<HashMap<String, Object>> selectDisPlayByTotal(String searchStr,
			String conditions,String beginDate, String endDate) throws Exception;
	
	
	public List<HashMap<String, Object>> selectDisPlayByTotalByAc(String searchStr,
			String conditions,String beginDate, String endDate) throws Exception;
	
	

	public List<LinkedHashMap<String, Object>> selectSaleDataBySize(String beginSize,
			String endSize, String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	public List<LinkedHashMap<String, Object>> selectSaleDataBySizeByAc(String beginSize,
			String endSize, String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	
	
	
	public List<HashMap<String, Object>> selectSaleDataBySizeLast(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	public List<LinkedHashMap<String, Object>> selectSaleTotalBySize(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	

	public List<LinkedHashMap<String, Object>> selectSaleTotalBySizeByAc(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	
	
	public List<LinkedHashMap<String, Object>> selectQtyTotalBySpecType(String spec,
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;
	
	
	
	public List<LinkedHashMap<String, Object>> selectQtyTotalBySpecTypeByAc(String spec,
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;
	
	public List<LinkedHashMap<String, Object>> selectQtyTotalBySpec(String spec,
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	public List<LinkedHashMap<String, Object>> selectQtyTotalBySpecByAc(String spec,
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	
	
	public List<LinkedHashMap<String, Object>> selectSaleDataByDEALERByAc(
			 String beginDate,
			 String endDate,
			 String searchStr,
			 String conditions,
			 boolean isHq);

	public List<HashMap<String, Object>> selectSaleTTLByDEALERByAc(
			 String beginDate,
			 String endDate,
			 String searchStr,
			String conditions,
			boolean isHq);
	
	public List<LinkedHashMap<String, Object>> selectQtyTotalBySpecYear(String spec,
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectQtyTotalBySpecModelYear(
			String spec, String searchStr, String conditions,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectQtyTotalBySpecTotalYear(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;
	
	
	public List<LinkedHashMap<String, Object>> selectQtyTotalBySpecYearByAc(String spec,
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectQtyTotalBySpecModelYearByAc(
			String spec, String searchStr, String conditions,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectQtyTotalBySpecTotalYearByAc(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;
	

	public List<HashMap<String, Object>> selectInfoByCPURH(String beginDate,
			String endDate, String searchStr, String conditions)
			throws Exception;

	public List<HashMap<String, Object>> selectDataByCPURH(String beginDate,
			String endDate, String countryId, String searchStr,
			String conditions,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectDataHeadByCPURH(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectTargetByAcfo(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq,
			String tBeginDate,
			 String tEndDate)
			throws Exception;
	
	public List<HashMap<String, Object>> selectTargetByAcfoByAc(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq,
			String tBeginDate,
			 String tEndDate)
			throws Exception;
	
	

	public List<HashMap<String, Object>> selectPartyNameByuser(String searchStr, String conditions)
			throws Exception;

	public  List<HashMap<String, Object>>   selectFpsNameByShop(String beginDate, String endDate, String searchStr, String conditions)
			throws Exception;
	

	public  List<HashMap<String, Object>>   selectFpsNameByShopByAc(String beginDate, String endDate, String searchStr, String conditions)
			throws Exception;
	
	
	

	public List<HashMap<String, Object>> selectTotalByCPURH(String beginDate,
			String endDate, String searchStr, String conditions,String countryId,boolean isHq,String tBeginDate,String tEndDate)
			throws Exception;

	public List<HashMap<String, Object>> selectTTLByCPURH(String beginDate,
			String endDate, String searchStr, String conditions,String countryId,boolean isHq)
			throws Exception;

	public List<HashMap<String, Object>> selectSaleTTLByCPURH(String beginDate,
			String endDate, String searchStr, String conditions,String countryId,boolean isHq,String tBeginDate,String tEndDate)
			throws Exception;

	/*public List<HashMap<String, Object>> selectTargetTTLByCPURH(
			String beginDate, String endDate, String searchStr,
			String conditions) throws Exception;
*/
	public List<HashMap<String, Object>> selectInfoByCPUSALE(String beginDate,
			String endDate, String searchStr, String conditions)
			throws Exception;

	public List<HashMap<String, Object>> selectTotalByCPUSALE(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq,String tBeginDate,String tEndDate)
			throws Exception;

	public List<HashMap<String, Object>> selectModelByCPUSALE(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq)
			throws Exception;

	public List<HashMap<String, Object>> selectModelTTLByCPUSALE(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectTTLByCPUSALE(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq,String tBeginDate,String tEndDate)
			throws Exception;

	public List<HashMap<String, Object>> selectInfoByCPUACFO(String beginDate,
			String endDate, String searchStr, String conditions)
			throws Exception;

	public List<HashMap<String, Object>> selectTotalByCPUACFO(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq,String tBeginDate,String tEndDate)
			throws Exception;

	public List<HashMap<String, Object>> selectModelByCPUACFO(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq)
			throws Exception;

	public List<HashMap<String, Object>> selectModelTTLByCPUACFO(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectTTLByCPUACFO(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq,String tBeginDate,String tEndDate)
			throws Exception;

	public List<LinkedHashMap<String, Object>> selectSaleDataByDEALER(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectSaleTTLByDEALER(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectDEALER(String searchStr,
			String conditions) throws Exception;

	public List<HashMap<String, Object>> selectCPUDisplayBYBRANCH(
			String searchStr, String conditions,String beginDate, String endDate,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectCPUInventoryBYBRANCH(
			String searchStr, String conditions,String beginDate, String endDate,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectCPUHeadDisplayBYBRANCH(
			String searchStr, String conditions,String beginDate, String endDate) throws Exception;

	public List<HashMap<String, Object>> selectCPUHeadInventoryBYBRANCH(
			String searchStr, String conditions,String beginDate, String endDate) throws Exception;

	public List<HashMap<String, Object>> selectCPUInfoByBRANCH(
			String searchStr, String conditions) throws Exception;

	public List<HashMap<String, Object>> selectSellDataBYBRANCH(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectSellDataByTotal(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectCPUDisplayByTotal(
			String searchStr, String conditions,String beginDate, String endDate) throws Exception;

	public List<HashMap<String, Object>> selectCPUInventoryByTotal(
			String searchStr, String conditions,String beginDate, String endDate) throws Exception;

	public List<HashMap<String, Object>> selectSellDataByTTL(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq)
			throws Exception;

	public List<HashMap<String, Object>> selectCPUDisplayByTTL(
			String searchStr, String conditions,String beginDate, String endDate,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectCPUInventoryByTTL(
			String searchStr, String conditions,String beginDate, String endDate,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectSellDataBySUM(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq)
			throws Exception;

	public List<HashMap<String, Object>> selectCPUDisplayBySUM(
			String searchStr, String conditions,String beginDate, String endDate) throws Exception;

	public List<HashMap<String, Object>> selectCPUInventoryBySUM(
			String searchStr, String conditions,String beginDate, String endDate) throws Exception;

	public List<HashMap<String, Object>> selectDataByAreaInfo(String searchStr,
			String conditions) throws Exception;

	public List<HashMap<String, Object>> selectTargetBySalemanInfo(
			String searchStr, String conditions) throws Exception;

	public List<HashMap<String, Object>> selectTargetByAcfoInfo(
			String searchStr, String conditions) throws Exception;

	public List<HashMap<String, Object>> selectSelloutByDealerInfo(
			String searchStr, String conditions) throws Exception;

	public String selectPartyByUser(String userId) throws Exception;

	public List<HashMap<String, Object>> selectRegionalHeadByParty(
			String searchStr, String conditions) throws Exception;

	public List<HashMap<String, Object>> selectAreaByUser(String searchStr, String conditions)
			throws Exception;

	public List<HashMap<String, Object>> selectCoreProductByCountry(
			String countryId,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectCoreProductByStock(
			String countryId,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectCoreProductBySample(
			String countryId,boolean isHq) throws Exception;

	public List<HashMap<String, Object>> selectCoreLine(String countryId)
			throws Exception;

	public List<HashMap<String, Object>> selectCoreSellData(String searchStr,
			String conditions, String countryId, String beginDate,
			String endDate,boolean isHq);

	public List<HashMap<String, Object>> selectCoreByTarget(String searchStr,
			String conditions, String beginDate,
			String endDate,String countryId,String tBeginDate,String tEndDate,boolean isHq);

	public List<HashMap<String, Object>> selectCoreSellTotal(String searchStr,
			String conditions, String countryId, String beginDate,
			String endDate,boolean isHq);

	public String selectCountryByUser(String userId);

	

}
