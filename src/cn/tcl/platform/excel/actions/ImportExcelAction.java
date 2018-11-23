package cn.tcl.platform.excel.actions;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.excel.dao.ImportExcelDao;
import cn.tcl.platform.excel.service.IExcelService;
import cn.tcl.platform.excel.service.ImportExcelService;
import cn.tcl.platform.excel.service.impl.ImportPHExcelServiceImpl;
import cn.tcl.platform.excel.vo.ImportExcel;
import cn.tcl.platform.modelmap.dao.IModelMapDao;
import cn.tcl.platform.shop.dao.IShopDao;
import cn.tcl.platform.shop.vo.Shop;
import cn.tcl.platform.user.vo.UserLogin;

@SuppressWarnings("serial")
public class ImportExcelAction extends BaseAction {
	
	@Autowired(required = false)
	@Qualifier("importPHExcelService")
	private ImportExcelService importPHExcelService;
	
	@Autowired
	private  ImportExcelDao importExcelDao;
	@Autowired
	private IShopDao shopDao;
	@Autowired
	private IModelMapDao modelMapDao;
	
	
	@Autowired
	@Qualifier("excelService")
	private IExcelService excelService;
	
	String user;
	String countryId;
	public void importExcels() {
		try {

			String what=request.getParameter("what");
			String errorMsg = importPHExcelService.readExcel(uploadExcel,
					uploadExcelFileName,what);
			
			

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
	
	
	public void importPHExcels() {
		try {
			String what=request.getParameter("what");
			String errorMsg = importPHExcelService.readExcel(uploadExcel,
					uploadExcelFileName,what);

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
	
	
	
	
	
	
	
	
	
	public  void setCoreProduct() throws Exception{
		if (WebPageUtil.getLoginedUserId() != null) {
			user = WebPageUtil.getLoginedUserId();
			countryId = excelService.selectCountryByUser(user);
		}
		System.out.println(request.getParameter("core"));
		System.out.println("==================="+countryId);
		
		if(request.getParameter("core")!=null && !request.getParameter("core").equals("")){
			String cores=request.getParameter("core");
			if(cores.contains(",")){
				String [] core =cores.split(",");
				importPHExcelService.deleteCore(countryId);
				for (int i = 0; i < core.length; i++) {
					//先删除这个国家之前的，然后重新添加现在所选的
					importPHExcelService.insertCore(countryId, core[i]);
				}
			}else{
				importPHExcelService.insertCore(countryId, cores);
			}
			
			
		}
		
	}
	
	public  void selectAllCore()throws Exception{
		if (WebPageUtil.getLoginedUserId() != null) {
			user = WebPageUtil.getLoginedUserId();
			countryId = excelService.selectCountryByUser(user);
		}
		List<HashMap<String, Object>> list=importPHExcelService.selectAllCore();
		List<HashMap<String, Object>> cores=importPHExcelService.selectCore(countryId);
	
	
		JSONObject jsonObject=new JSONObject();
		for (int i = 0; i < list.size(); i++) {
			jsonObject.accumulate(list.get(i).get("PVALUE").toString(), "<input type='checkbox' name='core'  id='"+list.get(i).get("PVALUE")+"'  value='"+list.get(i).get("PVALUE")+"' >" +
					"<label for='"+list.get(i).get("PVALUE")+"' >"+list.get(i).get("PVALUE")+"</label>");
		}
		
		WebPageUtil.writeBack(jsonObject.toString());
	}
	
	
	
	public  void selectCore()throws Exception{
		if (WebPageUtil.getLoginedUserId() != null) {
			user = WebPageUtil.getLoginedUserId();
			countryId = excelService.selectCountryByUser(user);
		}
		List<HashMap<String, Object>> list=importPHExcelService.selectCore(countryId);
		String str="";
		for (int i = 0; i < list.size(); i++) {
			if(list.size()-1==i){
				str+=list.get(i).get("product_line");
			}else{
				str+=list.get(i).get("product_line")+",";
			}
			
			
		}
		
		WebPageUtil.writeBack(str.toString());
	}
	
	
	public void exportTemplate(){
		try {
			String title ="DATA";
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
			String spec=request.getParameter("spec");
			
			String what=request.getParameter("what");
			XSSFWorkbook workbook = null;
			//国家化列表头
			String[] excelHeader = {"Country","REGION","Store","Date"};
			String[] excelHeaderTwo = {"Country","REGION","Dealer code","Store","Date"};
			String[] excelHeaderThree = {"Country","REGION","Customer","Date"};

			String[] excelHeaderFour = {"Country","Date"};

			
			if(what.equals("customer")){
				 workbook = importPHExcelService.exporExcelByCustomer(spec,conditions, excelHeaderThree, title);
				 
				 if(spec.equals("1")){
						fileName="TV SO data upload (with customers).xlsx";
					}else if(spec.equals("2")){
						fileName="AC SO data upload (with customers).xlsx";

					}
			}else if(what.equals("country")){
				workbook = importPHExcelService.exporExcelByCountry(spec,conditions, excelHeaderFour, title);
				 
				 if(spec.equals("1")){
						fileName="TV SO data upload (with country).xlsx";
					}else if(spec.equals("2")){
						fileName="AC SO data upload (with country).xlsx";

					}
			}else{
				if(Integer.parseInt(WebPageUtil.getLoginedUser().getPartyId())==16){
					 workbook = importPHExcelService.exporExcelByPK(spec,conditions, excelHeaderTwo, title);
				}else{
					 workbook = importPHExcelService.exporExcel(spec,conditions, excelHeader, title);

				}
				
				if(spec.equals("1")){
					fileName="TV SO data upload (with stores).xlsx";
				}else if(spec.equals("2")){
					fileName="AC SO data upload (with stores).xlsx";

				}
			}
			
			
			
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
		
		excelName = "SO data upload"+".xlsx";
	} catch (Exception e) {
		e.printStackTrace();
		log.error(e.getMessage(),e);
	}
	return excelName;
}



public void importSoVerify(){
	String msg = "{\"success\":\"true\",\"msg\":\"false\"}";
	response.setHeader("Content-Type", "application/json");
	try {
		request.setCharacterEncoding("utf-8");
		String verifyWhat =request.getParameter("what");
		String country =request.getParameter("country");
		String shop =request.getParameter("shop");
		String model =request.getParameter("model");
		if(verifyWhat.equals("country") ){
			if(country!=null && !country.equals("")){
				String countryId = importExcelDao.selectCountry(country);
				if(countryId!=null && !countryId.equals("")){
					msg = "{\"success\":\"true\",\"msg\":\"true\"}";
				}else{
					msg = "{\"success\":\"true\",\"msg\":\"false\"}";
				}

			}else{
				msg = "{\"success\":\"true\",\"msg\":\"false\"}";
			}
			
		}else if(verifyWhat.equals("shop")){
			if(shop!=null && !shop.equals("")){
				Shop shopId = shopDao.getShopByNames(shop);
				if(shopId!=null && !shopId.equals("")){
					msg = "{\"success\":\"true\",\"msg\":\"true\"}";
				}else{
					msg = "{\"success\":\"true\",\"msg\":\"false\"}";
				}

			}else{
				msg = "{\"success\":\"true\",\"msg\":\"false\"}";
			}
		}else if(verifyWhat.equals("model")){
			if(country!=null && !country.equals("")  && model!=null && !model.equals("") ){
				String countryId = importExcelDao.selectCountry(country);
				String li = "";
				String cond = " AND t.`party_id`='"+countryId+"'";
				
				int modelCount = modelMapDao.getModelIdByParty(cond,model, li);
				if(modelCount>=1){
					msg = "{\"success\":\"true\",\"msg\":\"true\"}";
				}else{
					msg = "{\"success\":\"true\",\"msg\":\"false\"}";
				}

			}else{
				msg = "{\"success\":\"true\",\"msg\":\"false\"}";
			}
		}
		
			
	} catch (Exception e) {
		e.printStackTrace();
	}
		WebPageUtil.writeBack(msg);

}

public void importByInput() {
	String errorMsg ="";

	try {
		response.setHeader("Content-Type", "application/json");
		String list=request.getParameter("list");
		SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String userID =WebPageUtil.getLoginedUserId();
		JSONArray batchList = JSONArray.fromObject(list);
		List<ImportExcel> excelList=new ArrayList<ImportExcel>();
		List<ImportExcel> stockList=new ArrayList<ImportExcel>();
		List<ImportExcel> stockListByupdate=new ArrayList<ImportExcel>();
		List<ImportExcel> displayList=new ArrayList<ImportExcel>();
		List<ImportExcel> displayListByupdate=new ArrayList<ImportExcel>();
		List<ImportExcel> displayListRe=new ArrayList<ImportExcel>();
		String msg="";
		System.out.println("===========batchList=============="+batchList);
		Set<String> sett= new HashSet<String>();
		Set<String> setStocks= new HashSet<String>();

		for (int i = 0; i < batchList.size(); i++) {
			ImportExcel im=new ImportExcel();
			JSONObject excel=	(JSONObject) batchList.get(i);
			String countryId = importExcelDao.selectCountry(excel.getString("country"));
			Shop shopId = shopDao.getShopByNames(excel.getString("shop"));
			String model = excel.getString("model");
			Date beDate=format.parse(excel.getString("date"));
			String type=excel.getString("type");
			String date =  dfd.format(beDate);;
			int qty=excel.getInt("qty");
			
			Float exchange=importExcelDao.selectExchange(countryId, "", "",date);
			String days []=date.split("-");
			im.setUserId(userID);
			im.setCountryId(countryId);
			im.setDataDate(date);
			im.setShopId(shopId.getShopId());
			im.setModel(model);
			
			if(type.toLowerCase().equals("sold")){
				double price=excel.getDouble("price");
				String amt=excel.getString("amt");
				im.setSaleQty(qty);
				im.setSalePrice(price);
				im.setAmt(amt);
				im.setSource("PC");

				if(exchange==null){
					msg+=excel.getString("country")+" " 
							+ getText("excel.error.exchange")+" "+days[1]+"/"+days[0]+ "&lt;br/&gt;";
				}else{
					im.setH_qty(qty);
					if(exchange!=null){
					BigDecimal	bd = new BigDecimal(price*exchange); 
					double Hprice=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					im.setH_price(Hprice);
					bd = new BigDecimal(qty*Hprice);
					BigDecimal HAmt=bd.setScale(2, BigDecimal.ROUND_HALF_UP);
					im.setH_amt(String.valueOf(HAmt));
				}
					
					
				}
				excelList.add(im);
			}else  if(type.toLowerCase().equals("display")){
				boolean one=	sett.add(String.valueOf(shopId.getShopId()));
				boolean two=	sett.add(model);
				    if( !one
				    		&&
				    		 !two){
			    	msg+="Display："+getText("sample.error.modelRe")+" ("+model+")"+ "&lt;br/&gt;";
			    			
					
		          }
			    
				im.setDisPlayQty(qty);
				int rows = importExcelDao.selectDisPlayCount(String.valueOf(shopId.getShopId()),
						im.getModel());
				if (rows == 0) {
				im.setClassId(1);
				displayList.add(im);
				displayListRe.add(im);
				
			} else {
				displayListByupdate.add(im);
				im.setClassId(1);
				displayListRe.add(im);
				
			}
			
			
			}else  if(type.toLowerCase().equals("stocks")){
			boolean one=	setStocks.add(String.valueOf(shopId.getShopId()));
			boolean two=	setStocks.add(model);
			    if( !one
			    		&&
			    		 !two
			    		){
			    	msg+="Stocks："+getText("sample.error.modelRe")+" ("+model+")"+ "&lt;br/&gt;";
			    			
					
		          }
			    
				im.setStockQty(qty);
				int rows = importExcelDao.selectStockCount(String.valueOf(shopId.getShopId()),
						im.getModel());

				if (rows == 0) {
					im.setClassId(1);
					stockList.add(im);
				
				} else {
					stockListByupdate.add(im);
					
				}
			}
			
			
			
			
			
		}
		if(msg!=null && !msg.equals("")){
			errorMsg+="{\"success\":\"false\",\"msg\":\""+msg+"\"}";
		}
		
		if(errorMsg.length()<=0){
			if(excelList.size()>0){
				int row = importExcelDao.saveSales(excelList);
				if(row>0){
					errorMsg="{\"success\":\"true\",\"msg\":\"true\"}";
				}
				
			}
			
			if(displayList.size()>0){
				int row=importExcelDao.saveDisPlays(displayList);
				if(row>0){
					errorMsg="{\"success\":\"true\",\"msg\":\"true\"}";
				}
			}
			if(stockList.size()>0){
				int  row=importExcelDao.saveStocks(stockList);
				if(row>0){
					errorMsg="{\"success\":\"true\",\"msg\":\"true\"}";
				}
			}
			if(displayListByupdate.size()>0){
				int row=importExcelDao.updateDisPlay(displayListByupdate);
				if(row>0){
					errorMsg="{\"success\":\"true\",\"msg\":\"true\"}";
				}
			}
			if(stockListByupdate.size()>0){
				int row=importExcelDao.updateStocks(stockListByupdate);
				if(row>0){
					errorMsg="{\"success\":\"true\",\"msg\":\"true\"}";
				}
			}
			
			if(displayListRe.size()>0){
				
			int row=	importExcelDao.saveDisPlaysByRe(displayListRe);
			if(row>0){
				errorMsg="{\"success\":\"true\",\"msg\":\"true\"}";
			}
			}

			
		}
		
		
		
		System.out.println("=========errorMsg========="+errorMsg);

	
	} catch (Exception e) {
		e.printStackTrace();
	}
	WebPageUtil.writeBack(errorMsg);

}


public  void selectType() throws Exception{
	String country=request.getParameter("country");
	String result="";
	String countryId=WebPageUtil.getLoginedUser().getPartyId();

	if(country!=null && country!="") {
		result=importPHExcelService.selectSoType(country);
	}else {
		result=importPHExcelService.selectSoType(countryId);
		
	}
	String msg = "{\"success\":\"true\",\"msg\":\""+result+"\"}";
	WebPageUtil.writeBack(msg);
}





}