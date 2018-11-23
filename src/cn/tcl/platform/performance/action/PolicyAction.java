package cn.tcl.platform.performance.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.performance.service.PolicyService;
import cn.tcl.platform.performance.vo.Policy;
import cn.tcl.platform.product.vo.Product;

public class PolicyAction extends BaseAction{
	
	@Autowired
	@Qualifier("PolicyService")
	private PolicyService policyService;
	
	
	public void loadPolicyList(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
	
		try {
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			String pageStr=request.getParameter("page");
			int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
			String rowStr=request.getParameter("rows");
			int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
			int start = (page-1)*limit;
			
			
			String conditions=" 1=1";
			String startDate = request.getParameter("startDate");
			
			if(startDate!=null && !"".equals(startDate)){
				conditions += " and ip.start_date = '"+startDate+"'";
			}
			
			String expirationDate = request.getParameter("expirationDate");
			if(expirationDate!=null && !"".equals(expirationDate)){
				conditions += " and ip.Expiration_date ='"+expirationDate+"'";
			}
			
			String searchStr=" ";
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			
			if(!WebPageUtil.isHAdmin()){
				conditions +=" and ul.party_id in (select distinct tt.country_id from party tt where tt.party_id in ("+userPartyIds+"))";
			}else{
				conditions +=" and 1=1";
			}
			Map<String, Object> map = policyService.selectPolicyList(start, limit, searchStr, conditions, order, sort);
			int total = (int)map.get("total");
			List<Policy> list = (ArrayList<Policy>) map.get("rows");
			String rows = JSONArray.fromObject(list).toString();
			result.accumulate("rows", rows);
			result.accumulate("total", total);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 添加奖励政策
	 */
	public void addPolicy(){
		JSONObject result = new JSONObject();
		
		try {
			
			String startDate = request.getParameter("startDate");
			String expirationDate = request.getParameter("expirationDate");
			String qtyCompletionRate = request.getParameter("qtyCompletionRate");
			String amtCompletionRate = request.getParameter("amtCompletionRate");
			String amtReWard = request.getParameter("amtReWard");
			
			Double ex =null;
			Policy po = new Policy();
			
			if(WebPageUtil.getLoginedUser().getPartyId().equals("999")){
				po.sethAmtReWard((new BigDecimal(amtReWard).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
			}else{
				ex = policyService.selectExchange(WebPageUtil.getLoginedUser().getPartyId());
				po.sethAmtReWard((new BigDecimal(ex*(Integer.parseInt(amtReWard))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
				System.out.println((new BigDecimal(ex).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())+"exchange---------");
				System.out.println(Integer.parseInt(amtReWard)+"amtReward-------------------------");
				System.out.println((new BigDecimal(ex*(Integer.parseInt(amtReWard))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())+"------------");
			}
			
			po.setExpirationDate(expirationDate);
			po.setQtyCompletionRate(qtyCompletionRate);
			po.setAmtCompletionRate(amtCompletionRate);
			po.setAmtReWard(amtReWard);
			po.setUserId(WebPageUtil.getLoginedUserId());
			po.setClassId("1");
			po.setStartDate(startDate);
			
			
			policyService.addPolicy(po);
			result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			result.accumulate("success", false);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	/**
	 * 修改政策
	 */
	public void updatePolicy(){
		JSONObject result = new JSONObject();
		String id = request.getParameter("id");
		String startDate = request.getParameter("startDate");
		String expirationDate = request.getParameter("expirationDate");
		String qtyCompletionRate = request.getParameter("qtyCompletionRate");
		String amtCompletionRate = request.getParameter("amtCompletionRate");
		String amtReWard = request.getParameter("amtReWard");
		
		Double ex;
		try {
			Policy po = policyService.selectPolicy(id);
			ex = policyService.selectExchange(WebPageUtil.getLoginedUser().getPartyId());
			
			if(WebPageUtil.getLoginedUser().getPartyId().equals("999")){
				po.sethAmtReWard((new BigDecimal(amtReWard).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
			}else{
				ex = policyService.selectExchange(WebPageUtil.getLoginedUser().getPartyId());
				po.sethAmtReWard((new BigDecimal(ex*(Integer.parseInt(amtReWard))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
			}
			
			po.setStartDate(startDate);
			po.setExpirationDate(expirationDate);
			po.setQtyCompletionRate(qtyCompletionRate);
			po.setAmtCompletionRate(amtCompletionRate);
			po.setAmtReWard(amtReWard);
			
			policyService.updatePolicy(po);
			result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.accumulate("msg", false);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	public void  selectProductLine(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
		try {
			List<Product> list = policyService.selectProductLine();
			String rows = JSONArray.fromObject(list).toString();
			result.accumulate("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WebPageUtil.writeBack(result.toString());
	} 
	
	
	/**
	 * 添加重点系列奖励
	 */
	public void addPolicyByProduct(){
		JSONObject result = new JSONObject();
		
		try {
			
			String startDate = request.getParameter("startDateTwo");
			String expirationDate = request.getParameter("expirationDateTwo");
			String qtyCompletionRate = request.getParameter("qtyCompletionRateTwo");
			String productLine = request.getParameter("productLine");
			String amtReWard = request.getParameter("amtReWardTwo");
			
			Double ex =null;
			Policy po = new Policy();
			
			if(WebPageUtil.getLoginedUser().getPartyId().equals("999")){
				po.sethAmtReWard((new BigDecimal(amtReWard).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
			}else{
				ex = policyService.selectExchange(WebPageUtil.getLoginedUser().getPartyId());
				po.sethAmtReWard((new BigDecimal(ex*(Integer.parseInt(amtReWard))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
			}
			
			po.setExpirationDate(expirationDate);
			po.setQtyCompletionRate(qtyCompletionRate);
			po.setAmtReWard(amtReWard);
			po.setUserId(WebPageUtil.getLoginedUserId());
			po.setClassId("2");
			po.setStartDate(startDate);
			po.setProductLine(productLine);
			
			
			policyService.addPolicyByProduct(po);
			result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			result.accumulate("success", false);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
	/**
	 * 加载重点奖励列表
	 */
	public void loadPolicyListByProduct(){
		JSONObject result = new JSONObject();
		response.setHeader("Content-Type", "application/json");
	
		try {
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			String pageStr=request.getParameter("page");
			int page = Integer.valueOf(pageStr==null|| "".equals(pageStr)?"1":pageStr);
			String rowStr=request.getParameter("rows");
			int limit = Integer.valueOf(rowStr==null|| "".equals(rowStr)?"20":rowStr);
			int start = (page-1)*limit;
			
			
			String conditions=" 1=1";
			String startDate = request.getParameter("startDate");
			
			if(startDate!=null && !"".equals(startDate)){
				conditions += " and ip.start_date = '"+startDate+"'";
			}
			
			String expirationDate = request.getParameter("expirationDate");
			if(expirationDate!=null && !"".equals(expirationDate)){
				conditions += " and ip.Expiration_date ='"+expirationDate+"'";
			}
			
			String searchStr=" ";
			String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
			
			if(!WebPageUtil.isHAdmin()){
				conditions +=" and ul.party_id in (select distinct tt.country_id from party tt where tt.party_id in ("+userPartyIds+"))";
			}else{
				conditions +=" and 1=1";
			}
			Map<String, Object> map = policyService.selectPolicyListByProduct(start, limit, searchStr, conditions, order, sort);
			int total = (int)map.get("total");
			List<Policy> list = (ArrayList<Policy>) map.get("rows");
			String rows = JSONArray.fromObject(list).toString();
			result.accumulate("rows", rows);
			result.accumulate("total", total);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		WebPageUtil.writeBack(result.toString());
	}
	
	
	/**
	 * 修改重点系列政策
	 */
	public void updatePolicyByProduct(){
		JSONObject result = new JSONObject();
		String id = request.getParameter("id");
		String startDate = request.getParameter("startDateTwo");
		String expirationDate = request.getParameter("expirationDateTwo");
		String qtyCompletionRate = request.getParameter("qtyCompletionRateTwo");
		String productLine = request.getParameter("productLine");
		String amtReWard = request.getParameter("amtReWardTwo");
		
		Double ex;
		try {
			Policy po = policyService.selectPolicy(id);
			ex = policyService.selectExchange(WebPageUtil.getLoginedUser().getPartyId());
			
			if(WebPageUtil.getLoginedUser().getPartyId().equals("999")){
				po.sethAmtReWard((new BigDecimal(amtReWard).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
			}else{
				ex = policyService.selectExchange(WebPageUtil.getLoginedUser().getPartyId());
				po.sethAmtReWard((new BigDecimal(ex*(Integer.parseInt(amtReWard))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
			}
			
			po.setStartDate(startDate);
			po.setExpirationDate(expirationDate);
			po.setQtyCompletionRate(qtyCompletionRate);
			po.setProductLine(productLine);
			po.setAmtReWard(amtReWard);
			
			policyService.updatePolicyByProduct(po);
			result.accumulate("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.accumulate("msg", false);
		}
		WebPageUtil.writeBack(result.toString());
	}
}
