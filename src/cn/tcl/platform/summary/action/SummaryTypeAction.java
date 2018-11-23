package cn.tcl.platform.summary.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.summary.service.SummaryTypeService;
import cn.tcl.platform.summary.vo.SummaryType;

@SuppressWarnings("all")
public class SummaryTypeAction extends BaseAction{
	
	@Autowired
	@Qualifier("summaryTypeService")
	private SummaryTypeService summaryTypeService;

	
	public void getMessageType(){
		JSONObject result = new JSONObject();
		try {
			List<SummaryType> list = summaryTypeService.getMessageType();
			String rows = JSONArray.fromObject(list).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
}
