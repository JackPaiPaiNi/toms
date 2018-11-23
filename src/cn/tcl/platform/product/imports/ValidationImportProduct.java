package cn.tcl.platform.product.imports;

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
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.product.service.IProductService;
import cn.tcl.platform.product.vo.Product;

public class ValidationImportProduct extends BaseAction{
	private static final long serialVersionUID = 1L;

	public String validationImport(List<Product> Products){
		StringBuffer msg = new StringBuffer();
		try {
			ActionContext context = ActionContext.getContext();
			HttpServletRequest request = (HttpServletRequest) context.get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
			ServletContext sc = request.getSession().getServletContext();
			ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
			IProductService productService = (IProductService) ac.getBean("productService");
			
			//1.�ж� excel ���Ƿ����ظ���Ʒ�ͺ�"importProduct"
			msg = productIsRepeat(Products);
			
			//2.ͨ�����ݿ�У�� �Ƿ��ظ�,����У�鲿�������Ƿ����
			if(msg.length() <= 0){
				for (int i = 0; i < Products.size(); i++) {
					Product product = productsLtrim(Products.get(i));
					String party = (String) request.getSession().getAttribute("loginUserId");
					product.setPartyId(party);
					
					if(!"".equals(product.getModelName()) && product.getModelName() != null){
						product.setModelName(product.getModelName().toUpperCase());
					}
					product.setPartyId(product.getModelName().trim());
					
					//��Ʒ�ͺ�
					Product pb = productService.searchProductById(product);
					if(pb != null){
						msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt02"));
					}
					
					//-------------������---------------------------
					//��Ʒ����
					/*int pt = productService.searchParamByNameType(product.getProductTypeId(),"TCL_PRODUCT","TYPE");*/
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("paramType", 2);
					map.put("paramValue", product.getProductTypeId());
					int pt = productService.selectModel(map);
					if(pt <= 0){
						msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt04"));
					}
					
					//��Ʒ����
					Map<String,Object> amap = new HashMap<String,Object>();
					amap.put("paramType", 1);
					amap.put("paramValue", product.getProductSpecId());
					pt = productService.selectModel(amap);
					if(pt <= 0){
						msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt13"));
					}
					
					//��Ʒϵ��(δУ��)
				    if(!"".equals(product.getSeries()) && null != product.getSeries()){
				    	int xl = productService.searchParamByNameType(product.getSeries(),"TCL_PRODUCT","line");
						if(xl <= 0){
							msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt14"));
						}
					}
					
					//�����ߴ�
					int ts = productService.searchParamByNameType(product.getSize(),"TCL_PRODUCT","SIZE");
					if(ts <= 0){
						msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt06"));
					}
					
					//�ֱ���
					if(!"".equals(product.getDisplay()) && null != product.getDisplay()){
						int s = productService.searchParamByNameType(product.getDisplay(),"TCL_PRODUCT","DISPLAY");
						if(s <= 0){
							msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt07"));
						}
					}
					
					//��Ļ����
					if(!"".equals(product.getRatio()) && null != product.getRatio()){
						int td = productService.searchParamByNameType(product.getRatio(),"TCL_PRODUCT","SCALE");
						if(td <= 0){
							msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt08"));
						}
					}
					
					//����ϵͳ
					if(!"".equals(product.getOs()) && product.getOs() != null){
						int sn = productService.searchParamByNameType(product.getOs(),"TCL_PRODUCT","OS");
						if(sn <= 0){
							msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt11"));
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
	
	/**
	 * 产品信息去空格
	 * @param product
	 */
	public Product productsLtrim(Product product){
		if (product == null){
			return null;
		}
		
		boolean isExist = WebPageUtil.isStringNullAvaliable(product.getModelName());
		product.setModelName(isExist ? product.getModelName().trim() : "");
		
		isExist = WebPageUtil.isStringNullAvaliable(product.getProductTypeId());
		product.setProductTypeId(isExist ? product.getProductTypeId().trim() : "");
		
		isExist = WebPageUtil.isStringNullAvaliable(product.getSize());
		product.setSize(isExist ? product.getSize().trim() : "");
		
		isExist = WebPageUtil.isStringNullAvaliable(product.getProductSpecId());
		product.setProductSpecId(isExist ? product.getProductSpecId().trim() : "");
		
		isExist = WebPageUtil.isStringNullAvaliable(product.getProductId());
		product.setProductId(isExist ? product.getProductId().trim() : "");
		
		isExist = WebPageUtil.isStringNullAvaliable(product.getCatena());
		product.setCatena(isExist ? product.getCatena().trim() : "");
		
		isExist = WebPageUtil.isStringNullAvaliable(product.getDisplay());
		product.setDisplay(isExist ? product.getDisplay().trim() : "");
		
		isExist = WebPageUtil.isStringNullAvaliable(product.getRatio());
		product.setRatio(isExist ? product.getRatio().trim() : "");
		
		isExist = WebPageUtil.isStringNullAvaliable(product.getOs());
		product.setOs(isExist ? product.getOs().trim() : "");
		
		isExist = WebPageUtil.isStringNullAvaliable(product.getDescription());
		product.setDescription(isExist ? product.getDescription().trim() : "");
		
		return product;
	};
	
	public String validationACImport(List<Product> products){
		StringBuffer msg = new StringBuffer();
		try {
			ActionContext context = ActionContext.getContext();
			HttpServletRequest request = (HttpServletRequest) context.get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
			ServletContext sc = request.getSession().getServletContext();
			ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
			IProductService productService = (IProductService) ac.getBean("productService");
				
			//1.�ж� excel ���Ƿ����ظ���Ʒ�ͺ�"importProduct"
			msg = productIsRepeat(products);
			
			//2.ͨ�����ݿ�У�� �Ƿ��ظ�,����У�鲿�������Ƿ����
			if(msg.length() <= 0){
				for (int i = 0; i < products.size(); i++) {
					Product product = products.get(i);
					
					if(!"".equals(product.getModelName()) && product.getModelName() != null){
						product.setModelName(product.getModelName().toUpperCase());
					}
					product.setPartyId(product.getModelName().trim());
					
					/*产品型号是否存在*/
					Product pb = productService.searchProductById(product);
					if(pb != null){
						msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt02"));
					}
					
					/*AC尺寸是否存在*/
					boolean isModelExist = productService.selectWhetherTheDimensionsExist(product.getSize().trim()) < 0 ? true : false;
					if(isModelExist){
						msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt06"));
					};
					
					/*AC分类是否存在*/
					isModelExist = productService.selectTheSeriesWillExist(product.getProductType().trim()) < 0 ? true : false;
					if(isModelExist){
						msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt04"));
					};
					
					/*AC系列是否存在*/
					isModelExist = productService.selectDoesTheTypeExist(product.getCatena().trim()) < 0 ? true : false;
					if(isModelExist){
						msg.append(getText("product.error.firstStr")+(i+2)+getText("product.error.prompt14"));
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
	
	
	private StringBuffer productIsRepeat(List<Product> productList){
		StringBuffer msg = new StringBuffer();
		List<String> repeatList = new ArrayList<String>();
		for (int i = 0; i < productList.size(); i++) {
			Product bean = productList.get(i);
			String pid = "";
			if(!"".equals(bean.getModelName()) && bean.getModelName() != null){
				pid = bean.getModelName().toUpperCase();
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
