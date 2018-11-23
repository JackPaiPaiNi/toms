package cn.tcl.platform.sale.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.customer.dao.ICustomerDao;
import cn.tcl.platform.excel.actions.DateUtil;
import cn.tcl.platform.modelmap.dao.IModelMapDao;
import cn.tcl.platform.party.dao.IPartyDAO;
import cn.tcl.platform.party.vo.Party;
import cn.tcl.platform.sale.dao.ISaleDao;
import cn.tcl.platform.sale.dao.ISaleTargetDao;
import cn.tcl.platform.sale.dao.ISampleDeviceDao;
import cn.tcl.platform.sale.dao.ITerminalPhotoDao;
import cn.tcl.platform.sale.vo.Sale;
import cn.tcl.platform.sale.vo.SaleTarget;
import cn.tcl.platform.sale.vo.SampleDevice;
import cn.tcl.platform.sale.vo.TerminalPhoto;
import cn.tcl.platform.shop.dao.IShopDao;
import cn.tcl.platform.shop.vo.Shop;
import cn.tcl.platform.target.dao.ImportTargetDao;
import cn.tcl.platform.target.service.ImportTargetService;
@Service("saleService")
public class ISaleServiceImpl extends BaseAction implements ISaleService {
	@Autowired
	private ISaleDao saleDao;
	@Autowired
	private ISaleTargetDao saleTargetDao;
	@Autowired
	private ITerminalPhotoDao terminalPhotoDao;
	@Autowired
	private ISampleDeviceDao sampleDeviceDao;
	@Autowired
	private ICustomerDao customerDao;
	@Autowired
	private IShopDao shopDao;
	@Autowired
	private IModelMapDao modelMapDao;
	@Autowired
	private IPartyDAO partyDAO;
	@Autowired
	private ImportTargetDao importTargetDao;
	
	
	@Override
	public Map<String, Object> selectSalesData(int start, int limit, String searchStr, String order, String sort,
			String conditions) throws Exception {
		List<Sale> list=saleDao.selectSales(start, limit, searchStr, order, sort,conditions);
		Map<String, Object> map=new HashMap<String, Object>();
		int count=saleDao.countSales(start, limit, searchStr,conditions);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}
	@Override
	public Map<String, Object> selectSalesDataByMobile(int start, int limit, String searchStr, String order, String sort,
			String conditions) throws Exception {
		List<Sale> list=saleDao.selectSalesByMobile(start, limit, searchStr, order, sort,conditions);
		Map<String, Object> map=new HashMap<String, Object>();
		int count=saleDao.countSalesByMobile(start, limit, searchStr,conditions);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}
	
	

	@Override
	public Sale getSale(Integer sid) throws Exception {
		return saleDao.getSale(sid);
	}

	@Override
	public SaleTarget getSaleTarget(Integer sid) throws Exception {
		return saleTargetDao.getSaleTarget(sid);
	}

	@Override
	public Map<String, Object> selectPhotos(int start, int limit, String searchStr, String order, String sort,
			String conditions) throws Exception {
		List<TerminalPhoto> list=terminalPhotoDao.selectPhotos(start, limit, searchStr, order, sort,conditions);
		Map<String, Object> map=new HashMap<String, Object>();
		int count=terminalPhotoDao.countPhotos(start, limit, searchStr,conditions);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}

	@Override
	public Map<String, Object> selectSampleDevices(int start, int limit, String searchStr, String order, String sort,
			String conditions) throws Exception {
		List<SampleDevice> list=sampleDeviceDao.selectSampleDevices(start, limit, searchStr, order, sort,conditions);
		Map<String, Object> map=new HashMap<String, Object>();
		int count=sampleDeviceDao.countSampleDevices(start, limit, searchStr,conditions);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}
	//保存销售数据
	@Override
	public void saveSale(Sale sale) throws Exception {
		saleDao.saveSale(sale);
	}

	@Override
	public void addSaleTarget(SaleTarget saleTarget) throws Exception {
		saleTargetDao.insertSaleTarget(saleTarget);
	}

	@Override
	public void editSaleTarget(SaleTarget saleTarget) throws Exception {
		saleTargetDao.updateSaleTarget(saleTarget);
	}

	@SuppressWarnings("deprecation")
	@Override
	public HSSFWorkbook exporSale(String conditions,String searchStr,String[] excelHeader,String title) throws Exception {
		//先查询出 导出的数据内容
		List<Sale> exporSaleList = saleDao.searchExporSale(conditions,searchStr);
		//设置 表头宽度
		int[] excelWidth = {120,120,120,120,130,120,200,130,130,120};
		
		 HSSFWorkbook workbook = new HSSFWorkbook();
		 HSSFSheet sheet = workbook.createSheet(title);
		 HSSFRow headerRow = sheet.createRow(0);
		 
		 //导出字体样式
		 HSSFFont font = workbook.createFont();
		 
		 //导出样式
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
         //表头数据
         for (int i = 0; i < excelHeader.length; i++) {   
             HSSFCell cell = row.createCell(i);    
             cell.setCellValue(excelHeader[i]);    
             cell.setCellStyle(style);  
             
         }   
         
         //表体数据
         for (int i = 0; i < exporSaleList.size(); i++) {    
             row = sheet.createRow(i + 2);    
             Sale sale = exporSaleList.get(i);
             
             //-------------单元格-------------------
             
             /**
              * 国家
              */
             HSSFCell cell0 = row.createCell(0);
             cell0.setCellValue(sale.getCountry());    
             cell0.setCellStyle(style);
             
             /**
              * 所属机构
              */
             HSSFCell cell1 = row.createCell(1);
             cell1.setCellValue(sale.getPartyName());    
             cell1.setCellStyle(style);
             
             /**
              * 客户
              */
             HSSFCell cell2 = row.createCell(2);
             cell2.setCellValue(sale.getCustomerName());    
             cell2.setCellStyle(style);
             
             /**
              * 门店名称
              */
             HSSFCell cell3 = row.createCell(3);
             cell3.setCellValue(sale.getShopName());    
             cell3.setCellStyle(style);
             
             /**
              * 总部型号
              */
             HSSFCell cell4 = row.createCell(4);
             cell4.setCellValue(sale.getHqModel());    
             cell4.setCellStyle(style);
             
             /**
              * 分公司型号
              */
             HSSFCell cell5 = row.createCell(5);
             cell5.setCellValue(sale.getModel());    
             cell5.setCellStyle(style);
             
             /**
              * 数量
              */
             HSSFCell cell6 = row.createCell(6);
             cell6.setCellValue(sale.getQuantity());    
             cell6.setCellStyle(style);
             
             /**
              * 销售金额
              */
             HSSFCell cell7 = row.createCell(7);
             cell7.setCellValue(sale.getAmount());    
             cell7.setCellStyle(style);
             
             /**
              * 数据日期
              */
             HSSFCell cell8 = row.createCell(8);
             cell8.setCellValue(sale.getDatadate());    
             cell8.setCellStyle(style);
             
             /**
              * 上传人
              */
             HSSFCell cell9 = row.createCell(9);
             cell9.setCellValue(sale.getUserName());    
             cell9.setCellStyle(style);
         }    
         
         return workbook;
	}

	
	@SuppressWarnings("deprecation")
	@Override
	public HSSFWorkbook exporCountrySale(String conditions,String searchStr,String[] excelHeader,String title) throws Exception {
		//先查询出 导出的数据内容
		List<Sale> exporSaleList = saleDao.searchCountryExporSale(conditions,searchStr);
		//设置 表头宽度
		int[] excelWidth = {120,120,120,120,130,120,200,130,130,120};
		
		 HSSFWorkbook workbook = new HSSFWorkbook();
		 HSSFSheet sheet = workbook.createSheet(title);
		 HSSFRow headerRow = sheet.createRow(0);
		 
		 //导出字体样式
		 HSSFFont font = workbook.createFont();
		 
		 //导出样式
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
         //表头数据
         for (int i = 0; i < excelHeader.length; i++) {   
             HSSFCell cell = row.createCell(i);    
             cell.setCellValue(excelHeader[i]);    
             cell.setCellStyle(style);  
             
         }   
         
         //表体数据
         for (int i = 0; i < exporSaleList.size(); i++) {    
             row = sheet.createRow(i + 2);    
             Sale sale = exporSaleList.get(i);
             
             //-------------单元格-------------------
             
           
             /**
              * 国家
              */
             HSSFCell cell0 = row.createCell(0);
             cell0.setCellValue(sale.getCountry());    
             cell0.setCellStyle(style);
             
           
             /**
              * 总部型号
              */
             HSSFCell cell4 = row.createCell(1);
             cell4.setCellValue(sale.getHqModel());    
             cell4.setCellStyle(style);
             
             /**
              * 分公司型号
              */
             HSSFCell cell5 = row.createCell(2);
             cell5.setCellValue(sale.getModel());    
             cell5.setCellStyle(style);
             
             /**
              * 数量
              */
             HSSFCell cell6 = row.createCell(3);
             cell6.setCellValue(sale.getQuantity());    
             cell6.setCellStyle(style);
             
             /**
              * 销售金额
              */
             HSSFCell cell7 = row.createCell(4);
             cell7.setCellValue(sale.getAmount());    
             cell7.setCellStyle(style);
             
             /**
              * 数据日期
              */
             HSSFCell cell8 = row.createCell(5);
             cell8.setCellValue(sale.getDatadate());    
             cell8.setCellStyle(style);
             
             /**
              * 上传人
              */
             HSSFCell cell9 = row.createCell(6);
             cell9.setCellValue(sale.getUserName());    
             cell9.setCellStyle(style);
         }    
         
         return workbook;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public HSSFWorkbook exporCustomerSale(String conditions,String searchStr,String[] excelHeader,String title) throws Exception {
		//先查询出 导出的数据内容
		List<Sale> exporSaleList = saleDao.searchCustomerExporSale(conditions,searchStr);
		//设置 表头宽度
		int[] excelWidth = {120,120,120,120,130,120,200,130,130,120};
		
		 HSSFWorkbook workbook = new HSSFWorkbook();
		 HSSFSheet sheet = workbook.createSheet(title);
		 HSSFRow headerRow = sheet.createRow(0);
		 
		 //导出字体样式
		 HSSFFont font = workbook.createFont();
		 
		 //导出样式
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
         //表头数据
         for (int i = 0; i < excelHeader.length; i++) {   
             HSSFCell cell = row.createCell(i);    
             cell.setCellValue(excelHeader[i]);    
             cell.setCellStyle(style);  
             
         }   
         
         //表体数据
         for (int i = 0; i < exporSaleList.size(); i++) {    
             row = sheet.createRow(i + 2);    
             Sale sale = exporSaleList.get(i);
             
             //-------------单元格-------------------
             /**
              * 国家
              */
             HSSFCell cell0 = row.createCell(0);
             cell0.setCellValue(sale.getCountry());    
             cell0.setCellStyle(style);
             
             /**
              * 所属机构
              */
             HSSFCell cell1 = row.createCell(1);
             cell1.setCellValue(sale.getPartyName());    
             cell1.setCellStyle(style);
             
             /**
              * 客户
              */
             HSSFCell cell2 = row.createCell(2);
             cell2.setCellValue(sale.getCustomerName());    
             cell2.setCellStyle(style);
             
           
             /**
              * 总部型号
              */
             HSSFCell cell4 = row.createCell(3);
             cell4.setCellValue(sale.getHqModel());    
             cell4.setCellStyle(style);
             
             /**
              * 分公司型号
              */
             HSSFCell cell5 = row.createCell(4);
             cell5.setCellValue(sale.getModel());    
             cell5.setCellStyle(style);
             
             /**
              * 数量
              */
             HSSFCell cell6 = row.createCell(5);
             cell6.setCellValue(sale.getQuantity());    
             cell6.setCellStyle(style);
             
             /**
              * 销售金额
              */
             HSSFCell cell7 = row.createCell(6);
             cell7.setCellValue(sale.getAmount());    
             cell7.setCellStyle(style);
             
             /**
              * 数据日期
              */
             HSSFCell cell8 = row.createCell(7);
             cell8.setCellValue(sale.getDatadate());    
             cell8.setCellStyle(style);
             
             /**
              * 上传人
              */
             HSSFCell cell9 = row.createCell(8);
             cell9.setCellValue(sale.getUserName());    
             cell9.setCellStyle(style);
             
             
           
         }    
         
         return workbook;
	}
	@SuppressWarnings("deprecation")
	@Override
	public HSSFWorkbook exporSamples(String conditions,String[] excelHeader,String title) throws Exception {
		//先查询出 导出的数据内容
		List<SampleDevice> exporSampleList = sampleDeviceDao.searchExporSamples(conditions);
		//设置 表头宽度
		int[] excelWidth = {120,120,120,120,120,130,200,130,250};
		
		 HSSFWorkbook workbook = new HSSFWorkbook();
		 HSSFSheet sheet = workbook.createSheet(title);
		 HSSFRow headerRow = sheet.createRow(0);
		 
		 //导出字体样式
		 HSSFFont font = workbook.createFont();
		 
		 //导出样式
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
         //表头数据
         for (int i = 0; i < excelHeader.length; i++) {   
             HSSFCell cell = row.createCell(i);    
             cell.setCellValue(excelHeader[i]);    
             cell.setCellStyle(style);  
             
         }   
         
         //表体数据
         for (int i = 0; i < exporSampleList.size(); i++) {    
             row = sheet.createRow(i + 2);    
             SampleDevice sample = exporSampleList.get(i);
             
             //-------------单元格-------------------
             
             /**
              * 分支机构
              */
             HSSFCell cell0 = row.createCell(0);
             cell0.setCellValue(sample.getPartyName());    
             cell0.setCellStyle(style);
             
             /**
              * 客户
              */
             HSSFCell cell1 = row.createCell(1);
             cell1.setCellValue(sample.getCustomerName());    
             cell1.setCellStyle(style);
             
             /**
              * 门店
              */
             HSSFCell cell2 = row.createCell(2);
             cell2.setCellValue(sample.getShopName());    
             cell2.setCellStyle(style);
             
             /**
              * 总部型号
              */
             HSSFCell cell3 = row.createCell(3);
             cell3.setCellValue(sample.getHqModel());    
             cell3.setCellStyle(style);
             
             /**
              * 分公司型号
              */
             HSSFCell cell4 = row.createCell(4);
             cell4.setCellValue(sample.getModel());    
             cell4.setCellStyle(style);
             
             /**
              * 数量
              */
             HSSFCell cell5 = row.createCell(5);
             cell5.setCellValue(sample.getQuantity());    
             cell5.setCellStyle(style);
             
             /**
              * 上传人
              */
             HSSFCell cell6 = row.createCell(6);
             cell6.setCellValue(sample.getUserName());    
             cell6.setCellStyle(style);
             
             /**
              * 日期
              */
             HSSFCell cell7 = row.createCell(7);
             cell7.setCellValue(sample.getDatadate());    
             cell7.setCellStyle(style);
             
         }    
         
         return workbook;
	}

	@Override
	public void deleteSaleTarget(SaleTarget saleTarget) throws Exception {
		saleTargetDao.deleteSaleTargetData(saleTarget);
	}

	@Override
	public int validationShopId(String shopId) throws Exception {
		return saleTargetDao.validationShopId(shopId);
	}

	@Override
	public List<Sale> getSaleCategoryData(String searchStr, String conditions,String searchStr1)
			throws Exception {
		return saleDao.getSaleCategoryData(searchStr,conditions,searchStr1);
	}

	@Override
	public List<Sale> getProductSizeSaleList(String conditions, String searchStr)
			throws Exception {
		return saleDao.getProductSizeSaleList(conditions,searchStr);
	}

	@Override
	public int getSaleCompletionList(String conditions, String searchStr)
			throws Exception {
		List<Sale> sList = saleDao.getSaleCompletionList(conditions,searchStr);
		int result = 0;
		if(sList.size() > 0){
			for (Sale sale : sList) {
				if(sale.getQuantity() != null){
					result += sale.getQuantity();
				}
			}
		}
		return result;
	}

	@Override
	public int getSaleTargetCompletionList(String conditionsToSaleTarget) throws Exception {
		List<SaleTarget> stList = saleTargetDao.getSaleTargetCompletionList(conditionsToSaleTarget);
		int result = 0;
		if(stList.size() > 0){
			for (SaleTarget saleTarget : stList) {
				if(saleTarget.getQuantity() != null){
					result += saleTarget.getQuantity();
				}
			}
		}
		return result;
	}

	@Override
	public void importSale(List<Sale> sales) throws Exception {
		if(sales!=null){
			for (Sale sale : sales) {
				
				//国家
				String countryName=sale.getCountry();
				String country = customerDao.getCountryByName(countryName);
				sale.setCountry(country == null ? null:country);
				
				String shopName=sale.getShopName();
				Shop shop = shopDao.getShopByNameOrLocation(shopName);
				sale.setShopId(shop.getShopId()== null ? null:shop.getShopId());
				
				
				List<Party> partys=partyDAO.getCountryByPartyId(country);
				String list="";
				String cond =null;
			
				int row = modelMapDao.getModelIdByParty(cond,sale.getModel(), list);
				if(row>=1){
					sale.setModel(sale.getModel());
					
				}
				
				
				sale.setUserId(WebPageUtil.getLoginedUserId());
				
				sale.setAmount(sale.getQuantity()*sale.getPrice());
				saleDao.saveSales(sale);
			}
		}
		
	}

	@Override
	public void editSale(Sale sale) throws Exception {
		saleDao.updateSale(sale);
		
	}

	@Override
	public void deleteSale(Sale sale) throws Exception {
		saleDao.deleteSale(sale);
	}

	@Override
	public String readExcel(File file, String fileName,String targetType) throws IOException {
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			//return read2003Excel(file,targetType);
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		} else if ("xlsx".equals(extension)) {
			return read2007Excel(file,targetType);
		} else {
			//throw new IOException("不支持的文件类型");
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		}
	}

	@Override
	public String read2007Excel(File file, String targetType) throws IOException {
		StringBuffer msg = new StringBuffer();
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			Object value = null;
			
			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			
			/**
			 * 模板检测
			 */
			msg.append(templateError(sheet));
			
			for (int i = 3; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					break;
				}
				if(row.getCell(7)!=null && row.getCell(7).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					String typeName = row.getCell(7).getStringCellValue();
					if(!typeName.equals("TV") && !typeName.equals("tv")){
						msg.append(getText("sale.error.row")+(i+1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel(8)
								+ getText("sale.error.tvtype")+"<br/>");
					}
				}else{
					msg.append(getText("sale.error.row")+(i+1)+getText("sale.error.cell")+DateUtil.getExcelColumnLabel(8)
							+ getText("sale.error.tvtype")+"<br/>");
				}
				String countryName="";
				String countryId ="";
				if(row.getCell(6)!=null  && row.getCell(6).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					 countryName = row.getCell(6).getStringCellValue();
					 countryId = saleTargetDao.selectCountry(countryName);
					if (countryId == null) {
						msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((6+1))
								+ getText("sale.error.country") + "<br/>");
					}else if(!countryId.equals(WebPageUtil.getLoginedUser().getPartyId()) && !WebPageUtil.isHQRole()){
						msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((6+1))
								+ getText("imports.error.otherCountry") + "<br/>");
					}
					
				}else{
					msg.append(getText("sale.error.row") + (i + 1)
							+ getText("sale.error.countryname") + "<br/>");
				}
				String shopName="";
				Shop shop=null;
				String customerName = null;
				String customer = null;
				
				HashMap<String, Object> storeMap = new HashMap<String, Object>();
				List<Object> linked = new LinkedList<Object>();
				if(row.getCell(0)!=null  && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
				
					if("4".equals(targetType)){//4识别为门店
						
						shopName = row.getCell(0).getStringCellValue();
						 shop = shopDao.getShopByNameOrLocation(shopName);
						if (shop == null) {
							msg.append(getText("sale.error.row") + (i + 1)
									+ getText("sale.error.shop") +" ("+shopName+")"+"<br/>");
						}
					}else if("3".equals(targetType)){//3识别为客户（渠道）
						
						customerName = row.getCell(0).getStringCellValue();
						customer = importTargetDao.selectChannel(customerName);
						if (customer == null) {
							msg.append(getText("sale.error.row") + (i + 1)
									+ getText("sale.error.Channel") +" ("+customerName+")"+"<br/>");
						}
					}else if("1".equals(targetType)){
						String cty = row.getCell(0).getStringCellValue();
						String ctyId = saleTargetDao.selectCountry(cty);
						if (ctyId == null) {
							msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((0+1))
									+ getText("sale.error.country") + "<br/>");
						}
					}
				}else{
					if("4".equals(targetType)){//4识别为门店
						msg.append(getText("sale.error.row") + (i + 1) +getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((0+1))
								+ getText("sale.error.shopname") + "<br/>");
					}else if("3".equals(targetType)){
						msg.append(getText("sale.error.row") + (i + 1) +getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((0+1))
								+ getText("sale.error.customerName") + "<br/>");
					}else if("1".equals(targetType)){
						msg.append(getText("sale.error.row") + (i + 1) +getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((0+1))
								+ getText("sale.error.countryname") + "<br/>");
					}
				}
				
					SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					Date date = new Date();
					String dt = dfd.format(date);
					Date dt1 = dfd.parse(dt);
					Date dt2;
				
						if(row.getCell(5)!=null  && row.getCell(5).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
							try{
								format.setLenient(false);
								date = format.parse(row.getCell(5).getStringCellValue());//有异常要捕获
								dfd.setLenient(false);
								String newD = dfd.format(date);
								date = dfd.parse(newD);//有异常要捕获
									dt2 =dfd.parse(newD);
									if (dt1.getTime() < dt2.getTime()) {
										msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((5+1))
												+ getText("sale.error.time") + "<br/>");
									} 
							}catch(Exception e){
								msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((5+1))
										+ getText("sale.error.date") + "<br/>");
							}
						}else{
							msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((5+1))
									+ getText("sale.error.dateNo") + "<br/>");
						}
							
					if(row.getCell(0)!=null  && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						
						if("4".equals(targetType)){//4识别为门店
							
							shopName = row.getCell(0).getStringCellValue();
							shop = shopDao.getShopByNameOrLocation(shopName);
							if (shop != null) {
								storeMap.put("Store", shop.getShopId() == null ? null
										: shop.getShopId());
								storeMap.put("StoreName", shopName);
							}
						}else if("3".equals(targetType)){//3识别为客户（渠道）
							
							customerName = row.getCell(0).getStringCellValue();
							customer = importTargetDao.selectChannel(customerName);
							storeMap.put("Store", customer);
							storeMap.put("StoreName", customerName);
						}else if("1".equals(targetType)){//1识别为国家
							storeMap.put("Store", countryId);
							storeMap.put("StoreName",countryName);
						}
					}
					
					/**
					 * 数量销售目标(必填)
					 */
					if(row.getCell(1)!=null  && row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						XSSFCell cell1 = row.getCell(1);	//把excel中的单元格数据转化为String类型
						cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
						storeMap.put("quantity", cell1.getStringCellValue());
					}else{
						msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((1+1))
								+ getText("sale.qty.target.notnull") + "<br/>");
					}
					
					/**
					 * 金额销售目标(必填)
					 */
					if(row.getCell(2)!=null && row.getCell(2).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						XSSFCell cell2 = row.getCell(2);	//把excel中的单元格数据转化为String类型
						cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
						storeMap.put("amount", cell2.getStringCellValue());
					}else{
						msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((2+1))
								+ getText("sale.amt.target.notnull") + "<br/>");
					}
					
					/**
					 * 挑战数量可填可不填(默认为0)
					 */
					if(row.getCell(3)!=null && row.getCell(3).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						XSSFCell cell3 = row.getCell(3);	
						cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
						storeMap.put("tz_quantity", cell3.getStringCellValue());
					}else{
						storeMap.put("tz_quantity", "0");
					}
					
						
					/**
					 * 挑战金额可填可不填(默认为0)
					 */
					if(row.getCell(4)!=null && row.getCell(4).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						XSSFCell cell4 = row.getCell(4);
						cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
						storeMap.put("tz_Amount", cell4.getStringCellValue());
					}else{
						storeMap.put("tz_Amount", "0");
					}
					
					if(row.getCell(5)!=null && row.getCell(5).getCellType()!=HSSFCell.CELL_TYPE_BLANK ){
						try{
							format.setLenient(false);
							date = format.parse(row.getCell(5).getStringCellValue());//有异常要捕获
							dfd.setLenient(false);
							String newD = dfd.format(date);
							date = dfd.parse(newD);//有异常要捕获
								dt2 =dfd.parse(newD);

								if (dt1.getTime() < dt2.getTime()) {
									
								} else {
									storeMap.put("datadate", newD);
								}
						}catch(Exception e){
							
						}
						
				
					}
					if(row.getCell(6)!=null  && row.getCell(6).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						countryName = row.getCell(6).getStringCellValue();
						countryId = saleTargetDao.selectCountry(countryName);
						if (countryId != null) {
							storeMap.put("Country", countryId == null ? null
									: countryId);
							storeMap.put("CountryName", countryName);
						}
					}
					if(row.getCell(7) != null  && row.getCell(7).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						String typeName = row.getCell(7).getStringCellValue();
						String type = saleTargetDao.selectType(typeName);
						if(type!=null){
							storeMap.put("type", type==null?null:type);
						}
					}
					allModelList.add(storeMap);
					
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
			for (int i = 0; i < allModelList.size(); i++) {
				SaleTarget excel = new SaleTarget();
				String datadate="";
				String countryName="";
				String type="";
				if(allModelList.get(i).get("type")!=null){
				type =	allModelList.get(i).get("type").toString();
				excel.setType(Integer.parseInt(allModelList.get(i).get("type").toString()));
				}
				if(allModelList.get(i).get("CountryName")!=null){
				countryName = allModelList.get(i).get("CountryName")
								.toString();
				}
				if(allModelList.get(i).get("datadate")!=null){
					datadate = allModelList.get(i).get("datadate")
							.toString();
					excel.setDatadate(datadate);
				}
				String shopName="";
				if(allModelList.get(i).get("StoreName")!=null){
					 shopName = allModelList.get(i).get("StoreName")
								.toString();
				}
		
				if(allModelList.get(i).get("Store")!=null){
				excel.setTargetId((allModelList.get(i)
						.get("Store").toString()));
				}
				if(allModelList.get(i).get("Country")!=null){
					excel.setCountryId(allModelList.get(i).get("Country")
							.toString());
				}
				excel.setClassId(targetType);
				
				int row = 0;
				if(msg.length()<=0){
					
					BigDecimal bqt = new BigDecimal(allModelList.get(i)
							.get("quantity").toString());
					excel.setQuantity(Long.parseLong(bqt.setScale(0,BigDecimal.ROUND_HALF_UP).toString()));
					
					BigDecimal bdAmount = new BigDecimal(allModelList.get(i)
							.get("amount").toString());
					String sAmount = bdAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(); //四舍五入，保留两位小数					
					excel.setAmount(sAmount);
					
					BigDecimal tzqt = new BigDecimal(allModelList.get(i)
							.get("tz_quantity").toString());
					excel.setTzQuantity(Long.parseLong(tzqt.setScale(0,BigDecimal.ROUND_HALF_UP).toString()));
												
					
					
					BigDecimal bdTzAmount = new BigDecimal(allModelList.get(i)
							.get("tz_Amount").toString());
					String stzAmount = bdTzAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
					excel.setTzAmount(stzAmount);
					excel.sethQuantity(Integer.parseInt(bqt.setScale(0,BigDecimal.ROUND_HALF_UP).toString()));
					excel.sethTzQuantity(Integer.parseInt(tzqt.setScale(0,BigDecimal.ROUND_HALF_UP).toString()));
					
					BigDecimal bd = new BigDecimal(allModelList.get(i)
							.get("amount").toString());
					java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
					nf.setGroupingUsed(false);
					double amount=Double.parseDouble(nf.format(Double.parseDouble(allModelList.get(i)
							.get("amount").toString())));
					double tzAmount=Double.parseDouble(nf.format(Double.parseDouble(allModelList.get(i)
							.get("tz_Amount").toString())));
					
					System.out.println(amount+"======"+tzAmount);
//						XSSFRow roc = sheet.getRow(3);
					if(allModelList.get(i).get("Country")!=null){
						String country=allModelList.get(i).get("Country").toString();
//							String datedate = allModelList.get(i).get("datadate").toString();
//							String	countryId = saleTargetDao.selectCountry(country);
						Float exchange = saleTargetDao.selectExchange(country);
//							System.out.println(exchange+"=============================="+datedate);
						if (exchange != null) {
						bd=new BigDecimal(amount*exchange);
						double Hamount=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						excel.sethAmount(Hamount);
						bd=new BigDecimal(tzAmount*exchange);
						double HtzAmount=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						excel.sethTzAmount(HtzAmount);
						}else{
							msg.append(countryName+"  "+getText("sale.error.currency") );
						}
					}
				
				}
//						 row = saleTargetDao.saveSales(excel);
				if(allModelList.get(i).get("StoreName")!=null && allModelList.get(i).get("datadate")!=null 
						&& allModelList.get(i).get("Country")!=null && "".equals(msg.toString())){
					
					String id =  allModelList.get(i).get("Store") +"";
					int r = saleTargetDao.selectCount(id,datadate,type,targetType,excel.getCountryId());
					
					if(r==0){
						// 执行插入
						row = saleTargetDao.saveSales(excel);
						if (row == 0) {
							msg.append(shopName + ":"
									+ getText("store.error.saleInsert") + "<br/>");

						}
					}
					else{
						saleTargetDao.updateTarget(excel);
					}
				}
			}
				
			System.out.println(allModelList);
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
	public String read2003Excel(File file, String targetType) throws IOException {
		StringBuffer msg = new StringBuffer();
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			HSSFWorkbook x = (HSSFWorkbook) new Object();
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			Object value = null;
			
//			List<HashMap<String, Object>> modelList = new LinkedList<HashMap<String, Object>>();
			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 3; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					break;
				}
				if(row.getCell(7)!=null && row.getCell(7).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					String typeName = row.getCell(7).getStringCellValue();
//					String type = saleTargetDao.selectType(typeName);
					if(!typeName.equals("TV") && !typeName.equals("tv")){
						msg.append(getText("sale.error.row")+(i+1)+getText("sale.error.cell")+8
								+ getText("sale.error.tvtype")+"<br/>");
					}
				}else{
					msg.append(getText("sale.error.row")+(i+1)+getText("sale.error.cell")+8
							+ getText("sale.error.tvtype")+"<br/>");
				}
				String countryName="";
				String countryId ="";
				if(row.getCell(6)!=null  && row.getCell(6).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					 countryName = row.getCell(6).getStringCellValue();
					 countryId = saleTargetDao.selectCountry(countryName);
					if (countryId == null) {
						msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+(6+1)
								+ getText("sale.error.country") + "<br/>");
					}
					
				}else{
					msg.append(getText("sale.error.row") + (i + 1)
							+ getText("sale.error.countryname") + "<br/>");
				}
				String shopName="";
				Shop shop=null;
				String customerName = null;
				String customer = null;
				HashMap<String, Object> storeMap = new HashMap<String, Object>();
				List<Object> linked = new LinkedList<Object>();
				if(row.getCell(0)!=null  && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					
					if("4".equals(targetType)){//4识别为门店
						 shopName = row.getCell(0).getStringCellValue();
						 shop = shopDao.getShopByNameOrLocation(shopName);
						if (shop == null) {
							msg.append(getText("sale.error.row") + (i + 1)
									+ getText("sale.error.shop") +" ("+shopName+")"+"<br/>");
						}
					}else if("3".equals(targetType)){//3识别为客户（渠道）
						
						customerName = row.getCell(0).getStringCellValue();
						customer = importTargetDao.selectChannel(customerName);
						
						if (customer == null) {
							msg.append(getText("sale.error.row") + (i + 1)
									+ getText("sale.error.Channel") +" ("+customerName+")"+"<br/>");
						}
					}
					
				}else{
					msg.append(getText("sale.error.row") + (i + 1)
							+ getText("sale.error.shopname") + "<br/>");
				}
				
					SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					Date date = new Date();
					String dt = dfd.format(date);
					Date dt1 = dfd.parse(dt);
					Date dt2;
				
					
//					if(  	row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK
//							&& row.getCell(5).getCellType()!=HSSFCell.CELL_TYPE_BLANK
//							&& row.getCell(6).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						
						if(row.getCell(5)!=null  && row.getCell(5).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
							try{
								format.setLenient(false);
								date = format.parse(row.getCell(5).getStringCellValue());//有异常要捕获
								dfd.setLenient(false);
								String newD = dfd.format(date);
								date = dfd.parse(newD);//有异常要捕获
									dt2 =dfd.parse(newD);
									if (dt1.getTime() < dt2.getTime()) {
										msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+(5+1)
												+ getText("sale.error.time") + "<br/>");
									} 
							}catch(Exception e){
								msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+(5+1)
										+ getText("sale.error.date") + "<br/>");
							}
							
								
							
					
						}else{
							msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+(5+1)
									+ getText("sale.error.dateNo") + "<br/>");
						}
							
						
//					}
//					storeMap.put("Store", shop.getShopId() == null ? null
//							: shop.getShopId());
//					for (int m = 0; m < modelList.size(); m++) {
//					HashMap<String, Object> modelMap = new HashMap<String, Object>();
//					storeMap.put("Store", shop.getShopName() == null ? null
//							: shop.getShopName());
//					storeMap.put("Store", shop == null ? null
//							: shop);
					if(row.getCell(0)!=null  && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						
						if("4".equals(targetType)){//4识别为门店
							shopName = row.getCell(0).getStringCellValue();
							shop = shopDao.getShopByNameOrLocation(shopName);
							if (shop != null) {
								storeMap.put("Store", shop.getShopId() == null ? null
										: shop.getShopId());
								storeMap.put("StoreName", shopName);
							}
						}else if("3".equals(targetType)){//3识别为客户（渠道）
							
							customerName = row.getCell(0).getStringCellValue();
							customer = importTargetDao.selectChannel(customerName);
							storeMap.put("Store", customer);
							storeMap.put("StoreName", customerName);
						}
						
						
						
					}
//					storeMap.put("StoreName", shopName);
//					if(row.getCell(1)!=null){
					XSSFCell cell1 = row.getCell(1);	//把excel中的单元格数据转化为String类型
					cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
					storeMap.put("quantity", cell1.getStringCellValue());
//					storeMap.put("quantity", (int) row.getCell( 1)
//							.getNumericCellValue());
//					}
					if(row.getCell(2)!=null){
						XSSFCell cell2 = row.getCell(2);	//把excel中的单元格数据转化为String类型
						cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
					storeMap.put("amount", cell2.getStringCellValue());
					}
					
					XSSFCell cell3 = row.getCell(3);	//把excel中的单元格数据转化为String类型
					cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
					storeMap.put("tz_quantity", WebPageUtil.isStringNullAvaliable(cell3.getStringCellValue()) ? cell3.getStringCellValue() : "0");
//					if(row.getCell(3)!=null 
//							&& row.getCell(3)
//							.getNumericCellValue()!=0){
//					storeMap.put("tz_quantity", (int) row.getCell(3)
//							.getNumericCellValue());
//					}
//					if(row.getCell(4)!=null 
//							&& row.getCell(4)
//							.getNumericCellValue()!=0){
						XSSFCell cell4 = row.getCell(4);
						cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
					storeMap.put("tz_Amount", WebPageUtil.isStringNullAvaliable(cell4.getStringCellValue()) ? cell4.getStringCellValue() : "0");
//					}
					
					if(row.getCell(5)!=null && row.getCell(5).getCellType()!=HSSFCell.CELL_TYPE_BLANK ){
						try{
							format.setLenient(false);
							date = format.parse(row.getCell(5).getStringCellValue());//有异常要捕获
							dfd.setLenient(false);
							String newD = dfd.format(date);
							date = dfd.parse(newD);//有异常要捕获
								dt2 =dfd.parse(newD);

								if (dt1.getTime() < dt2.getTime()) {
									
								} else {
									storeMap.put("datadate", newD);
								}
						}catch(Exception e){
							
						}
						
				
					}
					if(row.getCell(6)!=null  && row.getCell(6).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						countryName = row.getCell(6).getStringCellValue();
						countryId = saleTargetDao.selectCountry(countryName);
						if (countryId != null) {
							storeMap.put("Country", countryId == null ? null
									: countryId);
							storeMap.put("CountryName", countryName);
						}
					}
					if(row.getCell(7) != null  && row.getCell(7).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						String typeName = row.getCell(7).getStringCellValue();
						String type = saleTargetDao.selectType(typeName);
						if(type!=null){
							storeMap.put("type", type==null?null:type);
//							storeMap.put("typeName", typeName);
						}
					}
					allModelList.add(storeMap);
					
//					}
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
			for (int i = 0; i < allModelList.size(); i++) {
				SaleTarget excel = new SaleTarget();
				String datadate="";
				String countryName="";
				String type="";
				if(allModelList.get(i).get("type")!=null){
				type =	allModelList.get(i).get("type").toString();
				excel.setType(Integer.parseInt(allModelList.get(i).get("type").toString()));
				}
				if(allModelList.get(i).get("CountryName")!=null){
				countryName = allModelList.get(i).get("CountryName")
								.toString();
				}
				if(allModelList.get(i).get("datadate")!=null){
					datadate = allModelList.get(i).get("datadate")
							.toString();
					excel.setDatadate(datadate);
				}
				String shopName="";
				if(allModelList.get(i).get("StoreName")!=null){
					 shopName = allModelList.get(i).get("StoreName")
								.toString();
				}
//				allModelList.get(i).get("CountryName")
//				.toString();	
		
				if(allModelList.get(i).get("Store")!=null){
				excel.setTargetId((allModelList.get(i)
						.get("Store").toString()));
				}
				if(allModelList.get(i).get("Country")!=null){
					excel.setCountryId(allModelList.get(i).get("Country")
							.toString());
				}
				excel.setClassId(targetType);
				
				int row = 0;
				if(msg.length()<=0){
					
					if (allModelList.get(i).get("quantity") != null							
							&& allModelList.get(i)
									.get("amount").toString() != null
							) {
						BigDecimal bqt = new BigDecimal(allModelList.get(i)
								.get("quantity").toString());
						excel.setQuantity(Long.parseLong(bqt.setScale(0,BigDecimal.ROUND_HALF_UP).toString()));

						BigDecimal bdAmount = new BigDecimal(allModelList.get(i)
								.get("amount").toString());
						String sAmount = bdAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(); //四舍五入，保留两位小数					
						excel.setAmount(sAmount);
						
						BigDecimal tzqt = new BigDecimal(allModelList.get(i)
								.get("tz_quantity").toString());
						excel.setTzQuantity(Long.parseLong(tzqt.setScale(0,BigDecimal.ROUND_HALF_UP).toString()));
													
						
						
						BigDecimal bdTzAmount = new BigDecimal(allModelList.get(i)
								.get("tz_Amount").toString());
						String stzAmount = bdTzAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
						excel.setTzAmount(stzAmount);
						excel.sethQuantity(Integer.parseInt(bqt.setScale(0,BigDecimal.ROUND_HALF_UP).toString()));
						excel.sethTzQuantity(Integer.parseInt(tzqt.setScale(0,BigDecimal.ROUND_HALF_UP).toString()));
						
						BigDecimal bd = new BigDecimal(allModelList.get(i)
								.get("amount").toString());
						java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
						nf.setGroupingUsed(false);
						double amount=Double.parseDouble(nf.format(Double.parseDouble(allModelList.get(i)
								.get("amount").toString())));
						double tzAmount=Double.parseDouble(nf.format(Double.parseDouble(allModelList.get(i)
								.get("tz_Amount").toString())));
						
						System.out.println(amount+"======"+tzAmount);
//						XSSFRow roc = sheet.getRow(3);
						if(allModelList.get(i).get("Country")!=null){
							String country=allModelList.get(i).get("Country").toString();
//							String datedate = allModelList.get(i).get("datadate").toString();
//							String	countryId = saleTargetDao.selectCountry(country);
							Float exchange = saleTargetDao.selectExchange(country);
//							System.out.println(exchange+"=============================="+datedate);
							if (exchange != null) {
							bd=new BigDecimal(amount*exchange);
							double Hamount=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							excel.sethAmount(Hamount);
							bd=new BigDecimal(tzAmount*exchange);
							double HtzAmount=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							excel.sethTzAmount(HtzAmount);
							}else{
								msg.append(countryName+"  "+getText("sale.error.currency") );
							}
						}
					
					}
					
//						 row = saleTargetDao.saveSales(excel);
					if(allModelList.get(i).get("StoreName")!=null && allModelList.get(i).get("datadate")!=null 
							&& allModelList.get(i).get("Country")!=null){
						//int r = saleTargetDao.selectCount(shopName,datadate,type);
						String id =  allModelList.get(i).get("Store") +"";
						int r = saleTargetDao.selectCount(id,datadate,type,targetType,excel.getCountryId());
						if(r==0){
							// 执行插入
							row = saleTargetDao.saveSales(excel);
							if (row == 0) {
								msg.append(shopName + ":"
										+ getText("store.error.saleInsert") + "<br/>");

							}
						}
						else{
							saleTargetDao.updateTarget(excel);
						}
					}
				}
				
				
				
				
			}

			System.out.println(allModelList);
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
	public List<SaleTarget> selectSaleTargets(String datadate,String partyId) throws Exception {
		return saleTargetDao.selectSaleTargets(datadate,partyId);
	}

	@Override
	public List<SaleTarget> selectChannelTargets(String datadate,String partyId) throws Exception {
		
		return saleTargetDao.selectChannelTargets(datadate,partyId);
	}

	@Override
	public List<SaleTarget> selectOfficeTargets(String datadate,String partyId) throws Exception {
		
		return saleTargetDao.selectOfficeTargets(datadate,partyId);
	}

	@Override
	public List<SaleTarget> selectRegionTargets(String datadate,String partyId) throws Exception {
		
		return saleTargetDao.selectRegionTargets(datadate,partyId);
	}

	@Override
	public List<SaleTarget> selectBranchTargets(String conditions,String datadate,String partyId) throws Exception {
		return saleTargetDao.selectBranchTargets(conditions,datadate,partyId);
	}

//	@Override
//	public List<SaleTarget> selectPromoterTargets() throws Exception {
//		return saleTargetDao.selectPromoterTargets();
//	}

	@Override
	public List<SaleTarget> selectSupervisorTargets(String datadate,String partyId) throws Exception {
		return saleTargetDao.selectSupervisorTargets(datadate,partyId);
	}

	@Override
	public List<SaleTarget> selectSalesmanTargets(String datadate,String partyId) throws Exception {
		return saleTargetDao.selectSalesmanTargets(datadate,partyId);
	}

	@Override
	public List<SaleTarget> selectBusinessTargets(String datadate,String partyId) throws Exception {
		return saleTargetDao.selectBusinessTargets(datadate,partyId);
	}
	@Override
	public List<SaleTarget> selectACSaleTargets(String datadate, String partyId)
			throws Exception {		
		return saleTargetDao.selectACSaleTargets(datadate, partyId);
	}
	@Override
	public List<SaleTarget> selectACChannelTargets(String datadate,
			String partyId) throws Exception {
		return saleTargetDao.selectACChannelTargets(datadate, partyId);
	}
	@Override
	public List<SaleTarget> selectACOfficeTargets(String datadate,
			String partyId) throws Exception {
		return saleTargetDao.selectACOfficeTargets(datadate, partyId);
	}
	@Override
	public List<SaleTarget> selectACRegionTargets(String datadate,
			String partyId) throws Exception {
		return saleTargetDao.selectACRegionTargets(datadate, partyId);
	}
	@Override
	public List<SaleTarget> selectACBranchTargets(String conditions,
			String datadate, String partyId) throws Exception {
		return saleTargetDao.selectACBranchTargets(conditions, datadate, partyId);
	}
	@Override
	public List<SaleTarget> selectACSupervisorTargets(String datadate,
			String partyId) throws Exception {
		return saleTargetDao.selectACSupervisorTargets(datadate, partyId);
	}
	@Override
	public List<SaleTarget> selectACSalesmanTargets(String datadate,
			String partyId) throws Exception {
		return saleTargetDao.selectACSalesmanTargets(datadate, partyId);
	}
	@Override
	public List<SaleTarget> selectACBusinessTargets(String datadate,
			String partyId) throws Exception {
		return saleTargetDao.selectACBusinessTargets(datadate, partyId);
	}
	@Override
	public String readACExcel(File file, String fileName, String targetType) throws IOException {
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			//return read2003ACExcel(file, targetType);
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		} else if ("xlsx".equals(extension)) {
			return read2007ACExcel(file, targetType);
		} else {
			//throw new IOException("不支持的文件类型");
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		}
	}
	
	@Override
	public String read2007ACExcel(File file,String targetType) throws IOException{
		StringBuffer msg = new StringBuffer();
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			Object value = null;
			
			/**
			 * 模板检测
			 */
			msg.append(templateError(sheet));
			
			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 3; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					break;
				}
				if(row.getCell(7)!=null && row.getCell(7).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					String typeName = row.getCell(7).getStringCellValue();
					if(!typeName.equals("AC") && !typeName.equals("ac")){
						msg.append(getText("sale.error.row")+(i+1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel(8)
								+ getText("sale.error.actype")+"<br/>");
					}
				}else{
					msg.append(getText("sale.error.row")+(i+1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel(8)
							+ getText("sale.error.actype")+"<br/>");
				}
				String countryName="";
				String countryId ="";
				if(row.getCell(6)!=null  && row.getCell(6).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					 countryName = row.getCell(6).getStringCellValue();
					 countryId = saleTargetDao.selectCountry(countryName);
					if (countryId == null) {
						msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((6+1))
								+ getText("sale.error.country") + "<br/>");
					}else if(!countryId.equals(WebPageUtil.getLoginedUser().getPartyId()) && !WebPageUtil.isHQRole()){
						msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((6+1))
								+ getText("imports.error.otherCountry") + "<br/>");
					}
					
				}else{
					msg.append(getText("sale.error.row") + (i + 1)
							+ getText("sale.error.countryname") + "<br/>");
				}
				String shopName="";
				Shop shop=null;
				String customerName = null;
				String customer = null;
				HashMap<String, Object> storeMap = new HashMap<String, Object>();
				List<Object> linked = new LinkedList<Object>();
				if(row.getCell(0)!=null  && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
				
					if("4".equals(targetType)){//4识别为门店
						 shopName = row.getCell(0).getStringCellValue();
						 shop = shopDao.getShopByNameOrLocation(shopName);
						if (shop == null) {
							msg.append(getText("sale.error.row") + (i + 1)
									+ getText("sale.error.shop") +" ("+shopName+")"+"<br/>");
						}
					}else if("3".equals(targetType)){//3识别为客户（渠道）
						
						customerName = row.getCell(0).getStringCellValue();
						customer = importTargetDao.selectChannel(customerName);
						
						if (customer == null) {
							msg.append(getText("sale.error.row") + (i + 1)
									+ getText("sale.error.Channel") +" ("+customerName+")"+"<br/>");
						}
					}else if("1".equals(targetType)){
						String cty = row.getCell(0).getStringCellValue();
						String ctyId = saleTargetDao.selectCountry(cty);
						if (ctyId == null) {
							msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((0+1))
									+ getText("sale.error.country") + "<br/>");
						}
					}
				}else{
					/*msg.append(getText("sale.error.row") + (i + 1)
							+ getText("sale.error.shopname") + "<br/>");*/
					if("4".equals(targetType)){//4识别为门店
						msg.append(getText("sale.error.row") + (i + 1) +getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((0+1))
								+ getText("sale.error.shopname") + "<br/>");
					}else if("3".equals(targetType)){
						msg.append(getText("sale.error.row") + (i + 1) +getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((0+1))
								+ getText("sale.error.customerName") + "<br/>");
					}else if("1".equals(targetType)){
						msg.append(getText("sale.error.row") + (i + 1) +getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((0+1))
								+ getText("sale.error.countryname") + "<br/>");
					}
				}
				
					SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					Date date = new Date();
					String dt = dfd.format(date);
					Date dt1 = dfd.parse(dt);
					Date dt2;
				
						if(row.getCell(5)!=null  && row.getCell(5).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
							try{
								format.setLenient(false);
								date = format.parse(row.getCell(5).getStringCellValue());//有异常要捕获
								dfd.setLenient(false);
								String newD = dfd.format(date);
								date = dfd.parse(newD);//有异常要捕获
									dt2 =dfd.parse(newD);
									if (dt1.getTime() < dt2.getTime()) {
										msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((5+1))
												+ getText("sale.error.time") + "<br/>");
									} 
							}catch(Exception e){
								msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((5+1))
										+ getText("sale.error.date") + "<br/>");
							}
							
								
							
					
						}else{
							msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((5+1))
									+ getText("sale.error.dateNo") + "<br/>");
						}
							
					if(row.getCell(0)!=null  && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK){

						if("4".equals(targetType)){//4识别为门店
							shopName = row.getCell(0).getStringCellValue();
							shop = shopDao.getShopByNameOrLocation(shopName);
							if (shop != null) {
								storeMap.put("Store", shop.getShopId() == null ? null
										: shop.getShopId());
								storeMap.put("StoreName", shopName);
							}
						}else if("3".equals(targetType)){//3识别为客户（渠道）
							
							customerName = row.getCell(0).getStringCellValue();
							customer = importTargetDao.selectChannel(customerName);
							storeMap.put("Store", customer);
							storeMap.put("StoreName", customerName);
						}else if("1".equals(targetType)){
							
							customerName = row.getCell(0).getStringCellValue();
							customer = importTargetDao.selectChannel(customerName);
							storeMap.put("Store", countryId);
							storeMap.put("StoreName", countryName);
						}
						
					}
					
					
					/**
					 * 数量销售目标(必填)
					 */
					if(row.getCell(1)!=null  && row.getCell(1).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						XSSFCell cell1 = row.getCell(1);	//把excel中的单元格数据转化为String类型
						cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
						storeMap.put("quantity", cell1.getStringCellValue());
					}else{
						msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((1+1))
								+ getText("sale.qty.target.notnull") + "<br/>");
					}
					
					/**
					 * 金额销售目标(必填)
					 */
					if(row.getCell(2)!=null && row.getCell(2).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						XSSFCell cell2 = row.getCell(2);	//把excel中的单元格数据转化为String类型
						cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
						storeMap.put("amount", cell2.getStringCellValue());
					}else{
						msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+ DateUtil.getExcelColumnLabel((2+1))
								+ getText("sale.amt.target.notnull") + "<br/>");
					}
					
					/**
					 * 挑战数量可填可不填(默认为0)
					 */
					if(row.getCell(3)!=null && row.getCell(3).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						XSSFCell cell3 = row.getCell(3);	
						cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
						storeMap.put("tz_quantity", cell3.getStringCellValue());
					}else{
						storeMap.put("tz_quantity", "0");
					}
					
						
					/**
					 * 挑战金额可填可不填(默认为0)
					 */
					if(row.getCell(4)!=null && row.getCell(4).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						XSSFCell cell4 = row.getCell(4);
						cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
						storeMap.put("tz_Amount", cell4.getStringCellValue());
					}else{
						storeMap.put("tz_Amount", "0");
					}
					
					if(row.getCell(5)!=null && row.getCell(5).getCellType()!=HSSFCell.CELL_TYPE_BLANK ){
						try{
							format.setLenient(false);
							date = format.parse(row.getCell(5).getStringCellValue());//有异常要捕获
							dfd.setLenient(false);
							String newD = dfd.format(date);
							date = dfd.parse(newD);//有异常要捕获
								dt2 =dfd.parse(newD);

								if (dt1.getTime() < dt2.getTime()) {
									
								} else {
									storeMap.put("datadate", newD);
								}
						}catch(Exception e){
							
						}
						
				
					}
					if(row.getCell(6)!=null  && row.getCell(6).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						countryName = row.getCell(6).getStringCellValue();
						countryId = saleTargetDao.selectCountry(countryName);
						if (countryId != null) {
							storeMap.put("Country", countryId == null ? null
									: countryId);
							storeMap.put("CountryName", countryName);
						}
					}
					if(row.getCell(7) != null  && row.getCell(7).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						String typeName = row.getCell(7).getStringCellValue();
						String type = saleTargetDao.selectType(typeName);
						if(type!=null){
							storeMap.put("type", type==null?null:type);
						}
					}
					allModelList.add(storeMap);
					
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
			for (int i = 0; i < allModelList.size(); i++) {
				SaleTarget excel = new SaleTarget();
				String datadate="";
				String countryName="";
				String type="";
				if(allModelList.get(i).get("type")!=null){
				type =	allModelList.get(i).get("type").toString();
				excel.setType(Integer.parseInt(allModelList.get(i).get("type").toString()));
				}
				if(allModelList.get(i).get("CountryName")!=null){
				countryName = allModelList.get(i).get("CountryName")
								.toString();
				}
				if(allModelList.get(i).get("datadate")!=null){
					datadate = allModelList.get(i).get("datadate")
							.toString();
					excel.setDatadate(datadate);
				}
				String shopName="";
				if(allModelList.get(i).get("StoreName")!=null){
					 shopName = allModelList.get(i).get("StoreName")
								.toString();
				}
		
				if(allModelList.get(i).get("Store")!=null){
				excel.setTargetId((allModelList.get(i)
						.get("Store").toString()));
				}
				if(allModelList.get(i).get("Country")!=null){
					excel.setCountryId(allModelList.get(i).get("Country")
							.toString());
				}
				excel.setClassId(targetType);
				
				int row = 0;
				if(msg.length()<=0){
					
					if (allModelList.get(i).get("quantity") != null							
							&& allModelList.get(i)
									.get("amount").toString() != null
							) {
						BigDecimal bqt = new BigDecimal(allModelList.get(i)
								.get("quantity").toString());
						excel.setQuantity(Long.parseLong(bqt.setScale(0,BigDecimal.ROUND_HALF_UP).toString()));

						BigDecimal bdAmount = new BigDecimal(allModelList.get(i)
								.get("amount").toString());
						String sAmount = bdAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(); //四舍五入，保留两位小数					
						excel.setAmount(sAmount);
						
						BigDecimal tzqt = new BigDecimal(allModelList.get(i)
								.get("tz_quantity").toString());
						excel.setTzQuantity(Long.parseLong(tzqt.setScale(0,BigDecimal.ROUND_HALF_UP).toString()));
													
						
						
						BigDecimal bdTzAmount = new BigDecimal(allModelList.get(i)
								.get("tz_Amount").toString());
						String stzAmount = bdTzAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
						excel.setTzAmount(stzAmount);
						excel.sethQuantity(Integer.parseInt(bqt.setScale(0,BigDecimal.ROUND_HALF_UP).toString()));
						excel.sethTzQuantity(Integer.parseInt(tzqt.setScale(0,BigDecimal.ROUND_HALF_UP).toString()));
						
						BigDecimal bd = new BigDecimal(allModelList.get(i)
								.get("amount").toString());
						java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
						nf.setGroupingUsed(false);
						double amount=Double.parseDouble(nf.format(Double.parseDouble(allModelList.get(i)
								.get("amount").toString())));
						double tzAmount=Double.parseDouble(nf.format(Double.parseDouble(allModelList.get(i)
								.get("tz_Amount").toString())));
						
						System.out.println(amount+"======"+tzAmount);
						if(allModelList.get(i).get("Country")!=null){
							String country=allModelList.get(i).get("Country").toString();
							Float exchange = saleTargetDao.selectExchange(country);
							if (exchange != null) {
							bd=new BigDecimal(amount*exchange);
							double Hamount=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							excel.sethAmount(Hamount);
							bd=new BigDecimal(tzAmount*exchange);
							double HtzAmount=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							excel.sethTzAmount(HtzAmount);
							}else{
								msg.append(countryName+"  "+getText("sale.error.currency") );
							}
						}
					
					}
					excel.setClas(targetType);
					if(allModelList.get(i).get("StoreName")!=null && allModelList.get(i).get("datadate")!=null 
							&& allModelList.get(i).get("Country")!=null && "".equals(msg.toString())){
						//int r = saleTargetDao.selectCount(shopName,datadate,type);
						String id =  allModelList.get(i).get("Store") +"";
						int r = saleTargetDao.selectCount(id,datadate,type,targetType,excel.getCountryId());
						if(r==0){
							// 执行插入
							row = saleTargetDao.saveSales(excel);
							if (row == 0) {
								msg.append(shopName + ":"
										+ getText("store.error.saleInsert") + "<br/>");

							}
						}
						else{
							saleTargetDao.updateTarget(excel);
						}
					}
				}
				
				
				
				
			}

			System.out.println(allModelList);
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
	public String read2003ACExcel(File file,String targetType) throws IOException {
		StringBuffer msg = new StringBuffer();
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			HSSFWorkbook workbook = new HSSFWorkbook();
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			Object value = null;
			
//			XSSFRow ro = sheet.getRow(0);
//			XSSFRow roc = sheet.getRow(3);
//			XSSFCell cells = null;
//			

//			List<HashMap<String, Object>> modelList = new LinkedList<HashMap<String, Object>>();
			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 3; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					break;
				}
				if(row.getCell(7)!=null && row.getCell(7).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					String typeName = row.getCell(7).getStringCellValue();
//					String type = saleTargetDao.selectType(typeName);
					if(!typeName.equals("AC") && !typeName.equals("ac")){
						msg.append(getText("sale.error.row")+(i+1)+getText("sale.error.cell")+8
								+ getText("sale.error.actype")+"<br/>");
					}
				}else{
					msg.append(getText("sale.error.row")+(i+1)+getText("sale.error.cell")+8
							+ getText("sale.error.tvtype")+"<br/>");
				}
				String countryName="";
				String countryId ="";
				if(row.getCell(6)!=null  && row.getCell(6).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
					 countryName = row.getCell(6).getStringCellValue();
					 countryId = saleTargetDao.selectCountry(countryName);
					if (countryId == null) {
						msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+(6+1)
								+ getText("sale.error.country") + "<br/>");
					}
					
				}else{
					msg.append(getText("sale.error.row") + (i + 1)
							+ getText("sale.error.countryname") + "<br/>");
				}
				String shopName="";
				Shop shop=null;
				String customerName="";
				String customer="";
				HashMap<String, Object> storeMap = new HashMap<String, Object>();
				List<Object> linked = new LinkedList<Object>();
				if(row.getCell(0)!=null  && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
				
					if("4".equals(targetType)){//4识别为门店
						 shopName = row.getCell(0).getStringCellValue();
						 shop = shopDao.getShopByNameOrLocation(shopName);
						if (shop == null) {
							msg.append(getText("sale.error.row") + (i + 1)
									+ getText("sale.error.shop") +" ("+shopName+")"+"<br/>");
						}
					}else if("3".equals(targetType)){//3识别为客户（渠道）
						
						customerName = row.getCell(0).getStringCellValue();
						customer = importTargetDao.selectChannel(customerName);
						
						if (customer == null) {
							msg.append(getText("sale.error.row") + (i + 1)
									+ getText("sale.error.Channel") +" ("+customerName+")"+"<br/>");
						}
					}
				}else{
					msg.append(getText("sale.error.row") + (i + 1)
							+ getText("sale.error.shopname") + "<br/>");
				}
				
					SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					Date date = new Date();
					String dt = dfd.format(date);
					Date dt1 = dfd.parse(dt);
					Date dt2;
				
					
//					if(  	row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK
//							&& row.getCell(5).getCellType()!=HSSFCell.CELL_TYPE_BLANK
//							&& row.getCell(6).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						
						if(row.getCell(5)!=null  && row.getCell(5).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
							try{
								format.setLenient(false);
								date = format.parse(row.getCell(5).getStringCellValue());//有异常要捕获
								dfd.setLenient(false);
								String newD = dfd.format(date);
								date = dfd.parse(newD);//有异常要捕获
									dt2 =dfd.parse(newD);
									if (dt1.getTime() < dt2.getTime()) {
										msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+(5+1)
												+ getText("sale.error.time") + "<br/>");
									} 
							}catch(Exception e){
								msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+(5+1)
										+ getText("sale.error.date") + "<br/>");
							}
							
								
							
					
						}else{
							msg.append(getText("sale.error.row") + (i + 1)+getText("sale.error.cell")+(5+1)
									+ getText("sale.error.dateNo") + "<br/>");
						}
							
						
//					}
//					storeMap.put("Store", shop.getShopId() == null ? null
//							: shop.getShopId());
//					for (int m = 0; m < modelList.size(); m++) {
//					HashMap<String, Object> modelMap = new HashMap<String, Object>();
//					storeMap.put("Store", shop.getShopName() == null ? null
//							: shop.getShopName());
//					storeMap.put("Store", shop == null ? null
//							: shop);
					if(row.getCell(0)!=null  && row.getCell(0).getCellType()!=HSSFCell.CELL_TYPE_BLANK){

						if("4".equals(targetType)){//4识别为门店
							shopName = row.getCell(0).getStringCellValue();
							shop = shopDao.getShopByNameOrLocation(shopName);
							if (shop != null) {
								storeMap.put("Store", shop.getShopId() == null ? null
										: shop.getShopId());
								storeMap.put("StoreName", shopName);
							}
						}else if("3".equals(targetType)){//3识别为客户（渠道）
							
							customerName = row.getCell(0).getStringCellValue();
							customer = importTargetDao.selectChannel(customerName);
							storeMap.put("Store", customer);
							storeMap.put("StoreName", customerName);
						}
					}
//					storeMap.put("StoreName", shopName);
//					if(row.getCell(1)!=null){
					XSSFCell cell1 = row.getCell(1);	//把excel中的单元格数据转化为String类型
					cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
					storeMap.put("quantity", cell1.getStringCellValue());
//					storeMap.put("quantity", (int) row.getCell( 1)
//							.getNumericCellValue());
//					}
					if(row.getCell(2)!=null){
						XSSFCell cell2 = row.getCell(2);	//把excel中的单元格数据转化为String类型
						cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
					storeMap.put("amount", cell2.getStringCellValue());
					}
					
					XSSFCell cell3 = row.getCell(3);	//把excel中的单元格数据转化为String类型
					cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
					storeMap.put("tz_quantity", cell3.getStringCellValue());
//					if(row.getCell(3)!=null 
//							&& row.getCell(3)
//							.getNumericCellValue()!=0){
//					storeMap.put("tz_quantity", (int) row.getCell(3)
//							.getNumericCellValue());
//					}
//					if(row.getCell(4)!=null 
//							&& row.getCell(4)
//							.getNumericCellValue()!=0){
						XSSFCell cell4 = row.getCell(4);
						cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
					storeMap.put("tz_Amount", cell4.getStringCellValue());
//					}
					
					if(row.getCell(5)!=null && row.getCell(5).getCellType()!=HSSFCell.CELL_TYPE_BLANK ){
						try{
							format.setLenient(false);
							date = format.parse(row.getCell(5).getStringCellValue());//有异常要捕获
							dfd.setLenient(false);
							String newD = dfd.format(date);
							date = dfd.parse(newD);//有异常要捕获
								dt2 =dfd.parse(newD);

								if (dt1.getTime() < dt2.getTime()) {
									
								} else {
									storeMap.put("datadate", newD);
								}
						}catch(Exception e){
							
						}
						
				
					}
					if(row.getCell(6)!=null  && row.getCell(6).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						countryName = row.getCell(6).getStringCellValue();
						countryId = saleTargetDao.selectCountry(countryName);
						if (countryId != null) {
							storeMap.put("Country", countryId == null ? null
									: countryId);
							storeMap.put("CountryName", countryName);
						}
					}
					if(row.getCell(7) != null  && row.getCell(7).getCellType()!=HSSFCell.CELL_TYPE_BLANK){
						String typeName = row.getCell(7).getStringCellValue();
						String type = saleTargetDao.selectType(typeName);
						if(type!=null){
							storeMap.put("type", type==null?null:type);
//							storeMap.put("typeName", typeName);
						}
					}
					allModelList.add(storeMap);
					
//					}
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
			for (int i = 0; i < allModelList.size(); i++) {
				SaleTarget excel = new SaleTarget();
				String datadate="";
				String countryName="";
				String type="";
				if(allModelList.get(i).get("type")!=null){
				type =	allModelList.get(i).get("type").toString();
				excel.setType(Integer.parseInt(allModelList.get(i).get("type").toString()));
				}
				if(allModelList.get(i).get("CountryName")!=null){
				countryName = allModelList.get(i).get("CountryName")
								.toString();
				}
				if(allModelList.get(i).get("datadate")!=null){
					datadate = allModelList.get(i).get("datadate")
							.toString();
					excel.setDatadate(datadate);
				}
				String shopName="";
				if(allModelList.get(i).get("StoreName")!=null){
					 shopName = allModelList.get(i).get("StoreName")
								.toString();
				}
//				allModelList.get(i).get("CountryName")
//				.toString();	
		
				if(allModelList.get(i).get("Store")!=null){
				excel.setTargetId((allModelList.get(i)
						.get("Store").toString()));
				}
				if(allModelList.get(i).get("Country")!=null){
					excel.setCountryId(allModelList.get(i).get("Country")
							.toString());
				}
				excel.setClassId(targetType);
				
				int row = 0;
				if(msg.length()<=0){
					
					if (allModelList.get(i).get("quantity") != null							
							&& allModelList.get(i)
									.get("amount").toString() != null
							) {
						BigDecimal bqt = new BigDecimal(allModelList.get(i)
								.get("quantity").toString());
						excel.setQuantity(Long.parseLong(bqt.setScale(0,BigDecimal.ROUND_HALF_UP).toString()));

						BigDecimal bdAmount = new BigDecimal(allModelList.get(i)
								.get("amount").toString());
						String sAmount = bdAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(); //四舍五入，保留两位小数					
						excel.setAmount(sAmount);
						
						BigDecimal tzqt = new BigDecimal(allModelList.get(i)
								.get("tz_quantity").toString());
						excel.setTzQuantity(Long.parseLong(tzqt.setScale(0,BigDecimal.ROUND_HALF_UP).toString()));
													
						
						
						BigDecimal bdTzAmount = new BigDecimal(allModelList.get(i)
								.get("tz_Amount").toString());
						String stzAmount = bdTzAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
						excel.setTzAmount(stzAmount);
						excel.sethQuantity(Integer.parseInt(bqt.setScale(0,BigDecimal.ROUND_HALF_UP).toString()));
						excel.sethTzQuantity(Integer.parseInt(tzqt.setScale(0,BigDecimal.ROUND_HALF_UP).toString()));
						
						BigDecimal bd = new BigDecimal(allModelList.get(i)
								.get("amount").toString());
						java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
						nf.setGroupingUsed(false);
						double amount=Double.parseDouble(nf.format(Double.parseDouble(allModelList.get(i)
								.get("amount").toString())));
						double tzAmount=Double.parseDouble(nf.format(Double.parseDouble(allModelList.get(i)
								.get("tz_Amount").toString())));
						
						System.out.println(amount+"======"+tzAmount);
//						XSSFRow roc = sheet.getRow(3);
						if(allModelList.get(i).get("Country")!=null){
							String country=allModelList.get(i).get("Country").toString();
//							String datedate = allModelList.get(i).get("datadate").toString();
//							String	countryId = saleTargetDao.selectCountry(country);
							Float exchange = saleTargetDao.selectExchange(country);
//							System.out.println(exchange+"=============================="+datedate);
							if (exchange != null) {
							bd=new BigDecimal(amount*exchange);
							double Hamount=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							excel.sethAmount(Hamount);
							bd=new BigDecimal(tzAmount*exchange);
							double HtzAmount=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							excel.sethTzAmount(HtzAmount);
							}else{
								msg.append(countryName+"  "+getText("sale.error.currency") );
							}
						}
					
					}
					
//						 row = saleTargetDao.saveSales(excel);
					if(allModelList.get(i).get("StoreName")!=null && allModelList.get(i).get("datadate")!=null 
							&& allModelList.get(i).get("Country")!=null){
						//int r = saleTargetDao.selectCount(shopName,datadate,type);
						String id =  allModelList.get(i).get("Store") +"";
						int r = saleTargetDao.selectCount(id,datadate,type,targetType,excel.getCountryId());
						if(r==0){
							// 执行插入
							row = saleTargetDao.saveSales(excel);
							if (row == 0) {
								msg.append(shopName + ":"
										+ getText("store.error.saleInsert") + "<br/>");

							}
						}
						else{
							saleTargetDao.updateTarget(excel);
						}
					}
				}
				
				
				
				
			}

			System.out.println(allModelList);
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
	 * 文件信息为空，或者乱输入
	 */
	public String templateError (XSSFSheet sheet) {
		if(sheet == null){
			return "";
		};
		
		if(sheet.getLastRowNum() < 2){
			return getText("imports.template.error")+"<br/>";
		};
		
		if(sheet.getLastRowNum() == 2){
			return getText("imports.data.null")+"<br/>";
		};
		
		return "";
	};
	
	
	@Override
	public String selectSOType(String countryId) throws Exception {
		return saleTargetDao.selectSOType(countryId);
	}
	@Override
	public List<SaleTarget> getChannelTarget(String datadate, String partyId)
			throws Exception {
		return saleTargetDao.getChannelTarget(datadate, partyId);
	}
	@Override
	public List<SaleTarget> getBranchTarget(String datadate, String partyId)
			throws Exception {
		return saleTargetDao.getBranchTarget(datadate, partyId);
	}
	@Override
	public List<SaleTarget> getSupervisorTarget(String datadate, String partyId)
			throws Exception {
		return saleTargetDao.getSupervisorTarget(datadate, partyId);
	}
	@Override
	public List<SaleTarget> getSalemanTarget(String datadate, String partyId)
			throws Exception {
		return saleTargetDao.getSalemanTarget(datadate, partyId);
	}
	@Override
	public List<SaleTarget> getBusinessTarget(String datadate, String partyId)
			throws Exception {
		return saleTargetDao.getBusinessTarget(datadate, partyId);
	}
	@Override
	public List<SaleTarget> getBranchTargetList(String datadate, String partyId)
			throws Exception {
		return saleTargetDao.getBranchTargetList(datadate, partyId);
	}
	@Override
	public List<SaleTarget> getACChannelTarget(String datadate, String partyId)
			throws Exception {
		return saleTargetDao.getACChannelTarget(datadate, partyId);
	}
	@Override
	public List<SaleTarget> getACBranchTarget(String datadate, String partyId)
			throws Exception {
		return saleTargetDao.getACBranchTarget(datadate, partyId);
	}
	@Override
	public List<SaleTarget> getACSupervisorTarget(String datadate,
			String partyId) throws Exception {
		return saleTargetDao.getACSupervisorTarget(datadate, partyId);
	}
	@Override
	public List<SaleTarget> getACSalemanTarget(String datadate, String partyId)
			throws Exception {
		return saleTargetDao.getACSalemanTarget(datadate, partyId);
	}
	@Override
	public List<SaleTarget> getACBussinessTarget(String datadate, String partyId)
			throws Exception {
		return saleTargetDao.getACBussinessTarget(datadate, partyId);
	}
	@Override
	public List<SaleTarget> getACBranchTagetList(String datadate, String partyId)
			throws Exception {
		return saleTargetDao.getACBranchTagetList(datadate, partyId);
	}
	@Override
	public List<SaleTarget> getOBCTVBranchTarget(String datadate, String partyId)
			throws Exception {
		return saleTargetDao.getOBCTVBranchTarget(datadate, partyId);
	}
	@Override
	public List<SaleTarget> getOBCACBranchTarget(String datadate, String partyId)
			throws Exception {
		return saleTargetDao.getOBCACBranchTarget(datadate, partyId);
	}
	@Override
	public Map<String, Object> selectCountrySalesData(int start, int limit, String searchStr, String order, String sort,
			String conditions) throws Exception {
		List<Sale> list=saleDao.selectCountrySales(start, limit, searchStr, order, sort,conditions);
		Map<String, Object> map=new HashMap<String, Object>();
		int count=saleDao.countCountrySales(start, limit, searchStr,conditions);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}
	@Override
	public Map<String, Object> selectCustomerSalesData(int start, int limit, String searchStr, String order,
			String sort, String conditions) throws Exception {
		List<Sale> list=saleDao.selectCustomerSales(start, limit, searchStr, order, sort,conditions);
		Map<String, Object> map=new HashMap<String, Object>();
		int count=saleDao.countCustomerSales(start, limit, searchStr,conditions);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}
}
