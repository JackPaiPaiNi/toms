package cn.tcl.platform.product.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.product.vo.Product;

public interface IProductService {
	//查产品列表
	public Map<String, Object> selectProductsData(int start,int  limit,String keyword,String order,String sort,String conditions, String fatherId, String sonId, String catId) throws Exception;
	//保存添加新产品
	public void saveProduct(Product product) throws Exception;
	//查找某个产品
	public Product getProduct(String pid) throws Exception;
	//编辑修改产品
	public void editProduct(Product product) throws Exception;
	//导入产品
	public void importProduct(List<Product> products) throws Exception;
	
	public void acImportProduct(List<Product> products) throws Exception;
	//查询产品编码是否存在
	public int searchCountByProductId(String productId) throws Exception;
	//删除
	public void deleteProductByProductId(Product product) throws Exception;
	
	public List<Product> getHQProductParm() throws Exception;
	
	public int searchPartyByName(Product product) throws Exception;
	
	public Product searchProductById(Product product) throws Exception;
	
	public int searchParamByNameType(String paramValue,String paramDomain,String paramType) throws Exception;
	
	//	根据上级查询下级类别
	public List<Product> selectCategoryByFatherId() throws Exception;
	
	public List<Product> selectCategoryByFatherIdTWo(String fatherId) throws Exception;
	//查询型号
	public Integer selectModel (Map<String,Object> map)throws Exception;
	//查询系列
	public List<Product> selectLine () throws Exception;
	
	public List<Product> selectACAllSize () throws Exception;
	
	public List<Product> selectACAllClassification () throws Exception;
	
	public List<Product> selectACCatena () throws Exception;
	
	public int selectWhetherTheDimensionsExist (@Param(value="size") String size) throws Exception;
	
	public int selectTheSeriesWillExist (@Param(value="classification") String classification) throws Exception;
	
	public int selectDoesTheTypeExist (@Param(value="catena") String catena) throws Exception;
	
}
