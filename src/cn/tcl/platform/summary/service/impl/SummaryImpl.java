package cn.tcl.platform.summary.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.platform.message.dao.MessageDao;
import cn.tcl.platform.message.vo.Message;
import cn.tcl.platform.message.vo.SendMessageUser;
import cn.tcl.platform.summary.dao.SummaryDao;
import cn.tcl.platform.summary.service.SummaryService;
import cn.tcl.platform.summary.vo.Summary;

@Service("summaryService")
public class SummaryImpl implements SummaryService{
	
	@Autowired
	private SummaryDao summaryDao;
	
	@Autowired
	private MessageDao messageDao;
	
	@Override
	public void insetrSummary(Summary sum) throws Exception {
		summaryDao.insetrSummary(sum);
	}
	@Override
	public Map<String, Object> selectSummaryList(int start, int limit,
			String searchStr,String partyId,String typeId, String MessagetypeId,
			String order, String sort, String conditions) throws Exception {
		String _typeId=typeId==""?null:typeId;
		List<Summary> list = summaryDao.selectSummaryList(start, limit, searchStr,partyId, _typeId, MessagetypeId, order, sort, conditions);
		Map<String, Object> map = new HashMap<String, Object>();
		int count = summaryDao.countSummary(start, limit, searchStr,partyId, _typeId, MessagetypeId, order, sort, conditions);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}
	
	@Override
	@SuppressWarnings("all")
	public void updateSummary(Summary sum,Message msg,String allUserStr) throws Exception {
		summaryDao.updateSummary(sum);
		messageDao.updateMsgBySummaryId(msg);
		messageDao.deleteMessageByUser(msg.getMsgRoleId());
//		String[] userLogin = allUserStr.split(";");
		JSONArray jsonArray = JSONArray.fromObject(allUserStr); 
		String userLoginId="";
		
		List<SendMessageUser> list = new ArrayList<SendMessageUser>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = (JSONObject) jsonArray.get(i);
			userLoginId = obj.getString("userId");
//			userLoginId=userLogin[i];
			String userLogin1 = messageDao.getUserLoginByMsgRoleId(userLoginId,msg.getMsgRoleId());
//			System.out.println(userLogin1+"----------------------"+userLoginId);


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
		if(userLoginId!=null && !userLoginId.equals("")){
			messageDao.insertSendMessageUser(list);
		}		
	}
	
	
	@Override
	public Summary getSummaryById(String summaryId) throws Exception {
		return summaryDao.getSummaryById(summaryId);
	}
	@Override
	public void deleteSummary(Summary summaryId,Message msg) throws Exception {
		summaryDao.deleteSummary(summaryId);
		messageDao.deleteMsgUrlBySummaryId(msg);
	}

}
