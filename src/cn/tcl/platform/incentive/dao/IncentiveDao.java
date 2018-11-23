package cn.tcl.platform.incentive.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.incentive.vo.Incentive;
import cn.tcl.platform.modelmap.vo.ModelMap;

public interface IncentiveDao {
	
	/**
	 * 查询所有提成列表
	 * @param start
	 * @param limit
	 * @param searchStr
	 * @param order
	 * @param sort
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public List<Incentive> selectIncentiveList(
			@Param("start") int start,
			@Param("limit") int limit,
			@Param("searchStr") String searchStr,
			@Param("conditions") String conditions,
			@Param("order") String order,
			@Param("sort") String sort
			) throws Exception;
	
	/**
	 * 查询所有提成数量
	 * @param start
	 * @param limit
	 * @param searchStr
	 * @param order
	 * @param sort
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public int countIncentive(
			@Param("start") int start,
			@Param("limit") int limit,
			@Param("searchStr") String searchStr,
			@Param("conditions") String conditions,
			@Param("order") String order,
			@Param("sort") String sort
			) throws Exception;
	
	/**
	 * 添加提成
	 * @param incen
	 * @throws Exception
	 */
	public void addIncentive(Incentive incen) throws Exception;
	
	/**
	 * 删除提成
	 * @param incen
	 * @throws Exception
	 */
	public void deleteIncentive(Incentive incen) throws Exception;
	
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	public  Incentive queryIncentive (String id); 
	
	
	/**
	 * 修改提成
	 * @param incen
	 * @throws Exception
	 */
	public void updateIncentive(Incentive incen) throws Exception;
	/**
	 * 根据导入提成模块的国家，获取所在的partyId
	 * @param incen
	 * @return
	 * @throws Exception
	 */
	public String getPartyIdByCountryName(Incentive incen) throws Exception;
	
	/**
	 * 根据条件获取当月的提成数
	 * @param partyId
	 * @param branchModel
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public int countIncentiveByCondition(@Param("partyId")String partyId,
				@Param("branchModel")String branchModel,@Param("date")String date) throws Exception;
	
	/**
	 * 根据导入条件修改提成
	 * @param incen
	 * @throws Exception
	 */
	public void updateIncentiveByCondition(Incentive incen) throws Exception;
	
	/**
	 * 查找该型号的总数
	 * @param branchModel
	 * @param countryId
	 * @return
	 * @throws Exception
	 */
	public int countBranchModel(@Param("branchModel")String branchModel,@Param("countryId")String countryId) throws Exception;
	
	/**
	 * excel添加数据
	 * @param incen
	 * @throws Exception
	 */
	public void addExcelIncentive(Incentive incen) throws Exception;
	
	/**
	 * 根据国家查找分公司型号
	 * @param countryId
	 * @throws Exception
	 */
	public List<ModelMap> selectBranchModel(String countryId) throws Exception;
}
