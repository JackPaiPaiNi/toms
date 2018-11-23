package cn.tcl.platform.excel.service;



import java.util.Map;

import net.sf.json.JSONObject;


public interface IStatementTableService {
	public JSONObject selectDealerSellout(Map<String, Object> whereMap);


}
