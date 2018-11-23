package cn.tcl.platform.performance.service;

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

public interface IPerformanceService {

	public String readExcelByTarget(File file, String fileName,String what) throws IOException;
	public String readPCTarget(File file) throws IOException;
	
	public JSONObject selectBDSCTarger(String beginDate,String endDate,String searchStr,String conditions,String type);
	
	public Map<String, Object> selectPCTarget(int start,int  limit,String searchStr,String order,String sort,String conditions) throws Exception;

}
