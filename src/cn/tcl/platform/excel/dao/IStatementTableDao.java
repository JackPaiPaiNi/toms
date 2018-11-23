package cn.tcl.platform.excel.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface IStatementTableDao {
	public List<HashMap<String, Object>> selectSelloutByDealer(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectSelloutByDealerInfo(Map<String,Object> whereMap);

	
}
