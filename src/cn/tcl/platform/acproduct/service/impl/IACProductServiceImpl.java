package cn.tcl.platform.acproduct.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.platform.acproduct.dao.IACProductDao;
import cn.tcl.platform.acproduct.service.IACProductService;
import cn.tcl.platform.acproduct.vo.ACProduct;
@Service("acproductService")
public class IACProductServiceImpl implements IACProductService {
	@Autowired
	private IACProductDao productDao;
	
	@Override
	public Map<String, Object> selectProductsData(int start, int limit, String keyword, String order, String sort,String conditions
			/*,String fatherId, String sonId, String catId*/)
			throws Exception {
		
		List<ACProduct> list=productDao.selectProducts(start, limit, keyword, order, sort,conditions/*,fatherId, sonId,  catId*/);
		Map<String, Object> map=new HashMap<String, Object>();
		int count=productDao.countProducts(start, limit, keyword,conditions/*,fatherId, sonId,  catId*/);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}

	@Override
	public void saveProduct(ACProduct product) throws Exception {
		productDao.saveProduct(product);
	}

	@Override
	public ACProduct getProduct(String pid) throws Exception {
		return productDao.getProduct(pid);
	}

	@Override
	public void editProduct(ACProduct product) throws Exception {
		productDao.editProduct(product);
	}

	@Override
	public void importProduct(List<ACProduct> products) throws Exception {
		if(products!=null){
			for (ACProduct product : products) {
				/*批量迭代增加*/
				productDao.saveProduct(product);
			}
		}
	}

	@Override
	public int searchCountByProductId(String productId) throws Exception {
		return productDao.searchCountByProductId(productId);
	}

	@Override
	public void deleteProductByProductId(ACProduct product) throws Exception {
		productDao.deleteProductByProductId(product);
		
	}

	@Override
	public List<ACProduct> getHQProductParm() throws Exception {
		String conditions = "and p.party_id = '999'";
		return productDao.getHQProductParm(conditions);
	}

	@Override
	public int searchPartyByName(ACProduct product) throws Exception {
		return productDao.searchPartyByName(product);
	}

	@Override
	public ACProduct searchProductById(ACProduct product) throws Exception {
		return productDao.searchProductById(product);
	}

	@Override
	public int searchParamByNameType(String paramValue, String paramDomain,
			String paramType) throws Exception {
		return productDao.searchParamByNameType(paramValue,paramDomain,paramType);
	}
	
	@Override
	public List<ACProduct> selectCategoryByFatherId() throws Exception {
		return productDao.selectCategoryByFatherId();
	}

	@Override
	public List<ACProduct> selectCategoryByFatherIdTWo(String fatherId) throws Exception {
		return productDao.selectCategoryByFatherIdTWo(fatherId);
	}
	
	@Override
	public Integer selectModel(Map<String, Object> map) throws Exception {
		return productDao.selectModel(map);
	}

	@Override
	public List<ACProduct> selectLine() throws Exception {
		return productDao.selectLine();
	}
	
	

	@Override
	public List<ACProduct> selectACAllSize() throws Exception {
		return productDao.selectACAllSize();
	}

	@Override
	public int selectWhetherTheDimensionsExist(String size) throws Exception {
		return productDao.selectWhetherTheDimensionsExist(size);
	}

	@Override
	public int selectWhetherTheModelExists(String model) throws Exception {
		return productDao.selectWhetherTheModelExists(model);
	}
}
