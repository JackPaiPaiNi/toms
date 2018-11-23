package cn.tcl.platform.sellIn.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import cn.tcl.platform.sample.vo.SampleTarget;
import cn.tcl.platform.sellIn.vo.SellIn;

public interface ISellInDao {
	
	public int saveSellIn(List<SellIn> reddemCodeList) throws Exception;
	public LinkedList<SellIn> selectSellInByHq(Map<String,Object> sellInMap) throws Exception;

	public List<SampleTarget> selectModelByHq(@Param(value = "countryId")String countryId) throws Exception;
	public LinkedList<SellIn> selectSellInByHqModel(Map<String,Object> sellInMap) throws Exception;

	public LinkedList<SellIn> selectSellInByHqCountry(Map<String,Object> sellInMap) throws Exception;
	public SellIn selectCustomerCode(@Param(value = "customerCode")String customerCode,
									@Param(value = "countryId")String countryId) throws Exception;
	public SellIn selectCustomerModel(@Param(value = "channelModel")String channelModel,
			@Param(value = "countryId")String countryId,
			@Param(value = "customerId")String customerId) throws Exception;

	public int saveReturn(List<SellIn> reddemCodeList) throws Exception;
	
	public List<SellIn> selectSellInByTable(Map<String,Object> sellInMap) throws Exception;
	public List<SellIn> selectReturn(Map<String,Object> sellInMap) throws Exception;

	public int selectSellInByTableCount(Map<String,Object> sellInMap) throws Exception;
	public int selectReturnCount(Map<String,Object> sellInMap) throws Exception;
	
	public List<SellIn> selectStock(Map<String,Object> sellInMap) throws Exception;

	
	
	
}
