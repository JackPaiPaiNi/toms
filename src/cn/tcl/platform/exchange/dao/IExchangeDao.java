package cn.tcl.platform.exchange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.exchange.vo.Exchange;
import cn.tcl.platform.modelmap.vo.ModelMap;
import cn.tcl.platform.party.vo.Party;
import cn.tcl.platform.role.vo.Role;

public interface IExchangeDao {
	
	/**
	 * 列表数据
	 * @return
	 */
	public List<Exchange> selectExchange(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="keyword") String keyword,
			@Param(value="order") String order,
			@Param(value="sort") String sort,
			@Param(value="conditions") String conditions) throws Exception;
	
//	/**
//	 * 分页查询角色
//	 * @param conditions 查询条件
//	 * @param orderBy 排序条件 
//	 * @param startIndex 开始索引
//	 * @param endIndex 结束索引
//	 * @return
//	 * @throws Exception
//	 */
//	public List<Exchange> selectExchange(@Param(value="conditions") String conditions,
//										 @Param(value="orderBy") String orderBy,
//										 @Param(value="startIndex") int startIndex,
//										 @Param(value="endIndex") int endIndex) throws Exception;
	public int countExchange(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="keyword") String keyword,
			@Param(value="conditions") String conditions) throws Exception;
//	//统计所有条数
//	public int countExchange(@Param(value="conditions") String conditions) throws Exception;
	
	//保存汇率
	public void saveExchange(Exchange exchange) throws Exception;
	
	//根据国家名获取国家ID
//	public Party getCountryName(@Param("CountryName")String countryName) throws Exception; 
	
	//修改汇率
	public void updateByExchange(Exchange exchange) throws Exception;
	
	public Exchange selectExchangeById(String id) throws Exception;
	
	//删除汇率
	public void deleteExchange(Exchange exchange) throws Exception;
}
