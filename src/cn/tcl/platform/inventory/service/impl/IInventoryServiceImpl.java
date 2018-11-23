package cn.tcl.platform.inventory.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.inventory.dao.IInventoryDao;
import cn.tcl.platform.inventory.service.IInventoryService;
import cn.tcl.platform.inventory.vo.Inventory;
import cn.tcl.platform.modelmap.dao.IModelMapDao;
import cn.tcl.platform.party.dao.IPartyDAO;
import cn.tcl.platform.party.vo.Party;
import cn.tcl.platform.shop.dao.IShopDao;
import cn.tcl.platform.shop.vo.Shop;

@Service("inventoryService")
public class IInventoryServiceImpl implements IInventoryService{
	@Autowired
	private IInventoryDao inventoryDao;
	@Autowired
	private IPartyDAO partyDao;
	@Autowired
	private IModelMapDao modelMapDao;
	@Autowired
	private IShopDao shopDao;

	@Override
	public Map<String, Object> selectInventorydata(int start, int limit,
			String searchStr, String order, String sort, String conditions)
			throws Exception {
		List<Inventory> list = inventoryDao.selectInventory(start, limit, searchStr, order, sort, conditions);
		Map<String, Object> map=new HashMap<String, Object>();
		int count = inventoryDao.countInventory(start, limit, searchStr, conditions);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}

	@Override
	public Inventory getInventory(int iid) {
		
		return inventoryDao.getInventory(iid);
	}

	@Override
	public void importInventory(List<Inventory> inventorys) throws Exception {
		if(inventorys!=null){
			for (Inventory inventory : inventorys) {
			
				String party = inventoryDao.getPartyIdByName(inventory);
				inventory.setPartyId(party);
				
//				String userName = inventory.getUserId();
//				inventory.setUserName(userName);
				
//				String countryName = inventory.getCountryId();
//				String countryId = inventoryDao.getCountryIdByName(countryName);
//				inventory.setCountryId(countryId);
				
				inventory.setHqModel(inventory.getHqModel().toUpperCase());
				inventory.setModel(inventory.getModel().toUpperCase());
				
				String shopName = inventory.getShopName();
				Shop shop = shopDao.getShopByNames(shopName);
				inventory.setShopId(shop.getShopId()== null ? null:shop.getShopId());
				System.out.println("=========================="+inventory.getPartyId());
				System.out.println(inventory.getHqModel());
				System.out.println(inventory.getHqModel());
				System.out.println(shop.getShopId());
			inventoryDao.saveInventory(inventory);
			}
		}
		
	}

	@Override
	public void saveInventory(Inventory inventory) throws Exception {
		inventoryDao.saveInventory(inventory);
		
	}

	/*@Override
	public String getRepeatByHqModel(String hqmodel) throws Exception {
		
		return inventoryDao.getRepeatByHqModel(hqmodel);
	}*/

	
	

	@Override
	public Party getOnePartyByName(String partyName) throws Exception{
		
		return partyDao.getOnePartyByName(partyName);
	}

	@Override
	public int getCountModelMapByBranch(Inventory inventory) throws Exception {
		
		return inventoryDao.getCountModelMapByBranch(inventory);
	}

	@Override
	public int searchHqModelMapCount(Inventory inventory) throws Exception {
		return inventoryDao.searchHqModelMapCount(inventory);
	}

	@Override
	public int searchCountryByName(String countryName) throws Exception {
		return inventoryDao.searchCountryByName(countryName);
	}

//	@Override
//	public String getCountryIdByName(String partyName) throws Exception {
//		return inventoryDao.getCountryIdByName(partyName);
//	}

	/*@Override
	public ModelMap getRepeatByModel(String model) throws Exception {
		return inventoryDao.getRepeatByModel(model);
	}*/

	
}
