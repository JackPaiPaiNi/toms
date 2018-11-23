package cn.tcl.platform.inventory.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.excel.imports.ExcelImportUtil;
import cn.tcl.platform.customer.vo.Customer;
import cn.tcl.platform.inventory.service.IInventoryService;
import cn.tcl.platform.inventory.vo.Inventory;
import cn.tcl.platform.sale.vo.SampleDevice;

@SuppressWarnings("all")
public class UiInventoryAction extends BaseAction{
	@Autowired(required = false)
	@Qualifier("inventoryService")
	private IInventoryService inventoryService;
	public String loadInventoryListPage(){
		try{
			return SUCCESS;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	/**
	 * 加载库存数据
	 */
	public void loadInventoryListData(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try{
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			String pageStr=request.getParameter("page");
			int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
			String rowStr=request.getParameter("rows");
			int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
			int start = (page-1)*limit;
			
			String searchStr = "1 = 1 ";
			
			/*String searchDate = request.getParameter("searchDate");
			if(!"".equals(searchDate) && searchDate != null){
				searchStr += " and vs.datadate = '"+searchDate+"'";
			}*/
			String searchPatry = request.getParameter("searchPatry");
			if(!"".equals(searchPatry) && searchPatry != null){
				searchStr += " and vi.PARTY_NAME like CONCAT('%','"+searchPatry+"','%')";
			}
			/*String searchCustomer = request.getParameter("searchCustomer");
			if(!"".equals(searchCustomer) && searchCustomer != null){
				searchStr += " and vs.CUSTOMER_NAME like CONCAT('%','"+searchCustomer+"','%')";
			}*/
			String searchShop = request.getParameter("searchShop");
			if(!"".equals(searchShop) && searchShop != null){
				searchStr += " and vi.shop_name like CONCAT('%','"+searchShop+"','%')";
			}
			String searchModel = request.getParameter("searchModel");
			if(!"".equals(searchModel) && searchModel != null){
				searchStr += " and vi.model like CONCAT('%','"+searchModel+"','%')";
			}
			String searchHqModel = request.getParameter("searchHqModel");
			if(!"".equals(searchHqModel) && searchHqModel != null){
				searchStr += " and tm.hq_model like CONCAT('%','"+searchHqModel+"','%')";
			}
			
			//String conditions = WebPageUtil.buildDataPermissionSql("party_id", Contents.ROLE_DATA_PERMISSION_PARTY);
			
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += " vi.party_id in ("+userPartyIds+")";
				}
				else
				{
					conditions += " 1=2 ";
				}
			}
			else
			{
				conditions += " 1=1 ";
			}
			
			Map<String, Object> map = inventoryService.selectInventorydata(start, limit, searchStr, order, sort,conditions);//conditions
			int total = (Integer)map.get("total");
			List<Inventory> list = (ArrayList<Inventory>)map.get("rows");
			JSONArray jsonArray = JSONArray.fromObject(list);
			String rows = jsonArray.toString();
			result.accumulate("rows", rows);
			result.accumulate("total", total);
			result.accumulate("success", true);
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	//下载模版
	public void importInventory() {
		try{
			String path = getClassRealPath()+ File.separatorChar+"cn"+File.separatorChar+"tcl"+File.separatorChar+"platform" +
					File.separatorChar +"inventory"+File.separatorChar+"imports"+File.separatorChar+"imports.xml";
					
					ExcelImportUtil export = new ExcelImportUtil(path);
					export.init(uploadExcel,uploadExcelFileName,WebPageUtil.getLanguage());
					
					List<Inventory> Inventorys = export.bindToModelsAndImport(Inventory.class);
			String errorMsg = export.getError();
			if("".equals(errorMsg)){
				WebPageUtil.writeBack("success");
			}
			else{
				WebPageUtil.writeBack(errorMsg);
			}
		}catch(Exception e){
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
}
