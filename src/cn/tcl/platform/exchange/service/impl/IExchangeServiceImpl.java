package cn.tcl.platform.exchange.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.common.Contents;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.exchange.dao.IExchangeDao;
import cn.tcl.platform.exchange.service.IExchangeService;
import cn.tcl.platform.exchange.vo.Exchange;
import cn.tcl.platform.modelmap.vo.ModelMap;
import cn.tcl.platform.party.vo.Party;
import cn.tcl.platform.role.vo.Role;

@Service("ExchangeService")
public class IExchangeServiceImpl implements IExchangeService{
	@Autowired
	private IExchangeDao exchangeDao;

//	@Override
//	public Map<String, Object> selectExchange(String searchMsg, String startRow, String pageSize, String order, String sort,String selectQuertyPartyId) throws Exception {
//Map<String,Object> result = new HashMap<String,Object>();
//		
//		pageSize = "".equals(pageSize)?"0":pageSize;
//		startRow = "".equals(startRow)?"0":startRow;
//		String userPartyIds = WebPageUtil.loadPartyIdsByUserId();
//		String conditions = "";
//		if(!WebPageUtil.isHAdmin())
//		{
//			//conditions += " and ul.USER_LOGIN_ID not in (select tt.USER_LOGIN_ID from user_role_mapping tt where tt.role_id like '%"+Contents.ROLE_TYPE_HADMIN+"%')";
//			if(null!=userPartyIds && !"".equals(userPartyIds))
//			{
////				conditions += " and ul.USER_LOGIN_ID in " +
////						"(select DISTINCT m.USER_LOGIN_ID from user_role_mapping m,role_data_permission n " +
////						"where m.ROLE_ID = n.ROLE_ID and n.PERMISSION_TYPE = '"+Contents.ROLE_DATA_PERMISSION_PARTY+"'	and n.permission_value in (" +
////						userPartyIds + "))";
//				conditions += " p.party_Id in ("+userPartyIds+")";
//			}
//			else
//			{
//				conditions += " and 1=2 ";
//			}
//		}
//		else
//		{
////			conditions += " and 1=1 ";
//			conditions += (WebPageUtil.isStringNullAvaliable(selectQuertyPartyId) ? "AND p.`COUNTRY_ID` = "+selectQuertyPartyId : "and 1=1" );
//		}
////		conditions +=("".equals(searchMsg) || null==searchMsg)?"":" and (a.ROLE_ID like '%"+searchMsg+"%' or a.ROLE_NAME like '%"+searchMsg+"%')";
//		String orderBy= " order by t." + sort + " " + order;
//		
//		int startIndex = Integer.valueOf(startRow);
//		int endIndex = Integer.valueOf(pageSize);
//		
//		List<Exchange> roleList = exchangeDao.selectExchange(conditions, orderBy, startIndex, endIndex);
//		int total = exchangeDao.countExchange(conditions);
//		result.put("total", total);
//		result.put("list", roleList);
//		
//		return result;
//	}
	@Override
	public Map<String, Object> selectExchange(int start, int limit,
			String keyword, String order, String sort,String conditions,String selectQuertyPartyId) throws Exception {
		List<Exchange> list = exchangeDao.selectExchange(start, limit, keyword, order, sort,conditions);
		Map<String, Object> map = new HashMap<String, Object>();
		int count = exchangeDao.countExchange(start, limit, keyword,conditions);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}	
	

	@Override
	public void saveExchange(Exchange exchange) throws Exception {
			exchangeDao.saveExchange(exchange);
	}

	/*@Override
	public Party getCountryName(String countryName) throws Exception {
		return exchangeDao.getCountryName(countryName);
	}*/

	@Override
	public void updateByExchange(Exchange exchange) throws Exception {
		   exchangeDao.updateByExchange(exchange);
		
	}

	@Override
	public Exchange selectExchangeById(String id) throws Exception {
		return exchangeDao.selectExchangeById(id);
	}

	@Override
	public void deleteExchange(Exchange exchange) throws Exception {
		exchangeDao.deleteExchange(exchange);
		
	}


	
	
}
