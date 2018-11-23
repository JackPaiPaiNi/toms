package cn.tcl.platform.modelmap.imports;

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
import cn.tcl.platform.modelmap.service.IModelMapService;
import cn.tcl.platform.modelmap.vo.ModelMap;

public class ValidationImportModelPrice extends BaseAction{

	public String validationImport(List<ModelMap> modelmaps){
		StringBuffer msg = new StringBuffer();
		int lineNum = 0;
		try {
			ActionContext context = ActionContext.getContext();
			HttpServletRequest request = (HttpServletRequest) context.get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
			ServletContext sc = request.getSession().getServletContext();
			ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
			IModelMapService modelMapService = (IModelMapService) ac.getBean("modelmapService");
			
			msg = modelmapIsRepeat(modelmaps);
			if(msg.length() <= 0){
				for (ModelMap modelMap : modelmaps) {
					lineNum ++;
					
					//校验 所属国家是否存在
					int cn = modelMapService.searchCountryByName(modelMap);
					if(cn <= 0){
						msg.append(getText("modelmap.error.firstStr")+(lineNum+1)+getText("modelmap.error.prompt06"));
					}
					//校验分公司型号 是否唯一
					int c = modelMapService.bSearchBeanLimit(modelMap);
					System.out.println("=================================="+c);
					if(c <= 0){
						msg.append(getText("modelmap.error.firstStr")+(lineNum+1)+getText("modelmap.error.prompt08"));
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
	
	private StringBuffer modelmapIsRepeat(List<ModelMap> modelmapList){
		StringBuffer msg = new StringBuffer();
		List<String> repeatList = new ArrayList<String>();
		for (int i = 0; i < modelmapList.size(); i++) {
			ModelMap bean = modelmapList.get(i);
			//分公司型号
			String bran_code = bean.getBranchModel().toUpperCase();
			if(Collections.frequency(repeatList, bran_code) < 1){
				repeatList.add(bran_code);
			}else{
				msg.append(getText("modelmap.error.firstStr")+(i+2)+getText("modelmap.error.prompt07"));
			}
		}
		return msg;
	}
}
