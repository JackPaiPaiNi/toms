package cn.tcl.platform.shop.imports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionContext;

import cn.tcl.common.BaseAction;
import cn.tcl.platform.customer.service.ICustomerService;
import cn.tcl.platform.customer.vo.Customer;
import cn.tcl.platform.shop.service.IShopService;
import cn.tcl.platform.shop.vo.Level;
import cn.tcl.platform.shop.vo.Shop;

public class ValidationImportShop extends BaseAction{

	public String validationImport(List<Shop> ShopList){
		ActionContext context = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) context.get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
		ServletContext sc = request.getSession().getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		ICustomerService customerService = (ICustomerService) ac.getBean("customerService");
		IShopService shopService = (IShopService) ac.getBean("shopService");
		
		/**
		 * 检验 excel 的内容
		 */
		StringBuffer msg = new StringBuffer();
		
		try {
			msg = shopIsRepeat(ShopList);
		
			if(msg.length() <= 0){
				for (int i = 0; i < ShopList.size(); i++) {
					Shop bean = ShopList.get(i);
					
					String shopName = bean.getShopName();
					String ccode = bean.getCustomerId();
					
					Shop shop = shopService.getRepeatByName(shopName);
					if(shop != null){
						msg.append("</br>"+getText("shop.error.firstStr")+(i+2)+" "+getText("shop.error.prompt08")+"</br>");
					}
					
					Customer customer = customerService.getRepeatByCustomerCode(ccode);
					if(customer == null){
						msg.append("</br>"+getText("shop.error.firstStr")+(i+2)+" "+getText("shop.error.prompt01")+"</br>");
					}
					
					//国家省市县镇
					String country = bean.getCountryId();
					int ci = customerService.getCountryByName(country);
					if(ci == 0){
						msg.append("</br>"+getText("shop.error.firstStr")+(i+2)+" "+getText("shop.error.prompt02")+"</br>");
					}
					
					String province = bean.getProvinceId();
					int pi = customerService.getProvinceByName(province);
					if(pi == 0){
						msg.append("</br>"+getText("shop.error.firstStr")+(i+2)+" "+getText("shop.error.prompt03")+"</br>");
					}
					
					
					String party= bean.getPartyId();
					int pai = shopService.selectPartyByCount(party);
					if(pai == 0){
						msg.append("</br>"+getText("shop.error.firstStr")+(i+2)+" "+getText("shop.error.party")+"</br>");
					}
					
					//门店location
					int loaction = shopService.selectLocationIsExist(bean.getLocation());
					if(loaction != 0){
						msg.append(getText("shop.error.firstStr")+(i+2)+getText("excel.error.row")+getText("shop.error.prompt09"));
					}
					
					//门店等级
					String level = bean.getLevel();
					String countryName = bean.getCountryId();
					String countryId = shopService.selectLevelByCountryName(countryName);
					Level le = shopService.selectLevelName(level, countryId);
					/*if(le==null){
						msg.append("</br>"+getText("shop.error.firstStr")+(i+2)+" "+getText("shop.error.level")+"</br>");
					}*/
					
					/*String city = bean.getCityId();
					if(city != null && !"".equals(city)){
						int _ci = customerService.getCityByName(city);
						if(_ci == 0){
							msg.append(getText("shop.error.firstStr")+(i+2)+getText("shop.error.prompt04"));
						}
					}
	
					String county = bean.getCountyId();
					if(county != null && !"".equals(county)){
						int gc = customerService.getCountyByName(county);
						if(gc == 0){
							msg.append(getText("shop.error.firstStr")+(i+2)+getText("shop.error.prompt05"));
						}
					}
					
					String town = bean.getTownId();
					if(town != null && !"".equals(town)){
						int ti = customerService.getTownByName(town);
						if(ti == 0){
							msg.append(getText("shop.error.firstStr")+(i+2)+getText("shop.error.prompt06"));
						}
					}*/
					
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
	
	private StringBuffer shopIsRepeat(List<Shop> ShopList){
		StringBuffer msg = new StringBuffer();
		List<String> repeatList = new ArrayList<String>();
		List<String> repeatLoaction = new ArrayList<String>();
		for (int i = 0; i < ShopList.size(); i++) {
			Shop bean = ShopList.get(i);
			//门店名称
			String s_name = bean.getShopName();
			//客户代码
			String c_code = bean.getCustomerId();
			
			//门店位置 (门店位置是唯一的,不可重复存在的)
			String s_loac = bean.getLocation();
			//通过 "_" 组装成字符串 验证是否有重复存在--Shop_name + "_" + customer_code
			String onlyStr = s_name + "_" + c_code;
			
			if(Collections.frequency(repeatLoaction, s_loac) < 1){
				repeatLoaction.add(s_loac);
			}else {
				msg.append(getText("shop.error.firstStr")+(i+2)+getText("excel.error.row")+getText("shop.error.prompt10"));
			}
				
			if(Collections.frequency(repeatList, onlyStr) < 1){
				repeatList.add(onlyStr);
			}else{
				msg.append(getText("shop.error.firstStr")+(i+2)+getText("shop.error.prompt08"));
			}
		}
		return msg;
	}
	
}
