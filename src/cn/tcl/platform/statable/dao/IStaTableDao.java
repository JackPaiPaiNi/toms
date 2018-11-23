package cn.tcl.platform.statable.dao;

import java.util.List;
import java.util.Map;

import cn.tcl.platform.statable.vo.StaTable;


public interface IStaTableDao {
	
	public List<StaTable> monthlySalesPerformance (Map<String,Object> map)throws Exception;
	public List<StaTable> monthlySalesPerformanceCheck (Map<String,Object> map)throws Exception;
	
	public List<StaTable> regionalGrowthPerformance (Map<String,Object> map)throws Exception;
	public List<StaTable> regionalGrowthPerformanceCheck (Map<String,Object> map)throws Exception;
	
	public List<StaTable> getTheFirstLevel (Map<String,Object> map)throws Exception;
	
	public List<StaTable> regionalGrowthPerformanceByPartyId (Map<String,Object> map)throws Exception;
	public List<StaTable> regionalGrowthPerformanceByPartyIdCheck (Map<String,Object> map)throws Exception;
	
	public List<StaTable> PSeriesSalesStatus (Map<String,Object> map)throws Exception;
	public List<StaTable> PSeriesSalesStatusCheck (Map<String,Object> map)throws Exception;
	
	public List<StaTable> getTheFirstLevels (Map<String,Object> map)throws Exception;
	
	public List<StaTable> querysaleInfoByWhere (Map<String,Object> map)throws Exception;
	public List<StaTable> querysaleInfoByWhereCheck (Map<String,Object> map)throws Exception;
	
	public List<StaTable> querysaleInfoByMonth (Map<String,Object> map)throws Exception;
	public List<StaTable> querysaleInfoByMonthCheck (Map<String,Object> map)throws Exception;
	
	public List<StaTable> querysaleInfoBySeries (Map<String,Object> map)throws Exception;
	public List<StaTable> querysaleInfoBySeriesCheck (Map<String,Object> map)throws Exception;
}
