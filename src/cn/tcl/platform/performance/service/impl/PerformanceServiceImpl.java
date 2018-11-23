package cn.tcl.platform.performance.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.tcl.common.BaseAction;
import cn.tcl.common.WebPageUtil;
import cn.tcl.platform.BDExcel.dao.IBDExcelDao;
import cn.tcl.platform.BDExcel.service.IBDExcelService;
import cn.tcl.platform.BDExcel.dao.IBDExcelDao;
import cn.tcl.platform.BDExcel.vo.BDExcel;
import cn.tcl.platform.BDExcel.vo.BDSCImportExcel;
import cn.tcl.platform.excel.actions.DateUtil;
import cn.tcl.platform.excel.service.IExcelService;
import cn.tcl.platform.excel.vo.Excel;
import cn.tcl.platform.performance.dao.IPerformanceDao;
import cn.tcl.platform.performance.service.IPerformanceService;
import cn.tcl.platform.performance.vo.Performance;
import cn.tcl.platform.sale.vo.Sale;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("performanceService")
public class PerformanceServiceImpl extends BaseAction implements IPerformanceService {
	@Autowired
	private IPerformanceDao performanceDao;

	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat sdw = new SimpleDateFormat("E");
	static SimpleDateFormat formatEn = new SimpleDateFormat("MMMM.dd,yyyy", Locale.ENGLISH);

	static SimpleDateFormat mm = new SimpleDateFormat("MMMM", Locale.ENGLISH);
	static SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.ENGLISH);

	/**
	 * 将字符串放入字符串数组
	 * 
	 * @param arr
	 *            字符串数组
	 * @param str
	 *            字符串
	 * @return
	 */
	private static String[] insert(String[] arr, String str) {
		int size = arr.length;
		String[] tmp = new String[size + 1];
		System.arraycopy(arr, 0, tmp, 0, size);
		tmp[size] = str;
		return tmp;
	}

	@Override
	public String readPCTarget(File file) throws IOException {

		System.out.println("=======read2007ExcelByPK========");
		String countrySale = "";
		StringBuffer msg = new StringBuffer();
		List<List<Object>> list = new LinkedList<List<Object>>();
		try {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一章表格内容
			XSSFSheet sheet = xwb.getSheetAt(0);
			Object value = null;
			XSSFRow ro = sheet.getRow(1);
			XSSFCell cells = null;

			String userCountry = WebPageUtil.getLoginedUser().getPartyId();

			List<HashMap<String, Object>> allModelList = new LinkedList<HashMap<String, Object>>();
			//	Dealer 、Store Name 、PC Name、Date、Qty、Amount   
			for (int i = 2; i <= sheet.getLastRowNum(); i++) {
				HashMap<String, Object> countryTargetMap = new HashMap<String, Object>();
				XSSFRow row = null;
				XSSFCell cell = null;
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				String customerName = "";
				String customerId = "";
			

				
				if (row.getCell(1) != null && row.getCell(1).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
				String 	shopName = getCellValueByCell(row.getCell(1));
				String	shopId = performanceDao.selectShopByName(userCountry, shopName);
					if (shopId == null || shopId.equals("")) {
						msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.shop") + "("
								+ getCellValueByCell(row.getCell(1)) + ")" + "<br/>");
					} else {
						countryTargetMap.put("Shop", shopId);
						
						if (row.getCell(2) != null && row.getCell(2).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
							String 	pcName = getCellValueByCell(row.getCell(2));
							String	pcId = performanceDao.selectPCByShop(shopId, pcName);
								if (pcId == null || pcId.equals("")) {
									msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.pc") + "("
											+ getCellValueByCell(row.getCell(2)) + ")" + "<br/>");
								} else {
									countryTargetMap.put("PC", pcId);
								}

							} else {
									msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.line")
											+ DateUtil.getExcelColumnLabel(3) + " " + getText("excel.error.null") + "<br/>");

							}
						
						
						if (row.getCell(0) != null && row.getCell(0).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
							customerName = getCellValueByCell(row.getCell(0));
							customerId = performanceDao.selectCustomerByName(userCountry, customerName);
							if (customerId == null || customerId.equals("")) {
								msg.append(getText("excel.error.row") + (i + 1) + getText("excel.error.customer") + "("
										+ getCellValueByCell(row.getCell(0)) + ")" + "<br/>");
							} else {
								countryTargetMap.put("Customer", customerId);
							}

						} else {
							if ((row.getCell(1) != null && row.getCell(1).getCellType() != HSSFCell.CELL_TYPE_BLANK)) {
								msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.line")
										+ DateUtil.getExcelColumnLabel(1) + " " + getText("excel.error.null") + "<br/>");

							}
						}
							
					}

				} else {
					if ((row.getCell(2) != null && row.getCell(2).getCellType() != HSSFCell.CELL_TYPE_BLANK)) {
						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.line")
								+ DateUtil.getExcelColumnLabel(2) + " " + getText("excel.error.null") + "<br/>");

					}
				}
				
				
				
			
				
				
				SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM");// 设置日期格式
				SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
				Date date = new Date();
				String dt = dfd.format(date);
				Date dt1 = dfd.parse(dt);
				Date dt2;
				String rowDate = "";
				Float rowExchange = (float) 0;

				if (row.getCell(1) != null && row.getCell(1).getCellType() != HSSFCell.CELL_TYPE_BLANK) {

					if (row.getCell(3) != null && row.getCell(3).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
						try {
							format.setLenient(false);
							date = format.parse(getCellValueByCell(row.getCell(3)));// 有异常要捕获
							dfd.setLenient(false);
							String newD = dfd.format(date);
							date = dfd.parse(newD);// 有异常要捕获
							dt2 = dfd.parse(newD);
							rowDate = newD;
							countryTargetMap.put("DataDate", rowDate+"-01");
						} catch (Exception e) {
							msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
									+ DateUtil.getExcelColumnLabel(4) + " " + getText("excel.error.date") + "<br/>");
						}

					} else {
						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
								+ DateUtil.getExcelColumnLabel(4) + " " + getText("excel.error.dateNo") + "<br/>");
					}

				}

				

				if (row.getCell(1) != null && row.getCell(1).getCellType() != HSSFCell.CELL_TYPE_BLANK
						&& row.getCell(2) != null && row.getCell(2).getCellType() != HSSFCell.CELL_TYPE_BLANK
						&& row.getCell(4) != null && row.getCell(4).getCellType() != HSSFCell.CELL_TYPE_BLANK) {

					switch (row.getCell(4).getCellType()) {

					case HSSFCell.CELL_TYPE_STRING:
						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
								+ DateUtil.getExcelColumnLabel(5) + " " + getText("excel.error.num") + "<br/>");
						break;

					case HSSFCell.CELL_TYPE_FORMULA:

						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
								+ DateUtil.getExcelColumnLabel(5) + " " + getText("excel.error.num") + "<br/>");

						break;

					case HSSFCell.CELL_TYPE_NUMERIC:

						if (row.getCell(4).getNumericCellValue() != 0) {
							countryTargetMap.put("targetQty", (int) row.getCell(4).getNumericCellValue());
						}

						break;

					case HSSFCell.CELL_TYPE_ERROR:

						break;

					}

				}
				
				if (row.getCell(2) != null && row.getCell(2).getCellType() != HSSFCell.CELL_TYPE_BLANK
						&& row.getCell(1) != null && row.getCell(1).getCellType() != HSSFCell.CELL_TYPE_BLANK
						&& row.getCell(5) != null && row.getCell(5).getCellType() != HSSFCell.CELL_TYPE_BLANK) {

					switch (row.getCell(5).getCellType()) {

					case HSSFCell.CELL_TYPE_STRING:
						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
								+ DateUtil.getExcelColumnLabel(6) + " " + getText("excel.error.num") + "<br/>");
						break;

					case HSSFCell.CELL_TYPE_FORMULA:

						msg.append(getText("excel.error.row") + (i + 1) + " " + getText("excel.error.cell")
								+ DateUtil.getExcelColumnLabel(6) + " " + getText("excel.error.num") + "<br/>");

						break;

					case HSSFCell.CELL_TYPE_NUMERIC:

						if (row.getCell(5).getNumericCellValue() != 0) {
							countryTargetMap.put("targetAmt", (int) row.getCell(5).getNumericCellValue());
							

							String []days=rowDate.split("-");
							String begin=days[0]+"-"+days[1]+"-01";
							String end=days[0]+"-"+days[1]+"-31";
							Float exchange=performanceDao.selectExchange(userCountry, begin, end,rowDate);
							if(exchange==null){
								msg.append( getText("excel.error.exchange")+" "+days[1]+"/"+days[0]+ "<br/>");
							}else{
								countryTargetMap.put("exchange", exchange);
							}
						}

						break;

					case HSSFCell.CELL_TYPE_ERROR:

						break;

					}

				}


				List<Object> linked = new LinkedList<Object>();
			
				allModelList.add(countryTargetMap);
				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
					cell = row.getCell(j);
					if (cell == null) {
						continue;
					}
					DecimalFormat df = new DecimalFormat("0");// 格式化 number
																// String 字符
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
					DecimalFormat nf = new DecimalFormat("0");// 格式化数字
					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						if ("@".equals(cell.getCellStyle().getDataFormatString())) {
							value = df.format(cell.getNumericCellValue());
						} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
							value = nf.format(cell.getNumericCellValue());
						} else {
							value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
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

			System.out.println("=====size=============" + allModelList.size());
			System.out.println("=====allModelList=============" + allModelList);

			List<Performance> PCTargetList = new ArrayList<Performance>();

			List<Performance> deletePCTargetList = new ArrayList<Performance>();

			if (msg.length() <= 0) {
				for (int i = 0; i < allModelList.size(); i++) {
					if (

					allModelList.get(i).get("Customer") != null && allModelList.get(i).get("Customer") != ""
							&& allModelList.get(i).get("Shop") != null && allModelList.get(i).get("Shop") != ""
							&& allModelList.get(i).get("DataDate") != null && allModelList.get(i).get("DataDate") != ""
							&& allModelList.get(i).get("PC") != null && allModelList.get(i).get("PC") != ""
							&& allModelList.get(i).get("targetQty") != null && allModelList.get(i).get("targetQty") != ""
							) {
						Performance excel = new Performance();
						excel.setShopId(Integer.parseInt(allModelList.get(i).get("Shop").toString()));
						excel.setCountryId(userCountry);
						excel.setDataDate(allModelList.get(i).get("DataDate").toString());
						excel.setType(1);
						excel.setTargetId(allModelList.get(i).get("PC").toString());
						excel.setTargetQty(Integer.parseInt(allModelList.get(i).get("targetQty").toString()));
						if(allModelList.get(i).get("targetAmt")!=null
								&& !allModelList.get(i).get("targetAmt").toString().equals("")) {
							excel.setTargetAmt(allModelList.get(i).get("targetAmt").toString());
							Float 	exchange=Float.parseFloat(allModelList.get(i).get("exchange").toString());
							Float targetAmt=Float.parseFloat(allModelList.get(i).get("targetAmt").toString());
							Float h_targetAmt=exchange*targetAmt;
							excel.setH_targetAmt(Math.round(h_targetAmt)+"");
						}
					
						excel.setClassId(10);
						
						deletePCTargetList.add(excel);
						PCTargetList.add(excel);
					}
					// 判断该机型该门店是否存在，存在的话就修改，不存在就插入
				}
				
			}
			if (deletePCTargetList.size() > 0) {
				int row = performanceDao.deletePCTarget(deletePCTargetList);
				performanceDao.savePCTarget(PCTargetList);
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
	public String readExcelByTarget(File file, String fileName, String what) throws IOException {
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		} else if ("xlsx".equals(extension)) {
			return readPCTarget(file);
		} else {
			throw new IOException("Unsupported file type,the suffix name should be xlsx!");
		}
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

	@Override
	public JSONObject selectBDSCTarger(String beginDate, String endDate, String searchStr, String conditions,String type) {
		List<HashMap<String, Object>> targetList = performanceDao.selectBDSCTarget(beginDate, endDate, searchStr, conditions,type);
		HashMap<String, ArrayList<HashMap<String, Object>>> lineMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();

		JSONObject jsonObject = new JSONObject();
		JSONArray countryArray = new JSONArray();
		JSONArray lineArray = new JSONArray();
		JSONArray dataArray = new JSONArray();
		for (int m = 0; m < targetList.size(); m++) {

			if (targetList.get(m).get("class_id").toString().equals("6")) {
				JSONObject dataObject = new JSONObject();
				dataObject.accumulate("country", targetList.get(m).get("country_name"));
				dataObject.accumulate("type", targetList.get(m).get("TYPE"));
				dataObject.accumulate("date", targetList.get(m).get("date"));
				dataObject.accumulate("classId", targetList.get(m).get("class_id"));

				dataObject.accumulate("line", targetList.get(m).get("line"));
				dataObject.accumulate("targetQty", targetList.get(m).get("targetQty"));
				dataObject.accumulate("tz_targetQty", targetList.get(m).get("tz_targetQty"));
				dataArray.add(dataObject);
			}

			if (targetList.get(m).get("class_id").toString().equals("1")) {
				JSONObject jsonCountry = new JSONObject();
				jsonCountry.accumulate("country", targetList.get(m).get("country_name"));
				jsonCountry.accumulate("type", targetList.get(m).get("TYPE"));
				jsonCountry.accumulate("date", targetList.get(m).get("date"));
				jsonCountry.accumulate("targetQty", targetList.get(m).get("targetQty"));
				jsonCountry.accumulate("tz_targetQty", targetList.get(m).get("tz_targetQty"));
				if (!countryArray.contains(jsonCountry)) {
					countryArray.add(jsonCountry);
				}
			}

			if (targetList.get(m).get("class_id").toString().equals("6")) {
				JSONObject jsonline = new JSONObject();
				jsonline.put("line", targetList.get(m).get("line"));

				if (!lineArray.contains(jsonline)) {
					lineArray.add(jsonline);
				}
			}

		}

		JSONArray array = new JSONArray();
		jsonObject.put("line", JSONArray.fromObject(lineArray));
		jsonObject.put("country", JSONArray.fromObject(countryArray));
		jsonObject.put("data", JSONArray.fromObject(dataArray));
		return jsonObject;
	}

	@Override
	public Map<String, Object> selectPCTarget(int start, int limit, String searchStr, String order, String sort,
			String conditions) throws Exception {
		List<HashMap<String, Object>> list=performanceDao.selectPCTarget(start, limit, order, sort, searchStr,conditions);
		Map<String, Object> map=new HashMap<String, Object>();
		int count=performanceDao.selectPCTargetCount(searchStr,conditions);
		map.put("rows", list);
		map.put("total", count);
		return map;
	}

}
