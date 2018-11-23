package cn.tcl.platform.statable.dao;

import java.util.List;
import java.util.Map;

import cn.tcl.platform.statable.vo.StaHQTable;


public interface IStaHQTableDao {
	
	/**
	 * 每个国家时间段销售数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<StaHQTable> queryStateTimeSalesBycountry (Map<String,Object> map)throws Exception;
	
	/**
	 * 每个国家时间段销售数据(加上系数)
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<StaHQTable> queryStateTimeSalesBycountryReduction (Map<String,Object> map)throws Exception;
	
	/**
	 * 有销量的国家
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<StaHQTable> querySaleCountry (Map<String,Object> map)throws Exception;
	
}
