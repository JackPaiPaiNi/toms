package cn.tcl.platform.performance.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.performance.vo.Policy;
import cn.tcl.platform.product.vo.Product;

/**
 * 
 * @author fzl
 *
 */
public interface PolicyDao {
	
	/**
	 * 奖励列表
	 * @param start
	 * @param limit
	 * @param searchStr
	 * @param conditions
	 * @param order
	 * @param sort
	 * @return
	 * @throws Exception
	 */
	public List<Policy> selectPolicyList(
			@Param("start") int start,
			@Param("limit") int limit,
			@Param("searchStr") String searchStr,
			@Param("conditions") String conditions,
			@Param("order") String order,
			@Param("sort") String sort) throws Exception;
	
	/**
	 * 奖励数量
	 * @param start
	 * @param limit
	 * @param searchStr
	 * @param conditions
	 * @param order
	 * @param sort
	 * @return
	 * @throws Exception
	 */
	public int count(
			@Param("start") int start,
			@Param("limit") int limit,
			@Param("searchStr") String searchStr,
			@Param("conditions") String conditions,
			@Param("order") String order,
			@Param("sort") String sort) throws Exception;
	
	/**
	 * 添加个人奖励
	 * @param po
	 * @throws Exception
	 */
	public void addPolicy(Policy po) throws Exception;
	
	/**
	 * 查询最近的汇率
	 * @param countryId
	 * @return
	 * @throws Exception
	 */
	public Double selectExchange(@Param("countryId")String countryId) throws Exception;
	
	/**
	 * 根据id查询绩效
	 * @return
	 * @throws Exception
	 */
	public Policy selectPolicy(String id) throws Exception;
	/**
	 * 修改奖励政策
	 * @param po
	 * @throws Exception
	 */
	public void updatePolicy(Policy po) throws Exception;
	
	/**
	 * 查询产品系列
	 * @return
	 * @throws Exception
	 */
	public List<Product>  selectProductLine() throws Exception;
	
	/**
	 * 重点奖励列表
	 * @param start
	 * @param limit
	 * @param searchStr
	 * @param conditions
	 * @param order
	 * @param sort
	 * @return
	 * @throws Exception
	 */
	public List<Policy> selectPolicyListByProduct(
			@Param("start") int start,
			@Param("limit") int limit,
			@Param("searchStr") String searchStr,
			@Param("conditions") String conditions,
			@Param("order") String order,
			@Param("sort") String sort) throws Exception;
	
	/**
	 * 重点奖励数量
	 * @param start
	 * @param limit
	 * @param searchStr
	 * @param conditions
	 * @param order
	 * @param sort
	 * @return
	 * @throws Exception
	 */
	public int countByProduct(
			@Param("start") int start,
			@Param("limit") int limit,
			@Param("searchStr") String searchStr,
			@Param("conditions") String conditions,
			@Param("order") String order,
			@Param("sort") String sort) throws Exception;
	
	
	/**
	 * 添加重点系列奖励
	 * @param po
	 * @throws Exception
	 */
	public void addPolicyByProduct(Policy po) throws Exception;
	
	/**
	 * 修改重点系列奖励
	 * @param po
	 * @throws Exception
	 */
	public void updatePolicyByProduct(Policy po) throws Exception;
}
