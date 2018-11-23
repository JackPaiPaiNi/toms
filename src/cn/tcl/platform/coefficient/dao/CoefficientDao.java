package cn.tcl.platform.coefficient.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import cn.tcl.platform.coefficient.vo.Coefficient;
import cn.tcl.platform.excel.vo.Excel;
import cn.tcl.platform.sale.vo.Sale;

public interface CoefficientDao {
	  
	  
		//选择产品
		public List<Coefficient> selectCoefficient(@Param(value="start") int start,
				@Param(value="limit") int  limit,
				@Param(value="searchStr") String searchStr,
				@Param(value="order") String order,
				@Param(value="sort") String sort,
				@Param(value="conditions") String conditions) throws Exception;
		
		
		//选择产品
				public int selectCoefficientEE(@Param(value="searchStr") String searchStr,
						@Param(value="conditions") String conditions) throws Exception;
				
		//总数计算
		public int countCoefficient(@Param(value="start") int start,
				@Param(value="limit") int  limit,
				@Param(value="searchStr") String searchStr,
				@Param(value="conditions") String conditions) throws Exception;
		
		public int saveCoefficient(Coefficient coefficient) throws Exception;
		public int updateCoefficient(Coefficient coefficient) throws Exception;

		public int updateCoefficientByUsing(Coefficient coefficient) throws Exception;

		public int selectCoefficientFinally(@Param(value="searchStr") String searchStr,@Param(value="conditions") String conditions) throws Exception;

		
		public int saveCoefficientFinally(Coefficient coefficient) throws Exception;

}
