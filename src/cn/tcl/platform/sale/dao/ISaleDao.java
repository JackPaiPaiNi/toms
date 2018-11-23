package cn.tcl.platform.sale.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.sale.vo.Sale;
import cn.tcl.platform.sale.vo.SaleTarget;

public interface ISaleDao {
	//选择产品
	public List<Sale> selectSales(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="order") String order,
			@Param(value="sort") String sort,
			@Param(value="conditions") String conditions) throws Exception;
	//总数计算
	public int countSales(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="conditions") String conditions) throws Exception;
	
	
	
	public List<Sale> selectCountrySales(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="order") String order,
			@Param(value="sort") String sort,
			@Param(value="conditions") String conditions) throws Exception;
	//总数计算
	public int countCountrySales(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="conditions") String conditions) throws Exception;
	
	
	
	public List<Sale> selectCustomerSales(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="order") String order,
			@Param(value="sort") String sort,
			@Param(value="conditions") String conditions) throws Exception;
	//总数计算
	public int countCustomerSales(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="conditions") String conditions) throws Exception;
	
	public List<Sale> selectSalesByMobile(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="order") String order,
			@Param(value="sort") String sort,
			@Param(value="conditions") String conditions) throws Exception;
	//总数计算
	public int countSalesByMobile(@Param(value="start") int start,
			@Param(value="limit") int  limit,
			@Param(value="searchStr") String searchStr,
			@Param(value="conditions") String conditions) throws Exception;
	//获取其中一个
	public Sale getSale(int sid);
	//保存销售数据
	public void saveSale(Sale sale) throws Exception;
	
	public void saveSales(Sale sale) throws Exception;
	
	public void updateSale(Sale sale) throws Exception;
	
	//删除销售数据
	public void deleteSale(Sale sale) throws Exception;
	
	//导出 所有销售数据
	public List<Sale> searchExporSale(@Param(value="conditions") String conditions,@Param(value="searchStr") String searchStr) throws Exception;
	
	public List<Sale> searchCountryExporSale(@Param(value="conditions") String conditions,@Param(value="searchStr") String searchStr) throws Exception;

	public List<Sale> searchCustomerExporSale(@Param(value="conditions") String conditions,@Param(value="searchStr") String searchStr) throws Exception;

	
	
	public List<Sale> getSaleCategoryData(@Param(value="searchStr") String searchStr,
			@Param(value="conditions") String conditions,
			@Param(value="searchStr1") String searchStr1) throws Exception;
	
	public List<Sale> getSaleCompletionList(@Param(value="conditions") String conditions,
			@Param(value="searchStr") String searchStr) throws Exception;

	public List<Sale> getProductSizeSaleList(@Param(value="conditions") String conditions,
			@Param(value="searchStr") String searchStr) throws Exception;
}
