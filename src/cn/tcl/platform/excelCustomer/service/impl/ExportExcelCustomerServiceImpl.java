package cn.tcl.platform.excelCustomer.service.impl;


import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.platform.excel.vo.Excel;
import cn.tcl.platform.excelCustomer.dao.IExportExcelCustomerDao;
import cn.tcl.platform.excelCustomer.service.IExportExcelCustomerService;
import cn.tcl.platform.excelCustomer.vo.ExcelCustomer;
@Service("exportExcelCustomerService")
public class ExportExcelCustomerServiceImpl implements IExportExcelCustomerService{
	@Autowired
	private IExportExcelCustomerDao exportExcelDao;

	@Override
	public  List<ExcelCustomer> selectDatas(String searchStr, String conditions)
			throws Exception {
		return exportExcelDao.selectDatas(searchStr, conditions);
	}

	
	

	@Override
	public List<HashMap<String, Object>> selectSaleDataByshop(String date,String searchStr, String conditions, boolean isHq) {
		return exportExcelDao.selectSaleDataByshop(date, searchStr, conditions,isHq);
	}


	
	@Override
	public  List<HashMap<String,Object>> selectModel(String date,String searchStr, String conditions, boolean isHq) throws Exception {
		return exportExcelDao.selectModel(date,searchStr, conditions,isHq);
	}



	@Override
	public List<HashMap<String, Object>> selectModelTotal(String date, String searchStr, String conditions, boolean isHq)
			throws Exception {
		return exportExcelDao.selectModelTotal(date,searchStr, conditions,isHq);
	}

	

	@Override
	public List<HashMap<String, Object>> selectModelBySpec(String date,String spec,String searchStr,
			String conditions, boolean isHq) throws Exception {
		return exportExcelDao.selectModelBySpec(date,spec,searchStr, conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectModelListBySpec(String spec,
			String date, String searchStr,
			String conditions, boolean isHq) throws Exception {
		return exportExcelDao.selectModelListBySpec(spec,date, searchStr, conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectStockBymodel(String spec,
			String searchStr, String conditions, String date,boolean isHq) throws Exception {
		return exportExcelDao.selectStockBymodel(spec, searchStr, conditions,date,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectModelBySpecTotal(
			String date, String searchStr,
			String conditions, boolean isHq) throws Exception {
		return exportExcelDao.selectModelBySpecTotal(date, searchStr, conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectModelTotalBySpec(String spec,
			String date, String searchStr,
			String conditions, boolean isHq) throws Exception {
		return exportExcelDao.selectModelTotalBySpec(spec,date, searchStr, conditions,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectStockByTotal(String searchStr,
			String conditions,String date) throws Exception {
		return exportExcelDao.selectStockByTotal(searchStr, conditions,date);
	}

	

	@Override
	public List<HashMap<String, Object>> selectDisplayBymodel(String spec,
			String searchStr, String conditions,String date, boolean isHq) throws Exception {
		return exportExcelDao.selectDisplayBymodel(spec, searchStr, conditions, date,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectDisPlayByTotal(String searchStr,
			String conditions,String date) {
		return exportExcelDao.selectDisPlayByTotal(searchStr, conditions,date);
	}


	
	@Override
	public List<HashMap<String, Object>> selectStockByData(String spec,
			String searchStr, String conditions,String date, boolean isHq) throws Exception {
		return exportExcelDao.selectStockByData(spec, searchStr, conditions,date,isHq);
	}

	@Override
	public List<HashMap<String, Object>> selectDisplayByData(String spec,
			String searchStr, String conditions,String date, boolean isHq) throws Exception {
		return exportExcelDao.selectDisplayByData(spec, searchStr, conditions,date,isHq);
	}




	@Override
	public List<HashMap<String, Object>> selectModelList(String date, String searchStr, String conditions, boolean isHq)
			throws Exception {
		return exportExcelDao.selectModelList(date,searchStr, conditions,isHq);
	}




	@Override
	public String selectPartyByUser(String userId) throws Exception {
		return exportExcelDao.selectPartyByUser(userId);
	}




	@Override
	public List<HashMap<String, Object>> selectSaleDataByshopByAc(String date,
			String searchStr, String conditions, boolean isHq) {
		return exportExcelDao.selectSaleDataByshopByAc(date, searchStr, conditions, isHq);
	}




	@Override
	public List<HashMap<String, Object>> selectModelBySpecByAc(String date,
			String spec, String searchStr, String conditions, boolean isHq)
			throws Exception {
		return exportExcelDao.selectModelBySpecByAc(date, spec, searchStr, conditions, isHq);
	}




	@Override
	public List<HashMap<String, Object>> selectModelListBySpecByAc(String spec,
			String date, String searchStr, String conditions, boolean isHq)
			throws Exception {
		return exportExcelDao.selectModelListBySpecByAc(spec, date, searchStr, conditions, isHq);
	}




	@Override
	public List<HashMap<String, Object>> selectModelBySpecTotalByAc(
			String date, String searchStr, String conditions, boolean isHq)
			throws Exception {
		return exportExcelDao.selectModelBySpecTotalByAc(date, searchStr, conditions, isHq);
	}


 
	 
	
	
	 
	



	
}
