package cn.tcl.platform.excel.dao;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import cn.tcl.platform.excel.vo.Excel;

public interface IExportExcelDao {
	
	
	
	public List<Excel> selectDatas(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions) throws Exception;

	public List<HashMap<String, Object>> selectModelList(
			@Param(value = "date") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectModel(
			@Param(value = "date") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>> selectModelTotal(
			@Param(value = "date") String date,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	public List<HashMap<String, Object>>  selectSaleDataByshop(
			@Param(value = "date") String date,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq
		);

	public List<HashMap<String, Object>>  selectSaleDataByshopByAc(
			@Param(value = "date") String date,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq
		);
	
	
	public List<HashMap<String, Object>> selectStockBymodel(
			@Param(value = "spec") String spec,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "date") String date);

	
	public List<HashMap<String, Object>> selectModelBySpec(
			@Param(value = "date") String date,
			@Param(value = "spec") String spec,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	public List<HashMap<String, Object>> selectModelBySpecByAc(
			@Param(value = "date") String date,
			@Param(value = "spec") String spec,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	

	public List<HashMap<String, Object>> selectModelListBySpec(
			@Param(value = "spec") String spec,
			@Param(value = "date") String date,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	public List<HashMap<String, Object>> selectModelListBySpecByAc(
			@Param(value = "spec") String spec,
			@Param(value = "date") String date,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	
	public List<HashMap<String, Object>> selectModelBySpecTotal(
			@Param(value = "date") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	public List<HashMap<String, Object>> selectModelBySpecTotalByAc(
			@Param(value = "date") String endDate,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);
	
	
	
	

	public List<HashMap<String, Object>> selectModelTotalBySpec(
			@Param(value = "spec") String spec,
			@Param(value = "date") String date,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq);

	
	public List<HashMap<String, Object>> selectStockByTotal(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "date") String date);


	

	public List<HashMap<String, Object>> selectDisplayBymodel(
			@Param(value = "spec") String spec,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "date") String date);

	public List<HashMap<String, Object>> selectStockByData(
			@Param(value = "spec") String spec,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "date") String date);

	public List<HashMap<String, Object>> selectDisplayByData(
			@Param(value = "spec") String spec,
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "isHq") boolean isHq,
			@Param(value = "date") String date);

	public List<HashMap<String, Object>> selectDisPlayByTotal(
			@Param(value = "searchStr") String searchStr,
			@Param(value = "conditions") String conditions,
			@Param(value = "date") String date);
	

	public String selectPartyByUser(@Param(value = "userId") String userId);


	
}
