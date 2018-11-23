package cn.tcl.platform.acproduct.imports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionContext;

import cn.tcl.common.BaseAction;
import cn.tcl.platform.acproduct.service.IACProductService;
import cn.tcl.platform.acproduct.vo.ACProduct;

public class ValidationImportProduct extends BaseAction{
	private static final long serialVersionUID = 1L;

	public String validationImport(List<ACProduct> products){
		StringBuffer msg = new StringBuffer();
		try {
			ActionContext context = ActionContext.getContext();
			HttpServletRequest request = (HttpServletRequest) context.get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
			ServletContext sc = request.getSession().getServletContext();
			ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
			IACProductService productservice = (IACProductService) ac.getBean("acproductService");
			
			//1.�ж� excel ���Ƿ����ظ���Ʒ�ͺ�"importProduct"
			msg = productIsRepeat(products);
			
			//2.ͨ�����ݿ�У�� �Ƿ��ظ�,����У�鲿�������Ƿ����
			if(msg.length() <= 0){
				for (int i = 0; i < products.size(); i++) {
					ACProduct product = products.get(i);
					
					/*AC型号是否存在*/
					boolean isModelExist = productservice.selectWhetherTheModelExists(product.getModel().trim()) > 0 ? true : false;
					if(isModelExist){
						msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt02"));
					};
					
					/*AC尺寸是否存在*/
					isModelExist = productservice.selectWhetherTheDimensionsExist(product.getSize().trim()) < 0 ? true : false;
					if(isModelExist){
						msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt06"));
					};
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
	
	private StringBuffer productIsRepeat(List<ACProduct> productList){
		StringBuffer msg = new StringBuffer();
		List<String> repeatList = new ArrayList<String>();
		for (int i = 0; i < productList.size(); i++) {
			ACProduct bean = productList.get(i);
			String pid = "";
			if(!"".equals(bean.getModel()) && bean.getModel() != null){
				pid = bean.getModel().toUpperCase();
			}
			if(Collections.frequency(repeatList, pid) < 1){
				repeatList.add(pid);
			}else{
				msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt03"));
			}
		}
		return msg;
	}
}
