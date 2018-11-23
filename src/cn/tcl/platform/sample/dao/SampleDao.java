package cn.tcl.platform.sample.dao;

import java.util.List;
import java.util.Map;

import cn.tcl.platform.sample.vo.Sample;

public interface SampleDao {
	
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
	
}
