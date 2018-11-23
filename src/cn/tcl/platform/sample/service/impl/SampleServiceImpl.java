package cn.tcl.platform.sample.service.impl;

import java.util.ArrayList;
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

import cn.tcl.platform.sample.dao.SampleDao;
import cn.tcl.platform.sample.service.ISampleService;
import cn.tcl.platform.sample.vo.Sample;

@Service("sampleService")
public class SampleServiceImpl implements ISampleService{
	
	@Autowired
	private SampleDao sampleDao;
	
	/**
	 * ��ѯ�û������ŵ�
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Sample> selectShopId(String userId) throws Exception {
		return sampleDao.selectShopId(userId);
	}
	
	/**
	 * ��ѯ��Դ��
	 * @param sampleMap
	 * @return
	 * @throws ExcepEFFICIENCYtion
	 */
	@Override
	public List<Sample> selectSampleList(Map<String, Object> sampleMap) throws Exception {
		
		List<Sample> shopIdList = sampleDao.selectShopId((String) sampleMap.get("userId"));
		List<Integer> shopIdlis = new ArrayList<Integer>();
		
		for(int i=0;i<shopIdList.size();i++){
			shopIdlis.add(shopIdList.get(i).getShopId());
			/*shopIdlis.add(i);*/
		}
		
		sampleMap.put("ids", shopIdlis);
		return sampleDao.selectSampleList(sampleMap);
	}
	
	/**
	 * ��ѯ��Դ����
	 * @param sampleMap
	 * @return
	 * @throws Exception
	 */
	@Override
	public Integer selectSampleListCount(Map<String, Object> sampleMap) throws Exception {
		return sampleDao.selectSampleListCount(sampleMap);
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public HSSFWorkbook exporSamples(String conditions,String[] excelHeader,String title ,Map<String, Object> samMap) throws Exception {
		//�Ȳ�ѯ�� ��������������

		List<Sample> shopIdList = sampleDao.selectShopId((String) samMap.get("userId"));
		List<Integer> shopIdlis = new ArrayList<Integer>();
		
		for(int i=0;i<shopIdList.size();i++){
			shopIdlis.add(shopIdList.get(i).getShopId());
			/*shopIdlis.add(i);*/
		}
		
		samMap.put("ids", shopIdlis);
		
		List<Sample> exporSampleList = sampleDao.selectSampleList(samMap);
		//���� ��ͷ���
		int[] excelWidth = {120,120,120,120,120,130,200,130,250};
		
		 HSSFWorkbook workbook = new HSSFWorkbook();
		 HSSFSheet sheet = workbook.createSheet(title);
		 HSSFRow headerRow = sheet.createRow(0);
		 
		 //����������ʽ
		 HSSFFont font = workbook.createFont();
		 
		 //������ʽ
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
         //��ͷ����
         for (int i = 0; i < excelHeader.length; i++) {   
             HSSFCell cell = row.createCell(i);    
             cell.setCellValue(excelHeader[i]);    
             cell.setCellStyle(style);  
             
         }   
         
         //��������
         for (int i = 0; i < exporSampleList.size(); i++) {    
             row = sheet.createRow(i + 2);    
            Sample sample = exporSampleList.get(i);
             
             //-------------��Ԫ��-------------------
             
           
             
             /**
              * ��֧����
              */
             HSSFCell cell0 = row.createCell(0);
             cell0.setCellValue(sample.getBranch());    
             cell0.setCellStyle(style);
             
             /**
              * �ͻ�
              */
             HSSFCell cell1 = row.createCell(1);
             cell1.setCellValue(sample.getCustomer());    
             cell1.setCellStyle(style);
             
            /**
              * �ŵ�
              */
             HSSFCell cell2 = row.createCell(2);
             cell2.setCellValue(sample.getShopName());    
             cell2.setCellStyle(style);
             
             /**
              * �ܲ��ͺ�
              */
             HSSFCell cell3 = row.createCell(3);
             cell3.setCellValue(sample.getHqModel());    
             cell3.setCellStyle(style);
             
             /**
              * �ֹ�˾�ͺ�
              */
             HSSFCell cell4 = row.createCell(4);
             cell4.setCellValue(sample.getModel());    
             cell4.setCellStyle(style);
             
             /**
              * ����
              */
             HSSFCell cell5 = row.createCell(5);
             cell5.setCellValue(sample.getQuantity());    
             cell5.setCellStyle(style);
             
             /**
              * �ϴ���
              */
             HSSFCell cell6 = row.createCell(6);
             cell6.setCellValue(sample.getUserId());    
             cell6.setCellStyle(style);
             
             /*
               ** ����
              */
             HSSFCell cell7 = row.createCell(7);
             cell7.setCellValue(sample.getDatadate());    
             cell7.setCellStyle(style);
             
         }    
         
         return workbook;
	}


}
