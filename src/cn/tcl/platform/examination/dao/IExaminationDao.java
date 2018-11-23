package cn.tcl.platform.examination.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.examination.vo.Examination;

public interface IExaminationDao {
	
	/**
	 * 题目数据
	 * @return
	 */
	public List<Examination> selectExamQuestions (Map<String,Object> map) throws Exception;
	
	/**
	 * 根据编号查询题目数据
	 * @return
	 */
	public Examination selectExamQuestionsById (@Param(value="id") Integer  id) throws Exception;
	
	/**
	 * 题目个数
	 * @return
	 */
	public Integer selectExamQuestionsCount (Map<String,Object> map) throws Exception;
	
	/**
	 * 刪除题目
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void deleteExamQuestions (@Param(value="id") Integer  id) throws Exception;
	
	/**
	 * 刪除题目正确答案
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void deleteTopicAnswer (@Param(value="id") Integer  id) throws Exception;
	
	/**
	 *根据题目ID查询正确答案
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<Examination> selectCorrectAnswerById (@Param(value="id") String  id) throws Exception;
	
	/**
	 *根据id查询子类类别
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<Examination> selectSubclassCategoriesById (@Param(value="id") Integer  id) throws Exception;
	
	/**
	 *增加题目
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void insertExamQuestions (Examination ex) throws Exception;
	
	/**
	 *增加题目时返回项目编号
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int selectExamQuestionsId () throws Exception;
	
	/**
	 *导入正确答案
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void insertCorrectAnswer (List<Examination> corAnswerList) throws Exception;
	
	/**
	 *修改题目
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void updateExamQuestionsById (Examination corAnswerList) throws Exception;
	
	/**
	 *校验题目是否已有试卷使用
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Integer selectUseTheTitleById (String id) throws Exception;
	
	/**
	 *查询国家
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Examination> selectCountry (@Param(value="userId") String userId) throws Exception;
	
	/**
	 * 题目类型是否存在
	 * @param eType
	 * @return
	 * @throws Exception
	 */
	public String selectStypeIsExist (@Param(value="eType") String eType) throws Exception;
	
	/**
	 * 题目是否存在
	 * @param equs
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Integer selectTitleByEQAndType (Examination ex) throws Exception;
	
	/**
	 * 大类是否存在
	 * @param eName
	 * @return
	 * @throws Exception
	 */
	public Integer selectTypeCount (@Param(value="blankName") String blankName,
									@Param(value="underlineName") String underlineName) throws Exception;
	
	/**
	 * 根据类型名称获取类型编号
	 * @param eName
	 * @return
	 * @throws Exception
	 */
	public String selectTypeIdByEname (@Param(value="blankName") String blankName,
									   @Param(value="underlineName") String underlineName) throws Exception;
	
	/**
	 * 根据类型名称获取类型编号
	 * @param eName
	 * @return
	 * @throws Exception
	 */
	public String selectTypeIdByEnameByparentId (
			@Param(value="blankName") String blankName,
			@Param(value="underlineName") String underlineName,
			@Param(value="parentId") String parentId) throws Exception;
	
	/**
	 * 根据区域名称获取区域ID
	 * @param partyName
	 * @return
	 * @throws Exception
	 */
	public String selectPartyIdByPartyName (@Param(value="partyName") String partyName) throws Exception;

}
