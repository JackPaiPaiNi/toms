package cn.tcl.platform.target.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.customer.vo.Customer;
import cn.tcl.platform.excel.service.IExcelService;
import cn.tcl.platform.excel.service.ImportExcelService;
import cn.tcl.platform.shop.vo.Shop;
import cn.tcl.platform.target.service.ImportTargetService;
import cn.tcl.platform.target.vo.Target;
@SuppressWarnings("all")
public class TargetAction extends BaseAction {

	@Autowired(required = false)
	@Qualifier("importTargetService")
	private ImportTargetService importTargetService;
	
	
	
	public  void  ChannelTarget(){
		System.out.println("importChannelTarget");
		try {

			String errorMsg = importTargetService.readExcel(uploadExcel,
					uploadExcelFileName,"channel");

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
	
	
	
	public  void  OfficeTarget(){
		System.out.println("importOfficeTarget");
		try {

			String errorMsg = importTargetService.readExcel(uploadExcel,
					uploadExcelFileName,"office");

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
	
	
	public  void  RegTarget(){
		System.out.println("importRegTarget");
		try {

			String errorMsg = importTargetService.readExcel(uploadExcel,
					uploadExcelFileName,"reg");

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

	//经销商
	public void selectCustomer(){
		JSONObject result=new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("partyId");
		System.out.println(partyId+"===========111111===============");
		try {
			List<Customer> CusList = importTargetService.selectCustomer(partyId);
			String rows = JSONArray.fromObject(CusList).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
	//门店
	public void selectShop(){
		JSONObject result= new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("partyId");
		try {
			List<Shop> shopList = importTargetService.selectShop(partyId);
			String rows = JSONArray.fromObject(shopList).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	//业务员
	public void selectSale(){
		JSONObject result= new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("partyId");
		String salerType = request.getParameter("saleType");
		System.out.println(partyId+"======================"+salerType);
		
		Map<String,Object> staMap = new HashMap<String,Object>();
		staMap.put("partyId", partyId);
		staMap.put("saleType", salerType);
		try {
			List<Shop> shopSale = importTargetService.selectSale(staMap);
			String rows = JSONArray.fromObject(shopSale).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	//业务经理
		public void selectManager(){
			JSONObject result= new JSONObject();
			response.setHeader("Content-Type", "application/json");
			String partyId = request.getParameter("partyId");
			
			try {
				List<Shop> shopMan = importTargetService.selectManager(partyId);
				String rows = JSONArray.fromObject(shopMan).toString();
				result.accumulate("rows", rows);
			} catch (Exception e) {
				e.printStackTrace();
			}
			WebPageUtil.writeBack(result.toString());
		}
		
	//单品
	public void selectProduct(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("partyId");
		System.out.println(partyId+"======================");
		
		try {
			List<Shop> proList = importTargetService.selectProduct(partyId);
			String rows = JSONArray.fromObject(proList).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void chooseRegion(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("id");
		
		try {
			List<Target> regionList = importTargetService.chooseRegion(partyId);
			String rows = JSONArray.fromObject(regionList).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void chooseOffice(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		String partyId = request.getParameter("id");
		
		try {
			List<Target> regionList = importTargetService.chooseOffice(partyId);
			String rows = JSONArray.fromObject(regionList).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	}
}
