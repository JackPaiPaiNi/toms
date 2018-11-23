package cn.tcl.platform.examination.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.platform.examination.dao.IGradeDao;
import cn.tcl.platform.examination.service.IGradeService;
import cn.tcl.platform.examination.vo.Analysis;
import cn.tcl.platform.examination.vo.Grade;
import net.sf.json.JSONObject;

@Service("grService")
public class GradeServiceImpl implements IGradeService{
	
	@Autowired
	private IGradeDao grDao;
	
	/**
	 * 查看扫码考试数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> queryTempGreadData(Map<String, Object> map) throws Exception {
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("rows", grDao.queryTempGreadData(map));
		m.put("total", grDao.queryTempGreadCount(map));
		return m;
	}
	
	/**
	 * 扫码考试数据导出
	 */
	@Override
	public HSSFWorkbook exporSale(String condition, String[] excelHeader, String title, int start,
			int limit) throws Exception {
		
		List<Grade> grList = null;
		if(start > -1){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("start", start);
			map.put("limit", limit);
			map.put("condition", condition);
			grList = grDao.exportQueryTempGreadData(map);
		}
		
		int[] excelWidth = {120,120,120,120,130,120,200,130,130,120};
		
		 HSSFWorkbook workbook = new HSSFWorkbook();
		 HSSFSheet sheet = workbook.createSheet(title);
		 HSSFRow headerRow = sheet.createRow(0);
		 
		 HSSFFont font = workbook.createFont();
		 
         HSSFCellStyle style = workbook.createCellStyle();    
         style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         
         style.setFont(font);
		 
         
		 sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, excelHeader.length - 1)); 
		 
		 HSSFCell headerCell = headerRow.createCell(0);
		 headerCell.setCellValue(new HSSFRichTextString(title));
		 headerCell.setCellStyle(style);
		 
         //--------------------------------------------------------------
		 for (int i = 0; i < excelWidth.length; i++) {  
			    sheet.setColumnWidth(i, 32 * excelWidth[i]);  
		 } 
		 
         HSSFRow row = sheet.createRow(1);
         //琛ㄥご鏁版嵁
         for (int i = 0; i < excelHeader.length; i++) {   
             HSSFCell cell = row.createCell(i);    
             cell.setCellValue(excelHeader[i]);    
             cell.setCellStyle(style);  
             
         }   
         
         if(grList != null && grList.size() != 0){
	         //琛ㄤ綋鏁版嵁
	         for (int i = 0; i < grList.size(); i++) {    
	             row = sheet.createRow(i + 2);    
	             Grade an = grList.get(i);
	             
	             //-------------鍗曞厓鏍�-------------------
	             
	             /**
	              * 考试用户
	              */
	             HSSFCell cell0 = row.createCell(0);
	             cell0.setCellValue(an.getUserName());    
	             cell0.setCellStyle(style);
	             /**
	              * 用户所属国家
	              */
	             HSSFCell cell1 = row.createCell(1);
	             cell1.setCellValue(an.getPartyName());    
	             cell1.setCellStyle(style);
	             /**
	              * 区域
	              */
	             HSSFCell cell2 = row.createCell(2);
	             cell2.setCellValue(an.getRegionName());    
	             cell2.setCellStyle(style);
	             /**
	              * 办事处
	              */
	             HSSFCell cell3 = row.createCell(3);
	             cell3.setCellValue(an.getOfficeName());    
	             cell3.setCellStyle(style);
	             
	             /**
	              * 试卷标题
	              */
	             HSSFCell cell4 = row.createCell(4);
	             cell4.setCellValue(an.getPaperHead());
	             cell4.setCellStyle(style);
	             
	             /**
	              * 考试得分
	              */
	             HSSFCell cell5 = row.createCell(5);
	             cell5.setCellValue(an.getGrade());    
	             cell5.setCellStyle(style);
	             
	             /**
	              * 提交时间
	              */
	             HSSFCell cell6 = row.createCell(6);
	             cell6.setCellValue(an.getSubTime());    
	             cell6.setCellStyle(style);
	         }    
         }
         return workbook;
	}
}
