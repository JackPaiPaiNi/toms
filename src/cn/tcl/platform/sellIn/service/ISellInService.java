package cn.tcl.platform.sellIn.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.tcl.platform.sample.vo.SampleTarget;
import cn.tcl.platform.sellIn.vo.SellIn;




public interface ISellInService {
	public XSSFWorkbook exportExcel(String conditions,String[] excelHeader,String title) throws Exception;
	public String readExcel(File file, String fileName) throws IOException;

	public String read2007Excel(File file) throws IOException;
	public JSONObject selectSellInByHq(Map<String,Object> sellInMap) throws Exception;
	public List<SampleTarget> selectModelByHq(String countryId) throws Exception;

	
	public String readExcelByReturn(File file, String fileName) throws IOException;

	public String read2007ExcelByReturn(File file) throws IOException;
	
	public Map<String, Object> selectSellInByTable(Map<String,Object> sellInMap) throws Exception;
	public Map<String, Object> selectReturn(Map<String,Object> sellInMap) throws Exception;

}
