package cn.tcl.platform.product.actions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.excel.imports.ExcelImportUtil;
import cn.tcl.platform.product.service.IProductService;
import cn.tcl.platform.product.vo.Product;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings("all")
public class UiProductAction  extends BaseAction{
	@Autowired(required = false)
	@Qualifier("productService")
	private IProductService productService;
	public String loadProductPage(){
		try{
			return SUCCESS;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	public String loadACProductPage(){
		try{
			return SUCCESS;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return ERROR;
		}
	}
	
	public void loadProductListData(){//加载信息查询
		String pType = request.getParameter("pType");
		if(Contents.TV.equals(pType)){
			loadTVProductListData();
		}else if(Contents.AC.equals(pType)){
			loadACProductListData();
		};
	}
	
	
	public void loadACProductListData(){//加载AC信息查询
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try{
			
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			String keyword = request.getParameter("keyword");
			String pageStr=request.getParameter("page");
			int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
			String rowStr=request.getParameter("rows");
			int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
			int start = (page-1)*limit;
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "1=1";
			
			Map<String, Object> map = productService.selectProductsData(start, limit, keyword, order, sort,conditions,Contents.AC,null,null);
			int total = (Integer)map.get("total");
			List<Product> list = (ArrayList<Product>)map.get("rows");
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
	
	public void loadTVProductListData(){//加载TV信息查询
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try{
			String catId = request.getParameter("catId");
			String sonId = request.getParameter("sonId");
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			String keyword = request.getParameter("keyword");
			String pageStr=request.getParameter("page");
			int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
			String rowStr=request.getParameter("rows");
			int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
			int start = (page-1)*limit;
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			String conditions = "1=1";
			
			Map<String, Object> map = productService.selectProductsData(start, limit, keyword, order, 
					sort,conditions,Contents.TV,sonId,catId);
			int total = (Integer)map.get("total");
			List<Product> list = (ArrayList<Product>)map.get("rows");
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
	};
	
	
	public void addProduct() throws Exception{
		JSONObject result = new JSONObject();
		String display=request.getParameter("display");
		String size=request.getParameter("size");
		String brand=request.getParameter("brandId");
		String ratio=request.getParameter("ratio");
		String color=request.getParameter("colorId");
		String productId=request.getParameter("productId");
		String productName=request.getParameter("productName");
		String power=request.getParameter("power");
		String powerOn=request.getParameter("powerOn");
		String powerWait=request.getParameter("powerWait");
		String netweight=request.getParameter("netweight");
		String weight=request.getParameter("weight");
		String weightInclude=request.getParameter("weightInclude");
		String interFace=request.getParameter("interFace");
		String network=request.getParameter("network");
		String os=request.getParameter("os");
		/*String party=request.getParameter("partyId");*/
		Object obj = request.getSession();
		String party = (String) request.getSession().getAttribute("loginUserId");
		String status=request.getParameter("status");
		String description=request.getParameter("description");  
		String gasType = request.getParameter("gas");//AC机器类型 (冷气、热气)
		String introductionDate = request.getParameter("intDate");
		String modelName = request.getParameter("modelName");
		String productTypeId=request.getParameter("productTypeId");
		String productType=request.getParameter("productType");
		String productSpecId = request.getParameter("productSpecId");
		String productComments = request.getParameter("comments");
		String headType = request.getParameter("pType");
		String productCatena = request.getParameter("productCatena");
		
		String filePath = "";
		if(uploadExcel != null){
			filePath = this.productUploadFile(uploadExcel,uploadExcelFileName);
		}
		
		Product product=new Product();
		
		product.setProductTypeId(productTypeId);//类别typeId
		product.setProductSpecId(productSpecId);//类别规格Id
		product.setHeadType(headType);//首级
		product.setCatena(productCatena);//产品系列
		product.setProductId(WebPageUtil.isStringNullAvaliable(productId) ? productId.toUpperCase() : "");//产品编号
		product.setPartyId(party);
		product.setGasType(gasType);//AC机器类型 (冷气、热气)
		product.setBrandId(brand);
		product.setCategoryId("");//?
		product.setColorId(color);
		product.setComments(productComments);//备注
		product.setDescription(description);//描述
		product.setDescriptionEn("");
		product.setDisplay(display);
		product.setFilePath(filePath);//TODO:保存文件
		product.setInterFace(interFace);
		product.setInternalName("");//?
		
		if(!"".equals(introductionDate) && introductionDate != null){
			product.setIntroductionDate(new SimpleDateFormat("yyyy-MM-dd").parse(introductionDate));//涓婂競鏃堕棿
		}
		product.setManufacturerPartyId("");//?
		product.setModelName(modelName);//模型名称
		product.setNetweight(netweight);
		product.setNetwork(network);
		product.setOs(os);
		
		product.setPhoto("");//TODO 功能没实现INTRODUCTION_DATE
		product.setPower(power);
		product.setPowerOn(powerOn);
		product.setPowerWait(powerWait);
		product.setProductFuncId("");//?
		
		product.setProductName(productName);
		product.setProductScreenId("");//?
		product.setProductType(productType);
		
		product.setQuantityUomId("");
		product.setRatio(ratio);
		product.setSalesDiscontinuationDate(new Date());//?
		product.setSize(size);
		product.setStatus("1".equals(status)?true:false);
		product.setSupportDiscontinuationDate(new Date());//?
		product.setVolume("");//?
		product.setWeight(weight);
		product.setWeightInclude(weightInclude);
		try {
			//校验产品编码 是否存在
			int count = productService.searchCountByProductId(modelName.toUpperCase());
			if(count == 0){
				productService.saveProduct(product);
				result.accumulate("success", true);
			}else{
				result.accumulate("success", false);
				result.accumulate("msg", "TE_0001");
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
	
		public void editProduct(){
		JSONObject result = new JSONObject();
		String id=request.getParameter("editId");
		if(id!=null && id.trim()!=""){
			String display=request.getParameter("display");
//			String productType=request.getParameter("productType");
			String size=request.getParameter("size");
			String brand=request.getParameter("brandId");
			String ratio=request.getParameter("ratio");
			String color=request.getParameter("colorId");
			String productId=request.getParameter("productId");
			String productName=request.getParameter("productName");
			String power=request.getParameter("power");
			String powerOn=request.getParameter("powerOn");
			String powerWait=request.getParameter("powerWait");
			String netweight=request.getParameter("netweight");
			String weight=request.getParameter("weight");
			String gasType = request.getParameter("gas");//AC机器类型 (冷气、热气)
			String weightInclude=request.getParameter("weightInclude");
			String interFace=request.getParameter("interFace");
			String network=request.getParameter("network");
			String os=request.getParameter("os");
			/*String party=request.getParameter("partyId");*/
			String party = (String) request.getSession().getAttribute("loginUserId");
			String status=request.getParameter("status");
			
			String modelName = request.getParameter("modelName");
			String productComments = request.getParameter("comments");
			String introductionDate = request.getParameter("intDate");
			String productSpecId = request.getParameter("productSpecId");
			String productTypeId = request.getParameter("productTypeId");
			String productType = request.getParameter("productType");
			String headProId = request.getParameter("pType");
			String productCatena = request.getParameter("productCatena");
			String editProductId = request.getParameter("editProductId");
			
			if("true".equals(status) || "1".equals(status)){
				status = "1";
			}else{
				status = "2";
			}
			String description=request.getParameter("description");
			
			String filePath = "";
			if(uploadExcel != null){
				filePath = this.productUploadFile(uploadExcel,uploadExcelFileName);
			}
			try {
				Product product = productService.getProduct(id);
				product.setProductId(WebPageUtil.isStringNullAvaliable(productId) ? productId.toUpperCase() : "");//产品编号
				product.setCatena(productCatena);
				product.setHeadType(headProId);
				product.setProductSpecId(productSpecId);
				product.setProductTypeId(productTypeId);
				
				product.setPartyId(party);
				product.setBrandId(brand);
				product.setCategoryId("");//?
				product.setColorId(color);
				product.setComments(productComments);//备注
				product.setDescription(description);//描述
				product.setDescriptionEn("");
				product.setDisplay(display);
				//TODO 如果路径 有更新 则 把原有的附件 删除
				if(uploadExcel != null){
					product.setFilePath(filePath);//TODO:淇濆瓨鏂囦欢
				}
				product.setInterFace(interFace);
				product.setInternalName("");//?
				
				if(!"".equals(introductionDate) && introductionDate != null){
					product.setIntroductionDate(new SimpleDateFormat("yyyy-MM-dd").parse(introductionDate));//涓婂競鏃堕棿
				}
				
				product.setManufacturerPartyId("");//?
				product.setModelName(modelName);//产品型号
				product.setNetweight(netweight);
				product.setNetwork(network);
				product.setOs(os);
				
				product.setPhoto("");//?
				product.setPower(power);
				product.setPowerOn(powerOn);
				product.setPowerWait(powerWait);
				product.setProductFuncId("");//?
				
				product.setProductName(productName);
				product.setProductScreenId("");//?
				product.setProductType(productType);
				product.setQuantityUomId("");
				product.setRatio(ratio);
				product.setSalesDiscontinuationDate(new Date());//?
				product.setSize(size);
				product.setStatus("1".equals(status)?true:false);
				product.setSupportDiscontinuationDate(new Date());//?
				product.setVolume("");//?
				product.setGasType(gasType);//AC机器类型 (冷气、热气)
				product.setWeight(weight);
				product.setWeightInclude(weightInclude);
				
				if(editProductId.equals(modelName)){
					productService.editProduct(product);
					result.accumulate("success", true);
				}else{
					//校验产品编码 是否存在
					int count = productService.searchCountByProductId(modelName);
					if(count == 0){
						productService.editProduct(product);
						result.accumulate("success", true);
					}else{
						result.accumulate("success", false);
						result.accumulate("msg", "TE_0001");
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
	
	public void importProduct() {
		String pType = request.getParameter("pType");
		String xml = "";
		if(Contents.TV.equals(pType)){
			xml = "imports.xml";
		}else if(Contents.AC.equals(pType)){
			xml = "acimports.xml";
		};
		
		try{
			
			String path = getClassRealPath()+ File.separatorChar+"cn"+File.separatorChar+"tcl"+File.separatorChar+"platform" +
			File.separatorChar +"product"+File.separatorChar+"imports"+File.separatorChar+xml;
			
			ExcelImportUtil export = new ExcelImportUtil(path);
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
			if(null==errorMsg || "".equals(errorMsg))
			{
				errorMsg = this.getText("import.error.exist");
			}
			log.error(e.getStackTrace());
			e.printStackTrace();
			WebPageUtil.writeBack(errorMsg);
		}
	}
	
	public void deleteProduct() throws Exception{
		JSONObject result = new JSONObject();
		String _productId = request.getParameter("productId");
		try {
			Product product = new Product();
			product.setProductId(_productId);
			product.setDelUserId(WebPageUtil.getLoginedUserId());
			
			productService.deleteProductByProductId(product);
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
	
	private String productUploadFile(File uploadFlie,String fileName){
		String newFilePath = "";
		JSONObject result = new JSONObject();
		Date d = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateFileName = dateFormat.format(d);
		
		try {
			Properties props = WebPageUtil.getPropertiesByFileName(WebPageUtil.configFileName);
			String wenxinUrl = props.getProperty("WEIXIN.upload.URL");
			String weixinPathName = File.separatorChar+"upload"+File.separatorChar+"product"+File.separatorChar+dateFileName;
			String _weixinPathName = wenxinUrl + weixinPathName;
			File wenxinNewFile = new File(_weixinPathName,fileName);
			FileUtils.copyFile(uploadFlie, wenxinNewFile);
			
			newFilePath = weixinPathName + File.separatorChar + fileName;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			String msg = e.getCause()==null?e.getMessage():e.getCause().getMessage().replaceAll("\"", "").replaceAll("\n", "");
			result.accumulate("success", false);
			result.accumulate("msg", msg);
		}
		return newFilePath;
	}
	/**
	 * 查询 总部产品
	 */
	public void getHQProductParm(){
		JSONArray result = new JSONArray();
		response.setHeader("Content-Type", "application/json");
		try {
			
			List<Product> plist = productService.getHQProductParm();
			result = JSONArray.fromObject(plist);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
		
	}
	
	/**
	 * 
	 * 根据上级查询下级类别
	 */
	
	public void selectCategoryByFatherId(){
		JSONArray result = new JSONArray();
//		借用产品对象存储类别Id、name
		List<Product> list = null;
		try {
			 list = productService.selectCategoryByFatherId();
			 result = JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectCategoryByFatherIdTWo(){
		
		JSONArray result = new JSONArray();
		List<Product> list = null;
		try {
			 list = productService.selectCategoryByFatherIdTWo("1");
			 result = JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectLine (){
		JSONArray result = new JSONArray();
		List<Product> list = null;
		try {
			 list = productService.selectLine();
			 result = JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectACAllSize (){
		JSONArray result = new JSONArray();
		List<Product> list = null;
		try {
			list = productService.selectACAllSize();
			result = JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectACAllClassification (){
		JSONArray result = new JSONArray();
		List<Product> list = null;
		try {
			list = productService.selectACAllClassification();
			result = JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void selectACCatena (){
		JSONArray result = new JSONArray();
		List<Product> list = null;
		try {
			list = productService.selectACCatena();
			result = JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
}
