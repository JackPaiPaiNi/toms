package cn.tcl.platform.modelmap.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.modelmap.vo.ModelMap;


public interface IModelMapDao {
	/**
	 * 列表数据
	 * @return
	 */
	public List<ModelMap> selectModelMap(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="keyword") String keyword,
			@Param(value="order") String order,
			@Param(value="sort") String sort,
			@Param(value="conditions") String conditions) throws Exception;
	
	
	public List<ModelMap> selectModelMap(@Param(value="conditions") String conditions,
			@Param(value="keyword") String keyword
			) throws Exception;
	
	/**
	 *总数
	 * @return
	 */
	public int countModelMap(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="keyword") String keyword,
			@Param(value="conditions") String conditions) throws Exception;
	
	public ModelMap getModelMapById(String modelId) throws Exception;
	
	public void editModelMap(ModelMap modelMap) throws Exception;
	
	public void editModelPrice(ModelMap modelMap) throws Exception;
	
	public void addModelMap(ModelMap modelMap) throws Exception;
	
	public void deleteModelMap(ModelMap modelMap) throws Exception;
	
	public int aSearchBeanLimit(ModelMap modelMap) throws Exception;
	
	public int bSearchBeanLimit(ModelMap modelMap) throws Exception;
	
	public int searchBeanLimit(ModelMap modelMap) throws Exception;
	
	public int searchBeanHQLimit(ModelMap modelMap) throws Exception;
	
	public int searchHqModelMapCount(ModelMap modelMap) throws Exception;
	
	public int searchCountryByName(ModelMap modelMap) throws Exception;
	
	public String searchPartyIdByName(ModelMap modelMap) throws Exception;
	
	public String getPartyIdByName(ModelMap modelMap)throws Exception;
	
	public int getModelIdByParty(@Param(value="cond") String cond,@Param(value="branchModel")String branchModel,@Param(value="partyId")String partyId)throws Exception;
	
	public List<ModelMap> getModelMapByBModel(@Param("bmodel")String model) throws Exception;
	
	/**
	 *  渠道型号与分公司型号对应关系 deleteChannelModel
	 */
	public List<ModelMap> selectChannelModelMap(Map<String,Object> m) throws Exception;
	
	public int countChannelModelMap(Map<String,Object> m) throws Exception;
	
	public int channelModelIsBeing(@Param(value="customerId") String customerId,
								@Param(value="channelModel") String channelModel,
								@Param(value="branchModel") String branchModel,
								@Param(value="condition") String condition
			) throws Exception;
	
	public int brModelIsBeing(@Param(value="customerId") String customerId,
			@Param(value="branchModel") String branchModel,
			@Param(value="condition") String condition
			) throws Exception;
	
	public int searchChannelByName(@Param(value="countryName") String countryId,
								@Param(value="customerName") String customerName
			) throws Exception;
	
	public int searchChannelByNameCount(@Param(value="countryName") String countryId,
			@Param(value="customerName") String customerName
			) throws Exception;
	
	public int branchModelIsBeing(ModelMap modelMap) throws Exception;
	
//	public void deleteChannelModel(@Param(value="id") String id) throws Exception;
	public void deleteChannelModel(ModelMap modelMap) throws Exception;

	public void updateChannelModelById(ModelMap modelMap) throws Exception;
	
	public void insertChannelModel(ModelMap modelMap) throws Exception;
	
	public List<ModelMap> selectChennalModelByPartyId(@Param(value="partyId") String partyId) throws Exception;
	
	/**
	 * 是否存在分公司、渠道且分公司型号一致的数据
	 * @param partyId
	 * @param customerId
	 * @param branchModel
	 * @return
	 * @throws Exception
	 */
	public int isPartyAndCustAndPaModelUnan(
			@Param(value="partyId") String partyId,
			@Param(value="customerId") String customerId,
			@Param(value="branchModel") String branchModel
			) throws Exception;
	
	/**
	 * 型号是否存在对应销售数据
	 * @param branchModel
	 * @return
	 * @throws Exception
	 */
	public int selectSaleMappingBybranchModel(
			@Param(value="branchModel") String branchModel
			) throws Exception;
	
}
