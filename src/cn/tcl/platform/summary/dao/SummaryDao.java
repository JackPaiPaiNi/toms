package cn.tcl.platform.summary.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.message.vo.Message;
import cn.tcl.platform.summary.vo.Summary;

public interface SummaryDao {
	/**
	 * 创建计划与总结
	 * @param sum
	 * @throws Exception
	 */
	public void insetrSummary(Summary sum) throws Exception;
	
	/**
	 * 查询计划与总结列表
	 * @param start
	 * @param limit
	 * @param searchStr
	 * @param typeId
	 * @param MessagetypeId
	 * @param order
	 * @param sort
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public List<Summary> selectSummaryList(
			@Param("start") int start,
			@Param("limit") int limit,
			@Param("searchStr") String searchStr,
			@Param("partyId") String partyId,
			@Param("typeId") String typeId,
			@Param("MessagetypeId") String MessagetypeId,
			@Param("order") String order,
			@Param("sort") String sort,
			@Param("conditions") String conditions
			) throws Exception;

	/**
	 * 统计计划与总结条数
	 * @param start
	 * @param limit
	 * @param searchStr
	 * @param typeId
	 * @param MessagetypeId
	 * @param order
	 * @param sort
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public int countSummary(
			@Param("start") int start,
			@Param("limit") int limit,
			@Param("searchStr") String searchStr,
			@Param("partyId") String partyId,
			@Param("typeId") String typeId,
			@Param("MessagetypeId") String MessagetypeId,
			@Param("order") String order,
			@Param("sort") String sort,
			@Param("conditions") String conditions
			) throws Exception;
	
	/**
	 * 根据summaryId获取
	 * @param summaryId
	 * @return
	 * @throws Exception
	 */
	public Summary getSummaryById(String summaryId) throws Exception;
	/**
	 * 编辑计划与总结
	 * @param sum
	 * @throws Exception
	 */
	public void updateSummary(Summary sum) throws Exception;
	
	/**
	 * 根据计划与总结的summaryId删除
	 * @param summaryId
	 * @throws Exception
	 */
	public void deleteSummary(Summary summaryId) throws Exception;
}
