package cn.tcl.platform.shop.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.cfgparam.vo.CfgParameter;
import cn.tcl.platform.customer.dao.ICustomerDao;
import cn.tcl.platform.customer.vo.Customer;
import cn.tcl.platform.excel.vo.ImportExcel;
import cn.tcl.platform.modelmap.vo.ModelMap;
import cn.tcl.platform.shop.vo.ShopParty;
import cn.tcl.platform.shop.dao.IShopDao;
import cn.tcl.platform.shop.service.IShopService;
import cn.tcl.platform.shop.vo.Level;
import cn.tcl.platform.shop.vo.Shop;
import cn.tcl.platform.shop.vo.ShopPhotos;
import cn.tcl.platform.shop.vo.ShopUser;
@Service("shopService")
public class IShopServiceImpl extends BaseAction implements IShopService {
	@Autowired
	private IShopDao shopDao;
	@Autowired
	private ICustomerDao customerDao;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public Map<String, Object> selectShopsData(int start, int limit,String searchStr, String levelNum,
			String order, String sort,String conditions)
			throws Exception {
		String _levelNum=levelNum == ""?null:levelNum;
		
		List<Shop> list=shopDao.selectShops(start, limit,searchStr,_levelNum, order, sort,conditions);
		int count=shopDao.countShops(start, limit, searchStr,_levelNum,conditions);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", count);
		return map;
	}
	
	@Override
	@Transactional
	public void saveShop(Shop shop,String businessers,String salers,String supervisors) throws Exception {
		
		int effectrows=shopDao.saveShop(shop);//保存门店对象
		shopDao.clearShopUserRelations(shop.getShopId());
		//处理业务员
		if(businessers!=null && !"".equals(businessers)){
			//按;分隔，保存门店与user_login的关系
			String[] yewuyuans=businessers.split(";");
			for (String yewuyuan : yewuyuans) {
				shopDao.saShopUserRelation(shop.getShopId(),yewuyuan,0);//0业务员，1促销员
			}
		}
		//处理促销员
		if(salers!=null && !"".equals(salers)){
			//按;分隔，保存门店与user_login的关系
			String[] cuxiaoyuans=salers.split(";");
			for (String cuxiaoyuan : cuxiaoyuans) {
				shopDao.saShopUserRelation(shop.getShopId(),cuxiaoyuan,1);//0业务员，1促销员
			}
		}
		//处理促销员
		if(supervisors!=null && !"".equals(supervisors)){
			//按;分隔，保存门店与user_login的关系
			String[] dudaos=supervisors.split(";");
			for (String dudao : dudaos) {
				shopDao.saShopUserRelation(shop.getShopId(),dudao,2);//2督导员
			}
		}
		
	}

	@Override
	public Shop getShop(String id) throws Exception {
		return shopDao.getShop(id);
	}

	@Override
	@Transactional
	public void editShop(Shop shop,String businessers,String salers,String supervisors) throws Exception {
		shopDao.editShop(shop);
		shopDao.clearShopUserRelations(shop.getShopId());
		//处理业务员
		if(businessers!=null && !"".equals(businessers)){
			//按;分隔，保存门店与user_login的关系
			String[] yewuyuans=businessers.split(";");
			for (String yewuyuan : yewuyuans) {
				if("".equals(yewuyuan)){
					continue;
				}
				shopDao.saShopUserRelation(shop.getShopId(),yewuyuan,0);//0业务员，1促销员
			}
		}
		//处理促销员
		if(salers!=null && !"".equals(salers)){
			//按;分隔，保存门店与user_login的关系
			String[] cuxiaoyuans=salers.split(";");
			for (String cuxiaoyuan : cuxiaoyuans) {
				if("".equals(cuxiaoyuan)){
					continue;
				}
				shopDao.saShopUserRelation(shop.getShopId(),cuxiaoyuan,1);//0业务员，1促销员
			}
		}
		//处理促销员
		if(supervisors!=null && !"".equals(supervisors)){
			//按;分隔，保存门店与user_login的关系
			String[] dudaos=supervisors.split(";");
			for (String dudao : dudaos) {
				if("".equals(dudao)){
					continue;
				}
				shopDao.saShopUserRelation(shop.getShopId(),dudao,2);//2督导员
			}
		}
	}
	@Override
	//获取门店相关的业务员，促销员
	public List<ShopUser> getShopUserRelations(String shopId,String partyId) throws Exception{
		return shopDao.getShopUserRelations(shopId,partyId);
	}

	@Override
	public void importShop(List<Shop> shops) throws Exception {
		if(shops!=null){
			for (Shop shop : shops) {
				//门店所属客户
				String customerName=shop.getCustomerId();//导入时实际上输入的是客户的名字，这里根据名字找到对应的ID
				Customer customer = customerDao.getOneCustomerByName(customerName);
				shop.setCustomerId(customer==null?null:customer.getCustomerId()+"");
				
				//国家省市县镇
				String countryName=shop.getCountryId();
				String country = customerDao.getCountryByName(countryName);
				
				String provinceName=shop.getProvinceId();
				String province = customerDao.getProvinceByName(provinceName);
				
				String cityName=shop.getCityId();
				String city = customerDao.getCityByName(cityName);
				
				String countyName = shop.getCountyId();
				String county = customerDao.getCountyByName(countyName);
				
				String townName = shop.getTownId();
				String town = customerDao.getTownByName(townName);
				
				String partyName = shop.getPartyId();
				Shop party = shopDao.selectPartyByName(partyName);
				
				String level = shop.getLevel();
				String countryId = shopDao.selectLevelByCountryName(countryName);
				Level list = shopDao.selectLevelName(level,countryId);
				
				shop.setCountryId(country == null ? null:country);
				shop.setPartyId(party.getPartyId()== null ? null:party.getPartyId());
				shop.setProvinceId(province == null?null:province);
				shop.setCityId(city == null?null:city);
				shop.setCountyId(county == null?null:county);
				shop.setTownId(town == null?null:town);
//				shop.setLevel(cfg.getPkey()==null?null:cfg.getPkey());//level不允许为空
				shop.setLevel(list==null?null:list.getId());
				
				shop.setCreateBy(WebPageUtil.getLoginedUserId());
				shop.setCreateDate(dateFormat.format(new Date()));
				shop.setStatus("1");
				shopDao.saveShop(shop);
			}
		}
	}
	//删除门店
	@Override
	public void deleteShop(Shop shop) throws Exception {
		shopDao.deleteShop(shop);
		shopDao.deleteShopSalerMapping(shop.getShopId());
	}

	@Override
	public List<Shop> getShopDataList(String conditions) throws Exception {
		return shopDao.getShopDataList(conditions);
	}
	
	@Override
	public List<Shop> getShopGeoCoord(String conditions) throws Exception {
		return shopDao.getShopGeoCoord(conditions);
	}

	@Override
	public int getShopByName(String shopName, String customer)
			throws Exception {
		return shopDao.getShopByName(shopName,customer);
	}
	
	@Override
	public Shop getRepeatByName(String shopName)
	throws Exception {
		return shopDao.getRepeatByName(shopName);
	}

	@Override
	public Map<String, Object> selectShopPhotosData(int start, int limit,
			String searchStr, String order, String sort, String conditions)
			throws Exception {
		List<ShopPhotos> list=shopDao.selectShopPhotos(start, limit,searchStr, order, sort,conditions);
		int count=shopDao.countShopPhotos(start, limit, searchStr,conditions);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", count);
		return map;
	}

	
	
	@Override
	public List<ShopPhotos> getShopPhotosDataList(String conditions)
			throws Exception {
		return shopDao.getShopPhotosDataList(conditions);
	}

	@Override
	public Shop getShopByNames(String shopName) throws Exception {
		return shopDao.getShopByNames(shopName);
	}

	@Override
	public Shop selectPartyByName(String partyName) throws Exception {
		return shopDao.selectPartyByName(partyName);
	}

	@Override
	public int selectPartyByCount(String partyName) throws Exception {
		return shopDao.selectPartyByCount(partyName);
	}

	@Override
	public List<ShopParty> selectParty(String countryId) throws Exception {
		return shopDao.selectParty(countryId);
	}

	@Override
	public XSSFWorkbook exporShopName(String conditions, String[] excelHeader,
			String title) throws Exception {
		//先查询出 导出的数据内容
				String key=null;
				List<Shop> list=shopDao.selectShopName(conditions,null);
				//设置 表头宽度
				int[] excelWidth = {120,120,120,120,130,120,200,130,130,120};
				
				XSSFWorkbook workbook = new XSSFWorkbook();
				 XSSFSheet sheet = workbook.createSheet(title);
				 XSSFRow headerRow = sheet.createRow(0);
				 
				 //导出字体样式
				 XSSFFont font = workbook.createFont();
				 font.setFontHeightInPoints((short) 12); // 字体大小
				 font.setFontName("Times New Roman");
				 font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				 
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
		         
		         
		         //导出字体样式
				 XSSFFont fontTwo = workbook.createFont();
				 fontTwo.setFontHeightInPoints((short) 10); // 字体大小
				 fontTwo.setFontName("Times New Roman");

				 
				 //导出样式
		         XSSFCellStyle styleTwo = workbook.createCellStyle();    
		         styleTwo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		         styleTwo.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		         
		         styleTwo.setFont(fontTwo);
				 
				 
				 
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
		             if(shop.getShopName()!=null  && !shop.getShopName().equals("")){
		            	 XSSFCell cell0 = row.createCell(0);
		                 cell0.setCellValue(shop.getShopName());    
		                 cell0.setCellStyle(styleTwo);
		             }
		            
		             
		            
		             
		            
		         }    
		         
		         return workbook;
	}

	@Override
	public List<Shop> selectCustomer(String countryId) throws Exception {
		return shopDao.selectCustomer(countryId);
	}

	@Override
	public List<Level> selectShopLevel(String partyId) throws Exception {
		return shopDao.selectShopLevel(partyId);
	}

	@Override
	public List<Level> selectLevelBycountry(String countryId) throws Exception {
		return shopDao.selectLevelByCountry(countryId);
	}

	@Override
	public Level selectLevelName(String levelName, String countryId)
			throws Exception {
		return shopDao.selectLevelName(levelName, countryId);
	}

	@Override
	public String selectLevelByCountryName(String countryName) throws Exception {
		return shopDao.selectLevelByCountryName(countryName);
	}

	@Override
	public String read2007Excel(File file) throws IOException {
		StringBuffer msg = new StringBuffer();
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			Object value = null;
		
			Set<String> sett= new HashSet<String>();
			Set<String> loactionSet= new HashSet<String>();
			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);

				if (null==row ) {
					break;
				}
				HashMap<String, Object> modelMap = new HashMap<String, Object>();

				//*Shop name	*Dealer code
				String shopName="";
				if(row.getCell(0)!=null
						&&
						row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					shopName=row.getCell(0).getStringCellValue();
					Shop shop = shopDao.getRepeatByName(shopName);
					if(shop != null){
						msg.append(getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.prompt08")+"</br>");

					}else{
						modelMap.put("shop",shopName);
					}
					loactionSet.add(shopName);//存储门店名称用于判断是否和location同名
					boolean isRe=sett.add(row.getCell(0).getStringCellValue());
				    if( !isRe){
				    	msg.append(getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.shop")+"</br>");
			          }
				}else{
					msg.append(getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.shopNo")+"</br>");

				}

				
				String ccode = "";
				if(row.getCell(1)!=null
						&&
						row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					
					 switch (row.getCell(1).getCellType()) {
						
					 case HSSFCell.CELL_TYPE_STRING:

						 ccode=row.getCell(1).getStringCellValue();
							Customer customer = customerDao.getRepeatByCustomerCode(ccode);
							if(customer == null){
								msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.prompt01")+"</br>");
							}else{
								modelMap.put("customer", customer.getCustomerId());
							}
							
						 
				       break;
				 
				      case HSSFCell.CELL_TYPE_FORMULA:
				 
				    	
				  
				       break;
				 
				      case HSSFCell.CELL_TYPE_NUMERIC:
				    	 
				    	 int  ccodes=(int) row.getCell(1).getNumericCellValue();
							customer = customerDao.getRepeatByCustomerCode(String.valueOf(ccodes));
							if(customer == null){
								msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.prompt01")+"</br>");
							}else{
								modelMap.put("customer", customer.getCustomerId());
							}
							
				    	  
				 
				        break;
				 
				      case HSSFCell.CELL_TYPE_ERROR:
				 
				       break;
				 
				      }
					
					
					 
					
				}/*else{
					msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.customerNo")+"</br>");

				}*/
				
				
				
				//*Country
				String ci = "";
				if(row.getCell(4)!=null
						&&
						row.getCell(4).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					String country=row.getCell(4).getStringCellValue();
					ci = shopDao.selectLevelByCountryName(country);
					if(ci!=null  && !ci.equals("") ){
						modelMap.put("country",ci);

					}else{
						msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.prompt02")+"</br>");

					}
					
				}else{
					msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.countryNo")+"</br>");

				}
				
				
				
				//	Level	Date of joining TCL		
				if(row.getCell(2)!=null
						&&
						row.getCell(2).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					String   Level=row.getCell(2).getStringCellValue();
					Level le = shopDao.selectLevelName(Level, ci);
					if(le == null){
						msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.level")+"</br>");
					}else{
						modelMap.put("level",le.getId());
					}
					
				}
				
				
				
				SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date date = new Date();
				String dt = dfd.format(date);
				Date dt1 = dfd.parse(dt);
				Date dt2;
				
				if(row.getCell(3)!=null
						&&
						row.getCell(3).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					try{
						format.setLenient(false);
						date = format.parse(row.getCell(3).getStringCellValue());//有异常要捕获
						dfd.setLenient(false);
						String newD = dfd.format(date);
						date = dfd.parse(newD);//有异常要捕获
							dt2 =dfd.parse(newD);
							if (dt1.getTime() < dt2.getTime()) {
								msg.append(getText("excel.error.row") + (i + 1)+getText("excel.error.cell")+(3+1)
										+ getText("excel.error.time") + "<br/>");
							}else{
								modelMap.put("date",newD);
							}
					}catch(Exception e){
						msg.append(getText("excel.error.row") + (i + 1)+getText("excel.error.cell")+(3+1)
								+ getText("excel.error.date") + "<br/>");
					}
					
					
					
				}
				
				// *Party	*Province	*Address	
				//*Contact name	*Contact number	*Email	*Longtitude	*Latitude
				
				if(row.getCell(5)!=null
						&&
						row.getCell(5).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					String   partyName=row.getCell(5).getStringCellValue();
					Shop party = shopDao.selectPartyByName(partyName);
					if(party == null){
						msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.party")+"</br>");
					}else{
						modelMap.put("party",party.getPartyId());
					}
					
				}else{
					msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.partyNo")+"</br>");
				}
				
				
				
				
				if(row.getCell(6)!=null
						&&
						row.getCell(6).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					String   provinceName=row.getCell(6).getStringCellValue();
					String province = customerDao.getProvinceByName(provinceName);
					if(province != null  &&  !province.equals("")){
						modelMap.put("province",province);
					}else{
						msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.prompt03")+"</br>");
					}
				}else{
					msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.partyNo")+"</br>");
				}
				
				
				
				
				
				if(row.getCell(7)!=null
						&&
						row.getCell(7).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					modelMap.put("address",row.getCell(7).getStringCellValue());
					
				}else{
					msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.addressNo")+"</br>");

				}
				
				
				if(row.getCell(8)!=null
						&&
						row.getCell(8).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					modelMap.put("contactName",row.getCell(8).getStringCellValue());
					
				}else{
//					msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.contactNameNo")+"</br>");
					modelMap.put("contactName","");

				}
				
				
				
				if(row.getCell(9)!=null
						&&
						row.getCell(9).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					
					 switch (row.getCell(9).getCellType()) {
						
					 case HSSFCell.CELL_TYPE_STRING:


							modelMap.put("contactNum",row.getCell(9).getStringCellValue());

						 
				       break;
				 
				      case HSSFCell.CELL_TYPE_FORMULA:
				 
				    	
				  
				       break;
				 
				      case HSSFCell.CELL_TYPE_NUMERIC:
				    	 
							modelMap.put("contactNum",row.getCell(9).getNumericCellValue());

							
				    	  
				 
				        break;
				 
				      case HSSFCell.CELL_TYPE_ERROR:
				 
				       break;
				 
				      }
					 
					
				}else{
//					msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.contactNumNo")+"</br>");
					modelMap.put("contactNum","");
				}
				
				

				if(row.getCell(10)!=null
						&&
						row.getCell(10).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					modelMap.put("email",row.getCell(10).getStringCellValue());
					
				}else{
//					msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.emailNo")+"</br>");
					modelMap.put("email","");
				}
				
				
				
				if(row.getCell(11)!=null  
						 && row.getCell(11).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						
						){
					 switch (row.getCell(11).getCellType()) {
					
					 case HSSFCell.CELL_TYPE_STRING:
						 if(row.getCell(11)
									.getStringCellValue()!=null){
				    		  modelMap.put("lng", row.getCell(11).getStringCellValue());
				    		 
								
								
							}
						 
				       break;
				 
				      case HSSFCell.CELL_TYPE_FORMULA:
				 
				    	  msg.append(getText("excel.error.row") + (i + 1)+getText("excel.error.cell")+(12)
									+ getText("excel.error.num") + "<br/>");
				  
				       break;
				 
				      case HSSFCell.CELL_TYPE_NUMERIC:
				    	 
				    	  if(row.getCell(11)
									.getNumericCellValue()!=0){
				    		  modelMap.put("lng", row.getCell(11).getNumericCellValue());
				    		 
								
								
							}
				       
				    	  
				 
				        break;
				 
				      case HSSFCell.CELL_TYPE_ERROR:
				 
				       break;
				 
				      }
					
					
				
				}else{
					msg.append(getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.lngNo")+"</br>");

				}
				
				if(row.getCell(12)!=null  
						 && row.getCell(12).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						
						){
					 switch (row.getCell(12).getCellType()) {
					
					 case HSSFCell.CELL_TYPE_STRING:
						  if(row.getCell(12)
									.getStringCellValue()!=null ){
				    		  modelMap.put("lat", row.getCell(12).getStringCellValue());
								
							}
				       break;
				 
				      case HSSFCell.CELL_TYPE_FORMULA:
				 
				    	  msg.append(getText("excel.error.row") + (i + 1)+getText("excel.error.cell")+(13)
									+ getText("excel.error.num") + "<br/>");
				  
				       break;
				 
				      case HSSFCell.CELL_TYPE_NUMERIC:
				    	 
				    	  if(row.getCell(12)
									.getNumericCellValue()!=0){
				    		  modelMap.put("lat", row.getCell(12).getNumericCellValue());
								
							}
				       
				    	  
				 
				        break;
				 
				      case HSSFCell.CELL_TYPE_ERROR:
				 
				       break;
				 
				      }
					
					
				
				}else{
					msg.append(getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.latNo")+"<br/>");

				}
				
				/**
				 * Location
				 */
				if(row.getCell(13)!=null){
					
					boolean isRe= loactionSet.add(row.getCell(13).getStringCellValue());
				    if( !isRe){
				    	msg.append(getText("shop.error.firstStr")+(i+1)+ " " + getText("excel.error.row") +getText("shop.error.prompt10")+"</br>");
			         }
					
				    String loactions = row.getCell(13).getStringCellValue();
					int s = (!"".equals(loactions.trim()))?  selectLocationIsExist(loactions.trim()) : 0;
//					int s = selectLocationIsExist(row.getCell(13).getStringCellValue());
					if(s == 0){
						modelMap.put("location",row.getCell(13).getStringCellValue());
					}else{
				    	  msg.append(getText("shop.error.firstStr") + (i + 1) + " " +getText("excel.error.row")  + getText("shop.error.prompt09") + "<br/>");
					}
				}else{
					modelMap.put("location","");
				}
					
				allModelList.add(modelMap);
					
				
				List<Object> linked = new LinkedList<Object>();
				List<ImportExcel> test = new LinkedList<ImportExcel>();
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
			
			System.out.println("=======allModelList======================="+allModelList);
			if(msg.length()<=0){
			
			for (int i = 0; i < allModelList.size(); i++) {
				
				
					int row = 0;
						if(
								allModelList.get(i)
								.get("shop")!=null
								&& allModelList.get(i)
								.get("shop")!=""
								
								&& allModelList.get(i).get("country")!=null
								&&  allModelList.get(i).get("country")!=""
								
								&& allModelList.get(i).get("party")!=null
								&& allModelList.get(i).get("party")!=""
								/*&& allModelList.get(i).get("province")!=null
								&& allModelList.get(i).get("province")!=""
								&& allModelList.get(i).get("address")!=null
								&&allModelList.get(i).get("address")!=""
								&& allModelList.get(i).get("contactName")!=null
								&&allModelList.get(i).get("contactName")!=""
								&& allModelList.get(i).get("contactNum")!=null
								&&allModelList.get(i).get("contactNum")!=""
								&& allModelList.get(i).get("email")!=null
								&&allModelList.get(i).get("email")!=""
								&& allModelList.get(i).get("lng")!=null
								&&allModelList.get(i).get("lng")!=""
								&& allModelList.get(i).get("lat")!=null
								&&allModelList.get(i).get("lat")!=""*/
								
								){
							//*Shop name	Dealer code 
							
							Shop shop = new Shop();
							shop.setShopName(allModelList.get(i)
								.get("shop").toString());
							shop.setLocation(allModelList.get(i)
									.get("location").toString());
							
							if( allModelList.get(i).get("customer")!=null
									&&  allModelList.get(i).get("customer")!=""){
								shop.setCustomerId(allModelList.get(i).get("customer").toString());
							}
							
							if( allModelList.get(i).get("customer")!=null
									&&  allModelList.get(i).get("customer")!=""){
								shop.setCustomerId(allModelList.get(i).get("customer").toString());
							}
							
							//	Level	
							if( allModelList.get(i).get("level")!=null
									&&  allModelList.get(i).get("level")!=""){
								shop.setLevel(allModelList.get(i).get("level").toString());
							}
							//Date of joining TCL	
							if( allModelList.get(i).get("date")!=null
									&&  allModelList.get(i).get("date")!=""){
								 String  mydate=allModelList.get(i).get("date").toString();
							            Date date=dateFormat.parse(mydate);
							            
								shop.setEnterDate(date);
							}
					
							//*Country	*Party	*Province
							shop.setCountryId(allModelList.get(i).get("country").toString());
							shop.setPartyId(allModelList.get(i).get("party").toString());
							shop.setProvinceId(allModelList.get(i).get("province").toString());
							
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							shop.setCreateDate(sdf.format(new Date()));
							
							//*Address	*Contact name	*Contact number	*Email	*Longtitude	*Latitude
							shop.setDetailAddress(allModelList.get(i).get("address").toString());
							shop.setContactName(allModelList.get(i).get("contactName").toString());
							shop.setPhone(allModelList.get(i).get("contactNum").toString());
							shop.setEmail(allModelList.get(i).get("email").toString());
							if(allModelList.get(i).get("lng")!=null &&
									!allModelList.get(i).get("lng").equals(" ")
									&&
									allModelList.get(i).get("lng")!=""
											 &&
												!allModelList.get(i).get("lng").equals("")
									){
								shop.setLng(Float.parseFloat(allModelList.get(i).get("lng").toString()));

							}
							
							if(allModelList.get(i).get("lat")!=null &&
									!allModelList.get(i).get("lat").equals(" ")
									&&
									allModelList.get(i).get("lat")!=""
									&&
											!allModelList.get(i).get("lat").equals("")
									){
								shop.setLat(Float.parseFloat(allModelList.get(i).get("lat").toString()));

							}
							shop.setStatus("1");
							shopDao.saveShop(shop);

						}
							
					}
						
		
			
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
	public String readExcelByPe(File file, String fileName) throws IOException {
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		} else if ("xlsx".equals(extension)) {
			return read2007ExcelByPe(file);
		} else {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		}
	}

	@Override
	public String read2007ExcelByPe(File file) throws IOException {
		StringBuffer msg = new StringBuffer();
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			Object value = null;
		

			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);

				if (null==row ) {
					break;
				}
				HashMap<String, Object> modelMap = new HashMap<String, Object>();

				//*Shop name	*Dealer code
				String shopName="";
				if(row.getCell(0)!=null
						&&
						row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					shopName=row.getCell(0).getStringCellValue();
					Shop shop = shopDao.getRepeatByName(shopName);
					if(shop != null){
						modelMap.put("shop",shop.getShopId());
					}else{
						msg.append(getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.noShop")+"<br>");


					}
					
				}else{
					msg.append(getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.shopNo")+"<br>");

				}

				
				
				if(row.getCell(2)!=null
						&&
						row.getCell(2).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
				
					if(row.getCell(1)!=null
							&&
							row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK
							){
						String user=row.getCell(1).getStringCellValue();
						int userCount=shopDao.selectUserByCount(user);
						if(userCount>0){
							ShopUser isRole=shopDao.selectUserByRole(user);
							if(isRole!=null){
								//0业务员，1促销员,2督导
								if(row.getCell(2).getStringCellValue().toLowerCase().trim().equals("sales")){
									
									if(isRole.getRoleId().contains("SALES")){
										shopDao.deleteShopMapping(user);
										modelMap.put("role",0);
										modelMap.put("user",user);
									}else{
										msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.roleIsNot")+" Sales"+"</br>");

									}
									
									
								}else if(row.getCell(2).getStringCellValue().toLowerCase().trim().equals("promoter")){
									if(isRole.getRoleId().contains("PROM")){
										ShopUser shopUser=shopDao.selectUserByPro(user);
										if(shopUser!=null){
											if(shopUser.getShopId()!=null && shopUser.getShopName()!=null 
													&& 
													!shopUser.getShopId().equals("") && !shopUser.getShopName().equals("") ){
												msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.userExisting")+" "+shopUser.getShopName()+"</br>");
												
											}else if(
													(shopUser.getShopId()==null || shopUser.getShopName()==null 
																|| 
														shopUser.getShopId().equals("") || shopUser.getShopName().equals(""))
														&&
														shopUser.getUserLoginId()!=null && !shopUser.getUserLoginId().equals("")
														){
													int rows=shopDao.deleteShopMapping(user);
													if(rows>0){
														modelMap.put("role",1);
														modelMap.put("user",user);
													}
												
											}
										}else{
											modelMap.put("role",1);
											modelMap.put("user",user);
											
										}
										
										
									}else{
										msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.roleIsNot")+" Promoter"+"</br>");

									}
									

									
									
								
									
								}else if(row.getCell(2).getStringCellValue().toLowerCase().trim().equals("supervisor")){
									if(isRole.getRoleId().contains("SUPERVISOR")){
										shopDao.deleteShopMapping(user);
										modelMap.put("role",2);
										modelMap.put("user",user);
									}else{
										msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.roleIsNot")+" Supervisor"+"</br>");

									}
									
									
								}else{
									msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.role")+"</br>");
								}
							}else{
								msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.roleIsNull")+"</br>");

							}
							
				
							
						}else{
							msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.userNo")+"</br>");

						}
						
						
						
						
						
					
					}else{
						msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.userNotNull")+"</br>");

					}
					
					
					
				}else{
					msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.roleNo")+"</br>");

				}
				
				
				
						
				allModelList.add(modelMap);
					
				
				
				List<Object> linked = new LinkedList<Object>();
				List<ImportExcel> test = new LinkedList<ImportExcel>();
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
			
			System.out.println("=======allModelList======================="+allModelList);
			if(msg.length()<=0){
			
			for (int i = 0; i < allModelList.size(); i++) {
				
				
						if(
								allModelList.get(i)
								.get("shop")!=null
								&& allModelList.get(i)
								.get("shop")!=""
								
								&& allModelList.get(i).get("user")!=null
								&&  allModelList.get(i).get("user")!=""
								
								&& allModelList.get(i).get("role")!=null
								&& allModelList.get(i).get("role")!=""
								
								){
							//*Shop name	Dealer code 
							
							shopDao.saShopUserRelation(
									Integer.parseInt(allModelList.get(i).get("shop").toString()),
									allModelList.get(i).get("user").toString(),
									Integer.parseInt(allModelList.get(i).get("role").toString())
									);

						}
							
					}
						
		
			
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
	public String readExcelByShop(File file, String fileName) throws Exception{
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		} else if ("xlsx".equals(extension)) {
					return exportShop(file);

		} else {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		}
	}

	@Override
	public String exportShop(File file) throws Exception {
		StringBuffer msg = new StringBuffer();
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			Object value = null;
		

			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);

				if (null==row ) {
					break;
				}
				HashMap<String, Object> modelMap = new HashMap<String, Object>();

				String shopName="";
				if(row.getCell(0)!=null
						&&
						row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					shopName=row.getCell(0).getStringCellValue();
					Shop shop = shopDao.getRepeatByName(shopName);
					if(shop != null){
						modelMap.put("shopBefore",shop.getShopId());
					}else{
						
						msg.append("</br>"+getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.noShop")
								+"("+ row.getCell(0).getStringCellValue()+")"+"</br>");


					}
					
				}else{
					
					msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell") + (1)+" "
							+ getText("excel.error.null") + "<br/>");
				}
				
				
				
	

				
				
				if(row.getCell(1)!=null
						&&
						row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK
						){
					shopName=row.getCell(1).getStringCellValue();
					Shop shop = shopDao.getRepeatByName(shopName);
					if(shop == null){
						modelMap.put("shopAfter",shopName);
					}else{
						
						msg.append(getText("shop.error.firstStr")+(i+1)+" "+getText("shop.error.prompt08")
								+"("+ row.getCell(1).getStringCellValue()+")"+"</br>");
					

					}
					
				}else{
					msg.append(getText("excel.error.row") + (i + 1)+" "+getText("excel.error.cell") + (2)+" "
							+ getText("excel.error.null") + "<br/>");
				}
				
				
				
			
				
				
						
				allModelList.add(modelMap);
					
				
				
				List<Object> linked = new LinkedList<Object>();
				List<ImportExcel> test = new LinkedList<ImportExcel>();
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
			
			System.out.println("=======allModelList======================="+allModelList);
			if(msg.length()<=0){
			List<Shop> shopNameByUpdate=new ArrayList<Shop>();

			for (int i = 0; i < allModelList.size(); i++) {
				
				
						if(
								allModelList.get(i)
								.get("shopBefore")!=null
								&& allModelList.get(i)
								.get("shopBefore")!=""
								
								&& allModelList.get(i).get("shopAfter")!=null
								&&  allModelList.get(i).get("shopAfter")!=""
								
								
								
								){

							Shop shop=new Shop();
							shop.setShopId(Integer.parseInt(allModelList.get(i)
								.get("shopBefore").toString()));
							shop.setShopName(allModelList.get(i).get("shopAfter").toString());
							
							shopNameByUpdate.add(shop);
						}
							
					}
						
			if(shopNameByUpdate.size()>0){
				shopDao.updateShopName(shopNameByUpdate);
			}
		
			
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
	
	/**
	 * 查询Location是否已经存在
	 */
	@Override
	public int selectLocationIsExist(String location) throws Exception {
		return shopDao.selectLocationIsExist(location);
	}

	@Override
	public Map<String, Object> selectShopsBySupSaleData(int start, int limit,
			String searchStr, String levelNum, String order, String sort,
			String conditions) throws Exception {
			String _levelNum=levelNum == ""?null:levelNum;
		
		List<Shop> list=shopDao.selectShopBySupSale(start, limit,searchStr,_levelNum, order, sort,conditions);
		int count=shopDao.countShopBySupSale(start, limit, searchStr,_levelNum,conditions);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", count);
		return map;
	}
}
