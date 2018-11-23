package cn.tcl.platform.training.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.party.vo.Party;
import cn.tcl.platform.training.vo.CourseType;
import cn.tcl.platform.training.vo.TrainingCourse;

public interface TrainingCourseDao {
	//查询培训课程列表
	public List<TrainingCourse> GetCourseList(TrainingCourse tc) throws Exception;
	//根据条件查询培训课程列表
	public List<TrainingCourse> SelectCourseDataList(Map<String,Object> map) throws Exception;
	//根据条件查询培训课程列表
//	public List<TrainingCourse> SelectCourseList(@Param("trainingCourse")TrainingCourse tc,@Param("keyword")String keyword) throws Exception;
	//修改培训课程
	public int UpdateCourse(TrainingCourse tc) throws Exception;
	//批量修改培训课程
	public int UpdateCourseBatch(List<TrainingCourse> lst) throws Exception;
	//新增培训课程
	public int InsertCourse(TrainingCourse tc) throws Exception;
	//批量新增培训课程
	public int InsertCourseBatch(List<TrainingCourse> lst) throws Exception;
	//获取课程列表
	public List<TrainingCourse> SelectCourseList(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param("partyId")String partyId,
			@Param("typeId") String typeId,
			@Param("typeIdSubId") String typeIdSubId,
			@Param("typeIdSubtoId") String typeIdSubtoId,
			@Param("MessagetypeId") String MessagetypeId,
			@Param(value="order") String order,
			@Param(value="sort") String sort,
			@Param(value="conditions") String conditions) throws Exception;
	//获取课程总数
	public int count(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param("partyId")String partyId,
			@Param("typeId") String typeId,
			@Param("typeIdSubId") String typeIdSubId,
			@Param("typeIdSubtoId") String typeIdSubtoId,
			@Param("MessagetypeId") String MessagetypeId,
			@Param(value="conditions") String conditions) throws Exception;
	
	//获取课程对应的ID
	public TrainingCourse selectTrainCourseById(String courseId) throws Exception;
	
	//修改课程
	public void updateTrainCourse(TrainingCourse tc) throws Exception;
	
	//添加课程
//	public void saveTrainCourse(TrainingCourse tc) throws Exception;
	
	//删除课程
	public void delectTrainCourse(TrainingCourse courseId) throws Exception;
	//根据partyId获取对应的区域和国家
	public List<Party> selectRegion(@Param(value="partyId") String partyId) throws Exception;
	//获取所有的SC
	public List<Party> selectAllParty() throws Exception;
	//获取所有国家
	public List<Party> selectAllCountry(@Param(value="partyId") String partyId) throws Exception;
	
	//获取一级菜单的信息
	public List<CourseType> getLevelOneTypeId() throws Exception;
	
	//获取二级菜单的信息
	public List<CourseType> getLevelTwoOrthreeTypeId(@Param("typeId")String typeId) throws Exception;
	
	//获取三级菜单的信息
	public List<CourseType> getLevelthreeTypeId(@Param("typeId")String typeId) throws Exception;
	//获取所有国家
	public List<Party> getPartyList(@Param("userLoginId")String userLoginId) throws Exception;
	//获取分公司显示的国家
	public List<Party> getBranchPartyList(@Param("userLoginId")String userLoginId) throws Exception;
	
	/**
	 * 根据培训course_id删除消息提醒
	 */
	public void deleteTrainMsg(TrainingCourse courseId) throws Exception;
	
	/**
	 * 根据培训课件course_id删除封面消息地址
	 * @param courseId
	 * @throws Exception
	 */
	public void deleteMsgUrl(TrainingCourse courseId) throws Exception;
}
