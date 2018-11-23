package cn.tcl.platform.sale.actions;

import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;

import com.opensymphony.xwork2.ActionContext;


import cn.tcl.common.BaseAction;
import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.excel.imports.ExcelImportUtil;
import cn.tcl.platform.excel.actions.DateUtil;
import cn.tcl.platform.sale.service.ISaleService;
import cn.tcl.platform.sale.vo.Sale;
import cn.tcl.platform.sale.vo.SaleTarget;
import cn.tcl.platform.sale.vo.SampleDevice;
import cn.tcl.platform.sale.vo.TerminalPhoto;
import cn.tcl.platform.shop.vo.Shop;
import cn.tcl.platform.user.vo.UserLogin;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings("all")
public class UiSaleAction  extends BaseAction{
	@Autowired(required = false)
	@Qualifier("saleService")
	private ISaleService saleService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public String loadSalePage(){
		try{
			return SUCCESS;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	
	public String loadCountrySalePage(){
		try{
			return SUCCESS;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	
	public String loadCustomerSalePage(){
		try{
			return SUCCESS;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	public String loadSaleByMobilePage(){
		try{
			return SUCCESS;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	public String loadSaleTargetPage(){
		if (WebPageUtil.isHQRole()) {
			if(WebPageUtil.getLoginedUser().getRoleId().contains("HLEADER") && WebPageUtil.loadPartyIdsByUserId().contains("'2'")) {
				return "BDSC";
			}else {
				return INPUT;
			}
			
		} else {
			try {
				if(isBDSC()) {
					return "BDSC";
				}else {
					return SUCCESS;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
				return ERROR;
			}
		}
	}
	
	public String loadACTargetPage(){
		if (WebPageUtil.isHQRole()) {
			if(WebPageUtil.getLoginedUser().getRoleId().contains("HLEADER") && WebPageUtil.loadPartyIdsByUserId().contains("'2'")) {
				return "BDSC";
			}else {
				return INPUT;
			}
			
		} else {
			try {
				if(isBDSC()) {
					return "BDSC";
				}else {
					return SUCCESS;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
				return ERROR;
			}
		}
	}
	
	public static boolean isBDSC() {
		if(WebPageUtil.loadPartyIdsByUserId().contains("'30'") ||
				
				WebPageUtil.loadPartyIdsByUserId().contains("'304'")||
				
				WebPageUtil.loadPartyIdsByUserId().contains("'306'")||
				
				WebPageUtil.loadPartyIdsByUserId().contains("'307'")||
				
				WebPageUtil.loadPartyIdsByUserId().contains("'309'")||
				
				WebPageUtil.loadPartyIdsByUserId().contains("'310'")||
				
				WebPageUtil.loadPartyIdsByUserId().contains("'311'")||
				
				WebPageUtil.loadPartyIdsByUserId().contains("'312'")
				) {
			return true;
		}else {
			return false;
		}
	}
	
	public String loadSPhotosPage(){
		try{
			return SUCCESS;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	public String loadSSampleDevicesPage(){
		try{
			return SUCCESS;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	public void loadSaleListData(){
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
			
			String searchStr = "1 = 1";
			String searchCdate = request.getParameter("searchCdate");
			if(!"".equals(searchCdate) && searchCdate != null){
				searchStr += " and vs.datadate >= '"+searchCdate+"'";
			}
			String searchDdate = request.getParameter("searchDdate");
			if(!"".equals(searchDdate) && searchDdate != null){
				searchStr += " and vs.datadate <= '"+searchDdate+"'";
			}
			
			if((searchCdate == null || "".equals(searchCdate) ) && (searchDdate == null || "".equals(searchDdate))){
				Map<String, String>  dateMap=DateUtil.getSaleDate();
				String beginDate=dateMap.get("beginDate");
				String endDate=dateMap.get("endDate");
				System.out.println("beginDate===="+beginDate);
				System.out.println("endDate================"+endDate);
				searchStr += " and vs.datadate BETWEEN '"+beginDate+"' AND '"+endDate+"'";
			}
			String searchParty = request.getParameter("searchParty");
			if(!"".equals(searchParty) && searchParty != null){
				searchStr += " and  p.PARTY_NAME like CONCAT('%','"+searchParty+"','%')";
			}
			String searchCParty = request.getParameter("searchCParty");
			if(!"".equals(searchCParty) && searchCParty != null){
				searchStr += " and cp.PARTY_NAME like CONCAT('%','"+searchCParty+"','%')";
			}
			String searchCustomer = request.getParameter("searchCustomer");
			if(!"".equals(searchCustomer) && searchCustomer != null){
				searchStr += " and vs.customer_name like CONCAT('%','"+searchCustomer+"','%')";
			}
			String searchShop = request.getParameter("searchShop");
			if(!"".equals(searchShop) && searchShop != null){
				searchStr += " and vs.shop_name like CONCAT('%','"+searchShop+"','%')";
			}
			String searchHModel = request.getParameter("searchHModel");
			if(!"".equals(searchHModel) && searchHModel != null){
				searchStr += " and tm.hq_model like CONCAT('%','"+searchHModel+"','%')";
			}
			String searchBModel = request.getParameter("searchBModel");
			if(!"".equals(searchBModel) && searchBModel != null){
				searchStr += " and vs.model like CONCAT('%','"+searchBModel+"','%')";
			}
			
			//String conditions = WebPageUtil.buildDataPermissionSql("party_id", Contents.ROLE_DATA_PERMISSION_PARTY);
			
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += " p.party_id in ("+userPartyIds+")";
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
			
			Map<String, Object> map = saleService.selectSalesData(start, limit, searchStr, order, sort,conditions);//conditions
			int total = (Integer)map.get("total");
			List<Sale> list = (ArrayList<Sale>)map.get("rows");
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
	
	//门店目标
	public void loadStoreData(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("partyId");
		String datadate = request.getParameter("datadate");
		try {		 
			List<SaleTarget> storelist = saleService.selectSaleTargets(datadate,partyId);
			String rows = JSONArray.fromObject(storelist).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	//渠道目标
		public void loadChannelData(){
			JSONObject result = new JSONObject();
			response.setHeader("Content-Type", "application/json");
			String partyId = request.getParameter("partyId");
			String datadate = request.getParameter("datadate");
			try {		 
				List<SaleTarget> officelist = saleService.selectChannelTargets(datadate,partyId);
				String rows = JSONArray.fromObject(officelist).toString();
				result.accumulate("rows", rows);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage(),e);
			}
			WebPageUtil.writeBack(result.toString());
		}
	
	//办事处目标
	public void loadOfficeData(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("partyId");
		String datadate = request.getParameter("datadate");
		try {		 
			List<SaleTarget> officelist = saleService.selectOfficeTargets(datadate,partyId);
			String rows = JSONArray.fromObject(officelist).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	//区域目标
		public void loadRegionData(){
			JSONObject result = new JSONObject();
			response.setHeader("Content-Type", "application/json");
			String partyId = request.getParameter("partyId");
			String datadate = request.getParameter("datadate");
			try {		 
				List<SaleTarget> officelist = saleService.selectRegionTargets(datadate,partyId);
				String rows = JSONArray.fromObject(officelist).toString();
				result.accumulate("rows", rows);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage(),e);
			}
			WebPageUtil.writeBack(result.toString());
		}
		
		//分公司目标
		public void loadBranchData(){
			JSONObject result = new JSONObject();
			response.setHeader("Content-Type", "application/json");
			String partyId = request.getParameter("partyId");
			String datadate = request.getParameter("datadate");
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHQRole())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += " si.`COUNTRY_ID` in (select distinct tt.country_id from party tt where tt.party_id in ("+userPartyIds+"))";
					conditions +="or p.PARENT_PARTY_ID IN(SELECT PARTY_ID FROM party WHERE PARENT_PARTY_ID in ("+userPartyIds+"))";
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
			try {		 
				List<SaleTarget> officelist = saleService.selectBranchTargets(conditions,datadate,partyId);
				String rows = JSONArray.fromObject(officelist).toString();
				result.accumulate("rows", rows);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage(),e);
			}
			WebPageUtil.writeBack(result.toString());
		}		
		
	//导购员目标
//	public void loadPromoterData(){
//		JSONObject result = new JSONObject();
//		response.setHeader("Content-Type", "application/json");
//		try {		 
//			List<SaleTarget> officelist = saleService.selectPromoterTargets();
//			String rows = JSONArray.fromObject(officelist).toString();
//			result.accumulate("rows", rows);
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error(e.getMessage(),e);
//		}
//		WebPageUtil.writeBack(result.toString());
//	}		
	
	//督导目标
	public void loadSupervisorData(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("partyId");
		String datadate = request.getParameter("datadate");
		try {		 
			List<SaleTarget> officelist = saleService.selectSupervisorTargets(datadate,partyId);
			String rows = JSONArray.fromObject(officelist).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}		

	//业务员目标
	public void loadSalesmanData(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("partyId");
		String datadate = request.getParameter("datadate");
		try {		 
			List<SaleTarget> officelist = saleService.selectSalesmanTargets(datadate,partyId);
			String rows = JSONArray.fromObject(officelist).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}	
		//业务经理目标
		public void loadManagerData(){
			JSONObject result = new JSONObject();
			response.setHeader("Content-Type", "application/json");
			String partyId = request.getParameter("partyId");
			String datadate = request.getParameter("datadate");
			try {		 
				List<SaleTarget> officelist = saleService.selectBusinessTargets(datadate,partyId);
				String rows = JSONArray.fromObject(officelist).toString();
				result.accumulate("rows", rows);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage(),e);
			}
			WebPageUtil.writeBack(result.toString());
		}
/*	public void loadSaleTargetListData(){
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
			
			String searchParty = request.getParameter("searchParty");
			if("".equals(searchParty)){
				searchParty = null;
			}
			String searchCustomer = request.getParameter("searchCustomer");
			if("".equals(searchCustomer)){
				searchCustomer = null;
			}
			String searchShop = request.getParameter("searchShop");
			if("".equals(searchShop)){
				searchShop = null;
			}
			//String conditions = WebPageUtil.buildDataPermissionSql("party_id", Contents.ROLE_DATA_PERMISSION_PARTY);
			
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
//					conditions += "  v_target.USER_LOGIN_ID in " +
//							"(select DISTINCT m.USER_LOGIN_ID from user_role_mapping m,role_data_permission n " +
//							"where m.ROLE_ID = n.ROLE_ID and n.PERMISSION_TYPE = '"+Contents.ROLE_DATA_PERMISSION_PARTY+"'	and n.permission_value in (" +
//							userPartyIds + "))";
					conditions += " v_target.party_id in ("+userPartyIds+")";
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
			
			Map<String, Object> map = saleService.selectSaleTargets(start, limit, searchParty,searchCustomer,searchShop, order, sort,conditions);//conditions
			int total = (Integer)map.get("total");
			List<SaleTarget> list = (ArrayList<SaleTarget>)map.get("rows");
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
	}*/
	public void loadTerminalPhotoListData(){
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
			
			
			String searchStr = "1=1 ";
			
			String searchParty = request.getParameter("searchParty");
			if(!"".equals(searchParty) && searchParty != null){
				searchStr += " and a.PARTY_NAME like CONCAT('%','"+searchParty+"','%')";
			}
			String searchCustomer = request.getParameter("searchCustomer");
			if(!"".equals(searchCustomer) && searchCustomer != null){
				searchStr += " and a.customer_name like CONCAT('%','"+searchCustomer+"','%')";
			}
			String searchShop = request.getParameter("searchShop");
			if(!"".equals(searchShop) && searchShop != null){
				searchStr += " and a.SHOP_NAME like CONCAT('%','"+searchShop+"','%')";
			}
			String searchDate = request.getParameter("searchDate");
			if(!"".equals(searchDate) && searchDate != null){
				searchStr += " and a.datadate ='"+searchDate+"'";
			}
			
			//String conditions = WebPageUtil.buildDataPermissionSql("party_id", Contents.ROLE_DATA_PERMISSION_PARTY);
			
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += " party_id in ("+userPartyIds+")";
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
			
			Map<String, Object> map = saleService.selectPhotos(start, limit, searchStr, order, sort,conditions);//conditions
			int total = (Integer)map.get("total");
			List<TerminalPhoto> list = (ArrayList<TerminalPhoto>)map.get("rows");
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
	public void loadSampleDeviceListData(){
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
			
			String searchDate = request.getParameter("searchDate");
			if(!"".equals(searchDate) && searchDate != null){
				searchStr += " and vs.datadate = '"+searchDate+"'";
			}
			String searchPatry = request.getParameter("searchPatry");
			if(!"".equals(searchPatry) && searchPatry != null){
				searchStr += " and vs.PARTY_NAME like CONCAT('%','"+searchPatry+"','%')";
			}
			String searchCustomer = request.getParameter("searchCustomer");
			if(!"".equals(searchCustomer) && searchCustomer != null){
				searchStr += " and vs.CUSTOMER_NAME like CONCAT('%','"+searchCustomer+"','%')";
			}
			String searchShop = request.getParameter("searchShop");
			if(!"".equals(searchShop) && searchShop != null){
				searchStr += " and vs.shop_name like CONCAT('%','"+searchShop+"','%')";
			}
			String searchModel = request.getParameter("searchModel");
			if(!"".equals(searchModel) && searchModel != null){
				searchStr += " and vs.model like CONCAT('%','"+searchModel+"','%')";
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
					conditions += " data_party_id in ("+userPartyIds+")";
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
			
			Map<String, Object> map = saleService.selectSampleDevices(start, limit, searchStr, order, sort,conditions);//conditions
			int total = (Integer)map.get("total");
			List<SampleDevice> list = (ArrayList<SampleDevice>)map.get("rows");
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
	
	
	public void addSale(){
		JSONObject result = new JSONObject();
		String userId=request.getParameter("userId");
		String model=request.getParameter("model");
		Integer quantity=WebPageUtil.p2Int(request.getParameter("quantity"));
		Double amount=WebPageUtil.p2Double(request.getParameter("amount"));
		String datadate=request.getParameter("datadate");
		String country=request.getParameter("country");
		String remark=request.getParameter("remark");
		
		Sale sale=new Sale();
		sale.setAmount(amount);
		sale.setCountry(country);
		String _time = sdf.format(new Date());
		sale.setCtime(_time);
		sale.setDatadate(datadate);
		sale.setModel(model);
		sale.setQuantity(quantity);
		sale.setRemark(remark);
		sale.setUserId(userId);
		
		try {
			saleService.saveSale(sale);
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
	/*public void importSales() {
		try{
			ExcelImportUtil export = new ExcelImportUtil(getClassRealPath()+"/cn/tcl/platform/product/imports/imports.xml");
			export.init(uploadExcel,uploadExcelFileName,WebPageUtil.getLanguage());
			List<Product> products = export.bindToModelsAndImport(Product.class);
			String errorMsg = export.getError();
			if("".equals(errorMsg)){
				WebPageUtil.writeBack("success");
			}
			else{
				WebPageUtil.writeBack(errorMsg);
			}
		}
		catch(Exception e){
			String errorMsg = e.getMessage();
			log.error(errorMsg);
			e.printStackTrace();
			WebPageUtil.writeBack(errorMsg);
		}
	}*/
	
	/**
	 * 添加销售目标
	 */
	public void addSaleTarget() throws Exception{
		JSONObject result = new JSONObject();
		String _quantity = request.getParameter("quantity");
		String _shopId = request.getParameter("shopId");
		String _amount = request.getParameter("amount");
		
		String _ctime = sdf.format(new Date());
		try {
			SaleTarget saleTargetBean = new SaleTarget();
			saleTargetBean.setQuantity(Long.valueOf("".equals(_quantity) ? "0" : _quantity));
			saleTargetBean.setShopId(_shopId);
			saleTargetBean.setAmount("".equals(_amount) ? "0.0" : _amount);
			saleTargetBean.setCtime(_ctime);
			
			int c = saleService.validationShopId(_shopId);
			if(c > 0){
				result.accumulate("success", false);
				result.accumulate("msg", getText("sale.target.error.prompt01"));
			}else{
				saleService.addSaleTarget(saleTargetBean);
				result.accumulate("success", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 编辑销售数据
	 */
	public void editSale() throws Exception{
		JSONObject result = new JSONObject();
		String id = request.getParameter("editId");
		if(id!=null && id.trim()!=""){
			//String _country = request.getParameter("country");
			Integer _shopId = Integer.valueOf(request.getParameter("shopId"));
			String _userId = request.getParameter("userId");
			String _model = request.getParameter("model");
			//String _hqModel = request.getParameter("hqModel");
			String _quantity = request.getParameter("quantity");
			String _amount = request.getParameter("amount");
			String _datadate = request.getParameter("datadate");
			String _remark= request.getParameter("remark");
			try {
				Sale saleBean = saleService.getSale(Integer.valueOf(id));
				Integer dataShopId = saleBean.getShopId();
				//saleBean.setCountry(_country);
				saleBean.setQuantity(Integer.valueOf("".equals(_quantity) ? "0" : _quantity));
				saleBean.setShopId(_shopId);
				saleBean.setAmount(Double.valueOf("".equals(_amount) ? "0.0" : _amount));
				saleBean.setUserId(_userId);
				saleBean.setModel(_model);
				//saleBean.setHqModel(_hqModel);
				saleBean.setDatadate(_datadate);
				saleBean.setRemark(_remark);
				
				
				if(_shopId.equals(dataShopId)){
					saleService.editSale(saleBean);
					result.accumulate("success", true);
				}else{
					int c = saleService.validationShopId(request.getParameter("shopId"));
					if(c > 0){
						result.accumulate("success", false);
						result.accumulate("msg", getText("sale.target.error.prompt01"));
					}else{
						saleService.editSale(saleBean);
						result.accumulate("success", true);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage(),e);
				String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
				result.accumulate("success", false);
				result.accumulate("msg", msg);
			}
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 删除销售数据
	 */
	public void deleteSale() throws Exception{
		JSONObject result = new JSONObject();
		String id = request.getParameter("id");
		
		try {
			Sale sale = new Sale();
			sale.setSaleId(Integer.valueOf(id));
			
			saleService.deleteSale(sale);
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
	
	
	/**
	 * 编辑销售目标
	 */
	public void editSaleTarget() throws Exception{
		JSONObject result = new JSONObject();
		String id = request.getParameter("editId");
		if(id!=null && id.trim()!=""){
			String _quantity = request.getParameter("quantity");
			String _shopId = request.getParameter("shopId");
			String _amount = request.getParameter("amount");
			try {
				SaleTarget saleTargetBean = saleService.getSaleTarget(Integer.valueOf(id));
				String dataShopId = saleTargetBean.getShopId();
				
				saleTargetBean.setQuantity(Long.valueOf("".equals(_quantity) ? "0" : _quantity));
				saleTargetBean.setShopId(_shopId);
				saleTargetBean.setAmount("".equals(_amount) ? "0.0" : _amount);
				
				if(_shopId.equals(dataShopId)){
					saleService.editSaleTarget(saleTargetBean);
					result.accumulate("success", true);
				}else{
					int c = saleService.validationShopId(_shopId);
					if(c > 0){
						result.accumulate("success", false);
						result.accumulate("msg", getText("sale.target.error.prompt01"));
					}else{
						saleService.editSaleTarget(saleTargetBean);
						result.accumulate("success", true);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage(),e);
				String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
				result.accumulate("success", false);
				result.accumulate("msg", msg);
			}
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 删除销售目标
	 */
	public void deleteSaleTarget() throws Exception{
		JSONObject result = new JSONObject();
		String id = request.getParameter("id");
		
		try {
			SaleTarget saleTarget = new SaleTarget();
			saleTarget.setSaleTargetId(Integer.valueOf(id));
			
			saleService.deleteSaleTarget(saleTarget);
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
	
	public void exportSaleExcel(){
		try {
			String title = getText("sale.export.file.name");
			String fileName = getExportExcelName(title);
			final String userAgent = request.getHeader("USER-AGENT");
	      if (null != userAgent){    
                if (-1 != userAgent.indexOf("Firefox")) {//Firefox    
                	fileName = new String(fileName.getBytes(), "ISO8859-1");    
                }else if (-1 != userAgent.indexOf("Chrome")) {//Chrome    
                	fileName = new String(fileName.getBytes(), "ISO8859-1");    
                } else {//IE7+    
                	fileName = URLEncoder.encode(fileName, "UTF-8");    
                	fileName = StringUtils.replace(fileName, "+", "%20");//替换空格    
                }    
            } else {    
            	fileName = fileName;    
            }  
		String beginDate="";
		String endDate="";
	  	String searchStr = "1 = 1";
		String searchCdate = request.getParameter("searchCdate");
		if(!"".equals(searchCdate) && searchCdate != null){
			searchStr += " and vs.datadate >= '"+searchCdate+"'";
		}
		String searchDdate = request.getParameter("searchDdate");
		if(!"".equals(searchDdate) && searchDdate != null){
			searchStr += " and vs.datadate <= '"+searchDdate+"'";
		}
		if((searchCdate == null || "".equals(searchCdate) ) && (searchDdate == null || "".equals(searchDdate))){
			Map<String, String>  dateMap=DateUtil.getSaleDate();
			beginDate=dateMap.get("beginDate");
			endDate=dateMap.get("endDate");
			System.out.println("beginDate===="+beginDate);
			System.out.println("endDate================"+endDate);
			searchStr += " and vs.datadate BETWEEN '"+beginDate+"' AND '"+endDate+"'";
		}
		
		String searchParty = request.getParameter("searchParty");
		if(!"".equals(searchParty) && searchParty != null){
			searchStr += " and  p.PARTY_NAME like CONCAT('%','"+searchParty+"','%')";
		}
		String searchCParty = request.getParameter("searchCParty");
		if(!"".equals(searchCParty) && searchCParty != null){
			searchStr += " and cp.PARTY_NAME like CONCAT('%','"+searchCParty+"','%')";
		}
		String searchCustomer = request.getParameter("searchCustomer");
		if(!"".equals(searchCustomer) && searchCustomer != null){
			searchStr += " and vs.customer_name like CONCAT('%','"+searchCustomer+"','%')";
		}
		String searchShop = request.getParameter("searchShop");
		if(!"".equals(searchShop) && searchShop != null){
			searchStr += " and vs.shop_name like CONCAT('%','"+searchShop+"','%')";
		}
		String searchHModel = request.getParameter("searchHModel");
		if(!"".equals(searchHModel) && searchHModel != null){
			searchStr += " and tm.hq_model like CONCAT('%','"+searchHModel+"','%')";
		}
		String searchBModel = request.getParameter("searchBModel");
		if(!"".equals(searchBModel) && searchBModel != null){
			searchStr += " and vs.model like CONCAT('%','"+searchBModel+"','%')";
		}
		
		//String conditions = WebPageUtil.buildDataPermissionSql("party_id", Contents.ROLE_DATA_PERMISSION_PARTY);
		
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		String conditions = "";
		if(!WebPageUtil.isHAdmin())
		{
			if(null!=userPartyIds && !"".equals(userPartyIds))
			{
				conditions += " p.party_id in ("+userPartyIds+")";
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
			//国家化列表头
			String[] excelHeader = {getText("sale.export.th.country"),
					getText("sale.export.th.party"),getText("sale.export.th.customer"),
					getText("sale.export.th.shop"),getText("sale.export.th.hqModel"),
					getText("sale.export.th.bModel"),getText("sale.export.th.quantity"),
					getText("sale.export.th.amount"),getText("sale.export.th.saleDate"),
					getText("sale.export.th.userName")};
			
			HSSFWorkbook workbook = saleService.exporSale(conditions,searchStr,excelHeader,title);
			response.setContentType("application/vnd.ms-excel");   
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
	        OutputStream ouputStream = response.getOutputStream();    
	        workbook.write(ouputStream);    
	        ouputStream.flush();    
	        ouputStream.close();   
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
	}
	
	public void exportSamplesExcel(){
		try {
			String title = getText("samples.export.file.name");
			String fileName = getExportExcelName(title);
			final String userAgent = request.getHeader("USER-AGENT");
	      if (null != userAgent){    
                if (-1 != userAgent.indexOf("Firefox")) {//Firefox    
                	fileName = new String(fileName.getBytes(), "ISO8859-1");    
                }else if (-1 != userAgent.indexOf("Chrome")) {//Chrome    
                	fileName = new String(fileName.getBytes(), "ISO8859-1");    
                } else {//IE7+    
                	fileName = URLEncoder.encode(fileName, "UTF-8");    
                	fileName = StringUtils.replace(fileName, "+", "%20");//替换空格    
                }    
            } else {    
            	fileName = fileName;    
            } 
			
			//权限
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += " data_party_id in ("+userPartyIds+")";
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
			
			//国家化列表头
			String[] excelHeader = {getText("samples.export.th.party"),getText("samples.export.th.customer"),
					getText("samples.export.th.shop"),getText("samples.export.th.hqModel"),
					getText("samples.export.th.bModel"),getText("samples.export.th.quantity"),
					getText("samples.export.th.userName"),getText("samples.export.th.date")};
			
			HSSFWorkbook workbook = saleService.exporSamples(conditions,excelHeader,title);
			response.setContentType("application/vnd.ms-excel");   
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
	        OutputStream ouputStream = response.getOutputStream();    
	        workbook.write(ouputStream);    
	        ouputStream.flush();    
	        ouputStream.close();   
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
	}
	
	private String getExportExcelName(String name){
		String excelName = "";
		try {
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			
			excelName = name + sdf.format(d)+".xls";
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		return excelName;
	}
	
	/**
	 * 获取 销售数据--图表
	 */
	public void getCategorySale(){
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			request.setCharacterEncoding("utf-8");
			//月份查询条件
			String searchStr1 = " ";
			String searchStr = "1 = 1";
			String firstDate = getDateStr(1);
			String currDate = sdf.format(new Date());
			searchStr += " and vs.datadate >= '"+firstDate+"' and vs.datadate < '"+currDate+"'";
			
			//权限
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin()){
				if(null!=userPartyIds && !"".equals(userPartyIds)){
					conditions += " p.party_id in ("+userPartyIds+")";
				}
				else{
					conditions += " 1=2 ";
				}
			}
			else{
				conditions += " 1=1 ";
			}
			
			List<Sale> list = saleService.getSaleCategoryData(searchStr,conditions,searchStr1);
			//legend 
			List<String> legendList = new ArrayList<String>();
			
			//x 轴显示的点数
			String[] xAxisValue = getDateNumber(firstDate,currDate); 
			for (Sale sale : list) {
				String shopName = sale.getShopName();
				if(Collections.frequency(legendList, shopName) < 1){
					legendList.add(shopName);
				}
				int a = getDayByDate(sale.getDatadate());
				xAxisValue[a-1] = sale.getDatadate();
			}

			//数据
			List<Map> dataList = new ArrayList<Map>();
			
			for (int i = 0; i < legendList.size(); i++) {
				String shopName=legendList.get(i).toString().replace("\'", "\\'");
				searchStr1 = " and vs.shop_name = '"+shopName+"'";
				List<Sale> saleList = saleService.getSaleCategoryData(searchStr,conditions,searchStr1);
				Map dataMap = new HashMap();
				
				dataMap.put("name", legendList.get(i).toString());
				
				int[] data = new int[xAxisValue.length];
				for (Sale sale : saleList) {
					int x = getDayByDate(sale.getDatadate());
					data[x-1] = sale.getQuantity();
				}
				dataMap.put("data", data);
				dataList.add(dataMap);
			}
			
			List refList = new ArrayList();
			refList.add(legendList);
			refList.add(xAxisValue);
			refList.add(dataList);
			
			result = JSONArray.fromObject(refList);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 获取 任务进度--图表
	 */
	public void getSalePieDate(){
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try {
			request.setCharacterEncoding("utf-8");
			
			//权限
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			
			String conditions = "";//零售数据权限
			String conditionsToSaleTarget = "";//销售目标权限
			DecimalFormat doubleUtil = new DecimalFormat("#.00");  
			
			if(!WebPageUtil.isHAdmin()){
				if(null!=userPartyIds && !"".equals(userPartyIds)){
					conditions += " a.party_id in ("+userPartyIds+")";
					conditionsToSaleTarget += " v_target.party_id in ("+userPartyIds+")";
					
//					conditionsToSaleTarget += "  v_target.USER_LOGIN_ID in " +
//					"(select DISTINCT m.USER_LOGIN_ID from user_role_mapping m,role_data_permission n " +
//					"where m.ROLE_ID = n.ROLE_ID and n.PERMISSION_TYPE = '"+Contents.ROLE_DATA_PERMISSION_PARTY+"'	and n.permission_value in (" +
//					userPartyIds + "))";
				}
				else{
					conditions += " 1=2 ";
					conditionsToSaleTarget += " 1=2 ";
				}
			}
			else{
				conditions += " 1=1 ";
				conditionsToSaleTarget += " 1=1 ";
			}
			
			String searchStr = "1 = 1";
			String firstDate = getDateStr(1);
			String currDate = sdf.format(new Date());
			searchStr += " and a.datadate >= '"+firstDate+"' and a.datadate < '"+currDate+"'";
			
			int saleData = saleService.getSaleCompletionList(conditions,searchStr);
			int saleTargetData = saleService.getSaleTargetCompletionList(conditionsToSaleTarget);
			
			double sd = 0.0;//零售数量  显示给用户看的数据
			double st = 0.0;//零售目标  目标值
			if(saleTargetData == 0){
				sd = 100.0;
				st = 0.0;
			}else{
				sd = Double.valueOf(doubleUtil.format((Double.valueOf(saleData)/Double.valueOf(saleTargetData))*100));
				st = 100 - sd;
			}
			List<Map> lm = new ArrayList<Map>();
			Map m = new HashMap();
			m.put("saleData", sd);
			m.put("saleTar", st);
			lm.add(m);
				
			result = JSONArray.fromObject(lm);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 获取 尺寸占比数据--图表
	 */
	public void getSizePiceData(){
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try {
			request.setCharacterEncoding("utf-8");
			
			//权限
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin()){
				if(null!=userPartyIds && !"".equals(userPartyIds)){
					conditions += " ps.party_id in ("+userPartyIds+")";
				}
				else{
					conditions += " 1=2 ";
				}
			}
			else{
				conditions += " 1=1 ";
			}
			
			String searchStr = "1=1 ";
			String firstDate = getDateStr(1);
			String currDate = sdf.format(new Date());
			searchStr += " and ps.datadate >= '"+firstDate+"' and ps.datadate < '"+currDate+"'";
			
			List<Sale> list = saleService.getProductSizeSaleList(conditions,searchStr);
			
			result = JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 
	 * @return
	 */
	private String getDateStr(int day){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, day);
		String firstDate = sdf.format(c.getTime());
		return firstDate;
	}
	
	private String[] getDateNumber(String time,String time1){
		int days = getDateSpace(time,time1);
		String[] s = new String[days];
		for (int i = 0; i < s.length; i++) {
			String dateStr = getDateStr(i+1);
			s[i] = dateStr;
		}
		return s;
	}
	
	/**
	 * 获取两个时间的天数
	 * 时间的格式给 yyyy-MM-dd
	 * @param time   月头1号
	 * @param time1   当前时间
	 * @return
	 */
	private int getDateSpace(String time,String time1){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int day = 0;
		try {
			Calendar c = Calendar.getInstance();
			Calendar c1 = Calendar.getInstance();
			
			c.setTime(sdf.parse(time));
			c1.setTime(sdf.parse(time1));
			
			 c.set(Calendar.HOUR_OF_DAY, 0);   
	         c.set(Calendar.MINUTE, 0);   
	         c.set(Calendar.SECOND, 0);
	         c1.set(Calendar.HOUR_OF_DAY, 0);   
	         c1.set(Calendar.MINUTE, 0);   
	         c1.set(Calendar.SECOND, 0);
	         
	         if(c.getTime() == c1.getTime()){
	        	 day = 1;
	         }else{
	        	 day = ((int)(c1.getTime().getTime()/1000)-(int)(c.getTime().getTime()/1000))/3600/24;
	         }
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		return day;
	}
	
	/**
	 * 日期格式为 yyyy-MM-dd
	 * @param date
	 * @return
	 */
	private int getDayByDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int day = 0;
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(date));
			day = c.get(Calendar.DAY_OF_MONTH);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		return day;
	}
	
	
	
	public void importSales() {
		try{
			String path = getClassRealPath()+ File.separatorChar+"cn"+File.separatorChar+"tcl"+File.separatorChar+"platform" +
			File.separatorChar +"sale"+File.separatorChar+"imports"+File.separatorChar+"imports.xml";
			
			ExcelImportUtil export = new ExcelImportUtil(path);
			export.init(uploadExcel,uploadExcelFileName,WebPageUtil.getLanguage());
			List<Sale> sales = export.bindToModelsAndImport(Sale.class);
			String errorMsg = export.getError();
			if("".equals(errorMsg)){
				WebPageUtil.writeBack("success");
			}
			else{
				WebPageUtil.writeBack(errorMsg);
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
	
	
	public void importStore(){
		String targetType = request.getParameter("targetType");
		try {

			String errorMsg = saleService.readExcel(uploadExcel,
					uploadExcelFileName,targetType);

			if ("".equals(errorMsg)) {
				WebPageUtil.writeBack("success");
			} else {
				WebPageUtil.writeBack(errorMsg);
			}
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			if (null == errorMsg || "".equals(errorMsg)) {
				errorMsg = this.getText("import.error.exist");
			}
			log.error(e.getStackTrace());
			e.printStackTrace();
			WebPageUtil.writeBack(errorMsg);
		}
	}
	
	
	
	
	public void loadSaleListDataByMobile(){
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
			
			String searchStr = "1 = 1";
			String searchCdate = request.getParameter("searchCdate");
			if(!"".equals(searchCdate) && searchCdate != null){
				searchStr += " and a.datadate >= '"+searchCdate+"'";
			}
			String searchDdate = request.getParameter("searchDdate");
			if(!"".equals(searchDdate) && searchDdate != null){
				searchStr += " and a.datadate <= '"+searchDdate+"'";
			}
			String searchParty = request.getParameter("searchParty");
			if(!"".equals(searchParty) && searchParty != null){
				searchStr += " and a.PARTY_NAME like CONCAT('%','"+searchParty+"','%')";
			}
			String searchCParty = request.getParameter("searchCParty");
			if(!"".equals(searchCParty) && searchCParty != null){
				searchStr += " and a.country like CONCAT('%','"+searchCParty+"','%')";
			}
			String searchCustomer = request.getParameter("searchCustomer");
			if(!"".equals(searchCustomer) && searchCustomer != null){
				searchStr += " and a.customer_name like CONCAT('%','"+searchCustomer+"','%')";
			}
			String searchShop = request.getParameter("searchShop");
			if(!"".equals(searchShop) && searchShop != null){
				searchStr += " and a.shop_name like CONCAT('%','"+searchShop+"','%')";
			}
			
			//String conditions = WebPageUtil.buildDataPermissionSql("party_id", Contents.ROLE_DATA_PERMISSION_PARTY);
			
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += " party_id in ("+userPartyIds+")";
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
			
			Map<String, Object> map = saleService.selectSalesDataByMobile(start, limit, searchStr, order, sort,conditions);//conditions
			int total = (Integer)map.get("total");
			List<Sale> list = (ArrayList<Sale>)map.get("rows");
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
	
	
	
	//AC销售目标，门店目标
	public void selectACStoreTargets(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("partyId");
		String datadate = request.getParameter("datadate");
		try {
			List<SaleTarget> storelist = saleService.selectACSaleTargets(
					datadate, partyId);
			String rows = JSONArray.fromObject(storelist).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
		
	//渠道目标
	public void selectACChannelTargets(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("partyId");
		String datadate = request.getParameter("datadate");
		try {
			List<SaleTarget> officelist = saleService.selectACChannelTargets(
					datadate, partyId);
			String rows = JSONArray.fromObject(officelist).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
		
	//办事处目标
	public void selectACOfficeTargets(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("partyId");
		String datadate = request.getParameter("datadate");
		try {
			List<SaleTarget> officelist = saleService.selectACOfficeTargets(
					datadate, partyId);
			String rows = JSONArray.fromObject(officelist).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
		
	//区域目标
	public void selectACRegionTargets(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("partyId");
		String datadate = request.getParameter("datadate");
		try {
			List<SaleTarget> officelist = saleService.selectACRegionTargets(
					datadate, partyId);
			String rows = JSONArray.fromObject(officelist).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
			
	//分公司目标
	public void selectACBranchTargets(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("partyId");
		String datadate = request.getParameter("datadate");
		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		String conditions = "";
		if (!WebPageUtil.isHQRole()) {
			if (null != userPartyIds && !"".equals(userPartyIds)) {
				conditions += " si.`COUNTRY_ID` in (select distinct tt.country_id from party tt where tt.party_id in ("
						+ userPartyIds + "))";
				conditions += "or p.PARENT_PARTY_ID IN(SELECT PARTY_ID FROM party WHERE PARENT_PARTY_ID in ("
						+ userPartyIds + "))";
			} else {
				conditions += " 1=2 ";
			}
		} else {
			conditions += " 1=1 ";
		}
		try {
			List<SaleTarget> officelist = saleService.selectACBranchTargets(
					conditions, datadate, partyId);
			String rows = JSONArray.fromObject(officelist).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
		
	
	//督导目标
	public void selectACSupervisorTargets(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("partyId");
		String datadate = request.getParameter("datadate");
		try {
			List<SaleTarget> officelist = saleService
					.selectACSupervisorTargets(datadate, partyId);
			String rows = JSONArray.fromObject(officelist).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}

	//业务员目标
	public void selectACSalesmanTargets(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("partyId");
		String datadate = request.getParameter("datadate");
		try {
			List<SaleTarget> officelist = saleService.selectACSalesmanTargets(
					datadate, partyId);
			String rows = JSONArray.fromObject(officelist).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	//业务经理目标
	public void selectACBusinessTargets(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("partyId");
		String datadate = request.getParameter("datadate");
		try {		 
			List<SaleTarget> officelist = saleService.selectACBusinessTargets(datadate,partyId);
			String rows = JSONArray.fromObject(officelist).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
	public void importACtarget(){
		String targetType = request.getParameter("targetType"); 
		try {

			String errorMsg = saleService.readACExcel(uploadExcel,
					uploadExcelFileName,targetType);

			if ("".equals(errorMsg)) {
				WebPageUtil.writeBack("success");
			} else {
				WebPageUtil.writeBack(errorMsg);
			}
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			if (null == errorMsg || "".equals(errorMsg)) {
				errorMsg = this.getText("import.error.exist");
			}
			log.error(e.getStackTrace());
			e.printStackTrace();
			WebPageUtil.writeBack(errorMsg);
		}
	}
	
	//----------------------------------------渠道级-----------------------------------
	//获取导入渠道目标(TV)
	public void getChannelTarget(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String datadate = request.getParameter("datadate");
		String partyId = request.getParameter("partyId");
		try {
			List<SaleTarget> list = saleService.getChannelTarget(datadate, partyId);
			String rows = JSONArray.fromObject(list).toString();
			result.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 渠道分公司目标(TV)
	 */
	public void getBranchTarget(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String datadate = request.getParameter("datadate");
		String partyId = request.getParameter("partyId");
		try {
			List<SaleTarget> list = saleService.getBranchTarget(datadate, partyId);
			String rows = JSONArray.fromObject(list).toString();
			result.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 渠道督导目标(TV)
	 */
	public void getSupervisorTarget(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String datadate = request.getParameter("datadate");
		String partyId = request.getParameter("partyId");
		try {
			List<SaleTarget> list = saleService.getSupervisorTarget(datadate, partyId);
			String rows = JSONArray.fromObject(list).toString();
			result.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 渠道业务员目标(TV)
	 */
	public void getSalemanTarget(){
		JSONObject result= new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String datadate = request.getParameter("datadate");
		String partyId = request.getParameter("partyId");
		try {
			List<SaleTarget> list = saleService.getSalemanTarget(datadate, partyId);
			String rows = JSONArray.fromObject(list).toString();
			result.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 渠道业务经理目标数据(TV)
	 */
	public void getBusinessTarget(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String datadate = request.getParameter("datadate");
		String partyId = request.getParameter("partyId");
		try {
			List<SaleTarget> list = saleService.getBusinessTarget(datadate, partyId);
			String rows = JSONArray.fromObject(list).toString();
			result.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 获取导入国家级目标数据（TV）
	 */
	public void getBranchTargetList(){
		JSONObject result =new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String datadate = request.getParameter("datadate");
		String partyId = request.getParameter("partyId");
		try {
			List<SaleTarget> list = saleService.getBranchTargetList(datadate, partyId);
			String rows = JSONArray.fromObject(list).toString();
			result.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 获取导入渠道目标数据（AC）
	 */
	public void getACChannelTarget(){
		JSONObject result = new JSONObject();
		String datadate = request.getParameter("datadate");
		String partyId = request.getParameter("partyId");
		response.setHeader("Content-Type", "application/json");
		try {
			List<SaleTarget> list = saleService.getACChannelTarget(datadate, partyId);
			String rows = JSONArray.fromObject(list).toString();
			result.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 获取导入渠道分公司目标数据（AC）
	 */
	public void getACBranchTarget(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		
		String datadate = request.getParameter("datadate");
		String partyId = request.getParameter("partyId");
		
		try {
			List<SaleTarget> list = saleService.getACBranchTarget(datadate, partyId);
			String rows = JSONArray.fromObject(list).toString();
			result.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 获取导入督导目标数据（AC）
	 */
	public void getACSupervisorTarget(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-type", "application/json");
		String datadate = request.getParameter("datadate");
		String partyId = request.getParameter("partyId");
		
		try {
			List<SaleTarget> list = saleService.getACSupervisorTarget(datadate, partyId);
			String rows = JSONArray.fromObject(list).toString();
			result.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 获取导入渠道业务员目标数据（AC）
	 */
	public void getACSalemanTarget(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-type", "application/json");
		String datadate = request.getParameter("datadate");
		String partyId = request.getParameter("partyId");
		
		try {
			List<SaleTarget> list = saleService.getACSalemanTarget(datadate, partyId);
			String rows = JSONArray.fromObject(list).toString();
			result.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 获取导入渠道业务经理目标数据（AC）
	 */
	public void getACBussinessTarget(){
		JSONObject  result = new JSONObject();
		response.setHeader("Content-type", "application/json");
		String datadate = request.getParameter("datadate");
		String partyId = request.getParameter("partyId");
		
		try {
			List<SaleTarget> list = saleService.getACBussinessTarget(datadate, partyId);
			String rows = JSONArray.fromObject(list).toString();
			result.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 获取导入分公司目标数据（AC）
	 */
	public void getACBranchTagetList(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-type", "application/json");
		
		String datadate = request.getParameter("datadate");
		String partyId = request.getParameter("partyId");
		
		try {
			List<SaleTarget> list = saleService.getACBranchTagetList(datadate, partyId);
			String rows = JSONArray.fromObject(list).toString();
			result.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 登陆总部用户显示分公司目标数据(TV)
	 */
	public void getOBCTVBranchTarget(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-type", "application/json");
		
		String datadate = request.getParameter("datadate");
		String partyId = request.getParameter("partyId");
		
		try {
			List<SaleTarget> list = saleService.getOBCTVBranchTarget(datadate, partyId);
			String rows = JSONArray.fromObject(list).toString();
			result.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 	登陆总部用户显示分公司目标数据（AC）
	 */
	public void getOBCACBranchTarget(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-type", "application/json");
		
		String datadate = request.getParameter("datadate");
		String partyId = request.getParameter("partyId");
		
		try {
			List<SaleTarget> list = saleService.getOBCACBranchTarget(datadate, partyId);
			String rows = JSONArray.fromObject(list).toString();
			result.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
	
	
	
	
	public void loadCountrySaleListData(){
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
			
			String searchStr = "1 = 1";
			String searchCdate = request.getParameter("searchCdate");
			if(!"".equals(searchCdate) && searchCdate != null){
				searchStr += " and vs.datadate >= '"+searchCdate+"'";
			}
			String searchDdate = request.getParameter("searchDdate");
			if(!"".equals(searchDdate) && searchDdate != null){
				searchStr += " and vs.datadate <= '"+searchDdate+"'";
			}
			
			if((searchCdate == null || "".equals(searchCdate) ) && (searchDdate == null || "".equals(searchDdate))){
				Map<String, String>  dateMap=DateUtil.getSaleDate();
				String beginDate=dateMap.get("beginDate");
				String endDate=dateMap.get("endDate");
				System.out.println("beginDate===="+beginDate);
				System.out.println("endDate================"+endDate);
				searchStr += " and vs.datadate BETWEEN '"+beginDate+"' AND '"+endDate+"'";
			}
		
			
			String searchCParty = request.getParameter("searchCParty");
			if(!"".equals(searchCParty) && searchCParty != null){
				searchStr += " and cp.PARTY_NAME like CONCAT('%','"+searchCParty+"','%')";
			}
		
			String searchHModel = request.getParameter("searchHModel");
			if(!"".equals(searchHModel) && searchHModel != null){
				searchStr += " and tm.hq_model like CONCAT('%','"+searchHModel+"','%')";
			}
			String searchBModel = request.getParameter("searchBModel");
			if(!"".equals(searchBModel) && searchBModel != null){
				searchStr += " and vs.model like CONCAT('%','"+searchBModel+"','%')";
			}
			
			//String conditions = WebPageUtil.buildDataPermissionSql("party_id", Contents.ROLE_DATA_PERMISSION_PARTY);
			
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += " cp.`COUNTRY_ID` in ("+userPartyIds+")";
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
			
			Map<String, Object> map = saleService.selectCountrySalesData(start, limit, searchStr, order, sort,conditions);//conditions
			int total = (Integer)map.get("total");
			List<Sale> list = (ArrayList<Sale>)map.get("rows");
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
	public void loadCustomerSaleListData(){

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
			
			String searchStr = "1 = 1";
			String searchCdate = request.getParameter("searchCdate");
			if(!"".equals(searchCdate) && searchCdate != null){
				searchStr += " and vs.datadate >= '"+searchCdate+"'";
			}
			String searchDdate = request.getParameter("searchDdate");
			if(!"".equals(searchDdate) && searchDdate != null){
				searchStr += " and vs.datadate <= '"+searchDdate+"'";
			}
			
			if((searchCdate == null || "".equals(searchCdate) ) && (searchDdate == null || "".equals(searchDdate))){
				Map<String, String>  dateMap=DateUtil.getSaleDate();
				String beginDate=dateMap.get("beginDate");
				String endDate=dateMap.get("endDate");
				System.out.println("beginDate===="+beginDate);
				System.out.println("endDate================"+endDate);
				searchStr += " and vs.datadate BETWEEN '"+beginDate+"' AND '"+endDate+"'";
			}
			String searchParty = request.getParameter("searchParty");
			if(!"".equals(searchParty) && searchParty != null){
				searchStr += " and  p.PARTY_NAME like CONCAT('%','"+searchParty+"','%')";
			}
			String searchCParty = request.getParameter("searchCParty");
			if(!"".equals(searchCParty) && searchCParty != null){
				searchStr += " and cp.PARTY_NAME like CONCAT('%','"+searchCParty+"','%')";
			}
			String searchCustomer = request.getParameter("searchCustomer");
			if(!"".equals(searchCustomer) && searchCustomer != null){
				searchStr += " and ci.customer_name like CONCAT('%','"+searchCustomer+"','%')";
			}
			
			String searchHModel = request.getParameter("searchHModel");
			if(!"".equals(searchHModel) && searchHModel != null){
				searchStr += " and tm.hq_model like CONCAT('%','"+searchHModel+"','%')";
			}
			String searchBModel = request.getParameter("searchBModel");
			if(!"".equals(searchBModel) && searchBModel != null){
				searchStr += " and vs.model like CONCAT('%','"+searchBModel+"','%')";
			}
			
			//String conditions = WebPageUtil.buildDataPermissionSql("party_id", Contents.ROLE_DATA_PERMISSION_PARTY);
			
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += " (p.party_id in ("+userPartyIds+") or ci.party_id is null or cp.party_id in ("+userPartyIds+") )";
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
			
			Map<String, Object> map = saleService.selectCustomerSalesData(start, limit, searchStr, order, sort,conditions);//conditions
			int total = (Integer)map.get("total");
			List<Sale> list = (ArrayList<Sale>)map.get("rows");
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
	
	
	public void exportCountrySaleExcel(){
		try {
			String title = getText("sale.export.file.name");
			String fileName = getExportExcelName(title);
			final String userAgent = request.getHeader("USER-AGENT");
	      if (null != userAgent){    
                if (-1 != userAgent.indexOf("Firefox")) {//Firefox    
                	fileName = new String(fileName.getBytes(), "ISO8859-1");    
                }else if (-1 != userAgent.indexOf("Chrome")) {//Chrome    
                	fileName = new String(fileName.getBytes(), "ISO8859-1");    
                } else {//IE7+    
                	fileName = URLEncoder.encode(fileName, "UTF-8");    
                	fileName = StringUtils.replace(fileName, "+", "%20");//替换空格    
                }    
            } else {    
            	fileName = fileName;    
            }  
	      String searchStr = "1 = 1";
			String searchCdate = request.getParameter("searchCdate");
			if(!"".equals(searchCdate) && searchCdate != null){
				searchStr += " and vs.datadate >= '"+searchCdate+"'";
			}
			String searchDdate = request.getParameter("searchDdate");
			if(!"".equals(searchDdate) && searchDdate != null){
				searchStr += " and vs.datadate <= '"+searchDdate+"'";
			}
			
			if((searchCdate == null || "".equals(searchCdate) ) && (searchDdate == null || "".equals(searchDdate))){
				Map<String, String>  dateMap=DateUtil.getSaleDate();
				String beginDate=dateMap.get("beginDate");
				String endDate=dateMap.get("endDate");
				System.out.println("beginDate===="+beginDate);
				System.out.println("endDate================"+endDate);
				searchStr += " and vs.datadate BETWEEN '"+beginDate+"' AND '"+endDate+"'";
			}
		
			
			String searchCParty = request.getParameter("searchCParty");
			if(!"".equals(searchCParty) && searchCParty != null){
				searchStr += " and cp.PARTY_NAME like CONCAT('%','"+searchCParty+"','%')";
			}
		
			String searchHModel = request.getParameter("searchHModel");
			if(!"".equals(searchHModel) && searchHModel != null){
				searchStr += " and tm.hq_model like CONCAT('%','"+searchHModel+"','%')";
			}
			String searchBModel = request.getParameter("searchBModel");
			if(!"".equals(searchBModel) && searchBModel != null){
				searchStr += " and vs.model like CONCAT('%','"+searchBModel+"','%')";
			}
			
			//String conditions = WebPageUtil.buildDataPermissionSql("party_id", Contents.ROLE_DATA_PERMISSION_PARTY);
			
			String userPartyIds = WebPageUtil.getLoginedUser().getPartyId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += " cp.`COUNTRY_ID` in ("+userPartyIds+")";
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
			//国家化列表头
			String[] excelHeader = {getText("sale.export.th.country")
					,getText("sale.export.th.hqModel"),
					getText("sale.export.th.bModel"),getText("sale.export.th.quantity"),
					getText("sale.export.th.amount"),getText("sale.export.th.saleDate"),
					getText("sale.export.th.userName")};
			
			HSSFWorkbook workbook = saleService.exporCountrySale(conditions,searchStr,excelHeader,title);
			response.setContentType("application/vnd.ms-excel");   
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
	        OutputStream ouputStream = response.getOutputStream();    
	        workbook.write(ouputStream);    
	        ouputStream.flush();    
	        ouputStream.close();   
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
	}
	
	
	public void exportCustomerSaleExcel(){
		try {
			String title = getText("sale.export.file.name");
			String fileName = getExportExcelName(title);
			final String userAgent = request.getHeader("USER-AGENT");
	      if (null != userAgent){    
                if (-1 != userAgent.indexOf("Firefox")) {//Firefox    
                	fileName = new String(fileName.getBytes(), "ISO8859-1");    
                }else if (-1 != userAgent.indexOf("Chrome")) {//Chrome    
                	fileName = new String(fileName.getBytes(), "ISO8859-1");    
                } else {//IE7+    
                	fileName = URLEncoder.encode(fileName, "UTF-8");    
                	fileName = StringUtils.replace(fileName, "+", "%20");//替换空格    
                }    
            } else {    
            	fileName = fileName;    
            }  
	      String searchStr = "1 = 1";
			String searchCdate = request.getParameter("searchCdate");
			if(!"".equals(searchCdate) && searchCdate != null){
				searchStr += " and vs.datadate >= '"+searchCdate+"'";
			}
			String searchDdate = request.getParameter("searchDdate");
			if(!"".equals(searchDdate) && searchDdate != null){
				searchStr += " and vs.datadate <= '"+searchDdate+"'";
			}
			
			if((searchCdate == null || "".equals(searchCdate) ) && (searchDdate == null || "".equals(searchDdate))){
				Map<String, String>  dateMap=DateUtil.getSaleDate();
				String beginDate=dateMap.get("beginDate");
				String endDate=dateMap.get("endDate");
				System.out.println("beginDate===="+beginDate);
				System.out.println("endDate================"+endDate);
				searchStr += " and vs.datadate BETWEEN '"+beginDate+"' AND '"+endDate+"'";
			}
			String searchParty = request.getParameter("searchParty");
			if(!"".equals(searchParty) && searchParty != null){
				searchStr += " and  p.PARTY_NAME like CONCAT('%','"+searchParty+"','%')";
			}
			String searchCParty = request.getParameter("searchCParty");
			if(!"".equals(searchCParty) && searchCParty != null){
				searchStr += " and cp.PARTY_NAME like CONCAT('%','"+searchCParty+"','%')";
			}
			String searchCustomer = request.getParameter("searchCustomer");
			if(!"".equals(searchCustomer) && searchCustomer != null){
				searchStr += " and ci.customer_name like CONCAT('%','"+searchCustomer+"','%')";
			}
			
			String searchHModel = request.getParameter("searchHModel");
			if(!"".equals(searchHModel) && searchHModel != null){
				searchStr += " and tm.hq_model like CONCAT('%','"+searchHModel+"','%')";
			}
			String searchBModel = request.getParameter("searchBModel");
			if(!"".equals(searchBModel) && searchBModel != null){
				searchStr += " and vs.model like CONCAT('%','"+searchBModel+"','%')";
			}
			
			//String conditions = WebPageUtil.buildDataPermissionSql("party_id", Contents.ROLE_DATA_PERMISSION_PARTY);
			
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += " (p.party_id in ("+userPartyIds+") or ci.party_id is null or cp.party_id in ("+userPartyIds+") )";
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
			//国家化列表头
			String[] excelHeader = {getText("sale.export.th.country"),
					getText("sale.export.th.party"),getText("sale.export.th.customer"),
					getText("sale.export.th.hqModel"),
					getText("sale.export.th.bModel"),getText("sale.export.th.quantity"),
					getText("sale.export.th.amount"),getText("sale.export.th.saleDate"),
					getText("sale.export.th.userName")};
			
			HSSFWorkbook workbook = saleService.exporCustomerSale(conditions,searchStr,excelHeader,title);
			response.setContentType("application/vnd.ms-excel");   
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
	        OutputStream ouputStream = response.getOutputStream();    
	        workbook.write(ouputStream);    
	        ouputStream.flush();    
	        ouputStream.close();   
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
	}
}
