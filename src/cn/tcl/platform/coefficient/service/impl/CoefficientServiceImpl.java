package cn.tcl.platform.coefficient.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.coefficient.dao.CoefficientDao;
import cn.tcl.platform.coefficient.service.ICoefficientService;
import cn.tcl.platform.coefficient.vo.Coefficient;
import cn.tcl.platform.excel.dao.IExcelDao;
import cn.tcl.platform.excel.service.IExcelService;
import cn.tcl.platform.excel.vo.Excel;
import cn.tcl.platform.product.vo.Product;
import cn.tcl.platform.sale.vo.Sale;

@Service("coefficientService")
public class CoefficientServiceImpl implements ICoefficientService {
	@Autowired
	private CoefficientDao coefficientDao;
	private Object productDao;

	@Override
	public Map<String, Object>  selectCoefficient(int start, int limit,
			String searchStr, String order, String sort, String conditions)
			throws Exception {
		List<Coefficient> list=coefficientDao.selectCoefficient(start, limit, searchStr, order, sort, conditions);
		Map<String, Object> map=new HashMap<String, Object>();
		int count=coefficientDao.countCoefficient(start, limit, searchStr, conditions);
		map.put("rows", list);
		map.put("total", count);
		
		return map;
	}



	@Override
	public int saveCoefficient(Coefficient coefficient) throws Exception {
		int one=coefficientDao.selectCoefficientEE(null," co.country="+coefficient.getCountry());
		if(one>0){
			int row=coefficientDao.updateCoefficientByUsing(coefficient);
			if(row>0){
				return coefficientDao.saveCoefficient(coefficient);

			}else{
				return 0;
			}
			
		}else{
			return coefficientDao.saveCoefficient(coefficient);
		}
		
	
	}

	@Override
	public int updateCoefficient(Coefficient coefficient) throws Exception {
		return coefficientDao.updateCoefficient(coefficient);
		
	}






	@Override
	public int saveCoefficientFinally(Coefficient coefficient) throws Exception {
		return coefficientDao.saveCoefficientFinally(coefficient);
	}



	@Override
	public int selectCoefficientFinally(String searchStr, String conditions)
			throws Exception {
		return coefficientDao.selectCoefficientFinally(searchStr, conditions);
	}



}
