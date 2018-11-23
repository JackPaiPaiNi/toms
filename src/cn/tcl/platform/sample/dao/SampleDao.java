package cn.tcl.platform.sample.dao;

import java.util.List;
import java.util.Map;

import cn.tcl.platform.sample.vo.Sample;

public interface SampleDao {
	
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
	
}
