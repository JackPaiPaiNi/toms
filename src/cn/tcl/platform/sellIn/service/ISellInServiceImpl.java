package cn.tcl.platform.sellIn.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
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
import cn.tcl.platform.sample.vo.SampleTarget;
import cn.tcl.platform.sellIn.dao.ISellInDao;
import cn.tcl.platform.sellIn.vo.SellIn;
import cn.tcl.platform.shop.dao.IShopDao;
import cn.tcl.platform.shop.vo.Shop;

@Service("sellInService")
public class ISellInServiceImpl extends BaseAction implements ISellInService {
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
	public XSSFWorkbook exportExcel(String conditions, String[] excelHeader,
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
         
       /*  {"Country","Store","Model","Order Number","Date","Quantity"}*/
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
		
			String countryId=WebPageUtil.getLoginedUser().getPartyId();
			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
	    		  HashMap<String, Object> modelMap = new HashMap<String, Object>();
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				List<Object> linked = new LinkedList<Object>();
				


				String customerCode="";
				SellIn customer =null;
				if(row.getCell(0)!=null  && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK){

					 switch (row.getCell(0).getCellType()) {
						
					 case HSSFCell.CELL_TYPE_STRING:
							customerCode = row.getCell(0).getStringCellValue();
							customer = sellInDao.selectCustomerCode(customerCode,countryId);
						if (customer == null) {
							msg.append(getText("sample.error.row") + (i + 1)
									+ getText("sellIn.error.customerNo")+"("+ row.getCell(0).getStringCellValue()+")"+"<br/>");
						}else{
							modelMap.put("CustomerCode",customerCode);
						}
						
				       break;
				 
				      case HSSFCell.CELL_TYPE_NUMERIC:
				    	  customerCode = String.valueOf((int)row.getCell(0).getNumericCellValue());
							customer = sellInDao.selectCustomerCode(customerCode,countryId);
						if (customer == null) {
							msg.append(getText("sample.error.row") + (i + 1)
									+ getText("sellIn.error.customerNo")+"("+ (int)row.getCell(0).getNumericCellValue()+")"+"<br/>");
						}else{
							modelMap.put("CustomerCode",customerCode);
						}				    
								
				    	  
				 
				        break;
				 
				      case HSSFCell.CELL_TYPE_ERROR:
				 
				       break;
				       
				       
					 }
				
				
				}else{
					msg.append(getText("sample.error.row") + (i + 1)
							+ getText("sellIn.error.customerNull")+"<br/>");
			
						
				}
				
				
				
				if (row.getCell(1)!=null  && row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						) {
					
					if(countryId!=null &&!countryId.equals("") ){
						String li = "";
						String cond = " AND t.`party_id`='"+countryId+"'";

						int model = modelMapDao.getModelIdByParty(cond,
								row.getCell(1).getStringCellValue(), li);
					
							if (model >= 1) {
					    		  modelMap.put("Model", row.getCell(1).getStringCellValue());
								}else{
									
									SellIn channelModel = sellInDao.selectCustomerModel(row.getCell(1).getStringCellValue(), countryId,"");
									if(channelModel==null){
										msg.append(getText("sample.error.row") + (i + 1)+" "+getText("sample.error.model") 
												+"("+ row.getCell(1).getStringCellValue()+")"+ "<br/>");
									}else{
							    		  modelMap.put("Model", channelModel.getModel());

									}
									
										
								}
					}
					
					
				}else{
					msg.append(getText("sample.error.row") + (i + 1)+" "+getText("sample.error.modelNo")+ "<br/>");
					
				}
				
				if(row.getCell(2)!=null  
						 && row.getCell(2).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						
						){
					
					 switch (row.getCell(2).getCellType()) {
						
					 case HSSFCell.CELL_TYPE_STRING:
						 modelMap.put("OrderNum", row.getCell(2).getStringCellValue());
				       break;
				 
				      case HSSFCell.CELL_TYPE_NUMERIC:
				    	  modelMap.put("OrderNum", row.getCell(2).getNumericCellValue());
				    
								
				    	  
				 
				        break;
				 
				      case HSSFCell.CELL_TYPE_ERROR:
				 
				       break;
				 
				      }
				}
				
				SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date date = new Date();
				String dt = dfd.format(date);
				Date dt1 = dfd.parse(dt);
				Date dt2;
				
				if(  	row.getCell(0)!=null &&  row.getCell(1)!=null  
						&&
						row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						&& row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					
					
					if(row.getCell(3)!=null  && row.getCell(3).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						try{
							format.setLenient(false);
							date = format.parse(row.getCell(3).getStringCellValue());//有异常要捕获
							dfd.setLenient(false);
							String newD = dfd.format(date);
							date = dfd.parse(newD);//有异常要捕获
								dt2 =dfd.parse(newD);
								if (dt1.getTime() < dt2.getTime()) {
									msg.append(getText("sample.error.row") + (i + 1)+getText("sample.error.cell")+(4)
											+ getText("sample.error.time") + "<br/>");
								}else{
									modelMap.put("DataDate", newD);
									
								}
						}catch(Exception e){
							msg.append(getText("sample.error.row") + (i + 1)+getText("sample.error.cell")+(4)
									+ getText("sample.error.date") + "<br/>");
						}
						
							
						
				
					}else{
						msg.append(getText("sample.error.row") + (i + 1)+getText("sample.error.cell")+(4)
								+ getText("sample.error.dateNo") + "<br/>");
					}
						
					
				}
				
			if(row.getCell(4)!=null  
								 && row.getCell(4).getCellType()!=HSSFCell.CELL_TYPE_BLANK
								
								){
							 switch (row.getCell(4).getCellType()) {
							
							 case HSSFCell.CELL_TYPE_STRING:
						    	  msg.append(getText("sample.error.row") + (i + 1)+getText("sample.error.cell")+(5)
											+ getText("sample.error.num") + "<br/>");
						       break;
						 
						      case HSSFCell.CELL_TYPE_FORMULA:
						 
						    	  msg.append(getText("sample.error.row") + (i + 1)+getText("sample.error.cell")+(5)
											+ getText("sample.error.num") + "<br/>");
						  
						       break;
						 
						      case HSSFCell.CELL_TYPE_NUMERIC:
						    	 
									 modelMap.put("quantity", (int) row.getCell(4)
												.getNumericCellValue());
										
						 
						        break;
						 
						      case HSSFCell.CELL_TYPE_ERROR:
						 
						       break;
						 
						      }
						
					}else{
						 msg.append(getText("sample.error.row") + (i + 1)+" "
									+ getText("sellIn.error.quantityNull") + "<br/>");
					}
					
				
					
				
					
						
				allModelList.add(modelMap);
					
				
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
			List<SellIn> excelList=new ArrayList<SellIn>();
			
			//System.out.println("=========allModelList==========="+allModelList);
			if(msg.length()<=0){
			for (int i = 0; i < allModelList.size(); i++) {
					if (	allModelList.get(i)
							.get("CustomerCode")!=null
							&&allModelList.get(i)
							.get("CustomerCode")!=""
							&& allModelList.get(i).get("Model")!=null
							&& allModelList.get(i).get("Model")!=""
							&& allModelList.get(i).get("DataDate")!=null
							&& allModelList.get(i).get("DataDate")!=""	
							&& userId!=null && userId!=""
							&& allModelList.get(i).get("quantity")!=null
							&& allModelList.get(i).get("quantity")!=""
							
					
					) {
						
						SellIn excel = new SellIn();
					
						excel.setUserId(userId);
						excel.setModel(allModelList.get(i).get("Model").toString());
						excel.setDatadate(allModelList.get(i).get("DataDate")
								.toString());
						if(allModelList.get(i).get("OrderNum")!=null
								&& allModelList.get(i).get("OrderNum")!=""){
							excel.setOrderNum(allModelList.get(i).get("OrderNum")
									.toString());
						} 
								
						
						excel.setCustomerCode(allModelList.get(i)
								.get("CustomerCode").toString());
						excel.setQuantity(Integer.parseInt(allModelList.get(i)
								.get("quantity").toString()));
						
						excelList.add(excel);
						
					
					
				
			}
				}
				
					// 判断该机型该门店是否存在，存在的话就修改，不存在就插入
			}
			if(excelList.size()>0){
			
				sellInDao.saveSellIn(excelList);
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

/*	查询到后台，判断业务中心，型号一样的则加起来拼成一个业务中心total，key为业务中心名字+型号+total            
	 1.判断相同国家相同系列加起来合成系列国家total，key为国家+系列+total  2.相同系列相同业务中心一样加起来，key为业务中心+系列+total
	 1.判断相同国家的所有值加起来组成最大的total，key为国家+total  2.相同业务中心加起来，key为业务中心+total*/
	@Override
	public JSONObject selectSellInByHq(Map<String, Object> sellInMap)
			throws Exception {
		LinkedList<SellIn> sellList=sellInDao.selectSellInByHq(sellInMap);
		JSONArray jsonArray=new JSONArray();
		JSONArray jsonArraySum=new JSONArray();
		JSONObject result=new JSONObject();

		 Map<String, Integer> mapResultOne = new HashMap<String, Integer>();
		 Map<String, Integer> mapResultTwo = new HashMap<String, Integer>();
		 Map<String, Integer> mapResultThree = new HashMap<String, Integer>();

		 Map<String, Integer> mapResultFour = new HashMap<String, Integer>();

		 Map<String, Integer> mapResultFive = new HashMap<String, Integer>();

		for (int i=0;i<sellList.size();i++) {
			
			JSONObject object=new JSONObject();
			SellIn vo = sellList.get(i);
		  	object.accumulate("centerName",vo.getCenterName());
			object.accumulate("countryName",vo.getCountryName());
			object.accumulate("line",vo.getProductLine());
			object.accumulate("model",vo.getHqModel());
			object.accumulate("quantity",vo.getQuantity());
			
			
		  //计算业务中心型号的总和

		  Integer numOne = mapResultOne.get(vo.getCenterName()+"_"+vo.getHqModel()+"_CenterModel");
		  if (numOne == null) {
			  numOne = 0;
		  }
		  mapResultOne.put(vo.getCenterName()+"_"+vo.getHqModel()+"_CenterModel",numOne + vo.getQuantity());
		  
		//2.相同系列相同业务中心一样加起来，key为业务中心+系列+total

		  Integer numTwo = mapResultTwo.get(vo.getCenterName()+"_"+vo.getProductLine()+"_CenterLine");
		  if (numTwo == null) {
			  numTwo = 0;
		  }
		  mapResultTwo.put(vo.getCenterName()+"_"+vo.getProductLine()+"_CenterLine", numTwo + vo.getQuantity());
		  
		//相同业务中心加起来，key为业务中心+total

		  Integer numThree = mapResultThree.get(vo.getCenterName()+"_Center");
		  if (numThree == null) {
			  numThree = 0;
		  }
		  mapResultThree.put(vo.getCenterName()+"_Center", numThree + vo.getQuantity());
		  
		//1.判断相同国家的所有值加起来组成最大的total，key为国家+total

		  Integer numFour = mapResultFour.get(vo.getCountryName()+"_Country");
		  if (numFour == null) {
			  numFour = 0;
		  }
		  mapResultFour.put(vo.getCountryName()+"_Country", numFour + vo.getQuantity());
		  
		//1.判断相同国家相同系列加起来合成系列国家total，key为国家+系列+total
		  Integer numFive = mapResultFive.get(vo.getCountryName()+"_"+vo.getProductLine()+"_CountryLine");
		  if (numFive == null) {
			  numFive = 0;
		  }
		  mapResultFive.put(vo.getCountryName()+"_"+vo.getProductLine()+"_CountryLine" ,numFive + vo.getQuantity());

		  jsonArray.add(object);

		}
		
		LinkedList<SellIn> sellListCountry=sellInDao.selectSellInByHqCountry(sellInMap);
		JSONArray countryArray=new JSONArray();
		for (int i = 0; i < sellListCountry.size(); i++) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.accumulate("centerName",sellListCountry.get(i).getCenterName() );
			jsonObject.accumulate("countryName",sellListCountry.get(i).getCountryName() );
			countryArray.add(jsonObject);
		}
		
		LinkedList<SellIn> sellListModel=sellInDao.selectSellInByHqModel(sellInMap);
		JSONArray modelArray=new JSONArray();
		for (int i = 0; i < sellListModel.size(); i++) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.accumulate("line",sellListModel.get(i).getProductLine() );
			jsonObject.accumulate("model",sellListModel.get(i).getHqModel() );
			modelArray.add(jsonObject);
		}
		
		for (String key : mapResultOne.keySet()) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.accumulate("total",key);
			jsonObject.accumulate("qty",mapResultOne.get(key));
			jsonArraySum.add(jsonObject);
		 }
		for (String key : mapResultTwo.keySet()) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.accumulate("total",key);
			jsonObject.accumulate("qty",mapResultTwo.get(key));
			jsonArraySum.add(jsonObject);
		 }
		
		for (String key : mapResultThree.keySet()) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.accumulate("total",key);
			jsonObject.accumulate("qty",mapResultThree.get(key));
			jsonArraySum.add(jsonObject);
		 }
		for (String key : mapResultFour.keySet()) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.accumulate("total",key);
			jsonObject.accumulate("qty",mapResultFour.get(key));
			jsonArraySum.add(jsonObject);
		 }
		for (String key : mapResultFive.keySet()) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.accumulate("total",key);
			jsonObject.accumulate("qty",mapResultFive.get(key));
			jsonArraySum.add(jsonObject);
		 }
		
		result.accumulate("data", jsonArray);
		result.accumulate("country", countryArray);
		result.accumulate("model", modelArray);
		result.accumulate("sum", jsonArraySum);
		
		return result;
	}
	

	@Override
	public List<SampleTarget> selectModelByHq(String countryId)
			throws Exception {
		return sellInDao.selectModelByHq(countryId);
	}

	@Override
	public String readExcelByReturn(File file, String fileName)
			throws IOException {
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		} else if ("xlsx".equals(extension)) {
			return read2007ExcelByReturn(file);
		} else {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		}	}

	@Override
	public String read2007ExcelByReturn(File file) throws IOException {
		long start=System.currentTimeMillis();
		StringBuffer msg = new StringBuffer();
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			Object value = null;
		
			String countryId=WebPageUtil.getLoginedUser().getPartyId();

			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
	    		  HashMap<String, Object> modelMap = new HashMap<String, Object>();
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				List<Object> linked = new LinkedList<Object>();


				String customerCode="";
				SellIn customer =null;
				if(row.getCell(0)!=null  && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					
					 switch (row.getCell(0).getCellType()) {
						
					 case HSSFCell.CELL_TYPE_STRING:
							customerCode = row.getCell(0).getStringCellValue();
							customer = sellInDao.selectCustomerCode(customerCode,countryId);
						if (customer == null) {
							msg.append(getText("sample.error.row") + (i + 1)
									+ getText("sellIn.error.customerNo")+"("+ row.getCell(0).getStringCellValue()+")"+"<br/>");
						}else{
							modelMap.put("CustomerCode",customerCode);
						}
						
				       break;
				 
				      case HSSFCell.CELL_TYPE_NUMERIC:
				    	  customerCode = String.valueOf((int)row.getCell(0).getNumericCellValue());
							customer = sellInDao.selectCustomerCode(customerCode,countryId);
						if (customer == null) {
							msg.append(getText("sample.error.row") + (i + 1)
									+ getText("sellIn.error.customerNo")+"("+(int) row.getCell(0).getNumericCellValue()+")"+"<br/>");
						}else{
							modelMap.put("CustomerCode",customerCode);
						}				    
								
				    	  
				 
				        break;
				 
				      case HSSFCell.CELL_TYPE_ERROR:
				 
				       break;
				       
				       
					 }
				
				
				}else{
					msg.append(getText("sample.error.row") + (i + 1)
							+ getText("sellIn.error.customerNull")+"<br/>");
			
						
				}
				
				
				
				if (row.getCell(1)!=null  && row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						) {
					
					if(countryId!=null &&!countryId.equals("") ){
						String li = "";
						String cond = " AND t.`party_id`='"+countryId+"'";

						int model = modelMapDao.getModelIdByParty(cond,
								row.getCell(1).getStringCellValue(), li);
					
							if (model >= 1) {
					    		  modelMap.put("Model", row.getCell(1).getStringCellValue());
								}else{
									
									SellIn channelModel = sellInDao.selectCustomerModel(row.getCell(1).getStringCellValue(), countryId,"");
									if(channelModel==null){
										msg.append(getText("sample.error.row") + (i + 1)+" "+getText("sample.error.model") 
												+"("+ row.getCell(1).getStringCellValue()+")"+ "<br/>");
									}else{
							    		  modelMap.put("Model", channelModel.getModel());

									}
									
										
								}
					}
					
					
				}else{
					msg.append(getText("sample.error.row") + (i + 1)+" "+getText("sample.error.modelNo")+ "<br/>");
					
				}
				
			/*	if(row.getCell(2)!=null  
						 && row.getCell(2).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						
						){
					
					 switch (row.getCell(2).getCellType()) {
						
					 case HSSFCell.CELL_TYPE_STRING:
						 modelMap.put("OrderNum", row.getCell(2).getStringCellValue());
				       break;
				 
				      case HSSFCell.CELL_TYPE_NUMERIC:
				    	  modelMap.put("OrderNum", row.getCell(2).getNumericCellValue());
				    
								
				    	  
				 
				        break;
				 
				      case HSSFCell.CELL_TYPE_ERROR:
				 
				       break;
				 
				      }
				}*/
				
				SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date date = new Date();
				String dt = dfd.format(date);
				Date dt1 = dfd.parse(dt);
				Date dt2;
				
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
									modelMap.put("DataDate", newD);
									
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
				
			if(row.getCell(3)!=null  
								 && row.getCell(3).getCellType()!=HSSFCell.CELL_TYPE_BLANK
								
								){
							 switch (row.getCell(3).getCellType()) {
							
							 case HSSFCell.CELL_TYPE_STRING:
						    	  msg.append(getText("sample.error.row") + (i + 1)+getText("sample.error.cell")+(4)
											+ getText("sample.error.num") + "<br/>");
						       break;
						 
						      case HSSFCell.CELL_TYPE_FORMULA:
						 
						    	  msg.append(getText("sample.error.row") + (i + 1)+getText("sample.error.cell")+(4)
											+ getText("sample.error.num") + "<br/>");
						  
						       break;
						 
						      case HSSFCell.CELL_TYPE_NUMERIC:
						    	 
									 modelMap.put("quantity", (int) row.getCell(3)
												.getNumericCellValue());
										
						 
						        break;
						 
						      case HSSFCell.CELL_TYPE_ERROR:
						 
						       break;
						 
						      }
						
					}else{
						 msg.append(getText("sample.error.row") + (i + 1)+" "
									+ getText("sellIn.error.quantityNull") + "<br/>");
					}
					
				
					
				
					
						
				allModelList.add(modelMap);
					
				
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
			List<SellIn> excelList=new ArrayList<SellIn>();
			
			//System.out.println("=========allModelList==========="+allModelList);
			if(msg.length()<=0){
			for (int i = 0; i < allModelList.size(); i++) {
			
					if (	allModelList.get(i)
							.get("CustomerCode")!=null
							&&allModelList.get(i)
							.get("CustomerCode")!=""
							&& allModelList.get(i).get("Model")!=null
							&& allModelList.get(i).get("Model")!=""
							&& allModelList.get(i).get("DataDate")!=null
							&& allModelList.get(i).get("DataDate")!=""	
							&& userId!=null && userId!=""
							&& allModelList.get(i).get("quantity")!=null
							&& allModelList.get(i).get("quantity")!=""
							
					
					) {
						
						SellIn excel = new SellIn();
					
						excel.setUserId(userId);
						excel.setModel(allModelList.get(i).get("Model").toString());
						excel.setDatadate(allModelList.get(i).get("DataDate")
								.toString());
						
						excel.setCustomerCode(allModelList.get(i)
								.get("CustomerCode").toString());
						excel.setQuantity(Integer.parseInt(allModelList.get(i)
								.get("quantity").toString()));
						
						excelList.add(excel);
						
					
					
				
			}
				}
				
					// 判断该机型该门店是否存在，存在的话就修改，不存在就插入
			}
			System.out.println("==========size==========="+excelList.size());

			if(excelList.size()>0){
				sellInDao.saveReturn(excelList);
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
		}	}

	@Override
	public Map<String, Object> selectSellInByTable(Map<String, Object> sellInMap)
			throws Exception {
		List<SellIn> list=sellInDao.selectSellInByTable(sellInMap);
		int count=sellInDao.selectSellInByTableCount(sellInMap);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", count);
		return map;
	}

	@Override
	public Map<String, Object>selectReturn(Map<String, Object> sellInMap)
			throws Exception {
		List<SellIn> list=sellInDao.selectReturn(sellInMap);
		int count=sellInDao.selectReturnCount(sellInMap);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", count);
		return map;
	}
	
}
