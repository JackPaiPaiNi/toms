package cn.tcl.platform.shop.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.tcl.platform.cfgparam.vo.CfgParameter;

import cn.tcl.platform.shop.vo.Level;
import cn.tcl.platform.shop.vo.ShopParty;
import cn.tcl.platform.shop.vo.Shop;
import cn.tcl.platform.shop.vo.ShopPhotos;
import cn.tcl.platform.shop.vo.ShopUser;

public interface IShopService {
	//取列表
	public Map<String, Object> selectShopsData(int start,int  limit,String searchStr,String levelNum,
			String order,String sort,String conditions) throws Exception;
	//保存门店
	public void saveShop(Shop shop,String businessers,String salers,String supervisors) throws Exception;
	//获取门店
	public Shop getShop(String id) throws Exception;
	//编辑门店
	public void editShop(Shop shop,String businessers,String salers,String supervisors) throws Exception;
	//获取门店相关的业务员，促销员
//	public List<ShopUser> getShopUserRelations(Integer shopId,String partyId) throws Exception;
	public List<ShopUser> getShopUserRelations(String shopId,String partyId) throws Exception;
	//删除门店
	public void deleteShop(Shop shop) throws Exception;
	//导入客户
	public void importShop(List<Shop> shops) throws Exception;
	//查询门店
	public List<Shop> getShopDataList(String conditions) throws Exception;
	//门店 经纬度
	public List<Shop> getShopGeoCoord(String conditions) throws Exception;
	
	public int getShopByName(String shopName,String customer) throws Exception;
	
	public Shop getRepeatByName(String shopName) throws Exception;
	
	
	public Map<String, Object> selectShopPhotosData(int start,int  limit,String searchStr,
			String order,String sort,String conditions) throws Exception;
	//查询门店照片
	public List<ShopPhotos> getShopPhotosDataList(String conditions) throws Exception;
	public Shop getShopByNames(String shopName) throws Exception;
	
	public Shop selectPartyByName(String partyName) throws Exception;
	
	public int selectPartyByCount(String partyName) throws Exception;

	public List<ShopParty> selectParty(String countryId) throws Exception;
	public List<Shop> selectCustomer(String countryId) throws Exception;

	
	public XSSFWorkbook exporShopName(String conditions,String[] excelHeader,String title) throws Exception;
	
	public List<Level> selectShopLevel(String partyId) throws Exception;
	
	public Level selectLevelName(String levelName,String countryId) throws Exception;
	
	public String selectLevelByCountryName(String countryName) throws Exception;
	
	public List<Level> selectLevelBycountry(String countryId) throws Exception;
	
	
	public String readExcel(File file, String fileName) throws IOException;

	public String read2007Excel(File file) throws IOException;
	
	
	public String readExcelByPe(File file, String fileName) throws IOException;

	public String read2007ExcelByPe(File file) throws IOException;
	
	
	public String readExcelByShop(File file, String fileName) throws Exception;

	public String exportShop(File file) throws Exception;
	
	/**
	 * 查询Location是否已经存在
	 * @param location
	 * @return
	 * @throws Exception
	 */
	public int selectLocationIsExist(String location) throws Exception;
	
	
	public Map<String, Object> selectShopsBySupSaleData(int start,int  limit,String searchStr,String levelNum,
			String order,String sort,String conditions) throws Exception;

}
