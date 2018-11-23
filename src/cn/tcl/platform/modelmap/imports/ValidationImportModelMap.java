package cn.tcl.platform.modelmap.imports;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionContext;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.modelmap.service.IModelMapService;
import cn.tcl.platform.modelmap.vo.ModelMap;

public class ValidationImportModelMap extends BaseAction{

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
					if(!"".equals(modelMap.getBranchModel()) && modelMap.getBranchModel() != null){
						modelMap.setBranchModel(modelMap.getBranchModel().toUpperCase());
					}
					if(!"".equals(modelMap.getHqModel()) && modelMap.getHqModel() != null){
						modelMap.setHqModel(modelMap.getHqModel().toUpperCase());
					}
					
					//校验 所属国家是否存在
					int cn = modelMapService.searchCountryByName(modelMap);
					if(cn <= 0){
						msg.append(getText("modelmap.error.firstStr")+(lineNum+1)+getText("modelmap.error.prompt06") +"<br/>");
					}
					
					modelMap.setPartyId(modelMapService.searchPartyIdByName(modelMap));
					//校验总部型号 是否为空
					if(!WebPageUtil.isStringNullAvaliable(modelMap.getBranchModel())){
						msg.append(getText("modelmap.error.firstStr")+(lineNum+1)+ " " +getText("modelmap.error.prompt08") +"<br/>");
					}else{
						//校验分公司型号 是否唯一
						String c = modelMapService.searchBeanVerify(modelMap);
						if("modelError".equals(c)){
							/*msg.append(getText("modelmap.error.firstStr")+(lineNum+1)+ getText("modelmap.error.lineStr") +getText("modelmap.error.prompt01") +"<br/>");
						}else if("branchError".equals(c)){
						*/	msg.append(getText("modelmap.error.firstStr")+(lineNum+1)+getText("modelmap.error.prompt03") +"<br/>");
						}
					}
					//校验总部型号 是否为空
					if("".equals(modelMap.getHqModel())){
						
						msg.append(getText("modelmap.error.firstStr")+(lineNum+1)+getText("modelmap.error.prompt05") +"<br/>");
						
					}else{
						int hq = modelMapService.searchHqModelMapCount(modelMap);
						if(hq<=0){
							msg.append(getText("modelmap.error.firstStr")+(lineNum+1)+getText("modelmap.error.prompt04") +"<br/>");
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
	
	public String validationChannelModelImport(List<ModelMap> modelmaps){
		StringBuffer msg = new StringBuffer();
		int lineNum = 0;
		try {
			ActionContext context = ActionContext.getContext();
			HttpServletRequest request = (HttpServletRequest) context.get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
			ServletContext sc = request.getSession().getServletContext();
			ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
			IModelMapService modelMapService = (IModelMapService) ac.getBean("modelmapService");
			
			if(msg.length() <= 0){
				Set<ModelMap> sm = new HashSet<ModelMap>();
				for (ModelMap modelMap : modelmaps) {
					lineNum ++;
					
					//模板是否存在重复数据（同一渠道同一型号不能重复）
					boolean one= sm.add(modelMap);
					if(!one){
						msg.append(getText("modelmap.error.firstStr") + (lineNum+1)+" "+ getText("modelmap.error.prompt11") + " <br/>");
					};
					
					//校验 所属国家是否存在
					String country;
					if(WebPageUtil.isStringNullAvaliable(modelMap.getPartyId())){
						country = modelMap.getPartyId();
						int cn = modelMapService.searchCountryByName(modelMap);
						if(cn <= 0){
							country = "";
							msg.append(getText("modelmap.error.firstStr")+(lineNum+1)+" "+getText("modelmap.error.prompt06") + " <br/>");
						}
					}else{
						country = "";
						msg.append(getText("modelmap.error.firstStr")+(lineNum+1)+" "+getText("modelmap.error.prompt12") + " <br/>");
					}
					
					//校验 所属渠道是否存在
					String channel;
					String channelId = "-1";//渠道不存在
					if(WebPageUtil.isStringNullAvaliable(modelMap.getCustomerName())){
						channel = modelMap.getCustomerName();
						int cn = modelMapService.searchChannelByNameCount(country,channel);
						if(cn <= 0 ){
							msg.append(getText("modelmap.error.firstStr")+(lineNum+1)+" "+getText("modelmap.error.prompt14") + " <br/>");
						}else{
							channelId = modelMapService.searchChannelByName(country,channel) + "";
						}
					}else{
						channel = "";
						msg.append(getText("modelmap.error.firstStr")+(lineNum+1)+" "+getText("modelmap.error.prompt13") + " <br/>");
					}
					
					//检验渠道型号是否存在
					if(!WebPageUtil.isStringNullAvaliable(modelMap.getChannelModel())){
						msg.append(getText("modelmap.error.firstStr")+(lineNum+1)+" "+getText("modelmap.error.prompt10") + " <br/>");
					}else{
						//同一渠道同一型号不能重复//modelmap.error.prompt15
						String c = modelMapService.channelModelIsBeing(channelId,modelMap.getChannelModel(),modelMap.getBranchModel(),null);
						/*if("branModelError".equals(c)){
							msg.append(getText("modelmap.error.firstStr")+(lineNum+1)+" "+ getText("modelmap.error.lineStr")+getText("modelmap.error.prompt15") + " <br/>");
						}else*/ if("custModelError".equals(c)){
							msg.append(getText("modelmap.error.firstStr")+(lineNum+1)+" "+getText("modelmap.error.prompt09") + " <br/>");
						}
					}
					
					//校验分公司型号 是否为空
					if(!WebPageUtil.isStringNullAvaliable(modelMap.getBranchModel())){
						msg.append(getText("modelmap.error.firstStr")+(lineNum+1)+" "+getText("modelmap.error.prompt08") + " <br/>");
					}else{
						int hq = modelMapService.branchModelIsBeing(modelMap);
						if(hq<=0){
							msg.append(getText("modelmap.error.firstStr")+(lineNum+1)+" "+getText("modelmap.error.prompt16") + " <br/>");
						}
					}
				}
			}
		
			if(msg.length() > 0){
				return " <br/>" + msg.toString();
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
		Set<ModelMap> setModelSTO= new HashSet<ModelMap>();
		for (int i = 0; i < modelmapList.size(); i++) {
			ModelMap bean = modelmapList.get(i);
			boolean one=setModelSTO.add(bean);
			if(!one){
				msg.append(getText("modelmap.error.firstStr")+(i+2)+getText("modelmap.error.prompt07") + "<br/>");
			}
			
			/*//分公司型号
			String bran_code = bean.getBranchModel().toUpperCase();
			if(Collections.frequency(repeatList, bran_code) < 1){
				repeatList.add(bran_code);
			}else{
				msg.append(getText("modelmap.error.firstStr")+(i+2)+getText("modelmap.error.prompt07") + "<br/>");
			}*/
		}
		return msg;
	}
	
}
