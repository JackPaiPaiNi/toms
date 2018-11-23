package cn.tcl.platform.inventory.service;

import java.util.List;
import java.util.Map;

import cn.tcl.platform.inventory.vo.Inventory;
import cn.tcl.platform.party.vo.Party;

public interface IInventoryService {
	//终端库存数据列表
	public Map<String, Object> selectInventorydata(int start,int  limit,String searchStr,String order,String sort,String conditions) throws Exception;
	//获取其中一个
	public Inventory getInventory(int iid);
	//导入库存数据
	public void importInventory(List<Inventory> inventorys) throws Exception;
	//保存库存数据
	public void saveInventory(Inventory inventory) throws Exception;
	//获取新的总部型号
	//public String getRepeatByHqModel(String hqmodel) throws Exception;
	//获取新型号
	//public ModelMap getRepeatByModel(String model) throws Exception;
	//所属机构
	public Party getOnePartyByName(String partyName) throws Exception;
	
	public int getCountModelMapByBranch(Inventory inventory) throws Exception;
	
	public int searchHqModelMapCount(Inventory inventory) throws Exception;
	
	public int searchCountryByName(String countryName) throws Exception;
	
//	public String getCountryIdByName(String partyName) throws Exception;
}
