package cn.tcl.platform.modelmap.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.tcl.platform.modelmap.vo.ModelMap;


public interface IModelMapService {

	public Map<String,Object> selectModelMapData(int start,int  limit,String keyword,String order,String sort,String conditions) throws Exception;

	public void editModelMap(ModelMap modelMap) throws Exception;
	
	public void addModelMap(ModelMap modelMap) throws Exception;
	
	public void deleteModelMap(ModelMap modelMap) throws Exception;
	
	public ModelMap getModelMapById(String modelId) throws Exception;
	
	public void importModelMap(List<ModelMap> modelmaps) throws Exception;
	
	public void importModelPrice(List<ModelMap> modelmaps) throws Exception;
	
	public String searchBeanVerify(ModelMap modelMap) throws Exception;
	
	public int getCountModelMapByBranch(ModelMap modelMap) throws Exception;
	
	public int searchHqModelMapCount(ModelMap modelMap) throws Exception;
	
	public int searchCountryByName(ModelMap modelMap) throws Exception;
	
	public int getModelIdByParty(String conditions,String model,String partyId) throws Exception;
	
	public int bSearchBeanLimit(ModelMap modelMap) throws Exception;
	
	public String searchPartyIdByName(ModelMap modelMap) throws Exception;
		
	public XSSFWorkbook exporModelPrice(String conditions,String[] excelHeader,String title) throws Exception;
	
	/**
	 * 渠道型号与分公司型号的关系
	 */
	public Map<String, Object> selectChannelModelMap(Map<String,Object> m) throws Exception;
	
//	public void deleteChannelModel(String id) throws Exception;
	public void deleteChannelModel(ModelMap modelMap) throws Exception;
	
	public List<ModelMap> selectChennalModelByPartyId(@Param(value="partyId") String partyId) throws Exception;
	
	public void updateChannelModelById(ModelMap modelMap) throws Exception;
	
	public void insertChannelModel(ModelMap modelMap) throws Exception;
	
	public void importChannelModelMap(List<ModelMap> modelmaps) throws Exception ;
	
	public String channelModelIsBeing( String customerId,String channelModel,String branchModel,String condition) throws Exception;
	
	public int branchModelIsBeing(ModelMap modelMap) throws Exception;

	public int searchChannelByName(String partyId, String customerName)  throws Exception;
	
	public int searchChannelByNameCount(String partyId, String customerName)  throws Exception;
	
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
	public int selectSaleMappingBybranchModel( String branchModel) throws Exception;
}
