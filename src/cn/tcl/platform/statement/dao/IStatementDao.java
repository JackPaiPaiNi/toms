package cn.tcl.platform.statement.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.statement.vo.Statement;

public interface IStatementDao {
	
public Statement selectCountry(String userId) throws Exception;
	
	public List<Statement> selectAllCountry (String partyId)throws Exception;
	
	public List<Statement> selectUserSale(Map<String,Object> map)throws Exception;
	
	public List<Statement> selectSalertype(Map<String,Object> map) throws Exception;
	
	public List<Statement> selecPartySalerTar(Map<String,Object> map) throws Exception;
	
	public List<Statement> selectBusinessCenter() throws Exception;
	
	public List<Statement> businessCenterByUserId(String userId)throws Exception;
	
	public List<Statement> selectSaleRank(Map<String,Object> objMap) throws Exception;
	
	public List<Statement> selectShopByPartyId(Map<String,Object> map) throws Exception;
	
	public List<Statement> selectShopSaleIN(Map<String,Object> map) throws Exception;
	
	public List<Statement> selectChannelSaleIN(Map<String,Object> map) throws Exception;
	 
	public List<Statement> selectAllSize() throws Exception;
	
	public List<Statement> selectModelSize(Map<String,Object> map) throws Exception;
	
	public List<Statement> selectAttr()throws Exception;
	
	public List<Statement> selectSeriesByAttr(Map<String,Object> map) throws Exception; 
	
	public List<Statement> selectSizeBySeries (String series) throws Exception;
	
	public List<Statement> selectModelBy(Map<String,Object> map)throws Exception;
	
	public List<Statement> itemTypeSaleInfo(Map<String,Object> map)throws Exception;
	
	public List<Statement> codeItemTypeSaleInfo(Map<String,Object> map)throws Exception;
	
	public List<Statement> selectItemType(Map<String,Object> map)throws Exception;
	
	public List<Statement> selectAreaByPartyId (String partyId)throws Exception;
	
	public List<Statement> selectSizeByFunction(String function)throws Exception;
	
	public List<Statement> selectUserSaleNumber (Map<String,Object> map)throws Exception;
	
	public List<Statement> selectRegion (String partyId)throws Exception;
	
	public List<Statement> selectUserSaleNumberHead (Map<String,Object> map)throws Exception;
	
	public List<Statement> selectYearInfoHead (Map<String,Object> map)throws Exception;
	
	public List<Statement> selectShopByUserId (Map<String,Object> map)throws Exception;
	
	public List<Statement> selectCoefficientByPartyId (String partyId)throws Exception;
	
	public List<Statement> selectExchangeByPartyId (String partyId)throws Exception;
	
	public List<Statement> shopSaleAndTarget (String tDate)throws Exception;
	
	public List<Statement> selectItemTypeP (Map<String,Object> map)throws Exception;
	
	public List<Statement> pSaleInfo (Map<String,Object> map)throws Exception;
	
	public List<Statement> shopSaleTarget (Map<String,Object> map)throws Exception;
	
	public List<Statement> selectCountryByUserId (Map<String,Object> map)throws Exception;
	
	public List<Statement> selectShopByCountnry (Map<String,Object> map)throws Exception;
	
	public List<Statement> selectShopByCountnrys (Map<String,Object> map)throws Exception;
	
	public List<Statement> selecPartySalerInfo (Map<String,Object> map)throws Exception;
	
	public List<Statement> selectPartyIdSaleInfoInAll (Map<String,Object> map)throws Exception;
	
	public List<Statement> selectCountrysaleModel (Map<String,Object> map)throws Exception;
	
	public List<Statement> selectCountrysaleInfo (Map<String,Object> map)throws Exception;
	
	public List<Statement> selectAreaSaleInfoTest (Map<String,Object> map)throws Exception;
	
	public List<Statement> focusOnPersonalSelling (Map<String,Object> map)throws Exception;
	
	public List<Statement> focusOnRegionalSales (Map<String,Object> map)throws Exception;
	
	public List<Statement> selectCoefficientByCountryId (Map<String,Object> map)throws Exception;
	
	
	/*
	 * 优化
	 */
	public List<Statement> selectPartyIdByCountry (Map<String,Object> map)throws Exception;
	
	public List<Statement> selectPartyIdByArea (Map<String,Object> map)throws Exception;
	
	public List<Statement> queryFunctionSaleInfo (Map<String,Object> map)throws Exception;
	
	public List<Statement> queryUserSizeSaleInfo (Map<String,Object> map)throws Exception;
	
	public List<Statement> queryUsesrFunctionSaleInfo (Map<String,Object> map)throws Exception;
	
	public List<Statement> businessManagerByPartyId (Map<String,Object> map)throws Exception;
	
	public List<Statement> querySizeSaleInfo (Map<String,Object> map)throws Exception;
	
	public List<Statement> selectPartySupeAndSale (Map<String,Object> map)throws Exception;
	
	public List<Statement> selectPartyIdByUserManager (Map<String,Object> map)throws Exception;
	
	public List<Statement> queryManagerOfSalesYear (Map<String,Object> map)throws Exception;
	
	public List<Statement> queryRoleOfSalesYear (Map<String,Object> map)throws Exception;
	
	public List<Statement> selectPartySupeAndSales (Map<String,Object> map)throws Exception;
	
	public List<Statement> selectPartySupeAndSaless (Map<String,Object> map)throws Exception;
	
	public List<Statement> businessManagerByParty (Map<String,Object> map)throws Exception;
	
	public List<Statement> regionalSalesOfKeyProductsInfo (Map<String,Object> map)throws Exception;
	
	public List<Statement> personalSalesOfKeyProductsInfo (Map<String,Object> map)throws Exception;
	 
	public List<Statement> personalItemSalesInfo (Map<String,Object> map)throws Exception;
	
	public List<Statement> regionalSalesItemInfo (Map<String,Object> map)throws Exception;
	
	public List<Statement> personalManagerSalesData (Map<String,Object> map)throws Exception;
	
	public List<Statement> personalSalesData (Map<String,Object> map)throws Exception;
	
	public List<Statement> eachRegionalSalesQuantity (Map<String,Object> map)throws Exception;
	
	public List<Statement> eachStoresSalesQuantity (Map<String,Object> map)throws Exception;
	
	public List<Statement> eachChannelsSalesQuantity (Map<String,Object> map)throws Exception;
	
	public List<Statement> eachRoleSalesQuantity (Map<String,Object> map)throws Exception;
	
	public List<Statement> eachSingleAreaSalesQuantity (Map<String,Object> map)throws Exception;
	
	public List<Statement> regionalManagerSalesProportion (Map<String,Object> map)throws Exception;
	
	public List<Statement> toSuperviseTheSalesmanSalesProportion (Map<String,Object> map)throws Exception;
	
	public List<Statement> queryAllFunction ()throws Exception;
	
	public List<Statement> queryAllSize ()throws Exception;
	
	public List<Statement> philSelectAreaByCountry (String partyId)throws Exception;
	
	public List<Statement> selectShopByPartyList (Map<String,Object> map)throws Exception;
	
	public List<Statement> personalSalesACData (Map<String,Object> map)throws Exception;
	
	public List<Statement> personalManagerSalesACData (Map<String,Object> map)throws Exception;
	
	public List<Statement> selectCustomerByPartyList (@Param(value="countryList") String countryList)throws Exception;
	
	public List<Statement> storeLevelQuery (@Param(value="country") String country)throws Exception;
	
	public List<Statement> selectACSize (@Param(value="catena") String catena,
										 @Param(value="acType") String acType
										)throws Exception;
	
	public List<Statement> selectACCatena (@Param(value="acType") String acType)throws Exception;
	
	public List<Statement> selectAcMachine ()throws Exception;
	
}
