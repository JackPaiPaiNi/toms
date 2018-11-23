package cn.tcl.platform.training.actions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.training.service.CourseTypeService;
import cn.tcl.platform.training.vo.CourseType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings("serial")
public class CourseTypeAction extends BaseAction {
	@Autowired
	@Qualifier("courseTypeService")
	private CourseTypeService courseTypeService;
	
	//获取一级课程类别
	public void getLevel1TypeData()
	{
		JSONObject result=new JSONObject();
		response.setHeader("Content-Type","application/json");
		try {
			List<CourseType> ctlst=courseTypeService.GetLevelOneList();
			JSONArray jsonArray=JSONArray.fromObject(ctlst);
			result.accumulate("rows",jsonArray.toString());
			result.accumulate("total",ctlst.size());
			result.accumulate("success",true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage(), e);
			String msg = e.getCause()!=null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("rows", new JSONArray());
			result.accumulate("total", 0);
			result.accumulate("success", true);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void getSubTypeData()
	{
		JSONObject result=new JSONObject();
		response.setHeader("Content-Type","application/json");
		int typeId=Integer.parseInt(request.getParameter("typeId"));
		try {
			List<CourseType> ctlst=courseTypeService.GetSubTypeListById(typeId);
			JSONArray jsonArray=JSONArray.fromObject(ctlst);
			result.accumulate("rows",jsonArray.toString());
			result.accumulate("total",ctlst.size());
			result.accumulate("success",true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage(), e);
			String msg=e.getCause()!=null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("rows",new JSONArray());
			result.accumulate("total", 0);
			result.accumulate("success",true);
			result.accumulate("msg",msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
}
