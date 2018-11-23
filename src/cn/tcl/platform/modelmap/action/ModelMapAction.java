package cn.tcl.platform.modelmap.action;

import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.excel.imports.ExcelImportUtil;
import cn.tcl.platform.modelmap.service.IModelMapService;
import cn.tcl.platform.modelmap.vo.ModelMap;
import cn.tcl.platform.product.vo.Product;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings("all")
public class ModelMapAction extends BaseAction{
	
	@Autowired(required = false)
	@Qualifier("modelmapService")
	private IModelMapService modelmapService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 页面
	 * @return
	 */
	public String loadModelMapPage(){
		try {
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	/**
	 * 页面
	 * @return
	 */
	public String loadModelChannelMapPage(){
		try {
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	/**
	 * 获取数据
	 */
	public void loadModelMapListData(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try {
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			String keyword = request.getParameter("keyword");
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
					conditions += " and tm.party_id IN (select distinct tt.country_id from party tt where tt.party_id in ("+userPartyIds+"))";
				}else{
					conditions += " and 1=2 ";
				}
			}
			else
			{
				conditions += " and 1=1 ";
			}
			
			Map<String, Object> map = modelmapService.selectModelMapData(start, limit, keyword, order, sort,conditions);
			int total = (Integer)map.get("total");
			List<Product> list = (ArrayList<Product>)map.get("rows");
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
	}

	/**
	 * 修改
	 */
	public void editModelMap() throws Exception{
		JSONObject result = new JSONObject();
		String id = request.getParameter("editId");
		if(id!=null && id.trim()!=""){
			String _branchModel = request.getParameter("branchModel");
			String _hqModel = request.getParameter("hqModel");
			String _price = request.getParameter("price");
			String _partyId = request.getParameter("partyId");
			try {
				ModelMap ModelMapBean = new ModelMap();
				ModelMapBean.setBranchModel(_branchModel);
				ModelMapBean.setHqModel(_hqModel);
				Float f = WebPageUtil.isStringNullAvaliable(_price) ? Float.parseFloat(_price) : 0F;
				ModelMapBean.setPrice(f);
				ModelMapBean.setPartyId(_partyId);
				ModelMapBean.setCondition(" AND id !=  cast("+ id +" as SIGNED INTEGER)");
				ModelMapBean.setId(id);
				/*	ModelMap ModelMapBean = modelmapService.getModelMapById(id);
				
				if(!ModelMapBean.getBranchModel().equals(_branchModel.toUpperCase())){
					ModelMapBean.setBranchModel(_branchModel.toUpperCase());
				}else{
					ModelMapBean.setBranchModel(null);
				}
				if(!ModelMapBean.getHqModel().equals(_hqModel.toUpperCase())){
					ModelMapBean.setHqModel(_hqModel.toUpperCase());
				}else{
					ModelMapBean.setHqModel(null);
				}
				if(_price!=null &&  !_price.equals("")){
					ModelMapBean.setPrice(Float.parseFloat(_price));
				}else{
					ModelMapBean.setPrice(0F);
				}
				
				if(_partyId!=null &&  !"".equals(_partyId)){
					ModelMapBean.setPartyId(_partyId);
				}else{
					ModelMapBean.setPartyId(null);
				}*/
				
				String mc = modelmapService.searchBeanVerify(ModelMapBean);
				if("modelError".equals(mc)){
					/*result.accumulate("success", false);
					result.accumulate("msg", getText("modelmap.error.prompt01"));
				}else if("branchError".equals(mc)){*/
					result.accumulate("success", false);
					result.accumulate("msg", getText("modelmap.error.prompt02"));
				}else{
					modelmapService.editModelMap(ModelMapBean);
					result.accumulate("success", true);
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
	 * 型号是否存在对应销售数据
	 * @throws Exception
	 */
	public void selectSaleMappingBybranchModel() throws Exception{
		JSONObject result = new JSONObject();
		String branchModel = request.getParameter("branchModel");
		try {
			result.accumulate("msg", modelmapService.selectSaleMappingBybranchModel(branchModel));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("msg", -1);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 新增
	 */
	public void addModelMap() throws Exception{
		JSONObject result = new JSONObject();
		String _price = request.getParameter("price");
		String _branchModel = request.getParameter("branchModel");
		String _hqModel = request.getParameter("hqModel");
		String _partyId = request.getParameter("partyId");
		String _time = sdf.format(new Date());
		try {
			ModelMap modelMap = new ModelMap();
			modelMap.setBranchModel(_branchModel.toUpperCase());
			modelMap.setHqModel(_hqModel.toUpperCase());
			modelMap.setPartyId(_partyId);
			modelMap.setCtime(_time);
			if(_price!=null && !_price.equals("")){
				modelMap.setPrice(Float.parseFloat(_price));
			}
			String mc = modelmapService.searchBeanVerify(modelMap);
			if("modelError".equals(mc)){
			/*	result.accumulate("success", false);
				result.accumulate("msg", getText("modelmap.error.prompt01"));
			}else if("branchError".equals(mc)){*/
				result.accumulate("success", false);
				result.accumulate("msg", getText("modelmap.error.prompt02"));
			}else{
				modelmapService.addModelMap(modelMap);
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
	 * 删除
	 * @throws Exception
	 */
	public void deleteModelMap() throws Exception{
		JSONObject result = new JSONObject();
		String _id = request.getParameter("id");
		try {
			ModelMap modelMap = new ModelMap();
			modelMap.setId(_id);
			modelMap.setDelUserId(WebPageUtil.getLoginedUserId());
			
			modelmapService.deleteModelMap(modelMap);
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
	 * 导入
	 */
	public void importModelMap(){
		try{
			String path = getClassRealPath()+ File.separatorChar+"cn"+File.separatorChar+"tcl"+File.separatorChar+"platform" +
			File.separatorChar +"modelmap"+File.separatorChar+"imports"+File.separatorChar+"imports.xml";
			
			ExcelImportUtil export = new ExcelImportUtil(path);
			export.init(uploadExcel,uploadExcelFileName,WebPageUtil.getLanguage());
			List<ModelMap> ModelMaps = export.bindToModelsAndImport(ModelMap.class);
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
	
	
	
	
	
	
	/**
	 * 导入
	 */
	public void importModelPrice(){
		try{
			String path = getClassRealPath()+ File.separatorChar+"cn"+File.separatorChar+"tcl"+File.separatorChar+"platform" +
			File.separatorChar +"modelmap"+File.separatorChar+"imports"+File.separatorChar+"importsPrice.xml";
			
			ExcelImportUtil export = new ExcelImportUtil(path);
			export.init(uploadExcel,uploadExcelFileName,WebPageUtil.getLanguage());
			List<ModelMap> ModelMaps = export.bindToModelsAndImport(ModelMap.class);
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
	
	
	
	
	
	
	
	
	
	
	
	public void exportModelExcel(){
		try {
			String title ="modelPrice";
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
					conditions += "  tm.party_id IN (select distinct tt.country_id from party tt where tt.party_id in ("+userPartyIds+")) ";
				}else{
					conditions += "  1=2 ";
				}
			}
			else
			{
				conditions += "  1=1 ";
			}
			
			//国家化列表头
			String[] excelHeader = {"*Country",
					"*Branch model","*Price"};
			XSSFWorkbook workbook = modelmapService.exporModelPrice(conditions, excelHeader, title);
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
			
			excelName = name + sdf.format(d)+".xlsx";
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		return excelName;
	}
	
	/**
	 * ****** 渠道型号与分公司型号对应关系 
	 */
	
	/**
	 * 获取数据
	 */
	public void loadChannelModelMapListData(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try {
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			String keyword = request.getParameter("keyword");
			keyword = "".equals(keyword)?null:keyword;
			String pageStr=request.getParameter("page");
			int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
			String rowStr=request.getParameter("rows");
			int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
			int start = (page-1)*limit;
			
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "";
			
			if(!WebPageUtil.isHQRole())
			{
				if(null!=userPartyIds && !"".equals(userPartyIds)){
					conditions += " and tm.party_id IN (select distinct tt.country_id from party tt where tt.party_id in ("+userPartyIds+"))";
				}else{
					conditions += " and 1=2 ";
				}
			}
			else
			{
				conditions += " and 1=1 ";
			}
			Map<String, Object> mapData = new HashMap<String,Object>();
			mapData.put("start", start);
			mapData.put("limit", limit);
			mapData.put("keyword", keyword);
			mapData.put("order", order);
			mapData.put("sort", sort);
			mapData.put("conditions", conditions);
			
			Map<String, Object> map = modelmapService.selectChannelModelMap(mapData);
			int total = (Integer)map.get("total");
			List<Product> list = (ArrayList<Product>)map.get("rows");
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
	}
	
	/**
	 * 删除
	 * @throws Exception
	 */
	public void deleteChannelModel() throws Exception{
		JSONObject result = new JSONObject();
		String id = request.getParameter("id");
		try {
			ModelMap model = new ModelMap();
			model.setId(id);
			model.setDelUserId(WebPageUtil.getLoginedUserId());
			
			modelmapService.deleteChannelModel(model);
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
	
	public void selectChennalModelByPartyId(){
		response.setHeader("Content-Type", "application/json");
		String rows = "";
		try {
			String partyId = null;
			
			if(!WebPageUtil.isHAdmin())
			{
				partyId = WebPageUtil.getLoginedUser().getPartyId();
			}
			rows = JSONArray.fromObject(modelmapService.selectChennalModelByPartyId(partyId)).toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			
		}
		WebPageUtil.writeBack(rows);
	}
	
	/**
	 * 修改
	 */
	public void udpateChannelModelById() throws Exception{
		JSONObject result = new JSONObject();
		String id = request.getParameter("editId");
		if(id!=null && id.trim()!=""){
			String _branchModel = request.getParameter("branchModel");
			String _channelModel = request.getParameter("channelModel");
			String _price = request.getParameter("price");
			String _partyId = request.getParameter("partyId");
			String customerId = request.getParameter("customerId");
			try {
				ModelMap ModelMapBean = new ModelMap();
				ModelMapBean.setBranchModel(_branchModel);
				ModelMapBean.setChannelModel(_channelModel);
				ModelMapBean.setChannelPrice((long) (WebPageUtil.isStringNullAvaliable(_price) ? Integer.parseInt(_price) : 0));
				ModelMapBean.setPartyId(_partyId);
				ModelMapBean.setId(id);
				ModelMapBean.setCustomerId(customerId);
				String condition = " AND id !=  cast("+ id +" as SIGNED INTEGER)";
				String c = modelmapService.channelModelIsBeing(customerId,_channelModel,_branchModel,condition);
				/*if("branModelError".equals(c)){
					result.accumulate("success", "branModelError");
				}else*/ if("custModelError".equals(c)){
					result.accumulate("success", "custModelError");
				}else {
					modelmapService.updateChannelModelById(ModelMapBean);
					result.accumulate("success", true);
				};
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
	 * 增加
	 */
	public void insertChannelModel() throws Exception{
		JSONObject result = new JSONObject();
		String _branchModel = request.getParameter("branchModel");
		String _channelModel = request.getParameter("channelModel");
		String _price = request.getParameter("price");
		String _partyId = request.getParameter("partyId");
		String customerId = request.getParameter("customerId");
		try {
			ModelMap ModelMapBean = new ModelMap();
			ModelMapBean.setBranchModel(_branchModel);
			ModelMapBean.setChannelModel(_channelModel);
			ModelMapBean.setChannelPrice((long) (WebPageUtil.isStringNullAvaliable(_price) ? Integer.parseInt(_price) : 0));
			ModelMapBean.setPartyId(_partyId);
			ModelMapBean.setCustomerId(customerId);
			String c = modelmapService.channelModelIsBeing(customerId,_channelModel,_branchModel,null);
			
			/*if("branModelError".equals(c)){
				result.accumulate("success", "branModelError");
			}else*/ if("custModelError".equals(c)){
				result.accumulate("success", "custModelError");
			}else{
				modelmapService.insertChannelModel(ModelMapBean);
				result.accumulate("success", "true");
			};
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", "false");
			result.accumulate("msg", msg);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 导入
	 */
	public void importChannelModelMap(){
		try{
			String path = getClassRealPath()+ File.separatorChar+"cn"+File.separatorChar+"tcl"+File.separatorChar+"platform" +
			File.separatorChar +"modelmap"+File.separatorChar+"imports"+File.separatorChar+"importChannelModel.xml";
			
			ExcelImportUtil export = new ExcelImportUtil(path);
			export.init(uploadExcel,uploadExcelFileName,WebPageUtil.getLanguage());
			List<ModelMap> ModelMaps = export.bindToModelsAndImport(ModelMap.class);
			String errorMsg = export.getError();
			if("".equals(errorMsg)){
				WebPageUtil.writeBack("success");
			}
			else{
				//errorMsg = errorMsg.replace(regex, replacement)
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
}
