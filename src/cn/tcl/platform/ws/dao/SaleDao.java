package cn.tcl.platform.ws.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.sale.vo.Sale;

public interface SaleDao {
	//获取销售数据列表
	public List<Sale> selectSaleList(Sale sale) throws Exception;
	//批量插入销售数据
	public int InsertSaleDataBatch(List<Sale> saleList) throws Exception;
	//批量更新销售数据
	public int UpdateSaleDataBatch(List<Sale> saleList) throws Exception;
}
