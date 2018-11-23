package cn.tcl.platform.training.service;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public interface LearnStatisticsService {
	//查询统计汇总
	public Map<String,Object> selectLearnList(int start,int limit,String order,
			String sort,String conditions,String levelOneTypeId,String levelTwoTypeId,
			String levelThreeTypeId,String regionId,String countryId,String startdate,String enddate) throws Exception;
	//查询俄罗斯统计汇总
	public Map<String,Object> selectRussiaLearnList(int start,int limit,String order,
			String sort,String conditions,String levelOneTypeId,String levelTwoTypeId,
			String levelThreeTypeId,String regionId,String countryId,String startdate,String enddate) throws Exception;
	//导出学习统计
	public XSSFWorkbook exportLearn(String conditions,String[] excelHeader,String title,String levelOneTypeId,String 
			levelTwoTypeId,String levelThreeTypeId,String regionId,
			String countryId,String startdate,String enddate) throws Exception;
	
	//导出俄罗斯学习统计
	public XSSFWorkbook exportRusLearn(String conditions,String[] excelHeader,String title,String levelOneTypeId,String 
			levelTwoTypeId,String levelThreeTypeId,String regionId,
			String countryId,String startdate,String enddate) throws Exception;
}
