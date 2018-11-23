package cn.tcl.platform.ws.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.ws.vo.DMXShop;

public interface DMXShopDao {
	//插入未维护的世界电子门店数据
	public int InsertUnMaintainedShopBatch(List<DMXShop> lst) throws Exception;
	//根据门店名称查询DMX未维护的门店
	public List<DMXShop> GetShopByName(@Param("shopName")String shopName) throws Exception;
}
