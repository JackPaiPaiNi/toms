package cn.tcl.platform.excel.service;

import java.util.HashMap;
import java.util.List;
import cn.tcl.platform.excel.vo.Excel;

public interface IExportExcelService {
	// 查看列表
	public List<Excel> selectDatas(String searchStr, String conditions)
			throws Exception;


		public List<HashMap<String, Object>> selectModelList(String date, String searchStr, String conditions, boolean isHq)
				throws Exception;

		public List<HashMap<String, Object>> selectModel(String endDate, String searchStr, String conditions, boolean isHq)
				throws Exception;

		public List<HashMap<String, Object>> selectModelTotal(String date, String searchStr, String conditions, boolean isHq)
				throws Exception;


		public  List<HashMap<String, Object>>  selectSaleDataByshop( String date, String searchStr, String conditions, boolean isHq);
		
		public  List<HashMap<String, Object>>  selectSaleDataByshopByAc( String date, String searchStr, String conditions, boolean isHq);

		public List<HashMap<String, Object>> selectStockBymodel(String spec,
				String searchStr, String conditions, boolean isHq,String date) throws Exception;

		

		public List<HashMap<String, Object>> selectModelBySpec(String date,String spec,
				String searchStr, String conditions, boolean isHq) throws Exception;
		

		public List<HashMap<String, Object>> selectModelBySpecByAc(String date,String spec,
				String searchStr, String conditions, boolean isHq) throws Exception;
		
		

		public List<HashMap<String, Object>> selectModelListBySpec(String spec,
				String date, String searchStr,
				String conditions, boolean isHq) throws Exception;
		
		public List<HashMap<String, Object>> selectModelListBySpecByAc(String spec,
				String date, String searchStr,
				String conditions, boolean isHq) throws Exception;
		
		

		public List<HashMap<String, Object>> selectModelBySpecTotal(
				String date, String searchStr,
				String conditions, boolean isHq) throws Exception;

		public List<HashMap<String, Object>> selectModelBySpecTotalByAc(
				String date, String searchStr,
				String conditions, boolean isHq) throws Exception;

		
		
		
		public List<HashMap<String, Object>> selectModelTotalBySpec(String spec,
				String date, String searchStr,
				String conditions, boolean isHq) throws Exception;

		public List<HashMap<String, Object>> selectStockByTotal(String searchStr,
				String conditions,String date) throws Exception;


		public List<HashMap<String, Object>> selectDisplayBymodel(String spec,
				String searchStr, String conditions, boolean isHq,String date) throws Exception;

		public List<HashMap<String, Object>> selectStockByData(String spec,
				String searchStr, String conditions, boolean isHq,String date) throws Exception;

		public List<HashMap<String, Object>> selectDisplayByData(String spec,
				String searchStr, String conditions, boolean isHq,String date) throws Exception;

		public List<HashMap<String, Object>> selectDisPlayByTotal(String searchStr,
				String conditions,String date) throws Exception;

	
		
		public String selectPartyByUser(
				String userId) throws Exception;
		
		
		
}
