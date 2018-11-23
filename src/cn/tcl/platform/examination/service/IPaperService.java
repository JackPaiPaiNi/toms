package cn.tcl.platform.examination.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.tcl.platform.examination.vo.Examination;
import cn.tcl.platform.examination.vo.Paper;
import cn.tcl.platform.message.vo.Message;

public interface IPaperService {
	
	public Map<String,Object> selectPaperData (Map<String,Object> map) throws Exception;
	
	public void insertPaperData (Paper p) throws Exception;
	
	public boolean updatePaperData (Paper p,Message msg, String allUserStr,String userParam,String updatePaperData) throws Exception;
	
	public boolean updateEndTime (Paper p) throws Exception;
	
	public boolean deletePaperData (Map<String, Object> map) throws Exception;
	
	public Integer selectTopicTypeCount (@Param(value="type") String type,
										 @Param(value="countryId") String countryId,
										 Paper p
										 ) throws Exception;
	
	public List<Examination> selectTopicByPaperId (@Param(value="id") String id) throws Exception;
	
	public void insertQRcode(Paper p) throws Exception;
	
	public Map<String,Object> selectExaminationSituation(String conditions,int start,int limit) throws Exception;
	
	public Map<String,Object> selectRusExaminationSituation(String conditions,int start,int limit) throws Exception;

	HSSFWorkbook exporSale(String conditions, String[] excelHeader, String title,int start,int limit) throws Exception;
	
	public List<Examination> selectExamByTypeAndCountry(String countryId,String conditions) throws Exception;
	
	public Map<String,Object> selectExamSelectedInfo(String paperId,String countryId,String conditions) throws Exception;

	/**
	 * 考试是否已经开始
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer isSpecifiedTime (Map<String,Object> map) throws Exception;
	
	
	/**
	 * 用户是否已经考完试
	 * @param userId
	 * @param paperId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> selectUserIsExam ( String userId, String paperId) throws Exception;
	
	/**
	 * 根据试卷id查询试卷
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer isSpecifiedEndTime (Map<String,Object> map) throws Exception;
	/**
	 * 导出俄罗斯考试记录
	 * @param conditions
	 * @param excelHeader
	 * @param title
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	HSSFWorkbook exporRusSale(String conditions, String[] excelHeader, String title,int start,int limit) throws Exception;
}
