package cn.tcl.platform.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.excel.vo.ImportExcel;
import cn.tcl.platform.shop.vo.Level;
import cn.tcl.platform.shop.vo.ShopParty;
import cn.tcl.platform.shop.vo.Shop;
import cn.tcl.platform.shop.vo.ShopPhotos;
import cn.tcl.platform.shop.vo.ShopUser;

public interface IShopDao {
	public List<Shop> selectShops(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="levelNum") String levelNum,
			@Param(value="order") String order,
			@Param(value="sort") String sort,
			@Param(value="conditions") String conditions) throws Exception;
	public int countShops(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="levelNum") String levelNum,
			@Param(value="conditions") String conditions) throws Exception;
	
	//保存门店
	public int saveShop(Shop shop) throws Exception;
	
	//门店与业务员、促销员的关联关系
	public void saShopUserRelation(@Param(value="shopId")Integer shopId,
			@Param(value="user_login_id") String user_login_id,
			@Param(value="type") int type) throws Exception;
	
	//获取门店
	public Shop getShop(String sid) throws Exception;
	//获取门店与用户（促销员或业务员）关系
//	public List<ShopUser> getShopUserRelations(Integer sid) throws Exception;
	public List<ShopUser> getShopUserRelations(@Param("shopId")String shopId,@Param("partyId")String partyId) throws Exception;
	//清除门店与用户的所有关系
	public void clearShopUserRelations(Integer sid) throws Exception;
	
	//编辑门店
	public void editShop(Shop shop) throws Exception;
	//删除门店
//	public void deleteShop(Integer shopId) throws Exception;
	public void deleteShop(Shop shop) throws Exception;
	
	public void deleteShopSalerMapping(Integer shopId) throws Exception;
	
	public List<Shop> getShopDataList(@Param(value="conditions") String conditions) throws Exception;
	
	//门店
	public List<Shop> getShopGeoCoord(@Param(value="conditions") String conditions) throws Exception;
	
	public int getShopByName(@Param(value="shopName") String shopName,
			@Param(value="customer") String customer) throws Exception;
	
	public Shop getRepeatByName(@Param(value="shopName") String shopName) throws Exception;
	
	
	
	public List<ShopPhotos> selectShopPhotos(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="order") String order,
			@Param(value="sort") String sort,
			@Param(value="conditions") String conditions) throws Exception;

	public int countShopPhotos(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="conditions") String conditions) throws Exception;
	
	public List<ShopPhotos> getShopPhotosDataList(@Param(value="conditions") String conditions) throws Exception;
	
	
	
	public Shop getShopByNames(@Param(value="shopName") String shopName) throws Exception;
	public Shop getShopByNameOrLocation(@Param(value="shopName") String shopName) throws Exception;
	
	public Shop selectPartyByName(@Param(value="partyName") String partyName) throws Exception;
	public int selectPartyByCount(@Param(value="partyName") String partyName) throws Exception;
	public int selectUserByCount(@Param(value="user") String user) throws Exception;

	
	
	public List<ShopParty> selectParty(@Param(value="countryId") String countryId) throws Exception;

	public List<Shop> getShopBySNameAndCName(@Param(value="shopName")String shopName,@Param(value="customerName")String customerName) throws Exception;

	public List<Shop> selectShopName(
			@Param(value="searchStr") String searchStr,
			@Param(value="conditions") String conditions) throws Exception;
	

	
	public List<Shop> selectCustomer(@Param(value="countryId") String countryId) throws Exception;

	//根据用户登陆显示应对的门店级别
	public List<Level> selectShopLevel(@Param("partyId")String partyId) throws Exception;
	
	//根据countryid筛选对应的门店等级
	public List<Level> selectLevelByCountry(@Param("countryId")String countryId) throws Exception;
	//根据导入国家查询对应国家id
	public String selectLevelByCountryName(@Param("countryName")String countryName) throws Exception;
	//根据导入等级和国家id
	public Level selectLevelName(@Param(value="levelName")String levelName,@Param("countryId")String countryId) throws Exception;
	
	public int deleteShopMapping(@Param(value="user") String user) throws Exception;

	
	public ShopUser selectUserByPro(@Param(value="user") String user) throws Exception;
	public ShopUser selectUserByRole(@Param(value="user") String user) throws Exception;

	public int updateShopName(List<Shop > shopList) throws Exception;
	
	/**
	 * 查询Location是否已经存在
	 * @param location
	 * @return
	 * @throws Exception
	 */
	public int selectLocationIsExist(@Param("location")String location) throws Exception;

	 
	/**
	 * 根据促销员，业务员查看门店
	 * @param start
	 * @param limit
	 * @param searchStr
	 * @param levelNum
	 * @param order
	 * @param sort
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public List<Shop> selectShopBySupSale(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="levelNum") String levelNum,
			@Param(value="order") String order,
			@Param(value="sort") String sort,
			@Param(value="conditions") String conditions) throws Exception;
	public int countShopBySupSale(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="levelNum") String levelNum,
			@Param(value="conditions") String conditions) throws Exception;
}
