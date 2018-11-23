package cn.tcl.platform.excel.service;



import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import net.sf.json.JSONObject;


public interface ISoTableService {
	public JSONObject selectGrandTTL(Map<String, Object> whereMap);
	public JSONObject selectYearBySale(Map<String, Object> whereMap);
	public JSONObject selectMonthByACFO (Map<String, Object> whereMap);
	public JSONObject selectGrowthBySalesman  (Map<String, Object> whereMap) throws Exception;
	public JSONObject selectMonthByBigRegional(Map<String, Object> whereMap);

	public JSONObject selectMonthByDEALER(Map<String, Object> whereMap);
	
	public JSONObject selectSpecPro(Map<String, Object> whereMap);


	public JSONObject selectModelByMonth(Map<String, Object> whereMap);
	public List<HashMap<String, Object>> selectPartyCus(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectXCPLine(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectXCPModel(Map<String,Object> whereMap);
	public List<HashMap<String, Object>> selectCurvedModel(Map<String,Object> whereMap);

	public JSONObject selectDataByAreaChain(Map<String, Object> whereMap);

	public JSONObject selectGrowthByCountry(Map<String, Object> whereMap);
	public JSONObject selectSalemanDataByChain(Map<String, Object> whereMap);
	public JSONObject selectAcfoDataByChain(Map<String, Object> whereMap);
	public JSONObject selectCountryDataByChain(Map<String, Object> whereMap);
	public JSONObject selectCountryBigByYear(Map<String, Object> whereMap);

	public  XSSFWorkbook read2007Excel(File file,
			String uploadExcelFileName);

	
	
	public JSONObject selectMonthCountryTotal(Map<String,Object> whereMap);
	public JSONObject selectAreaDataByMonth (Map<String, Object> whereMap);
	
	public JSONObject selectMonthCountryXCP(Map<String,Object> whereMap);

	
	public JSONObject selectSalemanDataByMonth(Map<String,Object> whereMap);
	public JSONObject selectAcfoDataByMonth(Map<String,Object> whereMap);
	
	public JSONObject selectHQDataByYear(Map<String,Object> whereMap);
	public JSONObject selectHQDataByHalf(Map<String,Object> whereMap);
	public JSONObject selectHQDataByQuarter(Map<String,Object> whereMap);
	
	
	
	public JSONObject selectHQChainDataByYear(Map<String,Object> whereMap);
	public JSONObject selectHQChainDataByHalf(Map<String,Object> whereMap);
	public JSONObject selectHQChainDataByQuarter(Map<String,Object> whereMap);


}
