package cn.tcl.platform.excel.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.customer.dao.ICustomerDao;
import cn.tcl.platform.customer.vo.Customer;
import cn.tcl.platform.excel.actions.DateUtil;
import cn.tcl.platform.excel.dao.ImportExcelDao;
import cn.tcl.platform.excel.service.ImportExcelService;
import cn.tcl.platform.excel.vo.Excel;
import cn.tcl.platform.excel.vo.ImportExcel;
import cn.tcl.platform.importSo.service.ImportSoService;
import cn.tcl.platform.modelmap.dao.IModelMapDao;
import cn.tcl.platform.party.vo.Party;
import cn.tcl.platform.sale.vo.Sale;
import cn.tcl.platform.sale.vo.SaleTarget;
import cn.tcl.platform.sellIn.dao.ISellInDao;
import cn.tcl.platform.sellIn.vo.SellIn;
import cn.tcl.platform.shop.dao.IShopDao;
import cn.tcl.platform.shop.vo.Shop;
import cn.tcl.platform.ws.vo.SaleReq;

@Service("importPHExcelService")
public class ImportPHExcelServiceImpl extends BaseAction implements
		ImportExcelService {
	private static final int roeExchange = 0;
	@Autowired
	private ImportExcelDao importExcelDao;
	@Autowired
	private IShopDao shopDao;
	@Autowired
	private IModelMapDao modelMapDao;

	@Autowired(required = false)
	@Qualifier("importSoService")
	private ImportSoService importPHExcelServiceByTest;
	@Autowired
	private ISellInDao sellInDao;
	static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	public String read2007Excel(File file,String what) throws IOException {
		String countrySale="";
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
			XSSFRow rowPrice = sheet.getRow(1);
			XSSFCell cells = null;
			List<HashMap<String, Object>> modelList = new LinkedList<HashMap<String, Object>>();
			Set<String> setModelDIS= new HashSet<String>();
			Set<String> setModelSTO= new HashSet<String>();
			for (int j = 4; j < ro.getLastCellNum(); j++) {
				
				HashMap<String, Object> hashMap = new HashMap<>();
				XSSFRow row = null;
				XSSFCell cell = null;
				if(ro.getLastCellNum()>=3){
					row = sheet.getRow(3);

				}
				cells = ro.getCell(j);
				XSSFRow whatRow = sheet.getRow(2);
				if (cells!=null &&
						cells.getCellType()!=HSSFCell.CELL_TYPE_BLANK &&
								getCellValueByCell(cells) != null
						&& !getCellValueByCell(cells).equals("")
						
						) {
					
					String countryName ="";
					String countryId="";
					String customerName ="";
					String customerId ="";
					String li = "";
					String cond="";
					for (int i = 3; i <= sheet.getPhysicalNumberOfRows(); i++) {
						XSSFRow rowCus = null;
						rowCus = sheet.getRow(i);
						if (rowCus == null) {
							continue;
						}
						
						if ( rowCus.getCell(0)!=null &&
								 rowCus.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK &&
										 rowCus.getCell(0)!= null
								&& ! getCellValueByCell(rowCus.getCell(0)).equals("")
								&& rowCus.getCell(2)!=null && !getCellValueByCell(rowCus.getCell(2)).equals("")
								&& rowCus.getCell(2).getCellType()!=HSSFCell.CELL_TYPE_BLANK
								) {
							countryName = getCellValueByCell(rowCus.getCell(0));
							countryId = importExcelDao.selectCountry(countryName);
							
							if(what.equals("customer")) {
								customerName = getCellValueByCell(rowCus.getCell(2));
								customerId = importExcelDao.selectCustomerByName(customerName,countryId);
							 cond = " AND t.`party_id`='"+countryId+"'";
								int model = modelMapDao.getModelIdByParty(cond,
										getCellValueByCell(cells), li);
								if (model==0) {
									Excel channelModel = importExcelDao.selectCustomerModel(getCellValueByCell(cells), countryId,customerId);
									if(channelModel==null){
										msg.append(customerName+"  "  +getText("excel.error.line") + DateUtil.getExcelColumnLabel(j +1)+" "
												+ getText("excel.error.model") +"("+ getCellValueByCell(cells)+")"+ "<br/>");
									}
								}
							
							}
							
							
							
						}
					}
					

					
						 cond = " AND t.`party_id`='"+countryId+"'";
						int model = modelMapDao.getModelIdByParty(cond,
								getCellValueByCell(cells), li);
						if (model >= 1) {
							hashMap.put("model", getCellValueByCell(cells));
							//判断为so or stock or  sample
							if(whatRow.getCell(j)!=null  &&
									whatRow.getCell(j).getCellType()!=HSSFCell.CELL_TYPE_BLANK &&
									!whatRow.getCell(j).equals("")
									){
								if(getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("sold")){
									hashMap.put("what", "Sold");
									if(rowPrice!=null && rowPrice.getCell(j)!=null
											 && rowPrice.getCell(j).getCellType()!=HSSFCell.CELL_TYPE_BLANK
													&& !rowPrice.getCell(j).equals("")
										 ){
										
										 switch (rowPrice.getCell(j).getCellType()) {
										 
									      case HSSFCell.CELL_TYPE_STRING:
									 
												msg.append(getText("excel.error.row") + (2) + " "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(j +1)+" "
														+ getText("excel.error.num") + "<br/>");
									 
									       break;
									 
									      case HSSFCell.CELL_TYPE_FORMULA:
									 
												msg.append(getText("excel.error.row") + (2) + " "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(j +1)+" "
														+ getText("excel.error.num") + "<br/>");
									  
									       break;
									 
									      case HSSFCell.CELL_TYPE_NUMERIC:
									 
									    	  if(rowPrice.getCell(j)
														.getNumericCellValue()!=0){
									    		  hashMap.put("price", rowPrice.getCell(j)
															.getNumericCellValue());
													
												}
									        break;
									 
									      case HSSFCell.CELL_TYPE_ERROR:
									 
									       break;
									 
									      }
								  
										
									}else  {
										if(			countryId!=null
												&& !countryId.equals("")){
											
											Float 	price =importExcelDao.selectPrice(countryId,getCellValueByCell(cells));
											if(price!=null && !price.equals("")  && price!=0 &&  price!=0.0){
												
												hashMap.put("price", price);
												
											}else{
												hashMap.put("price", 0);
													
												
											}
											
										}
								
									}
								}else if(getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("stocks")){
									hashMap.put("what", "Stocks");
									boolean one=setModelSTO.add(getCellValueByCell(cells));
									if(!one){
										msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1)+" "
												+ getText("excel.error.stoRE")+"  ("+getCellValueByCell(cells)+") " + "<br/>");
									}
									
								}else if(getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("display")){
									hashMap.put("what", "DisPlay");
									boolean one=setModelDIS.add(getCellValueByCell(cells));
									if(!one){
										msg.append(getText("excel.error.line") +DateUtil.getExcelColumnLabel (j + 1)+" "
												+ getText("excel.error.disRE")+"  ("+getCellValueByCell(cells)+") " + "<br/>");
									}
									
								}else{
									msg.append(getText("excel.error.row")+" "+3+" "+
								getText("excel.error.line") + DateUtil.getExcelColumnLabel(j +1)+" "+"("+getCellValueByCell(cells)+")"
											+ getText("excel.error.what") + "<br/>");
								}
								
								
							}
						
						}else{
							if(what.equals("customer")) {
								

							Excel channelModel = importExcelDao.selectCustomerModel(getCellValueByCell(cells), countryId,customerId);
							if(channelModel==null){
								msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j +1)+" "
										+ getText("excel.error.model") +"("+ getCellValueByCell(cells)+")"+ "<br/>");
							}else{
								hashMap.put("model", channelModel.getModel());
								//判断为so or stock or  sample
								if(whatRow.getCell(j)!=null  &&
										whatRow.getCell(j).getCellType()!=HSSFCell.CELL_TYPE_BLANK &&
										!whatRow.getCell(j).equals("")
										){
									if(getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("sold")){
										hashMap.put("what", "Sold");
										System.out.println(sheet.getRow(1));
										if(rowPrice!=null && rowPrice.getCell(j)!=null
												 && rowPrice.getCell(j).getCellType()!=HSSFCell.CELL_TYPE_BLANK
												
											 ){
											
											 switch (rowPrice.getCell(j).getCellType()) {
											 
										      case HSSFCell.CELL_TYPE_STRING:
										 
													msg.append(getText("excel.error.row") + (2)+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(j +1)+" "
															+ getText("excel.error.num") + "<br/>");
										 
										       break;
										 
										      case HSSFCell.CELL_TYPE_FORMULA:
										 
													msg.append(getText("excel.error.row") + (2)+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(j +1)+" "
															+ getText("excel.error.num") + "<br/>");
										  
										       break;
										 
										      case HSSFCell.CELL_TYPE_NUMERIC:
										 
										    	  if(rowPrice.getCell(j)
															.getNumericCellValue()!=0){
										    		  hashMap.put("price", rowPrice.getCell(j)
																.getNumericCellValue());
														
													}
										        break;
										 
										      case HSSFCell.CELL_TYPE_ERROR:
										 
										       break;
										 
										      }
									  
											
										}else  {
											if(			countryId!=null
													&& !countryId.equals("")){
												
												Float	price =importExcelDao.selectPrice(countryId,channelModel.getModel());
												if(price!=null && !price.equals("")  && price!=0 &&  price!=0.0){
													hashMap.put("price", price);
													
												}else{
													hashMap.put("price", 0);
														
													
												}
												
											}
									
										}
									}else if(getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("stocks")){
										hashMap.put("what", "Stocks");
										boolean one=setModelSTO.add(getCellValueByCell(cells));
										if(!one){
											msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1)+" "
													+ getText("excel.error.stoRE")+"  ("+getCellValueByCell(cells)+") " + "<br/>");
										}
										
									}else if(getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("display")){
										hashMap.put("what", "DisPlay");
										boolean one=setModelDIS.add(getCellValueByCell(cells));
										if(!one){
											msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1)+" "
													+ getText("excel.error.disRE")+"  ("+getCellValueByCell(cells)+") " + "<br/>");
										}
										
									}else{
										msg.append(getText("excel.error.row")+" "+3+" "+
									getText("excel.error.line") + DateUtil.getExcelColumnLabel(j +1)+" "+"("+ getCellValueByCell(cells)+")"
												+ getText("excel.error.what") + "<br/>");
									}
									
									
								}
							

							}
							
							
						}else {
							msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j +1)+" "
									+ getText("excel.error.model") +"("+ getCellValueByCell(cells)+")"+ "<br/>");
						}
						}
						

						modelList.add(hashMap);
						
				
				}
				
				
				

			}

			String userCountry=WebPageUtil.getLoginedUser().getPartyId();
			

			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 3; i <= sheet.getPhysicalNumberOfRows(); i++) {
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
					countryName = getCellValueByCell(row.getCell(0));
					countryId= importExcelDao.selectCountry(countryName);
					if (countryId == null  ) {
						msg.append(getText("excel.error.row") + (i + 1)+" "
								+ getText("excel.error.country") +"("+getCellValueByCell(row.getCell(0))+")"+ "<br/>");
					}else if(!userCountry.equals("999") && !userCountry.equals(countryId)) {
						msg.append(getText("excel.error.row") + (i + 1)+" "
								+ getText("excel.error.userCountry") +"("+getCellValueByCell(row.getCell(0))+")"+ "<br/>");
						
					}
					
				}else{
					if((row.getCell(2)!=null && row.getCell(2).getCellType()!=HSSFCell.CELL_TYPE_BLANK )
							
							|| 
							
							(row.getCell(1)!=null && row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK)){
						msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.line") +DateUtil.getExcelColumnLabel (1)+" "
								+ getText("excel.error.null") + "<br/>");
						
					}
				}
				
				
				String shopName="";
				Excel shop =null;
				
				String customerName="";
				String customer =null;
				if(what.equals("shop")){
					if(row.getCell(2)!=null  && row.getCell(2).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						shopName =getCellValueByCell(row.getCell(2));
						shop = importExcelDao.getShopByNames(shopName);
					if (shop == null) {
						msg.append(getText("excel.error.row") + (i + 1)+" "
								+ getText("excel.error.shop")+"("+ getCellValueByCell(row.getCell(2))+")"+"<br/>");
					}
					
					}else{
						if((row.getCell(0)!=null && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK)
								
								
								|| 
								(row.getCell(1)!=null && row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK)){
							msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(3)+" "
									+ getText("excel.error.null") + "<br/>");
							
						}
					}
					
					
				}else if(what.equals("customer")){
					if(row.getCell(2)!=null  && row.getCell(2).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						customerName =getCellValueByCell(row.getCell(2));
						customer = importExcelDao.selectCustomerByName(customerName,countryId);
					if (customer == null || customer.equals("")) {
						msg.append(getText("excel.error.row") + (i + 1)
								+ getText("excel.error.customer")+"("+ getCellValueByCell(row.getCell(2))+")"+"<br/>");
					}
					
					}else{
						if((row.getCell(0)!=null && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK)
								
								
								|| 
								(row.getCell(1)!=null && row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK)){
							msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.line") + DateUtil.getExcelColumnLabel(3)+" "
									+ getText("excel.error.null") + "<br/>");
							
						}
					}
				}
				
				String branchId ="";
				if(row.getCell(1)!=null  && row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
				// 这里需要根据partyName查询partyID
				 branchId = importExcelDao.getPartyIdByName(
						 getCellValueByCell(row.getCell(1))
						);
				if (branchId == null) {
					msg.append(getText("excel.error.row") + (i + 1)+" "
							+ getText("excel.error.branch") +"("+  getCellValueByCell(row.getCell(1))
							 +")"+ "<br/>");
				}
				}/*else{
					if((row.getCell(2)!=null && row.getCell(2).getCellType()!=HSSFCell.CELL_TYPE_BLANK)
							
							
							||
							( row.getCell(0)!=null && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK)){
						msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.line") +DateUtil.getExcelColumnLabel (2)+" "
								+ getText("excel.error.null") + "<br/>");
						
					}
				}*/
				
				
				SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date date = new Date();
				String dt = dfd.format(date);
				Date dt1 = dfd.parse(dt);
				Date dt2;
				String rowDate="";
				Float rowExchange=(float) 0;
				
				if(  	row.getCell(0)!=null && row.getCell(2)!=null
						&&
						row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						
						&& row.getCell(2).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					
					
					if(row.getCell(3)!=null  && row.getCell(3).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						try{
							format.setLenient(false);
							date = format.parse( getCellValueByCell(row.getCell(3))
									);//有异常要捕获
							dfd.setLenient(false);
							String newD = dfd.format(date);
							date = dfd.parse(newD);//有异常要捕获
								dt2 =dfd.parse(newD);
								if (dt1.getTime() < dt2.getTime()) {
									msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(3+1)+" "
											+ getText("excel.error.time") + "<br/>");
								}else{
									rowDate=newD;
								
									
								}
						}catch(Exception e){
							msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(3+1)+" "
									+ getText("excel.error.date") + "<br/>");
						}
						
							
						
				
					}else{
						msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(3+1)+" "
								+ getText("excel.error.dateNo") + "<br/>");
					}
						
					
				}
			
				
				for (int m = 0; m < modelList.size(); m++) {
					
						
					if( modelList.get(m).get("what")!=null &&  !modelList.get(m).get("what").toString().equals("")){
						
						if(row.getCell(m+ 4)!=null  
								 && row.getCell(m+ 4).getCellType()!=HSSFCell.CELL_TYPE_BLANK
								
								){
							 switch (row.getCell(m+ 4).getCellType()) {
							
							 case HSSFCell.CELL_TYPE_STRING:
						    	  msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(m+5)+" "
											+ getText("excel.error.num") + "<br/>");
						       break;
						 
						      case HSSFCell.CELL_TYPE_FORMULA:
						 
						    	  msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(m+5)+" "
											+ getText("excel.error.num") + "<br/>");
						  
						       break;
						 
						      case HSSFCell.CELL_TYPE_NUMERIC:
						    	 
						    	  if(row.getCell(m+4)
											.getNumericCellValue()!=0){
						    		  HashMap<String, Object> modelMap = new HashMap<String, Object>();
						    		  modelMap.put("Model", modelList.get(m).get("model"));
						    		  if (countryId != null) {
						    			   countrySale=countryId;
											modelMap.put("Country", countryId == null ? null
													: countryId);
											modelMap.put("CountryName", countryName);
										}
									
						    		  if(what.equals("shop")){
						    			  if (shop != null) {
												modelMap.put("Store", shop.getShopId() == null ? null
														: shop.getShopId());
												modelMap.put("StoreName", shopName);
											}

						    		  }else if(what.equals("customer")){
						    			  if (customer != null && !customer.equals("")) {
												modelMap.put("Customer", customer);
												modelMap.put("CustomerName",customerName);
											}

						    			  
						    		  }
										
						
										if (branchId != null) {
											modelMap.put("Branch", branchId == null ? null : branchId);
										}
									
									
								
							
									if(rowDate!=null && !rowDate.equals("")){
										
										modelMap.put("DataDate", rowDate);
									
										if ( countryId!=null && !countryId.equals("")	
												&&
												modelList.get(m).get("price")!=null 
												&&  !modelList.get(m).get("price").toString().equals("")
												&&
												Double.parseDouble(modelList.get(m).get("price").toString())!=0.0
												) {
														String []days=rowDate.split("-");
														String begin=days[0]+"-"+days[1]+"-01";
														String end=days[0]+"-"+days[1]+"-31";
														Float exchange=importExcelDao.selectExchange(countryId, begin, end,rowDate);
														if(exchange==null){
															msg.append(countryName+" " 
																	+ getText("excel.error.exchange")+" "+days[1]+"/"+days[0]+ "<br/>");
														}else{
															modelMap.put("exchange", exchange);
														}
														
												}
									}
									
									
								/*	if(rowExchange!=null` && rowExchange!=0  && rowExchange!=0.0){
										modelMap.put("exchange", rowExchange);
									}*/
									if( modelList.get(m).get("price")!=null &&  !modelList.get(m).get("price").toString().equals("")){
										modelMap.put("Price", modelList.get(m).get("price"));
										
									}
						    		  if( modelList.get(m).get("what").toString().equals("Sold")){
						    			  modelMap.put("sold", (int) row.getCell(m+ 4)
													.getNumericCellValue());
										}else if(modelList.get(m).get("what").toString().equals("DisPlay")){
											
											  modelMap.put("display", (int) row.getCell(m+ 4)
														.getNumericCellValue());
										}else if(modelList.get(m).get("what").toString().equals("Stocks")){
											
											  modelMap.put("stocks", (int) row.getCell(m+ 4)
														.getNumericCellValue());
										}
										
						    		  allModelList.add(modelMap);
										
									}
						       
						    	  
						 
						        break;
						 
						      case HSSFCell.CELL_TYPE_ERROR:
						 
						       break;
						 
						      }
							
							
						
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
			System.out.println("==========size==========="+allModelList);
			List<ImportExcel> excelList=new ArrayList<ImportExcel>();
			List<ImportExcel> stockList=new ArrayList<ImportExcel>();
			List<ImportExcel> stockListByupdate=new ArrayList<ImportExcel>();
			List<ImportExcel> displayList=new ArrayList<ImportExcel>();
			List<ImportExcel> displayListRe=new ArrayList<ImportExcel>();
			List<ImportExcel> displayListReByupdate=new ArrayList<ImportExcel>();
			List<ImportExcel> displayListByupdate=new ArrayList<ImportExcel>();
			
			
			System.out.println("=========allModelList==========="+allModelList);
			if(msg.length()<=0){
			for (int i = 0; i < allModelList.size(); i++) {
				int row = 0;
				
					if (
							
							((allModelList.get(i)
									.get("Store")!=null
									&&allModelList.get(i)
									.get("Store")!="") ||
									(allModelList.get(i)
											.get("Customer")!=null
											&&allModelList.get(i)
											.get("Customer")!="")
									
									)
							
							&& allModelList.get(i).get("Model")!=null
							&& allModelList.get(i).get("Model")!=""
							
							
							&& allModelList.get(i).get("Country")!=null
							&& allModelList.get(i).get("Country")!=""
							&& allModelList.get(i).get("DataDate")!=null
							&& allModelList.get(i).get("DataDate")!=""	
							&& userId!=null && userId!=""
					
					) {
						ImportExcel excel = new ImportExcel();
						excel.setUserId(userId);
						excel.setModel(allModelList.get(i).get("Model").toString());
						System.out.println(allModelList.get(i).get("DataDate"));
						excel.setDataDate(allModelList.get(i).get("DataDate")
								.toString());
						if(what.equals("shop")){
							excel.setShopId(Integer.parseInt(allModelList.get(i)
									.get("Store").toString()));
						}else if(what.equals("customer")){
							excel.setCustomerId(allModelList.get(i)
									.get("Customer").toString());
						}
						if( allModelList.get(i).get("Branch")!=null &&
								allModelList.get(i).get("Branch")!="") {
							excel.setBranchId(allModelList.get(i).get("Branch").toString());
						}
						
						excel.setCountryId(allModelList.get(i).get("Country")
								.toString());
						
				if(
						allModelList.get(i).get("sold") != null
						&& Integer.parseInt(allModelList.get(i).get("sold")
								.toString()) != 0
						&& allModelList.get(i)
						.get("Price")!=null  ){
					
					BigDecimal bd = new BigDecimal(allModelList.get(i)
							.get("sold").toString()); 
				
					int qty=bd.intValue();
					excel.setSaleQty(bd.intValue());
					excel.setSource("PC");
					
					java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
					nf.setGroupingUsed(false);  
					
					
					excel.setSalePrice(Double.parseDouble(nf.format(Double.parseDouble(allModelList.get(i)
							.get("Price").toString()))));
					
					double price=Double.parseDouble(nf.format(Double.parseDouble(allModelList.get(i)
							.get("Price").toString())));
					
					
					bd = new BigDecimal(qty*price); 
					
					excel.setAmt(String.valueOf(bd.longValue()));
					excel.setH_qty(qty);
					if(allModelList.get(i).get("exchange")!=null 
							&& 
							Float.parseFloat(allModelList.get(i).get("exchange").toString())!=0
							){
						
						Float exchange=Float.parseFloat(allModelList.get(i).get("exchange").toString());
						
						if(exchange!=null){
							bd = new BigDecimal(price*exchange); 
							double Hprice=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							excel.setH_price(Hprice);
							bd = new BigDecimal(qty*Hprice);
							BigDecimal HAmt=bd.setScale(2, BigDecimal.ROUND_HALF_UP);
							excel.setH_amt(String.valueOf(HAmt));
						}
						
					}else {
						excel.setH_price(0);
						excel.setH_amt(String.valueOf('0'));
					}
					
				excelList.add(excel);
				System.out.println("======listSize============"+excelList.size());
						/*row = importExcelDao.saveSales(excel);*/
						/*if (row == 0) {
							msg.append(shopName + ":"
									+ getText("excel.error.saleInsert") + "<br/>");

						}*/
					
				}else if(allModelList.get(i).get("stocks") != null
						&& Integer.parseInt(allModelList.get(i).get("stocks")
								.toString()) != 0){
					excel.setStockQty(Integer.parseInt(allModelList.get(i)
							.get("stocks").toString()));
				
					if(what.equals("shop")){
						importExcelDao.deleteStockCountByRE(allModelList
								.get(i).get("Store").toString(),
								excel.getModel(),allModelList.get(i).get("DataDate")
								.toString());
					}else if(what.equals("customer")){
						importExcelDao.deleteStockCountByRECUS(allModelList
								.get(i).get("Customer").toString(),
								excel.getModel(),allModelList.get(i).get("DataDate")
								.toString());
					}
					
					
					
					
					//if(rowsRE==null){
						excel.setClassId(1);
						stockList.add(excel);
					/*}else{
						excel.setId(rowsRE);
						stockListByupdate.add(excel);
					}*/
					
					
				}else if(allModelList.get(i).get("display") != null
						&& Integer.parseInt(allModelList.get(i).get("display")
								.toString()) != 0){
					excel.setDisPlayQty(Integer.parseInt(allModelList.get(i)
							.get("display").toString()));
					
					if(what.equals("shop")){
						
					 importExcelDao.deleteDisplayCountByRE(allModelList
								.get(i).get("Store").toString(),
								excel.getModel(),allModelList.get(i).get("DataDate")
								.toString());
					}else if(what.equals("customer")){
						
					
					 importExcelDao.deleteDisplayCountByRECUS(allModelList
								.get(i).get("Customer").toString(),
								excel.getModel(),allModelList.get(i).get("DataDate")
								.toString());
					 
						
					}
					
					
				
						excel.setClassId(1);
						displayListRe.add(excel);
					
			
					
				}
				
				
			}
				}
				
					// 判断该机型该门店是否存在，存在的话就修改，不存在就插入
			}
			
			if(stockList.size()>0){
				if(what.equals("shop")){
					DateUtil.remove(stockList); 
					 
					}else if(what.equals("customer")){
						
						
						DateUtil.removeCus(stockList); 
						
					}
				
				
				importExcelDao.saveStocks(stockList);
			}
			
			if(excelList.size()>0){
				int row = importExcelDao.saveSales(excelList);
				if(row>0){
					DateUtil.ListSort(excelList);
					String [] beginDate=excelList.get(0).getDataDate().split("-");
					String [] endDate=excelList.get(excelList.size()-1).getDataDate().split("-");
					
					String beg=beginDate[0]+"-"+beginDate[1]+"-01";
					String end=endDate[0]+"-"+endDate[1]+"-31";
					String countryList="";
					/*for (int i = 0; i < excelList.size()-1; i++) {
			            for (int j = excelList.size()-1; j > i; j--) {
			                if (excelList.get(j).getDataDate()== excelList.get(i).getDataDate()) {
			                	excelList.remove(j);
			                }else{
			                	
			                	countryList+="'"+excelList.get(j).getDataDate()+"',";
			                }
			            }
			        }
					countryList = countryList.substring(0, countryList.length() - 1);  */
					if(what.equals("shop")){
						importExcelDao.insertTVSaleVive(countrySale, beg, end);
						importExcelDao.insertACSaleVive(countrySale, beg, end);
						importExcelDao.saleRe(countrySale, beg, end);
						importExcelDao.updateStocksByShop(excelList);
					}else if(what.equals("customer")) {
						importExcelDao.updateStocksByCustomer(excelList);
					}
				
				}
			}
			if(displayListRe.size()>0){
				if(what.equals("shop")){
					DateUtil.remove(displayListRe); 
					 
					}else if(what.equals("customer")){
						
						
						DateUtil.removeCus(displayListRe); 
						
					}
				
				
				
				importExcelDao.saveDisPlaysByRe(displayListRe);
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
	public String readExcel(File file, String fileName,String what) throws IOException {
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		} else if ("xlsx".equals(extension)) {
			if(what.equals("country")){
				return read2007ExcelByCountry(file, what);
			}else{
				if (Integer.parseInt(WebPageUtil.getLoginedUser().getPartyId()) == 16) {
					if(what.equals("local")) {
						return importPHExcelServiceByTest.read2007ExcelByPK(file);
					}else {
						return read2007ExcelByPK(file);
					}
					

				} else if (Integer.parseInt(WebPageUtil.getLoginedUser().getPartyId()) == 19) {
					
					if(what.equals("local")) {
						return importPHExcelServiceByTest.read2007ExcelByVn(file, "shop");
					}else {
						return read2007Excel(file, "shop");
					}
					
				

				} else if (Integer.parseInt(WebPageUtil.getLoginedUser().getPartyId()) == 5) {
					if(what.equals("local")) {
						return importPHExcelServiceByTest.read2007ExcelByPh(file, "shop");
					}else {
						return read2007Excel(file, "shop");
					}
					
					

				} 	 else if (Integer.parseInt(WebPageUtil.getLoginedUser().getPartyId()) == 17) {
					if(what.equals("local")) {
						return importPHExcelServiceByTest.read2007ExcelByIn(file, "shop");
					}else {
						return read2007Excel(file, "shop");
					}
					

				} /*else if (Integer.parseInt(WebPageUtil.getLoginedUser().getPartyId()) == 4) {
					return importPHExcelServiceByTest.read2007ExcelByThCustomer(file, what);

				}*/else {
					return read2007Excel(file, what);
				}
				
				
			}
			
		} else {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		}
	}

	@Override
	public void deleteCore(String countryId)throws Exception {
		 importExcelDao.deleteCore(countryId);
	}

	@Override
	public void insertCore(String countryId, String line) throws Exception {
		importExcelDao.insertCore(countryId, line);
	}

	@Override
	public List<HashMap<String, Object>> selectCore(String countryId) throws Exception {
		return importExcelDao.selectCore(countryId);
	}

	

	@Override
	public XSSFWorkbook exporExcel(String spec,String conditions, String[] excelHeader,
			String title) throws Exception {
		String key=null;
		List<Shop> list=shopDao.selectShopName(conditions,null);
		
		List<HashMap<String, Object>>  modelList=importExcelDao.selectModel(null,conditions,spec);
		//设置 表头宽度
		int[] excelWidth = {120,180,200,120,100,100,100,100};
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		 XSSFSheet sheet = workbook.createSheet(title);
		 XSSFSheet sheetNotice = workbook.createSheet("NOTICE");
		 sheet.createFreezePane(4,3,4,3)  ;
		// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
		sheet.addMergedRegion(new CellRangeAddress(0, 2,0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 2,1, 1));

		sheet.addMergedRegion(new CellRangeAddress(0, 2,2, 2));
		sheet.addMergedRegion(new CellRangeAddress(0, 2,3, 3));

		 
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
         
         
         
         XSSFCellStyle styleYellow = workbook.createCellStyle();    
         styleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleYellow.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         styleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
         styleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
         styleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
         styleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
         styleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
         styleYellow.setRightBorderColor(HSSFColor.BLACK.index);
         styleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
         styleYellow.setTopBorderColor(HSSFColor.BLACK.index);
         styleYellow.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
         styleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
         styleYellow.setFont(font);
         
         

         XSSFCellStyle styleGreen = workbook.createCellStyle();    
         styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleGreen.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
         styleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
         styleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
         styleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
         styleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
         styleGreen.setRightBorderColor(HSSFColor.BLACK.index);
         styleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
         styleGreen.setTopBorderColor(HSSFColor.BLACK.index);
         styleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
         styleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
         styleGreen.setFont(font);
         
         
         
         
         
         
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
         
         rowNotice=sheetNotice.createRow(6);
         XSSFCell cellNoticeSI = rowNotice.createCell(3);
         cellNoticeSI.setCellValue("3. Please don't rename the file, in case it can't be uploaded.");
         cellNoticeSI.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(7);
         XSSFCell cellNoticeSE = rowNotice.createCell(3);
         cellNoticeSE.setCellValue("3. 请不要重命名文件，以防无法顺利导入。");
         cellNoticeSE.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(8);
         XSSFCell cellNoticeEI = rowNotice.createCell(3);
         cellNoticeEI.setCellValue("4. Fill in the single price of the product in row 2.");
         cellNoticeEI.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(9);
         XSSFCell cellNoticeNI = rowNotice.createCell(3);
         cellNoticeNI.setCellValue("4. 请在第2行填写产品单价。");
         cellNoticeNI.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(10);
         XSSFCell cellNoticeTe = rowNotice.createCell(3);
         cellNoticeTe.setCellValue("5. Fill in Sold/Stocks/Display in row 3.");
         cellNoticeTe.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(11);
         XSSFCell cellNoticeEL = rowNotice.createCell(3);
         cellNoticeEL.setCellValue("5. 请在第3行填写Sold/Stocks/Display。");
         cellNoticeEL.setCellStyle(styleNo);
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
         XSSFRow rowTwo = sheet.createRow(1);
         XSSFRow rowThree = sheet.createRow(2);
         
         for (int i = 0; i < modelList.size(); i++) {
        	 
        	 
             

        	 if(modelList.get(i).get("branch_model")!=null  && !modelList.get(i).get("branch_model").equals("")){
        		 XSSFCell  cell0 = row.createCell(4+i);
                 cell0.setCellValue(modelList.get(i).get("branch_model").toString());    
                 cell0.setCellStyle(styleBlue);
                 
              
             }
        	 
        	 if(modelList.get(i).get("price")!=null  && Double.parseDouble(modelList.get(i).get("price").toString())!=0.0){
        		XSSFCell cell= rowTwo.createCell(4+i);
        		cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));    
        		cell.setCellStyle(styleBlue);  
             }
        	 
          	 XSSFCell  cellOne = rowThree.createCell(4+i);
               cellOne.setCellValue("Sold");    
               cellOne.setCellStyle(styleBlue);
        	 
		}
         int size;
         if(modelList.size()<=2){
        	 size=modelList.size();
         }else{
        	 size=2;
         }
         
         for (int i = 0; i < size; i++) {
        	 
             

        	 if(modelList.get(i).get("branch_model")!=null  && !modelList.get(i).get("branch_model").equals("")){
        		 XSSFCell  cell0 = row.createCell(4+i+modelList.size());
                 cell0.setCellValue(modelList.get(i).get("branch_model").toString());    
                 cell0.setCellStyle(styleGreen);
                 
              
             }
        	 
        	 if(modelList.get(i).get("price")!=null  && Double.parseDouble(modelList.get(i).get("price").toString())!=0.0){
        		XSSFCell cell= rowTwo.createCell(4+i+modelList.size());
        		cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));    
        		cell.setCellStyle(styleGreen);  
             }
        	 
          	 XSSFCell  cellOne = rowThree.createCell(4+i+modelList.size());
               cellOne.setCellValue("Display");    
               cellOne.setCellStyle(styleGreen);
        	 
		}
         
         	for (int i = 0; i < size; i++) {
        	 
             

        	 if(modelList.get(i).get("branch_model")!=null  && !modelList.get(i).get("branch_model").equals("")){
        		 XSSFCell  cell0 = row.createCell(4+i+modelList.size()+size);
                 cell0.setCellValue(modelList.get(i).get("branch_model").toString());    
                 cell0.setCellStyle(styleYellow);
                 
              
             }
        	 
        	 if(modelList.get(i).get("price")!=null  && Double.parseDouble(modelList.get(i).get("price").toString())!=0.0){
        		XSSFCell cell= rowTwo.createCell(4+i+modelList.size()+size);
        		cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));    
        		cell.setCellStyle(styleYellow);  
             }
        	 
          	 XSSFCell  cellOne = rowThree.createCell(4+i+modelList.size()+size);
               cellOne.setCellValue("Stocks");    
               cellOne.setCellStyle(styleYellow);
        	 
		}

         
         //表体数据
         for (int i = 0; i < list.size(); i++) {    
             row = sheet.createRow(i + 3);    
             Shop shop = list.get(i);
             
             //-------------单元格-------------------
             
             /**
              * 国家
              */
             if(i==0){
                 XSSFCell cell = row.createCell(3);
                 cell.setCellValue("dd/MM/yyyy");    
                 cell.setCellStyle(styleTwo);
             }
             
             
             if(shop.getCountryName()!=null  && !shop.getCountryName().equals("")){
            	 XSSFCell cell0 = row.createCell(0);
                 cell0.setCellValue(shop.getCountryName());    
                 cell0.setCellStyle(styleTwo);
             }
            
             
             if(shop.getShopName()!=null  && !shop.getShopName().equals("")){
            	 XSSFCell cell0 = row.createCell(2);
                 cell0.setCellValue(shop.getShopName());    
                 cell0.setCellStyle(styleTwo);
             }
             
             if(shop.getPartyName()!=null  && !shop.getPartyName().equals("")){
            	 XSSFCell cell0 = row.createCell(1);
                 cell0.setCellValue(shop.getPartyName());    
                 cell0.setCellStyle(styleTwo);
             }
            
             
            
             
            
         }    
         
         
         return workbook;
	}

	@Override
	public XSSFWorkbook exporExcelByPK(String spec, String conditions,
			String[] excelHeader, String title) throws Exception {
		String key=null;
		List<Shop> list=shopDao.selectShopName(conditions,null);
		
		List<HashMap<String, Object>>  modelList=importExcelDao.selectModel(null,conditions,spec);
		//设置 表头宽度
		int[] excelWidth = {120,180,200,120,100,100,100,100};
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		 XSSFSheet sheet = workbook.createSheet(title);
		 XSSFSheet sheetNotice = workbook.createSheet("NOTICE");
		 sheet.createFreezePane(4,3,4,3)  ;
		// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
		sheet.addMergedRegion(new CellRangeAddress(0, 2,0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 2,1, 1));

		sheet.addMergedRegion(new CellRangeAddress(0, 2,2, 2));
		sheet.addMergedRegion(new CellRangeAddress(0, 2,3, 3));
		sheet.addMergedRegion(new CellRangeAddress(0, 2,4, 4));

		 
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
         
         
         
         XSSFCellStyle styleYellow = workbook.createCellStyle();    
         styleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleYellow.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         styleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
         styleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
         styleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
         styleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
         styleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
         styleYellow.setRightBorderColor(HSSFColor.BLACK.index);
         styleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
         styleYellow.setTopBorderColor(HSSFColor.BLACK.index);
         styleYellow.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
         styleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
         styleYellow.setFont(font);
         
         

         XSSFCellStyle styleGreen = workbook.createCellStyle();    
         styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleGreen.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
         styleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
         styleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
         styleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
         styleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
         styleGreen.setRightBorderColor(HSSFColor.BLACK.index);
         styleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
         styleGreen.setTopBorderColor(HSSFColor.BLACK.index);
         styleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
         styleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
         styleGreen.setFont(font);
         
         
         
         
         
         
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
         		
         		

         rowNotice=sheetNotice.createRow(6);
         XSSFCell cellNoticeSI = rowNotice.createCell(3);
         cellNoticeSI.setCellValue("3. Please don't rename the file, in case it can't be uploaded.");
         cellNoticeSI.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(7);
         XSSFCell cellNoticeSE = rowNotice.createCell(3);
         cellNoticeSE.setCellValue("3. 请不要重命名文件，以防无法顺利导入。");
         cellNoticeSE.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(8);
         XSSFCell cellNoticeEI = rowNotice.createCell(3);
         cellNoticeEI.setCellValue("4. Fill in the single price of the product in row 2.");
         cellNoticeEI.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(9);
         XSSFCell cellNoticeNI = rowNotice.createCell(3);
         cellNoticeNI.setCellValue("4. 请在第2行填写产品单价。");
         cellNoticeNI.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(10);
         XSSFCell cellNoticeTe = rowNotice.createCell(3);
         cellNoticeTe.setCellValue("5. Fill in Sold/Stocks/Display in row 3.");
         cellNoticeTe.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(11);
         XSSFCell cellNoticeEL = rowNotice.createCell(3);
         cellNoticeEL.setCellValue("5. 请在第3行填写Sold/Stocks/Display。");
         cellNoticeEL.setCellStyle(styleNo);
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
         XSSFRow rowTwo = sheet.createRow(1);
         XSSFRow rowThree = sheet.createRow(2);
         
         for (int i = 0; i < modelList.size(); i++) {
        	 
        	 //开始行，结束行，开始列，结束列
        	 sheet.addMergedRegion(new CellRangeAddress(0, 0,i * 3 + 5, i * 3 + 7));

        	 if(modelList.get(i).get("branch_model")!=null  && !modelList.get(i).get("branch_model").equals("")){
        		 XSSFCell  cell0 = row.createCell(i * 3 + 5);
                 cell0.setCellValue(modelList.get(i).get("branch_model").toString());    
                 cell0.setCellStyle(styleBlue);
                 
              
             }
        	 
        	 if(modelList.get(i).get("branch_model")!=null  && !modelList.get(i).get("branch_model").equals("")){
        		 XSSFCell  cell0 = row.createCell(i * 3 + 6);
                 cell0.setCellValue(modelList.get(i).get("branch_model").toString());    
                 cell0.setCellStyle(styleBlue);
                 
              
             }
        	 if(modelList.get(i).get("branch_model")!=null  && !modelList.get(i).get("branch_model").equals("")){
        		 XSSFCell  cell0 = row.createCell(i * 3 + 7);
                 cell0.setCellValue(modelList.get(i).get("branch_model").toString());    
                 cell0.setCellStyle(styleBlue);
                 
              
             }
        	 
        	 
        	 if(modelList.get(i).get("price")!=null  && Double.parseDouble(modelList.get(i).get("price").toString())!=0.0){
        		XSSFCell cell= rowTwo.createCell(i * 3 + 5);
        		cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));    
        		cell.setCellStyle(styleYellow);  
             }else{
            	 XSSFCell cell= rowTwo.createCell(i * 3 + 5);
         		cell.setCellValue("price");    
         		cell.setCellStyle(styleYellow);  
             }
        	 
          	 
        	   XSSFCell  cellOne = rowThree.createCell(i * 3 + 5);
               cellOne.setCellValue("Sold");    
               cellOne.setCellStyle(styleGreen);
               
               XSSFCell  cellTwo = rowThree.createCell(i * 3 + 6);
               cellTwo.setCellValue("Stocks");    
               cellTwo.setCellStyle(styleGreen);
               
               XSSFCell  cellThree = rowThree.createCell(i * 3 + 7);
               cellThree.setCellValue("Display");    
               cellThree.setCellStyle(styleGreen);
               
               
        	 
		}
        
         
         
         //表体数据
         for (int i = 0; i < list.size(); i++) {    
             row = sheet.createRow(i + 3);    
             Shop shop = list.get(i);
             
             //-------------单元格-------------------
             
             /**
              * 国家
              */
             if(i==0){
                 XSSFCell cell = row.createCell(4);
                 cell.setCellValue("dd/MM/yyyy");    
                 cell.setCellStyle(styleTwo);
             }
             
             
             if(shop.getCountryName()!=null  && !shop.getCountryName().equals("")){
            	 XSSFCell cell0 = row.createCell(0);
                 cell0.setCellValue(shop.getCountryName());    
                 cell0.setCellStyle(styleTwo);
             }
            
             if(shop.getPartyName()!=null  && !shop.getPartyName().equals("")){
            	 XSSFCell cell0 = row.createCell(1);
                 cell0.setCellValue(shop.getPartyName());    
                 cell0.setCellStyle(styleTwo);
             }
             
             
             if(shop.getCustomerId()!=null  && !shop.getCustomerId().equals("")){
            	 XSSFCell cell0 = row.createCell(2);
                 cell0.setCellValue(shop.getCustomerId());    
                 cell0.setCellStyle(styleTwo);
             }
             
           
             

             
             if(shop.getShopName()!=null  && !shop.getShopName().equals("")){
            	 XSSFCell cell0 = row.createCell(3);
                 cell0.setCellValue(shop.getShopName());    
                 cell0.setCellStyle(styleTwo);
             }
             
            
             
            
             
            
         }    
         
         
         return workbook;
         }

	@Override
	public String read2007ExcelByPK(File file) throws IOException {
		System.out.println("=======read2007ExcelByPK========");
		String countrySale="";
		StringBuffer msg = new StringBuffer();
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			Object value = null;
			XSSFRow ro = sheet.getRow(0);
			XSSFRow roc = sheet.getRow(3);
			XSSFCell cells = null;
			XSSFRow rowPrice = sheet.getRow(1); 
			XSSFRow whatRow = sheet.getRow(2); 
			
			List<HashMap<String, Object>> modelList = new LinkedList<HashMap<String, Object>>();
			Set<String> setModelDIS= new HashSet<String>();
			Set<String> setModelSTO= new HashSet<String>();
			for (int j = 5; j < ro.getLastCellNum(); j++) {
				HashMap<String, Object> hashMap = new HashMap<>();
				cells = ro.getCell(j);
				if(cells==null) {
					continue;
				}
				if(ro.getLastCellNum()>=3){
					roc = sheet.getRow(3);

				}

				if (cells!=null &&
						cells.getCellType()!=HSSFCell.CELL_TYPE_BLANK &&
								getCellValueByCell(cells) != null
						&& !getCellValueByCell(cells).equals("")
						&& roc.getCell(0)!=null && !getCellValueByCell(roc.getCell(0)).equals("")
						&& roc.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK) {
					String countryName =getCellValueByCell(roc.getCell(0));
					String countryId = importExcelDao.selectCountry(countryName);
					String li = "";
					String cond = " AND t.`party_id`='"+countryId+"'";
					
					int model = modelMapDao.getModelIdByParty(cond,
							getCellValueByCell(cells), li);
					if (model >= 1) {
						hashMap.put("model", getCellValueByCell(cells));
					
						if(whatRow.getCell(j)!=null  &&
								whatRow.getCell(j).getCellType()!=HSSFCell.CELL_TYPE_BLANK &&
								!whatRow.getCell(j).equals("")
								){
							if(getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("sold")){
								hashMap.put("what", "Sold");
								//cellsPrice=rowPrice.getCell(j);
								if(rowPrice!=null &&  rowPrice.getCell(j)!=null
										 && rowPrice.getCell(j).getCellType()!=HSSFCell.CELL_TYPE_BLANK
										
									 ){
									
									 switch (rowPrice.getCell(j).getCellType()) {
									 
								      case HSSFCell.CELL_TYPE_STRING:
								 
											msg.append(getText("excel.error.row") + (2)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(j +1)+" "
													+ getText("excel.error.num") + "<br/>");
								 
								       break;
								 
								      case HSSFCell.CELL_TYPE_FORMULA:
								 
											msg.append(getText("excel.error.row") + (2)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(j +1)+" "
													+ getText("excel.error.num") + "<br/>");
								  
								       break;
								 
								      case HSSFCell.CELL_TYPE_NUMERIC:
								 
								    	  hashMap.put("price", rowPrice.getCell(j)
													.getNumericCellValue());
								    	  
								        break;
								 
								      case HSSFCell.CELL_TYPE_ERROR:
								 
								       break;
								 
								      }
							  
									
								}else  {
									if(			countryId!=null
											&& !countryId.equals("")){
										
										Float	price =importExcelDao.selectPrice(countryId,getCellValueByCell(cells));
										if(price!=null && !price.equals("") ){
											hashMap.put("price", price);
											
										}else{
											hashMap.put("price", 0);
												/* msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j +1)+" "+getCellValueByCell(cells)+" "
															+ getText("excel.error.price") + "<br/>");*/
											
										}
										
									}
							
								}
							}else if(getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("stocks")){
								hashMap.put("what", "Stocks");
								
								boolean one=setModelSTO.add(getCellValueByCell(cells));
								if(!one){
									msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1)+" "
											+ getText("excel.error.stoRE")+"  ("+getCellValueByCell(cells)+") " + "<br/>");
									
								}
								
							}else if(getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("display")){
								hashMap.put("what", "DisPlay");
								boolean one=setModelDIS.add(getCellValueByCell(cells));
								if(!one){
									msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1)+" "
											+ getText("excel.error.disRE")+"  ("+getCellValueByCell(cells)+") " + "<br/>");
								}
								
							}else{
								msg.append(getText("excel.error.row")+" "+3+" "+
							getText("excel.error.line") + DateUtil.getExcelColumnLabel(j +1)+" "+"("+ getCellValueByCell(cells)+")"
										+ getText("excel.error.what") + "<br/>");
							}
							
							
						}
					}
					if (model == 0) {

						SellIn channelModel = sellInDao.selectCustomerModel(getCellValueByCell(cells), countryId,"");
						if(channelModel==null){
							msg.append(getText("excel.error.line") +DateUtil.getExcelColumnLabel (j +1)+" "
									+ getText("excel.error.model") +"("+ getCellValueByCell(cells)+")"+ "<br/>");
						}else{
							hashMap.put("model", channelModel.getModel());
							
							if(whatRow.getCell(j)!=null  &&
									whatRow.getCell(j).getCellType()!=HSSFCell.CELL_TYPE_BLANK &&
									!whatRow.getCell(j).equals("")
									){
								if(getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("sold")){
									hashMap.put("what", "Sold");
									//cellsPrice=rowPrice.getCell(j);
									if(rowPrice!=null &&  rowPrice.getCell(j)!=null
											 && rowPrice.getCell(j).getCellType()!=HSSFCell.CELL_TYPE_BLANK
											
										 ){
										
										 switch (rowPrice.getCell(j).getCellType()) {
										 
									      case HSSFCell.CELL_TYPE_STRING:
									 
												msg.append(getText("excel.error.row") + (2)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(j +1)+" "
														+ getText("excel.error.num") + "<br/>");
									 
									       break;
									 
									      case HSSFCell.CELL_TYPE_FORMULA:
									 
												msg.append(getText("excel.error.row") + (2)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(j +1)+" "
														+ getText("excel.error.num") + "<br/>");
									  
									       break;
									 
									      case HSSFCell.CELL_TYPE_NUMERIC:
									 
									    	
									    		  hashMap.put("price", rowPrice.getCell(j)
															.getNumericCellValue());
													
									        break;
									 
									      case HSSFCell.CELL_TYPE_ERROR:
									 
									       break;
									 
									      }
								  
										
									}else  {
										if(			countryId!=null
												&& !countryId.equals("")){
											
											Float	price =importExcelDao.selectPrice(countryId,channelModel.getModel());
											if(price!=null && !price.equals("") ){
												hashMap.put("price", price);
												
											}else{
												
												hashMap.put("price", 0);
												
													/* msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j +1)+" "+getCellValueByCell(cells)+" "
																+ getText("excel.error.price") + "<br/>");
												*/
											}
											
										}
								
									}
								}else if(getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("stocks")){
									hashMap.put("what", "Stocks");
									
									boolean one=setModelSTO.add(getCellValueByCell(cells));
									if(!one){
										msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1)+" "
												+ getText("excel.error.stoRE")+"  ("+getCellValueByCell(cells)+") " + "<br/>");
										
									}
									
								}else if(getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("display")){
									hashMap.put("what", "DisPlay");
									boolean one=setModelDIS.add(getCellValueByCell(cells));
									if(!one){
										msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1)+" "
												+ getText("excel.error.disRE")+"  ("+getCellValueByCell(cells)+") " + "<br/>");
									}
									
								}else{
									msg.append(getText("excel.error.row")+" "+3+" "+
								getText("excel.error.line") + DateUtil.getExcelColumnLabel(j +1)+" "+"("+ getCellValueByCell(cells)+")"
											+ getText("excel.error.what") + "<br/>");
								}
								
								
							}
							
						}
						
					}

					modelList.add(hashMap);
				}
			}
			
			System.out.println("=========modeList========"+modelList.size());
			System.out.println("=========modeList========"+modelList);

			String userCountry=WebPageUtil.getLoginedUser().getPartyId();
	
			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 3; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = null;
				//XSSFRow rowPrice = sheet.getRow(1);
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				String countryName="";
				String countryId="";
				if(row.getCell(0)!=null  && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					countryName = getCellValueByCell(row.getCell(0));
					countryId= importExcelDao.selectCountry(countryName);
					if (countryId == null  ) {
						msg.append(getText("excel.error.row") + (i + 1)
								+ getText("excel.error.country") +"("+getCellValueByCell(row.getCell(1))+")"+ "<br/>");
					}		else if(!userCountry.equals("999") && !userCountry.equals(countryId)) {
						msg.append(getText("excel.error.row") + (i + 1)+" "
								+ getText("excel.error.userCountry") +"("+getCellValueByCell(row.getCell(0))+")"+ "<br/>");
						
					}
					
				}else{
					if((row.getCell(3)!=null && row.getCell(3).getCellType()!=HSSFCell.CELL_TYPE_BLANK )
							
							|| 
							
							(row.getCell(1)!=null && row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK)){
						msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.line") +DateUtil.getExcelColumnLabel (1)+" "
								+ getText("excel.error.null") + "<br/>");
						
					}
				}
				
				
					

			
				
				String branchId ="";
				if(row.getCell(1)!=null  && row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
				// 这里需要根据partyName查询partyID
				 branchId = importExcelDao.getPartyIdByName(getCellValueByCell(row.getCell(1)));
				if (branchId == null) {
					msg.append(getText("excel.error.row") + (i + 1)
							+ getText("excel.error.branch") +"("+getCellValueByCell(row.getCell(2)) +")"+ "<br/>");
				}
				}else{
					if((row.getCell(3)!=null && row.getCell(3).getCellType()!=HSSFCell.CELL_TYPE_BLANK)
							
							
							||
							( row.getCell(0)!=null && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK)){
						msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.line") +DateUtil.getExcelColumnLabel (2)+" "
								+ getText("excel.error.null") + "<br/>");
						
					}
				}
				
				
				String shopName="";
				Shop shop =null;
				if(row.getCell(3)!=null  && row.getCell(3).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					shopName = getCellValueByCell(row.getCell(3));
					shop = shopDao.getShopByNames(shopName);
				if (shop == null) {
					msg.append(getText("excel.error.row") + (i + 1)
							+ getText("excel.error.shop")+"("+ getCellValueByCell(row.getCell(3+1))+")"+"<br/>");
				}
				
				}else{
					if((row.getCell(0)!=null && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK)
							
							
							|| 
							(row.getCell(1)!=null && row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK)){
						msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.line") +DateUtil.getExcelColumnLabel (3+1)+" "
								+ getText("excel.error.null") + "<br/>");
						
					}
				}
				
				
				SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date date = new Date();
				String dt = dfd.format(date);
				Date dt1 = dfd.parse(dt);
				Date dt2;
				String rowDate="";
				Float rowExchange=(float) 0;
				
				if(  	row.getCell(0)!=null &&  row.getCell(1)!=null  && row.getCell(3)!=null
						&&
						row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						&& row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						&& row.getCell(3).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					
					
					if(row.getCell(4)!=null  && row.getCell(4).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						try{
							format.setLenient(false);
							date = format.parse(getCellValueByCell(row.getCell(4)));//有异常要捕获
							dfd.setLenient(false);
							String newD = dfd.format(date);
							date = dfd.parse(newD);//有异常要捕获
								dt2 =dfd.parse(newD);
								if (dt1.getTime() < dt2.getTime()) {
									msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(3+2)+" "
											+ getText("excel.error.time") + "<br/>");
								}else{
									rowDate=newD;
								
									
								}
						}catch(Exception e){
							msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(3+2)+" "
									+ getText("excel.error.date") + "<br/>");
						}
						
							
						
				
					}else{
						msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(3+2)+" "
								+ getText("excel.error.dateNo") + "<br/>");
					}
						
					
				}
			
				
				List<Object> linked = new LinkedList<Object>();
				List<ImportExcel> test = new LinkedList<ImportExcel>();
				for (int m = 0; m < modelList.size(); m++) {
					// HashMap<String,Object> modelMap=modelList.get(m);
					//HashMap<String, Object> modelMap = new HashMap<String, Object>();
					/*// 国家
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

		
						if (branchId != null) {
							modelMap.put("Branch", branchId == null ? null : branchId);
						}
					
					
				
			
					if(rowDate!=null && !rowDate.equals("")){
						
						modelMap.put("DataDate", rowDate);
					
				
					}
					
					if(rowExchange!=null && rowExchange!=0  && rowExchange!=0.0){
						modelMap.put("exchange", rowExchange);
					}
					
					if(row.getCell(m * 3 + 5)!=null  
							 && row.getCell(m * 3 + 5).getCellType()!=HSSFCell.CELL_TYPE_BLANK
							
							){
						 switch (row.getCell(m * 3 + 5).getCellType()) {
						
						 case HSSFCell.CELL_TYPE_STRING:
					    	  msg.append(getText("excel.error.row") + (i + 1)+getText("excel.error.cell")+(m * 3 + 5+1)
										+ getText("excel.error.num") + "<br/>");
					       break;
					 
					      case HSSFCell.CELL_TYPE_FORMULA:
					 
					    	  msg.append(getText("excel.error.row") + (i + 1)+getText("excel.error.cell")+(m * 3 + 5+1)
										+ getText("excel.error.num") + "<br/>");
					  
					       break;
					 
					      case HSSFCell.CELL_TYPE_NUMERIC:
					    	  if(row.getCell(m * 3 + 5)
										.getNumericCellValue()!=0){
									modelMap.put("sold_qty", (int) row.getCell(m * 3 + 5)
											.getNumericCellValue());
									
								}
					       
					    	  
					 
					        break;
					 
					      case HSSFCell.CELL_TYPE_ERROR:
					 
					       break;
					 
					      }
						
						
					
					}
					
					
					
					
					if(row.getCell(m * 3 + 7)!=null  
							 && row.getCell(m * 3 + 7).getCellType()!=HSSFCell.CELL_TYPE_BLANK
							){
						 switch (row.getCell(m * 3 + 7).getCellType()) {
						 
					      case HSSFCell.CELL_TYPE_STRING:
					 
					    	  msg.append(getText("excel.error.row") + (i + 1)+getText("excel.error.cell")+(m * 3+ 7+1)
										+ getText("excel.error.num") + "<br/>");
					 
					       break;
					 
					      case HSSFCell.CELL_TYPE_FORMULA:
					    	  msg.append(getText("excel.error.row") + (i + 1)+getText("excel.error.cell")+(m * 3 + 7+1)
										+ getText("excel.error.num") + "<br/>");
					  
					       break;
					 
					      case HSSFCell.CELL_TYPE_NUMERIC:
					    	  if(row.getCell(m * 3 + 7)
										.getNumericCellValue()!=0 ){
								 modelMap.put("display", (int) row.getCell(m * 3 + 7)
											.getNumericCellValue());
							 }     
					        break;
					 
					      case HSSFCell.CELL_TYPE_ERROR:
					 
					       break;
					 
					      }
						
						
					}
					*/
					
					if( modelList.get(m).get("what")!=null &&  !modelList.get(m).get("what").toString().equals("")){
						
						if(row.getCell(m+ 5)!=null  
								 && row.getCell(m+ 5).getCellType()!=HSSFCell.CELL_TYPE_BLANK
								
								){
							 switch (row.getCell(m+ 5).getCellType()) {
							
							 case HSSFCell.CELL_TYPE_STRING:
						    	  msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(m+5+1)+" "
											+ getText("excel.error.num") + "<br/>");
						       break;
						 
						      case HSSFCell.CELL_TYPE_FORMULA:
						 
						    	  msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(m+5+1)+" "
											+ getText("excel.error.num") + "<br/>");
						  
						       break;
						 
						      case HSSFCell.CELL_TYPE_NUMERIC:
						    	 
						    	  if(row.getCell(m+5)
											.getNumericCellValue()!=0){
						    		  HashMap<String, Object> modelMap = new HashMap<String, Object>();
						    		  modelMap.put("Model", modelList.get(m).get("model"));
						    		  if (countryId != null) {
						    			  countrySale=countryId;
											modelMap.put("Country", countryId == null ? null
													: countryId);
											modelMap.put("CountryName", countryName);
										}
									
									
										if (shop != null) {
											modelMap.put("Store", shop.getShopId() == null ? null
													: shop.getShopId());
											modelMap.put("StoreName", shopName);
										}

						
										if (branchId != null) {
											modelMap.put("Branch", branchId == null ? null : branchId);
										}
									
									
								
							
									if(rowDate!=null && !rowDate.equals("")){
										
										modelMap.put("DataDate", rowDate);
										if ( countryId!=null && !countryId.equals("")	
												&&
												 modelList.get(m).get("price")!=null 
												 &&  !modelList.get(m).get("price").toString().equals("")
												 && Double.parseDouble(modelList.get(m).get("price").toString())!=0.0
												) {
														String []days=rowDate.split("-");
														String begin=days[0]+"-"+days[1]+"-01";
														String end=days[0]+"-"+days[1]+"-31";
														Float exchange=importExcelDao.selectExchange(countryId, begin, end,rowDate);
														if(exchange==null){
															msg.append(countryName+" " 
																	+ getText("excel.error.exchange")+" "+days[1]+"/"+days[0]+ "<br/>");
														}else{
															modelMap.put("exchange", exchange);
														}
														
												}
								
									}
									
									
									
									/*if(rowExchange!=null && rowExchange!=0  && rowExchange!=0.0){
										modelMap.put("exchange", rowExchange);
									}*/
									if( modelList.get(m).get("price")!=null &&  !modelList.get(m).get("price").toString().equals("")){
										modelMap.put("Price", modelList.get(m).get("price"));
										
									}
						    		  if( modelList.get(m).get("what").toString().equals("Sold")){
						    			  modelMap.put("sold", (int) row.getCell(m+ 5)
													.getNumericCellValue());
										}else if(modelList.get(m).get("what").toString().equals("DisPlay")){
											
											  modelMap.put("display", (int) row.getCell(m+ 5)
														.getNumericCellValue());
										}else if(modelList.get(m).get("what").toString().equals("Stocks")){
											
											  modelMap.put("stocks", (int) row.getCell(m+ 5)
														.getNumericCellValue());
										}
										
						    		  allModelList.add(modelMap);
										
									}
						       
						    	  
						 
						        break;
						 
						      case HSSFCell.CELL_TYPE_ERROR:
						 
						       break;
						 
						      }
							
							
						
						}
						

					}
				
				}
				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
					cell = row.getCell(j);
					if (cell == null) {
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
			
			System.out.println("=====size============="+allModelList.size());
			System.out.println("=====allModelList============="+allModelList);
			
			String userId="";
			if(WebPageUtil.getLoginedUserId()!=null){
				userId=WebPageUtil.getLoginedUserId();
			}
			List<ImportExcel> excelList=new ArrayList<ImportExcel>();
			List<ImportExcel> stockList=new ArrayList<ImportExcel>();
			List<ImportExcel> stockListByupdate=new ArrayList<ImportExcel>();
			List<ImportExcel> displayList=new ArrayList<ImportExcel>();
			List<ImportExcel> displayListByupdate=new ArrayList<ImportExcel>();
			List<ImportExcel> displayListRe=new ArrayList<ImportExcel>();
			List<ImportExcel> displayListReByupdate=new ArrayList<ImportExcel>();

			
			if(msg.length()<=0){
			
			for (int i = 0; i < allModelList.size(); i++) {
				
				
					
					int row = 0;
						if (
							
							allModelList.get(i)
							.get("Store")!=null
							&&allModelList.get(i)
							.get("Store")!=""
							
							&& allModelList.get(i).get("Model")!=null
							&& allModelList.get(i).get("Model")!=""
							
							&& allModelList.get(i).get("Branch")!=null
							&& allModelList.get(i).get("Branch")!=""
							&& allModelList.get(i).get("Country")!=null
							&& allModelList.get(i).get("Country")!=""
							&& allModelList.get(i).get("DataDate")!=null
							&& allModelList.get(i).get("DataDate")!=""	
							&& userId!=null && userId!=""
					
					) {
						ImportExcel excel = new ImportExcel();
						excel.setUserId(userId);
						excel.setModel(allModelList.get(i).get("Model").toString());
						excel.setDataDate(allModelList.get(i).get("DataDate")
								.toString());
						excel.setShopId(Integer.parseInt(allModelList.get(i)
								.get("Store").toString()));
						excel.setBranchId(allModelList.get(i).get("Branch").toString());
						excel.setCountryId(allModelList.get(i).get("Country")
								.toString());
						
				if(
						allModelList.get(i).get("sold") != null
						&& Integer.parseInt(allModelList.get(i).get("sold")
								.toString()) != 0
						&& allModelList.get(i)
						.get("Price")!=null ){
					
					BigDecimal bd = new BigDecimal(allModelList.get(i)
							.get("sold").toString()); 
				
					int qty=bd.intValue();
					excel.setSaleQty(bd.intValue());
					excel.setSource("PC");
					
					java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
					nf.setGroupingUsed(false);  
					
					
					excel.setSalePrice(Double.parseDouble(nf.format(Double.parseDouble(allModelList.get(i)
							.get("Price").toString()))));
					
					double price=Double.parseDouble(nf.format(Double.parseDouble(allModelList.get(i)
							.get("Price").toString())));
					
					
					bd = new BigDecimal(qty*price); 
					
					excel.setAmt(String.valueOf(bd.longValue()));
					excel.setH_qty(qty);
					if(allModelList.get(i).get("exchange")!=null 
							&& 
							Float.parseFloat(allModelList.get(i).get("exchange").toString())!=0
							){
						
						Float exchange=Float.parseFloat(allModelList.get(i).get("exchange").toString());
					
						if(exchange!=null){
							bd = new BigDecimal(price*exchange); 
							double Hprice=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							excel.setH_price(Hprice);
							bd = new BigDecimal(qty*Hprice);
							BigDecimal HAmt=bd.setScale(2, BigDecimal.ROUND_HALF_UP);
							excel.setH_amt(String.valueOf(HAmt));
						}
						
					}else {
						excel.setH_price(0);
						excel.setH_amt(String.valueOf('0'));
					}
					
				excelList.add(excel);
					
				}else if(allModelList.get(i).get("stocks") != null
						&& Integer.parseInt(allModelList.get(i).get("stocks")
								.toString()) != 0){
					excel.setStockQty(Integer.parseInt(allModelList.get(i)
							.get("stocks").toString()));
				
					
					
					importExcelDao.deleteStockCountByRE(allModelList
							.get(i).get("Store").toString(),
							excel.getModel(),allModelList.get(i).get("DataDate")
							.toString());
					//if(rowsRE==null){
						excel.setClassId(1);
						stockList.add(excel);
					/*}else{
						excel.setId(rowsRE);
						stockListByupdate.add(excel);
					}*/
					
					
				}else if(allModelList.get(i).get("display") != null
						&& Integer.parseInt(allModelList.get(i).get("display")
								.toString()) != 0){
					excel.setDisPlayQty(Integer.parseInt(allModelList.get(i)
							.get("display").toString()));
					/*int rows = importExcelDao.selectDisPlayCount(allModelList
							.get(i).get("Store").toString(),
							excel.getModel());*/
					
				 importExcelDao.deleteDisplayCountByRE(allModelList
							.get(i).get("Store").toString(),
							excel.getModel(),allModelList.get(i).get("DataDate")
							.toString());
					//if(rowsRE==null){
						excel.setClassId(1);
						displayListRe.add(excel);
					/*}else{
						excel.setId(rowsRE);
						displayListReByupdate.add(excel);
					}*/
					
				/*	if (rows == 0) {
						// 判断该机型该门店是否存在，存在的话就修改，不存在就插入
						excel.setClassId(1);
						row = importExcelDao.saveDisPlays(excel);
						displayList.add(excel);
						if (row == 0) {
							msg.append(shopName + ":"
									+ getText("excel.error.disPlayInsert")
									+ "<br/>");
						}
					} else {
						displayListByupdate.add(excel);
						
						row = importExcelDao.updateDisPlay(excel);
						if (row == 0) {
							msg.append(shopName + ":"
									+ getText("excel.error.disPlayUpdate")
									+ "<br/>");
						}
					}*/
				}
				
			}
				}
				
					// 判断该机型该门店是否存在，存在的话就修改，不存在就插入
			}
		
			if(stockList.size()>0){
				DateUtil.remove(stockList); 
				importExcelDao.saveStocks(stockList);
			}
			
			if(excelList.size()>0){
				
				int row = importExcelDao.saveSales(excelList);
				if(row>0){
					DateUtil.ListSort(excelList);
					String [] beginDate=excelList.get(0).getDataDate().split("-");
					String [] endDate=excelList.get(excelList.size()-1).getDataDate().split("-");
					
					String beg=beginDate[0]+"-"+beginDate[1]+"-01";
					String end=endDate[0]+"-"+endDate[1]+"-31";
					String countryList="";
					/*for (int i = 0; i < excelList.size()-1; i++) {
			            for (int j = excelList.size()-1; j > i; j--) {
			                if (excelList.get(j).getDataDate()== excelList.get(i).getDataDate()) {
			                	excelList.remove(j);
			                }else{
			                	
			                	countryList+="'"+excelList.get(j).getDataDate()+"',";
			                }
			            }
			        }
					countryList = countryList.substring(0, countryList.length() - 1);  */
					importExcelDao.saleRe(countrySale, beg, end);
					importExcelDao.insertTVSaleVive(countrySale, beg, end);
					importExcelDao.insertACSaleVive(countrySale, beg, end);
					importExcelDao.updateStocksByShop(excelList);
				}
			}
			if(displayListRe.size()>0){
				DateUtil.remove(displayListRe); 
				importExcelDao.saveDisPlaysByRe(displayListRe);
			}
			
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
	public XSSFWorkbook exporExcelByCustomer(String spec, String conditions,
			String[] excelHeader, String title) throws Exception {
		String key=null;
		 List<HashMap<String, Object>> list=importExcelDao.selectCustomer(conditions,null);
		
		List<HashMap<String, Object>>  modelList=importExcelDao.selectModelByCustomer(null,conditions,spec);
		//设置 表头宽度
		int[] excelWidth = {120,180,200,120,100,100,100,100};
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		 XSSFSheet sheet = workbook.createSheet(title);
		 XSSFSheet sheetNotice = workbook.createSheet("NOTICE");
		 sheet.createFreezePane(4,3,4,3)  ;
		// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
		sheet.addMergedRegion(new CellRangeAddress(0, 2,0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 2,1, 1));

		sheet.addMergedRegion(new CellRangeAddress(0, 2,2, 2));
		sheet.addMergedRegion(new CellRangeAddress(0, 2,3, 3));

		 
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
         
         
         
         XSSFCellStyle styleYellow = workbook.createCellStyle();    
         styleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleYellow.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         styleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
         styleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
         styleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
         styleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
         styleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
         styleYellow.setRightBorderColor(HSSFColor.BLACK.index);
         styleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
         styleYellow.setTopBorderColor(HSSFColor.BLACK.index);
         styleYellow.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
         styleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
         styleYellow.setFont(font);
         
         

         XSSFCellStyle styleGreen = workbook.createCellStyle();    
         styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleGreen.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         styleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
         styleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
         styleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
         styleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
         styleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
         styleGreen.setRightBorderColor(HSSFColor.BLACK.index);
         styleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
         styleGreen.setTopBorderColor(HSSFColor.BLACK.index);
         styleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
         styleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
         styleGreen.setFont(font);
         
         
         
         
         
         
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
         
         rowNotice=sheetNotice.createRow(6);
         XSSFCell cellNoticeSI = rowNotice.createCell(3);
         cellNoticeSI.setCellValue("3. Please don't rename the file, in case it can't be uploaded.");
         cellNoticeSI.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(7);
         XSSFCell cellNoticeSE = rowNotice.createCell(3);
         cellNoticeSE.setCellValue("3. 请不要重命名文件，以防无法顺利导入。");
         cellNoticeSE.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(8);
         XSSFCell cellNoticeEI = rowNotice.createCell(3);
         cellNoticeEI.setCellValue("4. Fill in the single price of the product in row 2.");
         cellNoticeEI.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(9);
         XSSFCell cellNoticeNI = rowNotice.createCell(3);
         cellNoticeNI.setCellValue("4. 请在第2行填写产品单价。");
         cellNoticeNI.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(10);
         XSSFCell cellNoticeTe = rowNotice.createCell(3);
         cellNoticeTe.setCellValue("5. Fill in Sold/Stocks/Display in row 3.");
         cellNoticeTe.setCellStyle(styleNo);
         
         rowNotice=sheetNotice.createRow(11);
         XSSFCell cellNoticeEL = rowNotice.createCell(3);
         cellNoticeEL.setCellValue("5. 请在第3行填写Sold/Stocks/Display。");
         cellNoticeEL.setCellStyle(styleNo);
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
         XSSFRow rowTwo = sheet.createRow(1);
         XSSFRow rowThree = sheet.createRow(2);
         
         for (int i = 0; i < modelList.size(); i++) {
        	 
        	 
             

        	 if(modelList.get(i).get("channel_model")!=null  && !modelList.get(i).get("channel_model").equals("")){
        		 XSSFCell  cell0 = row.createCell(4+i);
                 cell0.setCellValue(modelList.get(i).get("channel_model").toString());    
                 cell0.setCellStyle(styleBlue);
                 
              
             }
        	 
        	 if(modelList.get(i).get("price")!=null  && Double.parseDouble(modelList.get(i).get("price").toString())!=0.0){
        		XSSFCell cell= rowTwo.createCell(4+i);
        		cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));    
        		cell.setCellStyle(styleBlue);  
             }
        	 
          	 XSSFCell  cellOne = rowThree.createCell(4+i);
               cellOne.setCellValue("Sold");    
               cellOne.setCellStyle(styleBlue);
        	 
		}
         int size;
         if(modelList.size()<=2){
        	 size=modelList.size();
         }else{
        	 size=2;
         }
         
         for (int i = 0; i < size; i++) {
        	 
             

        	 if(modelList.get(i).get("channel_model")!=null  && !modelList.get(i).get("channel_model").equals("")){
        		 XSSFCell  cell0 = row.createCell(4+i+modelList.size());
                 cell0.setCellValue(modelList.get(i).get("channel_model").toString());    
                 cell0.setCellStyle(styleGreen);
                 
              
             }
        	 
        	 if(modelList.get(i).get("price")!=null  && Double.parseDouble(modelList.get(i).get("price").toString())!=0.0){
        		XSSFCell cell= rowTwo.createCell(4+i+modelList.size());
        		cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));    
        		cell.setCellStyle(styleGreen);  
             }
        	 
          	 XSSFCell  cellOne = rowThree.createCell(4+i+modelList.size());
               cellOne.setCellValue("Display");    
               cellOne.setCellStyle(styleGreen);
        	 
		}
         
         	for (int i = 0; i < size; i++) {
        	 
             

        	 if(modelList.get(i).get("channel_model")!=null  && !modelList.get(i).get("channel_model").equals("")){
        		 XSSFCell  cell0 = row.createCell(4+i+modelList.size()+size);
                 cell0.setCellValue(modelList.get(i).get("channel_model").toString());    
                 cell0.setCellStyle(styleYellow);
                 
              
             }
        	 
        	 if(modelList.get(i).get("price")!=null  && Double.parseDouble(modelList.get(i).get("price").toString())!=0.0){
        		XSSFCell cell= rowTwo.createCell(4+i+modelList.size()+size);
        		cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));    
        		cell.setCellStyle(styleYellow);  
             }
        	 
          	 XSSFCell  cellOne = rowThree.createCell(4+i+modelList.size()+size);
               cellOne.setCellValue("Stocks");    
               cellOne.setCellStyle(styleYellow);
        	 
		}

         
         //表体数据
         for (int i = 0; i < list.size(); i++) {    
             row = sheet.createRow(i + 3);    
             HashMap<String, Object> customer=list.get(i);
             
             //-------------单元格-------------------
             
             /**
              * 国家
              */
             if(i==0){
                 XSSFCell cell = row.createCell(3);
                 cell.setCellValue("dd/MM/yyyy");    
                 cell.setCellStyle(styleTwo);
             }
             
             
             if(customer.get("COUNTRY_NAME")!=null  && !customer.get("COUNTRY_NAME").equals("")){
            	 XSSFCell cell0 = row.createCell(0);
                 cell0.setCellValue(customer.get("COUNTRY_NAME").toString());    
                 cell0.setCellStyle(styleTwo);
             }
            
             
             if(customer.get("customer_NAME")!=null  && !customer.get("customer_NAME").equals("")){
            	 XSSFCell cell0 = row.createCell(2);
                 cell0.setCellValue(customer.get("customer_NAME").toString());    
                 cell0.setCellStyle(styleTwo);
             }
             
             if(customer.get("party_name")!=null  && !customer.get("party_name").equals("")){
            	 XSSFCell cell0 = row.createCell(1);
                 cell0.setCellValue(customer.get("party_name").toString());    
                 cell0.setCellStyle(styleTwo);
             }
            
             
            
             
            
         }    
         
         
         return workbook;
	}





	
	@Override
	public List<HashMap<String, Object>> selectAllCore() throws Exception {
		return importExcelDao.selectAllCore();
	}

	@Override
	public String read2007ExcelByCountry(File file, String what)
			throws IOException {
		String countrySale="";
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
			XSSFRow rowPrice = sheet.getRow(1);
			XSSFCell cells = null;
			List<HashMap<String, Object>> modelList = new LinkedList<HashMap<String, Object>>();
			Set<String> setModelDIS= new HashSet<String>();
			Set<String> setModelSTO= new HashSet<String>();
			for (int j = 2; j < ro.getLastCellNum(); j++) {
				
				HashMap<String, Object> hashMap = new HashMap<>();
				XSSFRow row = null;
				XSSFCell cell = null;
				if(ro.getLastCellNum()>=3){
					row = sheet.getRow(3);

				}
				cells = ro.getCell(j);
				XSSFRow whatRow = sheet.getRow(2);
				if (cells!=null &&
						cells.getCellType()!=HSSFCell.CELL_TYPE_BLANK &&
						
						 !getCellValueByCell(cells).equals("")
						&& row.getCell(0)!=null &&
						!getCellValueByCell(row.getCell(0)).equals("")
						&& row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						) {
					
					String countryName = getCellValueByCell(row.getCell(0));
					String countryId = importExcelDao.selectCountry(countryName);
					String li = "";
					String cond = " AND t.`party_id`='"+countryId+"'";

					int model = modelMapDao.getModelIdByParty(cond,
							getCellValueByCell(cells), li);
					if (model >= 1) {
						hashMap.put("model", getCellValueByCell(cells));
						//判断为so or stock or  sample
						if(whatRow.getCell(j)!=null  &&
								whatRow.getCell(j).getCellType()!=HSSFCell.CELL_TYPE_BLANK &&
								!whatRow.getCell(j).equals("")
								){
							if(getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("sold")){
								hashMap.put("what", "Sold");
								if(rowPrice!=null && rowPrice.getCell(j)!=null
										 && rowPrice.getCell(j).getCellType()!=HSSFCell.CELL_TYPE_BLANK
												
									 ){
									
									 switch (rowPrice.getCell(j).getCellType()) {
									 
								      case HSSFCell.CELL_TYPE_STRING:
								 
											msg.append(getText("excel.error.row") + (2)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(j +1)+" "
													+ getText("excel.error.num") + "<br/>");
								 
								       break;
								 
								      case HSSFCell.CELL_TYPE_FORMULA:
								 
											msg.append(getText("excel.error.row") + (2)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(j +1)+" "
													+ getText("excel.error.num") + "<br/>");
								  
								       break;
								 
								      case HSSFCell.CELL_TYPE_NUMERIC:
								 
								    	  if(rowPrice.getCell(j)
													.getNumericCellValue()!=0){
								    		  hashMap.put("price", rowPrice.getCell(j)
														.getNumericCellValue());
												
											}
								        break;
								 
								      case HSSFCell.CELL_TYPE_ERROR:
								 
								       break;
								 
								      }
							  
									
								}else  {
									if(			countryId!=null
											&& !countryId.equals("")){
										
										Float	price =importExcelDao.selectPrice(countryId,getCellValueByCell(cells));
										if(price!=null && !price.equals("")  && price!=0 &&  price!=0.0){
											
											hashMap.put("price", price);
											
										}else{
											hashMap.put("price", 0);
										
											
										}
										
									}
							
								}
							}else if(getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("stocks")){
								hashMap.put("what", "Stocks");
								boolean one=setModelSTO.add(getCellValueByCell(cells));
								if(!one){
									msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j + 1)+" "
											+ getText("excel.error.stoRE")+"  ("+getCellValueByCell(cells)+") " + "<br/>");
								}
								
							}else if(getCellValueByCell(whatRow.getCell(j)).trim().toLowerCase().equals("display")){
								hashMap.put("what", "DisPlay");
								boolean one=setModelDIS.add(getCellValueByCell(cells));
								if(!one){
									msg.append(getText("excel.error.line") +DateUtil.getExcelColumnLabel (j + 1)+" "
											+ getText("excel.error.disRE")+"  ("+getCellValueByCell(cells)+") " + "<br/>");
								}
								
							}else{
								msg.append(getText("excel.error.row")+" "+3+" "+
							getText("excel.error.line") + DateUtil.getExcelColumnLabel(j +1)+" "+"("+ getCellValueByCell(cells)+")"
										+ getText("excel.error.what") + "<br/>");
							}
							
							
						}
					
					}else{
						msg.append(getText("excel.error.line") + DateUtil.getExcelColumnLabel(j +1)+" "
						+ getText("excel.error.model") +"("+ getCellValueByCell(cells)+")"+ "<br/>");
					}
			
					modelList.add(hashMap);
					
				}
				
				
				

			}

			System.out.println(modelList);
			String userCountry=WebPageUtil.getLoginedUser().getPartyId();
			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 3; i <= sheet.getPhysicalNumberOfRows(); i++) {
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
					countryName = getCellValueByCell(row.getCell(0));
					countryId= importExcelDao.selectCountry(countryName);
					if (countryId == null  ) {
						msg.append(getText("excel.error.row") + (i + 1)
								+ getText("excel.error.country") +"("+getCellValueByCell(row.getCell(0))+")"+ "<br/>");
					}else if(!userCountry.equals("999") && !userCountry.equals(countryId)) {
						msg.append(getText("excel.error.row") + (i + 1)+" "
								+ getText("excel.error.userCountry") +"("+getCellValueByCell(row.getCell(0))+")"+ "<br/>");
						
					}
					
				}else{
					if(
							row.getCell(1)!=null && row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.line") +DateUtil.getExcelColumnLabel (1)+" "
								+ getText("excel.error.null") + "<br/>");
						
					}
				}
				
				
				
				
				
				SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date date = new Date();
				String dt = dfd.format(date);
				Date dt1 = dfd.parse(dt);
				Date dt2;
				String rowDate="";
				Float rowExchange=(float) 0;
				
				if(  	row.getCell(0)!=null &&
						row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					
					
					if(row.getCell(1)!=null  && row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						try{
							format.setLenient(false);
							date = format.parse(getCellValueByCell(row.getCell(1)));//有异常要捕获
							dfd.setLenient(false);
							String newD = dfd.format(date);
							date = dfd.parse(newD);//有异常要捕获
								dt2 =dfd.parse(newD);
								if (dt1.getTime() < dt2.getTime()) {
									msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(2)+" "
											+ getText("excel.error.time") + "<br/>");
								}else{
									rowDate=newD;
								
									
								}
						}catch(Exception e){
							msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(2)+" "
									+ getText("excel.error.date") + "<br/>");
						}
						
							
						
				
					}else{
						msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(2)+" "
								+ getText("excel.error.dateNo") + "<br/>");
					}
						
					
				}
			
				
				for (int m = 0; m < modelList.size(); m++) {
					
						
					if( modelList.get(m).get("what")!=null &&  !modelList.get(m).get("what").toString().equals("")){
						
						if(row.getCell(m+ 2)!=null  
								 && row.getCell(m+ 2).getCellType()!=HSSFCell.CELL_TYPE_BLANK
								
								){
							 switch (row.getCell(m+ 2).getCellType()) {
							
							 case HSSFCell.CELL_TYPE_STRING:
						    	  msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(m+3)+" "
											+ getText("excel.error.num") + "<br/>");
						       break;
						 
						      case HSSFCell.CELL_TYPE_FORMULA:
						 
						    	  msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell")+DateUtil.getExcelColumnLabel(m+3)+" "
											+ getText("excel.error.num") + "<br/>");
						  
						       break;
						 
						      case HSSFCell.CELL_TYPE_NUMERIC:
						    	 
						    	  if(row.getCell(m+2)
											.getNumericCellValue()!=0){
						    		  HashMap<String, Object> modelMap = new HashMap<String, Object>();
						    		  modelMap.put("Model", modelList.get(m).get("model"));
						    		  if (countryId != null) {
						    			   countrySale=countryId;
											modelMap.put("Country", countryId == null ? null
													: countryId);
											modelMap.put("CountryName", countryName);
										}
									
						    		
								
							
									if(rowDate!=null && !rowDate.equals("")){
										
										modelMap.put("DataDate", rowDate);
										if ( countryId!=null && !countryId.equals("")	
												&& modelList.get(m).get("price")!=null 
												&&  !modelList.get(m).get("price").toString().equals("")
												&& Double.parseDouble(modelList.get(m).get("price").toString())!=0.0
												) {
														String []days=rowDate.split("-");
														String begin=days[0]+"-"+days[1]+"-01";
														String end=days[0]+"-"+days[1]+"-31";
														Float exchange=importExcelDao.selectExchange(countryId, begin, end,rowDate);
														if(exchange==null){
															msg.append(countryName+" " 
																	+ getText("excel.error.exchange")+" "+days[1]+"/"+days[0]+ "<br/>");
														}else{
															rowExchange=exchange;
														}
														
												}
								
									}
									
								/*	if(rowExchange!=null && rowExchange!=0  && rowExchange!=0.0){
										modelMap.put("exchange", rowExchange);
									}*/
									if( modelList.get(m).get("price")!=null &&  !modelList.get(m).get("price").toString().equals("")){
										modelMap.put("Price", modelList.get(m).get("price"));
										
									}
						    		  if( modelList.get(m).get("what").toString().equals("Sold")){
						    			  modelMap.put("sold", (int) row.getCell(m+ 2)
													.getNumericCellValue());
										}else if(modelList.get(m).get("what").toString().equals("DisPlay")){
											
											  modelMap.put("display", (int) row.getCell(m+ 2)
														.getNumericCellValue());
										}else if(modelList.get(m).get("what").toString().equals("Stocks")){
											
											  modelMap.put("stocks", (int) row.getCell(m+ 2)
														.getNumericCellValue());
										}
										
						    		  allModelList.add(modelMap);
										
									}
						       
						    	  
						 
						        break;
						 
						      case HSSFCell.CELL_TYPE_ERROR:
						 
						       break;
						 
						      }
							
							
						
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
			System.out.println("==========size==========="+allModelList);
			List<ImportExcel> excelList=new ArrayList<ImportExcel>();
			List<ImportExcel> stockList=new ArrayList<ImportExcel>();
			List<ImportExcel> stockListByupdate=new ArrayList<ImportExcel>();
			List<ImportExcel> displayList=new ArrayList<ImportExcel>();
			List<ImportExcel> displayListRe=new ArrayList<ImportExcel>();
			List<ImportExcel> displayListReByupdate=new ArrayList<ImportExcel>();
			List<ImportExcel> displayListByupdate=new ArrayList<ImportExcel>();
			//System.out.println("=========allModelList==========="+allModelList);
			if(msg.length()<=0){
			for (int i = 0; i < allModelList.size(); i++) {
				int row = 0;
				
					if (
							
							
							
							 allModelList.get(i).get("Model")!=null
							&& allModelList.get(i).get("Model")!=""
							
							
							&& allModelList.get(i).get("Country")!=null
							&& allModelList.get(i).get("Country")!=""
							&& allModelList.get(i).get("DataDate")!=null
							&& allModelList.get(i).get("DataDate")!=""	
							&& userId!=null && userId!=""
					
					) {
						ImportExcel excel = new ImportExcel();
						excel.setUserId(userId);
						excel.setModel(allModelList.get(i).get("Model").toString());
						excel.setDataDate(allModelList.get(i).get("DataDate")
								.toString());
						
						
						excel.setCountryId(allModelList.get(i).get("Country")
								.toString());
						
				if(
						allModelList.get(i).get("sold") != null
						&& Integer.parseInt(allModelList.get(i).get("sold")
								.toString()) != 0
						&& allModelList.get(i)
						.get("Price")!=null ){
					
					BigDecimal bd = new BigDecimal(allModelList.get(i)
							.get("sold").toString()); 
				
					int qty=bd.intValue();
					excel.setSaleQty(bd.intValue());
					excel.setSource("PC");
					
					java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
					nf.setGroupingUsed(false);  
					
					
					excel.setSalePrice(Double.parseDouble(nf.format(Double.parseDouble(allModelList.get(i)
							.get("Price").toString()))));
					
					double price=Double.parseDouble(nf.format(Double.parseDouble(allModelList.get(i)
							.get("Price").toString())));
					
					
					bd = new BigDecimal(qty*price); 
					
					excel.setAmt(String.valueOf(bd.longValue()));
					excel.setH_qty(qty);
					if(allModelList.get(i).get("exchange")!=null 
							&& 
							Float.parseFloat(allModelList.get(i).get("exchange").toString())!=0
							){
						
						Float exchange=Float.parseFloat(allModelList.get(i).get("exchange").toString());
					
						if(exchange!=null){
							bd = new BigDecimal(price*exchange); 
							double Hprice=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							excel.setH_price(Hprice);
							bd = new BigDecimal(qty*Hprice);
							BigDecimal HAmt=bd.setScale(2, BigDecimal.ROUND_HALF_UP);
							excel.setH_amt(String.valueOf(HAmt));
						}
						
					}else {
						excel.setH_price(0);
						excel.setH_amt(String.valueOf('0'));
					}
					
				excelList.add(excel);
				System.out.println("======listSize============"+excelList.size());
						/*row = importExcelDao.saveSales(excel);*/
						/*if (row == 0) {
							msg.append(shopName + ":"
									+ getText("excel.error.saleInsert") + "<br/>");

						}*/
					
				}else if(allModelList.get(i).get("stocks") != null
						&& Integer.parseInt(allModelList.get(i).get("stocks")
								.toString()) != 0){
					excel.setStockQty(Integer.parseInt(allModelList.get(i)
							.get("stocks").toString()));
				
					
					
					importExcelDao.deleteStockCountByRECountry(allModelList
							.get(i).get("Country").toString(),
							excel.getModel(),allModelList.get(i).get("DataDate")
							.toString());
					//if(rowsRE==null){
						excel.setClassId(1);
						stockList.add(excel);
					/*}else{
						excel.setId(rowsRE);
						stockListByupdate.add(excel);
					}*/
					
					
				}else if(allModelList.get(i).get("display") != null
						&& Integer.parseInt(allModelList.get(i).get("display")
								.toString()) != 0){
					excel.setDisPlayQty(Integer.parseInt(allModelList.get(i)
							.get("display").toString()));
				
				 importExcelDao.deleteDisplayCountByRECountry(allModelList
							.get(i).get("Country").toString(),
							excel.getModel(),allModelList.get(i).get("DataDate")
							.toString());
					//if(rowsRE==null){
						excel.setClassId(1);
						displayListRe.add(excel);
					/*}else{
						excel.setId(rowsRE);
						displayListReByupdate.add(excel);
					}*/
					
					
				}
				
				
			}
				}
				
					// 判断该机型该门店是否存在，存在的话就修改，不存在就插入
			}
		
			if(stockList.size()>0){
				DateUtil.remove(stockList); 
				importExcelDao.saveStocks(stockList);
			}
			
			
			if(excelList.size()>0){
				int row = importExcelDao.saveSales(excelList);
				if(row>0){
					DateUtil.ListSort(excelList);
					String [] beginDate=excelList.get(0).getDataDate().split("-");
					String [] endDate=excelList.get(excelList.size()-1).getDataDate().split("-");
					
					String beg=beginDate[0]+"-"+beginDate[1]+"-01";
					String end=endDate[0]+"-"+endDate[1]+"-31";
					
					if(what.equals("shop")){
						importExcelDao.insertTVSaleVive(countrySale, beg, end);
						importExcelDao.insertACSaleVive(countrySale, beg, end);
						importExcelDao.saleRe(countrySale, beg, end);
					}
					importExcelDao.updateStocksByCountry(excelList);
					
				}
			}
			if(displayListRe.size()>0){
				DateUtil.remove(displayListRe); 
				importExcelDao.saveDisPlaysByRe(displayListRe);
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
	public XSSFWorkbook exporExcelByCountry(String spec, String conditions,
			String[] excelHeader, String title) throws Exception {
		String key=null;
		 List<HashMap<String, Object>> list=importExcelDao.selectCountryList(conditions,null);
		
		List<HashMap<String, Object>>  modelList=importExcelDao.selectModel(null,conditions,spec);
		//设置 表头宽度
		int[] excelWidth = {120,180,120,120,100,100,100,100};
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		 XSSFSheet sheet = workbook.createSheet(title);
		 XSSFSheet sheetNotice = workbook.createSheet("NOTICE");
		 sheet.createFreezePane(4,3,4,3)  ;
		// 参数 1:行号 参数 2:起始列号 参数 3:行号 参数 4:终止列号
		sheet.addMergedRegion(new CellRangeAddress(0, 2,0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 2,1, 1));

		//sheet.addMergedRegion(new CellRangeAddress(0, 2,2, 2));
		//sheet.addMergedRegion(new CellRangeAddress(0, 2,3, 3));

		 
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
        
        
        
        XSSFCellStyle styleYellow = workbook.createCellStyle();    
        styleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleYellow.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        styleYellow.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
        styleYellow.setBottomBorderColor(HSSFColor.BLACK.index);
        styleYellow.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleYellow.setLeftBorderColor(HSSFColor.BLACK.index);
        styleYellow.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleYellow.setRightBorderColor(HSSFColor.BLACK.index);
        styleYellow.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleYellow.setTopBorderColor(HSSFColor.BLACK.index);
        styleYellow.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        styleYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleYellow.setFont(font);
        
        

        XSSFCellStyle styleGreen = workbook.createCellStyle();    
        styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleGreen.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleGreen.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 边框
        styleGreen.setBottomBorderColor(HSSFColor.BLACK.index);
        styleGreen.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleGreen.setLeftBorderColor(HSSFColor.BLACK.index);
        styleGreen.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleGreen.setRightBorderColor(HSSFColor.BLACK.index);
        styleGreen.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleGreen.setTopBorderColor(HSSFColor.BLACK.index);
        styleGreen.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
        styleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleGreen.setFont(font);
        
        
        
        
        
        
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
        
       
        
        rowNotice=sheetNotice.createRow(6);
        XSSFCell cellNoticeSI = rowNotice.createCell(3);
        cellNoticeSI.setCellValue("3. Please don't rename the file, in case it can't be uploaded.");
        cellNoticeSI.setCellStyle(styleNo);
        
        rowNotice=sheetNotice.createRow(7);
        XSSFCell cellNoticeSE = rowNotice.createCell(3);
        cellNoticeSE.setCellValue("3. 请不要重命名文件，以防无法顺利导入。");
        cellNoticeSE.setCellStyle(styleNo);
        
        rowNotice=sheetNotice.createRow(8);
        XSSFCell cellNoticeEI = rowNotice.createCell(3);
        cellNoticeEI.setCellValue("4. Fill in the single price of the product in row 2.");
        cellNoticeEI.setCellStyle(styleNo);
        
        rowNotice=sheetNotice.createRow(9);
        XSSFCell cellNoticeNI = rowNotice.createCell(3);
        cellNoticeNI.setCellValue("4. 请在第2行填写产品单价。");
        cellNoticeNI.setCellStyle(styleNo);
        
        rowNotice=sheetNotice.createRow(10);
        XSSFCell cellNoticeTe = rowNotice.createCell(3);
        cellNoticeTe.setCellValue("5. Fill in Sold/Stocks/Display in row 3.");
        cellNoticeTe.setCellStyle(styleNo);
        
        rowNotice=sheetNotice.createRow(11);
        XSSFCell cellNoticeEL = rowNotice.createCell(3);
        cellNoticeEL.setCellValue("5. 请在第3行填写Sold/Stocks/Display。");
        cellNoticeEL.setCellStyle(styleNo);
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
        XSSFRow rowTwo = sheet.createRow(1);
        XSSFRow rowThree = sheet.createRow(2);
        
        for (int i = 0; i < modelList.size(); i++) {
       	 
       	 
            

       	 if(modelList.get(i).get("branch_model")!=null  && !modelList.get(i).get("branch_model").equals("")){
       		 XSSFCell  cell0 = row.createCell(2+i);
                cell0.setCellValue(modelList.get(i).get("branch_model").toString());    
                cell0.setCellStyle(styleBlue);
                
             
            }
       	 
       	 if(modelList.get(i).get("price")!=null  && Double.parseDouble(modelList.get(i).get("price").toString())!=0.0){
       		XSSFCell cell= rowTwo.createCell(2+i);
       		cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));    
       		cell.setCellStyle(styleBlue);  
            }
       	 
         	 XSSFCell  cellOne = rowThree.createCell(2+i);
              cellOne.setCellValue("Sold");    
              cellOne.setCellStyle(styleBlue);
       	 
		}
        int size;
        if(modelList.size()<=2){
       	 size=modelList.size();
        }else{
       	 size=2;
        }
        
        for (int i = 0; i < size; i++) {
       	 
            

       	 if(modelList.get(i).get("branch_model")!=null  && !modelList.get(i).get("branch_model").equals("")){
       		 XSSFCell  cell0 = row.createCell(2+i+modelList.size());
                cell0.setCellValue(modelList.get(i).get("branch_model").toString());    
                cell0.setCellStyle(styleGreen);
                
             
            }
       	 
       	 if(modelList.get(i).get("price")!=null  && Double.parseDouble(modelList.get(i).get("price").toString())!=0.0){
       		XSSFCell cell= rowTwo.createCell(2+i+modelList.size());
       		cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));    
       		cell.setCellStyle(styleGreen);  
            }
       	 
         	 XSSFCell  cellOne = rowThree.createCell(2+i+modelList.size());
              cellOne.setCellValue("Display");    
              cellOne.setCellStyle(styleGreen);
       	 
		}
        
        	for (int i = 0; i < size; i++) {
       	 
            

       	 if(modelList.get(i).get("branch_model")!=null  && !modelList.get(i).get("branch_model").equals("")){
       		 XSSFCell  cell0 = row.createCell(2+i+modelList.size()+size);
                cell0.setCellValue(modelList.get(i).get("branch_model").toString());    
                cell0.setCellStyle(styleYellow);
                
             
            }
       	 
       	 if(modelList.get(i).get("price")!=null  && Double.parseDouble(modelList.get(i).get("price").toString())!=0.0){
       		XSSFCell cell= rowTwo.createCell(2+i+modelList.size()+size);
       		cell.setCellValue(Double.parseDouble(modelList.get(i).get("price").toString()));    
       		cell.setCellStyle(styleYellow);  
            }
       	 
         	 XSSFCell  cellOne = rowThree.createCell(2+i+modelList.size()+size);
              cellOne.setCellValue("Stocks");    
              cellOne.setCellStyle(styleYellow);
       	 
		}

        
        //表体数据
        for (int i = 0; i < list.size(); i++) {    
            row = sheet.createRow(i + 3);    
            HashMap<String, Object> customer=list.get(i);
            
            //-------------单元格-------------------
            
            /**
             * 国家
             */
            if(i==0){
                XSSFCell cell = row.createCell(1);
                cell.setCellValue("dd/MM/yyyy");    
                cell.setCellStyle(styleTwo);
            }
            
            
            if(customer.get("COUNTRY_NAME")!=null  && !customer.get("COUNTRY_NAME").equals("")){
           	 XSSFCell cell0 = row.createCell(0);
                cell0.setCellValue(customer.get("COUNTRY_NAME").toString());    
                cell0.setCellStyle(styleTwo);
            }
           
          
           
            
           
        }    
        
        
        return workbook;
	}

	@Override
	public  String selectSoType(String country) throws Exception {

		String  result="";
	
			result=importExcelDao.selectSoType(country);
		
			if(result==null || result.equals("")){
				result="admin";
			}
		
		return result;
		
	}
	
	private static String getCellValueByCell(XSSFCell xssfCell) {
		// 判断是否为null或空串
		if (xssfCell == null || xssfCell.toString().trim().equals("")) {
			return "";
		}
		String cellValue = "";
		int cellType = xssfCell.getCellType();
		if (cellType == HSSFCell.CELL_TYPE_FORMULA) { // 表达式类型
			FormulaEvaluator evaluator = xssfCell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
			evaluator.evaluateFormulaCell(xssfCell);
			CellValue cellValues = evaluator.evaluate(xssfCell);
			Double celldata = cellValues.getNumberValue();
			return celldata + "";
		}

		switch (cellType) {
		case HSSFCell.CELL_TYPE_STRING: // 字符串类型
			cellValue = xssfCell.getStringCellValue().trim();
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN: // 布尔类型
			cellValue = String.valueOf(xssfCell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_NUMERIC: // 数值类型
			if (HSSFDateUtil.isCellDateFormatted(xssfCell)) { // 判断日期类型
				cellValue = format.format(xssfCell.getDateCellValue());
			} else { // 否
				cellValue = new DecimalFormat("#.######").format(xssfCell.getNumericCellValue());
			}
			break;
		default: // 其它类型，取空串吧
			cellValue = "";
			break;
		}
		return cellValue;
	}

}
