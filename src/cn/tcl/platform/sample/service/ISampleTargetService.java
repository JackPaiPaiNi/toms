package cn.tcl.platform.sample.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.tcl.platform.sample.vo.SampleTarget;
import cn.tcl.platform.shop.vo.ShopParty;

public interface ISampleTargetService {
	
	
	public List<SampleTarget> selectSampleTargetList(Map<String,Object> sampleMap) throws Exception;
	
	
	public Integer selectSampleTargetListCount(Map<String,Object> sampleMap) throws Exception;
	
	public String readExcel(File file, String fileName) throws IOException;

	public String read2007Excel(File file) throws IOException;

	public XSSFWorkbook exporExcel(String conditions,String[] excelHeader,String title) throws Exception;

	public List<SampleTarget> selectShopByParty(String countryId,String partyId) throws Exception;
	public List<SampleTarget> selectModelByCountry(String countryId) throws Exception;
	
	public int updateSampleTargetById(SampleTarget sampleTarget) throws Exception;
	public int deleteSampleTarget(int id) throws Exception;
	
	public List<SampleTarget> selectSampleAchList(Map<String,Object> sampleMap) throws Exception;
	public Integer selectSampleAchListCount(Map<String,Object> sampleMap) throws Exception;
	
	public XSSFWorkbook exporTargetAch(Map<String,Object> sampleMap,String[] excelHeader,String title) throws Exception;
	
	
	public List<SampleTarget> selectSampleAchListByLine(Map<String,Object> sampleMap) throws Exception;
	public Integer selectSampleAchListCountByLine(Map<String,Object> sampleMap) throws Exception;
	
	public List<SampleTarget> selectSampleTargetListByLine(Map<String,Object> sampleMap) throws Exception;
	public Integer selectSampleTargetListCountByLine(Map<String,Object> sampleMap) throws Exception;
	
	public XSSFWorkbook exporSampleTarget(Map<String,Object> sampleMap,String[] excelHeader,String title) throws Exception;

	
	public List<SampleTarget> selectSampleTargetSumListByLine(Map<String,Object> sampleMap) throws Exception;
	public Integer selectSampleTargetSumListCountByLine(Map<String,Object> sampleMap) throws Exception;
	
	
	public List<SampleTarget> selectSampleAchSumListByLine(Map<String,Object> sampleMap) throws Exception;
	public Integer selectSampleAchSumListCountByLine(Map<String,Object> sampleMap) throws Exception;
	
}
