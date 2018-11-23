package cn.tcl.platform.sample.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.tcl.platform.sample.vo.Sample;

public interface ISampleService {
	
	/**
	 * 查询资源表
	 * @param sampleMap
	 * @return
	 * @throws Exception
	 */
	public List<Sample> selectSampleList(Map<String,Object> sampleMap) throws Exception;
	
	/**
	 * 查询资源个数
	 * @param sampleMap
	 * @return
	 * @throws Exception
	 */
	public Integer selectSampleListCount(Map<String,Object> sampleMap) throws Exception;
	
	/**
	 * 查询用户所在门店
	 * @return
	 * @throws Exception
	 */
	public List<Sample> selectShopId(String userId) throws Exception;
	
	
	/**
	 * 导出数据
	 * @param conditions
	 * @param excelHeader
	 * @param title
	 * @param samMap 
	 * @return
	 * @throws Exception
	 */
	HSSFWorkbook exporSamples(String conditions, String[] excelHeader, String title, Map<String, Object> samMap) throws Exception;


}
