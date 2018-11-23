package cn.tcl.platform.exchange.service;


import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.exchange.vo.Exchange;
import cn.tcl.platform.party.vo.Party;

public interface IExchangeService {
	/**
	 * 
	 * @param searchMsg 查询条件
	 * @param startRow 开始行索引
	 * @param pageSize 每页显示条数
	 * @param order 排序方式
	 * @param sort 排序字段
	 * @return
	 * @throws Exception
	 */
//	public Map<String, Object> selectExchange(String searchMsg, String startRow, String pageSize, String order, String sort,String selectQuertyPartyId) throws Exception;
	public Map<String,Object> selectExchange(int start,int  limit,String keyword,String order,String sort,String conditions,String selectQuertyPartyId) throws Exception;
	public void saveExchange(Exchange exchange) throws Exception;
	
	/*public Party getCountryName(String countryName) throws Exception; */
	
	public void updateByExchange(Exchange exchange) throws Exception;
	
	public Exchange selectExchangeById(String id) throws Exception;
	
	public void deleteExchange(Exchange exchange) throws Exception;
}
