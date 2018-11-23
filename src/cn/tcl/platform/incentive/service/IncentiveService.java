package cn.tcl.platform.incentive.service;

import java.util.List;
import java.util.Map;

import cn.tcl.platform.incentive.vo.Incentive;
import cn.tcl.platform.modelmap.vo.ModelMap;

public interface IncentiveService {
	//查询提成列表
	public Map<String, Object> selectIncentiveList(int start,int limit,String searchStr,String conditions,String order,String sort) throws Exception;
	
	//添加提成
	public void addIncentive(Incentive incen) throws Exception;
	
	//删除提成
	public void deleteIncentive(Incentive incen) throws Exception;
	
	//根据id查询
	public Incentive queryIncentive (String id) throws Exception;

	//修改提成
	public void updateIncentive(Incentive id) throws Exception;
	
	//导入提成
	public void importIncentive(List<Incentive> incen) throws Exception;
	
	//根据excel提成国家获取partyId
	public String getPartyIdByCountryName(Incentive incen) throws Exception;
	
	//计算填写的分公司型号数量
	public int countBranchModel(String branchModel,String countryId) throws Exception;
	
	
	public int countIncentiveByCondition(String partyId,String branchModel,String date) throws Exception;
	
	public void updateIncentiveByCondition(Incentive incen) throws Exception;
	
	public List<ModelMap> selectBranchModel(String countryId) throws Exception;
}
