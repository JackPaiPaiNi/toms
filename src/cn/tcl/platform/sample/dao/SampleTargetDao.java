package cn.tcl.platform.sample.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.excel.vo.ImportExcel;
import cn.tcl.platform.sample.vo.SampleTarget;
import cn.tcl.platform.shop.vo.ShopParty;

public interface SampleTargetDao {
	
	
	public List<SampleTarget> selectSampleTargetList(Map<String,Object> sampleMap) throws Exception;
	public Integer selectSampleTargetListCount(Map<String,Object> sampleMap) throws Exception;
	
	public int selectSampleTargetsCount(
			@Param(value = "beginDate") String beginDate,
			@Param(value = "endDate") String endDate,
			@Param(value = "shopId") String shopId,
			@Param(value = "model") String model) throws Exception;
	
	public int updateSampleTarget(List<SampleTarget > reddemCodeList) throws Exception;

	public int saveSampleTarget(List<SampleTarget > reddemCodeList) throws Exception;
	public List<SampleTarget> selectShopByParty(@Param(value = "countryId")String countryId,
			@Param(value = "partyId") String partyId) throws Exception;
	public List<SampleTarget> selectModelByCountry(@Param(value = "countryId")String countryId) throws Exception;
	
	public int updateSampleTargetById(SampleTarget sampleTarget) throws Exception;
	public int deleteSampleTarget(int id) throws Exception;
	
	
	public List<SampleTarget> selectSampleAchList(Map<String,Object> sampleMap) throws Exception;
	public Integer selectSampleAchListCount(Map<String,Object> sampleMap) throws Exception;
	
	public List<SampleTarget> selectSampleAchListByLine(Map<String,Object> sampleMap) throws Exception;
	public Integer selectSampleAchListCountByLine(Map<String,Object> sampleMap) throws Exception;
	
	public List<SampleTarget> selectSampleTargetListByLine(Map<String,Object> sampleMap) throws Exception;
	public Integer selectSampleTargetListCountByLine(Map<String,Object> sampleMap) throws Exception;
	
	
	
	
	
	
	
	public List<SampleTarget> selectSampleTargetSumListByLine(Map<String,Object> sampleMap) throws Exception;
	public Integer selectSampleTargetSumListCountByLine(Map<String,Object> sampleMap) throws Exception;
	
	
	public List<SampleTarget> selectSampleAchSumListByLine(Map<String,Object> sampleMap) throws Exception;
	public Integer selectSampleAchSumListCountByLine(Map<String,Object> sampleMap) throws Exception;
	



	public List<HashMap<String, Object>>  selectModel(
			@Param(value="searchStr") String searchStr,
			@Param(value="conditions") String conditions
			) throws Exception;
	


}
