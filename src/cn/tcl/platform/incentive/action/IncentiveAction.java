package cn.tcl.platform.incentive.action;

import java.io.File;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.excel.imports.ExcelImportUtil;
import cn.tcl.platform.incentive.service.IncentiveService;
import cn.tcl.platform.incentive.vo.Incentive;
import cn.tcl.platform.modelmap.vo.ModelMap;



public class IncentiveAction extends BaseAction{
	
	@Autowired
	@Qualifier("percentageService")
	private IncentiveService percentageService;
	
	public String loadIncentivePage(){
		try {
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}
	
	/**
	 * 加裁提成列表
	 */
	public void loadIncentiveList(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		
		try {
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			String pageStr=request.getParameter("page");
			String selectQuertyPartyId = request.getParameter("selectQuertyPartyId");

			
			int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
			String rowStr=request.getParameter("rows");
			int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
			int start = (page-1)*limit;
			
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			
			String keyword = request.getParameter("keyword");
			String searchStr = " 1=1";
			if(keyword!=null && !"".equals(keyword)){
				searchStr += " and tp.branch_model like concat ('%','"+keyword+"','%')";
			}
			
			
			String conditions = "";
			
			if(!WebPageUtil.isHAdmin()){
				if(userPartyIds!=null && !userPartyIds.equals("")){
					conditions += " and tp.country_id IN (select distinct tt.country_id from party tt where tt.party_id in ("+userPartyIds+"))";
				}else{
					conditions += "1=2";
				}				
			}else{				
				if(WebPageUtil.isStringNullAvaliable(selectQuertyPartyId)){
					conditions += " and tp.country_id in ("+selectQuertyPartyId+")";
				}else{
					conditions +=" and 1=1";
				}
			}
			
			
			Map<String, Object> map = percentageService.selectIncentiveList(start, limit,searchStr, conditions, order, sort);
			int total = (int)map.get("total");
			List<Incentive> list = (ArrayList<Incentive>) map.get("rows");
			String rows = JSONArray.fromObject(list).toString();
			result.accumulate("rows", rows);
			result.accumulate("total", total);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
	/**
	 * 添加提成
	 */
	public void addIncentive(){
		JSONObject result = new JSONObject();
		String branchModel = request.getParameter("branchModel");
//		String size = request.getParameter("size");
		String retailPrice = request.getParameter("retailPrice");
		String incentive = request.getParameter("incentive");
//		String quantity = request.getParameter("quantity");
		String partyId = request.getParameter("partyId");
		String remark = request.getParameter("remark");
		String date = request.getParameter("date");
//		String creatDate = request.getParameter("creatDate");
		
		
		try {
			Incentive incen = new Incentive();
			incen.setBranchModel(branchModel.toUpperCase());
//			incen.setSize(size);
			incen.setRetailPrice(retailPrice);
			incen.setIncentive(incentive);
//			incen.setQuantity(quantity);
			incen.setPartyId(partyId);
			incen.setDate(date);
//			incen.setCreatDate(new Date().toString());
			incen.setRemark(remark);
			
			int count = percentageService.countBranchModel(branchModel,partyId);
			if(count==0){
				result.accumulate("msg", true);
			}else{
				int counts = percentageService.countIncentiveByCondition(partyId, branchModel, date);
				if(counts>0){
					percentageService.updateIncentiveByCondition(incen);
				}else{
					percentageService.addIncentive(incen);
				}
				result.accumulate("success", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 删除提成
	 */
	public void deleteIncentive(){
		JSONObject result = new JSONObject();
		

		try {
			String id = request.getParameter("id");
			Incentive incen = new Incentive();
			incen.setId(id);
			percentageService.deleteIncentive(incen);
			result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 修改提成
	 * 
	 */
	
	public void updateIncentive(){
		JSONObject result = new JSONObject();
		String id = request.getParameter("id");
		String branchModel = request.getParameter("branchModel");
//		String size = request.getParameter("size");
		String retailPrice = request.getParameter("retailPrice");
		String incentive = request.getParameter("incentive");
		String quantity = request.getParameter("quantity");
		String partyId = request.getParameter("partyId");
//		String creatDate = request.getParameter("creatDate");
		String remark = request.getParameter("remark");
		String date = request.getParameter("date");
		

		
		
		try {
			Incentive incen = percentageService.queryIncentive(id);
			incen.setBranchModel(branchModel);
//			incen.setSize(size);
			incen.setRetailPrice(retailPrice);
			incen.setIncentive(incentive);
			incen.setQuantity(quantity);
			incen.setPartyId(partyId);
//			incen.setCreatDate(new Date().toString());
			incen.setRemark(remark);
			incen.setDate(date);
			
			int count = percentageService.countBranchModel(branchModel,partyId);
			if(count==0){
				result.accumulate("msg", true);
			}else{
//				int counts = percentageService.countIncentiveByCondition(partyId, branchModel, date);
//				if(counts>0){
//					percentageService.updateIncentiveByCondition(incen);
//				}else{
					percentageService.updateIncentive(incen);
//				}
			result.accumulate("success", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
	/**
	 * 导入提成
	 */
	public void importIncentive(){
		try{
			String path = getClassRealPath()+ File.separatorChar+"cn"+File.separatorChar+"tcl"+File.separatorChar+"platform" +
			File.separatorChar +"incentive"+File.separatorChar+"imports"+File.separatorChar+"imports.xml";
			
			ExcelImportUtil export = new ExcelImportUtil(path);
			export.init(uploadExcel,uploadExcelFileName,WebPageUtil.getLanguage());
			List<Incentive> list = export.bindToModelsAndImport(Incentive.class);

			String errorMsg = export.getError();
			if("".equals(errorMsg)){
				WebPageUtil.writeBack("success");
			}
			else{
				WebPageUtil.writeBack(errorMsg.substring(53));
			}
		}
		catch(Exception e){
			String errorMsg = e.getMessage();
			if(null==errorMsg || "".equals(errorMsg))
			{
				errorMsg = this.getText("import.error.exist");
			}
			log.error(e.getStackTrace());
			e.printStackTrace();
			WebPageUtil.writeBack(errorMsg);
		}
	}
	
	
	public void selectBranchModel(){
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try {
			String countryId = request.getParameter("countryId");
			List<ModelMap> list = percentageService.selectBranchModel(countryId);
			result=JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
}
