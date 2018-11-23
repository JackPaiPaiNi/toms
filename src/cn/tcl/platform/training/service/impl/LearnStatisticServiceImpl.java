package cn.tcl.platform.training.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.platform.training.dao.LearnStatisticsDao;
import cn.tcl.platform.training.service.LearnStatisticsService;
import cn.tcl.platform.training.vo.LearnStatistics;

@Service("LearnService")
public class LearnStatisticServiceImpl implements LearnStatisticsService{
	
	@Autowired
	private LearnStatisticsDao learnDao;
	
	public Map<String, Object> selectLearnList(int start, int limit,
			String order, String sort,String conditions,String levelOneTypeId,String levelTwoTypeId,String levelThreeTypeId,String regionId,String countryId,String startdate,String enddate) throws Exception {
		String _levelOneTypeId=levelOneTypeId==""?null:levelOneTypeId;
		String _levelTwoTypeId=levelTwoTypeId==""?null:levelTwoTypeId;
		String _levelThreeTypeId=levelThreeTypeId==""?null:levelThreeTypeId;
		String _regionId=regionId==""?null:regionId;
		String _countryId=countryId==""?null:countryId;
		String _startdate=startdate==""?null:startdate;
		String _enddate=enddate==""?null:enddate;
		
		List<LearnStatistics> learnList = learnDao.selectLearnList(start, limit, order, sort,conditions,_levelOneTypeId,_levelTwoTypeId,_levelThreeTypeId,_regionId,_countryId,_startdate,_enddate);
		Map<String,Object> map = new HashMap<String, Object>();
		int count = learnDao.countLearn(start, limit, order, sort,conditions,_levelOneTypeId,_levelTwoTypeId,_levelThreeTypeId,_regionId,_countryId,_startdate,_enddate);
		map.put("rows", learnList);
		map.put("total", count);
		return map;
	}

	
	@Override
	public XSSFWorkbook exportLearn(String conditions, String[] excelHeader,
			String title,String levelOneTypeId,String levelTwoTypeId,String levelThreeTypeId,String regionId,String countryId,String startdate,String enddate) throws Exception {
		String _levelOneTypeId=levelOneTypeId==""?null:levelOneTypeId;
		//先查询出要导出的内容
		List<LearnStatistics> exportLearnList = learnDao.searchExportLearn(conditions, _levelOneTypeId, levelTwoTypeId, levelThreeTypeId, regionId, countryId, startdate, enddate);
		//设置 表头宽度
		int[] excelWidth = {120,120,120,120,180,120,200,300,130,120,120,200,120};
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(title);
		XSSFRow row = sheet.createRow(0);
		
		 //导出字体样式
		 XSSFFont font = workbook.createFont();
		 font.setFontHeightInPoints((short) 12); // 字体大小
		 
		 //导出样式
         XSSFCellStyle style = workbook.createCellStyle();
         style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         style.setFont(font);
         
         for (int i = 0; i < excelWidth.length; i++) {  
			    sheet.setColumnWidth(i, 32 * excelWidth[i]);  
		 } 
         
   
         //表头数据
         for (int i = 0; i < excelHeader.length; i++) {   
             XSSFCell cell = row.createCell(i);    
             cell.setCellValue(excelHeader[i]);    
             cell.setCellStyle(style);  
             
         } 
         
         //表体数据
         for (int i = 0; i < exportLearnList.size(); i++) {
			row = sheet.createRow(i+1);
			LearnStatistics learn = exportLearnList.get(i);
			
			//-----------------单元格数据----------------
	         /**
	          * 国家
	          */
			XSSFCell cell0 = row.createCell(0);
			cell0.setCellValue(learn.getPartyName());
			cell0.setCellStyle(style);
			
			/**
			 * 用户帐号
			 */
			XSSFCell cell1 = row.createCell(1);
			cell1.setCellValue(learn.getUserLogin());
			cell1.setCellStyle(style);
			
			/**
			 * 用户名
			 */
			XSSFCell cell2 = row.createCell(2);
			cell2.setCellValue(learn.getUserName());
			cell2.setCellStyle(style);
			
			/**
			 * 微信
			 */
			XSSFCell cell3 = row.createCell(3);
			cell3.setCellValue(learn.getUserWC());
			cell3.setCellStyle(style);
			
			/**
			 * 职位
			 */
			XSSFCell cell4 = row.createCell(4);
			cell4.setCellValue(learn.getRoleName());
			cell4.setCellStyle(style);
			
			/**
			 * 员工号
			 */
			XSSFCell cell5 = row.createCell(5);
			cell5.setCellValue(learn.getUserWorkNum());
			cell5.setCellStyle(style);
			
			/**
			 * 访问时间
			 */
			XSSFCell cell6 = row.createCell(6);
			cell6.setCellValue(learn.getTime());
			cell6.setCellStyle(style);
			
			/**
			 * 阅读时长
			 */
			XSSFCell cell7 = row.createCell(7);
			cell7.setCellValue(learn.getOnlineTime());
			cell7.setCellStyle(style);
			
			/**
			 * 课程标题
			 */
			XSSFCell cell8 = row.createCell(8);
			cell8.setCellValue(learn.getCourseTittle());
			cell8.setCellStyle(style);
			
			
			/**
			 * 分数
			 */
			XSSFCell cell9 = row.createCell(9);
			cell9.setCellValue(learn.getOperationScore());
			cell9.setCellStyle(style);
			
			
			/**
			 * 应阅读人数
			 */
			XSSFCell cell10 = row.createCell(10);
			cell10.setCellValue(learn.getIsRead());
			cell10.setCellStyle(style);
			
			/**			
			 * 实际阅读人数
			 */
			XSSFCell cell11 = row.createCell(11);
			cell11.setCellValue(learn.getCont());
			cell11.setCellStyle(style);
			
			
			/**
			 * 学习率
			 */
			XSSFCell cell12 = row.createCell(12);
			cell12.setCellValue(learn.getPercent());
			cell12.setCellStyle(style);
		}         
         
		return workbook;
	}


	@Override
	public Map<String, Object> selectRussiaLearnList(int start, int limit,
			String order, String sort, String conditions,
			String levelOneTypeId, String levelTwoTypeId,
			String levelThreeTypeId, String regionId, String countryId,
			String startdate, String enddate) throws Exception {
		String _levelOneTypeId=levelOneTypeId==""?null:levelOneTypeId;
		String _levelTwoTypeId=levelTwoTypeId==""?null:levelTwoTypeId;
		String _levelThreeTypeId=levelThreeTypeId==""?null:levelThreeTypeId;
		String _regionId=regionId==""?null:regionId;
		String _countryId=countryId==""?null:countryId;
		String _startdate=startdate==""?null:startdate;
		String _enddate=enddate==""?null:enddate;
		
		List<LearnStatistics> learnList = learnDao.selectRussiaLearnList(start, limit, order, sort,conditions,_levelOneTypeId,_levelTwoTypeId,_levelThreeTypeId,_regionId,_countryId,_startdate,_enddate);
		Map<String,Object> map = new HashMap<String, Object>();
		int count = learnDao.countLearn(start, limit, order, sort,conditions,_levelOneTypeId,_levelTwoTypeId,_levelThreeTypeId,_regionId,_countryId,_startdate,_enddate);
		map.put("rows", learnList);
		map.put("total", count);
		return map;
	}


	@Override
	public XSSFWorkbook exportRusLearn(String conditions, String[] excelHeader,
			String title, String levelOneTypeId, String levelTwoTypeId,
			String levelThreeTypeId, String regionId, String countryId,
			String startdate, String enddate) throws Exception {
		String _levelOneTypeId=levelOneTypeId==""?null:levelOneTypeId;
		//先查询出要导出的内容
		List<LearnStatistics> exportLearnList = learnDao.searchRusExportLearn(conditions, _levelOneTypeId, levelTwoTypeId, 
				levelThreeTypeId, regionId, countryId, startdate, enddate);
		//设置 表头宽度
		int[] excelWidth = {120,120,120,120,180,120,200,300,130,120,120,200,120};
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(title);
	    XSSFRow row = sheet.createRow(0);
		
		 //导出字体样式
		 XSSFFont font = workbook.createFont();
		 font.setFontHeightInPoints((short) 12); // 字体大小
		 
		 //导出样式
         XSSFCellStyle style = workbook.createCellStyle();
         style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         style.setFont(font);
         
         for (int i = 0; i < excelWidth.length; i++) {  
			    sheet.setColumnWidth(i, 32 * excelWidth[i]);  
		 } 
         
         //表头数据
         for (int i = 0; i < excelHeader.length; i++) {   
             XSSFCell cell = row.createCell(i);    
             cell.setCellValue(excelHeader[i]);    
             cell.setCellStyle(style);  
             
         } 
         
         //表体数据
         for (int i = 0; i < exportLearnList.size(); i++) {
			row = sheet.createRow(i+1);
			LearnStatistics learn = exportLearnList.get(i);
			
			//-----------------单元格数据----------------
	         /**
	          * 国家
	          */
			XSSFCell cell0 = row.createCell(0);
			cell0.setCellValue(learn.getPartyName());
			cell0.setCellStyle(style);
			
			/**
			 * 用户帐号
			 */
			XSSFCell cell1 = row.createCell(1);
			cell1.setCellValue(learn.getUserLogin());
			cell1.setCellStyle(style);
			
			/**
			 * 用户名
			 */
			XSSFCell cell2 = row.createCell(2);
			cell2.setCellValue(learn.getUserName());
			cell2.setCellStyle(style);
			
			/**
			 * 微信
			 */
			XSSFCell cell3 = row.createCell(3);
			cell3.setCellValue(learn.getUserWC());
			cell3.setCellStyle(style);
			
			/**
			 * 渠道名
			 */
			XSSFCell cell4 = row.createCell(4);
			cell4.setCellValue(learn.getCustomerName());
			cell4.setCellStyle(style);
			
			/**
			 * 职位
			 */
			XSSFCell cell5 = row.createCell(5);
			cell5.setCellValue(learn.getRoleName());
			cell5.setCellStyle(style);
			
			/**
			 * 员工号
			 */
			XSSFCell cell6 = row.createCell(6);
			cell6.setCellValue(learn.getUserWorkNum());
			cell6.setCellStyle(style);
			
			/**
			 * 访问时间
			 */
			XSSFCell cell7 = row.createCell(7);
			cell7.setCellValue(learn.getTime());
			cell7.setCellStyle(style);
			
			/**
			 * 阅读时长
			 */
			XSSFCell cell8 = row.createCell(8);
			cell8.setCellValue(learn.getOnlineTime());
			cell8.setCellStyle(style);
			
			/**
			 * 课程标题
			 */
			XSSFCell cell9 = row.createCell(9);
			cell9.setCellValue(learn.getCourseTittle());
			cell9.setCellStyle(style);
			
			
			/**
			 * 分数
			 */
			XSSFCell cell10 = row.createCell(10);
			cell10.setCellValue(learn.getOperationScore());
			cell10.setCellStyle(style);
			
			
			/**
			 * 应阅读人数
			 */
			XSSFCell cell11 = row.createCell(11);
			cell11.setCellValue(learn.getIsRead());
			cell11.setCellStyle(style);
			
			/**			
			 * 实际阅读人数
			 */
			XSSFCell cell12 = row.createCell(12);
			cell12.setCellValue(learn.getCont());
			cell12.setCellStyle(style);
			
			
			/**
			 * 学习率
			 */
			XSSFCell cell13 = row.createCell(13);
			cell13.setCellValue(learn.getPercent());
			cell13.setCellStyle(style);
		}         
         
		return workbook;
	}	
}
