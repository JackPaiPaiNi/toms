package cn.tcl.platform.statable.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public interface IStaTableService {
	
	public List<HashMap<String,Object>> monthlySalesPerformance (Map<String,Object> map)throws Exception;

	public List<HashMap<String,Object>> regionalGrowthPerformance (Map<String,Object> map)throws Exception;
	
	public List<HashMap<String,Object>> regionalGrowthPerformanceByPartyId (Map<String,Object> map)throws Exception;
	
	public JSONObject PSeriesSalesStatus (Map<String,Object> map)throws Exception;
	
	public JSONObject queryBigSaleInfo(Map<String,Object> map) throws Exception;
	
	/**
	 * 每个国家时间段销售数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> queryStateTimeSalesBycountry (Map<String,Object> map)throws Exception;

	
}
