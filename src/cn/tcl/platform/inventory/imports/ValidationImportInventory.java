package cn.tcl.platform.inventory.imports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionContext;

import cn.tcl.common.BaseAction;
import cn.tcl.platform.inventory.service.IInventoryService;
import cn.tcl.platform.inventory.vo.Inventory;
import cn.tcl.platform.modelmap.service.IModelMapService;
import cn.tcl.platform.modelmap.vo.ModelMap;
import cn.tcl.platform.party.vo.Party;
import cn.tcl.platform.shop.service.IShopService;
import cn.tcl.platform.shop.vo.Shop;


public class ValidationImportInventory extends BaseAction{
	
	public String validationImport(List<Inventory> inventoryList){
		
		ActionContext context = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) context.get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
		ServletContext sc = request.getSession().getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		IInventoryService inventoryService = (IInventoryService) ac.getBean("inventoryService");
		IModelMapService modelMapService = (IModelMapService) ac.getBean("modelmapService");
		IShopService shopService = (IShopService) ac.getBean("shopService");
		/**
		 * 检验 excel 的内容
		 */
		StringBuffer msg = new StringBuffer();
		//判断是否有重复型号
		msg=inventoryIsRepeat(inventoryList);
		try {
			if(msg.length() <= 0){
				for (int i = 0; i < inventoryList.size(); i++) {
					Inventory bean = inventoryList.get(i);
					if(!"".equals(bean.getModel()) && bean.getModel()!=null){
						bean.setModel(bean.getModel().toUpperCase());
					}
					if(!"".equals(bean.getHqModel()) && bean.getHqModel()!=null){
						bean.setHqModel(bean.getHqModel().toUpperCase());
					}
					//所属机构
					String partyName = bean.getPartyId();
					Party party = inventoryService.getOnePartyByName(partyName);
					if(party == null ){
						msg.append(getText("inventory.error.row")+(i+2)+getText("inventory.error.partyName"));
					}
					
					//校验分公司型号 是否唯一
					int c = inventoryService.getCountModelMapByBranch(bean);
					if(c <= 0){
						msg.append(getText("inventory.error.row")+(i+2)+getText("inventory.error.branchModel"));
					}
					//校验总部型号 是否为空
					if("".equals(bean.getHqModel())){
						msg.append(getText("inventory.error.row")+(i+2)+getText("inventory.error.hqModel"));
					}else{
						int hq = inventoryService.searchHqModelMapCount(bean);
						if(hq<=0){
							msg.append(getText("inventory.error.row")+(i+2)+getText("inventory.error.notHqModel"));
						}
					}
					//校验 所属国家是否存在
//					String countryName = bean.getCountryId();
//					int ct = inventoryService.searchCountryByName(countryName);
//					if(ct<0){
//						msg.append(getText("inventory.error.row")+(i+2)+getText("inventory.error.country"));
//					}
					
					//门店是否为空
					String shopName = bean.getShopName();
					
					Shop shop = shopService.getShopByNames(shopName);
					if(shop == null){
						msg.append(getText("inventory.error.row")+(i+2)+getText("inventory.error.shop"));
					}
				}
				/*	//总部型号
					String hqModel = bean.getHqModel();
					String in = inventoryService.getRepeatByHqModel(hqModel);
					if(in == null ){
						msg.append(getText("inventory.error.row")+(i+2)+getText("inventory.error.hqmodel"));
					}*/

					/*//分公司型号
					String list="";
					String cond=null;
					//String model = bean.getModel();
					
					int mi = modelMapService.getModelIdByParty(cond, bean.getModel(), list);
					if(mi == 0){
						msg.append(getText("inventory.error.row")+(i+2)+getText("inventory.error.model"));
					}*/
								
				
			}
			if(msg.length() > 0){
				return msg.toString();
			}else{
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg.append(e.getMessage());
			return msg.toString();
		}
	
	}
	
	
	private StringBuffer inventoryIsRepeat(List<Inventory> inventoryList){
		StringBuffer msg = new StringBuffer();
		List<String> repeatList = new ArrayList<String>();
		for (int i = 0; i < inventoryList.size(); i++) {
			Inventory bean = inventoryList.get(i);
			//分公司型号
			String bran_code = bean.getModel().toUpperCase();
			if(Collections.frequency(repeatList, bran_code) < 1){
				repeatList.add(bran_code);
			}else{
				msg.append(getText("inventory.error.row")+(i+2)+getText("inventory.error.remodel"));
			}
		}
		return msg;
	}
}
