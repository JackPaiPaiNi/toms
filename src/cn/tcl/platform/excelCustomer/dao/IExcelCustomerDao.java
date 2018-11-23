package cn.tcl.platform.excelCustomer.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import cn.tcl.platform.excel.vo.Excel;
import cn.tcl.platform.excelCustomer.vo.ExcelCustomer;

public interface IExcelCustomerDao {
	public List<ExcelCustomer> selectDatas(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions) throws Exception;

	public List<HashMap<String, Object>> selectModelList(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectModel(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectModelTotal(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public  List<HashMap<String, Object>>  selectTargetByshop(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "tBeginDate") String tBeginDate,
			@Param(value = "tEndDate") String tEndDate,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "type") int type
			);

	public   List<HashMap<String, Object>> selectSaleDataByshop(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	public   List<HashMap<String, Object>> selectSaleDataByshopByAc(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	

	public List<ExcelCustomer> selectSaleDataBySum(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq );

	public List<ExcelCustomer> selectSaleDataBySumByAc(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq );

	
	
	
	public List<ExcelCustomer> selectTargetDataBySum(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "tBeginDate") String tBeginDate,
			@Param(value = "tEndDate") String tEndDate,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "type")			int type		
);

	public List<HashMap<String, Object>>selectSalerDatas(@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions)
			throws Exception;

	public List<HashMap<String, Object>> selectStockBymodel(
			@Param(value = "spec") String spec,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "isHq") boolean isHq);
	
	public List<HashMap<String, Object>> selectStockBymodelByAc(
			@Param(value = "spec") String spec,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "isHq") boolean isHq);
	
	

	public List<HashMap<String, Object>> selectDataByArea(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	public List<HashMap<String, Object>> selectDataByAreaByAc(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	
	

	public List<HashMap<String, Object>> selectTargetByArea(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "tBeginDate") String tBeginDate,
			@Param(value = "tEndDate") String tEndDate,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "type")			int type
			);

	public List<HashMap<String, Object>> selectTargetBySaleman(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "tBeginDate") String tBeginDate,
			@Param(value = "tEndDate") String tEndDate);
	public List<HashMap<String, Object>> selectTargetBySalemanByAc(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "tBeginDate") String tBeginDate,
			@Param(value = "tEndDate") String tEndDate);
	
	
	
	

	public List<HashMap<String, Object>> selectModelBySpec(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "spec") String spec,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	public List<HashMap<String, Object>> selectModelBySpecByAc(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "spec") String spec,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	
	

	public List<HashMap<String, Object>> selectModelListBySpec(
			@Param(value = "spec") String spec,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectModelListBySpecByAc(
			@Param(value = "spec") String spec,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	
	
	public List<HashMap<String, Object>> selectModelBySpecTotal(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	public List<HashMap<String, Object>> selectModelBySpecTotalByAc(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	
	
	public List<HashMap<String, Object>> selectModelTotalBySpec(
			@Param(value = "spec") String spec,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectStockByTotal(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate);
	
	public List<HashMap<String, Object>> selectStockByTotalByAc(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate);
	
	

	public List<HashMap<String, Object>> selectSelloutByDealer(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "tBeginDate") String tBeginDate,
			@Param(value = "tEndDate") String tEndDate);
	public List<HashMap<String, Object>> selectSelloutByDealerByAc(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "tBeginDate") String tBeginDate,
			@Param(value = "tEndDate") String tEndDate);

	
	
	
	public List<HashMap<String, Object>> selectDisplayBymodel(
			@Param(value = "spec") String spec,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "isHq") boolean isHq);
	
	public List<HashMap<String, Object>> selectDisplayBymodelByAc(
			@Param(value = "spec") String spec,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "isHq") boolean isHq);
	
	

	public List<HashMap<String, Object>> selectStockByData(
			@Param(value = "spec") String spec,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "isHq") boolean isHq);
	
	
	public List<HashMap<String, Object>> selectStockByDataByAc(
			@Param(value = "spec") String spec,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectDisplayByData(
			@Param(value = "spec") String spec,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "isHq") boolean isHq);
	

	public List<HashMap<String, Object>> selectDisplayByDataByAc(
			@Param(value = "spec") String spec,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "isHq") boolean isHq);
	
	
	

	public List<HashMap<String, Object>> selectDisPlayByTotal(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate);


	public List<HashMap<String, Object>> selectDisPlayByTotalByAc(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate);

	
	
	
	public List<LinkedHashMap<String, Object>> selectSaleDataBySize(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	
	public List<LinkedHashMap<String, Object>> selectSaleDataBySizeByAc(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	
	

	public List<HashMap<String, Object>> selectSaleDataBySizeLast(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<LinkedHashMap<String, Object>> selectSaleTotalBySize(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	public List<LinkedHashMap<String, Object>> selectSaleTotalBySizeByAc(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	
	

	public List<LinkedHashMap<String, Object>> selectQtyTotalBySpecType(
			@Param(value = "spec") String spec,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	public List<LinkedHashMap<String, Object>> selectQtyTotalBySpecTypeByAc(
			@Param(value = "spec") String spec,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	

	public List<LinkedHashMap<String, Object>> selectQtyTotalBySpec(
			@Param(value = "spec") String spec,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	public List<LinkedHashMap<String, Object>> selectQtyTotalBySpecByAc(
			@Param(value = "spec") String spec,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<LinkedHashMap<String, Object>> selectQtyTotalBySpecYearByAc(
			@Param(value = "spec") String spec,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectQtyTotalBySpecModelYearByAc(
			@Param(value = "spec") String spec,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectQtyTotalBySpecTotalYearByAc(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	
	public List<LinkedHashMap<String, Object>> selectQtyTotalBySpecYear(
			@Param(value = "spec") String spec,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectQtyTotalBySpecModelYear(
			@Param(value = "spec") String spec,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectQtyTotalBySpecTotalYear(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	

	public List<HashMap<String, Object>> selectInfoByCPURH(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions);

	public List<HashMap<String, Object>> selectDataByCPURH(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "countryId") String countryId,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectDataHeadByCPURH(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectTargetByAcfo(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "tBeginDate") String tBeginDate,
			@Param(value = "tEndDate") String tEndDate);
	
	public List<HashMap<String, Object>> selectTargetByAcfoByAc(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "tBeginDate") String tBeginDate,
			@Param(value = "tEndDate") String tEndDate);
	
	
	

	public List<HashMap<String, Object>> selectPartyNameByuser(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions);

	
	public List<HashMap<String, Object>>  selectFpsNameByShop(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions);

	
	public List<HashMap<String, Object>>  selectFpsNameByShopByAc(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions);

	
	
	
	
	
	
	public List<HashMap<String, Object>> selectTotalByCPURH(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "countryId") String countryId,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "tBeginDate") String tBeginDate,
			@Param(value = "tEndDate") String tEndDate);

	public List<HashMap<String, Object>> selectTTLByCPURH(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "countryId") String countryId,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectSaleTTLByCPURH(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "countryId") String countryId,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "tBeginDate") String tBeginDate,
			@Param(value = "tEndDate") String tEndDate);

	/*public List<HashMap<String, Object>> selectTargetTTLByCPURH(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions);*/

	public List<HashMap<String, Object>> selectInfoByCPUSALE(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions);

	public List<HashMap<String, Object>> selectTotalByCPUSALE(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "tBeginDate") String tBeginDate,
			@Param(value = "tEndDate") String tEndDate);

	public List<HashMap<String, Object>> selectModelByCPUSALE(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectModelTTLByCPUSALE(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectTTLByCPUSALE(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "tBeginDate") String tBeginDate,
			@Param(value = "tEndDate") String tEndDate);

	public List<HashMap<String, Object>> selectInfoByCPUACFO(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions);

	public List<HashMap<String, Object>> selectTotalByCPUACFO(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "tBeginDate") String tBeginDate,
			@Param(value = "tEndDate") String tEndDate);

	public List<HashMap<String, Object>> selectModelByCPUACFO(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectModelTTLByCPUACFO(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectTTLByCPUACFO(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "tBeginDate") String tBeginDate,
			@Param(value = "tEndDate") String tEndDate);

	public List<LinkedHashMap<String, Object>> selectSaleDataByDEALER(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectSaleTTLByDEALER(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	
	public List<LinkedHashMap<String, Object>> selectSaleDataByDEALERByAc(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectSaleTTLByDEALERByAc(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	

	public List<HashMap<String, Object>> selectDEALER(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions);

	public List<HashMap<String, Object>> selectCPUDisplayBYBRANCH(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectCPUInventoryBYBRANCH(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectCPUHeadDisplayBYBRANCH(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate
			);

	public List<HashMap<String, Object>> selectCPUHeadInventoryBYBRANCH(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate
			);

	public List<HashMap<String, Object>> selectCPUInfoByBRANCH(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions);

	public List<HashMap<String, Object>> selectSellDataBYBRANCH(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectSellDataByTotal(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectCPUDisplayByTotal(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate);

	public List<HashMap<String, Object>> selectCPUInventoryByTotal(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate);

	public List<HashMap<String, Object>> selectSellDataByTTL(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectCPUDisplayByTTL(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectCPUInventoryByTTL(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectSellDataBySUM(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectCPUDisplayBySUM(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate
			);

	public List<HashMap<String, Object>> selectCPUInventoryBySUM(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate);

	public List<HashMap<String, Object>> selectDataByAreaInfo(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions);

	public List<HashMap<String, Object>> selectTargetBySalemanInfo(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions);

	public List<HashMap<String, Object>> selectTargetByAcfoInfo(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions);

	public List<HashMap<String, Object>> selectSelloutByDealerInfo(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions);

	public String selectPartyByUser(@Param(value = "userId") String userId);

	public List<HashMap<String, Object>> selectRegionalHeadByParty(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions);

	public List<HashMap<String, Object>> selectAreaByUser(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions);

	public List<HashMap<String, Object>> selectCoreProductByCountry(
			@Param(value = "countryId") String countryId,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectCoreProductByStock(
			@Param(value = "countryId") String countryId,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectCoreProductBySample(
			@Param(value = "countryId") String countryId,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectCoreLine(
			@Param(value = "countryId") String countryId);

	public List<HashMap<String, Object>> selectCoreSellData(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "countryId") String countryId,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectCoreByTarget(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "countryId") String countryId,
			@Param(value = "tBeginDate") String tBeginDate,
			@Param(value = "tEndDate") String tEndDate,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectCoreSellTotal(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "countryId") String countryId,
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "isHq") boolean isHq);

	public String selectCountryByUser(@Param(value = "userId") String userId);
	
}
