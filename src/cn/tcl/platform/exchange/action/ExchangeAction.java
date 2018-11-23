package cn.tcl.platform.exchange.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.exchange.service.IExchangeService;
import cn.tcl.platform.exchange.vo.Exchange;
import cn.tcl.platform.inventory.vo.Inventory;
import cn.tcl.platform.party.vo.Party;
import cn.tcl.platform.product.vo.Product;
import cn.tcl.platform.role.vo.Role;
import cn.tcl.platform.user.vo.UserLogin;
@SuppressWarnings("all")
public class ExchangeAction extends BaseAction{
	@Autowired(required=false)
	@Qualifier("ExchangeService")
	private IExchangeService ExchangeService;
	//汇率转换页面
	public String loadCurrencyConversionPage(){
		if(!WebPageUtil.isHAdmin()){
			return INPUT;
		}else{
			try{
				return SUCCESS;
			}
			catch(Exception e){
				e.printStackTrace();
				log.error(e.getMessage(),e);
				return ERROR;
			}
		}
	}
	
	//加载汇率数据
	public void loadExchangeList(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try {
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			String keyword = request.getParameter("keyword");
			String selectQuertyPartyId = request.getParameter("selectQuertyPartyId");
			keyword = "".equals(keyword)?null:keyword;
			String pageStr=request.getParameter("page");
			int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
			String rowStr=request.getParameter("rows");
			int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
			int start = (page-1)*limit;
			
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds)){
					conditions += "t.country_id IN (select distinct tt.country_id from party tt where tt.party_id in ("+userPartyIds+"))";
				}else{
					conditions += " and 1=2 ";
				}
			}
			else
			{
//				conditions += " and 1=1 ";
				conditions += (WebPageUtil.isStringNullAvaliable(selectQuertyPartyId) ? "AND t.`COUNTRY_ID` = "+selectQuertyPartyId : "and 1=1" );
			}
			
			Map<String, Object> map = ExchangeService.selectExchange(start, limit, keyword, order, sort,conditions,selectQuertyPartyId);
			int total = (Integer)map.get("total");
			List<Exchange> list = (ArrayList<Exchange>)map.get("rows");
			JSONArray jsonArray = JSONArray.fromObject(list);
			String rows = jsonArray.toString();
			result.accumulate("rows", rows);
			result.accumulate("total", total);
			result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
//		JSONObject result = new JSONObject();
//		response.setHeader("Content-Type", "application/json");
//		try
//		{
//			String sort = request.getParameter("sort");
//			System.out.println(sort+"======================");
////			sort = WebPageUtil.tansBeanFiledToDbField(sort);
//			String order = request.getParameter("order");
//			String selectQuertyPartyId = request.getParameter("selectQuertyPartyId");
//			String searchMsg = request.getParameter("searchMsg");
//			String pageIndex = request.getParameter("page");
//			String pageSize = request.getParameter("rows");
//			String startRow = (Integer.valueOf(pageIndex)-1)*Integer.valueOf(pageSize) + "";
//			
//			Map<String, Object> map = ExchangeService.selectExchange(searchMsg, startRow, pageSize, order, sort,selectQuertyPartyId);
//			int total = (Integer)map.get("total");
//			List<Exchange> list = (List<Exchange>)map.get("list");
////			for(Exchange r : list)
////			{
////				String roleType = r.getRoleType();
////				String _roleType = this.getText("role.form.title.roleType."+roleType);
////				r.setRoleType(_roleType);
////			}
//			result.accumulate("rows", list);
//			result.accumulate("total", total);
//			result.accumulate("success", true);
//		}
//		catch (Exception e) 
//		{
//			e.printStackTrace();
//			log.error(e.getMessage(),e);
//			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
//			result.accumulate("success", true);
//			result.accumulate("msg", msg);
//		}
//		WebPageUtil.writeBack(result.toString());
//		JSONObject result = new JSONObject();
//		response.setHeader("Content-Type", "application/json");
//		try{
//			String sort = request.getParameter("sort");
//			String order = request.getParameter("order");
//			String pageStr=request.getParameter("page");
//			int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
//			String rowStr=request.getParameter("rows");
//			int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
//			int start = (page-1)*limit;
//			
//			String searchStr = "1 = 1 ";
//
//			//String conditions = WebPageUtil.buildDataPermissionSql("party_id", Contents.ROLE_DATA_PERMISSION_PARTY);
//			
//			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
//			String conditions = "";
//			if(!WebPageUtil.isHAdmin())
//			{
//				/*if(null!=userPartyIds && !"".equals(userPartyIds))
//				{
//					conditions += " p.party_id in ("+userPartyIds+")";
//				}
//				else
//				{
//					conditions += " 1=2 ";
//				}*/
//				conditions += " 1=1 ";
//			}
//			else
//			{
//				conditions += " 1=1 ";
//			}
//			
//			Map<String, Object> map = ExchangeService.selectExchange(start, limit, searchStr, order, sort, conditions);
//			int total = (Integer)map.get("total");
//			List<Inventory> list = (ArrayList<Inventory>)map.get("rows");
//			JSONArray jsonArray = JSONArray.fromObject(list);
//			String rows = jsonArray.toString();
//			result.accumulate("rows", rows);
//			result.accumulate("total", total);
//			result.accumulate("success", true);
//		}catch(Exception e){
//			e.printStackTrace();
//			log.error(e.getMessage(),e);
//			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
//			result.accumulate("success", true);
//			result.accumulate("msg", msg);
//		}
//		WebPageUtil.writeBack(result.toString());
	}
	
	public void saveExchange(){
		JSONObject result = new JSONObject();
		try {
		String countryId = request.getParameter("countryId");
		String exchange = request.getParameter("exchange");
		String datadate = request.getParameter("dataDate");
		Exchange ex = new Exchange();
//		String countryName = ex.getCountryName();
//		Party country = ExchangeService.getCountryName(countryName);
//		System.out.println(countryName+"+============================");
		ex.setCountryId(countryId);
		ex.setExchange(exchange);
		ex.setDataDate(datadate);
		
			ExchangeService.saveExchange(ex);
			result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	//编辑汇率
	public void editExchange(){
		JSONObject result = new JSONObject();
		String id = request.getParameter("editId");
		if(id!=null && id.trim()!=""){
			String countryId = request.getParameter("countryId");
			String exchange = request.getParameter("exchange");
			String datadate = request.getParameter("dataDate");
	try
		{
			Exchange ex = ExchangeService.selectExchangeById(id);
			ex.setCountryId(countryId);
			ex.setExchange(exchange);
			ex.setDataDate(datadate);
			ExchangeService.updateByExchange(ex);
			result.accumulate("success", true);
//			Exchange ex = new Exchange();
//			ex.setId(id);
//			ex.setCountryId(countryName);
//			ex.setExchange(exchange);
//			ex.setDataDate(WebPageUtil.strToDate(dataDate));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void delectExchange(){
		JSONObject result = new JSONObject();
		String id = request.getParameter("id");
		try {
		Exchange ex =new Exchange();
		ex.setId(id);
			ExchangeService.deleteExchange(ex);
			result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
}
