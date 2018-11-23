package cn.tcl.platform.product.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.party.dao.IPartyDAO;
import cn.tcl.platform.party.vo.Party;
import cn.tcl.platform.product.dao.IProductDao;
import cn.tcl.platform.product.service.IProductService;
import cn.tcl.platform.product.vo.Product;
@Service("productService")
public class IProductServiceImpl implements IProductService {
	@Autowired
	private IProductDao productDao;
	@Autowired
	private IPartyDAO partyDao;
	
	@Override
	public Map<String, Object> selectProductsData(int start, int limit, String keyword, String order, String sort,String conditions,
			 String fatherId, String sonId, String catId)
			throws Exception {
		
		List<Product> list=productDao.selectProducts(start, limit, keyword, order, sort,conditions,fatherId, sonId,  catId);
		Map<String, Object> map=new HashMap<String, Object>();
		int count=productDao.countProducts(start, limit, keyword,conditions,fatherId, sonId,  catId);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}

	@Override
	public void saveProduct(Product product) throws Exception {
		productDao.saveProduct(product);
	}

	@Override
	public Product getProduct(String pid) throws Exception {
		return productDao.getProduct(pid);
	}

	@Override
	public void editProduct(Product product) throws Exception {
		productDao.editProduct(product);
	}

	@Override
	public void importProduct(List<Product> products) throws Exception {
		if(products!=null){
			for (Product product : products) {
				//处理表的关联关系
				String partyName=product.getPartyId();//导入时实际上输入的是机构的名字，这里根据名字找到对应的ID
				Party party=partyDao.getOnePartyByName(partyName.trim());
				product.setPartyId(party==null?null:party.getPartyId());
				product.setStatus(true);
				product.setProductType(product.getProductTypeId());
				product.setProductId(WebPageUtil.isStringNullAvaliable(product.getProductId())? product.getProductId().toUpperCase() : "");
				product.setHeadType(Contents.TV);
				productDao.saveProduct(product);
			}
		}
	}
	
	@Override
	public void acImportProduct(List<Product> products) throws Exception {
		if(products!=null){
			for (Product product : products) {
				//处理表的关联关系
				product.setStatus(true);
				product.setGasType(getChangesInTemperatureNum(product.getGasType()));
				product.setHeadType(Contents.AC);
				productDao.saveProduct(product);
			}
		}
	}
	
	public String getChangesInTemperatureNum(String string){
		String numb = "";
		if(WebPageUtil.isStringNullAvaliable(string)){
			String isNotNull = string.trim().toLowerCase();
			numb = "cooling".equals(isNotNull) ? "1" : "2";
		};
		return numb;
	};

	@Override
	public int searchCountByProductId(String productId) throws Exception {
		return productDao.searchCountByProductId(productId);
	}

	@Override
	public void deleteProductByProductId(Product product) throws Exception {
		productDao.deleteProductByProductId(product);
		
	}

	@Override
	public List<Product> getHQProductParm() throws Exception {
//		String conditions = "and p.party_id = '999'";
		String conditions = "1=1";
		return productDao.getHQProductParm(conditions);
	}

	@Override
	public int searchPartyByName(Product product) throws Exception {
		return productDao.searchPartyByName(product);
	}

	@Override
	public Product searchProductById(Product product) throws Exception {
		return productDao.searchProductById(product);
	}

	@Override
	public int searchParamByNameType(String paramValue, String paramDomain,
			String paramType) throws Exception {
		return productDao.searchParamByNameType(paramValue,paramDomain,paramType);
	}
	
	@Override
	public List<Product> selectCategoryByFatherId() throws Exception {
		return productDao.selectCategoryByFatherId();
	}

	@Override
	public List<Product> selectCategoryByFatherIdTWo(String fatherId) throws Exception {
		return productDao.selectCategoryByFatherIdTWo(fatherId);
	}
	
	@Override
	public Integer selectModel(Map<String, Object> map) throws Exception {
		return productDao.selectModel(map);
	}

	@Override
	public List<Product> selectLine() throws Exception {
		return productDao.selectLine();
	}

	@Override
	public List<Product> selectACAllSize() throws Exception {
		return productDao.selectACAllSize();
	}

	@Override
	public int selectWhetherTheDimensionsExist(String size) throws Exception {
		return productDao.selectWhetherTheDimensionsExist(size);
	}

	@Override
	public List<Product> selectACAllClassification() throws Exception {
		return productDao.selectACAllClassification();
	}

	@Override
	public List<Product> selectACCatena() throws Exception {
		return productDao.selectACCatena();
	}

	@Override
	public int selectTheSeriesWillExist(String classification) throws Exception {
		return productDao.selectTheSeriesWillExist(classification);
	}

	@Override
	public int selectDoesTheTypeExist(String catena) throws Exception {
		return productDao.selectTheSeriesWillExist(catena);
	}

	
}
