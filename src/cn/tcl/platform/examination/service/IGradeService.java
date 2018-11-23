package cn.tcl.platform.examination.service;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface IGradeService {
	
	/**
	 * 查看扫码考试数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> queryTempGreadData (Map<String,Object> map) throws Exception;

	public HSSFWorkbook exporSale(String getqueryGreadCondition, String[] excelHeader, String title, int start,
			int limit) throws Exception;
}
