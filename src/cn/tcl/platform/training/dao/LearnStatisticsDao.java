package cn.tcl.platform.training.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.training.vo.LearnStatistics;

public interface LearnStatisticsDao {
	/**
	 * 学习情况信息列表
	 * @param start
	 * @param limit
	 * @param order
	 * @param sort
	 * @return
	 * @throws Exception
	 */
	public List<LearnStatistics> selectLearnList(
			@Param("start") int start, 
			@Param("limit") int limit,
			@Param("order")  String order,
			@Param("sort") String sort,
			@Param("conditions") String conditions,
			@Param("levelOneTypeId") String levelOneTypeId,
			@Param("levelTwoTypeId") String levelTwoTypeId,
			@Param("levelThreeTypeId") String levelThreeTypeId,
			@Param("regionId") String regionId,
			@Param("countryId") String countryId,
			@Param("startdate") String startdate,
			@Param("enddate") String enddate
			) throws Exception;
	
	/**
	 * 统计学习总数
	 * @param start
	 * @param limit
	 * @param order
	 * @param sort
	 * @return
	 * @throws Exception
	 */
	public int countLearn(
			@Param("start") int start,
			@Param("limit") int limit,
			@Param("order") String order,
			@Param("sort") String sort,
			@Param("conditions") String conditions,
			@Param("levelOneTypeId") String levelOneTypeId,
			@Param("levelTwoTypeId") String levelTwoTypeId,
			@Param("levelThreeTypeId") String levelThreeTypeId,
			@Param("regionId")String regionId,
			@Param("countryId")String countryId,
			@Param("startdate") String startdate,
			@Param("enddate") String enddate
			) throws Exception;
	/**
	 * 根据条件导出学习摘要
	 * @param condition
	 * @param levelOneTypeId
	 * @param levelTwoTypeId
	 * @param levelThreeTypeId
	 * @param regionId
	 * @param countryId
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws Exception
	 */
	public List<LearnStatistics> searchExportLearn(
			@Param("conditions")String conditions,
			@Param("levelOneTypeId") String levelOneTypeId,
			@Param("levelTwoTypeId") String levelTwoTypeId,
			@Param("levelThreeTypeId") String levelThreeTypeId,
			@Param("regionId")String regionId,
			@Param("countryId")String countryId,
			@Param("startdate") String startdate,
			@Param("enddate") String enddate			
			) throws Exception;
	
	/**
	 * 查询俄罗斯学习统计汇总
	 * @param start
	 * @param limit
	 * @param order
	 * @param sort
	 * @param conditions
	 * @param levelOneTypeId
	 * @param levelTwoTypeId
	 * @param levelThreeTypeId
	 * @param regionId
	 * @param countryId
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws Exception
	 */
	public List<LearnStatistics> selectRussiaLearnList(
			@Param("start") int start, 
			@Param("limit") int limit,
			@Param("order")  String order,
			@Param("sort") String sort,
			@Param("conditions") String conditions,
			@Param("levelOneTypeId") String levelOneTypeId,
			@Param("levelTwoTypeId") String levelTwoTypeId,
			@Param("levelThreeTypeId") String levelThreeTypeId,
			@Param("regionId") String regionId,
			@Param("countryId") String countryId,
			@Param("startdate") String startdate,
			@Param("enddate") String enddate
			) throws Exception;
	
	/**
	 * 导出俄罗斯学习统计
	 * @param conditions
	 * @param levelOneTypeId
	 * @param levelTwoTypeId
	 * @param levelThreeTypeId
	 * @param regionId
	 * @param countryId
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws Exception
	 */
	public List<LearnStatistics> searchRusExportLearn(
			@Param("conditions")String conditions,
			@Param("levelOneTypeId") String levelOneTypeId,
			@Param("levelTwoTypeId") String levelTwoTypeId,
			@Param("levelThreeTypeId") String levelThreeTypeId,
			@Param("regionId")String regionId,
			@Param("countryId")String countryId,
			@Param("startdate") String startdate,
			@Param("enddate") String enddate			
			) throws Exception;
}
