package cn.tcl.platform.acproduct.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.acproduct.vo.ACProduct;

public interface IACProductDao {
	//选择产品
	public List<ACProduct> selectProducts(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="keyword") String keyword,
			@Param(value="order") String order,
			@Param(value="sort") String sort,
			@Param(value="conditions") String conditions
			/*, 
			@Param(value="fatherId") String fatherId, 
			@Param(value="sonId") String sonId, 
			@Param(value="catId") String catId*/
			) throws Exception;
	//总数计算
	public int countProducts(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="keyword") String keyword,
			@Param(value="conditions") String conditions/*, 
			@Param(value="fatherId") String fatherId, 
			@Param(value="sonId") String sonId, 
			@Param(value="catId") String catId*/) throws Exception;
	
	//保存添加新产品
	public void saveProduct(ACProduct product) throws Exception;
	//查找某个产品
	public ACProduct getProduct(String pid) throws Exception;
	//编辑修改产品
	public void editProduct(ACProduct product) throws Exception;
	
	//查询产品编码是否存在
	public int searchCountByProductId(String productId) throws Exception;
	//删除
	public void deleteProductByProductId(ACProduct product)throws Exception;
	
	public List<ACProduct> getHQProductParm(@Param(value="conditions") String conditions) throws Exception;
	
	public int searchPartyByName(ACProduct product) throws Exception;
	
	public ACProduct searchProductById(ACProduct product) throws Exception;
	
	public int searchParamByNameType(@Param(value="paramValue") String paramValue,
			@Param(value="paramDomain") String paramDomain,
			@Param(value="paramType") String paramType) throws Exception;
			
	public List<ACProduct> selectCategoryByFatherId() throws Exception;
	
	public List<ACProduct> selectCategoryByFatherIdTWo(String fatherId) throws Exception;
	
	public Integer selectModel (Map<String,Object> map)throws Exception;
	
	public List<ACProduct> selectLine () throws Exception;
	
	
	
	public List<ACProduct> selectACAllSize () throws Exception;
	
	public int selectWhetherTheDimensionsExist (@Param(value="size") String size) throws Exception;
	
	public int selectWhetherTheModelExists (@Param(value="model") String model) throws Exception;
}
