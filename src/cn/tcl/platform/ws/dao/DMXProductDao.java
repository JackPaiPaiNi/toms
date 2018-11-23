package cn.tcl.platform.ws.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.ws.vo.DMXProduct;

public interface DMXProductDao {
	//插入未维护的世界电子产品数据
	public int InsertUnmaintenedModel(List<DMXProduct> dpLst) throws Exception;
	
	//根据产品model查询未维护的产品数据
	public List<DMXProduct> getDMXProductByModel(@Param("model")String model) throws Exception;
}
