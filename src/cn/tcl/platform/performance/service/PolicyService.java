package cn.tcl.platform.performance.service;

import java.util.List;
import java.util.Map;

import cn.tcl.platform.performance.vo.Policy;
import cn.tcl.platform.product.vo.Product;

public interface PolicyService {
	
	
	public Map<String, Object> selectPolicyList(int start,int limit,String searchStr,String conditions,String order,String sort) throws Exception;
	
	//添加奖励政策
	public void addPolicy(Policy po) throws Exception;
	
	public Double selectExchange(String countryId) throws Exception;
	
	//根据id查询奖励
	public Policy selectPolicy(String id) throws Exception;
	//修改奖励
	public void updatePolicy(Policy po) throws Exception;
	//查询产品系列
	public List<Product>  selectProductLine() throws Exception;
	//添加重点系列奖励
	public void addPolicyByProduct(Policy po) throws Exception;
	
	public Map<String, Object> selectPolicyListByProduct(int start,int limit,String searchStr,String conditions,String order,String sort) throws Exception;
	
	/**
	 * 修改重点系列奖励
	 * @param po
	 * @throws Exception
	 */
	public void updatePolicyByProduct(Policy po) throws Exception;
}
