package cn.tcl.platform.performance.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.platform.performance.dao.PolicyDao;
import cn.tcl.platform.performance.service.PolicyService;
import cn.tcl.platform.performance.vo.Policy;
import cn.tcl.platform.product.vo.Product;


@Service("PolicyService")
public class PolicyServiceImpl implements PolicyService{
	@Autowired
	private PolicyDao policyDao;
	
	@Override
	public Map<String, Object> selectPolicyList(int start, int limit,
			String searchStr, String conditions, String order, String sort)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Policy> list = policyDao.selectPolicyList(start, limit, searchStr, conditions, order, sort);
		int count = policyDao.count(start, limit, searchStr, conditions, order, sort);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}

	@Override
	public void addPolicy(Policy po) throws Exception {	
		policyDao.addPolicy(po);
		
	}

	@Override
	public Double selectExchange(String countryId) throws Exception {
		return policyDao.selectExchange(countryId);
	}

	@Override
	public void updatePolicy(Policy po) throws Exception {
			policyDao.updatePolicy(po);
		
	}

	@Override
	public Policy selectPolicy(String id) throws Exception {
		return policyDao.selectPolicy(id);
	}

	@Override
	public List<Product> selectProductLine() throws Exception {
		return policyDao.selectProductLine();
	}

	@Override
	public void addPolicyByProduct(Policy po) throws Exception {
			policyDao.addPolicyByProduct(po);
		
	}

	@Override
	public Map<String, Object> selectPolicyListByProduct(int start, int limit,
			String searchStr, String conditions, String order, String sort)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Policy> list = policyDao.selectPolicyListByProduct(start, limit, searchStr, conditions, order, sort);
		int count = policyDao.countByProduct(start, limit, searchStr, conditions, order, sort);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}

	@Override
	public void updatePolicyByProduct(Policy po) throws Exception {
			policyDao.updatePolicyByProduct(po);
	}

}
