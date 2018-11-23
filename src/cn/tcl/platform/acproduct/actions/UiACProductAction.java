package cn.tcl.platform.acproduct.actions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.excel.imports.ExcelImportUtil;
import cn.tcl.platform.acproduct.service.IACProductService;
import cn.tcl.platform.acproduct.vo.ACProduct;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings("all")
public class UiACProductAction  extends BaseAction{
	@Autowired(required = false)
	@Qualifier("acproductService")
	private IACProductService productService;
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
	public void loadProductListData(){//加载信息查询
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try{
			String fatherId = "1";
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
			
			Map<String, Object> map = productService.selectProductsData(start, limit, keyword, order, sort,conditions/*,fatherId,sonId,catId*/);
			int total = (Integer)map.get("total");
			List<ACProduct> list = (ArrayList<ACProduct>)map.get("rows");
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
	
	public void addProduct() throws Exception{
		JSONObject result = new JSONObject();
		
		String model = request.getParameter("model");
		String comments =request.getParameter("comments");
		String description =request.getParameter("description");
		String size =request.getParameter("acsize");

		ACProduct product=new ACProduct();
		product.setModel(model);
		product.setComments(comments);
		product.setDescription(description);
		product.setSize(size);
		
		try {
			//校验产品编码 是否存在
			int count = productService.searchCountByProductId(model.toUpperCase());
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
		
		String id = request.getParameter("id");
		
		if(id!=null && id.trim()!=""){
			String model = request.getParameter("model");
			String editModel = request.getParameter("editModel");
			String comments =request.getParameter("comments");
			String description =request.getParameter("description");
			String size =request.getParameter("acsize");
		
			try {
				ACProduct product = productService.getProduct(id);
				product.setId(Integer.parseInt(id));
				product.setModel(model);
				product.setComments(comments);
				product.setDescription(description);
				product.setSize(size);
				
				if(editModel.equals(model)){
					productService.editProduct(product);
					result.accumulate("success", true);
				}else{
					//校验产品编码 是否存在
					int count = productService.searchCountByProductId(model);
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
		try{
			String path = getClassRealPath()+ File.separatorChar+"cn"+File.separatorChar+"tcl"+File.separatorChar+"platform" +
			File.separatorChar +"acproduct"+File.separatorChar+"imports"+File.separatorChar+"imports.xml";
			
			ExcelImportUtil export = new ExcelImportUtil(path);
			export.init(uploadExcel,uploadExcelFileName,WebPageUtil.getLanguage());
			List<ACProduct> products = export.bindToModelsAndImport(ACProduct.class);
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
			ACProduct product = new ACProduct();
			product.setProductId(_productId);
			
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
			
			List<ACProduct> plist = productService.getHQProductParm();
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
		List<ACProduct> list = null;
		try {
			 list = productService.selectCategoryByFatherId();
			 result = JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
	
	
	public void selectACAllSize (){
		JSONArray result = new JSONArray();
		List<ACProduct> list = null;
		try {
			list = productService.selectACAllSize();
			result = JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
}
