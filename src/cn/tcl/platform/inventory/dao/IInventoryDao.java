package cn.tcl.platform.inventory.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.inventory.vo.Inventory;
import cn.tcl.platform.party.vo.Party;

public interface IInventoryDao {
	//选择库存
		public List<Inventory> selectInventory(@Param(value="start") int start,
				@Param(value="limit") int  limit,
				@Param(value="searchStr") String searchStr,
				@Param(value="order") String order,
				@Param(value="sort") String sort,
				@Param(value="conditions") String conditions) throws Exception;
		//总数计算
		public int countInventory(@Param(value="start") int start,
				@Param(value="limit") int  limit,
				@Param(value="searchStr") String searchStr,
				@Param(value="conditions") String conditions) throws Exception;
		//获取其中一个
		public Inventory getInventory(int iid);
		
		//保存库存
		public void saveInventory(Inventory inventory) throws Exception;
		//获取新的总部型号
		//public String getRepeatByHqModel(String hqmodel) throws Exception;
		//获取新型号
		//public ModelMap getRepeatByModel(String model) throws Exception;
		//所属国家
		public Party getOnePartyByName(String partyName) throws Exception;
		
		public int getCountModelMapByBranch(Inventory inventory) throws Exception; 
		
		public int searchHqModelMapCount(Inventory inventory) throws Exception;
		
		public int searchCountryByName(String countryName) throws Exception;
		
		public String getPartyIdByName(Inventory inventory) throws Exception;
		
//		public String getCountryIdByName(String partyName) throws Exception;
}
