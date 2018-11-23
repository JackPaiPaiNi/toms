package cn.tcl.platform.sale.imports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionContext;

import cn.tcl.common.BaseAction;
import cn.tcl.platform.customer.service.ICustomerService;
import cn.tcl.platform.customer.vo.Customer;
import cn.tcl.platform.modelmap.dao.IModelMapDao;
import cn.tcl.platform.modelmap.service.IModelMapService;
import cn.tcl.platform.modelmap.vo.ModelMap;
import cn.tcl.platform.party.service.IPartyService;
import cn.tcl.platform.party.vo.Party;
import cn.tcl.platform.product.service.IProductService;
import cn.tcl.platform.product.vo.Product;
import cn.tcl.platform.sale.vo.Sale;
import cn.tcl.platform.shop.service.IShopService;
import cn.tcl.platform.shop.vo.Shop;

public class ValidationImportSale extends BaseAction{

	public String validationImport(List<Sale> SaleList){
		ActionContext context = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) context.get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
		ServletContext sc = request.getSession().getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		ICustomerService customerService = (ICustomerService) ac.getBean("customerService");
		IShopService shopService = (IShopService) ac.getBean("shopService");
		IModelMapService modelMapService = (IModelMapService) ac.getBean("modelmapService");
		IPartyService partyService = (IPartyService) ac.getBean("partyService");
		
		/**
		 * 检验 excel 的内容
		 */
		StringBuffer msg = new StringBuffer();
		
		try {
			//msg = saleIsRepeat(SaleList);
			if(msg.length() <= 0){
				for (int i = 0; i < SaleList.size(); i++) {
					Sale bean = SaleList.get(i);
					
					
					
					
					
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
					Date date = new Date();
					String dt = df.format(date);
					Date dt1 = df.parse(dt);
					Date dt2;
					dt2 = df.parse(bean.getDatadate());
					if (dt1.getTime() < dt2.getTime()) {
						msg.append(getText("sale.error.row") + (i + 1)
								+ getText("sale.error.time") + "<br/>");
					} 
					
					
					
					
					
					
					
					
					
					
					
					String country = bean.getCountry();
					String ci = customerService.getCountryByNames(country);
					if(ci == null ){
						msg.append(getText("sale.error.row")+(i+2)+getText("sale.error.country"));
					}

					
					String shopName = bean.getShopName();
					
					Shop shop = shopService.getShopByNames(shopName);
					if(shop == null){
						msg.append(getText("sale.error.row")+(i+2)+getText("sale.error.shop"));
					}
					List<Party> partys=partyService.getCountryByPartyId(ci);
					String list="";
					
				
					String cond = null;
					/*if(partys!=null || partys.size()!=0){
						for (int j = 0; j < partys.size(); j++) {
							
							if(j!=partys.size()-1){
								list+="'"+partys.get(j).getPartyId()+"'"+",";
							}else{
								list+="'"+partys.get(j).getPartyId()+"'";
							}
						}
						
						cond+="AND t.party_id in ("+list+" )";

						
					}*/
					//产品型号
					int model = modelMapService.getModelIdByParty(cond,bean.getModel(),list);
					if(model == 0){
						msg.append(getText("sale.error.row")+(i+2)+getText("sale.error.model"));
					}
					//国家
					
					
				}
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
	
/*	private StringBuffer saleIsRepeat(List<Sale> SaleList){
		StringBuffer msg = new StringBuffer();
		List<String> repeatList = new ArrayList<String>();
		for (int i = 0; i < SaleList.size(); i++) {
			Sale bean = SaleList.get(i);
			//门店名称
			String s_name = bean.getShopName();
			//机型
			String s_model = bean.getModel();
			
			//通过 "_" 组装成字符串 验证是否有重复存在--Shop_name + "_" + customer_code
			String onlyStr = s_name + "_" + s_model;
			
			if(Collections.frequency(repeatList, onlyStr) < 1){
				repeatList.add(onlyStr);
			}else{
				msg.append(getText("shop.error.firstStr")+(i+2)+getText("shop.error.prompt08"));
			}
		}
		return msg;
	}*/
	
}
