package cn.tcl.platform.examination.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.examination.vo.Analysis;
import cn.tcl.platform.examination.vo.Examination;
import cn.tcl.platform.examination.vo.Paper;

public interface IPaperDao {
	
	/**
	 * 查询试卷数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Paper> selectPaperData (Map<String,Object> map) throws Exception;
	
	/**
	 * 查询试卷数据数量
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer selectPaperDataCount (Map<String,Object> map) throws Exception;
	
	/**
	 * 增加试卷数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void insertPaperData (Paper p) throws Exception;
	
	/**
	 * 修改试卷结束时间
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void updatePaperEndDate (Paper p) throws Exception;
	
	
	/**
	 * 修改试卷数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void updatePaperData (Paper p) throws Exception;
	
	/**
	 * 提前结束
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void updateEndTime (Paper p) throws Exception;
	
	/**
	 * 删除试卷数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void deletePaperData (Integer id) throws Exception;
	
	/**
	 * 删除试卷题目映射
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void deletePaperTopicData (String id) throws Exception;
	
	/**
	 * 根据试卷id查询试卷
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer isSpecifiedTime (Map<String,Object> map) throws Exception;
	
	/**
	 * 根据试卷id查询试卷
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer isSpecifiedEndTime (Map<String,Object> map) throws Exception;
	
	/**
	 * 随机抽取题目
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Paper selectTopicLIMIT (
			@Param(value="sLimit") Integer sLimit,
			@Param(value="type") String  type,
			@Param(value="countryId") String countryId,
			@Param(value="conditions") String conditions
			) throws Exception;
	
	/**
	 * 题目类型数量
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer selectTopicTypeCount (
			@Param(value="type") String type,
			@Param(value="countryId") String countryId,
			@Param(value="conditions") String conditions
			) throws Exception;
	
	/**
	 * 题目类型编号
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer selectPaperId () throws Exception;
	
	/**
	 * 生成试卷+题目
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void insertPaperTopicData (List<Paper> list) throws Exception;
	
	/**
	 * 根据试卷查询题目
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Examination> selectTopicByPaperId (@Param(value="id") String id) throws Exception;
	
	/**
	 * 根据试卷生成二维码试题
	 */
	public void insertQRcode(Paper p) throws Exception;
	
	/**
	 * 删除试题同时删除二维码映射表对应的p_id
	 */
	public void deleteQRcode(Integer p_id) throws Exception;
	
	/**
	 * 根据考试id删除消息提醒
	 */
	public void deletePaperMsg(Integer id) throws Exception;
	
	/**
	 * 考试情况分析
	 */
	public List<Analysis> selectExaminationSituation(@Param(value="conditions") String conditions,
			@Param(value="start") int start,
			@Param(value="limit") int limit) throws Exception;
	
	/**
	 * 考试情况分析
	 */
	public List<Analysis> selectExaminationExport(@Param(value="conditions") String conditions,
			@Param(value="start") int start,
			@Param(value="limit") int limit) throws Exception;
	
	/**
	 * 考试情况Country
	 */
	public Integer selectExaminationSituationCount(@Param(value="conditions") String conditions) throws Exception;
	
	/**
	 * 考试情况Country
	 */
	public Integer selectRusExaminationExportCount(@Param(value="conditions") String conditions) throws Exception;
	
	/**
	 * 根据类型、国家查询指定题目
	 */
	public List<Examination> selectExamByTypeAndCountry(@Param(value="countryId") String countryId,
												  @Param(value="conditions") String conditions) throws Exception;

	/**
	 * 用户是否已经考完试
	 * @param userId
	 * @param paperId
	 * @return
	 * @throws Exception
	 */
	public Integer selectUserIsExam ( @Param(value="userId") String userId,
			@Param(value="paperId") String paperId) throws Exception;
	
	/**
	 * 俄罗斯考试情况分析
	 */
	public List<Analysis> selectRusExaminationSituation(@Param(value="conditions") String conditions,
			@Param(value="start") int start,
			@Param(value="limit") int limit) throws Exception;
	
	/**
	 * 俄罗斯考试情况分析
	 */
	public List<Analysis> selectRusExaminationExport(@Param(value="conditions") String conditions,
			@Param(value="start") int start,
			@Param(value="limit") int limit) throws Exception;
}
