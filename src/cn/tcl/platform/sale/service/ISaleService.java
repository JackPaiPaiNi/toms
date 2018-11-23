package cn.tcl.platform.sale.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.tcl.platform.excel.vo.ImportExcel;
import cn.tcl.platform.sale.vo.Sale;
import cn.tcl.platform.sale.vo.SaleTarget;
import cn.tcl.platform.shop.vo.Shop;

public interface ISaleService {
	//查销售数据列表
	public Map<String, Object> selectSalesData(int start,int  limit,String searchStr,String order,String sort,String conditions) throws Exception;
	
	
	public Map<String, Object> selectCountrySalesData(int start,int  limit,String searchStr,String order,String sort,String conditions) throws Exception;

	public Map<String, Object> selectCustomerSalesData(int start,int  limit,String searchStr,String order,String sort,String conditions) throws Exception;

	
	public Map<String, Object> selectSalesDataByMobile(int start,int  limit,String searchStr,String order,String sort,String conditions) throws Exception;

	
	//查找某个销售数据
	public Sale getSale(Integer sid) throws Exception;
	//保存销售数据
	public void saveSale(Sale sale) throws Exception;
	
	//查销售目标列表
	//public Map<String, Object> selectSaleTargets(int start,int  limit,String searchParty,String searchCustomer,String searchShop,String order,String sort,String conditions) throws Exception;
	public List<SaleTarget> selectSaleTargets(String datadate,String partyId) throws Exception;
	//渠道目标
	public List<SaleTarget> selectChannelTargets(String datadate,String partyId) throws Exception;
	//办事处目标
	public List<SaleTarget> selectOfficeTargets(String datadate,String partyId) throws Exception;
	//经营部目标
	public List<SaleTarget> selectRegionTargets(String datadate,String partyId) throws Exception;
	//分公司目标
	public List<SaleTarget> selectBranchTargets(String conditions,String datadate,String partyId) throws Exception;
	//导购员目标
//	public List<SaleTarget> selectPromoterTargets() throws Exception;
	//督导目标
	public List<SaleTarget> selectSupervisorTargets(String datadate,String partyId) throws Exception;
	//业务员目标
	public List<SaleTarget> selectSalesmanTargets(String datadate,String partyId) throws Exception;
	//业务经理目标
	public List<SaleTarget> selectBusinessTargets(String datadate,String partyId) throws Exception;
	//查找某个销售目标
	public SaleTarget getSaleTarget(Integer sid) throws Exception;
	
	//终端图片列表
	public Map<String, Object> selectPhotos(int start,int  limit,String searchStr,String order,String sort,String conditions) throws Exception;
	
	//终端样机设备列表
	public Map<String, Object> selectSampleDevices(int start,int  limit,String searchStr,String order,String sort,String conditions) throws Exception;
	
	//添加销售目标
	public void addSaleTarget(SaleTarget saleTarget) throws Exception;
	
	//编辑销售目标
	public void editSaleTarget(SaleTarget saleTarget) throws Exception;
	
	//编辑销售数据
	public void editSale(Sale sale) throws Exception;
	
	//导出销售数据
	public HSSFWorkbook exporSale(String conditions,String searchStr,String[] excelHeader,String title) throws Exception;
	
	public HSSFWorkbook exporCountrySale(String conditions,String searchStr,String[] excelHeader,String title) throws Exception;

	public HSSFWorkbook exporCustomerSale(String conditions,String searchStr,String[] excelHeader,String title) throws Exception;

	//导出样机
	public HSSFWorkbook exporSamples(String conditions,String[] excelHeader,String title) throws Exception;
	
	//删除销售目标
	public void deleteSaleTarget(SaleTarget saleTarget) throws Exception;
	
	//删除销售数据
	public void deleteSale(Sale sale) throws Exception;
	
	public int validationShopId(String shopId) throws Exception;
	
	//销售图表数据
	public List<Sale> getSaleCategoryData(String searchStr,String conditions,String searchStr1) throws Exception;
	
	//完成进度
	public int getSaleCompletionList(String conditions,String searchStr) throws Exception;
	
	public int getSaleTargetCompletionList(String conditionsToSaleTarget) throws Exception;
	
	//尺寸占比
	public List<Sale> getProductSizeSaleList(String conditions,String searchStr)  throws Exception;
	
	
	
	//导入
	public void importSale(List<Sale> sales) throws Exception;
			
	public String readExcel(File file, String fileName,String targetType) throws IOException;

	public String read2007Excel(File file,String targetType) throws IOException;

	public String read2003Excel(File file,String targetType) throws IOException;
	
	
	//AC销售目标
	public List<SaleTarget> selectACSaleTargets(String datadate,String partyId) throws Exception;
	//渠道目标
	public List<SaleTarget> selectACChannelTargets(String datadate,String partyId) throws Exception;
	//办事处目标
	public List<SaleTarget> selectACOfficeTargets(String datadate,String partyId) throws Exception;
	//经营部目标
	public List<SaleTarget> selectACRegionTargets(String datadate,String partyId) throws Exception;
	//分公司目标
	public List<SaleTarget> selectACBranchTargets(String conditions,String datadate,String partyId) throws Exception;
	//导购员目标
//	public List<SaleTarget> selectPromoterTargets() throws Exception;
	//督导目标
	public List<SaleTarget> selectACSupervisorTargets(String datadate,String partyId) throws Exception;
	//业务员目标
	public List<SaleTarget> selectACSalesmanTargets(String datadate,String partyId) throws Exception;
	//业务经理目标
	public List<SaleTarget> selectACBusinessTargets(String datadate,String partyId) throws Exception;
	
	public String readACExcel(File file, String fileName,String targetType) throws IOException;
	
	public String read2007ACExcel(File file,String targetType) throws IOException;

	public String read2003ACExcel(File file,String targetType) throws IOException;
	
	public String selectSOType(String countryId) throws Exception;
	
	public List<SaleTarget> getChannelTarget(String datadate,String partyId) throws Exception;
	
	public List<SaleTarget> getBranchTarget(String datadate,String partyId) throws Exception;
	
	public List<SaleTarget> getSupervisorTarget(String datadate,String partyId) throws Exception;
	
	public List<SaleTarget> getSalemanTarget(String datadate,String partyId) throws Exception;
	
	public List<SaleTarget> getBusinessTarget(String datadate,String partyId) throws Exception;
	
	public List<SaleTarget> getBranchTargetList(String datadate,String partyId) throws Exception;
	
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
	
	//获取渠道分公司目标数据 （AC）
	public List<SaleTarget> getACBranchTagetList(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
	
	//登陆总部用户显示分公司目标数据(TV)
	public List<SaleTarget> getOBCTVBranchTarget(@Param("datadata")String datadate,@Param("partyId")String partyId) throws Exception;
	
	//登陆总部用户显示分公司目标数据（AC）
	public List<SaleTarget> getOBCACBranchTarget(@Param("datadate")String datadate,@Param("partyId")String partyId) throws Exception;
}
