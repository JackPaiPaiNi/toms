package cn.tcl.platform.BDExcel.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.tcl.platform.BDExcel.vo.BDTable;
import cn.tcl.platform.statable.vo.StaHQTable;



public interface IBDSCSoTableDao {
	public List<HashMap<String, Object>> selectModel(Map<String,Object> whereMap);
	public List<BDTable>   selectDatas(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectModelListBySpec(Map<String,Object> whereMap);
	
	public List<HashMap<String, Object>> selectModelBySpec(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectStockBymodel(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectDisplayBymodel(Map<String,Object> whereMap);
	
	
	public List<HashMap<String, Object>> selectSalerDatas(Map<String,Object> whereMap);
	
	public List<HashMap<String, Object>> selectSaleDataByshop(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectSaleDataByshopNoTarget(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectStockByData(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectDisplayByData(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectModelTotalBySpec(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectModelBySpecTotal(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectStockByTotal(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectDisPlayByTotal(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectDataByArea(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectRegionalHeadByParty(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectTargetByArea(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectTargetByAcfo(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectAreaByUser(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectFpsNameByShop(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectTargetBySalemanInfo(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectTargetBySaleman(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectPartyNameByuser(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectBigReg(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectModelListBySpecParty(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectModelByMonth(Map<String,Object> whereMap);

	public List<HashMap<String, Object>> selectPartyCus(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectXCPLine(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectGrowthByCountry(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectFpsNameByParty(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectDataByAreaChain(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectSalemanDataByChain(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectAcfoDataByChain(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectXCPModel(Map<String,Object> whereMap);

	public List<HashMap<String, Object>> 	selectCurvedModel(Map<String,Object> whereMap);
	
	public List<HashMap<String, Object>> selectSysByMis(Map<String,Object> whereMap);

	public List<HashMap<String, Object>> selectMonthCountryTotal(Map<String,Object> whereMap);
	
	public List<HashMap<String, Object>> selectAreaDataByMonth(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectXCPModelByMonth(Map<String,Object> whereMap);
	
	public List<HashMap<String, Object>> selectSalemanDataByMonth(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectAcfoDataByMonth(Map<String,Object> whereMap);
	
	public List<HashMap<String, Object>> selectCountryDataByHQ(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectTargetDataByHQ(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectTargetDataByHQUD(Map<String,Object> whereMap);

	
	public List<HashMap<String, Object>> selectYearTargetDataByHQUD(Map<String,Object> whereMap);

	public List<HashMap<String, Object>> selectYearTargetDataByHQ(Map<String,Object> whereMap);

	
	public List<BDTable> queryStateTimeSalesBycountry (Map<String,Object> map)throws Exception;
	
	/**
	 * 有销量的国家
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<BDTable> querySaleCountry (Map<String,Object> map)throws Exception;

}
