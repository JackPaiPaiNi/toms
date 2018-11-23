package cn.tcl.platform.customer.actions;

import java.io.File;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.excel.imports.ExcelImportUtil;
import cn.tcl.platform.barcode.vo.Barcode;
import cn.tcl.platform.customer.service.ICustomerService;
import cn.tcl.platform.customer.vo.Customer;
import cn.tcl.platform.customer.vo.CustomerUser;
import cn.tcl.platform.district.vo.Country;
import cn.tcl.platform.party.vo.Party;
import cn.tcl.platform.shop.vo.ShopParty;
import cn.tcl.platform.user.vo.UserLogin;
import javassist.compiler.ast.Keyword;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.WebUtils;
@SuppressWarnings("all")
public class UiCustomerAction  extends BaseAction{
	@Autowired(required = false)
	@Qualifier("customerService")
	private ICustomerService customerService;
	
	public String loadCustomerPage(){
		try{
			return SUCCESS;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	public void loadCustomerListData(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try{
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			String bt = request.getParameter("bt");
			int page = Integer.valueOf(request.getParameter("page"));
			int limit = Integer.valueOf(request.getParameter("rows"));
			int start = (page-1)*limit;
			
			String searchStr = "1=1 ";
			
			String countryId=request.getParameter("countryId");
			String provinceId=request.getParameter("provinceId");
			String searchCustomer = request.getParameter("searchCustomer");
			if(!"".equals(searchCustomer) && searchCustomer != null){
				searchCustomer=searchCustomer.replace("\'", "\\'");
				searchStr += " and c.CUSTOMER_NAME like CONCAT('%','"+searchCustomer+"','%' )";
			}
			String searchParty = request.getParameter("searchParty");
			if(!"".equals(searchParty) && searchParty != null){
				searchParty=searchParty.replace("\'", "\\'");
				searchStr += " and a.PARTY_NAME like CONCAT('%','"+searchParty+"','%' )";
			}
			
			//过滤数据权限
			//String conditions = WebPageUtil.buildDataPermissionSql("c.party_id", Contents.ROLE_DATA_PERMISSION_PARTY);
			
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += " c.party_id in ("+userPartyIds+")";
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
			
			//order为排序字段CREATE_DATE，sort为排序方向asc,desc
			//countryId,provinceId为过滤数据的字段
			Map<String, Object> map = customerService.selectCustomersData(start, limit, searchStr,countryId,provinceId, order, sort,conditions,bt);
			int total = (Integer)map.get("total");
			List<UserLogin> list = (ArrayList<UserLogin>)map.get("rows");
			JSONArray jsonArray = JSONArray.fromObject(list);
			String rows = jsonArray.toString();
			result.accumulate("rows", rows);
			result.accumulate("total", total);
			result.accumulate("success", true);
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", true);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
	
	public void addCustomer(){
		JSONObject result = new JSONObject();
		
		String customerName=request.getParameter("customerName");
		String enterdate=request.getParameter("enterDateStr");
		String party=request.getParameter("partyId");
		String country=request.getParameter("countryId");
		String province=request.getParameter("provinceId");
		String city=request.getParameter("cityId");
		String county=request.getParameter("countyId");
		String town=request.getParameter("townId");
		String address=request.getParameter("detailAddress");
		String contactName=request.getParameter("contactName");
		String phone=request.getParameter("phone");
		String email=request.getParameter("email");
		String status=request.getParameter("status");
		String website=request.getParameter("website");
		String customerCode=request.getParameter("customerCode");
		String channelType=request.getParameter("channelType");
		String customerAname=request.getParameter("aname");
		
		String salers = request.getParameter("salParam");
		String supers = request.getParameter("supsParam");
		String proers = request.getParameter("prosParam");
		
		
		
		Customer customer=new Customer();
		//customer.setCustomerId(UUID.randomUUID().toString());
		customer.setCityId(city);//TODO
		customer.setContactName(contactName);
		customer.setCountryId(country);//TODO
		customer.setCountyId(county);
		String loginUserId = (String)request.getSession().getAttribute("loginUserId");
		customer.setCreateBy(loginUserId);//TODO
		customer.setCreateDate(new Date());
		customer.setCustomerName(customerName);
		customer.setDetailAddress(address);
		customer.setEmail(email);
		Date enterDate2=WebPageUtil.strToDate(enterdate);
		customer.setEnterDate(enterDate2);//todo
		customer.setPartyId(party);//todo
		customer.setPhone(phone);
		customer.setProvinceId(province);//todo
		customer.setStatus(status);
		customer.setTownId(town);//TODO
		customer.setWebsite(website);
		customer.setCustomerCode(customerCode);
		customer.setChannelType(channelType);
		customer.setCustomerAname(customerAname);
		
		try {
			Customer mc = customerService.getRepeatByCustomerCode(customerCode);
			if(mc == null){
//				customerService.saveCustomer(customer);
				customerService.saveCustomer(customer,salers,proers,supers);
				result.accumulate("success", true);
			}else{
				result.accumulate("success", false);
				result.accumulate("msg", getText("customer.error.prompt05"));
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
	public void editCustomer(){
		JSONObject result = new JSONObject();
		
		String id=request.getParameter("editId");
		if(id!=null && id.trim()!=""){
			try {
				String customerName=request.getParameter("customerName");
				String enterdate=request.getParameter("enterDateStr");
				String party=request.getParameter("partyId");
				String country=request.getParameter("countryId");
				String province=request.getParameter("provinceId");
				String city=request.getParameter("cityId");
				String county=request.getParameter("countyId");
				String town=request.getParameter("townId");
				String address=request.getParameter("detailAddress");
				String contactName=request.getParameter("contactName");
				String phone=request.getParameter("phone");
				String email=request.getParameter("email");
				String status=request.getParameter("status");//TODO 绑定问题  加判断
				String website=request.getParameter("website");
				String customerCode=request.getParameter("customerCode");
				String channelType=request.getParameter("channelType");
				String aname=request.getParameter("aname");
				
				String salers = request.getParameter("salParam");
				String supers = request.getParameter("supsParam");
				String proers = request.getParameter("prosParam");
				
				Customer customer=customerService.getCustomer(id);//new Customer();
				String code = customer.getCustomerCode();
				customer.setCityId(city);//TODO
				customer.setContactName(contactName);
				customer.setCountryId(country);//TODO
				customer.setCountyId(county);
				customer.setCustomerName(customerName);
				customer.setDetailAddress(address);
				customer.setEmail(email);
				Date enterDate2=WebPageUtil.strToDate(enterdate);
				customer.setEnterDate(enterDate2);//todo
				customer.setPartyId(party);//todo
				customer.setPhone(phone);
				customer.setProvinceId(province);//todo
				customer.setCustomerAname(aname);
				if("0".equals(status) || "1".equals(status) || "2".equals(status)){
					customer.setStatus(status);
				}
				customer.setTownId(town);//TODO
				customer.setWebsite(website);
				customer.setCustomerCode(customerCode);
				customer.setChannelType(channelType);
			
				if(customerCode.equals(code)){
//					customerService.editCustomer(customer);
					customerService.editCustomer(customer,salers,proers,supers);
					result.accumulate("success", true);
				}else{
					Customer mc = customerService.getRepeatByCustomerCode(customerCode);
					if(mc == null){
//						customerService.editCustomer(customer);
						customerService.editCustomer(customer,salers,proers,supers);
						result.accumulate("success", true);
					}else{
						result.accumulate("success", false);
						result.accumulate("msg", getText("customer.error.prompt05"));
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
	public void importCustomer() {
		try{
			String path = getClassRealPath()+ File.separatorChar+"cn"+File.separatorChar+"tcl"+File.separatorChar+"platform" +
			File.separatorChar +"customer"+File.separatorChar+"imports"+File.separatorChar+"importsEN.xml";
			
			ExcelImportUtil export = new ExcelImportUtil(path);
			export.init(uploadExcel,uploadExcelFileName,WebPageUtil.getLanguage());
			
			List<Customer> customers = export.bindToModelsAndImport(Customer.class);
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
	
	public void deleteCustomer(){
		JSONObject result = new JSONObject();
		String _id = request.getParameter("id");
		try {
			Customer customer = new Customer();
			customer.setCustomerId(Integer.valueOf(_id));
			customer.setDelUserId(WebPageUtil.getLoginedUserId());
			
			customerService.deleteCustomer(customer);
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
	
	
	
	public void ExportDealerName(){
		try {
			String title ="Dealer Name";
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
			
	      String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds)){
					conditions += "  pa.party_id IN ("+userPartyIds+") ";
				}else{
					conditions += "  1=2 ";
				}
			}
			else
			{
				conditions += "  1=1 ";
			}
			
			//国家化列表头
			String[] excelHeader = {"Dealer Name","Dealer Code"};
			XSSFWorkbook workbook = customerService.exporDealerName(conditions, excelHeader, title);
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
			
			excelName = name +"("+sdf.format(d)+")"+".xlsx";
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		return excelName;
	}
	
	public void loadPartyList(){
//		JSONObject result = new JSONObject();
//		response.setHeader("Content-Type", "application/json");
//		try {
//			request.setCharacterEncoding("UTF-8");
//			String countryId = request.getParameter("countryId");
//			List<Party> partyList = customerService.selectParty(countryId);
//			System.out.println(countryId+"=====================");
//			String rows = JSONArray.fromObject(partyList).toString();
//			result.accumulate("rows", rows);
//		} catch (Exception e) {			
//			e.printStackTrace();
//		}
//		WebPageUtil.writeBack(result.toString());
		
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try {
			request.setCharacterEncoding("utf-8");
			String countryId = request.getParameter("countryId");
			List<Party> partyList = customerService.selectParty(countryId);
			result = JSONArray.fromObject(partyList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void loadCustomerSalesData(){
		JSONObject result = new JSONObject();
		String partyId = request.getParameter("partyId");
		String customerId = request.getParameter("customerId");
		response.setHeader("Content-Type", "application/json");
		
		System.out.println(customerId+"------------------------------------");
		
		
		String conditions="";
		
		String userPartys = WebPageUtil.loadPartyIdsByUserId();
		try {
			if(partyId!=null && !"".equals(partyId)){
				conditions+=" and ul.party_id ="+partyId+"";
			}
//			if(!WebPageUtil.isHAdmin()){
//				if(userPartys!=null && !"".equals(userPartys)){
//					conditions +=" and ul.party_id in ("+userPartys+")";
//				}else{
//					conditions +=" and 1=2";
//				}
//			}else{
//				conditions+=" and 1=1";
//			}
			
			String searchBus = request.getParameter("searchBus");
			if(searchBus!=null && !"".equals(searchBus)){
				conditions+=" and ul.user_name like concat ('%','"+searchBus+"','%')";
			}
			
			String searchprom = request.getParameter("searchprom");
			if(searchprom!=null && !"".equals(searchprom)){
				conditions+=" and ul.user_name like concat ('%','"+searchprom+"','%')";
			}
			
			String searchSup = request.getParameter("searchSup");
			if(searchSup!=null && !"".equals(searchSup)){
				conditions+=" and ul.user_name like concat ('%','"+searchSup+"','%')";
			}
			
			List<UserLogin> list = customerService.selectCustomerSaler(conditions, 0,null);
			JSONArray salers = new JSONArray();
			for (UserLogin user : list) {
				JSONObject item = new JSONObject();
				item.put("id", user.getUserLoginId());
				item.put("text", user.getUserName());
				salers.add(item);
			}
			result.accumulate("salers", salers);
			
			
			List<UserLogin> list2 = customerService.selectCustomerSaler(conditions, 1,customerId);
			JSONArray pro = new JSONArray();
			for (UserLogin user2 : list2) {
				JSONObject item = new JSONObject();
				item.put("id", user2.getUserLoginId());
				item.put("text", user2.getUserName());
				pro.add(item);
			}
			result.accumulate("pro", pro);
			
			List<UserLogin> list1 = customerService.selectCustomerSaler(conditions, 2,null);
			JSONArray sup = new JSONArray();
			for (UserLogin userLogin : list1) {
				JSONObject item = new JSONObject();
				item.put("id", userLogin.getUserLoginId());
				item.put("text", userLogin.getUserName());
				sup.add(item);
			}
			result.accumulate("sup", sup);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void getCustomerUserRelations(){
		JSONArray array = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("partyId");
		String customerId = request.getParameter("customerId");
		System.out.println(customerId+"customerId-------------------------------");
	
			try {
				List<CustomerUser> list = customerService.getCustomerUserRelations(partyId,customerId);
				array = JSONArray.fromObject(list);
			} catch (Exception e) {				
				e.printStackTrace();
				LOG.error(e.getMessage(), e);
			}
		WebPageUtil.writeBack(array.toString());	
		
	}
	
	/**
	 * 根据国家ID获取所属渠道
	 */
	public void getCustomerByCountry()
	{
		String countryId = request.getParameter("countryId");
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try
		{
			List<CustomerUser> list = customerService.getCustomerByCountry(countryId);
			result = JSONArray.fromObject(list);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	//添加新渠道时，根据筛选国家条件查询用户关系
	public void loadCustomerSalesDataByConditons(){
		JSONObject result = new JSONObject();
		String partyId = request.getParameter("partyId");
		String customerId = request.getParameter("customerId");
		response.setHeader("Content-Type", "application/json");
		
		System.out.println(customerId+"------------------------------------");
		
		
		String conditions="";
		
		String userPartys = WebPageUtil.loadPartyIdsByUserId();
		try {
			if(partyId!=null && !"".equals(partyId)){
				conditions+=" and ul.party_id ="+partyId+"";
			}
//			if(!WebPageUtil.isHAdmin()){
//				if(userPartys!=null && !"".equals(userPartys)){
//					conditions +=" and ul.party_id in ("+userPartys+")";
//				}else{
//					conditions +=" and 1=2";
//				}
//			}else{
//				conditions+=" and 1=1";
//			}
			
			String searchBus = request.getParameter("searchBus");
			if(searchBus!=null && !"".equals(searchBus)){
				conditions+=" and ul.user_name like concat ('%','"+searchBus+"','%')";
			}
			
			String searchprom = request.getParameter("searchprom");
			if(searchprom!=null && !"".equals(searchprom)){
				conditions+=" and ul.user_name like concat ('%','"+searchprom+"','%')";
			}
			
			String searchSup = request.getParameter("searchSup");
			if(searchSup!=null && !"".equals(searchSup)){
				conditions+=" and ul.user_name like concat ('%','"+searchSup+"','%')";
			}
			
			List<UserLogin> list = customerService.selectCustomerSalerData(conditions, 0,null);
			JSONArray salers = new JSONArray();
			for (UserLogin user : list) {
				JSONObject item = new JSONObject();
				item.put("id", user.getUserLoginId());
				item.put("text", user.getUserName());
				salers.add(item);
			}
			result.accumulate("salers", salers);
			
			
			List<UserLogin> list2 = customerService.selectCustomerSalerData(conditions, 1,customerId);
			JSONArray pro = new JSONArray();
			for (UserLogin user2 : list2) {
				JSONObject item = new JSONObject();
				item.put("id", user2.getUserLoginId());
				item.put("text", user2.getUserName());
				pro.add(item);
			}
			result.accumulate("pro", pro);
			
			List<UserLogin> list1 = customerService.selectCustomerSalerData(conditions, 2,null);
			JSONArray sup = new JSONArray();
			for (UserLogin userLogin : list1) {
				JSONObject item = new JSONObject();
				item.put("id", userLogin.getUserLoginId());
				item.put("text", userLogin.getUserName());
				sup.add(item);
			}
			result.accumulate("sup", sup);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
}
