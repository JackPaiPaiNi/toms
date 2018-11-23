package cn.tcl.platform.BDExcel.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import cn.tcl.platform.excel.vo.Excel;
import net.sf.json.JSONObject;

public interface IBDExcelService {
	public void commonByHq(SXSSFWorkbook wb, LinkedList<HashMap<String, Object>> list, String wbName, String tBeginDate,
			String tEndDate, String searchStr, String conditions);

	public void comparativeByHq(SXSSFWorkbook wb, String partyName, String beginDateOne, String endDateOne,
			String tBeginDate, String tEndDate, String searchStr, String conditions) throws Exception;

	public void monthYTDsellout(SXSSFWorkbook wb, String beginDateOne, String endDateOne, String searchStr,
			String conditions) throws Exception;

	public void commonByHqQua(String what,SXSSFWorkbook wb, LinkedList<HashMap<String, Object>> list, String wbName,
			String tBeginDate, String tEndDate, String searchStr, String conditions);
	public void commonByHqQuaCus(String what,SXSSFWorkbook wb, LinkedList<HashMap<String, Object>> list, String wbName,
			String tBeginDate, String tEndDate, String searchStr, String conditions);
	
	public void comparativeByHqQua(String what,SXSSFWorkbook wb, String partyName, String beginDateOne, String endDateOne,
			String tBeginDate, String tEndDate, String searchStr, String conditions) throws Exception;

	public void monthYTDselloutQua(String what,SXSSFWorkbook wb, String beginDateOne, String endDateOne, String searchStr,
			String conditions) throws Exception;
	public void monthYTDselloutQuaS(String what,SXSSFWorkbook wb, String beginDateOne, String endDateOne, String searchStr,
			String conditions) throws Exception;

	public void common(String what,SXSSFWorkbook wb, LinkedList<HashMap<String, Object>> list, String wbName, String beginDate,
			String endDate, String searchStr, String conditions) ;

	
	public String selectCountryByUser(String userId);

	public String selectPartyByUser(String userId) throws Exception;
	public String readExcelByTarget(File file, String fileName,String what) throws IOException;
	public String readBDSCTarget(File file) throws IOException;
	
	public JSONObject selectBDSCTarger(String beginDate,String endDate,String searchStr,String conditions,String type);
	public JSONObject selectBDSCTargerYear(String date,String searchStr,String conditions,String type);

	
}
