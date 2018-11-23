package cn.tcl.platform.coefficient.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.coefficient.vo.Coefficient;
import cn.tcl.platform.excel.vo.Excel;
import cn.tcl.platform.sale.vo.Sale;

public interface ICoefficientService {
	//选择产品
	public Map<String, Object>  selectCoefficient(int start,
			int  limit,
			String searchStr,
			String order,
			String sort,
			String conditions) throws Exception;
	
	
	public int saveCoefficient(Coefficient coefficient) throws Exception;
	public int updateCoefficient(Coefficient coefficient) throws Exception;
	
	
	public int selectCoefficientFinally(String searchStr, String conditions) throws Exception;
	public int saveCoefficientFinally(Coefficient coefficient) throws Exception;

	


}
