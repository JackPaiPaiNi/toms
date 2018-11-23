package cn.tcl.platform.customer.imports;

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
import cn.tcl.platform.party.vo.Party;

public class ValidationImportCustomer extends BaseAction{

	public String validationImport(List<Customer> customerList){
		ActionContext context = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) context.get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
		ServletContext sc = request.getSession().getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		ICustomerService customerService = (ICustomerService) ac.getBean("customerService");
		
		/**
		 * 检验 excel 的内容
		 */
		StringBuffer msg = new StringBuffer();
		//1.判断  客户代码是否重复
		msg = customerIsRepeat(customerList);
		try {
			if(msg.length() <= 0){
				//通过数据库校验 是否重复
				for (int i = 0; i < customerList.size(); i++) {
					Customer bean = customerList.get(i);
					//客户code
					String code = bean.getCustomerCode();
					if(code==null){
						msg.append(getText("customer.error.firstStr")+(i+2)+getText("customer.error.prompt13"));
					}else{
						Customer customer = customerService.getRepeatByCustomerCode(code);
						if(customer != null){
							msg.append(getText("customer.error.firstStr")+(i+2)+getText("customer.error.prompt02"));
						}
					}
					//所属机构
//					String partyName = bean.getPartyId();
//					Party party = customerService.getOnePartyByName(partyName);
//					if(party == null){
//						msg.append(getText("customer.error.firstStr")+(i+2)+getText("customer.error.prompt03"));
//					}
					//渠道类型
					String channelType = bean.getChannelType();
					int ct = customerService.getCountByChannel(channelType);
					if(ct == 0){
						msg.append(getText("customer.error.firstStr")+(i+2)+getText("customer.error.prompt04"));
					}
					
					//国家省市县镇
					String country = bean.getCountryId();
					int ci = customerService.getCountryByName(country);
					if(ci == 0){
						msg.append(getText("customer.error.firstStr")+(i+2)+getText("customer.error.prompt06"));
					}
					
					String province = bean.getProvinceId();
					if(province!=null && !"".equals(province)){
						int pi = customerService.getProvinceByName(province);
						if(pi == 0){
							msg.append(getText("customer.error.firstStr")+(i+2)+getText("customer.error.prompt07"));
						}
					}
				
					
					//校验渠道名字
					String customerName = bean.getCustomerName();
					if(customerName==null){
						msg.append(getText("customer.error.firstStr")+(i+2)+getText("customer.error.prompt11"));
					}
					
					String detailAddress = bean.getDetailAddress();
					if(detailAddress==null){
						msg.append(getText("customer.error.firstStr")+(i+2)+getText("customer.error.prompt12"));
					}
					
//					String city = bean.getCityId();
//					if(city != null && !"".equals(city)){
//						int _ci = customerService.getCityByName(city);
//						if(_ci == 0){
//							msg.append(getText("customer.error.firstStr")+(i+2)+getText("customer.error.prompt08"));
//						}
//					}
//
//					String county = bean.getCountyId();
//					if(county != null && !"".equals(county)){
//						int gc = customerService.getCountyByName(county);
//						if(gc == 0){
//							msg.append(getText("customer.error.firstStr")+(i+2)+getText("customer.error.prompt09"));
//						}
//					}
//					
//					String town = bean.getTownId();
//					if(town != null && !"".equals(town)){
//						int ti = customerService.getTownByName(town);
//						if(ti == 0){
//							msg.append(getText("customer.error.firstStr")+(i+2)+getText("customer.error.prompt10"));
//						}
//					}
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
	
	private StringBuffer customerIsRepeat(List<Customer> customerList){
		StringBuffer msg = new StringBuffer();
		List<String> repeatList = new ArrayList<String>();
		for (int i = 0; i < customerList.size(); i++) {
			Customer bean = customerList.get(i);
			String code = bean.getCustomerCode();
			if(Collections.frequency(repeatList, code) < 1){
				repeatList.add(code);
			}else{
				msg.append(getText("customer.error.firstStr")+(i+2)+getText("customer.error.prompt01"));
			}
		}
		return msg;
	}
	
}
