package cn.tcl.platform.statement.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.statement.dao.IStatementDao;
import cn.tcl.platform.statement.service.IStatementService;
import cn.tcl.platform.statement.vo.Statement;

@Service("statementService")
public class StatementServiceImpl implements IStatementService {

	@Autowired
	private IStatementDao statementDao;

	@Override
	public Statement selectCountry(String userId) throws Exception {
		return statementDao.selectCountry(userId);
	}

	@Override
	public List<Statement> selectAllCountry(String partyId) throws Exception {
		return statementDao.selectAllCountry(partyId);
	}

	@Override
	public List<Statement> selectUserSale(Map<String, Object> map) throws Exception {
		return statementDao.selectUserSale(map);
	}

	@Override
	public List<Statement> selectSalertype(Map<String, Object> map) throws Exception {
		map.put("countryList", jointPartyIdList(selectAreaByPartyId(map)));
		return statementDao.selectSalertype(map);
	}

	@Override
	public List<Statement> selecPartySalerTar(Map<String, Object> map) throws Exception {
		List<Statement> list = selectSubdomainByArea(map);
		map.put("partyIdList", jointPartyIdList(list));
		return statementDao.selecPartySalerTar(map);
	}

	@Override
	public List<Statement> selectBusinessCenter() throws Exception {
		return statementDao.selectBusinessCenter();
	}

	@Override
	public List<Statement> businessCenterByUserId(String userId) throws Exception {
		return statementDao.businessCenterByUserId(userId);
	}

	@Override
	public List<Statement> selectPartyManage(Map<String, Object> map) throws Exception {
		map.put("countryList", jointPartyIdList(selectAreaByPartyId(map)));
		return statementDao.businessManagerByParty(map);
	}

	@Override
	public List<Statement> selectSaleRank(Map<String, Object> map) throws Exception {

		List<Statement> stsList = null;
		List<Statement> staList = null;
		StringBuffer partyIdBu = new StringBuffer();

		if ("personage".equals(map.get("saleType"))) {
				
				map.put("isCountry", "true");
				map.put("partyId", map.get("country"));
				List<Statement> list = selectSubdomainByArea(map);
				map.put("countryList", jointPartyIdList(list));
			 
				stsList = new ArrayList<Statement>();
				Statement sta = new Statement();
				sta.setCountryId((String) map.get("userId"));
				sta.setCountryName((String) map.get("userName"));
				stsList.add(sta);

				staList = new ArrayList<Statement>();// 储存人员和销售数据
				collectSaleUser(staList, stsList, "1");
				collectSaleUser(staList, stsList, "2");
				collectSaleUser(staList, stsList, "3");
				collectSaleUser(staList, stsList, "4");

				partyIdBu.append("'" + map.get("userId") + "'");
				map.put("userList", partyIdBu.toString());
				if("sup".equals(map.get("isSorS"))){
					
					map.put("tDate", (String) map.get("year"));
					map.put("startTime", map.get("targYearStart"));
					map.put("endTime", map.get("targYearEnd"));
					List<Statement> managerSaleInfo = statementDao.regionalManagerSalesProportion(map);
					
					for (int i = 0; i < staList.size(); i++) {
						for (int j = 0; j < managerSaleInfo.size(); j++) {
							if(staList.get(i).getId().equals(managerSaleInfo.get(j).getUserId()) && "1".equals(staList.get(i).getQuarter())){
								staList.get(i).setVolume(managerSaleInfo.get(j).getVolume());
								staList.get(i).setTarget(managerSaleInfo.get(j).getTarget());
								break;
							}
						}
					}
					
					
					map.put("startTime", map.get("targQuarterStart"));
					map.put("endTime", map.get("targQuarterEnd"));
					map.put("tDate", (String) map.get("quarter"));
					managerSaleInfo = statementDao.regionalManagerSalesProportion(map);
					for (int i = 0; i < staList.size(); i++) {
						for (int j = 0; j < managerSaleInfo.size(); j++) {
							if(staList.get(i).getId().equals(managerSaleInfo.get(j).getUserId()) && "2".equals(staList.get(i).getQuarter())){
								staList.get(i).setVolume(managerSaleInfo.get(j).getVolume());
								staList.get(i).setTarget(managerSaleInfo.get(j).getTarget());
								break;
							}
						}
					}
					
					map.put("startTime", map.get("targMonStart"));
					map.put("endTime", map.get("targMonEnd"));
					map.put("tDate", (String) map.get("month"));
					managerSaleInfo = statementDao.regionalManagerSalesProportion(map);
					for (int i = 0; i < staList.size(); i++) {
						for (int j = 0; j < managerSaleInfo.size(); j++) {
							if(staList.get(i).getId().equals(managerSaleInfo.get(j).getUserId()) && "3".equals(staList.get(i).getQuarter())){
								staList.get(i).setVolume(managerSaleInfo.get(j).getVolume());
								staList.get(i).setTarget(managerSaleInfo.get(j).getTarget());
								break;
							}
						}
					}
					
					map.put("tDate", (String) map.get("week"));
					managerSaleInfo = statementDao.regionalManagerSalesProportion(map);
					for (int i = 0; i < staList.size(); i++) {
						for (int j = 0; j < managerSaleInfo.size(); j++) {
							if(staList.get(i).getId().equals(managerSaleInfo.get(j).getUserId()) && "4".equals(staList.get(i).getQuarter())){
								staList.get(i).setVolume(managerSaleInfo.get(j).getVolume());
								staList.get(i).setTarget(managerSaleInfo.get(j).getTarget());
								break;
							}
						}
					}
				}else{
					map.put("saleType", map.get("type"));
					
					map.put("tDate", (String) map.get("year"));
					map.put("startTime", map.get("targYearStart"));
					map.put("endTime", map.get("targYearEnd"));
					List<Statement> managerSaleInfo = statementDao.toSuperviseTheSalesmanSalesProportion(map);
					
					for (int i = 0; i < staList.size(); i++) {
						for (int j = 0; j < managerSaleInfo.size(); j++) {
							if(staList.get(i).getId().equals(managerSaleInfo.get(j).getUserId()) && "1".equals(staList.get(i).getQuarter())){
								staList.get(i).setVolume(managerSaleInfo.get(j).getVolume());
								staList.get(i).setTarget(managerSaleInfo.get(j).getTarget());
								break;
							}
						}
					}
					
					
					map.put("startTime", map.get("targQuarterStart"));
					map.put("endTime", map.get("targQuarterEnd"));
					map.put("tDate", (String) map.get("quarter"));
					managerSaleInfo = statementDao.toSuperviseTheSalesmanSalesProportion(map);
					for (int i = 0; i < staList.size(); i++) {
						for (int j = 0; j < managerSaleInfo.size(); j++) {
							if(staList.get(i).getId().equals(managerSaleInfo.get(j).getUserId()) && "2".equals(staList.get(i).getQuarter())){
								staList.get(i).setVolume(managerSaleInfo.get(j).getVolume());
								staList.get(i).setTarget(managerSaleInfo.get(j).getTarget());
								break;
							}
						}
					}
					
					map.put("startTime", map.get("targMonStart"));
					map.put("endTime", map.get("targMonEnd"));
					map.put("tDate", (String) map.get("month"));
					managerSaleInfo = statementDao.toSuperviseTheSalesmanSalesProportion(map);
					for (int i = 0; i < staList.size(); i++) {
						for (int j = 0; j < managerSaleInfo.size(); j++) {
							if(staList.get(i).getId().equals(managerSaleInfo.get(j).getUserId()) && "3".equals(staList.get(i).getQuarter())){
								staList.get(i).setVolume(managerSaleInfo.get(j).getVolume());
								staList.get(i).setTarget(managerSaleInfo.get(j).getTarget());
								break;
							}
						}
					}
					
					map.put("tDate", (String) map.get("week"));
					managerSaleInfo = statementDao.toSuperviseTheSalesmanSalesProportion(map);
					for (int i = 0; i < staList.size(); i++) {
						for (int j = 0; j < managerSaleInfo.size(); j++) {
							if(staList.get(i).getId().equals(managerSaleInfo.get(j).getUserId()) && "4".equals(staList.get(i).getQuarter())){
								staList.get(i).setVolume(managerSaleInfo.get(j).getVolume());
								staList.get(i).setTarget(managerSaleInfo.get(j).getTarget());
								break;
							}
						}
					}
			    }
			
		} else {
			
			List<Statement> list = selectSubdomainByArea(map);
			map.put("countryList", jointPartyIdList(list));

			if (!"".equals(map.get("saleType")) && map.get("saleType") != null && "-1".equals(map.get("saleType"))) {
				stsList = new ArrayList<Statement>();
				map.put("tDate", (String) map.get("year"));
				map.put("startTime", map.get("targYearStart"));
				map.put("endTime", map.get("targYearEnd"));
				List<Statement> managerSaleInfo = statementDao.regionalManagerSalesProportion(map);
				
				for (int i = 0; i < managerSaleInfo.size(); i++) {
					Statement s = new Statement();
					s.setCountryId(managerSaleInfo.get(i).getUserId());
					s.setCountryName(managerSaleInfo.get(i).getUserName());
					stsList.add(s);
				}
				
				staList = new ArrayList<Statement>();// 储存人员和销售数据
				collectSaleUser(staList, stsList, "1");
				collectSaleUser(staList, stsList, "2");
				collectSaleUser(staList, stsList, "3");
				collectSaleUser(staList, stsList, "4");
				
				for (int i = 0; i < staList.size(); i++) {
					for (int j = 0; j < managerSaleInfo.size(); j++) {
						if(staList.get(i).getId().equals(managerSaleInfo.get(j).getUserId()) && "1".equals(staList.get(i).getQuarter())){
							staList.get(i).setVolume(managerSaleInfo.get(j).getVolume());
							staList.get(i).setTarget(managerSaleInfo.get(j).getTarget());
							break;
						}
					}
				}
				
				
				map.put("startTime", map.get("targQuarterStart"));
				map.put("endTime", map.get("targQuarterEnd"));
				map.put("tDate", (String) map.get("quarter"));
				managerSaleInfo = statementDao.regionalManagerSalesProportion(map);
				for (int i = 0; i < staList.size(); i++) {
					for (int j = 0; j < managerSaleInfo.size(); j++) {
						if(staList.get(i).getId().equals(managerSaleInfo.get(j).getUserId()) && "2".equals(staList.get(i).getQuarter())){
							staList.get(i).setVolume(managerSaleInfo.get(j).getVolume());
							staList.get(i).setTarget(managerSaleInfo.get(j).getTarget());
							break;
						}
					}
				}
				
				map.put("startTime", map.get("targMonStart"));
				map.put("endTime", map.get("targMonEnd"));
				map.put("tDate", (String) map.get("month"));
				managerSaleInfo = statementDao.regionalManagerSalesProportion(map);
				for (int i = 0; i < staList.size(); i++) {
					for (int j = 0; j < managerSaleInfo.size(); j++) {
						if(staList.get(i).getId().equals(managerSaleInfo.get(j).getUserId()) && "3".equals(staList.get(i).getQuarter())){
							staList.get(i).setVolume(managerSaleInfo.get(j).getVolume());
							staList.get(i).setTarget(managerSaleInfo.get(j).getTarget());
							break;
						}
					}
				}
				
				map.put("tDate", (String) map.get("week"));
				managerSaleInfo = statementDao.regionalManagerSalesProportion(map);
				for (int i = 0; i < staList.size(); i++) {
					for (int j = 0; j < managerSaleInfo.size(); j++) {
						if(staList.get(i).getId().equals(managerSaleInfo.get(j).getUserId()) && "4".equals(staList.get(i).getQuarter())){
							staList.get(i).setVolume(managerSaleInfo.get(j).getVolume());
							staList.get(i).setTarget(managerSaleInfo.get(j).getTarget());
							break;
						}
					}
				}
			} else {
				
				stsList = statementDao.selectPartySupeAndSales(map);

				staList = new ArrayList<Statement>();// 储存人员和销售数据
				collectSaleUser(staList, stsList, "1");
				collectSaleUser(staList, stsList, "2");
				collectSaleUser(staList, stsList, "3");
				collectSaleUser(staList, stsList, "4");
				
				
				map.put("tDate", (String) map.get("year"));
				map.put("startTime", map.get("targYearStart"));
				map.put("endTime", map.get("targYearEnd"));
				List<Statement> managerSaleInfo = statementDao.toSuperviseTheSalesmanSalesProportion(map);
				
				for (int i = 0; i < staList.size(); i++) {
					for (int j = 0; j < managerSaleInfo.size(); j++) {
						if(staList.get(i).getId().equals(managerSaleInfo.get(j).getUserId()) && "1".equals(staList.get(i).getQuarter())){
							staList.get(i).setVolume(managerSaleInfo.get(j).getVolume());
							staList.get(i).setTarget(managerSaleInfo.get(j).getTarget());
							break;
						}
					}
				}
				
				
				map.put("startTime", map.get("targQuarterStart"));
				map.put("endTime", map.get("targQuarterEnd"));
				map.put("tDate", (String) map.get("quarter"));
				managerSaleInfo = statementDao.toSuperviseTheSalesmanSalesProportion(map);
				for (int i = 0; i < staList.size(); i++) {
					for (int j = 0; j < managerSaleInfo.size(); j++) {
						if(staList.get(i).getId().equals(managerSaleInfo.get(j).getUserId()) && "2".equals(staList.get(i).getQuarter())){
							staList.get(i).setVolume(managerSaleInfo.get(j).getVolume());
							staList.get(i).setTarget(managerSaleInfo.get(j).getTarget());
							break;
						}
					}
				}
				
				map.put("startTime", map.get("targMonStart"));
				map.put("endTime", map.get("targMonEnd"));
				map.put("tDate", (String) map.get("month"));
				managerSaleInfo = statementDao.toSuperviseTheSalesmanSalesProportion(map);
				for (int i = 0; i < staList.size(); i++) {
					for (int j = 0; j < managerSaleInfo.size(); j++) {
						if(staList.get(i).getId().equals(managerSaleInfo.get(j).getUserId()) && "3".equals(staList.get(i).getQuarter())){
							staList.get(i).setVolume(managerSaleInfo.get(j).getVolume());
							staList.get(i).setTarget(managerSaleInfo.get(j).getTarget());
							break;
						}
					}
				}
				
				map.put("tDate", (String) map.get("week"));
				managerSaleInfo = statementDao.toSuperviseTheSalesmanSalesProportion(map);
				for (int i = 0; i < staList.size(); i++) {
					for (int j = 0; j < managerSaleInfo.size(); j++) {
						if(staList.get(i).getId().equals(managerSaleInfo.get(j).getUserId()) && "4".equals(staList.get(i).getQuarter())){
							staList.get(i).setVolume(managerSaleInfo.get(j).getVolume());
							staList.get(i).setTarget(managerSaleInfo.get(j).getTarget());
							break;
						}
					}
				}
			}
		}
		return staList;
	}

	public void collectSaleUser(List<Statement> staList, List<Statement> stsList, String number) {
		if (stsList.size() > 0) {
			Statement s = null;

			s = new Statement();
			s.setId(stsList.get(0).getCountryId());
			s.setCountryName(stsList.get(0).getCountryName());
			s.setRevenue(0L);
			s.setVolume(0L);
			s.setTarget(0L);
			s.setTrueS(0L);
			s.setQuarter(number);
			staList.add(s);

			for (int i = 1; i < stsList.size(); i++) {

				s = new Statement();
				s.setId(stsList.get(i).getCountryId());
				s.setCountryName(stsList.get(i).getCountryName());
				s.setRevenue(0L);
				s.setVolume(0L);
				s.setTarget(0L);
				s.setTrueS(0L);
				s.setQuarter(number);
				staList.add(s);
			}
		}
	}

	@Override
	public List<Statement> sizeSaleNumber(Map<String, Object> map) throws Exception {
		return statementDao.selectModelSize(map);
	}

	@Override
	public List<Statement> selectAllSize() throws Exception {
		return statementDao.selectAllSize();
	}

	@Override
	public List<Statement> selectAttr() throws Exception {
		return statementDao.selectAttr();
	}

	@Override
	public List<Statement> selectSeriesByAttr(Map<String, Object> map) throws Exception {
		return statementDao.selectSeriesByAttr(map);
	}

	@Override
	public List<Statement> selectSizeBySeries(String series) throws Exception {
		return statementDao.selectSizeBySeries(series);
	}

	@Override
	public List<Statement> selectModelBy(Map<String, Object> map) throws Exception {
		return statementDao.selectModelBy(map);
	}

	@Override
	public List<Statement> itemTypeSaleInfo(Map<String, Object> map) throws Exception {
		return statementDao.itemTypeSaleInfo(map);
	}

	@Override
	public List<Statement> selectItemType(Map<String, Object> map) throws Exception {
		return statementDao.selectItemType(map);
	}
	
	@Override
	public List<Statement> selectAreaByPartyId(Map<String,Object> map) throws Exception {
		if( "true".equals((String)map.get("isCountry")) && "5".equals((String)map.get("country")) ){
			return statementDao.philSelectAreaByCountry((String)map.get("country"));
		}else{
			return "true".equals((String)map.get("isCountry")) ? statementDao.selectAreaByPartyId((String)map.get("partyId")): statementDao.selectPartyIdByArea(map);
		}
	}

	@Override
	public List<Statement> selectSizeByFunction(String function) throws Exception {
		return statementDao.selectSizeByFunction(function);
	}

	@Override
	public List<Statement> selectRegion(String partyId) throws Exception {
		return statementDao.selectRegion(partyId);
	}

	@Override
	public List<Statement> selectShopByUserId(Map<String, Object> map) throws Exception {
		return statementDao.selectShopByUserId(map);
	}

	@Override
	public List<Statement> selectCoefficientByPartyId(String partyId) throws Exception {
		return statementDao.selectCoefficientByPartyId(partyId);
	}

	@Override
	public List<Statement> selectExchangeByPartyId(String partyId) throws Exception {
		return statementDao.selectExchangeByPartyId(partyId);
	}

	@Override
	public List<Statement> selectItemTypeP(Map<String, Object> map) throws Exception {
		return statementDao.selectItemTypeP(map);
	}

	@Override
	public List<Statement> pSaleInfo(Map<String, Object> map) throws Exception {
		return statementDao.pSaleInfo(map);
	}

	@Override
	public List<Statement> selectCountryByUserId(Map<String, Object> map) throws Exception {
		return statementDao.selectCountryByUserId(map);
	}

	@Override
	public List<Statement> selectShopByCountnry(Map<String, Object> map) throws Exception {
		return statementDao.selectShopByCountnry(map);
	}

	@Override
	public List<Statement> shopSaleAndTarget(String tDate) throws Exception {
		return statementDao.shopSaleAndTarget(tDate);
	}

	@Override
	public List<Statement> selectShopByCountnrys(Map<String, Object> map) throws Exception {
		return statementDao.selectShopByCountnrys(map);
	}

	@Override
	public List<Statement> selecPartySalerInfo(Map<String, Object> map) throws Exception {
		return statementDao.selecPartySalerInfo(map);
	}

	@Override
	public List<Statement> selectAreaSaleInfoTest(Map<String, Object> map) throws Exception {
		return statementDao.selectAreaSaleInfoTest(map);
	}

	@Override
	public List<Statement> selectPartyIdSaleInfoInAll(Map<String,Object> map) throws Exception {
		return statementDao.selectPartyIdSaleInfoInAll(map);
	}

	@Override
	public List<Statement> selectCountrysaleInfo(Map<String, Object> map) throws Exception {
		return statementDao.selectCountrysaleInfo(map);
	}

	@Override
	public List<Statement> selectCountrysaleModel(Map<String, Object> map) throws Exception {
		return statementDao.selectCountrysaleModel(map);
	}

	@Override
	public List<Statement> codeItemTypeSaleInfo(Map<String, Object> map) throws Exception {
		return statementDao.codeItemTypeSaleInfo(map);
	}

	@Override
	public List<Statement> focusOnPersonalSelling(Map<String, Object> map) throws Exception {
		return statementDao.focusOnPersonalSelling(map);
	}

	@Override
	public List<Statement> focusOnRegionalSales(Map<String, Object> map) throws Exception {
		return statementDao.focusOnRegionalSales(map);
	}
	
	
	public String jointPartyId(String isQueryCountry,String partyId){
		if(!WebPageUtil.isStringNullAvaliable(partyId)){
			return "''";
		}else{
			List<Statement> list = null;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("partyId", partyId);
			
			try {
				if("true".equals(isQueryCountry)){
					list = statementDao.selectPartyIdByCountry(map);
				}else{
					list = statementDao.selectPartyIdByArea(map);
				}
				StringBuffer sbPartyId = new StringBuffer();
				if(list.size() > 0){
					sbPartyId.append("'"+list.get(0).getCountryId()+"'");
					for (int i = 1; i < list.size(); i++) {
						sbPartyId.append(",'"+list.get(i).getCountryId()+"'");
					}
				}else{
					 sbPartyId.append("''");
				}
				return sbPartyId.toString();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "''";
		}
	}
	
	@Override
	public List<Statement> selectCoefficientByCountryId(Map<String, Object> map) throws Exception {
		return statementDao.selectCoefficientByCountryId(map);
	}
	
	/*
	 *-----------********优化*********-----------
	 */

	@Override
	public List<Statement> selectPartyIdByCountry(Map<String, Object> map) throws Exception {
		return statementDao.selectPartyIdByCountry(map);
	}

	@Override
	public List<Statement> selectPartyIdByArea(Map<String, Object> map) throws Exception {
		return statementDao.selectPartyIdByArea(map);
	}

	
	/*
	 * 优化
	 */
	
	@Override
	public List<Statement> selectSubdomainByArea(Map<String, Object> map) throws Exception {
		return "true".equals(map.get("isCountry"))? statementDao.selectPartyIdByCountry(map):statementDao.selectPartyIdByArea(map);
	}
	
	@Override
	public List<Statement> queryFunctionSaleInfo(Map<String, Object> map) throws Exception {
		return statementDao.queryFunctionSaleInfo(map);
	}

	@Override
	public List<Statement> queryAllFunction() throws Exception {
		return statementDao.queryAllFunction();
	}

	@Override
	public List<Statement> queryUserSizeSaleInfo(Map<String, Object> map) throws Exception {
		return statementDao.queryUserSizeSaleInfo(map);
	}

	@Override
	public List<Statement> queryUsesrFunctionSaleInfo(Map<String, Object> map) throws Exception {
		return statementDao.queryUsesrFunctionSaleInfo(map);
	}

	@Override
	public List<Statement> querySizeSaleInfo(Map<String, Object> map) throws Exception {
		return statementDao.querySizeSaleInfo(map);
	}

	@Override
	public List<Statement> queryAllSize() throws Exception {
		return statementDao.queryAllSize();
	}

	@Override
	public List<Statement> businessManagerByPartyId(Map<String, Object> map) throws Exception {
		return statementDao.businessManagerByPartyId(map);
	}

	@Override
	public List<Statement> selectPartySupeAndSale(Map<String, Object> map) throws Exception {
		return statementDao.selectPartySupeAndSale(map);
	}

	@Override
	public List<Statement> selectPartyIdByUserManager(Map<String, Object> map) throws Exception {
		return statementDao.selectPartyIdByUserManager(map);
	}

	@Override
	public List<Statement> queryManagerOfSalesYear(Map<String, Object> map) throws Exception {
		return statementDao.queryManagerOfSalesYear(map);
	}

	@Override
	public List<Statement> queryRoleOfSalesYear(Map<String, Object> map) throws Exception {
		return statementDao.queryRoleOfSalesYear(map);
	}
	
	public String jointPartyIdList(List<Statement> list){
		if(list != null){
			StringBuffer sb = new StringBuffer();
			if(list.size() > 0){
				sb.append("'"+list.get(0).getCountryId()+"'");
				for (int i = 1; i < list.size(); i++) {
					sb.append(",'"+list.get(i).getCountryId()+"'");
				}
			}else{
				sb.append("''");
			}
			return sb.toString();
		};
		return "''";
	}

	@Override
	public List<Statement> regionalSalesOfKeyProductsInfo(Map<String, Object> map) throws Exception {
		return statementDao.regionalSalesOfKeyProductsInfo(map);
	}

	@Override
	public List<Statement> personalSalesOfKeyProductsInfo(Map<String, Object> map) throws Exception {
		return statementDao.personalSalesOfKeyProductsInfo(map);
	}

	@Override
	public List<Statement> personalItemSalesInfo(Map<String, Object> map) throws Exception {
		return statementDao.personalItemSalesInfo(map);
	}

	@Override
	public List<Statement> regionalSalesItemInfo(Map<String, Object> map) throws Exception {
		return statementDao.regionalSalesItemInfo(map);
	}

	@Override
	public List<Statement> personalManagerSalesData(Map<String, Object> map) throws Exception {
		return statementDao.personalManagerSalesData(map);
	}

	@Override
	public List<Statement> personalSalesData(Map<String, Object> map) throws Exception {
		return statementDao.personalSalesData(map);
	}

	@Override
	public List<Statement> selectPartySupeAndSaless(Map<String, Object> map) throws Exception {
		return statementDao.selectPartySupeAndSaless(map);
	}

	@Override
	public List<Statement> eachRegionalSalesQuantity(Map<String, Object> map) throws Exception {
		return statementDao.eachRegionalSalesQuantity(map);
	}
	@Override
	public List<Statement> eachStoresSalesQuantity(Map<String, Object> map) throws Exception {
		return statementDao.eachStoresSalesQuantity(map);
	}
	
	@Override
	public List<Statement> selectShopByPartyList (Map<String, Object> map)throws Exception {
		return statementDao.selectShopByPartyList(map);
	}
	@Override
	public List<Statement> selectCustomerByPartyList (String countryList)throws Exception {
		return statementDao.selectCustomerByPartyList(countryList);
	}

	@Override
	public List<Statement> eachChannelsSalesQuantity(Map<String, Object> map) throws Exception {
		return statementDao.eachChannelsSalesQuantity(map);
	}

	@Override
	public List<Statement> eachRoleSalesQuantity(Map<String, Object> map) throws Exception {
		return statementDao.eachRoleSalesQuantity(map);
	}

	@Override
	public List<Statement> eachSingleAreaSalesQuantity(Map<String, Object> map) throws Exception {
		return statementDao.eachSingleAreaSalesQuantity(map);
	}

	@Override
	public List<Statement> storeLevelQuery(String country) throws Exception {
		return statementDao.storeLevelQuery(country);
	}

	@Override
	public List<Statement> selectACCatena(String acType) throws Exception {
		return statementDao.selectACCatena(acType);
	}

	@Override
	public List<Statement> selectACSize(String catena,String acType) throws Exception {
		return statementDao.selectACSize(catena,acType);
	}

	@Override
	public List<Statement> personalSalesACData(Map<String, Object> map) throws Exception {
		return statementDao.personalSalesACData(map);
	}

	@Override
	public List<Statement> personalManagerSalesACData(Map<String, Object> map) throws Exception {
		return statementDao.personalManagerSalesACData(map);
	}

	@Override
	public List<Statement> selectAcMachine() throws Exception {
		return statementDao.selectAcMachine();
	}
}
