package cn.tcl.platform.training.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.platform.message.dao.MessageDao;
import cn.tcl.platform.message.vo.Message;
import cn.tcl.platform.message.vo.SendMessageUser;
import cn.tcl.platform.party.vo.Party;
import cn.tcl.platform.training.dao.TrainingCourseDao;
import cn.tcl.platform.training.service.CourseService;
import cn.tcl.platform.training.vo.CourseType;
import cn.tcl.platform.training.vo.TrainingCourse;

@Service("courseService")
public class CourseServiceImpl implements CourseService {
	
	protected Logger log=Logger.getLogger(this.getClass());
	
	@Autowired
	private TrainingCourseDao trainingCourseDao;
	
	@Autowired
	private MessageDao messageDao;

	@Override
	public List<TrainingCourse> GetCourseList(TrainingCourse tc) throws Exception {
		// TODO Auto-generated method stub
		return trainingCourseDao.GetCourseList(tc);
	}

	@Override
	public int UpdateCourse(TrainingCourse tc) throws Exception {
		// TODO Auto-generated method stub
		return trainingCourseDao.UpdateCourse(tc);
	}

	@Override
	public int InsertCourse(TrainingCourse tc) throws Exception {
		// TODO Auto-generated method stub
		return trainingCourseDao.InsertCourse(tc);
	}

	@Override
	public Map<String, Object> SelectCourseList(int start, int limit,String searchStr,String partyId,String typeId,String typeIdSubId,String typeIdSubtoId,String message_type,
			String order, String sort, String conditions) throws Exception {
		String _typeId=typeId==""?null:typeId;
		String _typeIdSubId=typeIdSubId==""?null:typeIdSubId;
		String _typeIdSubtoId=typeIdSubtoId==""?null:typeIdSubtoId;
		String _partyId=partyId==""?null:partyId;
		List<TrainingCourse> courseList = trainingCourseDao.SelectCourseList(start, limit,searchStr,_partyId,_typeId,_typeIdSubId,_typeIdSubtoId,message_type, order, sort, conditions);
		Map<String, Object> map = new HashMap<String, Object>();
		int count = trainingCourseDao.count(start, limit,searchStr,_partyId,_typeId,_typeIdSubId,_typeIdSubtoId,message_type, conditions);
		map.put("rows", courseList);
		map.put("total", count);
		return map;
	}

	@Override
	public TrainingCourse selectTrainCourseById(String id)
			throws Exception {
		return trainingCourseDao.selectTrainCourseById(id);
	}

	@Override
	public void updateTrainCourse(TrainingCourse tc,String allUserStr,String userParam) throws Exception {
		trainingCourseDao.updateTrainCourse(tc);
		String msgRoleId="";
		String [] msgRole = allUserStr.split(";");
		for(int i=0;i<msgRole.length;i++){
			 msgRoleId = msgRole[i];
		}
//		messageDao.deleteMessageByUser(msgRoleId);
		String [] allUser = userParam.split(";");
		for(int i=0;i<allUser.length;i++){
			String userName = allUser[i];
			String userLoginId = messageDao.selectUserLoginId(userName);
			SendMessageUser smu = new SendMessageUser();
			smu.setUserLoginId(userLoginId);
			smu.setMsgRoleId(msgRoleId);
			smu.setMsgState("1");
			
			if(userName!=null && !userName.equals("")){
//				messageDao.insertSendMessageUser(smu);
			}
		}
	}
	
	@Override
	public void updateTrainCourse(TrainingCourse tc,Message msg,String allUserStr,String userParam) throws Exception {
		trainingCourseDao.updateTrainCourse(tc);
		messageDao.updateMsgRoleIdByCourseId(msg);
		messageDao.deleteMessageByUser(msg.getMsgRoleId());//只删除未阅读课件的用户
		String userLoginId="";
		String userLogin1="";
//		String [] userLogin = allUserStr.split(";");
		
		JSONArray jsonArray = JSONArray.fromObject(allUserStr);  
//		String [] userLogin = new String[jsonArray.size()];
//		int userLoginIndex = 0;
//		@SuppressWarnings("unchecked")
//		Iterator <Object> it = jsonArray.iterator();
//		while (it.hasNext()) {
//			JSONObject ob = (JSONObject) it.next();
//			userLogin[userLoginIndex] = (String) ob.get("userId");
//			userLoginIndex ++;
//		}
		
		List<SendMessageUser> list = new ArrayList<SendMessageUser>();
		for(int i=0;i<jsonArray.size();i++){
//			userLoginId = userLogin[i];
			JSONObject obj = (JSONObject) jsonArray.get(i);
			userLoginId = obj.getString("userId");
		 userLogin1 = messageDao.getUserLoginByMsgRoleId(userLoginId,msg.getMsgRoleId());
		System.out.println(userLogin1+"----------------------"+userLoginId);
		
		SendMessageUser smu = new SendMessageUser();
		if(userLogin1==null){
			smu.setUserLoginId(userLoginId);
			smu.setMsgRoleId(msg.getMsgRoleId());
			smu.setMsgState("0");
			}else{
				messageDao.deleteMessageByUser2(userLogin1,msg.getMsgRoleId());//删除阅读课件的用户
				smu.setUserLoginId(userLoginId);
				smu.setMsgRoleId(msg.getMsgRoleId());
				smu.setMsgState("1");
			}
		list.add(smu);	
		}
		
		
			if(list.size()>0){
				messageDao.insertSendMessageUser(list);
			}
				
		
//		String [] allUser = userParam.split(";");
//		for(int i=0;i<allUser.length;i++){
//			String userName = allUser[i];
//			String userLoginId = messageDao.selectUserLoginId(userName);
//		}
	}
	
	/*@Override
	public void saveTrainCourse(TrainingCourse tc) throws Exception {
		trainingCourseDao.saveTrainCourse(tc);
	}*/

	@Override
	public void deleteTrainCourse(TrainingCourse courseId,Message msg) throws Exception {
		trainingCourseDao.delectTrainCourse(courseId);
//		trainingCourseDao.deleteTrainMsg(courseId);
		messageDao.deleteMsgUrl(msg);
	}

	@Override
	public List<Party> selectRegion(String partyId) throws Exception {
		return trainingCourseDao.selectRegion(partyId);
	}

	@Override
	public List<Party> selectAllParty() throws Exception {
		return trainingCourseDao.selectAllParty();
	}

	@Override
	public List<Party> selectAllCountry(String partyId) throws Exception {
		return trainingCourseDao.selectAllCountry(partyId);
	}

	@Override
	public List<CourseType> getLevelOneTypeId() throws Exception {
		
		return trainingCourseDao.getLevelOneTypeId();
	}

	@Override
	public List<CourseType> getLevelTwoOrthreeTypeId(String typeId) throws Exception {
		return trainingCourseDao.getLevelTwoOrthreeTypeId(typeId);
	}

	@Override
	public List<CourseType> getLevelthreeTypeId(String typeId) throws Exception {
		return trainingCourseDao.getLevelthreeTypeId(typeId);
	}

	@Override
	public List<Party> getPartyList(String userLoginId) throws Exception {
		return trainingCourseDao.getPartyList(userLoginId);
	}

	@Override
	public List<Party> getBranchPartyList(String userLoginId) throws Exception {
		return trainingCourseDao.getBranchPartyList(userLoginId);
	}


//	@Override
//	public List<TrainingCourse> SelectCourseList(TrainingCourse tc, String keyword) throws Exception {
//		// TODO Auto-generated method stub
//		return trainingCourseDao.SelectCourseList(tc, keyword);
//	}

}
