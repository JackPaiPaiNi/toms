package cn.tcl.platform.acproduct.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.acproduct.vo.ACProduct;

public interface IACProductService {
	//查产品列表
	public Map<String, Object> selectProductsData(int start,int  limit,String keyword,String order,String sort,String conditions/*, String fatherId, String sonId, String catId*/) throws Exception;
	//保存添加新产品
	public void saveProduct(ACProduct product) throws Exception;
	//查找某个产品
	public ACProduct getProduct(String pid) throws Exception;
	//编辑修改产品
	public void editProduct(ACProduct product) throws Exception;
	//导入产品
	public void importProduct(List<ACProduct> products) throws Exception;
	//查询产品编码是否存在
	public int searchCountByProductId(String productId) throws Exception;
	//删除
	public void deleteProductByProductId(ACProduct product) throws Exception;
	
	public List<ACProduct> getHQProductParm() throws Exception;
	
	public int searchPartyByName(ACProduct product) throws Exception;
	
	public ACProduct searchProductById(ACProduct product) throws Exception;
	
	public int searchParamByNameType(String paramValue,String paramDomain,String paramType) throws Exception;
	
	//	根据上级查询下级类别
	public List<ACProduct> selectCategoryByFatherId() throws Exception;
	
	public List<ACProduct> selectCategoryByFatherIdTWo(String fatherId) throws Exception;
	//查询型号
	public Integer selectModel (Map<String,Object> map)throws Exception;
	//查询系列
	public List<ACProduct> selectLine () throws Exception;
	
	
	public List<ACProduct> selectACAllSize () throws Exception;
	
	public int selectWhetherTheDimensionsExist (@Param(value="size") String size) throws Exception;
	
	public int selectWhetherTheModelExists (@Param(value="model") String model) throws Exception;
	
}
