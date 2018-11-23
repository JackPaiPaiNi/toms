package cn.tcl.platform.barcode.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


import cn.tcl.common.BaseAction;
import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.barcode.service.IBarcodeService;
import cn.tcl.platform.barcode.vo.Barcode;
import cn.tcl.platform.product.vo.Product;

@SuppressWarnings("all")
public class BarcodeAction extends BaseAction{
	
	@Autowired(required = false)
	@Qualifier("barcodeService")
	private IBarcodeService barcodeService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 页面
	 * @return
	 */
	public String loadBarcodePage(){
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
	public void loadBarcodeListData(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try {
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			String keyword = request.getParameter("keyword");
			String pageStr=request.getParameter("page");
			int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
			String rowStr=request.getParameter("rows");
			int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
			int start = (page-1)*limit;
			
			Map<String, Object> map = barcodeService.selectBarcodeData(start, limit, keyword, order, sort);
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
	public void editBarcode(){
		JSONObject result = new JSONObject();
		String id = request.getParameter("editId");
		if(id!=null && id.trim()!=""){
			String _barcode = request.getParameter("barcode");
			String _hqModel = request.getParameter("hqModel");
			//String _time = sdf.format(new Date());
			try {
				Barcode barcodeBean = barcodeService.getBarcodeByid(id);
				barcodeBean.setBarcode(_barcode);
				barcodeBean.setHqModel(_hqModel);
				//barcodeBean.setCtime(_time);
				barcodeService.editBarcode(barcodeBean);
				result.accumulate("success", true);
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
	 * 新增
	 */
	public void addBarcode() throws Exception{
		JSONObject result = new JSONObject();
		String _barcode = request.getParameter("barcode");
		String _hqModel = request.getParameter("hqModel");
		String _time = sdf.format(new Date());
		try {
			Barcode barcode = new Barcode();
			barcode.setBarcode(_barcode);
			barcode.setHqModel(_hqModel);
			barcode.setCtime(_time);
			
			barcodeService.addBarcode(barcode);
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
	public void deleteBarcode() throws Exception{
		JSONObject result = new JSONObject();
		String _id = request.getParameter("id");
		try {
			Barcode barcode = new Barcode();
			barcode.setId(_id);
			
			barcodeService.deleteBarcode(barcode);
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
