package cn.tcl.platform.training.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.message.vo.Message;
import cn.tcl.platform.party.vo.Party;
import cn.tcl.platform.training.vo.CourseType;
import cn.tcl.platform.training.vo.TrainingCourse;

public interface CourseService {
	public List<TrainingCourse> GetCourseList(TrainingCourse tc) throws Exception;
	public int UpdateCourse(TrainingCourse tc) throws Exception;
	public int InsertCourse(TrainingCourse tc) throws Exception;
//	public List<TrainingCourse> SelectCourseList(TrainingCourse tc,String keyword) throws Exception;
	public Map<String,Object> SelectCourseList(int start,int  limit,String searchStr,String partyId,String typeId,String typeIdSubId,String typeIdSubtoId,String message_type,String order,String sort,String conditions) throws Exception;
	public TrainingCourse selectTrainCourseById(String courseId)throws Exception;
	//修改课件的同时修改角色
	public void updateTrainCourse(TrainingCourse tc,String allUserStr,String userParam) throws Exception;
	//修改课件同时由选择角色类型修改角色 
	public void updateTrainCourse(TrainingCourse tc,Message msg,String allUserStr,String userParam) throws Exception;
//	public void saveTrainCourse(TrainingCourse tc) throws Exception;
	
	public void deleteTrainCourse(TrainingCourse courseId,Message msg) throws Exception; 
	public List<Party> selectRegion(String partyId) throws Exception;
	public List<Party> selectAllParty() throws Exception;
	public List<Party> selectAllCountry(String partyId) throws Exception;
	
	public List<CourseType> getLevelOneTypeId() throws Exception;
	
	public List<CourseType> getLevelTwoOrthreeTypeId(String typeId) throws Exception;
	public List<CourseType> getLevelthreeTypeId(String typeId) throws Exception;
	
	public List<Party> getPartyList(String userLoginId) throws Exception;
	
	public List<Party> getBranchPartyList(String userLoginId) throws Exception;
 }
