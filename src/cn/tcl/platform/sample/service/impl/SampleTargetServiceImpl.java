package cn.tcl.platform.sample.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.excel.dao.ImportExcelDao;
import cn.tcl.platform.modelmap.dao.IModelMapDao;
import cn.tcl.platform.sample.dao.SampleTargetDao;
import cn.tcl.platform.sample.service.ISampleTargetService;
import cn.tcl.platform.sample.vo.SampleTarget;
import cn.tcl.platform.sellIn.dao.ISellInDao;
import cn.tcl.platform.sellIn.vo.SellIn;
import cn.tcl.platform.shop.dao.IShopDao;
import cn.tcl.platform.shop.vo.Shop;
import cn.tcl.platform.shop.vo.ShopParty;
/**
 * 上样目标实现类
 * @author Fay
 *
 */
@SuppressWarnings("serial")
@Service("sampleTargetService")
public class SampleTargetServiceImpl  extends BaseAction  implements ISampleTargetService{
	
	@Autowired
	private SampleTargetDao sampleTargetDao;
	@Autowired
	private IShopDao shopDao;
	@Autowired
	private IModelMapDao modelMapDao;

	@Autowired
	private ImportExcelDao importExcelDao;
	
	@Autowired
	private ISellInDao sellInDao;
	
	
	
	@Override
	public List<SampleTarget> selectSampleTargetList(
			Map<String, Object> sampleMap) throws Exception {
		
		
		return sampleTargetDao.selectSampleTargetList(sampleMap);
	}

	@Override
	public Integer selectSampleTargetListCount(Map<String, Object> sampleMap)
			throws Exception {
		return sampleTargetDao.selectSampleTargetListCount(sampleMap);
	}



	
	
	@Override
	public String read2007Excel(File file) throws IOException {
		long start=System.currentTimeMillis();
		StringBuffer msg = new StringBuffer();
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			Object value = null;
			XSSFRow ro = sheet.getRow(0);
			XSSFCell cells = null;
			List<HashMap<String, Object>> modelList = new LinkedList<HashMap<String, Object>>();
			Set<String> sett= new HashSet<String>();
			for (int j = 3; j < ro.getLastCellNum(); j++) {
				HashMap<String, Object> hashMap = new HashMap<>();
				XSSFRow row = null;
				row = sheet.getRow(1);
				cells = ro.getCell(j);
				if (cells!=null &&
						cells.getCellType()!=HSSFCell.CELL_TYPE_BLANK &&
						cells.getStringCellValue() != null
						&& !cells.getStringCellValue().equals("")
						&& row.getCell(0)!=null && !row.getCell(0).getStringCellValue().equals("")
						&& row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						) {
					
					String countryName = row.getCell(0).getStringCellValue();
					String countryId = importExcelDao.selectCountry(countryName);
					String li = "";
					String cond = " AND t.`party_id`='"+countryId+"'";

					int model = modelMapDao.getModelIdByParty(cond,
							cells.getStringCellValue(), li);
				
						if (model >= 1) {
							hashMap.put("model", cells.getStringCellValue());
							}else{
								SellIn channelModel = sellInDao.selectCustomerModel(cells.getStringCellValue(), countryId,"");
								if(channelModel==null){
									msg.append(getText("sample.error.line") + (j +1)
											+ getText("sample.error.model") +"("+ cells.getStringCellValue()+")"+ "<br/>");
					
								}else{
									hashMap.put("model", channelModel.getModel());

								}
								}
						
				          if( !sett.add(cells.getStringCellValue())){
				        	  msg.append(getText("sample.error.line") + (j +1)
										+ getText("sample.error.modelRe") +"("+ cells.getStringCellValue()+")"+ "<br/>");
							
				          }
							
					
					modelList.add(hashMap);
					
				}
				

			}
			 
			 
		
			
			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				List<Object> linked = new LinkedList<Object>();
				String countryName="";
				String countryId="";
				if(row.getCell(0)!=null  && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					countryName = row.getCell(0).getStringCellValue();
					countryId= importExcelDao.selectCountry(countryName);
					if (countryId == null  ) {
						msg.append(getText("sample.error.row") + (i + 1)
								+ getText("sample.error.country") +"("+row.getCell(0).getStringCellValue()+")"+ "<br/>");
					}
					
				}else{
					if((row.getCell(1)!=null && row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK)){
						msg.append(getText("sample.error.row") + (i + 1)+" "+getText("sample.error.line") + (1)+" "
								+ getText("sample.error.null") + "<br/>");
						
					}
				}
				
				
				  

				String shopName="";
				Shop shop =null;
				if(row.getCell(1)!=null  && row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					shopName = row.getCell(1).getStringCellValue();
					shop = shopDao.getShopByNames(shopName);
				if (shop == null) {
					msg.append(getText("sample.error.row") + (i + 1)
							+ getText("sample.error.shop")+"("+ row.getCell(1).getStringCellValue()+")"+"<br/>");
				}
				
				}else{
					if((row.getCell(0)!=null && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK)
							){
						msg.append(getText("sample.error.row") + (i + 1)+" "+getText("sample.error.line") + (2)+" "
								+ getText("sample.error.null") + "<br/>");
						
					}
				}
				
				
				SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date date = new Date();
				String dt = dfd.format(date);
				Date dt1 = dfd.parse(dt);
				Date dt2;
				String rowDate="";
				
				if(  	row.getCell(0)!=null &&  row.getCell(1)!=null  
						&&
						row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						&& row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					
					
					if(row.getCell(2)!=null  && row.getCell(2).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						try{
							format.setLenient(false);
							date = format.parse(row.getCell(2).getStringCellValue());//有异常要捕获
							dfd.setLenient(false);
							String newD = dfd.format(date);
							date = dfd.parse(newD);//有异常要捕获
								dt2 =dfd.parse(newD);
								if (dt1.getTime() < dt2.getTime()) {
									msg.append(getText("sample.error.row") + (i + 1)+getText("sample.error.cell")+(3)
											+ getText("sample.error.time") + "<br/>");
								}else{
									rowDate=newD;
									
								}
						}catch(Exception e){
							msg.append(getText("sample.error.row") + (i + 1)+getText("sample.error.cell")+(3)
									+ getText("sample.error.date") + "<br/>");
						}
						
							
						
				
					}else{
						msg.append(getText("sample.error.row") + (i + 1)+getText("sample.error.cell")+(3)
								+ getText("sample.error.dateNo") + "<br/>");
					}
						
					
				}
			
				
				for (int m = 0; m < modelList.size(); m++) {
						
						
						if(row.getCell(m+ 3)!=null  
								 && row.getCell(m+ 3).getCellType()!=HSSFCell.CELL_TYPE_BLANK
								
								){
							 switch (row.getCell(m+ 3).getCellType()) {
							
							 case HSSFCell.CELL_TYPE_STRING:
						    	  msg.append(getText("sample.error.row") + (i + 1)+getText("sample.error.cell")+(m+3)
											+ getText("sample.error.num") + "<br/>");
						       break;
						 
						      case HSSFCell.CELL_TYPE_FORMULA:
						 
						    	  msg.append(getText("sample.error.row") + (i + 1)+getText("sample.error.cell")+(m+3)
											+ getText("sample.error.num") + "<br/>");
						  
						       break;
						 
						      case HSSFCell.CELL_TYPE_NUMERIC:
						    	 
						    	  if(row.getCell(m+3)
											.getNumericCellValue()!=0){
						    		  HashMap<String, Object> modelMap = new HashMap<String, Object>();
						    		  modelMap.put("Model", modelList.get(m).get("model"));
						    		  if (countryId != null) {
											modelMap.put("Country", countryId == null ? null
													: countryId);
											modelMap.put("CountryName", countryName);
										}
									
									
										if (shop != null) {
											modelMap.put("Store", shop.getShopId() == null ? null
													: shop.getShopId());
											modelMap.put("StoreName", shopName);
										}

							
									if(rowDate!=null && !rowDate.equals("")){
										
										modelMap.put("DataDate", rowDate);
								
									}
									
									 modelMap.put("quantity", (int) row.getCell(m+ 3)
												.getNumericCellValue());
										
						    		  allModelList.add(modelMap);
										
						    	  }
						    	  
						 
						        break;
						 
						      case HSSFCell.CELL_TYPE_ERROR:
						 
						       break;
						 
						      }
							
						
						
						
						
					}
					
				
					
				
					
						
							
					
				}
				
				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {

					if(j>=0){
						cell = row.getCell(j);
					}
					
					if (cell == null || cell.getCellType()!=HSSFCell.CELL_TYPE_BLANK) {
						continue;
					}
					DecimalFormat df = new DecimalFormat("0");// 格式化 number
																// String 字符
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
					DecimalFormat nf = new DecimalFormat("0");// 格式化数字
					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						if ("@".equals(cell.getCellStyle()
								.getDataFormatString())) {
							value = df.format(cell.getNumericCellValue());
						} else if ("General".equals(cell.getCellStyle()
								.getDataFormatString())) {
							value = nf.format(cell.getNumericCellValue());
						} else {
							value = sdf.format(HSSFDateUtil.getJavaDate(cell
									.getNumericCellValue()));
						}
						break;
					case XSSFCell.CELL_TYPE_BOOLEAN:
						value = cell.getBooleanCellValue();
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						value = "";
						break;
					default:
						value = cell.toString();
					}
					if (value == null || "".equals(value)) {
						continue;
					}
					linked.add(value);

				}
				list.add(linked);
			}
			String userId="";
			if(WebPageUtil.getLoginedUserId()!=null){
				userId=(WebPageUtil.getLoginedUserId());
			}
			System.out.println("==========size==========="+allModelList.size());
			List<SampleTarget> excelList=new ArrayList<SampleTarget>();
			List<SampleTarget> excelListByupdate=new ArrayList<SampleTarget>();
			
			//System.out.println("=========allModelList==========="+allModelList);
			if(msg.length()<=0){
			for (int i = 0; i < allModelList.size(); i++) {
					if (	allModelList.get(i)
							.get("Store")!=null
							&&allModelList.get(i)
							.get("Store")!=""
							
							&& allModelList.get(i).get("Model")!=null
							&& allModelList.get(i).get("Model")!=""
							&& allModelList.get(i).get("Country")!=null
							&& allModelList.get(i).get("Country")!=""
							&& allModelList.get(i).get("DataDate")!=null
							&& allModelList.get(i).get("DataDate")!=""	
							&& userId!=null && userId!=""
							&& allModelList.get(i).get("quantity")!=null
							&& allModelList.get(i).get("quantity")!=""	
					
					) {
						
						SampleTarget excel = new SampleTarget();
					
						excel.setUserId(userId);
						excel.setModel(allModelList.get(i).get("Model").toString());
						excel.setDataDate(allModelList.get(i).get("DataDate")
								.toString());
						excel.setShopId(allModelList.get(i)
								.get("Store").toString());
						excel.setQuantity(Integer.parseInt(allModelList.get(i)
								.get("quantity").toString()));
					
						String []days=allModelList.get(i).get("DataDate")
								.toString().split("-");
						String begin=days[0]+"-"+days[1]+"-01";
						String end=days[0]+"-"+days[1]+"-31";
						excel.setBeginDate(begin);
						excel.setEndDate(end);
						
					int rows = sampleTargetDao.selectSampleTargetsCount(begin, end, allModelList.get(i)
							.get("Store").toString(), allModelList.get(i).get("Model").toString());

					if (rows == 0) {
						excelList.add(excel);
						
					} else {
						excelListByupdate.add(excel);
						
					}
					
				
			}
				}
				
					// 判断该机型该门店是否存在，存在的话就修改，不存在就插入
			}
			if(excelList.size()>0){
				sampleTargetDao.saveSampleTarget(excelList);
			}
			if(excelListByupdate.size()>0){
				sampleTargetDao.updateSampleTarget(excelListByupdate);
			}
			
			System.out.println("=========================Time"+(System.currentTimeMillis()-start));

			if (msg.length() > 0) {
				return msg.toString();
			} else {
				
				return "";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			msg.append(e.getMessage());
			return msg.toString();
		}
		
	}

	@Override
	public String readExcel(File file, String fileName) throws IOException {
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		} else if ("xlsx".equals(extension)) {
			return read2007Excel(file);
		} else {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		}
	}
	
	@Override
	public XSSFWorkbook exporExcel(String conditions, String[] excelHeader,
			String title) throws Exception {
		String key=null;
		List<Shop> list=shopDao.selectShopName(conditions,null);
		
		List<HashMap<String, Object>>  modelList=sampleTargetDao.selectModel(null,conditions);
		//设置 表头宽度
		int[] excelWidth = {120,180,200,120,100,100,100,100};
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		 XSSFSheet sheet = workbook.createSheet(title);
		 XSSFSheet sheetNotice = workbook.createSheet("NOTICE");
		 sheet.createFreezePane(3,1,3,1)  ;
		

		 
		 //导出字体样式
		 XSSFFont font = workbook.createFont();
		 font.setFontHeightInPoints((short) 12); // 字体大小
		 font.setFontName("Times New Roman");
		 font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 
		 
		 XSSFFont fontNo = workbook.createFont();
		 fontNo.setFontHeightInPoints((short) 12); // 字体大小
		 fontNo.setFontName("Times New Roman");
		 
		 
		   XSSFCellStyle styleNo = workbook.createCellStyle();    
		   styleNo.setFont(fontNo);
	         
		 
		 //导出样式
         XSSFCellStyle style = workbook.createCellStyle();    
         style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         style.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
         style.setBottomBorderColor(HSSFColor.BLACK.index);
         style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
         style.setLeftBorderColor(HSSFColor.BLACK.index);
         style.setBorderRight(HSSFCellStyle.BORDER_THIN);
         style.setRightBorderColor(HSSFColor.BLACK.index);
         style.setBorderTop(HSSFCellStyle.BORDER_THIN);
         style.setTopBorderColor(HSSFColor.BLACK.index);
         style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
         style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
         style.setFont(font);
         
         
         //导出样式
         XSSFCellStyle styleBlue = workbook.createCellStyle();    
         styleBlue.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleBlue.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         styleBlue.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleBlue.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
         styleBlue.setBottomBorderColor(HSSFColor.BLACK.index);
         styleBlue.setBorderLeft(HSSFCellStyle.BORDER_THIN);
         styleBlue.setLeftBorderColor(HSSFColor.BLACK.index);
         styleBlue.setBorderRight(HSSFCellStyle.BORDER_THIN);
         styleBlue.setRightBorderColor(HSSFColor.BLACK.index);
         styleBlue.setBorderTop(HSSFCellStyle.BORDER_THIN);
         styleBlue.setTopBorderColor(HSSFColor.BLACK.index);
         styleBlue.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
         styleBlue.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
         styleBlue.setFont(font);
         
         
         
         //导出字体样式
		 XSSFFont fontTwo = workbook.createFont();
		 fontTwo.setFontHeightInPoints((short) 12); // 字体大小
		 fontTwo.setFontName("Times New Roman");

		 
		 //导出样式
         XSSFCellStyle styleTwo = workbook.createCellStyle();    
         styleTwo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleTwo.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         
         styleTwo.setFont(fontTwo);
		 
		 
         
         XSSFRow rowNotice=sheetNotice.createRow(1);
         XSSFCell cellNotice = rowNotice.createCell(3);
         cellNotice.setCellValue("Notice:");
         cellNotice.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(2);
         XSSFCell cellNoticeTW = rowNotice.createCell(3);
         cellNoticeTW.setCellValue("1. Please fill in the chart with the font Times New Roman in size 12.");
         cellNoticeTW.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(3);
         XSSFCell cellNoticeTh = rowNotice.createCell(3);
         cellNoticeTh.setCellValue("1. 请将字体统一为Times New Roman，字体大小12号。");
         cellNoticeTh.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(4);
         XSSFCell cellNoticeFO = rowNotice.createCell(3);
         cellNoticeFO.setCellValue("2. Store name here must be exactly the same as the ones filled in shopsInfo.");
         cellNoticeFO.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(5);
         XSSFCell cellNoticeFI = rowNotice.createCell(3);
         cellNoticeFI.setCellValue("2. 门店名称必须与门店信息表中所填的名称完全一致。");
         cellNoticeFI.setCellStyle(styleNo);
         
       
         //--------------------------------------------------------------
		 for (int i = 0; i < excelWidth.length; i++) {  
			    sheet.setColumnWidth(i, 32 * excelWidth[i]);  
		 } 
		 
         XSSFRow row = sheet.createRow(0);
         //表头数据
         for (int i = 0; i < excelHeader.length; i++) {   
             XSSFCell cell = row.createCell(i);    
             cell.setCellValue(excelHeader[i]);    
             cell.setCellStyle(style);  
             
         }   
         
         
         		 XSSFCell  cell4 = row.createCell(3);
                 cell4.setCellValue("65C2");    
                 cell4.setCellStyle(styleBlue);
                 
                 XSSFCell  cell1 = row.createCell(4);
                 cell1.setCellValue("55C2");    
                 cell1.setCellStyle(styleBlue);
                 
        	 
          
         
         
         
         //表体数据
         for (int i = 0; i < list.size(); i++) {    
             row = sheet.createRow(i + 1);    
             Shop shop = list.get(i);
             
             //-------------单元格-------------------
             
             /**
              * 国家
              */
             if(i==0){
                 XSSFCell cell = row.createCell(2);
                 cell.setCellValue("dd/MM/yyyy");    
                 cell.setCellStyle(styleTwo);

         		XSSFCell cell3= row.createCell(3);
         		cell3.setCellValue(200); 
         		
         		XSSFCell cell2= row.createCell(4);
         		cell2.setCellValue(300); 
             }
             
             
             if(shop.getCountryName()!=null  && !shop.getCountryName().equals("")){
            	 XSSFCell cell0 = row.createCell(0);
                 cell0.setCellValue(shop.getCountryName());    
                 cell0.setCellStyle(styleTwo);
             }
            
             
             if(shop.getShopName()!=null  && !shop.getShopName().equals("")){
            	 XSSFCell cell0 = row.createCell(1);
                 cell0.setCellValue(shop.getShopName());    
                 cell0.setCellStyle(styleTwo);
             }
             
             
            
         }    
         
         
         return workbook;
	}

	@Override
	public List<SampleTarget> selectShopByParty(String countryId, String partyId)
			throws Exception {
		return sampleTargetDao.selectShopByParty(countryId, partyId);
	}

	@Override
	public List<SampleTarget> selectModelByCountry(String countryId)
			throws Exception {
		return sampleTargetDao.selectModelByCountry(countryId);
	}

	@Override
	public int updateSampleTargetById(SampleTarget sampleTarget)
			throws Exception {
		return sampleTargetDao.updateSampleTargetById(sampleTarget);
	}

	@Override
	public int deleteSampleTarget(int id) throws Exception {
		return sampleTargetDao.deleteSampleTarget(id);
	}

	@Override
	public List<SampleTarget> selectSampleAchList(Map<String, Object> sampleMap)
			throws Exception {
		return sampleTargetDao.selectSampleAchList(sampleMap);
	}

	@Override
	public Integer selectSampleAchListCount(Map<String, Object> sampleMap)
			throws Exception {
		return sampleTargetDao.selectSampleAchListCount(sampleMap);
	}
	
	
	@Override
	public XSSFWorkbook exporTargetAch(Map<String,Object> sampleMap, String[] excelHeader,
			String title) throws Exception {
		List<SampleTarget> sampleList = sampleTargetDao.selectSampleAchList(sampleMap);	
		//设置 表头宽度
		int[] excelWidth ={80,100,100,120,100,100,100,100};
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		 XSSFSheet sheet = workbook.createSheet(title);
		 sheet.createFreezePane(3,1,3,1)  ;
		

		 
		 //导出字体样式
		 XSSFFont font = workbook.createFont();
		 font.setFontHeightInPoints((short) 12); // 字体大小
		 font.setFontName("Times New Roman");
		 font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 
		 
		 XSSFFont fontNo = workbook.createFont();
		 fontNo.setFontHeightInPoints((short) 12); // 字体大小
		 fontNo.setFontName("Times New Roman");
		 
		 
		   XSSFCellStyle styleNo = workbook.createCellStyle();    
		   styleNo.setFont(fontNo);
	         
		 
		 //导出样式
         XSSFCellStyle style = workbook.createCellStyle();    
         style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         style.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
         style.setBottomBorderColor(HSSFColor.BLACK.index);
         style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
         style.setLeftBorderColor(HSSFColor.BLACK.index);
         style.setBorderRight(HSSFCellStyle.BORDER_THIN);
         style.setRightBorderColor(HSSFColor.BLACK.index);
         style.setBorderTop(HSSFCellStyle.BORDER_THIN);
         style.setTopBorderColor(HSSFColor.BLACK.index);
         style.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
         style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
         style.setFont(font);
         style.setWrapText(true);

         
         
         //导出字体样式
		 XSSFFont fontTwo = workbook.createFont();
		 fontTwo.setFontHeightInPoints((short) 10); // 字体大小
		 fontTwo.setFontName("Times New Roman");

		 
		 //导出样式
         XSSFCellStyle styleOne = workbook.createCellStyle();    
         styleOne.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleOne.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         styleOne.setFont(fontTwo);
         styleOne.setWrapText(true);
		 
		 //导出样式
         XSSFCellStyle styleTwo = workbook.createCellStyle();    
         styleTwo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleTwo.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         styleTwo.setFont(fontTwo);
         styleTwo.setWrapText(true);
        
         
         
         	CellStyle cellStyleRED = workbook.createCellStyle();// 表头样式
			cellStyleRED.setFont(fontTwo);
			cellStyleRED.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleRED.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleRED.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setFillForegroundColor(HSSFColor.RED.index);
			cellStyleRED.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleRED.setWrapText(true);

			CellStyle cellStyleGreen = workbook.createCellStyle();// 表头样式
			cellStyleGreen.setFont(fontTwo);
			cellStyleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
			cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleGreen.setWrapText(true);

			CellStyle cellStyleYellow = workbook.createCellStyle();// 表头样式
			cellStyleYellow.setFont(fontTwo);
			cellStyleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow
					.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			
         //--------------------------------------------------------------
		 for (int i = 0; i < excelWidth.length; i++) {  
			    sheet.setColumnWidth(i, 32 * excelWidth[i]);  
		 } 
		 
         XSSFRow row = sheet.createRow(0);
         //表头数据
         for (int i = 0; i < excelHeader.length; i++) {   
             XSSFCell cell = row.createCell(i);    
             cell.setCellValue(excelHeader[i]);    
             cell.setCellStyle(style);  
             
         }   
         
         
         
         
         DataFormat df = workbook.createDataFormat();

         styleOne.setDataFormat(df
					.getFormat("#,##0"));// 数据格式只显示整数
         //表体数据
         for (int i = 0; i < sampleList.size(); i++) {    
             row = sheet.createRow(i + 1);    
             SampleTarget sampleTarget = sampleList.get(i);
             
             //-------------单元格-------------------
           //  "国家","所属机构","客户","门店","系列","总部型号","分公司型号"
             if(sampleTarget.getCountryName()!=null  && !sampleTarget.getCountryName().equals("")){
            	 XSSFCell cell0 = row.createCell(0);
                 cell0.setCellValue(sampleTarget.getCountryName());    
                 cell0.setCellStyle(styleTwo);
             }
             if(sampleTarget.getPartyName()!=null  && !sampleTarget.getPartyName().equals("")){
            	 XSSFCell cell0 = row.createCell(1);
                 cell0.setCellValue(sampleTarget.getPartyName());    
                 cell0.setCellStyle(styleTwo);
             }
             
             
             if(sampleTarget.getCustomerName()!=null  && !sampleTarget.getCustomerName().equals("")){
            	 XSSFCell cell0 = row.createCell(2);
                 cell0.setCellValue(sampleTarget.getCustomerName());    
                 cell0.setCellStyle(styleTwo);
             }
           
             
             
             if(sampleTarget.getShopName()!=null  && !sampleTarget.getShopName().equals("")){
            	 XSSFCell cell0 = row.createCell(3);
                 cell0.setCellValue(sampleTarget.getShopName());    
                 cell0.setCellStyle(styleTwo);
             }
             
             if(sampleTarget.getProductLine()!=null  && !sampleTarget.getProductLine().equals("")){
            	 XSSFCell cell0 = row.createCell(4);
                 cell0.setCellValue(sampleTarget.getProductLine());    
                 cell0.setCellStyle(styleTwo);
             }
             
             
             if(sampleTarget.getHqModel()!=null  && !sampleTarget.getHqModel().equals("")){
            	 XSSFCell cell0 = row.createCell(5);
                 cell0.setCellValue(sampleTarget.getHqModel());    
                 cell0.setCellStyle(styleTwo);
             }
             
        
             
             if(sampleTarget.getModel()!=null  && !sampleTarget.getModel().equals("")){
            	 XSSFCell cell0 = row.createCell(6);
                 cell0.setCellValue(sampleTarget.getModel());    
                 cell0.setCellStyle(styleTwo);
             }
             
             
             
            // "当月上样目标","当月上样达成","当月上样完成率(%)"
             
            XSSFCell cell0 = row.createCell(7);
            cell0.setCellValue(sampleTarget.getSampleTarget());    
            cell0.setCellStyle(styleOne);
            
            
            XSSFCell cell3 = row.createCell(8);
            cell3.setCellValue(sampleTarget.getSampleQty());    
            cell3.setCellStyle(styleOne);
            
            XSSFCell cell2 = row.createCell(9);
            cell2.setCellValue(sampleTarget.getAch());    
			
            if (sampleTarget.getAch()
					.contains("%")

			) {

				String b = sampleTarget.getAch().toString(); // 
				String tempB = b.substring(0,
						b.lastIndexOf("%")); // 精确表示 Integer
				BigDecimal dataB = new BigDecimal(tempB); // 大于为1，相同为0，小于为-1
															// if
				if (dataB.compareTo(BigDecimal.valueOf(100)) == -1

						&& (dataB.compareTo(BigDecimal
								.valueOf(80)) == 1 || dataB
								.compareTo(BigDecimal
										.valueOf(80)) == 0)
				) {
					cell2.setCellStyle(cellStyleYellow);
				} else if (dataB.compareTo(BigDecimal
						.valueOf(100)) == 1
						|| dataB.compareTo(BigDecimal
								.valueOf(100)) == 1
				) {
					cell2.setCellStyle(cellStyleGreen);
				} else if (dataB.compareTo(BigDecimal
						.valueOf(80)) == -1) {
					cell2.setCellStyle(cellStyleRED);
				}

			}


            
           
			
			
      		//"累计上样目标","累计上样达成","累计上样完成率(%)"
			
			XSSFCell cell14 = row.createCell(10);
			cell14.setCellValue(sampleTarget.getChangeQty());    
			cell14.setCellStyle(styleOne);
		 	
			

			
				//"当月SO","当月样机效率"
			 	XSSFCell cell8 = row.createCell(11);
			 	cell8.setCellValue(sampleTarget.getSaleQty());    
			 	cell8.setCellStyle(styleOne);
	            
	            
	            
	            
	            XSSFCell cell10 = row.createCell(12);
	            cell10.setCellValue(sampleTarget.getSoAch());    
	            cell10.setCellStyle(styleTwo);
	           /* BigDecimal  dataB = new BigDecimal(sampleTarget.getAch()); // 大于为1，相同为0，小于为-1
															// if
				if (dataB.compareTo(BigDecimal.valueOf(100)) == -1

						&& (dataB.compareTo(BigDecimal
								.valueOf(80)) == 1 || dataB
								.compareTo(BigDecimal
										.valueOf(80)) == 0)
				) {
					cell10.setCellStyle(cellStyleYellow);
				} else if (dataB.compareTo(BigDecimal
						.valueOf(100)) == 1
						|| dataB.compareTo(BigDecimal
								.valueOf(100)) == 1
				) {
					cell10.setCellStyle(cellStyleGreen);
				} else if (dataB.compareTo(BigDecimal
						.valueOf(80)) == -1
						) {
					cell10.setCellStyle(cellStyleRED);
				}*/
				
				
				
	            
			if(sampleTarget.getDataDate()!=null  && !sampleTarget.getDataDate().equals("")){
           	 XSSFCell cell5 = row.createCell(13);
           	cell5.setCellValue(sampleTarget.getDataDate());    
           	cell5.setCellStyle(styleTwo);
            }
			
			
            
         }    
         
         
         return workbook;
	}

	@Override
	public List<SampleTarget> selectSampleAchListByLine(
			Map<String, Object> sampleMap) throws Exception {
		return sampleTargetDao.selectSampleAchListByLine(sampleMap);
	}

	@Override
	public Integer selectSampleAchListCountByLine(Map<String, Object> sampleMap)
			throws Exception {
		return sampleTargetDao.selectSampleAchListCountByLine(sampleMap);
	}

	@Override
	public List<SampleTarget> selectSampleTargetListByLine(
			Map<String, Object> sampleMap) throws Exception {
		return sampleTargetDao.selectSampleTargetListByLine(sampleMap);
	}

	@Override
	public Integer selectSampleTargetListCountByLine(
			Map<String, Object> sampleMap) throws Exception {
		return sampleTargetDao.selectSampleTargetListCountByLine(sampleMap);
	}

	@Override
	public XSSFWorkbook exporSampleTarget(Map<String, Object> sampleMap,
			String[] excelHeader, String title) throws Exception {
		List<SampleTarget> sampleList = sampleTargetDao.selectSampleTargetList(sampleMap);	
		//设置 表头宽度
		int[] excelWidth = {80,120,120,120,100,100,100,120,120,120};
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		 XSSFSheet sheet = workbook.createSheet(title);
		 sheet.createFreezePane(3,1,3,1)  ;
		

		 
		 //导出字体样式
		 XSSFFont font = workbook.createFont();
		 font.setFontHeightInPoints((short) 12); // 字体大小
		 font.setFontName("Times New Roman");
		 font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 
		 
		 XSSFFont fontNo = workbook.createFont();
		 fontNo.setFontHeightInPoints((short) 12); // 字体大小
		 fontNo.setFontName("Times New Roman");
		 
		 
		   XSSFCellStyle styleNo = workbook.createCellStyle();    
		   styleNo.setFont(fontNo);
	         
		 
		 //导出样式
         XSSFCellStyle style = workbook.createCellStyle();    
         style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         style.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
         style.setBottomBorderColor(HSSFColor.BLACK.index);
         style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
         style.setLeftBorderColor(HSSFColor.BLACK.index);
         style.setBorderRight(HSSFCellStyle.BORDER_THIN);
         style.setRightBorderColor(HSSFColor.BLACK.index);
         style.setBorderTop(HSSFCellStyle.BORDER_THIN);
         style.setTopBorderColor(HSSFColor.BLACK.index);
         style.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
         style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
         style.setFont(font);
         style.setWrapText(true);

         
         
         //导出字体样式
		 XSSFFont fontTwo = workbook.createFont();
		 fontTwo.setFontHeightInPoints((short) 10); // 字体大小
		 fontTwo.setFontName("Times New Roman");

		 
		 //导出样式
         XSSFCellStyle styleOne = workbook.createCellStyle();    
         styleOne.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleOne.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         styleOne.setFont(fontTwo);
         styleOne.setWrapText(true);
		 
		 //导出样式
         XSSFCellStyle styleTwo = workbook.createCellStyle();    
         styleTwo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleTwo.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         styleTwo.setFont(fontTwo);
         styleTwo.setWrapText(true);
        
         
         
         	CellStyle cellStyleRED = workbook.createCellStyle();// 表头样式
			cellStyleRED.setFont(fontTwo);
			cellStyleRED.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleRED.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleRED.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleRED.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleRED.setFillForegroundColor(HSSFColor.RED.index);
			cellStyleRED.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleRED.setWrapText(true);

			CellStyle cellStyleGreen = workbook.createCellStyle();// 表头样式
			cellStyleGreen.setFont(fontTwo);
			cellStyleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleGreen.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
			cellStyleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyleGreen.setWrapText(true);

			CellStyle cellStyleYellow = workbook.createCellStyle();// 表头样式
			cellStyleYellow.setFont(fontTwo);
			cellStyleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
			cellStyleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setRightBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyleYellow.setTopBorderColor(HSSFColor.BLACK.index);
			cellStyleYellow
					.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			
         //--------------------------------------------------------------
		 for (int i = 0; i < excelWidth.length; i++) {  
			    sheet.setColumnWidth(i, 32 * excelWidth[i]);  
		 } 
		 
         XSSFRow row = sheet.createRow(0);
         //表头数据
         for (int i = 0; i < excelHeader.length; i++) {   
             XSSFCell cell = row.createCell(i);    
             cell.setCellValue(excelHeader[i]);    
             cell.setCellStyle(style);  
             
         }   
         
         
         
        
         DataFormat df = workbook.createDataFormat();
         cellStyleRED.setDataFormat(df
					.getFormat("#,##0.00"));
         cellStyleGreen.setDataFormat(df
					.getFormat("#,##0.00"));
         cellStyleYellow.setDataFormat(df
					.getFormat("#,##0.00"));
         styleOne.setDataFormat(df
					.getFormat("#,##0"));// 数据格式只显示整数
         //表体数据
         for (int i = 0; i < sampleList.size(); i++) {    
             row = sheet.createRow(i + 1);    
             SampleTarget sampleTarget = sampleList.get(i);
             
             //-------------单元格-------------------
             
             //"国家","所属机构","客户","门店","系列","总部型号","分公司型号"
             
             if(sampleTarget.getCountryName()!=null  && !sampleTarget.getCountryName().equals("")){
            	 XSSFCell cell0 = row.createCell(0);
                 cell0.setCellValue(sampleTarget.getCountryName());    
                 cell0.setCellStyle(styleTwo);
             }
             if(sampleTarget.getPartyName()!=null  && !sampleTarget.getPartyName().equals("")){
            	 XSSFCell cell0 = row.createCell(1);
                 cell0.setCellValue(sampleTarget.getPartyName());    
                 cell0.setCellStyle(styleTwo);
             }
             
             
             if(sampleTarget.getCustomerName()!=null  && !sampleTarget.getCustomerName().equals("")){
            	 XSSFCell cell0 = row.createCell(2);
                 cell0.setCellValue(sampleTarget.getCustomerName());    
                 cell0.setCellStyle(styleTwo);
             }
           
             
             
             if(sampleTarget.getShopName()!=null  && !sampleTarget.getShopName().equals("")){
            	 XSSFCell cell0 = row.createCell(3);
                 cell0.setCellValue(sampleTarget.getShopName());    
                 cell0.setCellStyle(styleTwo);
             }
             
             if(sampleTarget.getProductLine()!=null  && !sampleTarget.getProductLine().equals("")){
            	 XSSFCell cell0 = row.createCell(4);
                 cell0.setCellValue(sampleTarget.getProductLine());    
                 cell0.setCellStyle(styleTwo);
             }
             
             
             if(sampleTarget.getHqModel()!=null  && !sampleTarget.getHqModel().equals("")){
            	 XSSFCell cell0 = row.createCell(5);
                 cell0.setCellValue(sampleTarget.getHqModel());    
                 cell0.setCellStyle(styleTwo);
             }
             
             
             
             
             if(sampleTarget.getModel()!=null  && !sampleTarget.getModel().equals("")){
            	 XSSFCell cell0 = row.createCell(6);
                 cell0.setCellValue(sampleTarget.getModel());    
                 cell0.setCellStyle(styleTwo);
             }
             
             //"当月上样目标","累计上样目标","首次上样时间"
            XSSFCell cell0 = row.createCell(7);
            cell0.setCellValue(sampleTarget.getQuantity());    
            cell0.setCellStyle(styleOne);
            
            
            /*XSSFCell cell3 = row.createCell(8);
            cell3.setCellValue(sampleTarget.getTargetTTL());    
            cell3.setCellStyle(styleOne);*/
            
	            
			if(sampleTarget.getMinDate()!=null  && !sampleTarget.getMinDate().equals("")){
           	 XSSFCell cell5 = row.createCell(8);
           	cell5.setCellValue(sampleTarget.getMinDate());    
           	cell5.setCellStyle(styleTwo);
            }
			
			
            
         }    
         
         
         return workbook;
	}

	@Override
	public List<SampleTarget> selectSampleTargetSumListByLine(
			Map<String, Object> sampleMap) throws Exception {
		return sampleTargetDao.selectSampleTargetSumListByLine(sampleMap);
	}

	@Override
	public Integer selectSampleTargetSumListCountByLine(
			Map<String, Object> sampleMap) throws Exception {
		return sampleTargetDao.selectSampleTargetSumListCountByLine(sampleMap);
	}

	@Override
	public List<SampleTarget> selectSampleAchSumListByLine(
			Map<String, Object> sampleMap) throws Exception {
		return sampleTargetDao.selectSampleAchSumListByLine(sampleMap);
	}

	@Override
	public Integer selectSampleAchSumListCountByLine(
			Map<String, Object> sampleMap) throws Exception {
		return sampleTargetDao.selectSampleAchSumListCountByLine(sampleMap);
	}
	
	
	
	

}
