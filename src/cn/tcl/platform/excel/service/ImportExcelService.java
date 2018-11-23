package cn.tcl.platform.excel.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.tcl.platform.excel.vo.Excel;
import cn.tcl.platform.excel.vo.ImportExcel;

public interface ImportExcelService {


	
	public String readExcel(File file, String fileName,String what) throws IOException;
	
	public String read2007Excel(File file,String what) throws IOException;
	public String read2007ExcelByCountry(File file,String what) throws IOException;
	
	public String read2007ExcelByPK(File file) throws IOException;


	public void deleteCore(String countryId)throws Exception;
	public void insertCore(String countryId,String line)throws Exception;

	public List<HashMap<String, Object>> selectCore(String countryId) throws Exception;
	
	public List<HashMap<String, Object>> selectAllCore() throws Exception;
	
	public XSSFWorkbook exporExcel(String spec,String conditions,String[] excelHeader,String title) throws Exception;
	
	public XSSFWorkbook exporExcelByCustomer(String spec,String conditions,String[] excelHeader,String title) throws Exception;

	
	public XSSFWorkbook exporExcelByPK(String spec,String conditions,String[] excelHeader,String title) throws Exception;
	public XSSFWorkbook exporExcelByCountry(String spec,String conditions,String[] excelHeader,String title) throws Exception;
	public String selectSoType(String country) throws Exception;

	
	
}
