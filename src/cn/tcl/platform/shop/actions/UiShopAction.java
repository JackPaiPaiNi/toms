package cn.tcl.platform.shop.actions;

import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.util.logging.Logger;

import cn.tcl.common.BaseAction;
import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.excel.imports.ExcelImportUtil;
import cn.tcl.platform.cfgparam.vo.CfgParameter;
import cn.tcl.platform.district.vo.Country;
import cn.tcl.platform.shop.vo.ShopParty;
import cn.tcl.platform.shop.service.IShopService;
import cn.tcl.platform.shop.vo.Level;
import cn.tcl.platform.shop.vo.Shop;
import cn.tcl.platform.shop.vo.ShopUser;
import cn.tcl.platform.user.service.IUserLoginService;
import cn.tcl.platform.user.vo.UserLogin;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@SuppressWarnings("all")
public class UiShopAction  extends BaseAction{
	@Autowired
	@Qualifier("shopService")
	private IShopService shopService;
	
	@Autowired(required = false)
	@Qualifier("userLoginService")
	private IUserLoginService userLoginService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public String loadShopPage(){
		try{
			return SUCCESS;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	public String loadShopPhotosPage(){
		try{
			return SUCCESS;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	
	public void loadShopListData(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try {
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			int page = Integer.valueOf(request.getParameter("page"));
			int limit = Integer.valueOf(request.getParameter("rows"));
			int start = (page-1)*limit;
			
			
			String searchStr = "1 = 1";
			String searchParty = request.getParameter("searchParty");
			String levelNum = request.getParameter("levelNum");
			
			if(!"".equals(searchParty) && searchParty != null){
				searchParty=searchParty.replace("\'", "\\'");
				searchStr += " and v_shop.party_name like CONCAT('%','"+searchParty+"','%' )";
			}
			String searchCustomer = request.getParameter("searchCustomer");
			if(!"".equals(searchCustomer) && searchCustomer != null){
				searchCustomer=searchCustomer.replace("\'", "\\'");
				searchStr += " and v_shop.CUSTOMER_NAME like CONCAT('%','"+searchCustomer+"','%' )";
			}
			String searchShop = request.getParameter("searchShop");
			if(!"".equals(searchShop) && searchShop != null){
				searchShop=searchShop.replace("\'", "\\'");
				System.out.println(searchShop+"====================================");
				
				searchStr += " and (v_shop.SHOP_NAME like '%"+searchShop+"%' or v_shop.location like '%"+searchShop+"%')";
			}
			String searchUserName = request.getParameter("searchUserName");
			if(!"".equals(searchUserName) && searchUserName != null){
				
				searchStr += " and v_shop.shop_id IN (SELECT shop_id FROM shop_saler_mapping ssm,user_login ul WHERE  ssm.user_login_id=ul.USER_LOGIN_ID and ul.USER_NAME LIKE  CONCAT('%','"+searchUserName+"','%' ))";
			}

			String countryId=request.getParameter("countryId");
			if(!"-1".equals(countryId) && !"".equals(countryId) && countryId != null){
				searchStr += " and v_shop.COUNTRY_ID='"+countryId+"'";
			}
//			String provinceId=request.getParameter("provinceId");
//			if(!"-1".equals(provinceId) && !"".equals(provinceId) && provinceId != null){
//				searchStr += " and v_shop.PROVINCE_ID='"+provinceId+"'";
//			}
			
			//String conditions = WebPageUtil.buildDataPermissionSql("ci.PARTY_ID", Contents.ROLE_DATA_PERMISSION_PARTY);
			System.out.println(request.getParameter("searchShop"));
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String roleId = WebPageUtil.getLoginedUser().getRoleId();
			String userLoginId = WebPageUtil.getLoginedUser().getUserLoginId();
			System.out.println(roleId+"-------------------roleId-----------------");
			
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{	
				if(roleId.indexOf("SUPERVISOR")==0 || roleId.indexOf("SALES")==0){
						conditions += " v_shop.party_id in ("+userPartyIds+")";
						conditions +=" and ssm.user_login_id='"+userLoginId+"'";
				}else{
					if(null!=userPartyIds && !"".equals(userPartyIds))
					{
						conditions += " v_shop.party_id in ("+userPartyIds+")";
					}
					else
					{
						conditions += " 1=2 ";
					}
				}
			}
			else
			{
				conditions += " 1=1 ";
			}
			System.out.println("======"+request.getParameter("searchUserName"));
			System.out.println("++++++++++++++++++"+conditions);
			Map<String, Object> map;
			if(roleId.indexOf("SUPERVISOR")==0 || roleId.indexOf("SALES")==0){
				 map = shopService.selectShopsBySupSaleData(start, limit, searchStr,levelNum, order, sort,conditions);
			}else{
				 map = shopService.selectShopsData(start, limit, searchStr,levelNum, order, sort,conditions);
			}
			int total = (Integer)map.get("total");
			List<Shop> list = (ArrayList<Shop>)map.get("rows");
			JSONArray jsonArray = JSONArray.fromObject(list);
			String rows = jsonArray.toString();
			result.accumulate("rows", rows);
			result.accumulate("total", total);
			result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("rows", new JSONArray());
			result.accumulate("total", 0);
			result.accumulate("success", true);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	public void addShop(){
		JSONObject result = new JSONObject();
		try {
			String customer=request.getParameter("customer");
			String shopName=request.getParameter("shopName");
			String enterdate=request.getParameter("enterDateStr");
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
			String longitude=request.getParameter("lng");
			Float lon=Float.parseFloat("".equals(longitude)?"0.0":longitude);
			String latitude=request.getParameter("lat");
			Float lat=Float.parseFloat("".equals(latitude)?"0.0":latitude);
			String comments=request.getParameter("comments");
			String partyId=request.getParameter("partyId");
			String level = request.getParameter("level");
			String location = request.getParameter("location");
			
			Shop shop=new Shop();
			shop.setPartyId(partyId);
			shop.setCityId(city);
			shop.setComments(comments);
			shop.setContactName(contactName);
			shop.setCountryId(country);
			shop.setCountyId(county);
			String loginUserId = (String)request.getSession().getAttribute("loginUserId");//TODO:取创建人
			shop.setCreateBy(loginUserId);
			shop.setCreateDate(sdf.format(new Date()));
			shop.setCustomerId(customer);
			shop.setDetailAddress(address);
			shop.setEmail(email);
			Date enterDate2=WebPageUtil.strToDate(enterdate);
			shop.setEnterDate(enterDate2);
			shop.setLat(lat);
			shop.setLng(lon);
			shop.setPhone(phone);
			shop.setProvinceId(province);
			shop.setShopName(shopName);
			shop.setStatus(status);
			shop.setTownId(town);
			shop.setLevel(level);
			shop.setLocation(location);
			
			//关联的业务员，促销员
			String businessers=request.getParameter("businessers");
			String salers=request.getParameter("salers");
			String supervisors=request.getParameter("supervisors");
			
			//判断  门店名称和 客户名称
			int s = shopService.getShopByName(shopName,customer);
			int isLocationExist = (!"".equals(location.trim()))? shopService.selectLocationIsExist(location) : 0;
			if(isLocationExist != 0){
				result.accumulate("success", false);
				result.accumulate("msg", getText("shop.error.prompt09"));
			}else if(s==0){
				shopService.saveShop(shop,businessers,salers,supervisors);
				result.accumulate("success", true);
			}else{
				result.accumulate("success", false);
				result.accumulate("msg", getText("shop.error.prompt07"));
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	public void editShop(){
		JSONObject result = new JSONObject();
		String id=request.getParameter("editId");
		if(id!=null && id.trim()!=""){
			try {
				String customer=request.getParameter("customer");
				String shopName=request.getParameter("shopName");
				String enterdate =request.getParameter("enterDateStr");
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
				String longitude=request.getParameter("lng");
				Float lon=Float.parseFloat("".equals(longitude)?"0.0":longitude);
				String latitude=request.getParameter("lat");
				Float lat=Float.parseFloat("".equals(latitude)?"0.0":latitude);
				String comments=request.getParameter("comments");
				String partyId=request.getParameter("partyId");
				String level = request.getParameter("level");
				String location = request.getParameter("location");
				String hisLoac = request.getParameter("hisLoac");//历史位置,用来判断是否修改过location
				
				Shop shop=shopService.getShop(id);
				shop.setCityId(city);
				shop.setComments(comments);
				shop.setContactName(contactName);
				shop.setCountryId(country);
				shop.setCountyId(county);
				shop.setCustomerId(customer);
				shop.setDetailAddress(address);
				shop.setEmail(email);
				Date enterDate2=WebPageUtil.strToDate(enterdate);
				shop.setEnterDate(enterDate2);
				shop.setLat(lat);
				shop.setLng(lon);
				shop.setPhone(phone);
				shop.setProvinceId(province);
				shop.setLocation(location);
//				shop.setShopId(id);
				
				shop.setStatus(status);
				shop.setTownId(town);
				shop.setPartyId(partyId);
				shop.setLevel(level);
				
				//关联的业务员，促销员
				String businessers=request.getParameter("businessers");
				String salers=request.getParameter("salers");
				String supervisors=request.getParameter("supervisors");
				
				int isLocationExist = (!"".equals(location.trim()))? shopService.selectLocationIsExist(location) : 0;
				if(isLocationExist != 0 && !hisLoac.equals(location)){
					result.accumulate("success", false);
					result.accumulate("msg", getText("shop.error.prompt09"));
				
				}else if( shopName.equals(shop.getShopName())){
					shopService.editShop(shop,businessers,salers,supervisors);
					result.accumulate("success", true);
				}else{
					//判断  门店名称和 客户名称
					int s = shopService.getShopByName(shopName,customer);
					
					if(s==0){
						shop.setShopName(shopName);
						shopService.editShop(shop,businessers,salers,supervisors);
						result.accumulate("success", true);
					}else{
						result.accumulate("success", false);
						result.accumulate("msg", getText("shop.error.prompt07"));
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				log.error(e.getMessage(),e);
				String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
				result.accumulate("success", false);
				result.accumulate("msg", msg);
			}
		}
		
		WebPageUtil.writeBack(result.toString());
	}
	public void deleteShop(){
		JSONObject result = new JSONObject();
		Integer shopId=WebPageUtil.p2Int(request.getParameter("shopId"), null);
		if(shopId==null){
			result.accumulate("success", false);
			result.accumulate("msg", "lost parameter shodId!");
		}else{
			try {
				Shop shop= new Shop();
				shop.setDelUserId(WebPageUtil.getLoginedUserId());
				shop.setShopId(shopId);
				shopService.deleteShop(shop);
//				shopService.deleteShop(shopId);
				result.accumulate("success", true);
				result.accumulate("msg", "success delete");
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
	public void importShop() {
			try {

				String errorMsg = shopService.readExcel(uploadExcel,
						uploadExcelFileName);

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
	public void getShopUserRelations(){
		JSONArray array = new JSONArray();
		response.setHeader("Content-Type", "application/json");
//		Integer shopId=WebPageUtil.p2Int(request.getParameter("shopId"), null);
		String shopId = request.getParameter("shopId");
		String partyId = request.getParameter("partyId");
		if(shopId!=null){
			try {
				List<ShopUser> userloginids=shopService.getShopUserRelations(shopId,partyId);
				for (ShopUser shopUser : userloginids) {
					System.out.println(shopUser.getUserLoginId());
				}
				array=JSONArray.fromObject(userloginids);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			
		}
		WebPageUtil.writeBack(array.toString());
	}
	
	/**
	 * 查询 门店
	 */
	public void getShopList(){
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try {
			request.setCharacterEncoding("utf-8");
			
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += " si.PARTY_ID in ("+userPartyIds+")";
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
			
			List<Shop> plist = shopService.getShopDataList(conditions);
			result = JSONArray.fromObject(plist);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
	public void getShopGeoCoord(){
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try {
			request.setCharacterEncoding("utf-8");
			
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			if(!WebPageUtil.isHAdmin())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds))
				{
					conditions += " si.PARTY_ID in ("+userPartyIds+")";
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
			
			List<Shop> plist = shopService.getShopGeoCoord(conditions);
			List<Map> shopDataList = new ArrayList<Map>();
			Map<String,double[]> geoMap = new HashMap<String, double[]>();
			
			List refList = new ArrayList();
			for (Shop shop : plist) {
				double[] geo = new double[]{Double.valueOf(shop.getLng()),Double.valueOf(shop.getLat())};
				geoMap.put(shop.getShopName(), geo);
			}
			shopDataList.add(geoMap);
			
			refList.add(plist);
			refList.add(shopDataList);
			
			result = JSONArray.fromObject(refList);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
public void loadShopPhotosListData(){
	JSONObject result = new JSONObject();
	response.setHeader("Content-Type", "application/json");
	try {
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		int page = Integer.valueOf(request.getParameter("page"));
		int limit = Integer.valueOf(request.getParameter("rows"));
		int start = (page-1)*limit;
		
		
		String searchStr = "1 = 1";
		String searchShop = request.getParameter("searchShop");
		if(!"".equals(searchShop) && searchShop != null){
			searchStr += " and SI.SHOP_NAME like CONCAT('%','"+searchShop+"','%' )";
		}
		String searchDate=request.getParameter("searchDate");
		if(!"".equals(searchDate) && searchDate != null){
			searchStr += " and SP.UPTIME ='"+searchDate+"'";
		}
		

		String conditions = "";
    	String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
		if(!WebPageUtil.isHAdmin())
		{
			if(null!=userPartyIds && !"".equals(userPartyIds))
			{
				conditions += "	 pa.PARTY_ID in (" +
						userPartyIds + ")";
			}
			else
			{
				conditions += "  1=2 ";
			}
		}else{
			conditions=null;
		}
		
		Map<String, Object> map = shopService.selectShopPhotosData(start, limit, searchStr, order, sort,conditions);
		int total = (Integer)map.get("total");
		List<Shop> list = (ArrayList<Shop>)map.get("rows");
		JSONArray jsonArray = JSONArray.fromObject(list);
		String rows = jsonArray.toString();
		result.accumulate("rows", rows);
		result.accumulate("total", total);
		result.accumulate("success", true);
	} catch (Exception e) {
		e.printStackTrace();
		log.error(e.getMessage(),e);
		String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
		result.accumulate("rows", new JSONArray());
		result.accumulate("total", 0);
		result.accumulate("success", true);
		result.accumulate("msg", msg);
	}
	WebPageUtil.writeBack(result.toString());
}

public void loadPartyListData(){
	/*JSONObject result = new JSONObject();
	response.setHeader("Content-Type", "application/json");
	try {
		request.setCharacterEncoding("UTF-8");
		String countryId = request.getParameter("countryId");
		 List<Shop> partyList = shopService.selectParty(countryId);
		String rows=JSONArray.fromObject(partyList).toString();
		result.accumulate("rows", rows);
	} catch (Exception e) {
		e.printStackTrace();
	}
	WebPageUtil.writeBack(result.toString());
	*/
	JSONArray result = new JSONArray();
	response.setHeader("Content-Type", "application/json");
	try {
		request.setCharacterEncoding("utf-8");
		String countryId = request.getParameter("countryId");
		List<ShopParty> partyList = shopService.selectParty(countryId);
		result = JSONArray.fromObject(partyList);
	} catch (Exception e) {
		e.printStackTrace();
	}
	WebPageUtil.writeBack(result.toString());
}
	
	








public void ExportShopName(){
	try {
		String title ="Store Name";
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
				conditions += "  si.party_id IN ("+userPartyIds+") ";
			}else{
				conditions += "  1=2 ";
			}
		}
		else
		{
			conditions += "  1=1 ";
		}
		
		//国家化列表头
		String[] excelHeader = {"Store Name"};
		XSSFWorkbook workbook = shopService.exporShopName(conditions, excelHeader, title);
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

public void loadCustomerByParty(){
	
	JSONArray result = new JSONArray();
	response.setHeader("Content-Type", "application/json");
	try {
		request.setCharacterEncoding("utf-8");
		String countryId = request.getParameter("countryId");
		List<Shop> partyList = shopService.selectCustomer(countryId);
		result = JSONArray.fromObject(partyList);
	} catch (Exception e) {
		e.printStackTrace();
	}
	WebPageUtil.writeBack(result.toString());
}
	
	
	public void selectShopLevel(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("partyId");
		try {
			List<Level> list = shopService.selectShopLevel(partyId);
			String rows = JSONArray.fromObject(list).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
	public void selectLevelBycountry(){
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		String countryId = request.getParameter("countryId");
		try {
			List<Level> list = shopService.selectLevelBycountry(countryId);
			result = JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
	
	public void importShopPersonnel() {
		try {

			String errorMsg = shopService.readExcelByPe(uploadExcel,
					uploadExcelFileName);

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
	
	
	public void updateShopName() {
		try {

			String errorMsg = shopService.readExcelByShop(uploadExcel,
					uploadExcelFileName);

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
	
	
	
}
