package cn.tcl.platform.summary.service;

import java.util.Map;

import com.sun.mail.handlers.message_rfc822;

import cn.tcl.platform.message.vo.Message;
import cn.tcl.platform.summary.vo.Summary;

public interface SummaryService {
	
	public void insetrSummary(Summary sum) throws Exception;
	
	public Map<String, Object> selectSummaryList(int start,int limit,String searchStr,String partyId,String typeId,String MessagetypeId,String order,String sort,String conditions) throws Exception;
	
	public Summary getSummaryById(String summaryId) throws Exception;
	
	public void updateSummary(Summary sum,Message msg,String allUserStr) throws Exception;
	
	public void deleteSummary(Summary summaryId,Message msg) throws Exception;
}
