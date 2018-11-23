package cn.tcl.platform.excel.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.tcl.platform.excel.dao.IExcelDao;
import cn.tcl.platform.excel.service.IExcelService;
import cn.tcl.platform.excel.vo.Excel;

@Service("excelService")
public class ExcelServiceImpl implements IExcelService {
	@Autowired
	private IExcelDao excelDao;

	@Override
	public List<Excel> selectDatas(String searchStr, String conditions)
			throws Exception {
		return excelDao.selectDatas(searchStr, conditions);
	}

	@Override
	public List<HashMap<String, Object>> selectModelList(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq)
			throws Exception {
		return excelDao.selectModelList(beginDate, endDate, searchStr,
				conditions,isHq);
	}

	@Override
	public  List<HashMap<String, Object>> selectTargetByshop(String searchStr, String conditions,String tBeginDate,String tEndDate, boolean isHq,int type) {
		return excelDao.selectTargetByshop(searchStr, conditions, tBeginDate, tEndDate,isHq,type);
	}

	@Override
	public List<HashMap<String, Object>> selectSaleDataByshop(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq) {
		return excelDao.selectSaleDataByshop(beginDate, endDate, searchStr, conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectModel(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq)
			throws Exception {
		return excelDao.selectModel(beginDate, endDate, searchStr, conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectSalerDatas(String searchStr, String conditions) throws Exception {

		return excelDao.selectSalerDatas(searchStr, conditions);
	}

	@Override
	public List<HashMap<String, Object>> selectModelTotal(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq)
			throws Exception {
		return excelDao.selectModelTotal(beginDate, endDate, searchStr,
				conditions,isHq);
	}

	@Override
	public List<Excel> selectSaleDataBySum(String beginDate, String endDate,
			String searchStr, String conditions,boolean isHq) {
		return excelDao.selectSaleDataBySum(beginDate, endDate, searchStr,
				conditions,isHq);
	}

	@Override
	public List<Excel> selectTargetDataBySum(String beginDate, String endDate,
			String searchStr, String conditions,String tBeginDate,String tEndDate,boolean isHq,int type) {
		return excelDao.selectTargetDataBySum(beginDate, endDate, searchStr, conditions, tBeginDate, tEndDate,isHq,type);
	}

	@Override
	public List<HashMap<String, Object>> selectDataByArea(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq)
			throws Exception {
		return excelDao.selectDataByArea(beginDate, endDate, searchStr,
				conditions,isHq);
	}
	
	@Override
	public List<HashMap<String, Object>> selectDataByAreaByAc(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq)
			throws Exception {
		return excelDao.selectDataByAreaByAc(beginDate, endDate, searchStr,
				conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectTargetByArea(String searchStr,String conditions,String tBeginDate,String tEndDate,boolean isHq,int type)
			throws Exception {
		return excelDao.selectTargetByArea( searchStr,
				conditions, tBeginDate, tEndDate,isHq,type);
	}

	@Override
	public List<HashMap<String, Object>> selectTargetBySaleman(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq,String tBeginDate,String tEndDate) throws Exception {
		return excelDao.selectTargetBySaleman(beginDate, endDate, searchStr, conditions, isHq, tBeginDate, tEndDate);
	}

	@Override
	public List<HashMap<String, Object>> selectModelBySpec(String beginDate, String endDate,String spec,
			String searchStr, String conditions,boolean isHq) throws Exception {
		return excelDao.selectModelBySpec(beginDate, endDate,spec, searchStr, conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectModelListBySpec(String spec,
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectModelListBySpec(spec, beginDate, endDate,
				searchStr, conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectStockBymodel(String spec,
			String searchStr, String conditions,boolean isHq,String beginDate,String endDate) throws Exception {
		return excelDao.selectStockBymodel(spec, searchStr, conditions,isHq,beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectModelBySpecTotal(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectModelBySpecTotal(beginDate, endDate, searchStr,
				conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectModelTotalBySpec(String spec,
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectModelTotalBySpec(spec, beginDate, endDate,
				searchStr, conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectStockByTotal(String searchStr,
			String conditions,String beginDate, String endDate) throws Exception {
		return excelDao.selectStockByTotal(searchStr, conditions, beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectSelloutByDealer(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq,String tBeginDate,String tEndDate) throws Exception {
		return excelDao.selectSelloutByDealer(beginDate, endDate, searchStr, conditions, isHq, tBeginDate, tEndDate);
	}

	@Override
	public List<HashMap<String, Object>> selectDisplayBymodel(String spec,
			String searchStr, String conditions,boolean isHq,String beginDate, String endDate) throws Exception {
		return excelDao.selectDisplayBymodel(spec, searchStr, conditions,isHq,beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectDisPlayByTotal(String searchStr,
			String conditions,String beginDate, String endDate) throws Exception {
		return excelDao.selectDisPlayByTotal(searchStr, conditions,beginDate, endDate);
	}

	@Override
	public List<LinkedHashMap<String, Object>> selectSaleDataBySize(String beginSize,
			String endSize, String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectSaleDataBySize( beginDate,
				endDate, searchStr, conditions,isHq);
	}

	@Override
	public List<LinkedHashMap<String, Object>> selectSaleTotalBySize(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectSaleTotalBySize(beginDate, endDate, searchStr,
				conditions,isHq);
	}

	@Override
	public List<LinkedHashMap<String, Object>> selectQtyTotalBySpecType(String spec,
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectQtyTotalBySpecType(spec, beginDate, endDate,
				searchStr, conditions,isHq);
	}

	@Override
	public List<LinkedHashMap<String, Object>> selectQtyTotalBySpec(String spec,
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectQtyTotalBySpec(spec, beginDate, endDate,
				searchStr, conditions,isHq);
	}

	@Override
	public List<LinkedHashMap<String, Object>> selectQtyTotalBySpecYear(String spec,
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectQtyTotalBySpecYear(spec, beginDate, endDate,
				searchStr, conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectQtyTotalBySpecModelYear(
			String spec, String searchStr, String conditions,boolean isHq) throws Exception {
		return excelDao.selectQtyTotalBySpecModelYear(spec, searchStr,
				conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectQtyTotalBySpecTotalYear(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectQtyTotalBySpecTotalYear(beginDate, endDate,
				searchStr, conditions,isHq);
	}

	
	
	@Override
	public List<LinkedHashMap<String, Object>> selectQtyTotalBySpecYearByAc(String spec,
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectQtyTotalBySpecYearByAc(spec, beginDate, endDate,
				searchStr, conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectQtyTotalBySpecModelYearByAc(
			String spec, String searchStr, String conditions,boolean isHq) throws Exception {
		return excelDao.selectQtyTotalBySpecModelYearByAc(spec, searchStr,
				conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectQtyTotalBySpecTotalYearByAc(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectQtyTotalBySpecTotalYearByAc(beginDate, endDate,
				searchStr, conditions,isHq);
	}

	
	@Override
	public List<HashMap<String, Object>> selectSaleDataBySizeLast(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectSaleDataBySizeLast(beginDate, endDate, searchStr,
				conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectInfoByCPURH(String beginDate,
			String endDate, String searchStr, String conditions)
			throws Exception {
		return excelDao.selectInfoByCPURH(beginDate, endDate, searchStr,
				conditions);
	}

	@Override
	public List<HashMap<String, Object>> selectDataByCPURH(String beginDate,
			String endDate, String countryId, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectDataByCPURH(beginDate, endDate, countryId,
				searchStr, conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectDataHeadByCPURH(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectDataHeadByCPURH(beginDate, endDate, searchStr,
				conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectTargetByAcfo(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq,String tBeginDate,String tEndDate)
			throws Exception {
		return excelDao.selectTargetByAcfo(beginDate, endDate, searchStr, conditions, isHq, tBeginDate, tEndDate);
	}

	@Override
	public List<HashMap<String, Object>> selectPartyNameByuser(String searchStr, String conditions)
			throws Exception {
		return excelDao.selectPartyNameByuser(searchStr, conditions);
	}

	@Override
	public List<HashMap<String, Object>> selectFpsNameByShop(String beginDate, String endDate, String searchStr, String conditions)
			throws Exception {
		return excelDao.selectFpsNameByShop(beginDate, endDate, searchStr, conditions);
	}

	@Override
	public List<HashMap<String, Object>> selectTotalByCPURH(String beginDate,
			String endDate, String searchStr, String conditions,String countryId,boolean isHq,String tBeginDate,String tEndDate)
			throws Exception {
		return excelDao.selectTotalByCPURH(beginDate, endDate, searchStr, conditions, countryId, isHq, tBeginDate, tEndDate);
	}

	@Override
	public List<HashMap<String, Object>> selectTTLByCPURH(String beginDate,
			String endDate, String searchStr, String conditions,String countryId,boolean isHq)
			throws Exception {
		return excelDao.selectTTLByCPURH(beginDate, endDate, searchStr,
				conditions,countryId,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectSaleTTLByCPURH(String beginDate,
			String endDate, String searchStr, String conditions,String countryId,boolean isHq
			,String tBeginDate,String tEndDate)
			throws Exception {
		return excelDao.selectSaleTTLByCPURH(beginDate, endDate, searchStr, conditions, countryId, isHq, tBeginDate, tEndDate);
	}

/*	@Override
	public List<HashMap<String, Object>> selectTargetTTLByCPURH(
			String beginDate, String endDate, String searchStr,
			String conditions) throws Exception {
		return excelDao.selectTargetTTLByCPURH(beginDate, endDate, searchStr,
				conditions);
	}
*/
	@Override
	public List<HashMap<String, Object>> selectInfoByCPUSALE(String beginDate,
			String endDate, String searchStr, String conditions)
			throws Exception {
		return excelDao.selectInfoByCPUSALE(beginDate, endDate, searchStr,
				conditions);
	}

	@Override
	public List<HashMap<String, Object>> selectTotalByCPUSALE(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq,String tBeginDate,String tEndDate)
			throws Exception {
		return excelDao.selectTotalByCPUSALE(beginDate, endDate, searchStr, conditions, isHq, tBeginDate, tEndDate);
	}

	@Override
	public List<HashMap<String, Object>> selectModelByCPUSALE(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq)
			throws Exception {
		return excelDao.selectModelByCPUSALE(beginDate, endDate, searchStr,
				conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectModelTTLByCPUSALE(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectModelTTLByCPUSALE(beginDate, endDate, searchStr,
				conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectTTLByCPUSALE(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq,String tBeginDate,String tEndDate)
			throws Exception {
		return excelDao.selectTTLByCPUSALE(beginDate, endDate, searchStr, conditions, isHq, tBeginDate, tEndDate);
	}

	@Override
	public List<HashMap<String, Object>> selectInfoByCPUACFO(String beginDate,
			String endDate, String searchStr, String conditions)
			throws Exception {
		return excelDao.selectInfoByCPUACFO(beginDate, endDate, searchStr,
				conditions);
	}

	@Override
	public List<HashMap<String, Object>> selectTotalByCPUACFO(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq,String tBeginDate,String tEndDate)
			throws Exception {
		return excelDao.selectTotalByCPUACFO(beginDate, endDate, searchStr, conditions, isHq, tBeginDate, tEndDate);
	}

	@Override
	public List<HashMap<String, Object>> selectModelByCPUACFO(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq)
			throws Exception {
		return excelDao.selectModelByCPUACFO(beginDate, endDate, searchStr,
				conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectModelTTLByCPUACFO(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectModelTTLByCPUACFO(beginDate, endDate, searchStr,
				conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectTTLByCPUACFO(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq,String tBeginDate,String tEndDate)
			throws Exception {
		return excelDao.selectTTLByCPUACFO(beginDate, endDate, searchStr, conditions, isHq, tBeginDate, tEndDate);
	}

	@Override
	public List<HashMap<String, Object>> selectStockByData(String spec,
			String searchStr, String conditions,boolean isHq,String beginDate, String endDate) throws Exception {
		return excelDao.selectStockByData(spec, searchStr, conditions, isHq, beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectDisplayByData(String spec,
			String searchStr, String conditions,boolean isHq,String beginDate, String endDate) throws Exception {
		return excelDao.selectDisplayByData(spec, searchStr, conditions,isHq, beginDate, endDate);
	}

	@Override
	public List<LinkedHashMap<String, Object>> selectSaleDataByDEALER(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectSaleDataByDEALER(beginDate, endDate, searchStr,
				conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectSaleTTLByDEALER(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectSaleTTLByDEALER(beginDate, endDate, searchStr,
				conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectDEALER(String searchStr,
			String conditions) throws Exception {
		return excelDao.selectDEALER(searchStr, conditions);
	}

	@Override
	public List<HashMap<String, Object>> selectCPUDisplayBYBRANCH(
			String searchStr, String conditions,boolean isHq,String beginDate, String endDate) throws Exception {
		return excelDao.selectCPUDisplayBYBRANCH(searchStr, conditions,isHq, beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectCPUInventoryBYBRANCH(
			String searchStr, String conditions,boolean isHq,String beginDate, String endDate) throws Exception {
		return excelDao.selectCPUInventoryBYBRANCH(searchStr, conditions,isHq, beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectCPUHeadDisplayBYBRANCH(
			String searchStr, String conditions,String beginDate, String endDate) throws Exception {
		return excelDao.selectCPUHeadDisplayBYBRANCH(searchStr, conditions, beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectCPUHeadInventoryBYBRANCH(
			String searchStr, String conditions,String beginDate, String endDate) throws Exception {
		return excelDao.selectCPUHeadInventoryBYBRANCH(searchStr, conditions, beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectCPUInfoByBRANCH(
			String searchStr, String conditions) throws Exception {
		return excelDao.selectCPUInfoByBRANCH(searchStr, conditions);
	}

	@Override
	public List<HashMap<String, Object>> selectSellDataBYBRANCH(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectSellDataBYBRANCH(beginDate, endDate, searchStr,
				conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectSellDataByTotal(
			String beginDate, String endDate, String searchStr,
			String conditions,boolean isHq) throws Exception {
		return excelDao.selectSellDataByTotal(beginDate, endDate, searchStr,
				conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectCPUDisplayByTotal(
			String searchStr, String conditions,String beginDate, String endDate) throws Exception {
		return excelDao.selectCPUDisplayByTotal(searchStr, conditions,beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectCPUInventoryByTotal(
			String searchStr, String conditions,String beginDate, String endDate) throws Exception {
		return excelDao.selectCPUInventoryByTotal(searchStr, conditions,beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectSellDataByTTL(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq)
			throws Exception {
		return excelDao.selectSellDataByTTL(beginDate, endDate, searchStr,
				conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectCPUDisplayByTTL(
			String searchStr, String conditions,boolean isHq,String beginDate, String endDate) throws Exception {
		return excelDao.selectCPUDisplayByTTL(searchStr, conditions,isHq,beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectCPUInventoryByTTL(
			String searchStr, String conditions,boolean isHq,String beginDate, String endDate) throws Exception {
		return excelDao.selectCPUInventoryByTTL(searchStr, conditions,isHq,beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectSellDataBySUM(String beginDate,
			String endDate, String searchStr, String conditions,boolean isHq)
			throws Exception {
		return excelDao.selectSellDataBySUM(beginDate, endDate, searchStr,
				conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectCPUDisplayBySUM(
			String searchStr, String conditions,String beginDate, String endDate) throws Exception {
		return excelDao.selectCPUDisplayBySUM(searchStr, conditions,beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectCPUInventoryBySUM(
			String searchStr, String conditions,String beginDate, String endDate) throws Exception {
		return excelDao.selectCPUInventoryBySUM(searchStr, conditions,beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectDataByAreaInfo(String searchStr,
			String conditions) throws Exception {
		return excelDao.selectDataByAreaInfo(searchStr, conditions);
	}

	@Override
	public List<HashMap<String, Object>> selectTargetBySalemanInfo(
			String searchStr, String conditions) throws Exception {
		return excelDao.selectTargetBySalemanInfo(searchStr, conditions);
	}

	@Override
	public List<HashMap<String, Object>> selectTargetByAcfoInfo(
			String searchStr, String conditions) throws Exception {
		return excelDao.selectTargetByAcfoInfo(searchStr, conditions);
	}

	@Override
	public List<HashMap<String, Object>> selectSelloutByDealerInfo(
			String searchStr, String conditions) throws Exception {
		return excelDao.selectSelloutByDealerInfo(searchStr, conditions);
	}

	@Override
	public String selectPartyByUser(String userId) throws Exception {
		return excelDao.selectPartyByUser(userId);
	}

	@Override
	public List<HashMap<String, Object>> selectRegionalHeadByParty(
			String searchStr, String conditions) throws Exception {
		return excelDao.selectRegionalHeadByParty(searchStr, conditions);
	}

	@Override
	public List<HashMap<String, Object>> selectAreaByUser(String searchStr, String conditions)
			throws Exception {
		return excelDao.selectAreaByUser(searchStr, conditions);
	}

	@Override
	public List<HashMap<String, Object>> selectCoreProductByCountry(
			String countryId,boolean isHq) throws Exception {
		return excelDao.selectCoreProductByCountry(countryId,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectCoreLine(String countryId)
			throws Exception {
		return excelDao.selectCoreLine(countryId);
	}

	@Override
	public List<HashMap<String, Object>> selectCoreSellData(String searchStr,
			String conditions, String countryId, String beginDate,
			String endDate,boolean isHq) {
		return excelDao.selectCoreSellData(searchStr, conditions, countryId,
				beginDate, endDate,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectCoreByTarget(String searchStr,
			String conditions, String beginDate,
			String endDate, String countryId,String tBeginDate,String tEndDate,boolean isHq) {
		return excelDao.selectCoreByTarget(searchStr, conditions, beginDate, endDate, countryId, tBeginDate, tEndDate,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectCoreSellTotal(String searchStr,
			String conditions, String countryId, String beginDate,
			String endDate,boolean isHq) {
		return excelDao.selectCoreSellTotal(searchStr, conditions, countryId,
				beginDate, endDate,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectCoreProductByStock(
			String countryId,boolean isHq,String beginDate, String endDate) throws Exception {
		return excelDao.selectCoreProductByStock(countryId,isHq,beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectCoreProductBySample(
			String countryId,boolean isHq,String beginDate, String endDate) throws Exception {
		return excelDao.selectCoreProductBySample(countryId,isHq,beginDate, endDate);
	}

	@Override
	public String selectCountryByUser(String userId) {
		return excelDao.selectCountryByUser(userId);
	}

	@Override
	public List<HashMap<String, Object>> selectSaleDataByshopByAc(
			String beginDate, String endDate, String searchStr,
			String conditions, boolean isHq) {
		return excelDao.selectSaleDataByshopByAc(beginDate, endDate, searchStr, conditions, isHq);
	}

	@Override
	public List<Excel> selectSaleDataBySumByAc(String beginDate,
			String endDate, String searchStr, String conditions, boolean isHq) {
		return excelDao.selectSaleDataBySumByAc(beginDate, endDate, searchStr, conditions, isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectTargetBySalemanByAc(
			String beginDate, String endDate, String searchStr,
			String conditions, boolean isHq, String tBeginDate, String tEndDate)
			throws Exception {
		return excelDao.selectTargetBySalemanByAc(beginDate, endDate, searchStr, conditions, isHq, tBeginDate, tEndDate);
	}

	@Override
	public List<HashMap<String, Object>> selectTargetByAcfoByAc(
			String beginDate, String endDate, String searchStr,
			String conditions, boolean isHq, String tBeginDate, String tEndDate)
			throws Exception {
		return excelDao.selectTargetByAcfoByAc(beginDate, endDate, searchStr, conditions, isHq, tBeginDate, tEndDate);
	}

	@Override
	public List<HashMap<String, Object>> selectModelBySpecByAc(
			String beginDate, String endDate, String spec, String searchStr,
			String conditions, boolean isHq) throws Exception {
		return excelDao.selectModelBySpecByAc(beginDate, endDate, spec, searchStr, conditions, isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectStockBymodelByAc(String spec,
			String searchStr, String conditions, boolean isHq,String beginDate, String endDate) throws Exception {
		return excelDao.selectStockBymodelByAc(spec, searchStr, conditions, isHq,beginDate,endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectDisplayBymodelByAc(String spec,
			String searchStr, String conditions, boolean isHq,String beginDate, String endDate) throws Exception {
		return excelDao.selectDisplayBymodelByAc(spec, searchStr, conditions, isHq,beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectModelListBySpecByAc(String spec,
			String beginDate, String endDate, String searchStr,
			String conditions, boolean isHq) throws Exception {
		return excelDao.selectModelListBySpecByAc(spec, beginDate, endDate, searchStr, conditions, isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectModelBySpecTotalByAc(
			String beginDate, String endDate, String searchStr,
			String conditions, boolean isHq) throws Exception {
		return excelDao.selectModelBySpecTotalByAc(beginDate, endDate, searchStr, conditions, isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectStockByDataByAc(String spec,
			String searchStr, String conditions, boolean isHq,String beginDate, String endDate) throws Exception {
		return excelDao.selectStockByDataByAc(spec, searchStr, conditions, isHq,beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectDisplayByDataByAc(String spec,
			String searchStr, String conditions, boolean isHq,String beginDate, String endDate) throws Exception {
		return excelDao.selectDisplayByDataByAc(spec, searchStr, conditions, isHq,beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectStockByTotalByAc(
			String searchStr, String conditions,String beginDate, String endDate) throws Exception {
		return excelDao.selectStockByTotalByAc(searchStr, conditions,beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectDisPlayByTotalByAc(
			String searchStr, String conditions,String beginDate, String endDate) throws Exception {
		return excelDao.selectDisPlayByTotalByAc(searchStr, conditions,beginDate, endDate);
	}

	@Override
	public List<HashMap<String, Object>> selectFpsNameByShopByAc(
			String beginDate, String endDate, String searchStr,
			String conditions) throws Exception {
		return excelDao.selectFpsNameByShopByAc(beginDate, endDate, searchStr, conditions);
	}

	@Override
	public List<LinkedHashMap<String, Object>> selectSaleDataBySizeByAc(
			String beginSize, String endSize, String beginDate, String endDate,
			String searchStr, String conditions, boolean isHq) throws Exception {
		return excelDao.selectSaleDataBySizeByAc(beginDate, endDate, searchStr, conditions, isHq);
	}

	@Override
	public List<LinkedHashMap<String, Object>> selectSaleTotalBySizeByAc(
			String beginDate, String endDate, String searchStr,
			String conditions, boolean isHq) throws Exception {
		return excelDao.selectSaleTotalBySizeByAc(beginDate, endDate, searchStr, conditions, isHq);
	}

	@Override
	public List<LinkedHashMap<String, Object>> selectQtyTotalBySpecByAc(
			String spec, String beginDate, String endDate, String searchStr,
			String conditions, boolean isHq) throws Exception {
		return excelDao.selectQtyTotalBySpecByAc(spec, beginDate, endDate, searchStr, conditions, isHq);
	}

	@Override
	public List<LinkedHashMap<String, Object>> selectSaleDataByDEALERByAc(
			String beginDate, String endDate, String searchStr,
			String conditions, boolean isHq) {
		return excelDao.selectSaleDataByDEALERByAc(beginDate, endDate, searchStr, conditions, isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectSaleTTLByDEALERByAc(
			String beginDate, String endDate, String searchStr,
			String conditions, boolean isHq) {
		return excelDao.selectSaleTTLByDEALERByAc(beginDate, endDate, searchStr, conditions, isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectSelloutByDealerByAc(
			String beginDate, String endDate, String searchStr,
			String conditions, boolean isHq, String tBeginDate, String tEndDate)
			throws Exception {
		return excelDao.selectSelloutByDealerByAc(beginDate, endDate, searchStr, conditions, isHq, tBeginDate, tEndDate);
	}

	@Override
	public List<LinkedHashMap<String, Object>> selectQtyTotalBySpecTypeByAc(
			String spec, String beginDate, String endDate, String searchStr,
			String conditions, boolean isHq) throws Exception {
		return excelDao.selectQtyTotalBySpecTypeByAc(spec, beginDate, endDate, searchStr, conditions, isHq);
	}
	

}
