package cn.tcl.platform.sample.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.tcl.platform.sample.vo.Sample;

public interface ISampleService {
	
	/**
	 * ��ѯ��Դ��
	 * @param sampleMap
	 * @return
	 * @throws Exception
	 */
	public List<Sample> selectSampleList(Map<String,Object> sampleMap) throws Exception;
	
	/**
	 * ��ѯ��Դ����
	 * @param sampleMap
	 * @return
	 * @throws Exception
	 */
	public Integer selectSampleListCount(Map<String,Object> sampleMap) throws Exception;
	
	/**
	 * ��ѯ�û������ŵ�
	 * @return
	 * @throws Exception
	 */
	public List<Sample> selectShopId(String userId) throws Exception;
	
	
	/**
	 * ��������
	 * @param conditions
	 * @param excelHeader
	 * @param title
	 * @param samMap 
	 * @return
	 * @throws Exception
	 */
	HSSFWorkbook exporSamples(String conditions, String[] excelHeader, String title, Map<String, Object> samMap) throws Exception;


}
