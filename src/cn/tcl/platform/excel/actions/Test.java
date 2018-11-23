package cn.tcl.platform.excel.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.excel.dao.ISoTableDao;
import cn.tcl.platform.excel.service.ISoTableService;

public class Test  extends BaseAction{
	@Autowired(required = false)
	@Qualifier("soTableService")
	private ISoTableService soTableService;
	
	public void importMIS() {
		try {

			XSSFWorkbook work = soTableService.read2007Excel(uploadExcel,
					uploadExcelFileName);

			response.setContentType("application/vnd.ms-excel");   
			response.setHeader("Content-Disposition", "attachment; filename=\"" + "System-"+uploadExcelFileName + "\"");
	        OutputStream ouputStream = response.getOutputStream();    
	        work.write(ouputStream);    
	        ouputStream.flush();    
	        ouputStream.close();   
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
	}
	
	
	
	
}
