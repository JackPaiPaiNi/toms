package cn.tcl.platform.sale.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.sale.vo.SaleTarget;
import cn.tcl.platform.target.vo.Target;

public interface ISaleTargetDao {
	//选择产品
	/*public List<SaleTarget> selectSaleTargets(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchParty")String searchParty,
			@Param(value="searchCustomer")String searchCustomer,
			@Param(value="searchShop")String searchShop,
			@Param(value="order") String order,
			@Param(value="sort") String sort,
			@Param(value="conditions") String conditions) throws Exception;*/
	//--------------------------------门店级 type类型2-----------------------
	//TV销售目标
	//门店目标
	public List<SaleTarget> selectSaleTargets(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	//渠道目标
	public List<SaleTarget> selectChannelTargets(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	//办事处目标
	public List<SaleTarget> selectOfficeTargets(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	//经营部目标
	public List<SaleTarget> selectRegionTargets(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	//分公司目标
	public List<SaleTarget> selectBranchTargets(@Param("conditions")String conditions,@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	//导购员目标
//	public List<SaleTarget> selectPromoterTargets() throws Exception;
	//督导目标
	public List<SaleTarget> selectSupervisorTargets(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	//业务员目标
	public List<SaleTarget> selectSalesmanTargets(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	//业务经理目标
	public List<SaleTarget> selectBusinessTargets(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	//修改目标
	public int updateTarget(SaleTarget excel)throws Exception;
	//目标总数
	public int selectCount(@Param("id")String id,
							@Param("datadate")String datadate,
							@Param("type")String type,
							@Param("clas")String clas,
							@Param("country")String country
			) throws Exception;
	//总数计算
	public int countSaleTargets(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchParty")String searchParty,
			@Param(value="searchCustomer")String searchCustomer,
			@Param(value="searchShop")String searchShop,
			@Param(value="conditions") String conditions) throws Exception;
	//获取其中一个
	public SaleTarget getSaleTarget(int sid);
	
	public void insertSaleTarget(SaleTarget saleTarget) throws Exception;
	
	public void updateSaleTarget(SaleTarget saleTarget) throws Exception;
	
	public void deleteSaleTargetData(SaleTarget saleTarget) throws Exception;
	
	public int validationShopId(@Param(value="shopId") String shopId) throws Exception;
	
	public List<SaleTarget> getSaleTargetCompletionList(@Param(value="conditionsToSaleTarget") String conditionsToSaleTarget) throws Exception;
	
	public int saveSales(SaleTarget saleTarget);
	
	public String selectCountry(@Param("country")String country) throws Exception;
	
	public Float selectExchange(@Param("countryId")String countryId) throws Exception;
	
	
	//AC销售目标
	public List<SaleTarget> selectACSaleTargets(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	//渠道目标
	public List<SaleTarget> selectACChannelTargets(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	//办事处目标
	public List<SaleTarget> selectACOfficeTargets(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	//经营部目标
	public List<SaleTarget> selectACRegionTargets(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	//分公司目标
	public List<SaleTarget> selectACBranchTargets(@Param("conditions")String conditions,@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	//督导目标
	public List<SaleTarget> selectACSupervisorTargets(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	//业务员目标
	public List<SaleTarget> selectACSalesmanTargets(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	//业务经理目标
	public List<SaleTarget> selectACBusinessTargets(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	
	//根据type查询
	public String selectType(String type) throws Exception;
	
	//--------------------------------渠道级 type类型1-----------------------
	//根据国家获取目标显示的类型（1.国家，2.渠道，3.门店）
	public String selectSOType(@Param("countryId")String countryId) throws Exception;
	
	//获取导入渠道目标数据（TV）
	public List<SaleTarget> getChannelTarget(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	
	//获取渠道国家目标数据（TV）
	public List<SaleTarget> getBranchTarget(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	
	//获取渠道督导目标数据（TV）
	public List<SaleTarget> getSupervisorTarget(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	
	//获取渠道业务员目标数据（TV）
	public List<SaleTarget> getSalemanTarget(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	
	//获取渠道业务经理目标数据（TV）
	public List<SaleTarget> getBusinessTarget(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	
	//------------------------------------国家级type为0------------------------------------
	//获取导入分公司目标数据（TV）
	public List<SaleTarget> getBranchTargetList(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	
	
	
	//获取渠道目标数据（AC）
	public List<SaleTarget> getACChannelTarget(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	
	//获取渠道分公司目标数据（AC）
	public List<SaleTarget> getACBranchTarget(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception; 
	
	//获取渠道督导目标数据（AC）
	public List<SaleTarget> getACSupervisorTarget(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	
	//获取渠道业务员目标数据 （AC）
	public List<SaleTarget> getACSalemanTarget(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	
	//获取渠道业务经理目标数据 （AC）
	public List<SaleTarget> getACBussinessTarget(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	
	//------------------------------------国家级type为0------------------------------------
	//获取导入分公司目标数据 （AC）
	public List<SaleTarget> getACBranchTagetList(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	
	//登陆总部用户显示分公司目标数据(TV)
	public List<SaleTarget> getOBCTVBranchTarget(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	
	//登陆总部用户显示分公司目标数据（AC）
	public List<SaleTarget> getOBCACBranchTarget(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
}
