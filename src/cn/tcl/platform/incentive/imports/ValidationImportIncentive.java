package cn.tcl.platform.incentive.imports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionContext;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.incentive.service.IncentiveService;
import cn.tcl.platform.incentive.vo.Incentive;

public class ValidationImportIncentive extends BaseAction{

	
	
	public String validationImport(List<Incentive> incenList){
		ActionContext context = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) context.get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
		ServletContext sc = request.getSession().getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		IncentiveService incentiveService = (IncentiveService) ac.getBean("percentageService");
		
		
		/**
		 * 校验EXCEL中的内容
		 */
		StringBuffer msg = new StringBuffer();
		 msg = incentiveIsRepeat(incenList);
		
		 try {
			if(msg.length()<=0){
				for (int i = 0; i < incenList.size(); i++) {
					Incentive incentive = incenList.get(i);
					
					
					//校验国家是否为空
					if(!WebPageUtil.isStringNullAvaliable(incentive.getPartyId())){
						msg.append(getText("incentive.error.row")+(i+2)+getText("incentive.error.partyName")+"<br/>");
					}else{
						String partyId = incentiveService.getPartyIdByCountryName(incentive);
						if(partyId==null){//国家是否存在
							msg.append(getText("incentive.error.row")+(i+2)+getText("incentive.error.no.partyName")+"<br/>");
						}
					}
					
					//校验分公司型号是否为空
					if(!WebPageUtil.isStringNullAvaliable(incentive.getBranchModel())){
						msg.append(getText("incentive.error.row")+(i+2)+getText("incentive.error.branchModel")+"<br/>");
					}else{
						String countryId = incentiveService.getPartyIdByCountryName(incentive);
						int count = incentiveService.countBranchModel(incentive.getBranchModel(),countryId);
						if(count==0){
							msg.append(getText("incentive.error.row")+(i+2)+getText("incentive.error.no.branchModel")+"<br/>");
						}
						
					}
					

					if(!WebPageUtil.isStringNullAvaliable(incentive.getRetailPrice())){//零售价不能为空
						msg.append(getText("incentive.error.row")+(i+2)+getText("incentive.error.retailPrice")+"<br/>");
					}else{
						if(!NumberUtils.isNumber(incentive.getRetailPrice())){//零售价是否是数字
							msg.append(getText("incentive.error.row")+(i+2)+getText("incentive.error.retailPrice.noNum")+"<br/>");
						}
					}
					
					if(!WebPageUtil.isStringNullAvaliable(incentive.getIncentive())){//提成不能为空
						msg.append(getText("incentive.error.row")+(i+2)+getText("incentive.error.incentive")+"<br/>");
					}else{
						if(!NumberUtils.isNumber(incentive.getIncentive())){
							msg.append(getText("incentive.error.row")+(i+2)+getText("incentive.error.incentive.noNum")+"<br/>");
						}
					}
					
//					if(incentive.getQuantity()==null){
//						msg.append(getText("incentive.error.row")+(i+2)+getText("incentive.error.quantity")+"<br/>");
//					}else{
//						if(!NumberUtils.isNumber(incentive.getQuantity())){
//							msg.append(getText("incentive.error.row")+(i+2)+getText("incentive.error.quantity.noNum")+"<br/>");
//						}
//					}
					
					if(incentive.getDate()==null){//时间不能为空
						msg.append(getText("incentive.error.row")+(i+2)+getText("incentive.error.date")+"<br/>");
					}else{
						try {
							SimpleDateFormat format = new SimpleDateFormat("MM-yyyy");// 设置日期格式
							format.setLenient(false);
							System.out.println(incentive.getDate()+"date");
							format.parse(incentive.getDate());
							
						} catch (Exception e) {
							e.printStackTrace();
							msg.append(getText("incentive.error.row")+(i+2)+getText("incentive.error.Nodate")+"<br/>");	
						}
					}

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
	
	
	
	private StringBuffer incentiveIsRepeat(List<Incentive> incen){
		StringBuffer msg = new StringBuffer();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < incen.size(); i++) {
			Incentive bean = incen.get(i);
			if(bean.getBranchModel()!=null && !"".equals(bean.getBranchModel())){
				String upperCase = bean.getBranchModel().toUpperCase();
				if(Collections.frequency(list, upperCase) < 1){
					list.add(upperCase);
				}else{
					msg.append(getText("incentive.error.row")+(i+2)+getText("incentive.error.remodel")+"<br/>");
				}
			}
		}
		return msg;		
	}
}
