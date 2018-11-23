package cn.tcl.platform.incentive.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.platform.incentive.dao.IncentiveDao;
import cn.tcl.platform.incentive.service.IncentiveService;
import cn.tcl.platform.incentive.vo.Incentive;
import cn.tcl.platform.modelmap.vo.ModelMap;


@Service("percentageService")
public class IncentiveServiceImpl implements IncentiveService{
	
	@Autowired
	private IncentiveDao percentageDao;

	@Override
	public Map<String, Object> selectIncentiveList(int start, int limit,
			String searchStr,String conditions, String order, String sort)
			throws Exception {
		List<Incentive> list = percentageDao.selectIncentiveList(start, limit,searchStr, conditions, order, sort);
		Map<String, Object> map =new HashMap<String, Object>();
		int count= percentageDao.countIncentive(start, limit,searchStr, conditions, order, sort);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}

	@Override
	public void addIncentive(Incentive incen) throws Exception {
		percentageDao.addIncentive(incen);
	}

	@Override
	public void deleteIncentive(Incentive incen) throws Exception {
		percentageDao.deleteIncentive(incen);
	}

	@Override
	public void updateIncentive(Incentive incen) throws Exception {
			percentageDao.updateIncentive(incen);
		
	}

	@Override
	public Incentive queryIncentive(String id) throws Exception {
		return percentageDao.queryIncentive(id);
	}

	@Override
	public void importIncentive(List<Incentive> incen) throws Exception {
		if(!"".equals(incen) && incen!=null){
			for (Incentive incentive : incen) {
				
				System.out.println(incentive.getPartyId()+"---------------------------");
				//导入时实际获取的时国家名字
				String partyId = percentageDao.getPartyIdByCountryName(incentive);
				int count;
				if(partyId!=null  && incentive.getBranchModel()!=null && incentive.getDate()!=null){
					 count = percentageDao.countIncentiveByCondition(partyId, incentive.getBranchModel(), incentive.getDate());
					 if(count>0){
						 incentive.setBranchModel(incentive.getBranchModel().toUpperCase());
							incentive.setPartyId(partyId);
//							incentive.setQuantity(incentive.getQuantity());
							incentive.setRetailPrice(incentive.getRetailPrice());
							incentive.setSize(incentive.getSize());
							incentive.setIncentive(incentive.getIncentive());
				//			incentive.setFlag("0");
							
							percentageDao.updateIncentiveByCondition(incentive);
						}else{
							incentive.setBranchModel(incentive.getBranchModel().toUpperCase());
							incentive.setPartyId(partyId);
//							incentive.setQuantity(incentive.getQuantity());
							incentive.setRetailPrice(incentive.getRetailPrice());
							incentive.setSize(incentive.getSize());
							incentive.setIncentive(incentive.getIncentive());
							
							percentageDao.addExcelIncentive(incentive);
						}
				}
				
			}
		}
	}

	@Override
	public String getPartyIdByCountryName(Incentive incen) throws Exception {
		return percentageDao.getPartyIdByCountryName(incen);
	}

	@Override
	public int countBranchModel(String branchModel,String countryId) throws Exception {
		return percentageDao.countBranchModel(branchModel,countryId);
	}

	@Override
	public int countIncentiveByCondition(String partyId, String branchModel,
			String date) throws Exception {
		return percentageDao.countIncentiveByCondition(partyId, branchModel, date);
	}

	@Override
	public void updateIncentiveByCondition(Incentive incen) throws Exception {
			percentageDao.updateIncentiveByCondition(incen);
		
	}

	@Override
	public List<ModelMap> selectBranchModel(String countryId) throws Exception {	
		return percentageDao.selectBranchModel(countryId);
		
	}

}
