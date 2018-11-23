package cn.tcl.platform.target.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.customer.dao.ICustomerDao;
import cn.tcl.platform.customer.vo.Customer;
import cn.tcl.platform.excel.dao.ImportExcelDao;
import cn.tcl.platform.excel.service.ImportExcelService;
import cn.tcl.platform.excel.vo.Excel;
import cn.tcl.platform.excel.vo.ImportExcel;
import cn.tcl.platform.modelmap.dao.IModelMapDao;
import cn.tcl.platform.party.vo.Party;
import cn.tcl.platform.sale.vo.Sale;
import cn.tcl.platform.sale.vo.SaleTarget;
import cn.tcl.platform.shop.dao.IShopDao;
import cn.tcl.platform.shop.vo.Shop;
import cn.tcl.platform.target.dao.ImportTargetDao;
import cn.tcl.platform.target.service.ImportTargetService;
import cn.tcl.platform.target.vo.Target;
@Service("importTargetService")
public class ImportTargetServiceImpl extends BaseAction implements
		ImportTargetService {
	@Autowired
	private ImportExcelDao importExcelDao;
	@Autowired
	private ICustomerDao customerDao;
	@Autowired
	private IShopDao shopDao;
	@Autowired
	private IModelMapDao modelMapDao;
	
	@Autowired
	private ImportTargetDao importTargetDao;

	
	

	@Override
	public String readChannel(File file) throws IOException {
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
			
			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 3; i <= sheet.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				List<Object> linked = new LinkedList<Object>();
				List<SaleTarget> test = new LinkedList<SaleTarget>();
					HashMap<String, Object> modelMap = new HashMap<String, Object>();
					// 国家
					// 国家
					String channelName = row.getCell(0).getStringCellValue();
					String customerId = importTargetDao.selectChannel(channelName);
					
					if (customerId == null) {
						msg.append(getText("target.error.row") + (i + 1)
								+ getText("target.error.channle") +" ("+channelName+")"+"<br/>");
					}
					modelMap.put("Channel", customerId == null ? null
							: customerId);
					
					
					if(row.getCell(1)!=null 
							&& row.getCell(1)
							.getNumericCellValue()!=0){
						modelMap.put("Qty", (int) row.getCell(1)
								.getNumericCellValue());
					}
					
					if(row.getCell(2)!=null 
							&&  row.getCell(2)
							.getNumericCellValue()!=0){
						modelMap.put("Amt", row.getCell(2)
								.getNumericCellValue());
					}
					
					if( row.getCell(3)!=null 
							&&  row.getCell(3)
							.getNumericCellValue()!=0){
						modelMap.put("tz_qty", (int) row.getCell(3)
								.getNumericCellValue());
					}
					
					
					if(row.getCell(4)!=null  && row.getCell(4)
							.getNumericCellValue()!=0 ){
						modelMap.put("tz_amt", (int) row.getCell(4)
								.getNumericCellValue());
					}
					

					
					allModelList.add(modelMap);
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
				Target target = new Target();
				String channel = allModelList.get(i).get("Channel")
						.toString();
				target.setTargetId(channel);
				BigDecimal bd =null;
				int row = 0;
				if (allModelList.get(i).get("Qty") != null
						&& Integer.parseInt(allModelList.get(i).get("Qty")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("Qty").toString()); 
				
					target.setQuantity(bd.longValue());
					
				}
				if (allModelList.get(i).get("Amt") != null
						&& Double.parseDouble(allModelList.get(i).get("Amt")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("Amt").toString()); 
				
					target.setAmount(bd.toPlainString());
					
				}
				if (allModelList.get(i).get("tz_qty") != null
						&& Double.parseDouble(allModelList.get(i).get("tz_qty")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("tz_qty").toString()); 
				
					target.setTzQuantity(bd.longValue());
					
				}
				
				if (allModelList.get(i).get("tz_amt") != null
						&& Integer.parseInt(allModelList.get(i).get("tz_amt")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("tz_amt").toString()); 
				
					target.setTzAmount(bd.toPlainString());
					
				}
				target.setClassId(3);
				int  r=importTargetDao.selectCount("3",channel);
				if(r==0){
					row = importTargetDao.saveChannelTarget(target);
					if (row == 0) {
						msg.append(":"
								+ getText("target.error.saleInsert") + "<br/>");
					}
				}else{
					importTargetDao.updateTarget(target);
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
	public String readOffice(File file) throws IOException {
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
			
			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 3; i <= sheet.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				List<Object> linked = new LinkedList<Object>();
				List<SaleTarget> test = new LinkedList<SaleTarget>();
					HashMap<String, Object> modelMap = new HashMap<String, Object>();
					// 国家
					// 国家
					String officeName = row.getCell(0).getStringCellValue();
					String officeId = importTargetDao.selectReg(officeName);
					
					if (officeId == null) {
						msg.append(getText("target.error.row") + (i + 1)
								+ getText("target.error.office") +" ("+officeName+")"+"<br/>");
					}
					modelMap.put("Office", officeId == null ? null
							: officeId);
					
					
					if(row.getCell(1)!=null 
							&& row.getCell(1)
							.getNumericCellValue()!=0){
						modelMap.put("Qty", (int) row.getCell(1)
								.getNumericCellValue());
					}
					
					if(row.getCell(2)!=null 
							&&  row.getCell(2)
							.getNumericCellValue()!=0){
						modelMap.put("Amt", row.getCell(2)
								.getNumericCellValue());
					}
					
					if( row.getCell(3)!=null 
							&&  row.getCell(3)
							.getNumericCellValue()!=0){
						modelMap.put("tz_qty", (int) row.getCell(3)
								.getNumericCellValue());
					}
					
					
					if(row.getCell(4)!=null  && row.getCell(4)
							.getNumericCellValue()!=0 ){
						modelMap.put("tz_amt", (int) row.getCell(4)
								.getNumericCellValue());
					}
					

					
					allModelList.add(modelMap);
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
				Target target = new Target();
				String office = allModelList.get(i).get("Office")
						.toString();
				target.setTargetId(office);
				BigDecimal bd =null;
				int row = 0;
				if (allModelList.get(i).get("Qty") != null
						&& Integer.parseInt(allModelList.get(i).get("Qty")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("Qty").toString()); 
				
					int qty=bd.intValue();
					target.setQuantity(bd.longValue());
					
				}
				if (allModelList.get(i).get("Amt") != null
						&& Double.parseDouble(allModelList.get(i).get("Amt")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("Amt").toString()); 
				
					target.setAmount(bd.toPlainString());
					
				}
				if (allModelList.get(i).get("tz_qty") != null
						&& Double.parseDouble(allModelList.get(i).get("tz_qty")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("tz_qty").toString()); 
				
					target.setTzQuantity(bd.longValue());
					
				}
				
				if (allModelList.get(i).get("tz_amt") != null
						&& Integer.parseInt(allModelList.get(i).get("tz_amt")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("tz_amt").toString()); 
				
					target.setTzAmount(bd.toPlainString());
					
				}
				target.setClassId(9);
				int  r=importTargetDao.selectCount("9",office);
				if(r==0){
					row = importTargetDao.saveChannelTarget(target);
					if (row == 0) {
						msg.append(":"
								+ getText("target.error.saleInsert") + "<br/>");
					}
				}else{
					importTargetDao.updateTarget(target);
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
	public String readReg(File file) throws IOException {
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
			
			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 3; i <= sheet.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				List<Object> linked = new LinkedList<Object>();
				List<SaleTarget> test = new LinkedList<SaleTarget>();
					HashMap<String, Object> modelMap = new HashMap<String, Object>();
					// 国家
					// 国家
					String regName = row.getCell(0).getStringCellValue();
					String regId = importTargetDao.selectReg(regName);
					
					if (regId == null) {
						msg.append(getText("target.error.row") + (i + 1)
								+ getText("target.error.reg") +" ("+regName+")"+"<br/>");
					}
					modelMap.put("Reg", regId == null ? null
							: regId);
					
					
					if(row.getCell(1)!=null 
							&& row.getCell(1)
							.getNumericCellValue()!=0){
						modelMap.put("Qty", (int) row.getCell(1)
								.getNumericCellValue());
					}
					
					if(row.getCell(2)!=null 
							&&  row.getCell(2)
							.getNumericCellValue()!=0){
						modelMap.put("Amt", row.getCell(2)
								.getNumericCellValue());
					}
					
					if( row.getCell(3)!=null 
							&&  row.getCell(3)
							.getNumericCellValue()!=0){
						modelMap.put("tz_qty", (int) row.getCell(3)
								.getNumericCellValue());
					}
					
					
					if(row.getCell(4)!=null  && row.getCell(4)
							.getNumericCellValue()!=0 ){
						modelMap.put("tz_amt", (int) row.getCell(4)
								.getNumericCellValue());
					}
					

					
					allModelList.add(modelMap);
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
				Target target = new Target();
				String reg = allModelList.get(i).get("Reg")
						.toString();
				target.setTargetId(reg);
				BigDecimal bd =null;
				int row = 0;
				if (allModelList.get(i).get("Qty") != null
						&& Integer.parseInt(allModelList.get(i).get("Qty")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("Qty").toString()); 
				
					int qty=bd.intValue();
					target.setQuantity(bd.longValue());
					
				}
				if (allModelList.get(i).get("Amt") != null
						&& Double.parseDouble(allModelList.get(i).get("Amt")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("Amt").toString()); 
				
					target.setAmount(bd.toPlainString());
					
				}
				if (allModelList.get(i).get("tz_qty") != null
						&& Double.parseDouble(allModelList.get(i).get("tz_qty")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("tz_qty").toString()); 
				
					target.setTzQuantity(bd.longValue());
					
				}
				
				if (allModelList.get(i).get("tz_amt") != null
						&& Integer.parseInt(allModelList.get(i).get("tz_amt")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("tz_amt").toString()); 
				
					target.setTzAmount(bd.toPlainString());
					
				}
				target.setClassId(2);
				int  r=importTargetDao.selectCount("2",reg);
				if(r==0){
					row = importTargetDao.saveChannelTarget(target);
					if (row == 0) {
						msg.append(":"
								+ getText("target.error.saleInsert") + "<br/>");
					}
				}else{
					importTargetDao.updateTarget(target);
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
	public String readBranch(File file) throws IOException {
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
			
			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 3; i <= sheet.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				List<Object> linked = new LinkedList<Object>();
				List<SaleTarget> test = new LinkedList<SaleTarget>();
					HashMap<String, Object> modelMap = new HashMap<String, Object>();
					// 国家
					// 国家
					String	branchName = row.getCell(0).getStringCellValue();
					String branchId = importTargetDao.selectReg(branchName);
					
					if (branchId == null) {
						msg.append(getText("target.error.row") + (i + 1)
								+ getText("target.error.branch")+" ("+branchName+")" +"<br/>");
					}
					modelMap.put("Branch", branchId == null ? null
							: branchId);
					
					
					if(row.getCell(1)!=null 
							&& row.getCell(1)
							.getNumericCellValue()!=0){
						modelMap.put("Qty", (int) row.getCell(1)
								.getNumericCellValue());
					}
					
					if(row.getCell(2)!=null 
							&&  row.getCell(2)
							.getNumericCellValue()!=0){
						modelMap.put("Amt", row.getCell(2)
								.getNumericCellValue());
					}
					
					if( row.getCell(3)!=null 
							&&  row.getCell(3)
							.getNumericCellValue()!=0){
						modelMap.put("tz_qty", (int) row.getCell(3)
								.getNumericCellValue());
					}
					
					
					if(row.getCell(4)!=null  && row.getCell(4)
							.getNumericCellValue()!=0 ){
						modelMap.put("tz_amt", (int) row.getCell(4)
								.getNumericCellValue());
					}
					

					
					allModelList.add(modelMap);
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
				Target target = new Target();
				String branch = allModelList.get(i).get("Branch")
						.toString();
				target.setTargetId(branch);
				BigDecimal bd =null;
				int row = 0;
				if (allModelList.get(i).get("Qty") != null
						&& Integer.parseInt(allModelList.get(i).get("Qty")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("Qty").toString()); 
				
					int qty=bd.intValue();
					target.setQuantity(bd.longValue());
					
				}
				if (allModelList.get(i).get("Amt") != null
						&& Double.parseDouble(allModelList.get(i).get("Amt")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("Amt").toString()); 
				
					target.setAmount(bd.toPlainString());
					
				}
				if (allModelList.get(i).get("tz_qty") != null
						&& Double.parseDouble(allModelList.get(i).get("tz_qty")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("tz_qty").toString()); 
				
					target.setTzQuantity(bd.longValue());
					
				}
				
				if (allModelList.get(i).get("tz_amt") != null
						&& Integer.parseInt(allModelList.get(i).get("tz_amt")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("tz_amt").toString()); 
				
					target.setTzAmount(bd.toPlainString());
					
				}
				target.setClassId(1);
				int  r=importTargetDao.selectCount("1",branch);
				if(r==0){
					row = importTargetDao.saveChannelTarget(target);
					if (row == 0) {
						msg.append(":"
								+ getText("target.error.saleInsert") + "<br/>");
					}
				}else{
					importTargetDao.updateTarget(target);
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
	public String readRole(File file,String role) throws IOException {
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
			
			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			for (int i = 3; i <= sheet.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				List<Object> linked = new LinkedList<Object>();
				List<SaleTarget> test = new LinkedList<SaleTarget>();
					HashMap<String, Object> modelMap = new HashMap<String, Object>();
					// 国家
					String	userName = row.getCell(0).getStringCellValue();
					String userId = importTargetDao.selectUser(userName,"%"+role+"%");
					String user="";
			
					if(role.equals("PROM")){
						user="target.error.prom";
					}else if(role.equals("SALES")){
						user="target.error.salesMan";
					}else if(role.equals("REGIONAL")){
						user="target.error.bm";
					}else if(role.equals("SUPERVISOR")){
						user="target.error.sup";
					}
					
					if (userId == null) {
						msg.append(getText("target.error.row") + (i + 1)
								+ getText(user) +"("+userName+")"+"<br/>");
					}
					modelMap.put("User", userId == null ? null
							: userId);
					
					
					if(row.getCell(1)!=null 
							&& row.getCell(1)
							.getNumericCellValue()!=0){
						modelMap.put("Qty", (int) row.getCell(1)
								.getNumericCellValue());
					}
					
					if(row.getCell(2)!=null 
							&&  row.getCell(2)
							.getNumericCellValue()!=0){
						modelMap.put("Amt", row.getCell(2)
								.getNumericCellValue());
					}
					
					if( row.getCell(3)!=null 
							&&  row.getCell(3)
							.getNumericCellValue()!=0){
						modelMap.put("tz_qty", (int) row.getCell(3)
								.getNumericCellValue());
					}
					
					
					if(row.getCell(4)!=null  && row.getCell(4)
							.getNumericCellValue()!=0 ){
						modelMap.put("tz_amt", (int) row.getCell(4)
								.getNumericCellValue());
					}
					

					
					allModelList.add(modelMap);
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
				Target target = new Target();
				String user = allModelList.get(i).get("User")
						.toString();
				target.setTargetId(user);
				BigDecimal bd =null;
				int row = 0;
				if (allModelList.get(i).get("Qty") != null
						&& Integer.parseInt(allModelList.get(i).get("Qty")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("Qty").toString()); 
				
					int qty=bd.intValue();
					target.setQuantity(bd.longValue());
					
				}
				if (allModelList.get(i).get("Amt") != null
						&& Double.parseDouble(allModelList.get(i).get("Amt")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("Amt").toString()); 
				
					target.setAmount(bd.toPlainString());
					
				}
				if (allModelList.get(i).get("tz_qty") != null
						&& Double.parseDouble(allModelList.get(i).get("tz_qty")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("tz_qty").toString()); 
				
					target.setTzQuantity(bd.longValue());
					
				}
				
				if (allModelList.get(i).get("tz_amt") != null
						&& Integer.parseInt(allModelList.get(i).get("tz_amt")
								.toString()) != 0
						) {
					bd = new BigDecimal(allModelList.get(i)
							.get("tz_amt").toString()); 
				
					target.setTzAmount(bd.toPlainString());
					
				}
				target.setClassId(5);
				int  r=importTargetDao.selectCount("5",user);
				if(r==0){
					row = importTargetDao.saveChannelTarget(target);
					if (row == 0) {
						msg.append(":"
								+ getText("target.error.saleInsert") + "<br/>");
					}
				}else{
					importTargetDao.updateTarget(target);
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
	public String readExcel(File file, String fileName,String name) throws IOException {
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		 if ("xlsx".equals(extension)) {
			 if(name.equals("channel")){
				 System.out.println("channel");
				 return readChannel(file);
			 }else  if(name.equals("reg")){
				 System.out.println("reg");
				 return readReg(file);
			 }else  if(name.equals("office")){
				 System.out.println("office");
				 return readOffice(file);
			 }else if(name.equals("branch")){
				 System.out.println("branch");
				 return readBranch(file);
			 }
			 
			 return readRole(file,name);
			 
		} else {
			throw new IOException("不支持的文件类型");
		}
	}



	@Override
	public List<Customer> selectCustomer(String partyId) throws Exception {
		return importTargetDao.selectCustomer(partyId);
	}



	@Override
	public List<Shop> selectShop(String partyId) throws Exception {
		return importTargetDao.selectShop(partyId);
	}



	@Override
	public List<Shop> selectSale(Map<String,Object> map) throws Exception {
		return importTargetDao.selectSale(map);
	}



	@Override
	public List<Shop> selectManager(String partyId) throws Exception {		
		return importTargetDao.selectManager(partyId);
	}



	@Override
	public List<Shop> selectProduct(String partyId) throws Exception {
		return importTargetDao.selectProduct(partyId);
	}



	@Override
	public List<Target> chooseRegion(String partyId) throws Exception {
		
		return importTargetDao.chooseRegion(partyId);
	}



	@Override
	public List<Target> chooseOffice(String partyId) throws Exception {
		
		return importTargetDao.chooseOffice(partyId);
	}


	

}
